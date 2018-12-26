package com.zixu.paysapi.jpa.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zixu.paysapi.jpa.dao.TestQrcodeDao;
import com.zixu.paysapi.jpa.entity.TestQrcode;
import com.zixu.paysapi.jpa.service.TestQrcodeService;
import com.zixu.paysapi.mvc.util.Page;
import com.zixu.paysapi.util.SysUtil;

@Service
@Transactional
public class TestQrcodeServiceImpl implements TestQrcodeService {

	private static String redisKey = "mysql_com_zixu_test_qrcode";
	
	@Autowired
	private TestQrcodeDao dao;
	

	@Override
	public TestQrcode save(TestQrcode testQrcode) {
		
		String sql = "select * from com_zixuapp_test_qrcode where type = ? and price = ?";
		
		TestQrcode testQrcode2 =  dao.nativeFindUnique(TestQrcode.class,sql, testQrcode.getType(),testQrcode.getPrice());
		
		if(testQrcode2 != null) {
			testQrcode.setId(testQrcode2.getId());
		}
		
		return dao.save(testQrcode);
	}

	@Override
	public Page<TestQrcode> page(int pageNum) {
		
		Page<TestQrcode> page = new Page<>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from com_zixuapp_test_qrcode  ");
		sql.append(" order by price desc ");
		
		int count = dao.getCount(sql.toString());
		if(count == 0) {
			return null;
		}
		
		String sqlPage = page.getMysqlPageSql(sql.toString(), pageNum, 30);
		List<TestQrcode> list = dao.nativeFind(TestQrcode.class, sqlPage);
		
		page.setList(list);
		page.setPageNum(pageNum);
		page.setPageSize(30);
		page.setTotalRow(count);
		page.setTotalPage(count % 30 == 0 ? count / 30 : count / 30 + 1);
		return page;
	}

	@Override
	public List<TestQrcode> findByList(String type) {
		String sql = "select * from com_zixuapp_test_qrcode ";
		if(type != null) {
			sql = sql + "where type = '"+type+"' ";
		}
		sql = sql + " order by price asc ";
		return dao.nativeFind(TestQrcode.class, sql);
	}

	@Override
	public TestQrcode findByFee(String type, int fee) {
		String sql = "select * from com_zixuapp_test_qrcode where type = ? and price = ?";
		return dao.nativeFindUnique(TestQrcode.class, sql,type,fee);
	}

	@Override
	public void delete(Long id) {
		
		dao.delete(id);
		
		
	}

}
