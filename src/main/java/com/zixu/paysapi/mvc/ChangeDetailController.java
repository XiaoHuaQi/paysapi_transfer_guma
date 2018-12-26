package com.zixu.paysapi.mvc;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zixu.paysapi.jpa.dto.ReturnDto;
import com.zixu.paysapi.jpa.service.ChangeDetailService;

@RequestMapping("/user/changeDetail")
@Controller
public class ChangeDetailController {
	
	@Autowired
	private ChangeDetailService changeDetailService;
	
	@RequestMapping("/page")
	@ResponseBody
	public ReturnDto page(HttpServletRequest request,int pageNum,String endDate,String startDate) {
		
		if(pageNum == 0) {
			return ReturnDto.send(100001);
		}
		
		HashMap<String, String> userInfo = (HashMap<String, String>)request.getAttribute("userInfo");
		
		return ReturnDto.send(changeDetailService.page(pageNum, userInfo.get("userID"), startDate, endDate));
	}
	
}
