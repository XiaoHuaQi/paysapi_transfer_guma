package com.zixu.paysapi.mvc;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
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
import com.zixu.paysapi.jpa.dto.StatisticsDto;
import com.zixu.paysapi.jpa.entity.AdminConfig;
import com.zixu.paysapi.jpa.entity.Order;
import com.zixu.paysapi.jpa.entity.RechargeList;
import com.zixu.paysapi.jpa.entity.RechargeQrcode;
import com.zixu.paysapi.jpa.entity.RechargeUserDetailed;
import com.zixu.paysapi.jpa.entity.RegisterCode;
import com.zixu.paysapi.jpa.entity.SetMeal;
import com.zixu.paysapi.jpa.entity.SetMealPurchase;
import com.zixu.paysapi.jpa.entity.SetMealQrcode;
import com.zixu.paysapi.jpa.entity.TestQrcode;
import com.zixu.paysapi.jpa.entity.User;
import com.zixu.paysapi.jpa.service.AdminConfigService;
import com.zixu.paysapi.jpa.service.OrderService;
import com.zixu.paysapi.jpa.service.RechargeListService;
import com.zixu.paysapi.jpa.service.RechargeQrcodeService;
import com.zixu.paysapi.jpa.service.RechargeUserDetailedService;
import com.zixu.paysapi.jpa.service.RegisterCodeService;
import com.zixu.paysapi.jpa.service.SetMealPurchaseService;
import com.zixu.paysapi.jpa.service.SetMealQrcodeService;
import com.zixu.paysapi.jpa.service.SetMealService;
import com.zixu.paysapi.jpa.service.TestQrcodeService;
import com.zixu.paysapi.jpa.service.UserService;
import com.zixu.paysapi.mvc.util.AipOcrExample;
import com.zixu.paysapi.mvc.util.HttpClientUtils;
import com.zixu.paysapi.mvc.util.Page;
import com.zixu.paysapi.util.AsciiOrder;
import com.zixu.paysapi.util.DateUtil;
import com.zixu.paysapi.util.FileUtil;
import com.zixu.paysapi.util.MD5;
import com.zixu.paysapi.util.RandomUtil;
import com.zixu.paysapi.util.SysUtil;

