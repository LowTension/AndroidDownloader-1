package com.httpionichina.androiddownloader.services;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.httpionichina.androiddownloader.entity.FileInfo;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 行径文件下载的服务
 * Created by justo on 2015/7/16.
 */
public class DownloadServices extends Service {
    private final String TAG ="DownloadServices";

    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String FILE_INFO = "FILE_INFO";

    //标识初始化
    public static  final int MSG_INIT=0;

    //文件下载路径
    //TODO 这里写成了固定的，后面改为可扩展的，而且需要判断SD卡是否存在，
    public static final String FILE_SAVE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/download/";


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ACTION_START.equals(intent.getAction())) {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra(FILE_INFO);
            Log.i(TAG, "fileInfo..Start" + fileInfo.toString());
            //启动初始化线程：
            InitThread initThread=new InitThread(fileInfo);
            initThread.start();
        } else {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra(FILE_INFO);
            Log.i(TAG, "fileInfo..Stop" + fileInfo.toString());
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_INIT:
                    //从MSG中得到传输的数据
                    FileInfo fileInfo=(FileInfo)msg.obj;
                    Log.i(TAG,"init:"+fileInfo.toString());
                    break;
            }
        }
    };


    //初始化线程
    class InitThread extends Thread{
        private FileInfo mFileInfo;
        public InitThread(FileInfo fileInfo) {
            this.mFileInfo=fileInfo;
        }

        @Override
        public void run() {
            //网络连接对象
            HttpURLConnection connection=null;
            RandomAccessFile accessFile=null;
            try {
                //连接网络文件
                URL url=new URL(mFileInfo.getFileUrl());
                connection=(HttpURLConnection)url.openConnection();
                //一般除了下载文件使用GET请求以外别的请求都采用POST请求
                connection.setRequestMethod("GET");
                //设置请求3秒未响应超时
                connection.setConnectTimeout(3000);
                //定义请求到的文件的长度
                int fileSize=-1;
                //判断请求的返回码
                if(connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                    //获取网络文件长度
                    fileSize=connection.getContentLength();
                }
                //如果获取到的文件大小小于0说明网络请求有问题，直接跳过
                if (fileSize<0){
                    return;
                }
                //判断下载路径是否存在，
                File dir=new File(FILE_SAVE_PATH);
                if (!dir.exists()){
                    dir.mkdir();
                }
                //在本地创建文件
                File file=new File(dir,mFileInfo.getFileName());
                //随机文件访问对象，是一个特殊的输出流，可以在文件的任意位置进行写入操作
                accessFile=new RandomAccessFile(file,"rwd");
                //设置文件的大小
                accessFile.setLength(fileSize);
                //需要将长度返回，给services，使用handle进行交互
                mFileInfo.setFileSize(fileSize);
                //创建出MSG对象，并发送到handler
                handler.obtainMessage(MSG_INIT,mFileInfo).sendToTarget();
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                try {
                    //断开网络连接
                    connection.disconnect();
                    //关闭输出流
                    accessFile.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}