package com.zixu.paysapi.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="com_zixu_set_meal_purchase")
public class SetMealPurchase implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="id")
	private String id;
	
	@Column(name="expireDate")
	private String expireDate;
	
	@Column(name="userID")
	private String userID;
	
	@Column(name="setMealID")
	private String setMealID;
	
	@Column(name="state")
	private String state;
	
	@Column(name="procedures")
	private int procedures;
	
	@Transient
	private String userName;
	
	@Transient
	private String account;
	
	@Transient
	private String mobile;
	
	@Transient
	private String setMealTitle;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSetMealTitle() {
		return setMealTitle;
	}

	public void setSetMealTitle(String setMealTitle) {
		this.setMealTitle = setMealTitle;
	}

	public String getSetMealID() {
		return setMealID;
	}

	public void setSetMealID(String setMealID) {
		this.setMealID = setMealID;
	}

	public int getProcedures() {
		return procedures;
	}

	public void setProcedures(int procedures) {
		this.procedures = procedures;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

}
