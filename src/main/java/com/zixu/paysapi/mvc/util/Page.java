package com.zixu.paysapi.mvc.util;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author TuYongKang
 *
 * @param <T>
 */
public class Page<T> implements Serializable{

	private static final long serialVersionUID = -3479893339718206705L;

	private List<T> list;		
	private int pageNum = 1;		
	private int pageSize = 30;
	private int totalPage;
	private int totalRow = 0;
	
	public Page(){
		
	}
	
//	public Page(List<T> list, int pageNumber, int pageSize, int totalPage, int totalRow){
//		this.list = list;
//		this.pageNumber = pageNumber;
//		this.pageSize = pageSize;
//		this.totalPage = totalPage;
//		this.totalRow = totalRow;
//	}
	
	public List<T> getList(){
		return list;
	}
	
	public void setList(List<T> list){
		this.list = list;
	}
	
//	public int pageNumber(){
//		if(pageNumber < 1) pageNumber = 1;
//		if(pageNumber > totalPage) pageNumber = totalPage;
//		return pageNumber;
//	}
	
	
	public int pageSize(){
		return pageSize;
	}
	
	public void setPageSize(int pageSize){
		this.pageSize = pageSize;
	}
	
	public int getTotalPage() {
		return totalPage;
	}
	
	public void setTotalPage(int totalpage){
		this.totalPage = totalpage;
	}
	
	public int getRecordsTotal() {
		return totalRow;
	}
	
	public int getTotalRow() {
		return totalRow;
	}
	
	public int getRecordsFiltered() {
		return totalRow;
	}
	
	public void setTotalRow(int totalRow){
		this.totalRow = totalRow;
	}
	
	public boolean isFirstPage() {
		return pageNum == 1;
	}
	
	public boolean isLastPage() {
		return pageNum >= totalPage;
	}
	
	/*public String toString() {
		StringBuilder msg = new StringBuilder();
		msg.append("pageNum : ").append(pageNum);
		msg.append("\npageSize : ").append(pageSize);
		msg.append("\ntotalPage : ").append(totalPage);
		msg.append("\ntotalRow : ").append(totalRow);
		return msg.toString();
	}
	*/
	public String getOraclePageSql(String sql, int pageNumber, int pageSize){
		String sqlPage = "SELECT * FROM ( SELECT a.*,rownum r_ FROM ( "+ sql + " ) a WHERE rownum < "
				+ ((pageNumber * pageSize) + 1) + " ) WHERE r_ >= " + (((pageNumber-1) * pageSize) + 1);
		return sqlPage;
	}
	
	public String getSqlServerPageSql(String sql, int pageNumber, int pageSize,String pk){
		String sqlPage = "select top "+pageSize+" * from ("+sql+") tmp_1 where "+pk+" not in(select top "
					+(pageNumber-1) * pageSize+" "+pk+" from ("+sql+") tmp_2)";
		return sqlPage;
	}
	
	public String getMysqlPageSql(String sql,int pageNumber, int pageSize){
		String sqlPage = sql+" LIMIT "+(pageNumber-1) * pageSize+","+pageSize;
		return sqlPage;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	
}
