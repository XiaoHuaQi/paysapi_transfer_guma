package com.zixu.paysapi.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Demo {
	
	public static void main(String[] args) {
		
		String token = "1M0I75T8XOFUXSSKKIA5D433FU0T90WI";
		
		Map<String, String> res = new HashMap<>();
		
		res.put("outTradeNo", "123224335123123");
		res.put("price", "100");
		res.put("payTime", "2018-12-17 16:48:34");
		res.put("alipayPushFee", "100");
		res.put("payState", "SUCCESS");
		res.put("nonceStr", "QH5QI8XEUDH5W7VDJX0FJ191K2OYI6FP");
		res.put("type", "alipay");
		res.put("uid", "5EWAYOD3GFE5X2KCNL4SD1GGC8O79CXL");
		//res.put("state", "1");
		//res.put("nonceStr", "1");
		//res.put("notifyUrl", "https://www.baidu.com");
		//res.put("payTime", DateUtil.getStringDateTime());
		//res.put("scene", "qrcode");
		res.put("sign", AsciiOrder.sign(res, token));
		
		System.out.println(res);
		
		
		
		/*Map<String,String> res = new HashMap<>();
        res.put("status","SUCCESS");
        res.put("outTradeNo","pad5375627967535928260");
        res.put("uid","e1f320e7182e420eaab94fb46c84372a");
        res.put("totalFee","2000");
        res.put("payTime","2018-11-26 12:37:08");
        res.put("transactionID", "1543207013641811d757724d499c6f97");
        res.put("type","alipay");
        res.put("sign",sign(res, "775bd8f6cfc74b8ba727adca766ad0a8"));
		
		System.out.println(res);*/
		
		
		
		
		//String reString = sendPost("http://119.29.27.13:81/pay/gateway", JSONObject.toJSONString(res), null);
		//String reString = sendPost("http:/127.0.0.1:8080/order/api/pay", JSONObject.toJSONString(res), null);
		//System.out.println(reString);
	}
	
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
        return Md5Utils.md5(strSign).toUpperCase();
    }
	
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
