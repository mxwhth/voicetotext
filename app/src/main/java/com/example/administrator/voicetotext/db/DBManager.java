package com.example.administrator.voicetotext.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.administrator.voicetotext.entity.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/4.
 */

public class DBManager implements Serializable{
    private SQLiteService service;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        Log.d("Database_TAG", "DBManager --> Constructor");
        service = new SQLiteService(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = service.getWritableDatabase();
    }

    //添加文档
    public void add(Document documents) {
        Log.d("Database_TAG", "DBManager --> add");
        try {
            db.beginTransaction(); // 开始事务
//            db.execSQL("insert into documentTable(_id,title,text,time) values(null,'title222','text222','time222');");
            db.execSQL("INSERT INTO " + service.TABLE_NAME + "(_id,title,text,time)" + " VALUES(null, ?, ?, ?)", new Object[]{documents.getTitle(), documents.getText(), documents.getTime()});
            Log.d("Database_TAG", "INSERT " + "Title: " + documents.getTitle() + " Text: " + documents.getText() + " Time: " + documents.getTime());
            db.setTransactionSuccessful(); // 设置事务成功完成
        } finally {
            db.endTransaction();// 结束事务
        }

    }

    //按时间查找文档
    public Document queryByTime(String time) {
        Log.d("Database_TAG", "DBManager --> queryByTime");
        Cursor cursor = db.rawQuery("select * from documentTable where time = ? ", new String[]{time});
        cursor.moveToLast();
        Document document = new Document(cursor.getInt(cursor.getColumnIndex("_id")), cursor.getString(cursor.getColumnIndex("title")), cursor.getString(cursor.getColumnIndex("text")), cursor.getString(cursor.getColumnIndex("time")));
        cursor.close();
        return document;
    }

    //查询全部文档
    public List<Document> queryAll() {
        ArrayList<Document> documents = new ArrayList<>();
        Log.d("Database_TAG", "DBManager --> queryAll");
        Cursor cursor = db.rawQuery("SELECT * FROM " + service.TABLE_NAME, null);
        while (cursor.moveToNext()){
            Document document = new Document();
            document.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            document.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            document.setText(cursor.getString(cursor.getColumnIndex("text")));
            document.setTime(cursor.getString(cursor.getColumnIndex("time")));
            documents.add(document);
        }
        cursor.close();
        return documents;
    }

    //删除文档
    public void delete(String time) {
        Log.d("Database_TAG", "DBManager --> delete");
        try {
            db.beginTransaction(); // 开始事务
            db.delete(service.TABLE_NAME, "time = ?", new String[]{time});
            Log.d("Database_TAG", "DELETE Document time = " + time);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction(); // 结束事务
        }
    }

    //更新文档
    public boolean update(Document document) {
        if (!document.getTime().equals("") && !document.getTitle().equals("")) {
            try {
                Log.d("Database_TAG", "DBManager --> update");
                ContentValues cv = new ContentValues();
                cv.put("title", document.getTitle());
                cv.put("text", document.getText());
                cv.put("time", document.getTime());
                db.beginTransaction(); // 开始事务
                db.update(service.TABLE_NAME, cv, "_id = ?", new String[]{String.valueOf(document.getId())});
                Log.d("Database_TAG", "UPDATE Document time = " + document.getTime());
                db.setTransactionSuccessful(); //设置事务成功
                return true;
            } finally {
                db.endTransaction(); //结束事务
            }
        }
        return false;
    }

    //查询全部文档，返回Cursor
    public Cursor queryAllForCursor(){
        Log.d("Database_TAG", "DBManager --> queryAllForCursor");
        Cursor cursor = db.rawQuery("SELECT * FROM " + service.TABLE_NAME, null);
        return cursor;
    }

    // 生产不使用
    public void cleanAllTables() {
        try {
            Log.d("Database_TAG", "DBManager --> cleanAllTables");
            db.beginTransaction(); //开始事务
            db.delete(service.TABLE_NAME, "", null);
            db.setTransactionSuccessful(); //设置事务成功
        } finally {
            db.endTransaction(); //结束事务
        }
    }

    //关闭数据库
    public void closeDB() {
        Log.d("Database_TAG", "DBManager --> closeDB");
        // 释放数据库资源
        db.close();
    }

}

