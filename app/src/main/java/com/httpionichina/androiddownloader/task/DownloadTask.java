package com.httpionichina.androiddownloader.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.httpionichina.androiddownloader.db.dao.ThreadDAO;
import com.httpionichina.androiddownloader.db.imple.ThreadDAOImple;
import com.httpionichina.androiddownloader.entity.FileInfo;
import com.httpionichina.androiddownloader.entity.ThreadInfo;
import com.httpionichina.androiddownloader.services.DownloadServices;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * 下载任务
 * Created by Martin on 2015/7/17.
 */
public class DownloadTask {

    private final String TAG = "DownloadTask";

    public static final String FINISHED_SIZE = "FINISHED_SIZE";

    private Context mContext = null;

    private FileInfo mFileInfo = null;

    private ThreadDAO mThreadDAO = null;

    //下载的状态
    public boolean isPause = false;

    //下载线程完成的进度
    private int mFinishedSize = 0;

    public DownloadTask(Context mContext, FileInfo mFileInfo) {
        this.mContext = mContext;
        this.mFileInfo = mFileInfo;
        mThreadDAO = new ThreadDAOImple(mContext);
    }

    public void downloa() {
        //读取本地已经存在的线程信息
        List<ThreadInfo> infoList = mThreadDAO.getThreads(mFileInfo.getFileUrl());
        ThreadInfo threadInfo = null;
        if (infoList.size() == 0) {//如果本地不存在线程信息，那么就初始化一个
            threadInfo = new ThreadInfo(0, mFileInfo.getFileUrl(), 0, mFileInfo.getFileSize(), 0);
        } else {
            threadInfo = infoList.get(0);
        }
        //创建子线程进行下载
        new DownloadThread(threadInfo).start();
    }

    //下载线程
    class DownloadThread extends Thread {
        ThreadInfo mThreadInfo = null;

        public DownloadThread(ThreadInfo mThreadInfo) {
            this.mThreadInfo = mThreadInfo;
        }

        @Override
        public void run() {
            //向数据库插入线程数据信息
            //查看数据库是否存在这个线程信息
            if (!mThreadDAO.isExist(mThreadInfo.getFileUrl(), mThreadInfo.getId())) {
                //不存在即插入数据
                mThreadDAO.insertThread(mThreadInfo);
            }
            //进行网络连接
            HttpURLConnection connection = null;
            InputStream input = null;
            RandomAccessFile accessFile = null;
            try {
                URL url = new URL(mThreadInfo.getFileUrl());
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(3000);
                connection.setRequestMethod("GET");
                //设置下载位置
                //得到线程的开始加上已经完成的进度从而获取要设置的连接开始的位置
                int start = mThreadInfo.getStart() + mThreadInfo.getFinishedSize();
                connection.setRequestProperty("Range", "bytes=" + start + "-" + mThreadInfo.getEnd());

                //设置文件的写入位置
                File file = new File(DownloadServices.FILE_SAVE_PATH, mFileInfo.getFileName());
                accessFile = new RandomAccessFile(file, "rwd");
                //设置写入位置，在读写的时候跳过设置的数值，从下一位开始读写
                accessFile.seek(start);

                //广播回传的进度意图
                Intent finishedIntent = new Intent(DownloadServices.ACTION_UPDATE);

                //获取开始下载前已经完成的进度
                mFinishedSize += mThreadInfo.getFinishedSize();

                //开始下载
                if (connection.getResponseCode() == HttpStatus.SC_PARTIAL_CONTENT) {//连接成功

                    //读取数据
                    input = connection.getInputStream();
                    //定义缓冲区
                    byte[] bytes = new byte[1024 * 4];
                    //循环写入本地
                    int len = -1;
                    //定义广播发送的时间间隔，减少对UI的调用
                    long time = System.currentTimeMillis();
                    while ((len = input.read(bytes)) != -1) {
                        //写入文件
                        accessFile.write(bytes, 0, len);
                        //把下载进度发送广播给Activity
                        //将每次写入文件的时候的进度累加到完成进度中
                        mFinishedSize += len;
                        if (System.currentTimeMillis() - time > 500) {
                            Log.e(TAG, TAG + "完成进度。。。。" + mFinishedSize);
                            time = System.currentTimeMillis();
                            //将线程的下载进度传送到广播中,采用百分比的形式
                            finishedIntent.putExtra(FINISHED_SIZE, mFinishedSize * 100 / mFileInfo.getFileSize());
                            //发出广播
                            mContext.sendBroadcast(finishedIntent);
                        }
                        //在下载暂停时，保存下载进度
                        if (isPause) {
                            mThreadDAO.updateThread(
                                    mThreadInfo.getFileUrl(),
                                    mThreadInfo.getId(),
                                    mFinishedSize);
                            //跳出循环
                            return;
                        }
                    }
                    //下载完成
                    //删除线程信息
                    mThreadDAO.deleteThread(mThreadInfo.getFileUrl(), mThreadInfo.getId());
                }


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //释放资源
                try {
                    connection.disconnect();
                    input.close();
                    accessFile.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
