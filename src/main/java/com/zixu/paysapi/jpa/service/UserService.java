package com.zixu.paysapi.jpa.service;

import java.util.List;

import com.zixu.paysapi.jpa.dto.UserDto;
import com.zixu.paysapi.jpa.entity.User;
import com.zixu.paysapi.mvc.util.Page;

public interface UserService {
	
	User save(User user);
	
	void delete(String id);
	
	User findById(String id);
	
	User findByUid(String uid);
	
	User findByName(String userName);
	
	List<User> findAll();
	
	Page<User> findAll(int pageNum,String userName);
	
	List<UserDto> findByOrderPay();
	
}
