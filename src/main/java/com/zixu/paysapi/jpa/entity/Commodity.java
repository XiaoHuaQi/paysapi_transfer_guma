package com.zixu.paysapi.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="com_zixu_commodity")
public class Commodity implements Serializable{
	

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="userID")
	private String userID;
	
	@Column(name="time")
	private String time;
	
	@Column(name="fee")
	private int fee;
	
	@Transient
	private int wechatQrcodeNum;
	
	@Transient
	private int alipayQrcodeNum;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public int getWechatQrcodeNum() {
		return wechatQrcodeNum;
	}

	public void setWechatQrcodeNum(int wechatQrcodeNum) {
		this.wechatQrcodeNum = wechatQrcodeNum;
	}

	public int getAlipayQrcodeNum() {
		return alipayQrcodeNum;
	}

	public void setAlipayQrcodeNum(int alipayQrcodeNum) {
		this.alipayQrcodeNum = alipayQrcodeNum;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	
}
