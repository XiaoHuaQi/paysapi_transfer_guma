package com.zixu.paysapi.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
public class PageController {
	
	@RequestMapping("admin")
	public String admin() {
		return "/WEB-INF/view/main/admin/index";
	}
	
	
	@RequestMapping("merchant")
	public String merchant() {
		return "/WEB-INF/view/main/merchant/index";
	}
	
}
