package com.zixu.paysapi.mvc.util;

import okhttp3.*;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class HttpClientUtils {
    public static String sendGet(String url) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            URLConnection connection = realUrl.openConnection();
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                return null;
            }
        }
        return result.toString();
    }
    

    public static String sendPost(String url, String param) {
        return sendPost(url, param, null);
    }

    /***
     * 发送post请求
     * @param url 请求url
     * @param param 请求报文
     * @return 返回数据
     */
    public static String sendPost(String url, String param, String ContentType) {
        String result = null;
        OkHttpClient httpClient = new OkHttpClient();
        String ContentTypea = "text/html;charset=utf-8";
        if(ContentType != null){
            ContentTypea = ContentType;
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse(ContentTypea), param);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        try {
            Response response = httpClient.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            return null;
        }
        return result;
    }
}
