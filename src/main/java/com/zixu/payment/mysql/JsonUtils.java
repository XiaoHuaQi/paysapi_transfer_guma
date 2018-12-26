package com.zixu.payment.mysql;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Zixu Liao
 * @description:json工具
 */
public class JsonUtils {
    public static String toJson(Object arg) {
        if (arg == null) {
            return null;
        }
        Gson gson = new Gson();
        try {
            return gson.toJson(arg);
        } catch (Exception e) {
            return null;
        }
    }

    public static Map<String, Object> jsonObject(String json) {
        if(json == null){
            return null;
        }
        try {
            return (Map<String, Object>) JSONObject.parse(json);
        } catch (Exception e) {
            return null;
        }
    }

    public static List jsonArray(String json) {
        if(json == null){
            return null;
        }
        try {
            return JSONObject.parseArray(json);
        } catch (Exception e) {
            return null;
        }
    }

    public static List jsonArray(Object json) {
        if(json == null){
            return null;
        }
        try {
            return JSONObject.parseArray(toJson(json));
        } catch (Exception e) {
            return null;
        }
    }

    public static List<Map<String, String>> listJSONObjectToListMap(List list) {
        if(list == null){
            return null;
        }
        List<Map<String,String>> tempList = new ArrayList<>();
        for (Object item : list) {
            Map<String,String> tempItem = (Map<String, String>) item;
            tempList.add(tempItem);
        }
        return tempList;
    }
}
