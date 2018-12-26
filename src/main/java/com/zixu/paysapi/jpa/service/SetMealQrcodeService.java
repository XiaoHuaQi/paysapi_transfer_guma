package com.zixu.paysapi.jpa.service;

import java.util.List;

import com.zixu.paysapi.jpa.entity.SetMealQrcode;

public interface SetMealQrcodeService {

	SetMealQrcode save(SetMealQrcode setMealQrcode);
	
	SetMealQrcode findByFee(int fee,String type);
	
	List<SetMealQrcode> findByFee(String type,String setMealID);
	
	List<SetMealQrcode> findByQrcodeList(String setMealID);
	
	int findByRechargeIdNumber(String id,String type);
	
	void delete(String id);
	
	void deleteByAll(String id);
	
}
