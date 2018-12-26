package com.zixu.paysapi.util;

/**
 * 封装返回操作结果
 * @author Administrator
 *
 */
public class ResDto {

	public static final int SUCCESS_CODE = 200;
	public static final int ERROR_CODE = 500;
	
	public static final String PARAM_ERROR = "参数错误";
	public static final String SERVER_ERROR = "服务异常";
	public static final String SUCCESS = "操作成功";

	private int status;
	private String msg;
	private Object result;
	
	public ResDto(){
		
	}
	
	public ResDto(int status,String msg){
		this.status = status;
		this.msg = msg;
	}

	public ResDto(int status,String msg,Object result){
		this.status = status;
		this.msg = msg;
		this.result = result;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
	
}
