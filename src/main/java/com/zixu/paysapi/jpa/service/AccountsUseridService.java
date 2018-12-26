package com.zixu.paysapi.jpa.service;

import com.zixu.paysapi.jpa.entity.AccountsUserid;

public interface AccountsUseridService {
	
	AccountsUserid save(AccountsUserid accountsUserid);
	
	AccountsUserid findByUid(String uid);
	
}
