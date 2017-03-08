package com.example.administrator.voicetotext.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/4.
 * 文档实体类
 */

public class Document implements Serializable{
    public int id;
    private String title;
    private String text;
    private String time;

    public Document(int id, String title, String text, String time) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.time = time;
    }

    public Document(String title, String text, String time) {
        this.title = title;
        this.text = text;
        this.time = time;
    }

    public Document() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "title: "+title+" text: "+text+" time: "+time;
    }
}
