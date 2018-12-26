package com.zixu.paysapi.jpa.dto;

import java.math.BigInteger;

public class StatisticsDto {
	
	private BigInteger thisMonthFee;
	
	private BigInteger thirtyDateFee;
	
	private BigInteger nowDateFee;
	
	private BigInteger yesterdayFee;
	
	private BigInteger thisMonthNum;
	
	private BigInteger thirtyDateNum;
	
	private BigInteger nowDateNum;
	
	private BigInteger yesterdayNum;

	public BigInteger getThisMonthFee() {
		return thisMonthFee;
	}

	public void setThisMonthFee(BigInteger thisMonthFee) {
		this.thisMonthFee = thisMonthFee;
	}

	public BigInteger getThirtyDateFee() {
		return thirtyDateFee;
	}

	public void setThirtyDateFee(BigInteger thirtyDateFee) {
		this.thirtyDateFee = thirtyDateFee;
	}

	public BigInteger getNowDateFee() {
		return nowDateFee;
	}

	public void setNowDateFee(BigInteger nowDateFee) {
		this.nowDateFee = nowDateFee;
	}

	public BigInteger getYesterdayFee() {
		return yesterdayFee;
	}

	public void setYesterdayFee(BigInteger yesterdayFee) {
		this.yesterdayFee = yesterdayFee;
	}

	public BigInteger getThisMonthNum() {
		return thisMonthNum;
	}

	public void setThisMonthNum(BigInteger thisMonthNum) {
		this.thisMonthNum = thisMonthNum;
	}

	public BigInteger getThirtyDateNum() {
		return thirtyDateNum;
	}

	public void setThirtyDateNum(BigInteger thirtyDateNum) {
		this.thirtyDateNum = thirtyDateNum;
	}

	public BigInteger getNowDateNum() {
		return nowDateNum;
	}

	public void setNowDateNum(BigInteger nowDateNum) {
		this.nowDateNum = nowDateNum;
	}

	public BigInteger getYesterdayNum() {
		return yesterdayNum;
	}

	public void setYesterdayNum(BigInteger yesterdayNum) {
		this.yesterdayNum = yesterdayNum;
	}

	
}
