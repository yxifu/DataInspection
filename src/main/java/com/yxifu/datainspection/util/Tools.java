package com.yxifu.datainspection.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    public static String stringToMD5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }


}
