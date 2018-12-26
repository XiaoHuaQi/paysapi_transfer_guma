package com.zixu.paysapi.jpa.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zixu.paysapi.jpa.dao.SetMealPurchaseDao;
import com.zixu.paysapi.jpa.entity.SetMealPurchase;
import com.zixu.paysapi.jpa.service.SetMealPurchaseService;
import com.zixu.paysapi.mvc.util.Page;
import com.zixu.paysapi.util.DateUtil;
import com.zixu.paysapi.util.SysUtil;

@Service
@Transactional
public class SetMealPurchaseServiceImpl implements SetMealPurchaseService{
	
	@Autowired
	private SetMealPurchaseDao dao;

	@Override
	public Page<SetMealPurchase> findAll(int pageNum, String userName) {
		
		Page<SetMealPurchase> page = new Page<>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT q.id,q.userID,q.expireDate,q.procedures,u.userName,u.account,u.mobile FROM com_zixu_set_meal_purchase q LEFT JOIN com_zixu_user u ON  q.userID = u.id  ");
		if(userName != null && !userName.equals("")) {
			sql.append("where u.userName like '%"+userName+"%' ");
		}
		sql.append(" order by q.id desc ");
		
		int count = dao.getCount(sql.toString());
		if(count == 0) {
			return null;
		}
		
		String sqlPage = page.getMysqlPageSql(sql.toString(), pageNum, 30);
		List<SetMealPurchase> list = dao.nativeFind(SetMealPurchase.class, sqlPage);
		
		page.setList(list);
		page.setPageNum(pageNum);
		page.setPageSize(30);
		page.setTotalRow(count);
		page.setTotalPage(count % 30 == 0 ? count / 30 : count / 30 + 1);
		return page;
	}

	@Override
	public SetMealPurchase save(String userID,String date,int procedures,String setMealID) {
		
		BigDecimal termTime = new BigDecimal(date);
		BigDecimal ssTime = new BigDecimal((86400000l));
		
		SetMealPurchase setMealPurchase = findByUserID(userID);
		Date effectiveTime = new Date();
		if(setMealPurchase == null) {
			setMealPurchase = new SetMealPurchase();
			setMealPurchase.setUserID(userID);
			setMealPurchase.setState("0");
			setMealPurchase.setProcedures(procedures);
			setMealPurchase.setId(SysUtil.generalPK());
		}else {
			if(setMealID != null) {
				if(setMealID.equals(setMealPurchase.getSetMealID())) {
					effectiveTime = DateUtil.strToDateLong(setMealPurchase.getExpireDate());
					if(effectiveTime.getTime() < new Date().getTime()) {
						effectiveTime = new Date();
					}
				}
			}
		}
		
		BigDecimal bigEffectiveTime = new BigDecimal(effectiveTime.getTime());
		
		effectiveTime = new Date(bigEffectiveTime.add(ssTime.multiply(termTime)).longValue());
		
		setMealPurchase.setExpireDate(DateUtil.getFormatDate(effectiveTime, "yyyy-MM-dd HH:mm:ss"));
		setMealPurchase.setSetMealID(setMealID);
		return dao.save(setMealPurchase);
	}

	@Override
	public SetMealPurchase findByUserID(String userID) {
		String sql = "SELECT * FROM com_zixu_set_meal_purchase WHERE userID = ?";
		return dao.nativeFindUnique(SetMealPurchase.class, sql, userID);
	}

	@Override
	public SetMealPurchase save(SetMealPurchase setMealPurchase) {
		
		return dao.save(setMealPurchase);
	}

	@Override
	public SetMealPurchase findByUserIDAndExpireDate(String userID) {
		String sql = "SELECT * FROM com_zixu_set_meal_purchase WHERE userID = ? and DATE_FORMAT(expireDate,'%Y-%m-%d') >  DATE_FORMAT(now(),'%Y-%m-%d') ";
		return dao.nativeFindUnique(SetMealPurchase.class, sql, userID);
	}
	
}
