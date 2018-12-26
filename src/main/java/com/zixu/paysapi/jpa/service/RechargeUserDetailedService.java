package com.zixu.paysapi.jpa.service;

import com.zixu.paysapi.jpa.dto.StatisticsDto;
import com.zixu.paysapi.jpa.entity.RechargeUserDetailed;
import com.zixu.paysapi.mvc.util.Page;

public interface RechargeUserDetailedService {
	
	int sum(String userID);
	
	int rechargeSum(String userID);
	
	int consumptionSum(String userID);
	
	Page<RechargeUserDetailed> page(int pageNum,String userID,String startDate,String endDate,String state);
	
	RechargeUserDetailed save(RechargeUserDetailed rechargeUserDetailed);
	
	StatisticsDto adminStatistics();
	
	RechargeUserDetailed findByOutTradeNo(String outTradeNo);
	
}
