package com.zixu.paysapi.mvc.util;

import java.util.HashMap;
import java.util.Map;

import com.zixu.paysapi.jpa.entity.Order;
import com.zixu.paysapi.jpa.service.OrderService;
import com.zixu.paysapi.util.DateUtil;

public class GcnyOrderAddNotice extends Thread {

	private String outTradeNo;
	
	private OrderService orderService;
	
	public void run() {
		
		
		Order order = orderService.findByOutTradeNo(outTradeNo);
		
		if(order == null) {
			return;
		}
		
		Map<String, String> res = new HashMap<>();
		res.put("outTradeNo", order.getOutTradeNo());
		res.put("price", String.valueOf(order.getPrice()));
		res.put("route", order.getType());
		res.put("uid", order.getUid());
		res.put("orderCode", String.valueOf(order.getId()));
		
		for (int i = 0; i < 5;i++) {
			String req = HttpClientUtils.sendPost("http://118.25.45.153/trade/orderStaAddGuma.html",com.alibaba.fastjson.JSONObject.toJSONString(res));
			
			if("SUCCESS".equals(req)) {
				return;
			}
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public GcnyOrderAddNotice(String outTradeNo,OrderService orderService) {
		this.outTradeNo = outTradeNo;
		this.orderService = orderService;
	}
	
	
	
}
