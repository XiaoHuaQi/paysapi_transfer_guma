package com.zixu.paysapi.util;

public class PKUtil {

    private PKUtil() {
    }

    public synchronized static String generalPK() {
        String newPK = RandomUtil.getRandomString(32);
        return newPK;
    }
}
