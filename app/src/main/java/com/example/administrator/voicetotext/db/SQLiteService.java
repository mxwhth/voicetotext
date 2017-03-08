package com.example.administrator.voicetotext.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2017/2/4.
 * used for operate database
 */

public class SQLiteService extends SQLiteOpenHelper {

    // 数据库版本号
    public static final int DATABASE_VERSION = 1;
    // 数据库名
    public static final String DATABASE_NAME = "TextDB.db";
    // 数据表名,存储文档信息
    public static final String TABLE_NAME = "documentTable";
    //标题列
    public static final String TITLE_COLUMN = "title";
    //正文列
    public static final String TEXT_COLUMN = "text";
    //时间
    public static final String TIME_COLUMN = "time";


    public SQLiteService(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SQLiteService(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // 数据库实际被创建是在getWritableDatabase()或getReadableDatabase()方法调用时
        Log.d("Database_TAG", "SQLiteService Constructor");
        // CursorFactory设置为null,使用系统默认的工厂类
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Database_TAG", "SQLiteService onCreate");

        //create database
        StringBuilder sql = new StringBuilder("");
        /*
        BEGIN;
        CREATE TABLE [documentTable]
        ([_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
            [title] VARCHAR(20),
            [text] TEXT,
            [time] VARCHAR(20)) );
       */

        //建表
        sql.append("CREATE TABLE [" + TABLE_NAME + "] (")
                .append("[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ")
                .append("[" + TITLE_COLUMN + "] VARCHAR(20), ")
                .append("[" + TEXT_COLUMN + "] TEXT, ")
                .append("[" + TIME_COLUMN + "] VARCHAR(20)) ");

        db.execSQL(sql.toString());

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        Log.d("Database_TAG", "SQLiteService onOpen");

    }
}
