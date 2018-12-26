package com.zixu.paysapi.jpa.service;

import com.zixu.paysapi.jpa.entity.SetMealPurchase;
import com.zixu.paysapi.mvc.util.Page;

public interface SetMealPurchaseService {
	
	Page<SetMealPurchase> findAll(int pageNum, String userName);
	
	SetMealPurchase save(String userID,String date,int procedures,String setMealID);
	
	SetMealPurchase findByUserID(String userID);
	
	SetMealPurchase save(SetMealPurchase  setMealPurchase);
	
	SetMealPurchase findByUserIDAndExpireDate(String userID);
}
