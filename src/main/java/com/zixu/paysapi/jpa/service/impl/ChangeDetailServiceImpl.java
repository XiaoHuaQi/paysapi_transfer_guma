package com.zixu.paysapi.jpa.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zixu.paysapi.jpa.dao.ChangeDetailDao;
import com.zixu.paysapi.jpa.entity.ChangeDetail;
import com.zixu.paysapi.jpa.service.ChangeDetailService;
import com.zixu.paysapi.mvc.util.Page;
import com.zixu.paysapi.util.DateUtil;
import com.zixu.paysapi.util.SysUtil;

@Service
@Transactional
public class ChangeDetailServiceImpl implements ChangeDetailService{

	@Autowired
	private ChangeDetailDao dao;
	
	@Override
	public ChangeDetail save(String userID,int changeFee) {
		
		ChangeDetail changeDetail = new ChangeDetail();
		
		
		String sql = "select CONCAT(IFNULL(sum(price),0)) from com_zixu_order where payState = '1' and orderType='0' and userID = ?";
		
		String sumFee = dao.nativeFindUnique(String.class, sql, userID);
		
		changeDetail.setBeforeFee(Long.valueOf(sumFee));
		changeDetail.setChangeFee(changeFee);
		changeDetail.setRemarks("收入");
		changeDetail.setTime(DateUtil.getStringDateTime());
		changeDetail.setUserID(userID);
		BigDecimal afterFee =  new BigDecimal(changeFee).add(new BigDecimal(changeDetail.getBeforeFee()));
		changeDetail.setAfterFee(afterFee.intValue());
		
		return dao.save(changeDetail);
	}

	@Override
	public Page<ChangeDetail> page(int pageNum, String userID,String startDate, String endDate) {
		
		Page<ChangeDetail> page = new Page<>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from com_zixu_change_detail where userID = ? ");
		
		if(startDate != null) {
			sql.append(" and DATE_FORMAT(time,'%Y-%m-%d') >= '"+startDate+"'");
		}
		if(endDate != null) {
			sql.append(" and DATE_FORMAT(time,'%Y-%m-%d') <= '"+endDate+"'");
		}
		
		sql.append(" order by time desc ");
		
		int count = dao.getCount(sql.toString(),userID);
		if(count == 0) {
			return null;
		}
		
		String sqlPage = page.getMysqlPageSql(sql.toString(), pageNum, 30);
		List<ChangeDetail> list = dao.nativeFind(ChangeDetail.class, sqlPage,userID);
		
		page.setList(list);
		page.setPageNum(pageNum);
		page.setPageSize(30);
		page.setTotalRow(count);
		page.setTotalPage(count % 30 == 0 ? count / 30 : count / 30 + 1);
		return page;
	}

}
