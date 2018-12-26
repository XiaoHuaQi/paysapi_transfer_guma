package com.zixu.paysapi.jpa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zixu.paysapi.jpa.dao.RechargeQrcodeDao;
import com.zixu.paysapi.jpa.entity.RechargeQrcode;
import com.zixu.paysapi.jpa.service.RechargeQrcodeService;

@Service
@Transactional
public class RechargeQrcodeServiceImpl implements RechargeQrcodeService {

	@Autowired
	private RechargeQrcodeDao dao;
	
	@Override
	public RechargeQrcode save(RechargeQrcode rechargeQrcode) {
		
		RechargeQrcode oldQrcode = findByFee(rechargeQrcode.getPrice(), rechargeQrcode.getType());
		if(oldQrcode != null) {
			rechargeQrcode.setId(oldQrcode.getId());
		}
		
		return dao.save(rechargeQrcode);
	}

	@Override
	public List<RechargeQrcode> findByFee(String type,String rechargeID) {
		
		String sql = "SELECT * FROM com_zixu_recharge_qrcode WHERE  type = ? and rechargeID = ? order by price desc ";
		
		return dao.nativeFind(RechargeQrcode.class, sql, type, rechargeID);
	}

	@Override
	public int findByRechargeIdNumber(Long id, String type) {
		
		String sql = "select * from com_zixu_recharge_qrcode where rechargeID = ? and type = ?";
		
		int count = dao.getCount(sql,id,type);
		return count;
		
	}

	@Override
	public RechargeQrcode findByFee(int fee, String type) {
		
		String sql = "SELECT * FROM com_zixu_recharge_qrcode WHERE price = ? and type = ? ";
		
		return dao.nativeFindUnique(RechargeQrcode.class, sql, fee, type);
	}

	@Override
	public void delete(String id) {
		
		String sql = "delete FROM com_zixu_recharge_qrcode WHERE id = ?";
		
		dao.nativeExecuteUpdate(sql,id);
	}

	@Override
	public List<RechargeQrcode> findByQrcodeList(String rechargeID) {
		
		String sql = "SELECT * FROM com_zixu_recharge_qrcode WHERE  rechargeID = ? order by price desc ";
		
		return dao.nativeFind(RechargeQrcode.class, sql,rechargeID);
	}

	@Override
	public void deleteByAll(String id) {
		
		String sql = "delete FROM com_zixu_recharge_qrcode WHERE rechargeID = ?";
		
		dao.nativeExecuteUpdate(sql,id);
		
	}

}
