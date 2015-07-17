package com.httpionichina.androiddownloader.db.imple;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.httpionichina.androiddownloader.db.DBHelper;
import com.httpionichina.androiddownloader.db.dao.ThreadDAO;
import com.httpionichina.androiddownloader.entity.ThreadInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin on 2015/7/17.
 */
public class ThreadDAOImple implements ThreadDAO {

    //数据库操作类
    private DBHelper mDbHelper = null;


    public ThreadDAOImple(Context context) {
        mDbHelper = new DBHelper(context);
    }


    @Override
    public void insertThread(ThreadInfo threadInfo) {
        //获取SQLite数据库写入的对象
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String sql_insert = "insert into thread_info(thread_id,fileUrl,start,end,finishedSize) values (?,?,?,?,?)";
        db.execSQL(sql_insert, new Object[]{threadInfo.getId(), threadInfo.getFileUrl(),
                threadInfo.getStart(), threadInfo.getEnd(), threadInfo.getFinishedSize()});
        db.close();
    }

    @Override
    public void deleteThread(String url, int thread_id) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String sql_delete = "delete from thread_info where thread_id=? and fileUrl=?";
        db.execSQL(sql_delete, new Object[]{thread_id, url});
        db.close();
    }

    @Override
    public void updateThread(String url, int thread_id, int finished) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String sql_update = "update thread_info set finishedSize=?  where thread_id=? and fileUrl=?";
        db.execSQL(sql_update, new Object[]{finished, thread_id, url});
        db.close();
    }

    @Override
    public List<ThreadInfo> getThreads(String url) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        //定义要返回的数据集和
        List<ThreadInfo> list = new ArrayList<ThreadInfo>();
        String sql_query = "select * from thread_info where fileUrl=?";
        //获取插查询结果游标
        Cursor cursor = db.rawQuery(sql_query, new String[]{url});
        //进行游标的移动来获取下一个数据
        while (cursor.moveToNext()) {
            //进行循环的创建线程对象
            ThreadInfo threadInfo = new ThreadInfo();
            threadInfo.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
            threadInfo.setFileUrl(cursor.getString(cursor.getColumnIndex("fileUrl")));
            threadInfo.setStart(cursor.getInt(cursor.getColumnIndex("start")));
            threadInfo.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
            threadInfo.setFinishedSize(cursor.getInt(cursor.getColumnIndex("finishedSize")));
            //将创建的的县城信息对象添加到数据集
            list.add(threadInfo);
        }
        //关闭游标
        cursor.close();
        //关闭数据库连接
        db.close();

        return list;
    }

    @Override
    public boolean isExist(String url, int thread_id) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        //定义要返回的数据集和
        List<ThreadInfo> list = new ArrayList<ThreadInfo>();
        String sql_query = "select * from thread_info where fileUrl=? and thread_id=?";
        //获取插查询结果游标
        Cursor cursor = db.rawQuery(sql_query, new String[]{url, thread_id + ""});
        boolean exists = cursor.moveToNext();
        cursor.close();
        db.close();
        return exists;
    }
}