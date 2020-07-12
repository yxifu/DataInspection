package com.yxifu.datainspection.util;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

//import org.apache.commons.codec.binary.Hex;

/**
 * @author yxifu
 * @date 2020/07/12
 **/
public class MD5Util {

    /**
     * 普通MD5
     *
     * @param input
     * @return
     * @author daniel
     * @time 2016-6-11 下午8:00:28
     */
    public static String MD5(String input) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return "check jdk";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        char[] charArray = input.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }

    /**
     * 加盐MD5
     *
     * @param password
     * @return
     * @author daniel
     * @time 2016-6-11 下午8:45:04
     */
    public static String generate(String password) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sb.append("0");
            }
        }
        String salt = sb.toString();
        System.out.println("salt:"+salt);
        return generate(password,salt);
    }
    /**
     * 加盐MD5
     *
     * @param password
     * @return
     * @author daniel
     * @time 2016-6-11 下午8:45:04
     */
    public static String generate(String password, String salt) {
/*        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sb.append("0");
            }
        }
        String salt = sb.toString();*/
        System.out.println("salt:"+salt);
        password = md5Hex(password + salt);
        while (password.length()<32){
            password+="0";
        }
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return new String(cs);
    }

    /**
     * 校验加盐后是否和原文一致
     *
     * @param password
     * @param md5
     * @return
     * @author daniel
     * @time 2016-6-11 下午8:45:39
     */
    public static boolean verify(String password, String md5) {
        try {
            char[] cs1 = new char[32];
            char[] cs2 = new char[16];
            for (int i = 0; i < 48; i += 3) {
                cs1[i / 3 * 2] = md5.charAt(i);
                cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
                cs2[i / 3] = md5.charAt(i + 1);
            }
            String salt = new String(cs2);
            return md5Hex(password + salt).equals(new String(cs1));
        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 获取十六进制字符串形式的MD5摘要
     */
    private static String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            // return new String(new Hex().encode(bs));
            return new BigInteger(1, bs).toString(16);
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        String text = "com.yxifu";
        System.out.println("原始的：" + MD5(text));
        //System.out.println("加盐后：" + generate(text,"1684823178010576"));
        System.out.println("加盐后：" + generate(text));
        System.out.println("加盐后：" + generate(text));
        System.out.println("加盐后：" + generate(text));
        System.out.println("加盐后：" + generate(text));
        System.out.println("加盐后：" + generate(text));
        System.out.println("加盐后：" + generate(text));
        System.out.println("加盐后：" + generate(text));

        System.out.println("比较后：" + verify("com.yxifu", generate(text)));
        System.out.println("比较后：" + verify("com.yxifu0", generate(text)));

        //tempSalt 某一次加盐后的值
        String[] tempSalt = {
                "b8944db2c065a5d656c7aa14928029f4038155d691e0958a",
                "86049966055de66882b99f92750594a1fb06d3107ed53d92",
                "972d8446d106147e6fd81212579562c38f77130f1b52b13e",
                "444659a21c62054c3f44e99c620a6893175226476fb88829"
        };

        for (String temp : tempSalt) {
            System.out.println("是否是同一字符串:" + verify("com.yxifu", temp));
        }
    }
}