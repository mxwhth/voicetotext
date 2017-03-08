package com.example.administrator.voicetotext.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/2/4.
 */

public class TimeFormatUtil {
    public static TimeFormatUtil instance;
    private SimpleDateFormat format;
    private TimeFormatUtil() {
        format = new SimpleDateFormat("yyyy/MM/dd EEaa hh:mm:ss");
    }
    public static TimeFormatUtil getInstance(){
        if (instance==null) {
            instance = new TimeFormatUtil();
        }
        return instance;
    }

    public String getFormatTime(){
        return format.format(new Date());
    }

}
