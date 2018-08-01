package com.xrj.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import com.xrj.demo.common.util.JwtTokenUtil;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Authentication authenticatedUser = null;
        // Only process the PreAuthenticatedAuthenticationToken
        if (authentication.getClass().isAssignableFrom(PreAuthenticatedAuthenticationToken.class)
                && authentication.getPrincipal() != null) {
            String tokenHeader = (String) authentication.getPrincipal();
            UserDetails userDetails = jwtTokenUtil.getJwtUser(tokenHeader);
            if (userDetails != null) {
                authenticatedUser = new JWTAuthentication(userDetails, tokenHeader);
            }
        } else {
            // It is already a JsonWebTokenAuthentication
            authenticatedUser = authentication;
        }
        return authenticatedUser;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return
                authentication.isAssignableFrom(
                        PreAuthenticatedAuthenticationToken.class) ||
                        authentication.isAssignableFrom(
                                JWTAuthentication.class);
	}

}
