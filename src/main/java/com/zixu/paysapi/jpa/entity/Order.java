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
@Table(name="com_zixu_order")
public class Order implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", unique = true,nullable=false)
	private Long id;
	
	@Column(name="userID")
	private String userID;
	
	@Column(name="uid")
	private String uid;
	
	@Column(name="type")
	private String type;
	
	@Column(name="notifyUrl")
	private String notifyUrl;
	
	@Column(name="outTradeNo")
	private String outTradeNo;
	
	@Column(name="commdityName")
	private String commdityName;
	
	@Column(name="nonceStr")
	private String nonceStr;
	
	@Column(name="payState")
	private String payState;
	
	@Column(name="notifyState")
	private String notifyState;
	
	@Column(name="commdityID")
	private String commdityID;
	
	@Column(name="payTime")
	private String payTime;
	
	@Column(name="notifyTime")
	private String notifyTime;
	
	@Column(name="errorMsg")
	private String errorMsg;
	
	@Column(name="qrcodeUrl")
	private String qrcodeUrl;
	
	@Column(name="orderType")
	private String orderType;
	
	@Column(name="price")
	private int price;
	
	@Column(name="procedures")
	private int procedures;
	
	@Column(name="proceduresFee")
	private int proceduresFee;
	
	@Column(name="chainAdd")
	private String chainAdd;
	
	@Column(name="systemID")
	private String systemID;
	
	@Transient
	private String userName;
	
	
	public String getSystemID() {
		return systemID;
	}

	public void setSystemID(String systemID) {
		this.systemID = systemID;
	}

	public String getChainAdd() {
		return chainAdd;
	}

	public void setChainAdd(String chainAdd) {
		this.chainAdd = chainAdd;
	}

	public int getProcedures() {
		return procedures;
	}

	public void setProcedures(int procedures) {
		this.procedures = procedures;
	}

	public int getProceduresFee() {
		return proceduresFee;
	}

	public void setProceduresFee(int proceduresFee) {
		this.proceduresFee = proceduresFee;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getQrcodeUrl() {
		return qrcodeUrl;
	}

	public void setQrcodeUrl(String qrcodeUrl) {
		this.qrcodeUrl = qrcodeUrl;
	}

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

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getCommdityName() {
		return commdityName;
	}

	public void setCommdityName(String commdityName) {
		this.commdityName = commdityName;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getPayState() {
		return payState;
	}

	public void setPayState(String payState) {
		this.payState = payState;
	}

	public String getNotifyState() {
		return notifyState;
	}

	public void setNotifyState(String notifyState) {
		this.notifyState = notifyState;
	}

	public String getCommdityID() {
		return commdityID;
	}

	public void setCommdityID(String commdityID) {
		this.commdityID = commdityID;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getNotifyTime() {
		return notifyTime;
	}

	public void setNotifyTime(String notifyTime) {
		this.notifyTime = notifyTime;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	
}