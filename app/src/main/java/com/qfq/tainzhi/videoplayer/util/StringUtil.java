package com.qfq.tainzhi.videoplayer.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by muqing on 2019/6/12.
 * Email: qfq61@qq.com
 */
public class StringUtil {
    public static int hour = 1000 * 60 * 60;
    public static int minute = 1000 * 60;
    public static int second = 1000;
    
    public static int kbyte = 1024;
    public static int mbyte = 1024 * 1024;
    public static int gbyte = 1024 * 1024 * 1024;
    
    public static String formatMediaTime(long millsec) {
        //"hh:mm:ss"
        //"mm:ss"
        int h = (int) millsec / hour;
        int m = (int) millsec % hour / minute;
        int sec = (int) millsec % minute / second;
        
        if (h > 0) {
            //"hh:mm:ss" "1:36:2"
            return String.format("%02d:%02d:%02d", h, m, sec);
        } else {
            return String.format("%02d:%02d", m, sec);
        }
    }
    
    public static String formatMediaSize(long size) {
        // 5B
        // 5KB
        // 5MB
        // 5GB
        int k = (int)size % kbyte;
        int m = (int)size / mbyte;
        int g = (int)size / gbyte;
        if (g > 0) {
            return g + "GB";
        } else if (m > 0) {
            return m + "MB";
        } else if (k > 0) {
            return  k + "KB";
        } else {
            return size + "B";
        }
    }
    
    public static String formatDate(String date) {
        if (date == null) {
            return null;
        }
        String year = date.substring(0, 4);
        String month = date.substring(4, 6);
        String day = date.substring(6, 8);
        return year + "/" + month + "/" + day;
    }
    
    /**
     * 获取当前系统时间 返回格式"HH:mm:ss"
     *
     * @return
     */
    public static String formatSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }
}
