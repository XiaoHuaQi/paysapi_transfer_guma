package com.zixu.paysapi.mvc.util;

import javax.servlet.http.HttpServletRequest;

public class WebUtil {
	/**
	 * 根目录
	 * @param request
	 * @return
	 */
	public static String getRootPath(HttpServletRequest request){
		StringBuffer sb = new StringBuffer("http://");
		sb.append(request.getServerName());
		if(request.getServerPort()!=80)
			sb.append(":").append(request.getServerPort());
		sb.append(request.getContextPath());		
		return sb.toString();
	}
	
	/**
	 * 本地部署根目录
	 * @param request
	 * @return
	 */
	public static String getRealPath(HttpServletRequest request){
		return request.getSession().getServletContext().getRealPath("");
	}

}
