package com.zixu.paysapi.jpa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zixu.paysapi.jpa.dao.CommodityDao;
import com.zixu.paysapi.jpa.entity.Commodity;
import com.zixu.paysapi.jpa.service.CommodityService;
import com.zixu.paysapi.mvc.util.Page;
import com.zixu.paysapi.util.DateUtil;
import com.zixuapp.redis.Config;
import com.zixuapp.redis.RedisOperationManager;

import net.sf.json.JSONObject;

@Service
@Transactional
public class CommodityServiceImpl implements CommodityService{

	@Autowired
	private CommodityDao dao;
	
	private static String redisKeyID = "mysql_commodity_id:";
	
	@Override
	public Commodity save(Commodity commodity) {
		
		if(commodity.getId() == null) {
			//commodity.setId(SysUtil.generalPK());
			commodity.setTime(DateUtil.getStringDateTime());
		}
		return dao.save(commodity);
	}

	@Override
	public Commodity findById(Long id) {
		
		String info = RedisOperationManager.getString(redisKeyID+id);
		if(info != null) {
			return (Commodity)JSONObject.toBean(JSONObject.fromObject(info),Commodity.class);
		}
		
		Commodity commodity = dao.findOne(id);
		if(commodity != null) {
			RedisOperationManager.setString(redisKeyID+id, com.alibaba.fastjson.JSONObject.toJSONString(commodity), Integer.valueOf(new Config().get("expires")));
		}
		
		return commodity;
	}

	@Override
	public Page<Commodity> findByPage(int pageNum, String name,String userID) {
		
		Page<Commodity> page = new Page<>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from com_zixu_commodity where userID = '"+userID+"' ");
		if(name != null && !name.equals("")) {
			sql.append(" and name = '"+name+"'");
		}
		sql.append(" order by time desc ");
		
		int count = dao.getCount(sql.toString());
		if(count == 0) {
			return null;
		}
		
		String sqlPage = page.getMysqlPageSql(sql.toString(), pageNum, 30);
		List<Commodity> list = dao.nativeFind(Commodity.class, sqlPage);
		
		page.setList(list);
		page.setPageNum(pageNum);
		page.setPageSize(30);
		page.setTotalRow(count);
		page.setTotalPage(count % 30 == 0 ? count / 30 : count / 30 + 1);
		return page;
	}

	@Override
	public Commodity findByName(String name,String userID) {
		
		String sql = "select * from com_zixu_commodity where name = ? and userID = ?";
		
		return dao.nativeFindUnique(Commodity.class, sql, name,userID);
	}

	@Override
	public Commodity findByFee(int fee,String userID) {
		
		String sql = "select * from com_zixu_commodity where fee = ? and userID = ?";
		
		return dao.nativeFindUnique(Commodity.class, sql, fee, userID);
	}

	@Override
	public void delete(Long id) {
		
		dao.delete(id);
		
		String sql = "delete from com_zixu_qrcode where commodityID = ?";
		dao.nativeExecuteUpdate(sql,String.valueOf(id));
		
	}

	@Override
	public List<Commodity> findByList(String userID) {
		
		String sql = "select * from com_zixu_commodity where userID = ?";
		
		return dao.nativeFind(Commodity.class, sql,userID);
	}
	
	
}
