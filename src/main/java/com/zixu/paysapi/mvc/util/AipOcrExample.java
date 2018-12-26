package com.zixu.paysapi.mvc.util;

import java.util.HashMap;

import org.json.JSONObject;

import com.alibaba.fastjson.JSONArray;
import com.baidu.aip.ocr.AipOcr;
import com.zixuapp.redis.Config;

public class AipOcrExample {
	
	//设置APPID/AK/SK
	private static Config config = new Config();
    public static final String APP_ID = config.get("bappid");
    public static final String API_KEY = config.get("bapikey");
    public static final String SECRET_KEY = config.get("bsecretKey");

    private static AipOcr client;
    
    public static AipOcr getAipOcr() {
    	if(client == null) {
    		client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
    	}
    	return client;
    }
    
    
    public static void main(String[] args) {
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        HashMap<String, String> options = new HashMap<String, String>();
        options.put("language_type", "CHN_ENG");
        options.put("detect_direction", "true");
        options.put("detect_language", "true");
        options.put("probability", "true");
        
        
        //参数为本地图片路径
        String image = "C:\\Users\\wangkaipeng\\Desktop\\固码apk\\1.jpg";
        JSONObject res = client.basicGeneral(image, options);
       System.out.println(res.toString(2));

        // 参数为本地图片二进制数组
        /*byte[] file = readImageFile(image);
        res = client.basicGeneral(file, options);
        System.out.println(res.toString(2));*/

        
        // 通用文字识别, 图片参数为远程url图片
        //JSONObject res = client.basicGeneralUrl(url, options);
        //System.out.println(res.toString(2));
       
        
       String price = "";
       JSONArray jsonArray = JSONArray.parseArray(res.get("words_result").toString());
       for(Object object : jsonArray) {
			String str = com.alibaba.fastjson.JSONObject.parseObject(object.toString()).getString("words");
			if(str.indexOf("￥") != -1) {
				price = String.valueOf(Double.valueOf(str.substring(str.indexOf("￥")+1,str.length()))*100).replace(".0", "");
				break;
			}
		}
       System.out.println(price);
    }
}
