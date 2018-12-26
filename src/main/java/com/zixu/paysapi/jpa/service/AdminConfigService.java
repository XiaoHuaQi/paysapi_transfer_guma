package com.zixu.paysapi.jpa.service;

import com.zixu.paysapi.jpa.entity.AdminConfig;

public interface AdminConfigService {
	
	AdminConfig save(AdminConfig adminConfig);
	
	AdminConfig findByOne();
	
}
