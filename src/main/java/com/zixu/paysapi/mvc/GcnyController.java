package com.zixu.paysapi.mvc;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.zixu.paysapi.jpa.dto.ReturnDto;
import com.zixu.paysapi.jpa.entity.Commodity;
import com.zixu.paysapi.jpa.entity.Order;
import com.zixu.paysapi.jpa.entity.Qrcode;
import com.zixu.paysapi.jpa.entity.RechargeUserDetailed;
import com.zixu.paysapi.jpa.entity.SetMealPurchase;
import com.zixu.paysapi.jpa.entity.User;
import com.zixu.paysapi.jpa.service.CommodityService;
import com.zixu.paysapi.jpa.service.OrderService;
import com.zixu.paysapi.jpa.service.QrcodeService;
import com.zixu.paysapi.jpa.service.RechargeUserDetailedService;
import com.zixu.paysapi.jpa.service.SetMealPurchaseService;
import com.zixu.paysapi.jpa.service.UserService;
import com.zixu.paysapi.mvc.util.AipOcrExample;
import com.zixu.paysapi.mvc.util.GcnyOrderChangeNotice;
import com.zixu.paysapi.mvc.util.RequestJson;
import com.zixu.paysapi.util.DateUtil;
import com.zixu.paysapi.util.FileUtil;
import com.zixu.paysapi.util.MD5;
import com.zixu.paysapi.util.SysUtil;

