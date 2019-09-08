package com.colt.app.response;

public class CommonReturnType {
	private String status;
	
	private Object result;
	
	public static CommonReturnType create(Object result) {
		return CommonReturnType.create("success", result);
	}
	
	public static CommonReturnType create(String status, Object result) {
		CommonReturnType type = new CommonReturnType();
		type.setStatus(status);
		type.setResult(result);
		return type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
