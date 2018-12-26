package com.zixu.paysapi.mvc.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.enterprise.inject.New;
import javax.ws.rs.GET;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.alibaba.fastjson.JSONObject;
import com.zixu.payment.mysql.MysqlBaseContorManager;
import com.zixuapp.redis.Config;


public class MyWebSocketClient extends WebSocketClient{
	
	private static MyWebSocketClient client;
	
 
    public MyWebSocketClient(String url) throws URISyntaxException {
        super(new URI(url));
    }
 
    @Override
    public void onOpen(ServerHandshake shake) {
        System.out.println("握手...");
        for(Iterator<String> it=shake.iterateHttpFields();it.hasNext();) {
            String key = it.next();
            System.out.println(key+":"+shake.getFieldValue(key));
        }
    }
 
    @Override
    public void onMessage(String res) {
       
    	if(res.indexOf("service:") != -1) {
    		res = res.substring(8, res.length());
    	}else {
    		return;
    	}
    	System.out.println(res);
    	JSONObject jsonObject = null;
    	try {
    		jsonObject = JSONObject.parseObject(res);
		} catch (Exception e) {
			return;
		}
    	if(jsonObject == null) {
    		return;
    	}
    	String outTradeNo = jsonObject.get("outTradeNo").toString();
    	String price = jsonObject.get("price").toString();
    	String uid = jsonObject.get("uid").toString();
    	String qrcode = jsonObject.get("qrcode").toString();
    	String type = jsonObject.get("type").toString();
    	
    	if(outTradeNo != null || price != null || uid != null || qrcode != null || type != null) {
    		System.out.println(qrcode);
    		
    		if(MysqlBaseContorManager.findOne("arbitrarily_qrcode", new Object[] {"outTradeNo","=",outTradeNo}) != null) {
    			return;
    		}
    		
    		Map<String, String> saveMap = new HashMap<>();
    		saveMap.put("outTradeNo", outTradeNo);
    		saveMap.put("type", type);
    		saveMap.put("uid", uid);
    		saveMap.put("price", price);
    		saveMap.put("qrcode", qrcode);
    		
    		System.out.println(MysqlBaseContorManager.add(new Config().get("pre")+"arbitrarily_qrcode",saveMap));
    		
    		
    	}
    	
    }
 
    @Override
    public void onClose(int paramInt, String paramString, boolean paramBoolean) {
        System.out.println("关闭...");
    }
 
    @Override
    public void onError(Exception e) {
        System.out.println("异常"+e);
        
    }
    
    public static void main(String[] args) {
        try {
            MyWebSocketClient client = new MyWebSocketClient("ws://test.zixuapp.com/websocket?uid=this");
            client.connect();
            while (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                System.out.println("还没有打开");
            }
            /*for (int i =0;i<1000;i++) {
            	   client.send("{\"outTradeNo\":\""+i+"\",\"price\":\""+i+"\",\"uid\":\"5EWAYOD3GFE5X2KCNL4SD1GGC8O79CXL\",\"type\":\"alipay\"}");
                   
			}*/
            System.out.println("建立websocket连接");
         } catch (URISyntaxException e) {
            e.printStackTrace();
        } 
    }
    
    public static MyWebSocketClient get() throws URISyntaxException {
    	
    	if(client == null || !client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
    		client = new MyWebSocketClient(new Config().get("wsUrl"));
            client.connect();
            while (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                System.out.println("还没有打开");
            }
            return client;
    	}
    	return client;
    	
    	
    }
    

    
    
}