package com.ecmoho.base.Util;

import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by meidejing on 2016/7/15.
 */
public class DateUtil {
    public static final String DAY="day";
    public static final String WEEK="week";
    public static final String MONTH="month";
    //获取当前日期格式yyyy-MM-dd
    public static String getNowDateStr(String format){
        Calendar cal   =   Calendar.getInstance();
        String nowDateStr= new SimpleDateFormat(format).format(cal.getTime());
        return nowDateStr;
    }
    //获取当前日期上周一日期
    public static String getLastWeekStartDate(String dateStr){
        String lastMonday="";
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = DateUtils.addDays(df.parse(dateStr), -1);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.WEEK_OF_YEAR, -1);// 一周
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            lastMonday= df.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return lastMonday;
    }
    //获取当前日期上周日日期
    public static String getLastWeekEndDate(String dateStr){
        String lastSunday="";
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = DateUtils.addDays(df.parse(dateStr), -1);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.DAY_OF_WEEK, 1);
            lastSunday= df.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return lastSunday;
    }
    //获取当前日期上个月第一天日期
    public static String getLastMonthStartDate(String dateStr){
        String lastMonthStart="";
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = DateUtils.addDays(df.parse(dateStr), -1);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MONTH, -1);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            lastMonthStart= df.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return lastMonthStart;
    }
    //获取当前日期上个月最后一天日期
    public static String getLastMonthEndDate(String dateStr){
        String lastMonthEnd="";
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = DateUtils.addDays(df.parse(dateStr), -1);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.add(Calendar.DATE, -1);
            lastMonthEnd= df.format(cal.getTime());
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return lastMonthEnd;
    }
    //获取过去的十五天日期集合
    public static List<String> getLastFifteenDays(String dateStr) {
        List<String> dateList = new ArrayList<String>();
        for (int i = 1; i < 16; i++) {
            try {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date date = df.parse(dateStr);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.DATE, -i);
                String day = df.format(cal.getTime());
                dateList.add(day);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return  dateList;
    }
}
