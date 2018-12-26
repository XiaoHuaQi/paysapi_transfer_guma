package com.zixu.paysapi.jpa.dto;

import com.alibaba.fastjson.JSONObject;

public class ReturnDto {

	private int errCode;

	private String errCodeDes;

	private boolean resultCode;

	private Object data;

	public ReturnDto() {

	}

	public ReturnDto(Integer errCode, String errCodeDes, boolean resultCode, Object data) {
		this.errCode = errCode;
		this.errCodeDes = errCodeDes;
		this.resultCode = resultCode;
		this.data = JSONObject.toJSONString(data);
	}

	public ReturnDto(boolean resultCode, Object data) {
		this.resultCode = resultCode;
		this.data = data;
	}

	public ReturnDto(boolean resultCode) {
		this.resultCode = resultCode;
	}

	public ReturnDto(Integer errCode, String errCodeDes, boolean resultCode) {
		super();
		this.errCode = errCode;
		this.errCodeDes = errCodeDes;
		this.resultCode = resultCode;
	}

	public static ReturnDto send(Object data) {

		return send(0, null, true, data);
	}

	public static ReturnDto send(Integer errCode) {

		return send(errCode, null, false, null);
	}

	public static ReturnDto send(Integer errCode, String errCodeDes) {

		return send(errCode, errCodeDes, false, null);
	}

	public static ReturnDto send(boolean resultCode) {

		return send(0, null, resultCode, null);
	}

	public static ReturnDto send(Integer errCode, boolean resultCode, Object data) {

		return send(errCode, null, resultCode, data);
	}

	public static ReturnDto send(Integer errCode, String errCodeDes, boolean resultCode, Object data) {

		if (!resultCode && errCodeDes == null) {
			errCodeDes = getErrCodeDes(errCode);
		}

		return new ReturnDto(errCode, errCodeDes, resultCode, data);
	}

	private static String getErrCodeDes(Integer errCode) {
		String errCodeDes = null;
		switch (errCode) {
		case 100001:
			errCodeDes = "缺少必要参数";
			break;
		case 100002:
			errCodeDes = "用户不存在";
			break;
		case 100003:
			errCodeDes = "密码不正确";
			break;
		case 100004:
			errCodeDes = "登陆过期";
			break;
		case 100005:
			errCodeDes = "系统繁忙";
			break;
		case 100006:
			errCodeDes = "商品名称已存在";
			break;
		case 100007:
			errCodeDes = "商品不存在";
			break;
		case 100008:
			errCodeDes = "该金额已经存在";
			break;
		case 100009:
			errCodeDes = "数据格式有误";
			break;
		case 100010:
			errCodeDes = "未知支付类型";
			break;
		case 100011:
			errCodeDes = "签名错误";
			break;
		case 100012:
			errCodeDes = "无匹配二维码";
			break;
		case 100013:
			errCodeDes = "订单不存在";
			break;
		case 100014:
			errCodeDes = "用户名已经存在";
			break;
		case 100015:
			errCodeDes = "套餐不存在";
			break;
		case 100016:
			errCodeDes = "后台系统配置错误，请联系客服处理";
			break;
		case 100017:
			errCodeDes = "充值金额不存在";
			break;
		case 100018:
			errCodeDes = "账户余额不足";
			break;
		case 100019:
			errCodeDes = "商户未购买套餐";
			break;
		case 100020:
			errCodeDes = "无对应商品";
			break;
		case 100021:
			errCodeDes = "该订单业务已处理";
			break;
		case 100022:
			errCodeDes = "账户已被禁用";
			break;
		case 100023:
			errCodeDes = "注册码无效";
			break;
		case 100024:
			errCodeDes = "toekn有误，请重新点击邮件中的链接";
			break;
		case 100025:
			errCodeDes = "订单与用户不匹配";
			break;
		case 100026:
			errCodeDes = "关系不匹配";
			break;
		case 100027:
			errCodeDes = "重复的商家订单号";
			break;
		case 100028:
			errCodeDes = "支付宝未登陆";
			break;
		case 100029:
			errCodeDes = "app心跳停止";
			break;
		default:
			errCodeDes = "无业务类型";
			break;
		}
		return errCodeDes;
	}

	public Integer getErrCode() {
		return errCode;
	}

	public void setErrCode(Integer errCode) {
		this.errCode = errCode;
	}

	public String getErrCodeDes() {
		return errCodeDes;
	}

	public void setErrCodeDes(String errCodeDes) {
		this.errCodeDes = errCodeDes;
	}

	public boolean isResultCode() {
		return resultCode;
	}

	public void setResultCode(boolean resultCode) {
		this.resultCode = resultCode;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
