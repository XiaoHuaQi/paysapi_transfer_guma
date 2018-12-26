package com.zixu.paysapi.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.zixu.paysapi.jpa.dto.ReturnDto;
import com.zixuapp.redis.RedisOperationManager;

/**
 * Servlet Filter implementation class MainFiter
 */
@WebFilter(filterName="AdminFilter",urlPatterns="/admin/*")
public class AdminFilter implements Filter {

    /**
     * Default constructor. 
     */
    public AdminFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest)request;
		String url = req.getRequestURI();
		if(exclude(url)) {
			chain.doFilter(request, response);
			return;
		}
		
		//在session里面拿
		HashMap<String, String> userInfo = (HashMap<String, String>) req.getSession().getAttribute("userInfo");
		if(userInfo != null) {
			if("admin".equals(userInfo.get("type"))) {
				req.setAttribute("userInfo", userInfo);
				chain.doFilter(request, response);
				return;
			}
		}
		
		//在参数里面拿
		String token = req.getParameter("token");
		if(token != null) {
			userInfo = RedisOperationManager.getMap("user_login_token:"+token);
			if(userInfo != null) {
				if("admin".equals(userInfo.get("type"))) {
					req.setAttribute("userInfo", userInfo);
					chain.doFilter(request, response);
					return;
				}
			}
		}
		
		//在cookie拿
		Cookie[] cookies = req.getCookies();
		if(cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals("user_login_token")) {
					userInfo = RedisOperationManager.getMap("user_login_token:"+cookie.getValue());
					if(userInfo != null) {
						if("admin".equals(userInfo.get("type"))) {
							req.setAttribute("userInfo", userInfo);
							chain.doFilter(request, response);
							return;
						}
					}
					break;
				}
			}
		}
		
		
		if(req.getMethod().equals("POST")) {
			PrintWriter writer =  response.getWriter();
			writer.write(JSONObject.toJSONString(ReturnDto.send(100004)));
			writer.close();
			return;
		}
		
		HttpServletResponse res = (HttpServletResponse)response;
		res.sendRedirect("/user/loginPage");
	}

	public boolean exclude(String url) {
		
		String[] urls = new String[] {"/admin/merchant/register/save"};
		
		if(Arrays.binarySearch(urls, url) < 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}
	
}
