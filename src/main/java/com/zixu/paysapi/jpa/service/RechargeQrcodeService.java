package com.zixu.paysapi.jpa.service;

import java.util.List;

import com.zixu.paysapi.jpa.entity.RechargeQrcode;

public interface RechargeQrcodeService {

	RechargeQrcode save(RechargeQrcode RechargeQrcode);
	
	List<RechargeQrcode> findByFee(String type,String rechargeID);
	
	List<RechargeQrcode> findByQrcodeList(String rechargeID);
	
	RechargeQrcode findByFee(int fee,String type);
	
	int findByRechargeIdNumber(Long id,String type);
	
	void delete(String id);
	
	void deleteByAll(String id);
	
}
