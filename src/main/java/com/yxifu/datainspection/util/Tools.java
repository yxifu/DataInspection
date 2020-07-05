package com.yxifu.datainspection.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yxifu
 * @date 2020/05/24
 **/
public  class Tools {

    public static String DateFormat_yyyyMMDD = "yyyy-MM-dd";
    public static String DateFormat_yyyyMMDDHHmmss = "yyyy-MM-dd HH:mm:ss";


    public static String formatDate(){
        return formatDate(new Date(),DateFormat_yyyyMMDD);
    }

    public static String formatDate(Date date){
        return formatDate(date,DateFormat_yyyyMMDD);
    }

    public static String formatDate(String format){
        return formatDate(new Date(),format);
    }

    public static String formatDate(Date date, String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }


    public static Date parseDate(String date) throws ParseException {
        return parseDate(date, DateFormat_yyyyMMDD);
    }
    public static Date parseDate(String date, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.parse(date);
    }



}
