package com.zixu.paysapi.jpa.service;

import java.util.List;

import com.zixu.paysapi.jpa.entity.SetMeal;

public interface SetMealService {
	
	void delete(String id);
	
	SetMeal findById(String id);
	
	List<SetMeal> findAll();
	
	SetMeal save(SetMeal setMeal);
	
}
