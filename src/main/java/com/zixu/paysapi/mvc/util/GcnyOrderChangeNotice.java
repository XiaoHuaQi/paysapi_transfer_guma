package com.zixu.paysapi.mvc.util;

import java.util.HashMap;
import java.util.Map;

import com.zixu.paysapi.jpa.entity.Order;
import com.zixu.paysapi.jpa.service.OrderService;
import com.zixu.paysapi.util.RandomUtil;

public class GcnyOrderChangeNotice extends Thread {

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
		res.put("type", order.getType());
		res.put("uid", order.getUid());
		res.put("nonceStr", RandomUtil.getRandomString(32));
		res.put("payTime", order.getPayTime());
		res.put("payState", "SUCCESS");
		res.put("chain_add", order.getChainAdd());
		
		for (int i = 0; i < 5;i++) {
			
			String req = HttpClientUtils.sendPost("http://118.25.45.153/trade/orderStaChangeGuma.html",com.alibaba.fastjson.JSONObject.toJSONString(res));
			
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

	public GcnyOrderChangeNotice(String outTradeNo,OrderService orderService) {
		this.outTradeNo = outTradeNo;
		this.orderService = orderService;
	}
	
	
	
}
