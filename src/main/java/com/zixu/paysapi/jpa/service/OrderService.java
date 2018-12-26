package com.zixu.paysapi.jpa.service;

import java.util.List;

import com.zixu.paysapi.jpa.dto.StatisticsDto;
import com.zixu.paysapi.jpa.entity.Order;
import com.zixu.paysapi.mvc.util.Page;

public interface OrderService {
	
	Order save(Order order);
	
	Order findByFee(int fee,String userID,String type);
	
	Order findByFee(int fee,String type);
	
	Order findById(Long id);
	
	Page<Order> fiveMatching(int pageNum,String type,String userID,String startDate,String endDate,int fee);
	
	Page<Order> page(int pageNum,String type,String userID,String startDate,String endDate,String outTradeNo,String systemID);
	
	Page<Order> setMealList(int pageNum,String type,String startDate,String endDate,String orderType,String payType,String userID,String systemID,String outTradeNo);
	
	List<Order> indexData(String userID);
	
	StatisticsDto statistics(String userID);
	
	StatisticsDto adminStatistics();
	
	StatisticsDto adminRechargeStatistics();
	
	StatisticsDto adminSetMealStatistics();
	
	Order findByOutTradeNo(String userID,String outTradeNo);
	
	Order findByOutTradeNo(String outTradeNo);
	
	Order findBySystemID(String systemID);
	
}
