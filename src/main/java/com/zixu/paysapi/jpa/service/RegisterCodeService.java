package com.zixu.paysapi.jpa.service;

import com.zixu.paysapi.jpa.entity.RegisterCode;
import com.zixu.paysapi.mvc.util.Page;

public interface RegisterCodeService {
	
	RegisterCode save(RegisterCode registerCode);
	
	void delete(Long id);
	
	Page<RegisterCode> page(int pageNum,String userName,String state);
	
	RegisterCode findByCode(String code);
	
}
