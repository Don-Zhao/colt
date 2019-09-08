package com.colt.app.enums;

import com.colt.app.utils.ColtUtils;

public enum CharacterType {
	
	HLLF_DIGIT_CHAR(0),
	HLLF_DIGIT(1);
	
	private String charStr;
	
	private int value;
	
	private CharacterType(int value) {
		this.value = value;
		this.charStr = ColtUtils.CHAR_STR[this.value];
	}
	
	public int getValue() {
		return this.value;
	}
	
	public String getCharStr() {
		return this.charStr;
	}
}
