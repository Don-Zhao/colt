package com.colt.app.error;

public enum BusinessError implements CommonError {
	
	PARAMETER_VALIDATION_ERROR(00001, "参数不合法"),
	USER_NOT_EXIST(10001, "用户不存在");
	
	private int errorCode;
	
	private String errorMsg;
	
	private BusinessError(int errorCode, String errorMsg) {
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	@Override
	public int getErrorCode() {
		return this.errorCode;
	}

	@Override
	public String getErrorMsg() {
		return this.errorMsg;
	}

	@Override
	public CommonError setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
		return this;
	}

}
