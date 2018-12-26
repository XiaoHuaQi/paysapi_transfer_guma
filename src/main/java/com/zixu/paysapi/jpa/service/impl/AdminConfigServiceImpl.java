package com.zixu.paysapi.jpa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zixu.paysapi.jpa.dao.AdminConfigDao;
import com.zixu.paysapi.jpa.entity.AdminConfig;
import com.zixu.paysapi.jpa.service.AdminConfigService;
import com.zixuapp.redis.Config;
import com.zixuapp.redis.RedisOperationManager;

import net.sf.json.JSONObject;

@Service
@Transactional
public class AdminConfigServiceImpl implements AdminConfigService {

	private static String redisKey = "mysql_com_zixu_admin_config";
	
	@Autowired
	private AdminConfigDao dao;
	
	@Override
	public AdminConfig save(AdminConfig adminConfig) {
		
		AdminConfig oldAdminConfig = findByOne();
		if(oldAdminConfig != null) {
			adminConfig.setId(oldAdminConfig.getId());
		}
		
		RedisOperationManager.del(redisKey);
		
		return dao.save(adminConfig);
	}

	@Override
	public AdminConfig findByOne() {
		
		String json = RedisOperationManager.getString(redisKey);
		
		if(json != null) {
			return (AdminConfig)JSONObject.toBean(JSONObject.fromObject(json),AdminConfig.class);
		}
		
		List<AdminConfig> list = dao.findAll();
		if(list == null || list.size() <= 0) {
			return null;
		}
		RedisOperationManager.setString(redisKey,com.alibaba.fastjson.JSONObject.toJSONString(list.get(0)),Integer.valueOf(new Config().get("expires")));
		
		return list.get(0);
	}

}
