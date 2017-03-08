package com.example.administrator.voicetotext.entity;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/7.
 * 词典类
 */

public class Directory {
    private static Directory instance;
    private List<String> words;
    private AssetManager manager;

    //词典最长词长
    public static int MAX_LENGTH;

    //词典词条量
    public static int COUNT;

    //词典是否加载
    public static boolean isLoad = false;


    private static String filename = "test.txt";


    private Directory() {
    }

    public static Directory getInstance() {
        if (instance == null) {
            instance = new Directory();
        }
        return instance;
    }

    public List<String> getDirectory() {
        if (words == null) {
            words = new ArrayList<>();
        }
        return words;
    }

    //初始化词典
    //AssetManager 是为了获取asset中的txt文件,需要在activity中设置
    public void initDirectory(AssetManager manager) {
        if (!isLoad) {
            //词典未加载，进行加载

            BufferedReader reader = null;
            List<String> lines = new ArrayList<>();
            int max = 0;//当前词典最长词长度
            int count = 0;//当前词典词条数量
            try {
                Log.d("Directory", "初始化词典开始 ——> 读取 " + filename + " 文件");
                reader = new BufferedReader(new InputStreamReader(manager.open(filename)));
                String tempString = "";
                int line = 1;
                // 一次读入一行，直到读入null为文件结束
                while ((tempString = reader.readLine()) != null) {
                    // 显示行号
//                System.out.println("line " + line + ": " + tempString);
                    if (!"".equals(tempString.trim())) {
                        //读入不为空即添入词典
                        lines.add(tempString);
                        max = tempString.length() > max ? tempString.length() : max;
                        count++;
                    }
                    line++;
                }
                reader.close();
            } catch (IOException e) {
                Log.e("IO Error", e.getMessage());
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e("IO Error", e.getMessage());
                    }
                }
            }
            Log.d("Directory", "初始化词典结束");

            isLoad = true;
            words = lines;
            MAX_LENGTH = max;
            COUNT = count;
        }

    }

    //词典添加词
    public void addWord(String word) {
        if (!"".equals(word) && !words.contains(word)) {
            words.add(word);
        }
    }

    //词典删除词
    public boolean deleteWord(String word) {
        return words.remove(word);
    }
}
