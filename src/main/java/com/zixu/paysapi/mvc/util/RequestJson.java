package com.zixu.paysapi.mvc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;


public class RequestJson {
	
	public static String  returnJson(HttpServletRequest request) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		
		return sb.toString();
	}
	
}
