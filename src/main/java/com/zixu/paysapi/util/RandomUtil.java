package com.zixu.paysapi.util;

import java.util.Random;

public class RandomUtil {

    private RandomUtil() {
    }

    private static int step = 0;

    public synchronized static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "qwertyuiopasdfghjklzxcvbnm0123456789";
        Random random = new Random(System.currentTimeMillis() + step);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        step++;
        return sb.toString();
    }

    public synchronized static String getRandomNumber(int length) { //length表示生成字符串的长度
        String base = "0123456789";
        Random random = new Random(System.currentTimeMillis() + step);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        step++;
        return sb.toString();
    }

}
