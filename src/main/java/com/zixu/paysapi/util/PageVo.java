package com.zixu.paysapi.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageVo {
	/**
	 * 起始行，用于和iDisplayLength一起分页
	 */
    private int iDisplayStart=0;
	/**
	 * 每页大小
	 */
    private int iDisplayLength=10;
    /**
	 * 第几列排序
	 */
    private String iSortCol_0;
    /**
	 * 排序字段名
	 */
    private String orderColumn;
    /**
	 * 降序升序
	 */
    private String sSortDir_0;
    /**
	 * 用于存储分页参数
	 */
    Map<String, Object> map = new HashMap<String,Object>();
	/**
	 * 总记录数
	 */
    private int iTotalRecords;   
    private int iTotalDisplayRecords;
    
	/**
	 * 数据集合
	 */
    private List aaData;


	public int getiDisplayStart() {
		return iDisplayStart;
	}

	public void setiDisplayStart(int iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}

	public int getiDisplayLength() {
		return iDisplayLength;
	}

	public void setiDisplayLength(int iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}

	public String getiSortCol_0() {
		return iSortCol_0;
	}

	public void setiSortCol_0(String iSortCol_0) {
		this.iSortCol_0 = iSortCol_0;
	}

	public String getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}
	
	public String getsSortDir_0() {
		return sSortDir_0;
	}

	public void setsSortDir_0(String sSortDir_0) {
		this.sSortDir_0 = sSortDir_0;
	}

	public int getiTotalRecords() {
		return iTotalRecords;
	}

	public void setiTotalRecords(int iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}

	public void setiTotalRecords2(int iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
		this.iTotalDisplayRecords = iTotalRecords;
	}
	
	public int getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

	public List getAaData() {
		return aaData;
	}

	public void setAaData(List aaData) {
		this.aaData = aaData;
	}

	/**
	 * 封装分页参数
	 * @return
	 */
	public Map<String, Object> getMap() {
		map.put("sSortDir_0", sSortDir_0);
		map.put("orderColumn", orderColumn);
		map.put("iDisplayLength", iDisplayLength);
		map.put("iDisplayStart", iDisplayStart);
		return map;
	}

}
