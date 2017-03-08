package com.example.administrator.voicetotext;

import com.example.administrator.voicetotext.util.ChineseSegmentationUtil;
import com.example.administrator.voicetotext.util.TimeFormatUtil;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    public static int[] A = new int[5];
    @Test
    public void addition_isCorrect() throws Exception {
        System.out.println("Hello".substring(0,7));
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test(){
        String s = "we are young ";
        String[] k = s.split(" ");
        for (int i =0 ;i<k.length;i++) {
            System.out.print(k[i]);
        }
        System.out.println(k.length);
    }

}