@RequestMapping("/gcny")
@Controller
public class GcnyController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SetMealPurchaseService setMealPurchaseService;
	
	@Autowired
	private RechargeUserDetailedService rechargeUserDetailedService;
	
	@Autowired
	private CommodityService commodityService;
	
	@Autowired
	private QrcodeService qrcodeService;
	
	@Autowired
	private OrderService orderService;
	
	
	@RequestMapping("/user/save")
	@ResponseBody
	public ReturnDto userSave(HttpServletRequest request) {
		
		String userName = request.getParameter("userName");
		String account = request.getParameter("account");
		String password = request.getParameter("password");
		String mobile = request.getParameter("mobile");
		if(userName == null || account == null || password == null || mobile == null) {
			return ReturnDto.send(100001);
		}
		
		User user = new User();
		if(userService.findByName(userName) != null) {
			return ReturnDto.send(100014);
		}
		user.setUserName(userName);
		user.setMobile(mobile);
		user.setAccount(account);
		user.setPassword(MD5.MD5Encode(password));
		user.setState("0");
		user.setType("merchant");
		user = userService.save(user);
		if(user == null) {
			return ReturnDto.send(100005);
		}
		
		return ReturnDto.send(user.getUid());
	}
	
	@RequestMapping("/user/isUse")
	@ResponseBody
	public ReturnDto userIsUse(HttpServletRequest request) {
		
		String uid = request.getParameter("uid");
		String isUse = request.getParameter("isUse");
		if(uid == null || isUse == null) {
			return ReturnDto.send(100001);
		}
		User user = userService.findByUid(uid);
		
		if(user == null) {
			return ReturnDto.send(100002);
		}
		
		if("1".equals(isUse)) {
			user.setState("0");
		}else if("0".equals(isUse)) {
			user.setState("1");
		}else{
			return ReturnDto.send(-1);
		}
		
		user = userService.save(user);
		if(user == null) {
			return ReturnDto.send(100005);
		}
		
		return ReturnDto.send(true);
	}
	
	@RequestMapping("/user/openMeal")
	@ResponseBody
	public ReturnDto userOpenMeal(HttpServletRequest request) {
		
		String uid = request.getParameter("uid");
		String data = request.getParameter("data");
		if(uid == null || data == null) {
			return ReturnDto.send(100001);
		}
		User user = userService.findByUid(uid);
		
		if(user == null) {
			return ReturnDto.send(100002);
		}
		try {
			DateUtil.strToDateLong(data);
		} catch (Exception e) {
			return ReturnDto.send(100009);
		}
		
		SetMealPurchase setMealPurchase = new SetMealPurchase();
		setMealPurchase.setUserID(user.getId());
		setMealPurchase.setId(SysUtil.generalPK());
		setMealPurchase.setState("0");
		setMealPurchase.setExpireDate(data);
		setMealPurchase.setProcedures(1000);
		setMealPurchase = setMealPurchaseService.save(setMealPurchase);
		if(setMealPurchase == null) {
			return ReturnDto.send(100005);
		}
		
		return ReturnDto.send(true);
	}
	
	@RequestMapping("/user/queryCommodity")
	@ResponseBody
	public ReturnDto userQueryCommodity(HttpServletRequest request) {
		
		String uid = request.getParameter("uid");
		if(uid == null) {
			return ReturnDto.send(100001);
		}
		User user = userService.findByUid(uid);
		
		if(user == null) {
			return ReturnDto.send(100002);
		}
		List<Commodity> list =  commodityService.findByList(user.getId());
		if(list == null) {
			return ReturnDto.send(true);
		}
		
		
		List<Map<String, Object>> res = new ArrayList<>();
		for (Commodity commodity : list) {
			Map<String, Object> map = new HashMap<>();
			map.put("commodityID", commodity.getId());
			map.put("alipayQrcodeNum", qrcodeService.findByCommodityID(String.valueOf(commodity.getId()), "alipay"));
			map.put("wechatQrcodeNum", qrcodeService.findByCommodityID(String.valueOf(commodity.getId()), "wechat"));
			map.put("fee", commodity.getFee());
			res.add(map);
		}
		return ReturnDto.send(res);
		
	}
	
	@RequestMapping("/user/deleteCommodity")
	@ResponseBody
	public ReturnDto deleteCommodity(HttpServletRequest request) {
		
		String uid = request.getParameter("uid");
		String commodityIDStr = request.getParameter("commodityID");
		
		
		if(uid == null || commodityIDStr == null) {
			return ReturnDto.send(100001);
		}
		Long commodityID = 0L;
		try {
			commodityID = Long.valueOf(commodityIDStr);
		} catch (Exception e) {
			return ReturnDto.send(100009);
		}
		User user = userService.findByUid(uid);
		
		if(user == null) {
			return ReturnDto.send(100002);
		}
		Commodity commodity = commodityService.findById(commodityID);
		if(commodity == null) {
			return ReturnDto.send(100007);
		}
		
		commodityService.delete(commodityID);
		qrcodeService.deleteByAll(String.valueOf(commodityID));
		
		return ReturnDto.send(true);
		
	}
	
	@RequestMapping("/user/saveCommodity")
	@ResponseBody
	public ReturnDto saveCommodity(HttpServletRequest request) {
		
		String uid = request.getParameter("uid");
		String money = request.getParameter("money");
		String name = request.getParameter("name");
		if(uid == null || name == null || money == null) {
			return ReturnDto.send(100001);
		}
		User user = userService.findByUid(uid);
		int fee = 0;
		try {
			fee = Integer.valueOf(money);
		} catch (Exception e) {
			return ReturnDto.send(100009);
		}
		if(user == null) {
			return ReturnDto.send(100002);
		}
		Commodity commodity = commodityService.findByFee(fee, user.getId());
		if(commodity != null) {
			commodity.setName(name);
			commodity.setTime(DateUtil.getStringDateTime());
		}else {
			commodity = new Commodity();
			commodity.setName(name);
			commodity.setUserID(user.getId());
			commodity.setFee(fee);
		}
		
		commodity = commodityService.save(commodity);
		if(commodity == null) {
			return ReturnDto.send(100005);
		}
		
		return ReturnDto.send(true);
		
	}
	
	@RequestMapping("/user/rechargeMerchant")
	@ResponseBody
	public ReturnDto rechargeMerchant(HttpServletRequest request) {
		
		String uid = request.getParameter("uid");
		String money = request.getParameter("money");
		if(uid == null || money == null) {
			return ReturnDto.send(100001);
		}
		try {
			Integer.valueOf(money);
		} catch (Exception e) {
			return ReturnDto.send(100009);
		}
		
		User user = userService.findByUid(uid);
		
		if(user == null) {
			return ReturnDto.send(100002);
		}
		
		RechargeUserDetailed userDetailed = new RechargeUserDetailed();
		userDetailed.setUserID(user.getId());
		userDetailed.setFee(Integer.valueOf(money));
		userDetailed.setRemarks("接口充值");
		userDetailed = rechargeUserDetailedService.save(userDetailed);
		if(userDetailed == null) {
			return ReturnDto.send(100005);
		}
		
		return ReturnDto.send(true);
		
	}
	
	@RequestMapping("/user/queryMerchantBalance")
	@ResponseBody
	public ReturnDto queryMerchantBalance(HttpServletRequest request) {
		
		String uid = request.getParameter("uid");
		if(uid == null) {
			return ReturnDto.send(100001);
		}
		User user = userService.findByUid(uid);
		
		if(user == null) {
			return ReturnDto.send(100002);
		}
		
		return ReturnDto.send(String.valueOf(rechargeUserDetailedService.sum(user.getId())));
		
	}
	
	@RequestMapping("/user/uploadImg")
	@ResponseBody
	public ReturnDto uploadImg(HttpServletRequest request) {
		
		String uid = request.getParameter("uid");
		String commodityIDStr = request.getParameter("commodityID");
		String imgJson = request.getParameter("imgJson");
		if(uid == null || imgJson == null || commodityIDStr == null) {
			return ReturnDto.send(100001);
		}
		Long commodityID = 0L;
		try {
			commodityID = Long.valueOf(commodityIDStr);
		} catch (Exception e) {
			return ReturnDto.send(100009);
		}
		JSONArray jsonArray = null;
		try {
			jsonArray = JSONArray.parseArray(imgJson);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = JSONObject.parseObject(jsonArray.get(i).toString());
				if(jsonObject.getString("imgUrl") == null || jsonObject.getString("type") == null || jsonObject.get("price") == null) {
					return ReturnDto.send(100009);
				}
				if(!jsonObject.getString("type").equals("alipay") && !"wechat".equals(jsonObject.getString("type"))) {
					return ReturnDto.send(100009);
				}
				Integer.valueOf(jsonObject.get("price").toString());
			}
		} catch (Exception e) {
			return ReturnDto.send(100009);
		}
		
		User user = userService.findByUid(uid);
		
		if(user == null) {
			return ReturnDto.send(100002);
		}
		
		Commodity commodity = commodityService.findById(commodityID);
		if(commodity == null) {
			return ReturnDto.send(100007);
		}
		if(!commodity.getUserID().equals(user.getId())) {
			return ReturnDto.send(100026);
		}
		
		int success = 0;
		int fail = 0;
		List<Map<String, Object>> errorList = new ArrayList<>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = JSONObject.parseObject(jsonArray.get(i).toString());
			
			Qrcode qrcode = new Qrcode();
			
			qrcode.setType(jsonObject.getString("type"));
			qrcode.setFee(Integer.valueOf(jsonObject.get("price").toString()));
			qrcode.setCommodityID(String.valueOf(commodityID));
			qrcode.setUrl(jsonObject.getString("imgUrl"));
			qrcode.setUserID(user.getId());
			qrcode = qrcodeService.save(qrcode);
			if(qrcode == null) {
				fail++;
				Map<String, Object> error = new HashMap<>();
				error.put("imgUrl", jsonObject.getString("imgUrl"));
				error.put("price", jsonObject.getString("price"));
				error.put("error", jsonObject.getString("系统繁忙"));
				errorList.add(error);
			}else {
				success++;
			}
		}
		Map<String, Object> res = new HashMap<>();
		res.put("success", success);
		res.put("fail", fail );
		res.put("failImgUrl", errorList );
		
		return ReturnDto.send(res);
		
	}
	
	@RequestMapping("/user/queryQrcodeUrl")
	@ResponseBody
	public ReturnDto queryQrcodeUrl(HttpServletRequest request) {
		

		String uid = request.getParameter("uid");
		String commodityIDStr = request.getParameter("commodityID");
		if(uid == null || commodityIDStr == null) {
			return ReturnDto.send(100001);
		}
		
		User user = userService.findByUid(uid);
		
		if(user == null) {
			return ReturnDto.send(100002);
		}
		Long commodityID = 0L;
		try {
			commodityID = Long.valueOf(commodityIDStr);
		} catch (Exception e) {
			return ReturnDto.send(100009);
		}
		
		Commodity commodity = commodityService.findById(commodityID);
		if(commodity == null) {
			return ReturnDto.send(100007);
		}
		if(!commodity.getUserID().equals(user.getId())) {
			return ReturnDto.send(100026);
		}
		
		List<Qrcode> list = qrcodeService.findByQrcodeList(commodity.getUserID(), String.valueOf(commodityID));
		List<Map<String, Object>> resList = new ArrayList<>();
		if(list == null) {
			list = new ArrayList<>();
		}
		for (Qrcode qrcode : list) {
			Map<String, Object> res = new HashMap<>();
			res.put("id", qrcode.getId());
			res.put("fee", qrcode.getFee());
			res.put("qrcodeUrl", qrcode.getUrl());
			res.put("type", qrcode.getType());
			resList.add(res);
		}
		
		return ReturnDto.send(resList);
	}
	
	@RequestMapping("/user/saveQrcodeUrl")
	@ResponseBody
	public ReturnDto saveQrcodeUrl(HttpServletRequest request) {
		
		
		String uid = request.getParameter("uid");
		String dataJson = request.getParameter("dataJson");
		if(uid == null || dataJson == null) {
			return ReturnDto.send(100001);
		}
		
		User user = userService.findByUid(uid);
		
		if(user == null) {
			return ReturnDto.send(100002);
		}
		JSONArray jsonArray = null;
		try {
			jsonArray = JSONArray.parseArray(dataJson);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = JSONObject.parseObject(jsonArray.get(i).toString());
				if(jsonObject.getString("qrcodeUrl") == null || jsonObject.getString("commodityID") == null || jsonObject.getString("type") == null || jsonObject.get("fee") == null) {
					return ReturnDto.send(100009);
				}
				if(!jsonObject.getString("type").equals("alipay") && !"wechat".equals(jsonObject.getString("type"))) {
					return ReturnDto.send(100009);
				}
				Integer.valueOf(jsonObject.get("fee").toString());
				Commodity commodity = commodityService.findById(Long.valueOf(jsonObject.getString("commodityID")));
				if(commodity == null) {
					return ReturnDto.send(100007,false,jsonObject.getString("commodityID"));
				}
			}
		} catch (Exception e) {
			return ReturnDto.send(100009);
		}
		int success = 0;
		int fail = 0;
		List<Map<String, Object>> errorList = new ArrayList<>();
		
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = JSONObject.parseObject(jsonArray.get(i).toString());
			
			Qrcode qrcode = new Qrcode();
			
			qrcode.setType(jsonObject.getString("type"));
			qrcode.setFee(Integer.valueOf(jsonObject.get("fee").toString()));
			qrcode.setCommodityID(jsonObject.getString("commodityID"));
			qrcode.setUrl(jsonObject.getString("qrcodeUrl"));
			qrcode.setUserID(user.getId());
			qrcode = qrcodeService.save(qrcode);
			if(qrcode == null) {
				fail++;
				Map<String, Object> error = new HashMap<>();
				error.put("qrcodeUrl", jsonObject.getString("commodityID"));
				error.put("fee", jsonObject.getString("fee"));
				error.put("type", jsonObject.getString("type"));
				error.put("error", jsonObject.getString("系统繁忙"));
				errorList.add(error);
			}else {
				success++;
			}
		}
		
		Map<String, Object> res = new HashMap<>();
		res.put("success", success);
		res.put("fail", fail );
		res.put("failImgUrl", errorList );
		
		return ReturnDto.send(res);
	}
	
	@RequestMapping("/user/deleteQrcodeUrl")
	@ResponseBody
	public ReturnDto deleteQrcodeUrl(HttpServletRequest request) {
		
		
		String uid = request.getParameter("uid");
		String qrCodeIDStr = request.getParameter("qrCodeID");
		if(uid == null || qrCodeIDStr == null) {
			return ReturnDto.send(100001);
		}
		
		User user = userService.findByUid(uid);
		
		if(user == null) {
			return ReturnDto.send(100002);
		}
		Long qrCodeID = 0L;
		try {
			qrCodeID = Long.valueOf(qrCodeID);
		} catch (Exception e) {
			return ReturnDto.send(100009);
		}
		
		Qrcode qrcode =  qrcodeService.findById(qrCodeID);
		if(qrcode == null) {
			return ReturnDto.send(100012);
		}
		
		qrcodeService.delete(qrCodeID);
		
		return ReturnDto.send(true);
	}
	
	@RequestMapping("/user/closeCommodity")
	@ResponseBody
	public ReturnDto closeCommodity(HttpServletRequest request) {
		
		
		String uid = request.getParameter("uid");
		String commodityIDStr = request.getParameter("commodityID");
		if(uid == null || commodityIDStr == null) {
			return ReturnDto.send(100001);
		}
		
		User user = userService.findByUid(uid);
		
		if(user == null) {
			return ReturnDto.send(100002);
		}
		Long commodityID = 0L;
		try {
			commodityID = Long.valueOf(commodityIDStr);
		} catch (Exception e) {
			return ReturnDto.send(100009);
		}
		Commodity commodity = commodityService.findById(commodityID);
		if(commodity == null) {
			return ReturnDto.send(100007);
		}
		if(!commodity.getUserID().equals(user.getId())) {
			return ReturnDto.send(100026);
		}
		qrcodeService.deleteByAll(String.valueOf(commodityID));
		
		return ReturnDto.send(true);
	}
	
	@RequestMapping("/order/isSuccess")
	@ResponseBody
	public ReturnDto orderIsSuccess(HttpServletRequest request) {
		
		
		String uid = request.getParameter("uid");
		String orderCodeStr = request.getParameter("orderCode");
		if(uid == null || orderCodeStr == null) {
			return ReturnDto.send(100001);
		}
		
		User user = userService.findByUid(uid);
		
		if(user == null) {
			return ReturnDto.send(100002);
		}
		
		Long orderCode = 0L;
		try {
			orderCode = Long.valueOf(orderCodeStr);
		} catch (Exception e) {
			return ReturnDto.send(100009);
		}
		
		Order order =  orderService.findById(orderCode);
		if(order == null) {
			return ReturnDto.send(100013);
		}
		
		order.setPayState("1");
		order.setPayTime(DateUtil.getStringDateTime());
		order = orderService.save(order);
		if(order == null) {
			return ReturnDto.send(100015);
		}
		
		RechargeUserDetailed userDetailed = rechargeUserDetailedService.findByOutTradeNo(order.getOutTradeNo());
		
		if(userDetailed != null) {
			
			userDetailed.setRemarks("下单扣除手续费");
			userDetailed.setOutTradeNo(null);
			rechargeUserDetailedService.save(userDetailed);
		}else {

			//扣除手续费
			SetMealPurchase setMealPurchase = setMealPurchaseService.findByUserIDAndExpireDate(user.getId());
			if(setMealPurchase != null) {
				int procedures = setMealPurchase.getProcedures();
				if(procedures > 0) {
					int proceduresFee = 0;
					try {
						proceduresFee = new BigDecimal(Math.ceil(new BigDecimal(order.getPrice()).multiply(new BigDecimal(procedures).divide(new BigDecimal(1000))).doubleValue())).intValue();
					} catch (Exception e) {
					}
					if(proceduresFee != 0) {
						RechargeUserDetailed rechargeUserDetailed = new RechargeUserDetailed();
						rechargeUserDetailed.setFee(-proceduresFee);
						rechargeUserDetailed.setUserID(order.getUserID());
						rechargeUserDetailed.setRemarks("下单扣除手续费");
						rechargeUserDetailed =  rechargeUserDetailedService.save(rechargeUserDetailed);
						
						order.setProcedures(procedures);
						order.setProceduresFee(proceduresFee);
						orderService.save(order);
					}
				}
				
			}
		}
		
		
		//开启线程
		OrderController orderController = new OrderController(order.getId(),order.getPrice(),orderService,userService);
		orderController.start();
		
		//gcny
		GcnyOrderChangeNotice gcnyOrderChangeNotice = new GcnyOrderChangeNotice(order.getOutTradeNo(), orderService);
		gcnyOrderChangeNotice.start();
		
		
		return ReturnDto.send(true);
	}
	
	@RequestMapping("/file/upload")
	@ResponseBody
	public ReturnDto upload(MultipartFile file, HttpServletRequest request, HttpServletResponse response)  {

		
		String uid = request.getParameter("uid");
		if(uid == null) {
			return ReturnDto.send(100001);
		}
		User user = userService.findByUid(uid);
		
		if(user == null) {
			return ReturnDto.send(100002);
		}
		
		if (file != null) {
			
			 Map<String, Object> resMap = new HashMap<>();
			
			MultiFormatReader formatReader=new MultiFormatReader();
			BufferedImage image;
			try {
				image = ImageIO.read(FileUtil.multipartFileToFile(file));
			} catch (IOException e1) {
				resMap.put("state", false);
				resMap.put("type", "other");
            	return ReturnDto.send(resMap);
			}
			BinaryBitmap binaryBitmap=new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
			
			HashMap hints=new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            
            Result result;
			try {
				result = formatReader.decode(binaryBitmap, hints);
			} catch (NotFoundException e1) {
				resMap.put("state", false);
				resMap.put("type", "other");
            	return ReturnDto.send(resMap);
			}
			resMap.put("qrCodeUrl", result.getText());
            if(!"QR_CODE".equals(result.getBarcodeFormat().toString())) {
            	
            	resMap.put("state", false);
            	return ReturnDto.send(resMap);
            }else {
            	resMap.put("state", true);
            }
            
            if(result.getText().indexOf("HTTPS://QR.ALIPAY.COM") != -1 || result.getText().indexOf("https://qr.alipay.com") != -1) {
            	resMap.put("type", "alipay");
            }else if(result.getText().indexOf("wxp") != -1){
            	resMap.put("type", "wechat");
            }else {
            	resMap.put("type", "other");
            	return ReturnDto.send(resMap);
            }
			
			String url = null;
			
			try {
				url = FileUtil.uploadCOS(file, request);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return ReturnDto.send(100005);
			}
			
			String qrcodePngUrl = null;
			
			try {
				qrcodePngUrl = FileUtil.uploadZXCOS(file, request);
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
				return ReturnDto.send(100005);
			}
			
			
			HashMap<String, String> options = new HashMap<String, String>();
	        options.put("language_type", "CHN_ENG");
	        options.put("detect_direction", "true");
	        options.put("detect_language", "true");
	        options.put("probability", "true");
	        
	        org.json.JSONObject res = AipOcrExample.getAipOcr().basicGeneralUrl(url, options);
	        if(res.get("log_id") == null) {
	        	return ReturnDto.send(Integer.valueOf(res.getString("error_code")),res.getString("error_msg"));
	        }
	       
	        String price = null;
	        JSONArray jsonArray = JSONArray.parseArray(res.get("words_result").toString());
	        for(Object object : jsonArray) {
	 			String str = com.alibaba.fastjson.JSONObject.parseObject(object.toString()).getString("words");
	 			if(str.indexOf("￥") != -1) {
	 				price = str.substring(str.indexOf("￥")+1,str.length());
	 				try {
						Double.valueOf(price);
						
					} catch (Exception e) {
						resMap.put("distinguish", false);
			        	resMap.put("price", 0);
			        	resMap.put("url", qrcodePngUrl);
						return ReturnDto.send(resMap);
					}
	 				break;
	 			}
	 		}
	        if(price == null) {
	        	resMap.put("distinguish", false);
	        	resMap.put("price", 0);
	        	resMap.put("url", qrcodePngUrl);
	        }else {
	        	resMap.put("distinguish", true);
	        	resMap.put("price", price);
	        	resMap.put("url", qrcodePngUrl);
	        	
	        	int fee = new BigDecimal(price).multiply(new BigDecimal(100)).toBigInteger().intValue();
	        	
	        	Qrcode qrcode = qrcodeService.findByFee(fee, user.getId(),resMap.get("type").toString());
	        	if(qrcode == null) {
	        		resMap.put("existence", true);
	        	}else {
	        		resMap.put("existence", false);
	        	}
	        }
	        
	        return ReturnDto.send(resMap);
			
		} else {
			 return ReturnDto.send(100001);
		}
		

	}
	
	@RequestMapping("/success")
	@ResponseBody
	public void success(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String jsonStr = RequestJson.returnJson(request);
		System.out.println(jsonStr);
	}
	
}
	
	
