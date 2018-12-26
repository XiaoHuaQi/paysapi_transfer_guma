package com.zixu.paysapi.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="com_zixu_change_detail")
public class ChangeDetail implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", unique = true,nullable=false)
	private Long id;
	
	@Column(name="userID")
	private String userID;
	
	@Column(name="time")
	private String time;
	
	@Column(name="remarks")
	private String remarks;
	
	@Column(name="beforeFee")
	private long beforeFee;
	
	@Column(name="afterFee")
	private long afterFee;
	
	@Column(name="changeFee")
	private long changeFee;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public long getBeforeFee() {
		return beforeFee;
	}

	public void setBeforeFee(long beforeFee) {
		this.beforeFee = beforeFee;
	}

	public long getAfterFee() {
		return afterFee;
	}

	public void setAfterFee(long afterFee) {
		this.afterFee = afterFee;
	}

	public long getChangeFee() {
		return changeFee;
	}

	public void setChangeFee(long changeFee) {
		this.changeFee = changeFee;
	}
	

	
}