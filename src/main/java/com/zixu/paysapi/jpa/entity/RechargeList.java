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
@Table(name="com_zixu_recharge_list")
public class RechargeList implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name="title")
	private String title;
	
	@Column(name="changeDate")
	private String changeDate;
	
	@Column(name="price")
	private int price;
	
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(String changeDate) {
		this.changeDate = changeDate;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	
}
