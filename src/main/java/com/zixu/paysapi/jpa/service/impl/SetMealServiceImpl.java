package com.zixu.paysapi.jpa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zixu.paysapi.jpa.dao.SetMealDao;
import com.zixu.paysapi.jpa.entity.SetMeal;
import com.zixu.paysapi.jpa.service.SetMealService;
import com.zixu.paysapi.util.SysUtil;

@Service
@Transactional
public class SetMealServiceImpl implements SetMealService {

	@Autowired
	private SetMealDao dao;
	
	@Override
	public void delete(String id) {
		dao.delete(id);
		String sql = "DELETE FROM com_zixu_set_meal_qrcode WHERE setMealID = ?";
		dao.nativeExecuteUpdate(sql,id);
	}

	@Override
	public SetMeal findById(String id) {
		
		return dao.findOne(id);
	}

	@Override
	public List<SetMeal> findAll() {
		
		return dao.findAll();
	}

	@Override
	public SetMeal save(SetMeal setMeal) {
		
		if(setMeal.getId() == null) {
			setMeal.setId(SysUtil.generalPK());
		}
		return dao.save(setMeal);
	}

}
