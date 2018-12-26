package com.zixu.paysapi.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zixu.paysapi.jpa.dto.ReturnDto;
import com.zixu.paysapi.jpa.entity.AdminConfig;
import com.zixu.paysapi.jpa.entity.Config;
import com.zixu.paysapi.jpa.entity.Order;
import com.zixu.paysapi.jpa.entity.RechargeList;
import com.zixu.paysapi.jpa.entity.RechargeQrcode;
import com.zixu.paysapi.jpa.entity.SetMealPurchase;
import com.zixu.paysapi.jpa.service.AdminConfigService;
import com.zixu.paysapi.jpa.service.ConfigService;
import com.zixu.paysapi.jpa.service.OrderService;
import com.zixu.paysapi.jpa.service.RechargeListService;
import com.zixu.paysapi.jpa.service.RechargeQrcodeService;
import com.zixu.paysapi.jpa.service.RechargeUserDetailedService;
import com.zixu.paysapi.jpa.service.SetMealPurchaseService;
import com.zixu.paysapi.util.SysUtil;
import com.zixuapp.redis.RedisOperationManager;

@RequestMapping("/user/recharge")
@Controller
public class RechargeController {
	
	@Autowired
	private RechargeUserDetailedService rechargeUserDetailedService;
	
	@Autowired
	private RechargeQrcodeService rechargeQrcodeService;
	
	@Autowired
	private RechargeListService rechargeListService;
	
	@Autowired
	private AdminConfigService adminConfigService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private SetMealPurchaseService setMealPurchaseService;
	
	@Autowired
	private ConfigService configService;
	
	private static String redisKey = "mysql_recharge_qrcode_pay:";
	
	@RequestMapping("/list")
	@ResponseBody
	public ReturnDto list() {
		
		return ReturnDto.send(rechargeListService.findAll());
	}
	
	@RequestMapping("/info")
	@ResponseBody
	public ReturnDto info(Long id) {
		
		return ReturnDto.send(rechargeListService.findByID(id));
	}
	
	@RequestMapping("/consumptionDetail")
	@ResponseBody
	public ReturnDto consumptionDetail(HttpServletRequest request) {
		
		//套餐到期情况
		HashMap<String, String> userInfo = (HashMap<String, String>)request.getAttribute("userInfo");
		
		String userID = userInfo.get("userID");
		
		Map<String, Object> res = new HashMap<>();
		
		SetMealPurchase setMealPurchase =  setMealPurchaseService.findByUserID(userID);
		if(setMealPurchase == null) {
			//无购买套餐
			res.put("expireDate", "当前暂未购买套餐");
			res.put("procedures", "无");
		}else {
			res.put("expireDate", setMealPurchase.getExpireDate());
			res.put("procedures", setMealPurchase.getProcedures());
		}
		
		//剩余金额
		int sum = rechargeUserDetailedService.sum(userID);
		res.put("fee", sum);
		
		Config config =  configService.findById(userID);
		if(config != null && config.getMinFee() != 0) {
			if(sum <= config.getMinFee()) {
				res.put("minFee", false);
			}else {
				res.put("minFee", true);
			}
		}else {
			res.put("minFee", true);
		}
		
		return ReturnDto.send(res);
	}
	

	@RequestMapping("/pay")
	@ResponseBody
	public ReturnDto pay(Long rechargeID,HttpServletRequest request) {
		
		
		HashMap<String, String> userInfo = (HashMap<String, String>)request.getAttribute("userInfo");
		
		RechargeList rechargeList = rechargeListService.findByID(rechargeID);
		if(rechargeList == null) {
			return ReturnDto.send(100017);
		}
		
		AdminConfig adminConfig = adminConfigService.findByOne();
		if(adminConfig == null) {
			return ReturnDto.send(100015);
		}
		
		Map<String, String> map = findQrcode(rechargeList.getPrice(), rechargeQrcodeService, adminConfig.getType(),rechargeID);
		if(map.get("errorCode") != null) {
			return ReturnDto.send(Integer.valueOf(map.get("errorCode")));
		}
		
		String qrcodeUrl = map.get("qrcodeUrl");
		String fee = map.get("fee");
		
		//创建订单
		String outTradeNo = SysUtil.generalPK();
		
		Order order = new Order();
		order.setCommdityID(String.valueOf(rechargeList.getId()));
		order.setOrderType("2");
		order.setType(adminConfig.getType());
		order.setPayState("0");
		order.setPrice(Integer.valueOf(fee));
		order.setOutTradeNo(outTradeNo);
		order.setUserID(userInfo.get("userID"));
		order.setQrcodeUrl(qrcodeUrl);
		order.setCommdityName(rechargeList.getTitle());
		order.setOrderType("2");
		order.setProcedures(0);
		order.setProceduresFee(0);
		order = orderService.save(order);
		if(order == null) {
			return ReturnDto.send(100015);
		}
		Map<String, String> res = new HashMap<>();
		res.put("qrcodeUrl", qrcodeUrl);
		res.put("fee", fee);
		res.put("outTradeNo", outTradeNo);
		res.put("payType", adminConfig.getType());
		
		return ReturnDto.send(res);
		
	}
	
	public static Map<String, String> findQrcode(int fee,RechargeQrcodeService rechargeQrcodeService,String type,Long rechargeID) {
		
		
		Map<String, String> res = new HashMap<>();
		List<RechargeQrcode>  list = rechargeQrcodeService.findByFee(type,String.valueOf(rechargeID));
		if(list == null || list.size() == 0) {
			res.put("errorCode", "100012");
			return res;
		}
		for (RechargeQrcode qrcode : list) {
			
			String data = RedisOperationManager.getString(redisKey+"fee="+qrcode.getPrice()+"&type="+type);
			
			if(data != null) {
				continue;
			}
			if(qrcode.getPrice() == 0) {
				res.put("errorCode", "100012");
				return res;
			}
			res.put("fee", String.valueOf(qrcode.getPrice()));
			res.put("qrcodeUrl", qrcode.getUrl());
			
			RedisOperationManager.setString(redisKey+"fee="+qrcode.getPrice()+"&type="+type,qrcode.getUrl(),7200);
			
			return res;
			
		}
		res.put("errorCode", "100012");
		return res;
		
		/*
		
		String qrcode = RedisOperationManager.getString(redisKey+"fee="+fee+"&type="+type);
		
		if(qrcode != null) {
			findQrcode(fee-1, rechargeQrcodeService, type);
		}
		Map<String, String> map = new HashMap<>();
		
		RechargeQrcode rechargeQrcode = rechargeQrcodeService.findByFee(fee,type);
		if(rechargeQrcode != null) {
			map.put("qrcodeUrl", rechargeQrcode.getUrl());
			map.put("fee", String.valueOf(rechargeQrcode.getPrice()));
			RedisOperationManager.setString(redisKey+"fee="+fee+"&type="+type,rechargeQrcode.getUrl(),Integer.valueOf(new Config().get("expires")));
		}else {
			rechargeQrcode = rechargeQrcodeService.findByFee(0,type);
			if(rechargeQrcode != null) {
				map.put("qrcodeUrl", rechargeQrcode.getUrl());
				map.put("fee",String.valueOf(fee));
				RedisOperationManager.setString(redisKey+"fee="+fee+"&type="+type,rechargeQrcode.getUrl(),Integer.valueOf(new Config().get("expires")));
			
			}else {
				map.put("error", String.valueOf(100012));
			}
		}
		
		return map;*/
	}
	
	
	
	
}
