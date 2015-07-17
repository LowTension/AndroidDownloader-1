package com.httpionichina.androiddownloader.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 进行数据库的初始化操作
 * Created by Martin on 2015/7/17.
 */
public class DBHelper extends SQLiteOpenHelper {
    //数据库名称
    private static final String DB_NAME = "download.db";
    //数据库版本
    private static final int DB_VERSION = 1;
    //创建数据表的语法
    private static final String SQL_CREATE_THREAD_TABLE =
                "create table thread_info (_id integer primary key autoincrement," +
                    "thread_id integer," +
                        "fileUrl txt," +
                        "start integer," +
                        "end integer," +
                        "finishedSize integer) "; //创建数据表的语法
    //删除数据表的语法
    private static final String SQL_DROP_THREAD_TABLE = "drop table if exists thread_info";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_THREAD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_THREAD_TABLE);
        db.execSQL(SQL_CREATE_THREAD_TABLE);
    }
}
