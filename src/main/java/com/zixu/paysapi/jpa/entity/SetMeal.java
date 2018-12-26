package com.zixu.paysapi.jpa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="com_zixu_set_meal")
public class SetMeal implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="id")
	private String id;
	
	@Column(name="title")
	private String title;
	
	@Column(name="number")
	private int number;
	
	@Column(name="describes")
	private String describes;
	
	@Column(name="price")
	private int price;
	
	@Column(name="type")
	private String type;
	
	@Column(name="procedures")
	private int procedures;
	
	@Transient
	private int wechatQrcodeNum;
	
	@Transient
	private int alipayQrcodeNum;
	
	
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

	public int getProcedures() {
		return procedures;
	}

	public void setProcedures(int procedures) {
		this.procedures = procedures;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}


	public String getDescribes() {
		return describes;
	}

	public void setDescribes(String describes) {
		this.describes = describes;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
