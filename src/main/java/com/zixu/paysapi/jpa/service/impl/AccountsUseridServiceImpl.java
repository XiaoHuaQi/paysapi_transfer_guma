package com.zixu.paysapi.jpa.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zixu.payment.mysql.DateUtil;
import com.zixu.paysapi.jpa.dao.AccountsUseridDao;
import com.zixu.paysapi.jpa.entity.AccountsUserid;
import com.zixu.paysapi.jpa.service.AccountsUseridService;

@Service
@Transactional
public class AccountsUseridServiceImpl implements AccountsUseridService{

	@Autowired
	private AccountsUseridDao dao;

	@Override
	public AccountsUserid save(AccountsUserid accountsUserid) {
		accountsUserid.setChangeDate(DateUtil.getStringDateTime());
		return dao.save(accountsUserid);
	}

	@Override
	public AccountsUserid findByUid(String uid) {
		
		String sql = "SELECT * FROM com_zixu_accounts_userid WHERE uid = ? order by changeDate desc limit 0,1 ";
		
		return dao.nativeFindUnique(AccountsUserid.class, sql, uid);
	}
	

}
