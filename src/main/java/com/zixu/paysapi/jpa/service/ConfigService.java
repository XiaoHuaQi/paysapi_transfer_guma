package com.zixu.paysapi.jpa.service;

import com.zixu.paysapi.jpa.entity.Config;

public interface ConfigService {
	
	Config save(Config config);
	
	Config findById(String id);
	
}
