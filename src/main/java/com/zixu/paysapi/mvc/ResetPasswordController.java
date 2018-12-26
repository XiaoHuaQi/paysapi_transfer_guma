package com.zixu.paysapi.mvc;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zixu.paysapi.jpa.dto.ReturnDto;
import com.zixu.paysapi.jpa.entity.User;
import com.zixu.paysapi.jpa.service.UserService;
import com.zixu.paysapi.util.MD5;
import com.zixu.paysapi.util.SysUtil;
import com.zixuapp.redis.Config;
import com.zixuapp.redis.RedisOperationManager;

@RequestMapping("/resetPassword")
@Controller
public class ResetPasswordController {
	
	@Autowired
	private UserService userService;
	
	
	private static final String ALIDM_SMTP_HOST = "smtpdm.aliyun.com";
    private static final String ALIDM_SMTP_PORT = "80";
    
    private static final String REDIS_KEY = "resetPassword_token:";
    
    private static final String URL = "https://www.wmpayer.com/resetPassword.htm?token=";
	
	
	public static Map<String, Object> sendMail(String mail,String token) throws UnsupportedEncodingException{
		
		Map<String, Object> map = new HashMap<>();
		
		final Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", ALIDM_SMTP_HOST);
        props.put("mail.smtp.port", ALIDM_SMTP_PORT);
        
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.port", "465");
        
        props.put("mail.user", "service@wmpayer.com");
        props.put("mail.password", "KOKO23wmkjJIJI");
        
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        
        Session mailSession = Session.getInstance(props, authenticator);
        MimeMessage message = new MimeMessage(mailSession);
        
        
        String emailMsg = mailStr();
        
        emailMsg = emailMsg.replaceAll("userName", mail);
        emailMsg = emailMsg.replace("URL", URL+token);
        try {
	        InternetAddress from = new InternetAddress("service@wmpayer.com", "WMpayer");
	        message.setFrom(from);
	       
	        InternetAddress to = new InternetAddress(mail);
	        message.setRecipient(MimeMessage.RecipientType.TO, to);
	        message.setSubject("WMpayer密码找回");
	        message.setContent(emailMsg, "text/html;charset=UTF-8");
	        Transport.send(message);
        }
        catch (MessagingException e) {
            String err = e.getMessage();
            map.put("error", false);
            map.put("errorMsg", err);
            return map;
        }
        map.put("error", true);
		
		return map;
	}
	
	
	@RequestMapping("/submissions")
	@ResponseBody
	public ReturnDto submissions(String userName) {
		
		if(userName == null) {
			return ReturnDto.send(100001);
		}
		
		User user = userService.findByName(userName);
		
		if(user == null) {
			return ReturnDto.send(100002);
		}
		
		String token = SysUtil.generalPK();
		
		boolean state  = RedisOperationManager.setString(REDIS_KEY+token, userName, 7200);
		if(!state){
			return ReturnDto.send(100005);
		}
		
		try {
			Map<String, Object> map = sendMail(userName,token );
			
			if(Boolean.valueOf(map.get("error").toString())) {
				return ReturnDto.send(true);
			}else{
				return ReturnDto.send(100005,map.get("errorMsg").toString());
			}
			
		} catch (UnsupportedEncodingException e) {
			return ReturnDto.send(100005,"发送邮件异常");
		}
	}
	
	@RequestMapping("/updatePassword")
	@ResponseBody
	public ReturnDto updatePassword(String userName,String password,String token) {
		
		if(token == null || userName == null || password == null) {
			return ReturnDto.send(100001);
		}
		
		String tokenData = RedisOperationManager.getString(REDIS_KEY+token);
		
		if(tokenData == null) {
			return ReturnDto.send(100024);
		}
		if(!tokenData.equals(userName)) {
			return ReturnDto.send(100024);
		}
		

		User user = userService.findByName(userName);
		
		if(user == null) {
			return ReturnDto.send(100005);
		}
		
		user.setPassword(MD5.MD5Encode(password));
		
		user = userService.save(user);
		
		if(user == null) {
			return ReturnDto.send(100005);
		}
		
		return ReturnDto.send(true);
		
		
	}
	
	
	
