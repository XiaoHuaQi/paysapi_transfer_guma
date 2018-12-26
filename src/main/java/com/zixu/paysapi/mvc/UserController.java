package com.zixu.paysapi.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.interceptor.StaxInEndingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zixu.paysapi.jpa.dto.ReturnDto;
import com.zixu.paysapi.jpa.entity.AdminConfig;
import com.zixu.paysapi.jpa.entity.Order;
import com.zixu.paysapi.jpa.entity.RechargeQrcode;
import com.zixu.paysapi.jpa.entity.RechargeUserDetailed;
import com.zixu.paysapi.jpa.entity.SetMeal;
import com.zixu.paysapi.jpa.entity.SetMealPurchase;
import com.zixu.paysapi.jpa.entity.SetMealQrcode;
import com.zixu.paysapi.jpa.entity.User;
import com.zixu.paysapi.jpa.service.AdminConfigService;
import com.zixu.paysapi.jpa.service.ConfigService;
import com.zixu.paysapi.jpa.service.OrderService;
import com.zixu.paysapi.jpa.service.QrcodeService;
import com.zixu.paysapi.jpa.service.RechargeUserDetailedService;
import com.zixu.paysapi.jpa.service.SetMealPurchaseService;
import com.zixu.paysapi.jpa.service.SetMealQrcodeService;
import com.zixu.paysapi.jpa.service.SetMealService;
import com.zixu.paysapi.jpa.service.UserLogService;
import com.zixu.paysapi.jpa.service.UserService;
import com.zixu.paysapi.util.DateUtil;
import com.zixu.paysapi.util.MD5;
import com.zixu.paysapi.util.SysUtil;
import com.zixuapp.redis.Config;
import com.zixuapp.redis.RedisOperationManager;

@RequestMapping("/user")
@Controller
public class UserController {
	
	private static String redisKey = "pay_set_meal:";
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ConfigService configService;
	
	@Autowired
	private SetMealService setMealService;
	
	@Autowired
	private SetMealPurchaseService setMealPurchaseService;
	
	@Autowired
	private SetMealQrcodeService setMealQrcodeService;
	
	@Autowired
	private AdminConfigService adminConfigService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private RechargeUserDetailedService rechargeUserDetailedService;
	
	@Autowired
	private QrcodeService qrcodeService;
	
	@Autowired
	private UserLogService userLogService;
	
	@RequestMapping("/userInfo")
	@ResponseBody
	public ReturnDto userInfo(HttpServletRequest request) {
		
		HashMap<String, String> userInfo = (HashMap<String, String>)request.getAttribute("userInfo");
		User user = userService.findById(userInfo.get("userID"));
		
		return ReturnDto.send(user);
	}
	
	@RequestMapping("/update")
	@ResponseBody
	public ReturnDto update(HttpServletRequest request,String qqNumber,String wechat,String mobile,String account) {
		
		HashMap<String, String> userInfo = (HashMap<String, String>)request.getAttribute("userInfo");
		User user = userService.findById(userInfo.get("userID"));
		if(user == null) {
			return ReturnDto.send(100002);
		}
		user.setAccount(account);
		user.setMobile(mobile);
		user.setQqNumber(qqNumber);
		user.setWechat(wechat);
		
		user = userService.save(user);
		if(user == null) {
			return ReturnDto.send(100005);
		}
		
		return ReturnDto.send(true);
		
	}
	
	@RequestMapping("/resetToken")
	@ResponseBody
	public ReturnDto resetToken(HttpServletRequest request) {
		
		HashMap<String, String> userInfo = (HashMap<String, String>)request.getAttribute("userInfo");
		User user = userService.findById(userInfo.get("userID"));
		if(user == null) {
			return ReturnDto.send(100002);
		}
		user.setToken(SysUtil.generalPK());
		
		user = userService.save(user);
		if(user == null) {
			return ReturnDto.send(100005);
		}
		
		return ReturnDto.send(true);
		
	}
	
	
	@RequestMapping("/loginPage")
	public String loginPage() {
		
		return "/WEB-INF/view/user/login";
	}
	
