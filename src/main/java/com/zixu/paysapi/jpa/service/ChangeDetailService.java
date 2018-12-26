package com.zixu.paysapi.jpa.service;

import com.zixu.paysapi.jpa.entity.ChangeDetail;
import com.zixu.paysapi.mvc.util.Page;

public interface ChangeDetailService {
	
	ChangeDetail save(String userID,int changeFee);
	
	Page<ChangeDetail> page(int pageNum, String userID, String startDate,String endDate);
	
}
