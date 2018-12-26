package com.zixu.paysapi.jpa.service;

import java.util.List;

import com.zixu.paysapi.jpa.entity.TestQrcode;
import com.zixu.paysapi.mvc.util.Page;

public interface TestQrcodeService {
	
	TestQrcode save(TestQrcode testQrcode);
	
	Page<TestQrcode> page(int pageNum);
	
	List<TestQrcode> findByList(String type);
	
	TestQrcode findByFee(String type,int fee);
	
	void delete(Long id);
	
}