	@RequestMapping("/admin")
	public String admin() {
		
		return "/WEB-INF/view/main/admin/index";
	}
	
	@RequestMapping("/login")
	@ResponseBody
	public ReturnDto login(String userName,String password,HttpServletRequest reques,HttpServletRequest request) {
		
		if(userName == null || password == null) {
			return ReturnDto.send(100001);
		}
		
		User user =  userService.findByName(userName);
		if(user == null) {
			return ReturnDto.send(100002);
		}
		
		if("1".equals(user.getState())) {
			return ReturnDto.send(100022);
		}
		
		
		if(!user.getPassword().equals(MD5.MD5Encode(password))) {
			return ReturnDto.send(100003);
		}
		
		String token = SysUtil.generalPK();
		
		HashMap<String, String> userInfo = new HashMap<>();
		userInfo.put("userID", user.getId());
		userInfo.put("name", user.getUserName());
		userInfo.put("account", user.getAccount());
		userInfo.put("uid", user.getUid());
		userInfo.put("token", token);
		userInfo.put("cookieKey", "user_login_token");
		userInfo.put("type", user.getType());
		
		userLogService.save(userName, getLocalIp(request));;
		
		boolean state = RedisOperationManager.setMap("user_login_token:"+token, userInfo, Integer.valueOf(new Config().get("expires")));
		if(!state) {
			return ReturnDto.send(100005);
		}
		reques.getSession().setAttribute("userInfo",userInfo);
		return ReturnDto.send(userInfo);
	}
	
