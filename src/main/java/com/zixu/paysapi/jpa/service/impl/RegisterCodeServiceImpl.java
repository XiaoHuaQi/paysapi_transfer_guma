package com.zixu.paysapi.jpa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zixu.paysapi.jpa.dao.RegisterCodeDao;
import com.zixu.paysapi.jpa.entity.RegisterCode;
import com.zixu.paysapi.jpa.service.RegisterCodeService;
import com.zixu.paysapi.mvc.util.Page;
import com.zixu.paysapi.util.DateUtil;

@Service
@Transactional
public class RegisterCodeServiceImpl implements RegisterCodeService {

	@Autowired
	private RegisterCodeDao dao;
	
	@Override
	public RegisterCode save(RegisterCode registerCode) {
		
		if(registerCode.getId() != null) {
			registerCode.setChangeTime(DateUtil.getStringDateTime());
		}
		
		return dao.save(registerCode);
	}

	@Override
	public void delete(Long id) {
		dao.delete(id);
	}

	@Override
	public Page<RegisterCode> page(int pageNum, String userName, String state) {
		
		Page<RegisterCode> page = new Page<>();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM com_zixu_register_code WHERE 1=1  ");
		
		if(userName != null) {
			sql.append(" AND userName = '"+userName+"' ");
		}
		if(state != null) {
			sql.append(" AND state = '"+state+"' ");
		}
		
		sql.append(" order by changeTime desc ");
		
		int count = dao.getCount(sql.toString());
		if(count == 0) {
			return null;
		}
		
		String sqlPage = page.getMysqlPageSql(sql.toString(), pageNum, 30);
		List<RegisterCode> list = dao.nativeFind(RegisterCode.class, sqlPage);
		
		page.setList(list);
		page.setPageNum(pageNum);
		page.setPageSize(30);
		page.setTotalRow(count);
		page.setTotalPage(count % 30 == 0 ? count / 30 : count / 30 + 1);
		return page;
	}

	@Override
	public RegisterCode findByCode(String code) {
		
		String sql = "SELECT * FROM com_zixu_register_code WHERE CODE = ? LIMIT 0,1";
		
		return dao.nativeFindUnique(RegisterCode.class, sql,code);
	}

}
