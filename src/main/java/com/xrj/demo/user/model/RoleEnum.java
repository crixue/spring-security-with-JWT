package com.xrj.demo.user.model;

public enum RoleEnum {
	
	ROLE_ADMIN(1, "ROLE_ADMIN"),
	ROLE_USER(2, "ROLE_USER"),
	ROLE_VIP(3, "ROLE_VIP"),
	ROLE_GUEST(4, "ROLE_GUEST");

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
