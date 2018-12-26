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
import com.zixu.paysapi.jpa.dto.StatisticsDto;
import com.zixu.paysapi.jpa.entity.ChangeDetail;
import com.zixu.paysapi.jpa.entity.Order;
import com.zixu.paysapi.jpa.entity.User;
import com.zixu.paysapi.jpa.service.ChangeDetailService;
import com.zixu.paysapi.jpa.service.OrderService;
import com.zixu.paysapi.jpa.service.UserService;
import com.zixu.paysapi.mvc.util.HttpClientUtils;
import com.zixu.paysapi.mvc.util.Page;
import com.zixu.paysapi.util.AsciiOrder;
import com.zixu.paysapi.util.DateUtil;
import com.zixu.paysapi.util.SysUtil;

@RequestMapping("/user/orderList")
@Controller
public class OrderListController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ChangeDetailService changeDetailService;
	
	@Autowired
	private UserService userService;
	
	//无匹配
	@RequestMapping("/five/matching")
	@ResponseBody
	public ReturnDto fiveMatching(HttpServletRequest request,int pageNum,String type,String startDate,String endDate,int fee) {
		
		if(pageNum == 0) {
			return ReturnDto.send(100001);
		}
		
		HashMap<String, String> userInfo = (HashMap<String, String>)request.getAttribute("userInfo");
		
		Page<Order> order = orderService.fiveMatching(pageNum,type,userInfo.get("userID"),startDate,endDate,fee);
		
		return ReturnDto.send(order);
	}
	
	//资金变动明细
	@RequestMapping("/changeDetailList")
	@ResponseBody
	public ReturnDto changeDetailList(HttpServletRequest request,int pageNum,String startDate,String endDate) {
		
		if(pageNum == 0) {
			return ReturnDto.send(100001);
		}
		
		HashMap<String, String> userInfo = (HashMap<String, String>)request.getAttribute("userInfo");
		
		Page<ChangeDetail> changeDetail = changeDetailService.page(pageNum,userInfo.get("userID"),startDate,endDate);
		
		return ReturnDto.send(changeDetail);
	}
	
	
	//订单管理
	@RequestMapping("/list")
	@ResponseBody
	public ReturnDto list(HttpServletRequest request,int pageNum,String startDate,String endDate,String type,String outTradeNo,String systemID) {
		
		if(pageNum == 0) {
			return ReturnDto.send(100001);
		}
		
		HashMap<String, String> userInfo = (HashMap<String, String>)request.getAttribute("userInfo");
		
		Page<Order> changeDetail = orderService.page(pageNum,type,userInfo.get("userID"),startDate,endDate,outTradeNo,systemID);
		
		return ReturnDto.send(changeDetail);
	}
	
	//首页
	@RequestMapping("/index/data")
	@ResponseBody
	public ReturnDto indexData(HttpServletRequest request) {
		
		HashMap<String, String> userInfo = (HashMap<String, String>)request.getAttribute("userInfo");
		
		
		String userID = userInfo.get("userID");
		
		List<Order> list = orderService.indexData(userInfo.get("userID"));
		
		StatisticsDto statisticsDto = orderService.statistics(userID);
		
		Map<String, Object> res = new HashMap<>();
		res.put("statistics", statisticsDto);
		res.put("list", list);
		
		return ReturnDto.send(res);
	}
	
	@RequestMapping("/retransmission")
	@ResponseBody
	public ReturnDto retransmission(Long orderID,HttpServletRequest request) {
		
		if(orderID == null) {
			return ReturnDto.send(100001);
		}
		
		Order order = orderService.findById(orderID);
		
		if(order == null) {
			return ReturnDto.send(100021);
		}
		User user = userService.findById(order.getUserID());
		
		HashMap<String, String> userInfo = (HashMap<String, String>)request.getAttribute("userInfo");
		String userID = userInfo.get("userID");
		if(!userID.equals(order.getUserID())) {
			return ReturnDto.send(100025);
		}
		
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
	
}
