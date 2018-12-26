package com.zixu.payment.mysql;

import java.security.MessageDigest;
import java.util.*;

/**
 * @author: Zixu Liao
 * @description:
 */
public class CommonUtils {
    /***
     * 判断数组里有没有某某某
     *
     * @param v
     *            字符串
     * @param data
     *            数组
     * @return
     */
    public static boolean isArrayString(String v, String[] data) {
        if (data.length != 0) {
            for (String rV : data) {
                if (rV.equals(v)) {
                    return true;
                }
            }
        }
        return false;
    }

    /***
     * 通过uuid生成随机数
     *
     * @return 随机数[32位数]
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

    /***
     * 获得当前时间戳
     *
     * @return s
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    /***
     * 获得当前时间戳
     *
     * @return 毫秒
     */
    public static long getCurrentTimestampMs() {
        return System.currentTimeMillis();
    }

    public static String getToken() {
        return generateUUID() + getCurrentTimestamp();
    }


    /***
     * 驼峰转下横线写法
     * @param param
     * @return
     */

    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c) && i != 0) {
                sb.append('_');
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString().toLowerCase();
    }

    /***
     * md5编码
     *
     * @param string
     *            需编码内容
     * @return md5 String
     */
    public static String md5Encode(String string) {
        byte[] hash = new byte[0];
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (Exception e) {
            return null;
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /***
     *  微信支付md5签名
     *
     * @param map
     * @param keyValue
     * @return
     */

    public static String wechatSign(Map<String, String> map, String keyValue) {
        Object[] array = map.keySet().toArray();
        Arrays.sort(array);
        StringBuilder sb = new StringBuilder();
        for (Object mapKey : array) {
            sb.append(mapKey).append("=").append(map.get(mapKey.toString())).append("&");
        }
        String sortArray = sb.append("key").append("=").append(keyValue).toString();
        return md5Encode(sortArray).toUpperCase();
    }
    /***
     *  系统md5签名
     *
     * @param map
     * @param token
     * @return
     */

    public static String paySign(Map<String, Object> map, String token) {
        Object[] array = map.keySet().toArray();
        Arrays.sort(array);
        StringBuilder sb = new StringBuilder();
        for (Object mapKey : array) {
            sb.append(mapKey).append("=").append(map.get(mapKey.toString())).append("&");
        }
        String sortArray = sb.append("key").append("=").append(token).toString();
        return md5Encode(sortArray).toUpperCase();
    }

}