	public static String mailStr() {
		
		
		return "<html><head>\r\n" + 
				"<base target=\"_blank\">\r\n" + 
				"<style type=\"text/css\">\r\n" + 
				"::-webkit-scrollbar{ display: none; }\r\n" + 
				"</style>\r\n" + 
				"<style id=\"cloudAttachStyle\" type=\"text/css\">\r\n" + 
				"#divNeteaseBigAttach, #divNeteaseBigAttach_bak{display:none;}\r\n" + 
				"</style>\r\n" + 
				"\r\n" + 
				"</head>\r\n" + 
				"<body tabindex=\"0\" role=\"listitem\">\r\n" + 
				"<table width=\"700\" border=\"0\" align=\"center\" cellspacing=\"0\" style=\"width:700px;\"><tbody><tr><td>\r\n" + 
				"<div style=\"width:700px;margin:0 auto;border-bottom:1px solid #ccc;margin-bottom:30px;\">\r\n" + 
				"  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"700\" height=\"39\" style=\"font:12px Tahoma, Arial, 宋体;\">\r\n" + 
				"    <tbody><tr>\r\n" + 
				"      <td width=\"210\"><a target=\"_blank\" href=\"https://www.wmpayer.com/\"><img src=\"http://47.107.86.21/logo_wm.jpg\" width=\"128\" height=\"39\" border=\"0\" alt=\"WMpayer\"></a></td>\r\n" + 
				"      <td width=\"490\" align=\"right\" valign=\"bottom\" style=\"padding-bottom:10px;\"><a target=\"_blank\" style=\"color:#07f;text-decoration:none;font-size:12px;\" href=\"https://www.wmpayer.com/user/loginPage\">登录</a> | <a target=\"_blank\" style=\"color:#07f;text-decoration:none;padding-right:5px;font-size:12px;\" href=\"https://www.wmpayer.com/\">帮助中心</a></td>\r\n" + 
				"    </tr>\r\n" + 
				"  </tbody></table>\r\n" + 
				"</div>\r\n" + 
				"<div style=\"width:680px;padding:0 10px;margin:0 auto;\">\r\n" + 
				"  <div style=\"line-height:1.5;font-size:14px;margin-bottom:25px;color:#4d4d4d;\"><strong style=\"display:block;margin-bottom:15px;\">亲爱的会员：<a href=\"mailto:userName\">userName</a></strong>\r\n" + 
				"\r\n" + 
				"        <p>您的 <span style=\"font-size: 15px; color: rgb(255,104,1)\"> WMpayer </span>账户: <span style=\"font-size: 20px;\">userName </span> 正在找回密码, 请点击下面的链接找回：</p>\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"        <div>\r\n" + 
				"        	<a href=\"URL\">URL</a>\r\n" + 
				"        </div>\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"        <p>感谢您对WMpayer的关注与支持！WMpayer努力提供更优质的个人支付接口服务。\r\n" + 
				"</p>\r\n" + 
				"    </div>\r\n" + 
				"</div>\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"<div style=\"width:700px;margin:0 auto;\">\r\n" + 
				"  <div style=\"padding:0 10px;width:285px;margin-bottom:30px;\">\r\n" + 
				"    <p style=\"padding-top:10px;border-top:1px dashed #ccc;\"><a target=\"_blank\" style=\"color:#07f;text-decoration:none;\" href=\"https://www.wmpayer.com\">更多帮助</a></p>\r\n" + 
				"  </div>\r\n" + 
				"  <div style=\"padding:10px 10px 0;border-top:1px solid #ccc;color:#999;margin-bottom:20px;line-height:1.3em;font-size:12px;\">\r\n" + 
				"    <p style=\"margin-bottom:15px;\">此为系统邮件，请勿回复<br>\r\n" + 
				"      请保管好您的邮箱，避免WMpayer账户被他人盗用</p>\r\n" + 
				"    <p>如有任何疑问，可查看 <a target=\"_blank\" style=\"color:#666;text-decoration:none;\" href=\"https://www.wmpayer.com/user_agree.htm\"><span style=\"color: rgb(255,104,1)\"> WMpayer </span>相关规则</a>，WMpayer网站访问 <a target=\"_blank\" style=\"color:#666;text-decoration:none;\" href=\"https://www.wmpayer.com\">https://www.wmpayer.com</a><br>\r\n" + 
				"      Copyright WMpayer 2017-2018 All Rights Reserved</p>\r\n" + 
				"  </div>\r\n" + 
				"</div>\r\n" + 
				"</td></tr></tbody></table>\r\n" + 
				"<div style=\"visibility:hidden\"><img src=\"https://kcart.alipay.com/web/bi.do?screen=-&amp;color=-&amp;BIProfile=gotone&amp;pg=http%3A%2F%2Fgotone.alipay.com%2Fmail.htm%3Fgotonemail%3DMEMBERPROD_CLOSE_SUCCESS%7C%D6%A7%B8%B6%B1%A6%CC%E1%D0%D1%7C0000000000000030&amp;rnd=00a04bd0ab1e1406a0efff98e9aa5f3f0003&amp;ref=-\"></div> \r\n" + 
				"\r\n" + 
				"<style type=\"text/css\">\r\n" + 
				"body{font-size:14px;font-family:arial,verdana,sans-serif;line-height:1.666;padding:0;margin:0;overflow:auto;white-space:normal;word-wrap:break-word;min-height:100px}\r\n" + 
				"td, input, button, select, body{font-family:Helvetica, \"Microsoft Yahei\", verdana}\r\n" + 
				"pre {white-space:pre-wrap;white-space:-moz-pre-wrap;white-space:-pre-wrap;white-space:-o-pre-wrap;word-wrap:break-word;width:95%}\r\n" + 
				"th,td{font-family:arial,verdana,sans-serif;line-height:1.666}\r\n" + 
				"img{ border:0}\r\n" + 
				"header,footer,section,aside,article,nav,hgroup,figure,figcaption{display:block}\r\n" + 
				"blockquote{margin-right:0px}\r\n" + 
				"</style>\r\n" + 
				"\r\n" + 
				"<style id=\"ntes_link_color\" type=\"text/css\">a,td a{color:#064977}</style>\r\n" + 
				"\r\n" + 
				"</body></html>";
	}
	
	
	@RequestMapping("/findVersion")
	@ResponseBody
	public ReturnDto findVersion() {
		return ReturnDto.send(new Config().get("version"));
	}
	
	
}