	@RequestMapping("/updatePassword")
	@ResponseBody
	public ReturnDto updatePassword(String oldPassword,String newPassword,HttpServletRequest request) {
		
		HashMap<String, String> userInfo = (HashMap<String, String>)request.getAttribute("userInfo");
		User user = userService.findById(userInfo.get("userID"));
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
	
	@RequestMapping("/config/info")
	@ResponseBody
	public ReturnDto info(HttpServletRequest request) {
		
		HashMap<String, String> userInfo = (HashMap<String, String>)request.getAttribute("userInfo");
		
		com.zixu.paysapi.jpa.entity.Config config = configService.findById(userInfo.get("userID"));
		
		return ReturnDto.send(config);
	}
	
	@RequestMapping("/config/save")
	@ResponseBody
	public ReturnDto info(HttpServletRequest request,String overdueTime,String immediately,int minFee) {
		
		if(overdueTime == null || immediately == null) {
			return ReturnDto.send(100001);
		}
		
		HashMap<String, String> userInfo = (HashMap<String, String>)request.getAttribute("userInfo");
		
		com.zixu.paysapi.jpa.entity.Config config = new com.zixu.paysapi.jpa.entity.Config();
		config.setImmediately(immediately);
		config.setOverdueTime(Integer.valueOf(overdueTime));
		config.setUserID(userInfo.get("userID"));
		config.setMinFee(minFee);
		config = configService.save(config);
		if(config == null) {
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
		return ReturnDto.send(setMealService.findAll());
	}
	
	/**
	 * 账号到期情况
	 * @return
	 */
	@RequestMapping("/setMeal/info")
	@ResponseBody
	public ReturnDto setMealInfo(HttpServletRequest request,String id) {
		
		if(id == null) {
			return ReturnDto.send(100001);
		}
		
		return ReturnDto.send(setMealService.findById(id));
	}
	
	@RequestMapping("/setMealPurchase/pay")
	@ResponseBody
	public ReturnDto setMealPurchasePay(HttpServletRequest request, String setMealID) {
	
		HashMap<String, String> userInfo = (HashMap<String, String>)request.getAttribute("userInfo");
		
		SetMeal setMeal = setMealService.findById(setMealID);
		
		if(setMeal == null) {
			return ReturnDto.send(100016);
		}
		
		AdminConfig adminConfig = adminConfigService.findByOne();
		if(adminConfig == null) {
			return ReturnDto.send(100005);
		}
		
		Map<String, String> map = findQrcode(setMeal.getPrice(), setMealService, adminConfig.getType(), setMealQrcodeService,setMealID);
		
		if(map.get("errorCode") != null) {
			return ReturnDto.send(Integer.valueOf(map.get("errorCode")));
		}
		
		String qrcodeUrl = map.get("qrcodeUrl");
		String fee = map.get("fee");
		
		//创建订单
		
		String outTradeNo = SysUtil.generalPK();
		
		Order order = new Order();
		order.setCommdityID(setMeal.getId());
		order.setOrderType("1");
		order.setType(adminConfig.getType());
		order.setPayState("0");
		order.setPrice(Integer.valueOf(fee));
		order.setOutTradeNo(outTradeNo);
		order.setUserID(userInfo.get("userID"));
		order.setQrcodeUrl(qrcodeUrl);
		order.setCommdityName(setMeal.getTitle());
		order.setProcedures(0);
		order.setProceduresFee(0);
		order = orderService.save(order);
		if(order == null) {
			return ReturnDto.send(100005);
		}
		Map<String, String> res = new HashMap<>();
		res.put("qrcodeUrl", qrcodeUrl);
		res.put("fee", fee);
		res.put("outTradeNo", outTradeNo);
		res.put("payType", adminConfig.getType());
		
		return ReturnDto.send(res);
	}
	
	public static Map<String, String> findQrcode(int fee,SetMealService setMealService,String type,SetMealQrcodeService setMealQrcodeService,String setMealID) {
		
		Map<String, String> res = new HashMap<>();
		List<SetMealQrcode>  list = setMealQrcodeService.findByFee(type,setMealID);
		if(list == null || list.size() == 0) {
			res.put("errorCode", "100012");
			return res;
		}
		for (SetMealQrcode qrcode : list) {
			
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
			
			RedisOperationManager.setString(redisKey+"fee="+qrcode.getPrice()+"&type="+type,qrcode.getUrl(),Integer.valueOf( new Config().get("expires")));
			
			return res;
			
		}
		res.put("errorCode", "100012");
		return res;
		
		
		
		/*String qrcode = RedisOperationManager.getString(redisKey+"fee="+fee+"&type="+type);
		
		if(qrcode != null) {
			findQrcode(fee-1, setMealService, type, setMealQrcodeService);
		}
		Map<String, String> map = new HashMap<>();
		
		SetMealQrcode setMealQrcode = setMealQrcodeService.findByFee(fee,type);
		if(setMealQrcode != null) {
			map.put("qrcodeUrl", setMealQrcode.getUrl());
			map.put("fee", String.valueOf(setMealQrcode.getPrice()));
			RedisOperationManager.setString(redisKey+"fee="+fee+"&type="+type,setMealQrcode.getUrl(),Integer.valueOf(new Config().get("expires")));
		}else {
			setMealQrcode = setMealQrcodeService.findByFee(0,type);
			if(setMealQrcode != null) {
				map.put("qrcodeUrl", setMealQrcode.getUrl());
				map.put("fee", String.valueOf(fee));
				RedisOperationManager.setString(redisKey+"fee="+fee+"&type="+type,setMealQrcode.getUrl(),Integer.valueOf(new Config().get("expires")));
			
			}else {
				map.put("error", String.valueOf(100012));
			}
		}
		
		return map;*/
	}
	
	
	@RequestMapping("/setMealPurchase/query")
	@ResponseBody
	public ReturnDto setMealPurchaseQuery(HttpServletRequest request, String outTradeNo) {
		
		if(outTradeNo == null) {
			return ReturnDto.send(100001);
		}
		
		HashMap<String, String> userInfo = (HashMap<String, String>)request.getAttribute("userInfo");
		
		Order order = orderService.findByOutTradeNo(userInfo.get("userID"), outTradeNo);
		
		if(order == null) {
			return ReturnDto.send(100013);
		}
		
		if(!"1".equals(order.getPayState())) {
			return ReturnDto.send(0,true,false);
		}
		
		return ReturnDto.send(0,true,true);
	}
	
	@RequestMapping("/recharge/user/detailed")
	@ResponseBody
	public ReturnDto rechargeUserDetailed(int pageNum,String startDate,String endDate,String state,HttpServletRequest request) {
		
		HashMap<String, String> userInfo = (HashMap<String, String>)request.getAttribute("userInfo");
		
		return ReturnDto.send(rechargeUserDetailedService.page(pageNum, userInfo.get("userID"), startDate, endDate, state));
	}
	
	
	@RequestMapping("/setMealPurchase/paySetMeat")
	@ResponseBody
	public ReturnDto setMealPurchasePaySetMeat(String setMeatID,HttpServletRequest request) {
		
		if(setMeatID == null) {
			return ReturnDto.send(100001);
		}
		
		SetMeal setMeal =  setMealService.findById(setMeatID);
		if(setMeal == null) {
			return ReturnDto.send(100015);
		}
		
		
		HashMap<String, String> userInfo = (HashMap<String, String>)request.getAttribute("userInfo");
		
		String userID = userInfo.get("userID");
		
		int sum = rechargeUserDetailedService.sum(userID);
		
		if(sum < setMeal.getPrice()) {
			return ReturnDto.send(100018);
		}
		
		String outTradeNo = SysUtil.generalPK();
		
		Order order = new Order();
		order.setCommdityID(setMeal.getId());
		order.setOrderType("1");
		order.setType("balance");
		order.setPayState("0");
		order.setPrice(setMeal.getPrice());
		order.setOutTradeNo(outTradeNo);
		order.setUserID(userID);
		order.setCommdityName(setMeal.getTitle());
		
		order = orderService.save(order);
		if(order == null) {
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
		
		//新增消费明细
		RechargeUserDetailed rechargeUserDetailed = new RechargeUserDetailed();
		rechargeUserDetailed.setFee(-setMeal.getPrice());
		rechargeUserDetailed.setRemarks("购买"+setMeal.getTitle()+"套餐");
		rechargeUserDetailed.setUserID(userID);
		rechargeUserDetailed = rechargeUserDetailedService.save(rechargeUserDetailed);
		if(rechargeUserDetailed == null) {
			return ReturnDto.send(100015);
		}
		
		order.setPayState("1");
		order = orderService.save(order);
		if(order == null) {
			return ReturnDto.send(100015);
		}
		
		return ReturnDto.send(0,true,true);
	}
	
	
	@RequestMapping("/clear/qrcode")
	@ResponseBody
	public ReturnDto clearQrcode(String id) {
		
		if(id == null) {
			return ReturnDto.send(100001);
		}
		
		qrcodeService.deleteByAll(id);
		
		
		return ReturnDto.send(true);
	}
	
	 public static String getLocalIp(HttpServletRequest request) {
	        String remoteAddr = request.getRemoteAddr();
	        String forwarded = request.getHeader("X-Forwarded-For");
	        String realIp = request.getHeader("X-Real-IP");

	        String ip = null;
	        if (realIp == null) {
	            if (forwarded == null) {
	                ip = remoteAddr;
	            } else {
	                ip = remoteAddr + "/" + forwarded.split(",")[0];
	            }
	        } else {
	            if (realIp.equals(forwarded)) {
	                ip = realIp;
	            } else {
	                if(forwarded != null){
	                    forwarded = forwarded.split(",")[0];
	                }
	                ip = realIp + "/" + forwarded;
	            }
	        }
	        return ip;
	    }
	
}
