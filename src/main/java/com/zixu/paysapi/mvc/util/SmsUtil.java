package com.zixu.paysapi.mvc.util;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;

public class SmsUtil {
	
	public static void send(String phone,String name,String date,String company,String address,String contacts,String mobile) {
		
    	SmsSingleSender sender = new SmsSingleSender(1400097493,"f951ff3b41a08038d3d73d3df2e72f56");
    	String[] params = new String[] {name,date,company,address,contacts,mobile};
    	System.out.println(params);
    	try {
    		SmsSingleSenderResult smsSingleSenderResult = sender.sendWithParam("86", phone,129197, params,"澄和体育", "", "");
    		System.out.println(JSONObject.valueToString(smsSingleSenderResult));
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (HTTPException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
