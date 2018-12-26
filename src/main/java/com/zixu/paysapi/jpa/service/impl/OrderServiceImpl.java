package com.zixu.paysapi.jpa.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zixu.paysapi.jpa.dao.OrderDao;
import com.zixu.paysapi.jpa.dto.StatisticsDto;
import com.zixu.paysapi.jpa.entity.Config;
import com.zixu.paysapi.jpa.entity.Order;
import com.zixu.paysapi.jpa.entity.User;
import com.zixu.paysapi.jpa.service.ConfigService;
import com.zixu.paysapi.jpa.service.OrderService;
import com.zixu.paysapi.jpa.service.UserService;
import com.zixu.paysapi.mvc.util.Page;
import com.zixu.paysapi.util.DateUtil;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDao dao;
	
	@Autowired
	private ConfigService configService;
	
	@Autowired
	private UserService userService;
	
	@Override
	public Order save(Order order) {
		
		if(order.getId() == null) {
			order.setPayTime(DateUtil.getStringDateTime());
		}
		
		return dao.save(order);
	}

	@Override
	public Order findByFee(int fee, String userID,String type) {
		int time;
		Config config = configService.findById(userID);
		if(config == null) {
			time = 300;
		}else {
			time = config.getOverdueTime();
		}
		
		String sql = "select * from com_zixu_order where price = ? and userID = ? and payTime > ?  and type = ?  order by payTime desc limit 0,1";
		
		Order order = dao.nativeFindUnique(Order.class, sql,fee, userID , calculationDate(time),type);
		
		return order;
	}
	
	public static String calculationDate(int time) {
		
		if(time == 0) {
			return DateUtil.getStringDateTime();
		}
		
		Date calculation = new Date(new BigDecimal(new Date().getTime()).subtract(new BigDecimal(time*1000)).longValue());
		
		return DateUtil.getFormatDate(calculation, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public Order findById(Long id) {
		
		return dao.findOne(id);
	}

	@Override
	public Page<Order> fiveMatching(int pageNum, String type,String userID,String startDate,String endDate,int fee) {
		
		Page<Order> page = new Page<>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from com_zixu_order where outTradeNo is null and orderType = '0' and userID = '"+userID+"'");
		if(type != null && !type.equals("")) {
			sql.append("and type = '"+type+"'");
		}
		if(fee != 0 ) {
			sql.append(" and price = '"+fee+"'");
		}
		if(startDate != null && !startDate.equals("")) {
			sql.append(" and DATE_FORMAT(payTime,'%Y-%m-%d') >= '"+startDate+"'");
		}
		if(endDate != null && !endDate.equals("")) {
			sql.append(" and DATE_FORMAT(payTime,'%Y-%m-%d') <= '"+endDate+"'");
		}
		sql.append(" order by payTime desc ");
		
		int count = dao.getCount(sql.toString());
		if(count == 0) {
			return null;
		}
		
		String sqlPage = page.getMysqlPageSql(sql.toString(), pageNum, 30);
		List<Order> list = dao.nativeFind(Order.class, sqlPage);
		
		page.setList(list);
		page.setPageNum(pageNum);
		page.setPageSize(30);
		page.setTotalRow(count);
		page.setTotalPage(count % 30 == 0 ? count / 30 : count / 30 + 1);
		return page;
	}

	@Override
	public Page<Order> page(int pageNum, String type, String userID, String startDate, String endDate,String outTradeNo,String systemID) {
		
		Page<Order> page = new Page<>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from com_zixu_order where outTradeNo is not  null and orderType = '0' and userID = '"+userID+"' ");
		if(type != null && !type.equals("")) {
			sql.append(" and type = '"+type+"'");
		}
		if(outTradeNo != null && !outTradeNo.equals("")) {
			sql.append(" and outTradeNo = '"+outTradeNo+"'");
		}
		if(systemID != null && !systemID.equals("")) {
			sql.append(" and systemID like '%"+systemID+"%'");
		}
		if(startDate != null && !startDate.equals("")) {
			sql.append(" and DATE_FORMAT(payTime,'%Y-%m-%d') >= '"+startDate+"'");
		}
		if(endDate != null && !endDate.equals("")) {
			sql.append(" and DATE_FORMAT(payTime,'%Y-%m-%d') <= '"+endDate+"'");
		}
		sql.append(" order by payTime desc ");
		
		int count = dao.getCount(sql.toString());
		if(count == 0) {
			return null;
		}
		
		String sqlPage = page.getMysqlPageSql(sql.toString(), pageNum, 30);
		List<Order> list = dao.nativeFind(Order.class, sqlPage);
		
		page.setList(list);
		page.setPageNum(pageNum);
		page.setPageSize(30);
		page.setTotalRow(count);
		page.setTotalPage(count % 30 == 0 ? count / 30 : count / 30 + 1);
		return page;
	}

	@Override
	public List<Order> indexData(String userID) {
		if(userID == null) {
			String sql = "select * from com_zixu_order where payState = '1'  and outTradeNo is not null and orderType = '2' order by payTime desc  LIMIT 0,10";
			return dao.nativeFind(Order.class, sql);
		}
		String sql = "select * from com_zixu_order where payState = '1' and  userID = ?  and outTradeNo is not null and orderType = '0' order by payTime desc  LIMIT 0,10";
		return dao.nativeFind(Order.class, sql, userID);
	}

	@Override
	public StatisticsDto statistics(String userID) {
		
		String sql = "SELECT IFNULL((SELECT SUM(price) FROM com_zixu_order WHERE payState = '1' and orderType = '0' AND userID = '"+userID+"' AND outTradeNo IS NOT NULL AND DATE_FORMAT(payTime,'%Y-%m') = DATE_FORMAT(NOW(),'%Y-%m')),0) AS thisMonthFee,"
				+ "IFNULL((SELECT SUM(price) FROM com_zixu_order WHERE payState = '1' and orderType = '0' AND userID = '"+userID+"' AND  outTradeNo IS NOT NULL) ,0) AS thirtyDateFee,"
				+ "IFNULL((SELECT SUM(price) FROM com_zixu_order WHERE payState = '1' and orderType = '0' AND userID = '"+userID+"' AND  outTradeNo IS NOT NULL AND DATE_FORMAT(payTime,'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d')),0) AS nowDateFee,"
				+ "IFNULL((SELECT SUM(price) FROM com_zixu_order WHERE payState = '1' and orderType = '0' AND userID = '"+userID+"' AND  outTradeNo IS NOT NULL AND TO_DAYS( NOW( )) - TO_DAYS( payTime) = 1),0) AS yesterdayFee,"
				+ "IFNULL((SELECT COUNT(price) FROM com_zixu_order WHERE payState = '1' and orderType = '0' AND userID = '"+userID+"' AND  outTradeNo IS NOT NULL AND DATE_FORMAT(payTime,'%Y-%m') = DATE_FORMAT(NOW(),'%Y-%m')),0) AS thisMonthNum,"
				+ "IFNULL((SELECT COUNT(price) FROM com_zixu_order WHERE payState = '1' and orderType = '0' AND userID = '"+userID+"' AND  outTradeNo IS NOT NULL),0) AS thirtyDateNum,"
				+ "IFNULL((SELECT COUNT(price) FROM com_zixu_order WHERE payState = '1' and orderType = '0' AND userID = '"+userID+"' AND outTradeNo IS NOT NULL AND DATE_FORMAT(payTime,'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d')),0) AS nowDateNum,"
				+ "IFNULL((SELECT COUNT(price) FROM com_zixu_order WHERE payState = '1' and orderType = '0' AND userID = '"+userID+"' AND  outTradeNo IS NOT NULL AND TO_DAYS( NOW( )) - TO_DAYS( payTime) = 1),0) AS yesterdayNum "
				+ "FROM com_zixu_order LIMIT 0,1 ";
		
		return dao.nativeFindUnique(StatisticsDto.class, sql);
	}

	@Override
	public Order findByOutTradeNo(String userID, String outTradeNo) {
		String sql = "select * from com_zixu_order where userID = ?  and outTradeNo = ?";
		return dao.nativeFindUnique(Order.class, sql, userID,outTradeNo);
	}

	@Override
	public StatisticsDto adminStatistics() {
		
		String sql = "SELECT IFNULL((SELECT SUM(price) FROM com_zixu_order WHERE payState = '1' and orderType = '0'  AND outTradeNo IS NOT NULL AND DATE_FORMAT(payTime,'%Y-%m') = DATE_FORMAT(NOW(),'%Y-%m')),0) AS thisMonthFee,"
				+ "IFNULL((SELECT SUM(price) FROM com_zixu_order WHERE payState = '1' and orderType = '0'  AND  outTradeNo IS NOT NULL) ,0) AS thirtyDateFee,"
				+ "IFNULL((SELECT SUM(price) FROM com_zixu_order WHERE payState = '1' and orderType = '0'  AND  outTradeNo IS NOT NULL AND DATE_FORMAT(payTime,'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d')),0) AS nowDateFee,"
				+ "IFNULL((SELECT SUM(price) FROM com_zixu_order WHERE payState = '1' and orderType = '0'  AND  outTradeNo IS NOT NULL AND TO_DAYS( NOW( )) - TO_DAYS( payTime) = 1),0) AS yesterdayFee,"
				+ "IFNULL((SELECT COUNT(price) FROM com_zixu_order WHERE payState = '1' and orderType = '0'  AND  outTradeNo IS NOT NULL AND DATE_FORMAT(payTime,'%Y-%m') = DATE_FORMAT(NOW(),'%Y-%m')),0) AS thisMonthNum,"
				+ "IFNULL((SELECT COUNT(price) FROM com_zixu_order WHERE payState = '1' and orderType = '0'  AND  outTradeNo IS NOT NULL),0) AS thirtyDateNum,"
				+ "IFNULL((SELECT COUNT(price) FROM com_zixu_order WHERE payState = '1' and orderType = '0'  AND outTradeNo IS NOT NULL AND DATE_FORMAT(payTime,'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d')),0) AS nowDateNum,"
				+ "IFNULL((SELECT COUNT(price) FROM com_zixu_order WHERE payState = '1' and orderType = '0'  AND  outTradeNo IS NOT NULL AND TO_DAYS( NOW( )) - TO_DAYS( payTime) = 1),0) AS yesterdayNum "
				+ "FROM com_zixu_order LIMIT 0,1 ";
		
		return dao.nativeFindUnique(StatisticsDto.class, sql);
	}

	@Override
	public Order findByFee(int fee, String type) {
		
		String sql = "select * from com_zixu_order where price = ?  and type = ?  order by payTime desc limit 0,1";
		
		Order order = dao.nativeFindUnique(Order.class, sql,fee, type);
		
		return order;
	}

	@Override
	public StatisticsDto adminRechargeStatistics() {
		String sql = "SELECT IFNULL((SELECT SUM(price) FROM com_zixu_order WHERE payState = '1' and orderType = '2'  AND outTradeNo IS NOT NULL AND DATE_FORMAT(payTime,'%Y-%m') = DATE_FORMAT(NOW(),'%Y-%m')),0) AS thisMonthFee,"
				+ "IFNULL((SELECT SUM(price) FROM com_zixu_order WHERE payState = '1' and orderType = '2'  AND  outTradeNo IS NOT NULL) ,0) AS thirtyDateFee,"
				+ "IFNULL((SELECT SUM(price) FROM com_zixu_order WHERE payState = '1' and orderType = '2'  AND  outTradeNo IS NOT NULL AND DATE_FORMAT(payTime,'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d')),0) AS nowDateFee,"
				+ "IFNULL((SELECT SUM(price) FROM com_zixu_order WHERE payState = '1' and orderType = '2'  AND  outTradeNo IS NOT NULL AND TO_DAYS( NOW( )) - TO_DAYS( payTime) = 1),0) AS yesterdayFee,"
				+ "IFNULL((SELECT COUNT(price) FROM com_zixu_order WHERE payState = '1' and orderType = '2'  AND  outTradeNo IS NOT NULL AND DATE_FORMAT(payTime,'%Y-%m') = DATE_FORMAT(NOW(),'%Y-%m')),0) AS thisMonthNum,"
				+ "IFNULL((SELECT COUNT(price) FROM com_zixu_order WHERE payState = '1' and orderType = '2'  AND  outTradeNo IS NOT NULL),0) AS thirtyDateNum,"
				+ "IFNULL((SELECT COUNT(price) FROM com_zixu_order WHERE payState = '1' and orderType = '2'  AND outTradeNo IS NOT NULL AND DATE_FORMAT(payTime,'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d')),0) AS nowDateNum,"
				+ "IFNULL((SELECT COUNT(price) FROM com_zixu_order WHERE payState = '1' and orderType = '2'  AND  outTradeNo IS NOT NULL AND TO_DAYS( NOW( )) - TO_DAYS( payTime) = 1),0) AS yesterdayNum "
				+ "FROM com_zixu_order LIMIT 0,1 ";
		
		return dao.nativeFindUnique(StatisticsDto.class, sql);
	}

	@Override
	public StatisticsDto adminSetMealStatistics() {
		String sql = "SELECT IFNULL((SELECT SUM(price) FROM com_zixu_order WHERE payState = '1' and orderType = '1'  AND outTradeNo IS NOT NULL AND DATE_FORMAT(payTime,'%Y-%m') = DATE_FORMAT(NOW(),'%Y-%m')),0) AS thisMonthFee,"
				+ "IFNULL((SELECT SUM(price) FROM com_zixu_order WHERE payState = '1' and orderType = '1'  AND  outTradeNo IS NOT NULL) ,0) AS thirtyDateFee,"
				+ "IFNULL((SELECT SUM(price) FROM com_zixu_order WHERE payState = '1' and orderType = '1'  AND  outTradeNo IS NOT NULL AND DATE_FORMAT(payTime,'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d')),0) AS nowDateFee,"
				+ "IFNULL((SELECT SUM(price) FROM com_zixu_order WHERE payState = '1' and orderType = '1'  AND  outTradeNo IS NOT NULL AND TO_DAYS( NOW( )) - TO_DAYS( payTime) = 1),0) AS yesterdayFee,"
				+ "IFNULL((SELECT COUNT(price) FROM com_zixu_order WHERE payState = '1' and orderType = '1'  AND  outTradeNo IS NOT NULL AND DATE_FORMAT(payTime,'%Y-%m') = DATE_FORMAT(NOW(),'%Y-%m')),0) AS thisMonthNum,"
				+ "IFNULL((SELECT COUNT(price) FROM com_zixu_order WHERE payState = '1' and orderType = '1'  AND  outTradeNo IS NOT NULL),0) AS thirtyDateNum,"
				+ "IFNULL((SELECT COUNT(price) FROM com_zixu_order WHERE payState = '1' and orderType = '1'  AND outTradeNo IS NOT NULL AND DATE_FORMAT(payTime,'%Y-%m-%d') = DATE_FORMAT(NOW(),'%Y-%m-%d')),0) AS nowDateNum,"
				+ "IFNULL((SELECT COUNT(price) FROM com_zixu_order WHERE payState = '1' and orderType = '1'  AND  outTradeNo IS NOT NULL AND TO_DAYS( NOW( )) - TO_DAYS( payTime) = 1),0) AS yesterdayNum "
				+ "FROM com_zixu_order LIMIT 0,1 ";
		
		return dao.nativeFindUnique(StatisticsDto.class, sql);
	}

	@Override
	public Page<Order> setMealList(int pageNum, String type, String startDate, String endDate, String orderType,
			String payType,String userID,String systemID,String outTradeNo) {
		Page<Order> page = new Page<>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from com_zixu_order where 1=1 ");
		if(type != null && !type.equals("")) {
			sql.append(" and payState = '"+type+"'");
		}
		if(orderType != null ) {
			sql.append(" and orderType = '"+orderType+"'");
		}
		if(systemID != null ) {
			sql.append(" and systemID like '%"+systemID+"%'");
		}
		if(outTradeNo != null ) {
			sql.append(" and outTradeNo like '%"+outTradeNo+"%'");
		}
		if(startDate != null && !startDate.equals("")) {
			sql.append(" and DATE_FORMAT(payTime,'%Y-%m-%d') >= '"+startDate+"'");
		}
		if(endDate != null && !endDate.equals("")) {
			sql.append(" and DATE_FORMAT(payTime,'%Y-%m-%d') <= '"+endDate+"'");
		}
		if(payType != null) {
			sql.append(" and type = '"+payType+"'");
		}
		if(userID != null) {
			sql.append(" and userID = '"+userID+"'");
		}
		
		sql.append(" order by payTime desc ");
		
		int count = dao.getCount(sql.toString());
		if(count == 0) {
			return null;
		}
		
		String sqlPage = page.getMysqlPageSql(sql.toString(), pageNum, 30);
		List<Order> list = dao.nativeFind(Order.class, sqlPage);
		for (Order order : list) {
			if(order.getUserID() != null) {
				User user = userService.findById(order.getUserID());
				if(user != null) {
					order.setUserName(user.getUserName());
				}
			}
			
		}
		page.setList(list);
		page.setPageNum(pageNum);
		page.setPageSize(30);
		page.setTotalRow(count);
		page.setTotalPage(count % 30 == 0 ? count / 30 : count / 30 + 1);
		return page;
	}

	@Override
	public Order findByOutTradeNo(String outTradeNo) {
		String sql = "select * from com_zixu_order where  outTradeNo = ?";
		return dao.nativeFindUnique(Order.class, sql, outTradeNo);
	}
	
	@Override
	public Order findBySystemID(String systemID) {
		String sql = "select * from com_zixu_order where  systemID = ?";
		return dao.nativeFindUnique(Order.class, sql, systemID);
	}
}
