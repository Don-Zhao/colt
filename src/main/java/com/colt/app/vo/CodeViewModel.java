package com.colt.app.vo;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class CodeViewModel {

	@NotBlank(message="邮箱不能为空, 请填写")
	@Email(message="邮箱格式不正确，请重新填写")
	private String email;
	
	@NotBlank(message="验证码不能为空")
	private String code;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
