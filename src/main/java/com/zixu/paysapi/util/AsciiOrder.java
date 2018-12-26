package com.zixu.paysapi.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AsciiOrder {
	
	public static String sign(Map<String,String> params,String weixinkey){
        //按参数名asscic码排序
        List<String> names = new ArrayList<>();
        names.addAll(params.keySet());
        java.util.Collections.sort(names);
        String strSign = "";
        for(String key:names){
        	 strSign+=key+"="+params.get(key)+"&";
        }
        String secret = "key="+weixinkey;
        strSign+=secret;
        System.out.println(strSign);
        System.out.println(Md5Utils.md5(strSign).toLowerCase());
        return Md5Utils.md5(strSign).toLowerCase();
    }
	
}
