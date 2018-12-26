package com.zixu.paysapi.mvc;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import com.zixu.paysapi.jpa.dto.ReturnDto;
import com.zixu.paysapi.jpa.entity.Qrcode;
import com.zixu.paysapi.jpa.service.QrcodeService;
import com.zixu.paysapi.mvc.util.AipOcrExample;
import com.zixu.paysapi.util.FileUtil;

@RequestMapping("/user/file")
@Controller
public class FileUploadController {
	
	
	@Autowired
	private QrcodeService qrcodeService;

	@RequestMapping("/upload")
	@ResponseBody
	public ReturnDto upload(MultipartFile file, HttpServletRequest request, HttpServletResponse response)  {

		HashMap<String, String> userInfo = (HashMap<String, String>)request.getAttribute("userInfo");
		if(userInfo == null) {
			return ReturnDto.send(100004);
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
	        
	        JSONObject res = AipOcrExample.getAipOcr().basicGeneralUrl(url, options);
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
	        	
	        	Qrcode qrcode = qrcodeService.findByFee(fee, userInfo.get("userID"),resMap.get("type").toString());
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
}
