package com.zixu.paysapi.jpa.service;

import java.util.List;

import com.zixu.paysapi.jpa.entity.Qrcode;

public interface QrcodeService {
	
	Qrcode save(Qrcode qrcode);
	
	Qrcode findByFee(int fee,String userID,String type);
	
	int findByCommodityID(String commodityID,String type);
	
	List<Qrcode> findByQrcode(int fee,String userID,String type,String commodityID);
	
	List<Qrcode> findByQrcodeList(String userID,String commodityID);

	void delete(Long id);
	
	void deleteByAll(String id);
	
	Qrcode findById(Long id);
}
