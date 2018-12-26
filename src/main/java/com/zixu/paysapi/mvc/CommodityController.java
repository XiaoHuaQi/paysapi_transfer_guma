package com.zixu.paysapi.mvc;



import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.zookeeper.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zixu.paysapi.jpa.dto.ReturnDto;
import com.zixu.paysapi.jpa.entity.Commodity;
import com.zixu.paysapi.jpa.entity.Qrcode;
import com.zixu.paysapi.jpa.service.CommodityService;
import com.zixu.paysapi.jpa.service.QrcodeService;
import com.zixu.paysapi.jpa.service.UserService;
import com.zixu.paysapi.mvc.util.Page;


@RequestMapping("/user/commodity")
@Controller
public class CommodityController {
	
	@Autowired
	private CommodityService commodityService;
	
	@Autowired
	private QrcodeService qrcodeService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/save")
	@ResponseBody
	public ReturnDto save(HttpServletRequest request,String name,int fee,Long id) {
		
		if(name == null ) {
			return ReturnDto.send(100001);
		}
		
		HashMap<String, String> userInfo = (HashMap<String, String>)request.getAttribute("userInfo");
		
		Commodity commodity =  commodityService.findByName(name,userInfo.get("userID"));
		if(commodity != null) {
			if(id == null) {
				return ReturnDto.send(100006);
			}
			if(!id.equals(commodity.getId())) {
				return ReturnDto.send(100006);
			}
		}
		
		commodity = commodityService.findByFee(fee,userInfo.get("userID"));
		
		if(commodity != null) {
			if(id == null) {
				return ReturnDto.send(100008);
			}
			if(!id.equals(commodity.getId())) {
				return ReturnDto.send(100008);
			}
		}
		
		if(id != null) {
			commodity = commodityService.findById(id);
			if(commodity == null) {
				return ReturnDto.send(100007);
			}
		}
		if(commodity == null) {
			commodity = new Commodity();
		}
		commodity.setName(name);
		commodity.setUserID(userInfo.get("userID"));
		commodity.setFee(fee);
		commodity = commodityService.save(commodity);
		if(commodity == null) {
			return ReturnDto.send(100005);
		}
		return ReturnDto.send(true);
	}
	
	@RequestMapping("/page")
	@ResponseBody
	public ReturnDto page(HttpServletRequest request,int pageNum,String name) {
		
		if(pageNum == 0) {
			return ReturnDto.send(100001);
		}
		
		HashMap<String, String> userInfo = (HashMap<String, String>)request.getAttribute("userInfo");
		
		Page<Commodity> page = commodityService.findByPage(pageNum, name,userInfo.get("userID"));
		
		if(page != null) {
			List<Commodity> list = page.getList();
			for (Commodity commodity : list) {
				commodity.setWechatQrcodeNum(qrcodeService.findByCommodityID(String.valueOf(commodity.getId()), "wechat"));
				commodity.setAlipayQrcodeNum(qrcodeService.findByCommodityID(String.valueOf(commodity.getId()), "alipay"));
			}
			page.setList(list);
		}
		
		return ReturnDto.send(page);
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public ReturnDto delete(HttpServletRequest request,Long id) {
		
		if(id == 0) {
			return ReturnDto.send(100001);
		}
		
		commodityService.delete(id);
		
		return ReturnDto.send(true);
	}
	
	
	@RequestMapping("/saveQrcode")
	@ResponseBody
	public ReturnDto saveQrcode(HttpServletRequest request) {
		
		
		String dataJson = request.getParameter("dataJson");
		if(dataJson == null) {
			return ReturnDto.send(100001);
		}
		System.out.println();
		
		JSONArray jsonArray = JSONArray.parseArray(dataJson);
		
		if(jsonArray == null) {
			return ReturnDto.send(100009);
		}
		
		for (int i = 0; i < jsonArray.size(); i++) {
			
			JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(jsonArray.get(i)));
			
			
			String qrcodeUrl= jsonObject.getString("qrcodeUrl");
			String type = jsonObject.getString("type");
			String commodityID = jsonObject.getString("commodityID");
			String fee = jsonObject.getString("fee");
			
			if(qrcodeUrl == null || type == null || commodityID == null) {
				return ReturnDto.send(100009);
			}
			if(fee == null) {
				fee = "0";
			}
			
			HashMap<String, String> userInfo = (HashMap<String, String>)request.getAttribute("userInfo");
			
			Qrcode qrcode = new Qrcode();
			qrcode.setCommodityID(commodityID);
			qrcode.setFee(Integer.valueOf(fee));
			qrcode.setType(type);
			qrcode.setUrl(qrcodeUrl);
			qrcode.setUserID(userInfo.get("userID"));
			qrcode = qrcodeService.save(qrcode);
			if(qrcode == null) {
				return ReturnDto.send(100005);
			}
			
		}
		
		return ReturnDto.send(true);
	}
	
	@RequestMapping("/findByFee")
	@ResponseBody
	public ReturnDto findByFee(HttpServletRequest request,int fee,String type) {
		if(type == null) {
			return ReturnDto.send(100001);
		}
		HashMap<String, String> userInfo = (HashMap<String, String>)request.getAttribute("userInfo");
		
		Qrcode qrcode = qrcodeService.findByFee(fee, userInfo.get("userID"),type);
		
		if(qrcode == null) {
			return ReturnDto.send("true");
		}
		return ReturnDto.send("false");
	}
	
	@RequestMapping("/findByQrcodeList")
	@ResponseBody
	public ReturnDto findByQrcodeList(String commodityID,HttpServletRequest request) {
		
		if(commodityID == null) {
			return ReturnDto.send(100001);
		}
		
		HashMap<String, String> userInfo = (HashMap<String, String>)request.getAttribute("userInfo");
		
		List<Qrcode> list =  qrcodeService.findByQrcodeList(userInfo.get("userID"), commodityID);
		
		return ReturnDto.send(list);
	}
	
	
	@RequestMapping("/deleteQrcode")
	@ResponseBody
	public ReturnDto deleteQrcode(Long commodityID,HttpServletRequest request) {
		
		if(commodityID == null) {
			return ReturnDto.send(100001);
		}
		
		qrcodeService.delete(commodityID);
		
		return ReturnDto.send(true);
	}
	
	
	
}
