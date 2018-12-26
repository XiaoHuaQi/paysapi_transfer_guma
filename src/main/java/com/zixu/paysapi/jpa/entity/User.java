package com.zixu.paysapi.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="com_zixu_user")
public class User implements Serializable{
	

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="id")
	private String id;
	
	@Column(name="userName")
	private String userName;
	
	@Column(name="account")
	private String account;
	
	@Column(name="password")
	private String password;
	
	@Column(name="uid")
	private String uid;
	
	@Column(name="token")
	private String token;
	
	@Column(name="wechat")
	private String wechat;
	
	@Column(name="qqNumber")
	private String qqNumber;
	
	@Column(name="mobile")
	private String mobile;
	
	@Column(name="type")
	private String type;
	
	@Column(name="state")
	private String state;
	
	@Column(name="useAdmission")
	private String useAdmission;
	
	@Transient
	private int sumFee;
	
	@Transient
	private int procedures;
	
	public String getUseAdmission() {
		return useAdmission;
	}

	public void setUseAdmission(String useAdmission) {
		this.useAdmission = useAdmission;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getSumFee() {
		return sumFee;
	}

	public void setSumFee(int sumFee) {
		this.sumFee = sumFee;
	}

	public int getProcedures() {
		return procedures;
	}

	public void setProcedures(int procedures) {
		this.procedures = procedures;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public String getQqNumber() {
		return qqNumber;
	}

	public void setQqNumber(String qqNumber) {
		this.qqNumber = qqNumber;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
