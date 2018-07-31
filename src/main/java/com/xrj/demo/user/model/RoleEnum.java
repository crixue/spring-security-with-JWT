package com.xrj.demo.user.model;

public enum RoleEnum {
	
	ROLE_ADMIN(1, "ADMIN"),
	ROLE_USER(2, "USER"),
	ROLE_VIP(3, "VIP"),
	ROLE_GUEST(4, "GUEST");

	private final int code;
	private final String desc;
	public int getCode() {
		return code;
	}
	public String getDesc() {
		return desc;
	}
	private RoleEnum(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
}
