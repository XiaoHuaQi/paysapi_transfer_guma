package com.zixu.paysapi.jpa.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zixu.paysapi.jpa.dao.UserLogDao;
import com.zixu.paysapi.jpa.entity.UserLog;
import com.zixu.paysapi.jpa.service.UserLogService;
import com.zixu.paysapi.util.DateUtil;
import com.zixu.paysapi.util.SysUtil;

@Service
@Transactional
public class UserLogServiceImpl implements UserLogService {

	@Autowired
	private UserLogDao dao;
	
	@Override
	public void save(String userName, String ip) {
		
		UserLog log = new UserLog();
		log.setAccount(userName);
		log.setChangeDate(DateUtil.getStringDateTime());
		log.setLoginIp(ip);
		dao.save(log);
	}

}
