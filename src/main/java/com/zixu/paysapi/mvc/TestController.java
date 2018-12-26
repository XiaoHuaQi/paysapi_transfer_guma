package com.zixu.paysapi.mvc;




import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zixu.paysapi.jpa.dto.ReturnDto;
import com.zixu.paysapi.jpa.entity.Order;
import com.zixu.paysapi.jpa.entity.TestQrcode;
import com.zixu.paysapi.jpa.service.OrderService;
import com.zixu.paysapi.jpa.service.TestQrcodeService;
import com.zixu.paysapi.util.ImportExecel;
import com.zixu.paysapi.util.SysUtil;
import com.zixuapp.redis.RedisOperationManager;


@RequestMapping("/test")
@Controller
public class TestController {
	
	@Autowired
	private TestQrcodeService testQrcodeService;
	
	@Autowired
	private OrderService orderService;
	
	
	private static String redisKey = "test_qrcode_fee:";
	
	@RequestMapping("/pay")
	@ResponseBody
	public ReturnDto save(HttpServletRequest request,String type) {
		
		if(type == null) {
			return ReturnDto.send(100001);
		}
		
		List<TestQrcode> testQrcodes =  testQrcodeService.findByList(type);
		if(testQrcodes == null) {
			return ReturnDto.send(100012);
		}
		String qrcode = null;
		int price = 0;
		Long testQrcodeID = 0L;
		for (TestQrcode testQrcode : testQrcodes) {
			if(RedisOperationManager.getString(redisKey+type+testQrcode.getPrice()) == null) {
				qrcode = testQrcode.getQrcode();
				price = testQrcode.getPrice();
				testQrcodeID = testQrcode.getId();
				RedisOperationManager.setString(redisKey+type+testQrcode.getPrice(),qrcode,300);
				break;
			}
		}
		if(qrcode == null || price == 0) {
			return ReturnDto.send(100012);
		}
		
		String outTradeNo = SysUtil.generalPK();
		
		Order order = new Order();
		order.setCommdityID(String.valueOf(testQrcodeID));
		order.setOrderType("1");
		order.setType(type);
		order.setPayState("0");
		order.setPrice(price);
		order.setOutTradeNo(outTradeNo);
		order.setCommdityName("首页回调测试");
		order.setOrderType("3");
		
		order = orderService.save(order);
		if(order == null) {
			return ReturnDto.send(100015);
		}
		Map<String, Object> res = new HashMap<>();
		res.put("qrcode", qrcode);
		res.put("price", price);
		res.put("outTradeNo", outTradeNo);
		
		return ReturnDto.send(res);
	}
	
	@RequestMapping("/query")
	@ResponseBody
	public ReturnDto query(HttpServletRequest request,String outTradeNo) {
		
		if(outTradeNo == null) {
			return ReturnDto.send(100001);
		}
		
		Order order =  orderService.findByOutTradeNo(outTradeNo);
		if(order == null) {
			return ReturnDto.send(100013);
		}
		
		Map<String, Object> res = new HashMap<>();
		res.put("payState", order.getPayState());
		
		return ReturnDto.send(res);
	}
	
	
	@RequestMapping("/importExecel")
    @ResponseBody
    public ReturnDto importExecel(@RequestParam(value = "file", required = true) MultipartFile file,HttpServletRequest request,HttpServletResponse response) throws Exception {
    	
    	
    	Map<String, String> m = ImportExecel.addHotelMap();
    	InputStream inputStream = file.getInputStream();
		List<Map<String, Object>> list = ImportExecel.parseExcel(inputStream, file.getOriginalFilename(), m);
		
		System.out.println(list.size());
		return new ReturnDto(false,"表格为空");
    }
	
	
	
}
