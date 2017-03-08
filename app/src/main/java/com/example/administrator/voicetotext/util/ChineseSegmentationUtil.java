package com.example.administrator.voicetotext.util;

import android.content.res.AssetManager;
import android.util.Log;

import com.example.administrator.voicetotext.entity.Directory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/21.
 * 中文分词工具类
 */

public class ChineseSegmentationUtil {

    private static ChineseSegmentationUtil instance;

    //词典
    private static List<String> directory = new ArrayList<>();

    //词典最长词长
    private static int MAX_LENGTH;

    //词典词条量
    private static int COUNT;

    private ChineseSegmentationUtil() {
//        //初始化词典
//        BufferedReader reader = null;
//        List<String> lines = new ArrayList<>();
//        int max = 0;//当前词典最长词长度
//        int count = 0;//当前词典词条数量
//        try {
//            Log.d("ChineseSegmentationUtil", "初始化词典开始");
//            reader = new BufferedReader(new InputStreamReader(manager.open(filename)));
//            String tempString = null;
//            int line = 1;
//            // 一次读入一行，直到读入null为文件结束
//            while ((tempString = reader.readLine()) != null) {
//                // 显示行号
////                System.out.println("line " + line + ": " + tempString);
//                if (!"".equals(tempString.trim())) {
//                    //读入不为空即添入词典
//                    lines.add(tempString);
//                    max = tempString.length() > max ? tempString.length() : max;
//                    count++;
//                }
//                line++;
//            }
//            reader.close();
//        } catch (IOException e) {
//            Log.e("IO Error", e.getMessage());
//        } finally {
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (IOException e) {
//                    Log.e("IO Error", e.getMessage());
//                }
//            }
//        }

        directory = Directory.getInstance().getDirectory();
        MAX_LENGTH = Directory.MAX_LENGTH;
        COUNT = Directory.COUNT;
    }


    public static ChineseSegmentationUtil getInstance() {
        if (instance == null) {
            instance = new ChineseSegmentationUtil();
        }
        return instance;
    }


    //获取词典最长词长度
    public Integer getCOUNT() {
        return COUNT;
    }

    //获取词典词条数量
    public Integer getMaxLength() {
        return MAX_LENGTH;
    }

    //显示词典
    public void show() {
        for (String s : directory) {
            System.out.println(s);
        }
    }

    //得到分词 String
    public String getSegmentation(String str) {
        StringBuilder sb = new StringBuilder("");

        long times = System.currentTimeMillis();
        System.out.println("匹配开始");
        //执行分词算法
        sb.append(NMaxMatch(str));
        System.out.println("匹配结束");
        System.out.println("<---------------耗费时间："+(System.currentTimeMillis()-times)+"ms--------------->");

        return sb.toString();
    }

    //得到分词 List
    public List<String> getSegmentationList(String str) {
        List<String> list = new ArrayList<>();
        return list;
    }

    //正向最大匹配法
    private String ZMaxMatch(String str) {
        if (!"".equals(str.trim())) {
            //str 不为空
            StringBuilder sb = new StringBuilder("");

            while (str.length() > 0) {

                //子字符串是否有匹配项
                boolean isMatch = false;

                //从源字符串取出的待匹配子字符串
                String sb1;

                if (str.length() > MAX_LENGTH) {
                    sb1 = str.substring(0, MAX_LENGTH );
                } else {
                    sb1 = str;
                }

                //从子字符串拆分，用与字典匹配
                String sb2 = sb1;

                while (sb2.length() > 0) {
                    if (isContain(sb2)) {
                        isMatch = true;
                        Log.d("ChineseSegmentationUtil", sb2+"->匹配成功！");
                        System.out.println(sb2+"->匹配成功！");
                        //匹配到词条, 添加进StringBuilder
                        sb.append(sb2 + " ");
                        break;
                    } else {
                        //未匹配到词条, 减去末尾字符
                        sb2 = sb2.substring(0, sb2.length() - 1);
                    }
                }

                if (isMatch) {
                    //子字符串匹配中，原字符串左边减去匹配过的字符串
                    str = str.substring(sb2.length(), str.length());
                } else {
                    //子字符串未匹配中，原字符串左边减去一个字符
                    str = str.substring(1, str.length());
                }
            }

            //返回分词结果
            return sb.toString();

        }
        return "";
    }

    //逆向最大匹配法
    private String NMaxMatch(String str) {
        if (!"".equals(str.trim())) {
            //str 不为空
            StringBuilder sb = new StringBuilder("");

            while (str.length() > 0) {

                //子字符串是否有匹配项
                boolean isMatch = false;

                //从源字符串取出的待匹配子字符串
                String sb1;

                if (str.length() > MAX_LENGTH) {
                    sb1 = str.substring(str.length()-MAX_LENGTH, str.length() );
                } else {
                    sb1 = str;
                }

                //从子字符串拆分，用与字典匹配
                String sb2 = sb1;

                while (sb2.length() > 0) {
                    if (isContain(sb2)) {
                        isMatch = true;
                        //匹配到词条, 添加进StringBuilder
                        Log.d("ChineseSegmentationUtil", sb2+"->匹配成功！");
                        System.out.println(sb2+"->匹配成功！");
                        sb.append(sb2 + " ");
                        break;
                    } else {
                        //未匹配到词条, 减去末尾字符
                        sb2 = sb2.substring(1, sb2.length());
                    }
                }

                if (isMatch) {
                    //子字符串匹配中，原字符串左边减去匹配过的字符串
                    str = str.substring(0, str.length()-sb2.length());
                } else {
                    //子字符串未匹配中，原字符串右边减去一个字符
                    str = str.substring(0, str.length()-1);
                }
            }

            //返回分词结果
            return sb.toString();

        }
        return "";
    }

    //词典是否包含该词条
    private boolean isContain(String word) {
        if (directory.contains(word)) {
            return true;
        }
        return false;
    }
}
