package com.zixu.paysapi.jpa.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zixu.paysapi.jpa.dao.ConfigDao;
import com.zixu.paysapi.jpa.entity.User;
import com.zixu.paysapi.jpa.service.ConfigService;
import com.zixu.paysapi.util.SysUtil;
import com.zixuapp.redis.Config;
import com.zixuapp.redis.RedisOperationManager;

import net.sf.json.JSONObject;

@Service
@Transactional
public class ConfigServiceImpl implements ConfigService{

	@Autowired
	private ConfigDao dao;
	
	private static String redisKeyID = "mysql_config_userID:";
	
	@Override
	public com.zixu.paysapi.jpa.entity.Config save(com.zixu.paysapi.jpa.entity.Config config) {
		
		com.zixu.paysapi.jpa.entity.Config oldConfig = findById(config.getUserID());
		if(oldConfig != null) {
			config.setId(oldConfig.getId());
		}else {
			config.setId(SysUtil.generalPK());
		}
		config = dao.save(config);
		if(config != null) {
			RedisOperationManager.del(redisKeyID+config.getUserID());
		}
		return config;
	}

	@Override
	public com.zixu.paysapi.jpa.entity.Config findById(String id) {
		String redisData = RedisOperationManager.getString(redisKeyID+id);
		if(redisData != null) {
			return (com.zixu.paysapi.jpa.entity.Config) JSONObject.toBean(JSONObject.fromObject(redisData),com.zixu.paysapi.jpa.entity.Config.class);
		}
		String sql = "select * from com_zixu_config where userID = ?";
		com.zixu.paysapi.jpa.entity.Config user = dao.nativeFindUnique(com.zixu.paysapi.jpa.entity.Config.class, sql,id);
		if(user != null) {
			RedisOperationManager.setString(redisKeyID+id, com.alibaba.fastjson.JSONObject.toJSONString(user), Integer.valueOf(new Config().get("expires")));
		}
		
		return user;
	}
	
	
}
