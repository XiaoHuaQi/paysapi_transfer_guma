package com.zixuapp.redis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Config {
	
	private static String redisKey = "system_config_key:";
	
	private Properties prop = null;

	public Properties conn() {
		if (prop != null) {
			return prop;
		}
		Properties prop = new Properties();
		InputStream is = Config.class.getClassLoader().getResourceAsStream("system.config.properties");
		try {
			prop.load(is);
			is.close();
		} catch (IOException e) {
			return null;
		}
		this.prop = prop;
		return prop;
	}

	public String get(String k) {
		
		String getProperty = RedisOperationManager.getString(redisKey+k);
		if(getProperty != null) {
			return getProperty;
		}
		Properties prop = conn();
		if (prop == null) {
			return null;
		}
		getProperty = prop.getProperty(k);
		if(getProperty != null) {
			RedisOperationManager.setString(redisKey+k, getProperty, 7200);
		}
		return getProperty;
	}

}
