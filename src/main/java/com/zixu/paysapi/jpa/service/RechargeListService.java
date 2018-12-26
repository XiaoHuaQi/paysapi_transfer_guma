package com.zixu.paysapi.jpa.service;

import java.util.List;

import com.zixu.paysapi.jpa.entity.RechargeList;

public interface RechargeListService {
	
	List<RechargeList> findAll();
	
	RechargeList findByID(Long id);
	
	RechargeList save(RechargeList rechargeList);
	
	void delete(Long id);
	
}
