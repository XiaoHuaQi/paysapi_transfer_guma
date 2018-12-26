package com.zixu.paysapi.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
	
	/**
	 * 删除cookie
	 * @param request
	 * @param response
	 * @param name
	 */
	public static void deleteCookie(HttpServletRequest request,HttpServletResponse response,String name) {
		Cookie[] cookies = request.getCookies();  
        if(null != cookies) {  
            for(Cookie cookie : cookies){  
                if(cookie.getName().equals(name)){  
                    cookie.setValue(null);  
                    cookie.setMaxAge(0);
                    cookie.setPath("/");  
                    response.addCookie(cookie);  
                    break;  
                }  
            }  
        }
	}
	
	/**  
     * 将cookie封装到Map里面  
     * @param request  
     * @return  
     */  
    private static Map<String,Cookie> ReadCookieMap(HttpServletRequest request){    
        Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();  
        Cookie[] cookies = request.getCookies();  
        if(null!=cookies){  
            for(Cookie cookie : cookies){  
                cookieMap.put(cookie.getName(), cookie);  
            }  
        }  
        return cookieMap;  
    }  
    
    /**  
     * 根据名字获取cookie  
     * @param request  
     * @param name cookie名字  
     * @return  
     */  
    public static String getCookieByName(HttpServletRequest request,String name){  
        Map<String,Cookie> cookieMap = ReadCookieMap(request);  
        if(cookieMap.containsKey(name)){  
            Cookie cookie = (Cookie)cookieMap.get(name);  
            return cookie.getValue();  
        }else{  
            return null;  
        }    
    }  
    
    /**  
     * 添加cookie  
     * @param response  
     * @param name  
     * @param value  
     */  
    public static void addCookie(HttpServletResponse response,String name,String value){  
        Cookie cookie = new Cookie(name.trim(), value.trim());  
        cookie.setMaxAge(30 * 60);
        cookie.setPath("/");  
        response.addCookie(cookie);  
    }  
	
}
