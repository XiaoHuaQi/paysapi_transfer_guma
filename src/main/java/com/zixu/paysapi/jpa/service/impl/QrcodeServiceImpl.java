package com.zixu.paysapi.jpa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zixu.paysapi.jpa.dao.QrcodeDao;
import com.zixu.paysapi.jpa.entity.Qrcode;
import com.zixu.paysapi.jpa.service.QrcodeService;

@Service
@Transactional
public class QrcodeServiceImpl implements QrcodeService{

	@Autowired
	private QrcodeDao dao;
	

	@Override
	public Qrcode save(Qrcode qrcode) {
		
		Qrcode oldQrcode = findByFee(qrcode.getFee(), qrcode.getUserID(),qrcode.getType());
		if(oldQrcode != null) {
			qrcode.setId(oldQrcode.getId());
		}
		qrcode = dao.save(qrcode);
		return qrcode;
	}

	@Override
	public Qrcode findByFee(int fee,String userID,String type) {
		
		String sql = "select * from com_zixu_qrcode where fee = ? and userID = ? and type = ?";
		Qrcode qrcode =  dao.nativeFindUnique(Qrcode.class, sql, fee, userID, type);
		
		return qrcode;
	}

	@Override
	public int findByCommodityID(String commodityID,String type) {
		
		String sql = "select * from com_zixu_qrcode where commodityID = ? and type = ?";
		
		int count = dao.getCount(sql,commodityID,type);
		
		return count;
	}

	@Override
	public List<Qrcode> findByQrcode(int fee, String userID, String type,String commodityID) {
		
		String sql = "select * from com_zixu_qrcode where fee <= ? and userID = ? and type = ? and commodityID = ? order by fee desc ";
		
		return dao.nativeFind(Qrcode.class, sql,fee,userID,type,commodityID);
		
		
	}

	@Override
	public void delete(Long id) {
		dao.delete(id);
	}
	
	@Override
	public void deleteByAll(String id) {
		String sql = "delete from com_zixu_qrcode where commodityID = ?";
		dao.nativeExecuteUpdate(sql,id);
	}

	@Override
	public List<Qrcode> findByQrcodeList(String userID, String commodityID) {
		
		String sql = "SELECT * FROM com_zixu_qrcode WHERE commodityID = ? AND userID = ? order by fee desc";
		
		return dao.nativeFind(Qrcode.class, sql, commodityID, userID);
	}

	@Override
	public Qrcode findById(Long id) {
		return dao.findOne(id);
	}

	
	
}
