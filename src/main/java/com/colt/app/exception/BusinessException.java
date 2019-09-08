package com.colt.app.exception;

import com.colt.app.error.CommonError;

// 包装器异常实现
public class BusinessException extends Exception implements CommonError {

	private static final long serialVersionUID = 1L;
	
	private CommonError commonError;
	
	public BusinessException(CommonError commonError) {
		super();
		this.commonError = commonError;
	}
	
	public BusinessException(CommonError commonError, String errorMsg) {
		super();
		this.commonError = commonError;
		this.commonError.setErrorMsg(errorMsg);
	}
	
	@Override
	public int getErrorCode() {
		return this.commonError.getErrorCode();
	}

	@Override
	public String getErrorMsg() {
		return this.commonError.getErrorMsg();
	}

	@Override
	public CommonError setErrorMsg(String errorMsg) {
		this.commonError.setErrorMsg(errorMsg);
		return this;
	}

}
