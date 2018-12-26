package com.zixu.paysapi.jpa.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zixu.paysapi.jpa.dao.RechargeUserDetailedDao;
import com.zixu.paysapi.jpa.dto.StatisticsDto;
import com.zixu.paysapi.jpa.entity.RechargeUserDetailed;
import com.zixu.paysapi.jpa.service.RechargeUserDetailedService;
import com.zixu.paysapi.mvc.util.Page;
import com.zixu.paysapi.util.DateUtil;

@Service
@Transactional
public class RechargeUserDetailedServiceImpl implements RechargeUserDetailedService{

	@Autowired
	private RechargeUserDetailedDao dao;
	
	@Override
	public int sum(String userID) {
		
		String sql = "SELECT cast(SUM(fee) as char) FROM com_zixu_recharge_user_detailed WHERE userID = ?";
		
		String sum = dao.nativeFindUnique(String.class, sql,userID);
		
		if(sum == null || "".equals(sum)) {
			sum = "0";
		}
		
		return Integer.valueOf(sum);
	}

	@Override
	public int rechargeSum(String userID) {
		
		String sql = "SELECT concat(SUM(fee),'') FROM com_zixu_recharge_user_detailed WHERE userID = ? and fee > 0";
		
		String sum = dao.nativeFindUnique(String.class, sql,userID);
		
		if(sum == null || "".equals(sum)) {
			sum = "0";
		}
		
		return Integer.valueOf(sum);
	}

	@Override
	public int consumptionSum(String userID) {
		
		String sql = "SELECT concat(SUM(fee),'') FROM com_zixu_recharge_user_detailed WHERE userID = ? and fee < 0";
		
		String sum = dao.nativeFindUnique(String.class, sql,userID);
		
		if(sum == null || "".equals(sum)) {
			sum = "0";
		}
		
		return Integer.valueOf(sum);
	}

	@Override
	public Page<RechargeUserDetailed> page(int pageNum, String userID, String startDate, String endDate,String state) {
		
		Page<RechargeUserDetailed> page = new Page<>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from com_zixu_recharge_user_detailed where 1=1 ");
		if(userID != null && !userID.equals("")) {
			sql.append("and userID = '"+userID+"'");
		}
		if(state != null ) {
			if("1".equals(state)) {
				sql.append(" and fee > 0");
			}else if("2".equals(state)) {
				sql.append(" and fee < 0");
			}
			
		}
		if(startDate != null && !startDate.equals("")) {
			sql.append(" and DATE_FORMAT(changeDate,'%Y-%m-%d') >= '"+startDate+"'");
		}
		if(endDate != null && !endDate.equals("")) {
			sql.append(" and DATE_FORMAT(changeDate,'%Y-%m-%d') <= '"+endDate+"'");
		}
		sql.append(" order by changeDate desc ");
		
		int count = dao.getCount(sql.toString());
		if(count == 0) {
			return null;
		}
		
		String sqlPage = page.getMysqlPageSql(sql.toString(), pageNum, 30);
		List<RechargeUserDetailed> list = dao.nativeFind(RechargeUserDetailed.class, sqlPage);
		
		page.setList(list);
		page.setPageNum(pageNum);
		page.setPageSize(30);
		page.setTotalRow(count);
		page.setTotalPage(count % 30 == 0 ? count / 30 : count / 30 + 1);
		return page;
		
	}

	@Override
	public RechargeUserDetailed save(RechargeUserDetailed rechargeUserDetailed) {
		
		rechargeUserDetailed.setChangeDate(DateUtil.getStringDateTime());
		
		return dao.save(rechargeUserDetailed);
	}

	@Override
	public StatisticsDto adminStatistics() {
		
		String sql = "SELECT IFNULL((SELECT SUM(fee) FROM com_zixu_recharge_user_detailed WHERE remarks = '下单扣除手续费'  AND DATE_FORMAT(changeDate,'%Y-%m') = DATE_FORMAT(NOW(),'%Y-%m')),0) AS thisMonthFee,"
				+ "IFNULL((SELECT SUM(fee) FROM com_zixu_recharge_user_detailed WHERE remarks = '下单扣除手续费') ,0) AS thirtyDateFee,"
				+ "IFNULL((SELECT SUM(fee) FROM com_zixu_recharge_user_detailed WHERE remarks = '下单扣除手续费'  AND DATE_FORMAT(changeDate,'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d')),0) AS nowDateFee,"
				+ "IFNULL((SELECT SUM(fee) FROM com_zixu_recharge_user_detailed WHERE remarks = '下单扣除手续费'  AND TO_DAYS( NOW( )) - TO_DAYS( changeDate) = 1),0) AS yesterdayFee,"
				+ "IFNULL((SELECT COUNT(fee) FROM com_zixu_recharge_user_detailed WHERE remarks = '下单扣除手续费' AND DATE_FORMAT(changeDate,'%Y-%m') = DATE_FORMAT(NOW(),'%Y-%m')),0) AS thisMonthNum,"
				+ "IFNULL((SELECT COUNT(fee) FROM com_zixu_recharge_user_detailed WHERE remarks = '下单扣除手续费'),0) AS thirtyDateNum,"
				+ "IFNULL((SELECT COUNT(fee) FROM com_zixu_recharge_user_detailed WHERE remarks = '下单扣除手续费'  AND DATE_FORMAT(changeDate,'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d')),0) AS nowDateNum,"
				+ "IFNULL((SELECT COUNT(fee) FROM com_zixu_recharge_user_detailed WHERE remarks = '下单扣除手续费'  AND TO_DAYS( NOW( )) - TO_DAYS( changeDate) = 1),0) AS yesterdayNum "
				+ "FROM com_zixu_recharge_user_detailed LIMIT 0,1 ";
		
		return dao.nativeFindUnique(StatisticsDto.class, sql);
	}

	@Override
	public RechargeUserDetailed findByOutTradeNo(String outTradeNo) {
		
		String sql = "SELECT * FROM com_zixu_recharge_user_detailed WHERE outTradeNo = ? ";
		
		return dao.nativeFindUnique(RechargeUserDetailed.class, sql,outTradeNo);
	}

}
