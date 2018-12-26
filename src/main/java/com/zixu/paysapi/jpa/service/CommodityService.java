package com.zixu.paysapi.jpa.service;

import java.util.List;

import com.zixu.paysapi.jpa.entity.Commodity;
import com.zixu.paysapi.mvc.util.Page;

public interface CommodityService {
	
	Commodity save(Commodity commodity);
	
	Commodity findById(Long id);
	
	Commodity findByName(String name,String userID);
	
	Commodity findByFee(int fee,String userID);
	
	Page<Commodity> findByPage(int pageNum,String name,String userID);
	
	void delete(Long id);
	
	List<Commodity> findByList(String userID);
}
