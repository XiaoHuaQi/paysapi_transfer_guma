package com.zixu.paysapi.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

public class RedisUtil {
	
	
	public static Map<String, String> dept = new HashMap<>();
	public static Map<String, String> org = new HashMap<>();
	
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	
	public void init(){
		
	}
	

}
