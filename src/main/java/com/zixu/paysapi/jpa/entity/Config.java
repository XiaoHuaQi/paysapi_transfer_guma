package com.zixu.paysapi.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="com_zixu_config")
public class Config implements Serializable{
	

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="id")
	private String id;
	
	@Column(name="userID")
	private String userID;
	
	@Column(name="overdueTime")
	private int overdueTime;
	
	@Column(name="minFee")
	private int minFee;
	
	@Column(name="immediately")
	private String immediately;

	public int getMinFee() {
		return minFee;
	}

	public void setMinFee(int minFee) {
		this.minFee = minFee;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public int getOverdueTime() {
		return overdueTime;
	}

	public void setOverdueTime(int overdueTime) {
		this.overdueTime = overdueTime;
	}

	public String getImmediately() {
		return immediately;
	}

	public void setImmediately(String immediately) {
		this.immediately = immediately;
	}
	
}
