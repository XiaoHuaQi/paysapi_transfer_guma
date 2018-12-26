package com.zixu.paysapi.jpa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zixu.paysapi.jpa.dao.UserDao;
import com.zixu.paysapi.jpa.dto.UserDto;
import com.zixu.paysapi.jpa.entity.SetMealPurchase;
import com.zixu.paysapi.jpa.entity.User;
import com.zixu.paysapi.jpa.service.UserService;
import com.zixu.paysapi.mvc.util.Page;
import com.zixu.paysapi.util.SysUtil;
import com.zixuapp.redis.Config;
import com.zixuapp.redis.RedisOperationManager;

import net.sf.json.JSONObject;

@Service
@Transactional
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao dao;
	
	private static String redisKeyID = "mysql_user_id:";
	
	private static String redisKeyUID = "mysql_user_uid:";
	

	@Override
	public User save(User user) {
		
		if(user.getId() == null) {
			user.setId(SysUtil.generalPK());
			user.setUid(SysUtil.generalPK());
			user.setToken(SysUtil.generalPK());
		}
		
		user = dao.save(user);
		
		if(user != null) {
			RedisOperationManager.del(redisKeyID+user.getId());
			RedisOperationManager.del(redisKeyUID+user.getUid());
		}
		
		return dao.save(user);
	}

	@Override
	public void delete(String id) {
		
		dao.delete(id);
		RedisOperationManager.del(redisKeyID+id);
	}

	@Override
	public User findById(String id) {
		
		String redisData = RedisOperationManager.getString(redisKeyID+id);
		if(redisData != null) {
			return (User) JSONObject.toBean(JSONObject.fromObject(redisData),User.class);
		}
		
		User user = dao.findOne(id);
		if(user != null) {
			RedisOperationManager.setString(redisKeyID+id, com.alibaba.fastjson.JSONObject.toJSONString(user), Integer.valueOf(new Config().get("expires")));
		}
		
		return user;
	}

	@Override
	public User findByName(String userName) {
		String sql = "select * from com_zixu_user where userName = ?";
		return dao.nativeFindUnique(User.class, sql, userName);
	}

	@Override
	public User findByUid(String uid) {
		String redisData = RedisOperationManager.getString(redisKeyUID+uid);
		if(redisData != null) {
			return (User) JSONObject.toBean(JSONObject.fromObject(redisData),User.class);
		}
		String sql = "select * from com_zixu_user where uid = ?";
		
		User user = dao.nativeFindUnique(User.class, sql, uid);
		if(user != null) {
			RedisOperationManager.setString(redisKeyUID+uid, com.alibaba.fastjson.JSONObject.toJSONString(user), Integer.valueOf(new Config().get("expires")));
		}
		
		return user;
	}

	@Override
	public List<User> findAll() {
		String sql = "select * from com_zixu_user where type = 'merchant' ";
		return dao.nativeFind(User.class, sql);
	}

	@Override
	public Page<User> findAll(int pageNum, String userName) {
		
		Page<User> page = new Page<>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from com_zixu_user where type = 'merchant'");
		if(userName != null && !userName.equals("")) {
			sql.append(" and  userName like '%"+userName+"%' ");
		}
		sql.append(" order by id desc ");
		
		int count = dao.getCount(sql.toString());
		if(count == 0) {
			return null;
		}
		
		String sqlPage = page.getMysqlPageSql(sql.toString(), pageNum, 30);
		List<User> list = dao.nativeFind(User.class, sqlPage);
		
		page.setList(list);
		page.setPageNum(pageNum);
		page.setPageSize(30);
		page.setTotalRow(count);
		page.setTotalPage(count % 30 == 0 ? count / 30 : count / 30 + 1);
		return page;
	}

	@Override
	public List<UserDto> findByOrderPay() {
		
		String sql = "SELECT u.id as id,IFNULL(SUM(d.fee),0) AS sumFee FROM com_zixu_user u LEFT JOIN com_zixu_recharge_user_detailed d ON u.id = d.userID WHERE u.state='0' AND u.type='merchant' GROUP BY u.id";
		
		return dao.nativeFind(UserDto.class, sql);
	}
	
	
}
