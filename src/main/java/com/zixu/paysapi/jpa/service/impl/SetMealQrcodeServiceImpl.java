package com.zixu.paysapi.jpa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zixu.paysapi.jpa.dao.SetMealQrcodeDao;
import com.zixu.paysapi.jpa.entity.SetMealQrcode;
import com.zixu.paysapi.jpa.service.SetMealQrcodeService;

@Service
@Transactional
public class SetMealQrcodeServiceImpl implements SetMealQrcodeService {

	@Autowired
	private SetMealQrcodeDao dao;
	
	@Override
	public SetMealQrcode save(SetMealQrcode setMealQrcode) {
		
		
		SetMealQrcode oldQrcode = findByFee(setMealQrcode.getPrice(), setMealQrcode.getType());
		if(oldQrcode != null) {
			setMealQrcode.setId(oldQrcode.getId());
		}
		
		return dao.save(setMealQrcode);
	}

	@Override
	public SetMealQrcode findByFee(int fee,String type) {
		
		String sql = "SELECT * FROM com_zixu_set_meal_qrcode WHERE price = ? and type = ?";
		
		return dao.nativeFindUnique(SetMealQrcode.class, sql, fee, type);
	}

	@Override
	public int findByRechargeIdNumber(String id, String type) {
		
		String sql = "select * from com_zixu_set_meal_qrcode where setMealID = ? and type = ?";
		
		int count = dao.getCount(sql,id,type);
		
		return count;
		
	}

	@Override
	public List<SetMealQrcode> findByFee( String type, String setMealID) {
		
		String sql = "SELECT * FROM com_zixu_set_meal_qrcode WHERE type = ? and setMealID = ? order by price desc ";
		
		return dao.nativeFind(SetMealQrcode.class, sql,type,setMealID);
	}

	@Override
	public void delete(String id) {
		
		String sql = "delete from  com_zixu_set_meal_qrcode where id = ? ";
		dao.nativeExecuteUpdate(sql,id);
	}

	@Override
	public List<SetMealQrcode> findByQrcodeList(String setMealID) {
		String sql = "SELECT * FROM com_zixu_set_meal_qrcode WHERE setMealID = ? order by price desc ";
		
		return dao.nativeFind(SetMealQrcode.class, sql,setMealID);
	}

	@Override
	public void deleteByAll(String id) {
		String sql = "delete from  com_zixu_set_meal_qrcode where setMealID = ? ";
		dao.nativeExecuteUpdate(sql,id);
		
	}
	
}
