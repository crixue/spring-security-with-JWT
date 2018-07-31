package com.xrj.demo.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;

public class JWTAuthentication extends AbstractAuthenticationToken {
	
	public JWTAuthentication(UserDetails principal, String jsonWebToken) {
		super(principal.getAuthorities());
        this.principal = principal;
        this.jsonWebToken = jsonWebToken;
	}

	private static final long serialVersionUID = 899695095799851561L;
	
	private UserDetails principal;
    private String jsonWebToken;


	@Override
	public Object getCredentials() {
		return null;
	}
	
    public String getJsonWebToken() {
        return jsonWebToken;
    }

	@Override
	public Object getPrincipal() {
		return principal;
	}

}