@RequestMapping("/admin")
@Controller
public class AdminController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private SetMealService setMealService;
	
	@Autowired
	private SetMealQrcodeService setMealQrcodeService;
	
	@Autowired
	private SetMealPurchaseService setMealPurchaseService;
	
	@Autowired
	private AdminConfigService adminConfigService;
	
	@Autowired
	private RechargeQrcodeService rechargeQrcodeService;
	
	@Autowired
	private RechargeListService rechargeListService;
	
	@Autowired
	private RechargeUserDetailedService rechargeUserDetailedService;
	
	@Autowired
	private RegisterCodeService registerCodeService;
	
	@Autowired
	private TestQrcodeService testQrcodeService;
	
	
	/**
	 * 购买套餐用户列表
	 * @param pageNum
	 * @param userName
	 * @return
	 */
	
	@RequestMapping("/setMealPurchase/list")
	@ResponseBody
	public ReturnDto setMealPurchaseList(int pageNum,String userName) {
		
		return ReturnDto.send(setMealPurchaseService.findAll(pageNum, userName));
	}
	/**
	 * 新增/修改用户到期时间
	 * @param userID
	 * @param number
	 * @param type
	 * @return
	 */
	@RequestMapping("/setMealPurchase/save")
	@ResponseBody
	public ReturnDto setMealPurchaseSave(String userID,String setMealID) {
		
		
		SetMeal setMeal =  setMealService.findById(setMealID);
		if(setMeal == null) {
			return ReturnDto.send(100015);
		}
		int number = setMeal.getNumber();
		String type = setMeal.getType();
		
		if(userID == null || number == 0 || type == null) {
			return ReturnDto.send(100001);
		}
		if("1".equals(type)) {
			number = number * 1;//日
		}else if("2".equals(type)) {
			number = number * 30;//月
		}else if("3".equals(type)) {
			number = number * 365;//年
		}else {
			return ReturnDto.send(-1);
		}
		SetMealPurchase setMealPurchase = setMealPurchaseService.save(userID, String.valueOf(number),setMeal.getProcedures(),setMeal.getId());
		if(setMealPurchase == null) {
			return ReturnDto.send(100005);
		}
		return ReturnDto.send(true);
	}
	
	/**
	 * 套餐列表
	 * @return
	 */
	@RequestMapping("/setMeal/list")
	@ResponseBody
	public ReturnDto setMealList() {
		
		List<SetMeal> list  = setMealService.findAll();
		
		if(list != null && list.size() > 0) {
			for (SetMeal setMeal : list) {
				setMeal.setWechatQrcodeNum(setMealQrcodeService.findByRechargeIdNumber(setMeal.getId(),"wechat"));
				setMeal.setAlipayQrcodeNum(setMealQrcodeService.findByRechargeIdNumber(setMeal.getId(),"alipay"));
			}
		}
		
		return ReturnDto.send(list);
	}
	
	/**
	 * 套餐详情
	 * @param id
	 * @return
	 */
	@RequestMapping("/setMeal/info")
	@ResponseBody
	public ReturnDto setMealInfo(String id) {
		
		if(id == null) {
			return ReturnDto.send(100001);
		}
		
		return ReturnDto.send(setMealService.findById(id));
	}
	
	/**
	 * 删除套餐
	 * @param id
	 * @return
	 */
	@RequestMapping("/setMeal/delete")
	@ResponseBody
	public ReturnDto setMealDelete(String id) {
		
		if(id == null) {
			return ReturnDto.send(100001);
		}
		setMealService.delete(id);
		return ReturnDto.send(true);
	}
	
	/**
	 * 新增/修改套餐
	 * @param setMeal
	 * @return
	 */
	@RequestMapping("/setMeal/save")
	@ResponseBody
	public ReturnDto setMealSave(SetMeal setMeal) {
		
		if(setMeal.getTitle() == null || setMeal.getNumber() == 0 || 
				setMeal.getType() == null || 
				setMeal.getDescribes() == null) {
			return ReturnDto.send(100001);
		}
		setMeal = setMealService.save(setMeal);
		if(setMeal == null) {
			return ReturnDto.send(100005);
		}
		
		return ReturnDto.send(true);
	}
	
	/**
	 * 用户列表(不包括管理员)
	 * @return
	 */
	@RequestMapping("/merchant/list")
	@ResponseBody
	public ReturnDto merchantList() {
		return ReturnDto.send(userService.findAll());
	}
	
	/**
	 * 用户分页列表(不包括管理员)
	 * @param pageNum
	 * @param userName
	 * @return
	 */
	@RequestMapping("/merchant/page")
	@ResponseBody
	public ReturnDto merchantPage(int pageNum,String userName) {
		
		Page<User> page = userService.findAll(pageNum,userName);
		if(page != null) {
			List<User> users = page.getList();
			for (User user : users) {
				user.setSumFee(rechargeUserDetailedService.sum(user.getId()));
				SetMealPurchase setMealPurchase =  setMealPurchaseService.findByUserID(user.getId());
				if(setMealPurchase != null) {
					user.setProcedures(setMealPurchase.getProcedures());
				}
			}
			page.setList(users);
		}
		
		return ReturnDto.send(page);
	}
	

	@RequestMapping("/merchant/delete")
	@ResponseBody
	public ReturnDto merchantDelete(String id) {
		userService.delete(id);
		return ReturnDto.send(true);
	}
	
	/**
	 * 详情
	 * @param pageNum
	 * @param userName
	 * @return
	 */
	@RequestMapping("/merchant/info")
	@ResponseBody
	public ReturnDto merchantInfo(String id) {
		return ReturnDto.send(userService.findById(id));
	}
	
	@RequestMapping("/merchant/updateState")
	@ResponseBody
	public ReturnDto merchantUpdateState(String id) {
		
		if(id == null) {
			return ReturnDto.send(100001);
		}
		User user =  userService.findById(id);
		if(user == null) {
			return ReturnDto.send(100002);
		}
		if("1".equals(user.getState())) {
			user.setState("0");
		}else {
			user.setState("1");
		}
		user = userService.save(user);
		
		if(user == null) {
			return ReturnDto.send(100005);
		}
		
		return ReturnDto.send(true);
	}
	@RequestMapping("/merchant/updateUseAdmission")
	@ResponseBody
	public ReturnDto updateUseAdmission(String id) {
		
		if(id == null) {
			return ReturnDto.send(100001);
		}
		User user =  userService.findById(id);
		if(user == null) {
			return ReturnDto.send(100002);
		}
		if("1".equals(user.getUseAdmission())) {
			user.setUseAdmission("2");
		}else {
			user.setUseAdmission("1");
		}
		user = userService.save(user);
		
		if(user == null) {
			return ReturnDto.send(100005);
		}
		
		return ReturnDto.send(true);
	}
	
	@RequestMapping("/merchant/recharge/add")
	@ResponseBody
	public ReturnDto merchantRechargeAdd(String userID,int fee) {
		
		if(userID == null || fee == 0) {
			ReturnDto.send(100001);
		}
		
		RechargeUserDetailed rechargeUserDetailed = new RechargeUserDetailed();
		rechargeUserDetailed.setFee(fee);
		rechargeUserDetailed.setUserID(userID);
		rechargeUserDetailed.setRemarks("后台充值");
		rechargeUserDetailed =  rechargeUserDetailedService.save(rechargeUserDetailed);
		if(rechargeUserDetailed  == null) {
			return ReturnDto.send(100005);
		}
		
		return ReturnDto.send(true);
	}
	
	@RequestMapping("/merchant/setMeal/update")
	@ResponseBody
	public ReturnDto merchantSetMealUpdate(String userID,int procedures) {
		
		if(userID == null) {
			ReturnDto.send(100001);
		}
		
		SetMealPurchase setMealPurchase =  setMealPurchaseService.findByUserID(userID);
		if(setMealPurchase == null) {
			ReturnDto.send(100019);
		}
		setMealPurchase.setProcedures(procedures);
		setMealPurchase = setMealPurchaseService.save(setMealPurchase);
		if(setMealPurchase == null) {
			return ReturnDto.send(100005);
		}
		
		return ReturnDto.send(true);
	}
	
	
	/**
	 * 新增用户
	 * @param user
	 * @return
	 */
	@RequestMapping("/merchant/save")
	@ResponseBody
	public ReturnDto merchantSave(User user) {
		
		if(user == null || user.getUserName() == null || user.getPassword() == null) {
			return ReturnDto.send(100001);
		}
		if(user.getId() != null) {
			User userCheck = userService.findById(user.getId());
			if(userCheck == null) {
				return ReturnDto.send(100002);
			}
			if(!userCheck.getUserName().equals(user.getUserName())) {
				if(userService.findByName(user.getUserName()) != null) {
					return ReturnDto.send(100014);
				}
			}
			user.setPassword(userCheck.getPassword());
			user.setUid(userCheck.getUid());
			user.setToken(userCheck.getToken());
			user.setQqNumber(userCheck.getQqNumber());
			user.setWechat(userCheck.getWechat());
			user.setState(userCheck.getState());
		}else {
			if(userService.findByName(user.getUserName()) != null) {
				return ReturnDto.send(100014);
			}
			user.setPassword(MD5.MD5Encode(user.getPassword()));
			user.setUid(SysUtil.generalPK());
			user.setToken(SysUtil.generalPK());
			user.setState("0");
		}
		user.setType("merchant");
		user = userService.save(user);
		
		if(user == null) {
			return ReturnDto.send(100005);
		}
		
		return ReturnDto.send(true);
	}
	
	//首页
	@RequestMapping("/index/data")
	@ResponseBody
	public ReturnDto indexData(HttpServletRequest request) {
		
		List<Order> list = orderService.indexData(null);
		
		if(list != null && list.size() > 0) {
			for (Order order : list) {
				User user = userService.findById(order.getUserID());
				if(user != null) {
					order.setUserName(user.getUserName());
				}
			}
		}
		
		StatisticsDto statisticsDto = orderService.adminStatistics();
		
		StatisticsDto setMealDto = orderService.adminSetMealStatistics();
		
		StatisticsDto rechargeDto = orderService.adminRechargeStatistics();
		
		StatisticsDto proceduresDto = rechargeUserDetailedService.adminStatistics();
		
		
		Map<String, Object> res = new HashMap<>();
		res.put("statistics", statisticsDto);
		res.put("setMealDto", setMealDto);
		res.put("rechargeDto", rechargeDto);
		res.put("proceduresDto", proceduresDto);
		res.put("list", list);
		
		return ReturnDto.send(res);
	}
	
	@RequestMapping("/setMealQrcode/save")
	@ResponseBody
	public ReturnDto setMealQrcodeSave(HttpServletRequest request) {
		
		
		String dataJson = request.getParameter("dataJson");
		if(dataJson == null) {
			return ReturnDto.send(100001);
		}
		
		JSONArray jsonArray = JSONArray.parseArray(dataJson);
		
		if(jsonArray == null) {
			return ReturnDto.send(100009);
		}
		
		for (int i = 0; i < jsonArray.size(); i++) {
			
			JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(jsonArray.get(i)));
			
			String qrcodeUrl= jsonObject.getString("qrcodeUrl");
			String type = jsonObject.getString("type");
			String setMealID = jsonObject.getString("setMealID");
			String fee = jsonObject.getString("fee");
			
			if(qrcodeUrl == null || type == null || setMealID == null) {
				return ReturnDto.send(100009);
			}
			if(fee == null) {
				fee = "0";
			}
			
			SetMealQrcode qrcode = new SetMealQrcode();
			qrcode.setSetMealID(setMealID);
			qrcode.setPrice(Integer.valueOf(fee));
			qrcode.setType(type);
			qrcode.setUrl(qrcodeUrl);
			qrcode = setMealQrcodeService.save(qrcode);
			if(qrcode == null) {
				return ReturnDto.send(100005);
			}
			
		}
		return ReturnDto.send(true);
	}
	
	
	@RequestMapping("/config/info")
	@ResponseBody
	public ReturnDto configInfo() {
		
		return ReturnDto.send(adminConfigService.findByOne());
	}
	
	@RequestMapping("/config/save")
	@ResponseBody
	public ReturnDto configSave(AdminConfig adminConfig) {
		
		if(adminConfig == null || adminConfig.getType() == null || adminConfig.getOverdueTime() == 0 || adminConfig.getImmediately() == null) {
			return ReturnDto.send(100001);
		}
		
		if(!"wechat".equals(adminConfig.getType()) && !"alipay".equals(adminConfig.getType())) {
			return ReturnDto.send(100010);
		}
		
		adminConfig = adminConfigService.save(adminConfig);
		
		if(adminConfig == null) {
			return ReturnDto.send(100005);
		}
		
		return ReturnDto.send(true);
	}
	

	@RequestMapping("/rechargeQrcode/save")
	@ResponseBody
	public ReturnDto RechargeQrcodeSave(HttpServletRequest request) {
		
		
		String dataJson = request.getParameter("dataJson");
		if(dataJson == null) {
			return ReturnDto.send(100001);
		}
		
		JSONArray jsonArray = JSONArray.parseArray(dataJson);
		
		if(jsonArray == null) {
			return ReturnDto.send(100009);
		}
		
		for (int i = 0; i < jsonArray.size(); i++) {
			
			JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(jsonArray.get(i)));
			
			String qrcodeUrl= jsonObject.getString("qrcodeUrl");
			String type = jsonObject.getString("type");
			String rechargeID = jsonObject.getString("rechargeID");
			String fee = jsonObject.getString("fee");
			
			if(qrcodeUrl == null || type == null || rechargeID == null) {
				return ReturnDto.send(100009);
			}
			if(fee == null) {
				fee = "0";
			}
			
			RechargeQrcode qrcode = new RechargeQrcode();
			qrcode.setRechargeID(rechargeID);
			qrcode.setPrice(Integer.valueOf(fee));
			qrcode.setType(type);
			qrcode.setUrl(qrcodeUrl);
			qrcode = rechargeQrcodeService.save(qrcode);
			if(qrcode == null) {
				return ReturnDto.send(100005);
			}
			
		}
		return ReturnDto.send(true);
	}
	
	/**
	 * 充值列表
	 * @return
	 */
	@RequestMapping("/recharge/list")
	@ResponseBody
	public ReturnDto RechargeListList() {
		
		List<RechargeList> list = rechargeListService.findAll();
		
		if(list != null && list.size() > 0) {
			for (RechargeList rechargeList : list) {
				rechargeList.setWechatQrcodeNum(rechargeQrcodeService.findByRechargeIdNumber(rechargeList.getId(),"wechat"));
				rechargeList.setAlipayQrcodeNum(rechargeQrcodeService.findByRechargeIdNumber(rechargeList.getId(),"alipay"));
			}
		}
		return ReturnDto.send(list);
	}
	
	/**
	 * 新增充值
	 * @return
	 */
	@RequestMapping("/recharge/save")
	@ResponseBody
	public ReturnDto RechargeSave(com.zixu.paysapi.jpa.entity.RechargeList rechargeList) {
		
		if(rechargeList.getTitle() == null || rechargeList.getPrice() == 0) {
			return ReturnDto.send(100001);
		}
		rechargeList = rechargeListService.save(rechargeList);
		if(rechargeList == null) {
			return ReturnDto.send(100005);
		}
		
		return ReturnDto.send(true);
	}
	
	
	/**
	 * 删除充值
	 * @param id
	 * @return
	 */
	@RequestMapping("/recharge/delete")
	@ResponseBody
	public ReturnDto RechargeListDelete(Long id) {
		
		if(id == null) {
			return ReturnDto.send(100001);
		}
		rechargeListService.delete(id);
		return ReturnDto.send(true);
	}
	
	
	@RequestMapping("/file/upload")
	@ResponseBody
	public ReturnDto upload(MultipartFile file, HttpServletRequest request, HttpServletResponse response)  {

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
				return ReturnDto.send(100005);
			}
			
			String qrcodePngUrl = null;
			
			try {
				qrcodePngUrl = FileUtil.uploadZXCOS(file, request);
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
				return ReturnDto.send(100005);
			}
			resMap.put("qrCodeUrl", result.getText());
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
	        	
	        	SetMealQrcode qrcode = setMealQrcodeService.findByFee(fee,resMap.get("type").toString());
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

	@RequestMapping("/file/recharge/upload")
	@ResponseBody
	public ReturnDto rechargeUpload(MultipartFile file, HttpServletRequest request, HttpServletResponse response)  {

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
				return ReturnDto.send(100005);
			}
			
			String qrcodePngUrl = null;
			resMap.put("qrCodeUrl", result.getText());
			try {
				qrcodePngUrl = FileUtil.uploadZXCOS(file, request);
			} catch (Exception e1) {
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
	        	
	        	RechargeQrcode qrcode = rechargeQrcodeService.findByFee(fee,resMap.get("type").toString());
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
	@RequestMapping("/test/upload")
	@ResponseBody
	public ReturnDto testUpload(MultipartFile file, HttpServletRequest request, HttpServletResponse response)  {
		
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
				return ReturnDto.send(100005);
			}
			
			String qrcodePngUrl = null;
			resMap.put("qrCodeUrl", result.getText());
			try {
				qrcodePngUrl = FileUtil.uploadZXCOS(file, request);
			} catch (Exception e1) {
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
				
				TestQrcode qrcode = testQrcodeService.findByFee(resMap.get("type").toString(),fee);
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
	
	@RequestMapping("/updatePassword")
	@ResponseBody
	public ReturnDto updatePassword(String oldPassword,String newPassword) {
		
		if(oldPassword == null || newPassword == null) {
			 return ReturnDto.send(100001);
		}
		
		User user =  userService.findByName("admin");
		if(user == null) {
			 return ReturnDto.send(100002);
		}
		if(!user.getPassword().equals(MD5.MD5Encode(oldPassword))) {
			 return ReturnDto.send(100003);
		}
		user.setPassword(MD5.MD5Encode(newPassword));
		
		user = userService.save(user);
		
		if(user == null) {
			 return ReturnDto.send(100005);
		}
		
		return ReturnDto.send(true);
	}
	
	/**
	 * 购买套餐 充值 订单列表
	 * @param startDate
	 * @param endDate
	 * @param pageNum
	 * @param type
	 * @param orderType
	 * @return
	 */
	@RequestMapping("/setMealOrder/list")
	@ResponseBody
	public ReturnDto setMealOrderList(String startDate,String endDate,int pageNum,String type,String orderType) {
		
		return ReturnDto.send(orderService.setMealList(pageNum, type, startDate, endDate, orderType,null,null,null,null));
	}
	
	
	
	@RequestMapping("/setMeal/update")
	@ResponseBody
	public ReturnDto setMealUpdate(Long id) {
		
		if(id == null) {
			 return ReturnDto.send(100001);
		}
		
		Order order = orderService.findById(id);
		
		if(order == null) {
			return ReturnDto.send(100013);
		}
		if(order.getPayState().equals("1")){
			return ReturnDto.send(100021);
		}
		
		String orderType = order.getOrderType();
		if(orderType == null) {
			return ReturnDto.send(-1);
		}
		
		if("1".equals(orderType)) {
			//套餐
			SetMeal setMeal = setMealService.findById(String.valueOf(order.getCommdityID()));
			if(setMeal == null) {
				return ReturnDto.send(100015);
			}
			int number = setMeal.getNumber();
			if("1".equals(setMeal.getType())) {
				number = number * 1;//日
			}else if("2".equals(setMeal.getType())) {
				number = number * 30;//月
			}else if("3".equals(setMeal.getType())) {
				number = number * 365;//年
			}else {
				return ReturnDto.send(100015);
			}
			
			SetMealPurchase setMealPurchase =  setMealPurchaseService.save(order.getUserID(), String.valueOf(number),setMeal.getProcedures(),setMeal.getId());
			if(setMealPurchase == null) {
				return ReturnDto.send(100015);
			}
			
		}else if("2".equals(orderType)) {
			
			RechargeList rechargeList =  rechargeListService.findByID(Long.valueOf(order.getCommdityID()));
			if(rechargeList == null) {
				return ReturnDto.send(100017);
			}
			RechargeUserDetailed rechargeUserDetailed = new RechargeUserDetailed();
			rechargeUserDetailed.setFee(order.getPrice());
			rechargeUserDetailed.setUserID(order.getUserID());
			rechargeUserDetailed.setRemarks("充值");
			rechargeUserDetailed =  rechargeUserDetailedService.save(rechargeUserDetailed);
			if(rechargeUserDetailed == null) {
				return ReturnDto.send(100015);
			}
		}else {
			//return ReturnDto.send(-1);
		}
		
		order.setPayState("1");
		order = orderService.save(order);
		if(order == null) {
			return ReturnDto.send(100015);
		}
		
		return ReturnDto.send(true);
	}
	
	//商户订单
	@RequestMapping("/merchant/order/statistics")
	@ResponseBody
	public ReturnDto merchantOrderStatistics(String userID) {
		
		StatisticsDto statisticsDto = null;
		if(userID != null) {
			statisticsDto = orderService.statistics(userID);
		}else {
			statisticsDto = orderService.adminStatistics();
		}
		
		return ReturnDto.send(statisticsDto);
	}
	
	@RequestMapping("/merchant/order/dataList")
	@ResponseBody
	public ReturnDto merchantOrderDataList(String userID,int pageNum,String startDate,String endDate,
			String type,String payType,String systemID,String outTradeNo) {
		
		if(pageNum == 0) {
			return ReturnDto.send(100001);
		}
		
		return ReturnDto.send(orderService.setMealList(pageNum, type, startDate, endDate, "0",payType,userID,systemID,outTradeNo));
	}
	
	
	
	@RequestMapping("/registerCode/list")
	@ResponseBody
	public ReturnDto registerCodeList(int pageNum,String userName,String state) {
		
		if(pageNum == 0) {
			return ReturnDto.send(100001);
		}
		
		return ReturnDto.send(registerCodeService.page(pageNum, userName, state));
	}
	
	@RequestMapping("/registerCode/save")
	@ResponseBody
	public ReturnDto registerCodeSave() {
		
		RegisterCode registerCode = new RegisterCode();
		registerCode.setCode(RandomUtil.getRandomNumber(8));
		registerCode.setState("0");
		registerCode = registerCodeService.save(registerCode);
		if(registerCode == null) {
			return ReturnDto.send(100005);
		}
		
		return ReturnDto.send(registerCode);
	}
	
	@RequestMapping("/registerCode/delete")
	@ResponseBody
	public ReturnDto registerCodeDelete(Long id) {
		
		if(id == null) {
			return ReturnDto.send(100001);
		}
		
		registerCodeService.delete(id);
		
		return ReturnDto.send(true);
	}
	
	/**
	 * 新增用户
	 * @param user
	 * @return
	 */
	@RequestMapping("/merchant/register/save")
	@ResponseBody
	public ReturnDto merchantRegisterSave(User user,String code) {
		
		if(user == null || user.getUserName() == null || user.getPassword() == null || code == null) {
			return ReturnDto.send(100001);
		}
		if(userService.findByName(user.getUserName()) != null) {
			return ReturnDto.send(100014);
		}
		
		RegisterCode registerCode =  registerCodeService.findByCode(code);
		if(registerCode == null || registerCode.getState().equals("1")) {
			return ReturnDto.send(100023);
		}
		
		user.setPassword(MD5.MD5Encode(user.getPassword()));
		user.setUid(SysUtil.generalPK());
		user.setToken(SysUtil.generalPK());
		user.setState("0");
		user.setType("merchant");
		user = userService.save(user);
		
		if(user == null) {
			return ReturnDto.send(100005);
		}
		
		registerCode.setState("1");
		registerCode.setUserID(user.getId());
		registerCode.setUserName(user.getUserName());
		registerCodeService.save(registerCode);
		
		return ReturnDto.send(true);
	}
	
	
	//清空二维码
	@RequestMapping("/clear/setMeatQrcode")
	@ResponseBody
	public ReturnDto clearSetMeatQrcode(String id) {
		
		if(id == null) {
			return ReturnDto.send(100001);
		}
		
		setMealQrcodeService.deleteByAll(id);
		
		
		return ReturnDto.send(true);
	}
	
	@RequestMapping("/clear/rechargeQrcode")
	@ResponseBody
	public ReturnDto clearRechargeQrcode(String id) {
		
		if(id == null) {
			return ReturnDto.send(100001);
		}
		
		rechargeQrcodeService.deleteByAll(id);
		
		
		return ReturnDto.send(true);
	}
	
	@RequestMapping("/setMeat/findByQrcodeList")
	@ResponseBody
	public ReturnDto setMeatFindByQrcodeList(String setMealID) {
		
		if(setMealID == null) {
			return ReturnDto.send(100001);
		}
		
		List<SetMealQrcode> list =  setMealQrcodeService.findByQrcodeList(setMealID);
		
		return ReturnDto.send(list);
	}
	
	@RequestMapping("/recharge/findByQrcodeList")
	@ResponseBody
	public ReturnDto rechargeFindByQrcodeList(String rechargeID) {
		
		if(rechargeID == null) {
			return ReturnDto.send(100001);
		}
		
		List<RechargeQrcode> list =  rechargeQrcodeService.findByQrcodeList(rechargeID);
		
		return ReturnDto.send(list);
	}
	
	@RequestMapping("/setMeat/deleteQrcode")
	@ResponseBody
	public ReturnDto setMeatDeleteQrcode(String setMealID) {
		
		if(setMealID == null) {
			return ReturnDto.send(100001);
		}
		
		setMealQrcodeService.delete(setMealID);
		
		return ReturnDto.send(true);
	}
	
	@RequestMapping("/recharge/deleteQrcode")
	@ResponseBody
	public ReturnDto rechargeDeleteQrcode(String rechargeID) {
		
		if(rechargeID == null) {
			return ReturnDto.send(100001);
		}
		
		rechargeQrcodeService.delete(rechargeID);
		
		return ReturnDto.send(true);
	}
	
	@RequestMapping("/test/deleteQrcode")
	@ResponseBody
	public ReturnDto testDeleteQrcode(Long id) {
		
		if(id == null) {
			return ReturnDto.send(100001);
		}
		
		testQrcodeService.delete(id);
		
		return ReturnDto.send(true);
	}
	
	@RequestMapping("/test/findByPage")
	@ResponseBody
	public ReturnDto testFindByPage(int pageNum) {
		
		if(pageNum == 0) {
			return ReturnDto.send(100001);
		}
		
		return ReturnDto.send(testQrcodeService.page(pageNum));
	}
	
	@RequestMapping("/merchant/order/retransmission")
	@ResponseBody
	public ReturnDto merchantOrderRetransmission(Long orderID,HttpServletRequest request) {
		
		if(orderID == null) {
			return ReturnDto.send(100001);
		}
		
		Order order = orderService.findById(orderID);
		
		if(order == null) {
			return ReturnDto.send(100021);
		}
		User user = userService.findById(order.getUserID());
		
		Map<String, String> res = new HashMap<>();
		res.put("outTradeNo", order.getOutTradeNo());
		res.put("price", String.valueOf(order.getPrice()));
		res.put("type", order.getType());
		res.put("uid", order.getUid());
		res.put("payState", "SUCCESS");
		res.put("payTime", order.getPayTime());
		res.put("nonceStr", SysUtil.generalPK());
		res.put("sign", AsciiOrder.sign(res, user.getToken()));
		
			
		String req = HttpClientUtils.sendPost(order.getNotifyUrl(),com.alibaba.fastjson.JSONObject.toJSONString(res));
		
		if("SUCCESS".equals(req)) {
			order.setPayState("1");
			order.setNotifyState("1");
			order.setNotifyTime(DateUtil.getStringDateTime());
			orderService.save(order);
			return ReturnDto.send(true);
		}else {
			order.setErrorMsg(req);
			orderService.save(order);
			return ReturnDto.send(10005,req);
		}
	}
	
	@RequestMapping("/test/saveQrcode")
	@ResponseBody
	public ReturnDto testSaveQrcode(HttpServletRequest request) {
		
		String dataJson = request.getParameter("dataJson");
		if(dataJson == null) {
			return ReturnDto.send(100001);
		}
		
		JSONArray jsonArray = JSONArray.parseArray(dataJson);
		
		if(jsonArray == null) {
			return ReturnDto.send(100009);
		}
		
		for (int i = 0; i < jsonArray.size(); i++) {
			
			JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(jsonArray.get(i)));
			
			String qrcodeUrl= jsonObject.getString("qrcodeUrl");
			String type = jsonObject.getString("type");
			String fee = jsonObject.getString("fee");
			
			if(qrcodeUrl == null || type == null) {
				return ReturnDto.send(100009);
			}
			if(fee == null) {
				fee = "0";
			}
			
			TestQrcode qrcode = new TestQrcode();
			qrcode.setPrice(Integer.valueOf(fee));
			qrcode.setType(type);
			qrcode.setQrcode(qrcodeUrl);
			qrcode = testQrcodeService.save(qrcode);
			if(qrcode == null) {
				return ReturnDto.send(100005);
			}
			
		}
		return ReturnDto.send(true);
	}
	
	
	
}
	
	
