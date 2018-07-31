package com.xrj.demo.common.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.xrj.demo.config.JwtUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = -3301605591108950415L;

	public static final String CLAIM_KEY_USERNAME = "username";
	public static final String CLAIM_KEY_CREATED = "created";
	public static final String CLAIM_KEY_PHONE = "phone";
	public static final String CLAIM_KEY_EMAIL = "email";
	public static final String CLAIM_KEY_USER_ID = "userid";

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long expiration;

	public JwtUser getJwtUser(String token) {
		JwtUser jwtUser = null;
		Long userId = null;
		final Claims claims = getClaimsFromToken(token);
		try {
			userId = (Long) claims.get(CLAIM_KEY_USER_ID);
			
			String username = "";
			if(claims.get(CLAIM_KEY_USERNAME) != null) {
				username = (String) claims.get(CLAIM_KEY_USERNAME);
			}
			
			String phone = "";
			if(claims.get(CLAIM_KEY_PHONE) != null) {
				phone = (String) claims.get(CLAIM_KEY_PHONE);
			}
			
			Date created = null;
			if(claims.get(CLAIM_KEY_CREATED) != null) {
				created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
			}
			
			List<? extends GrantedAuthority> authorities  = new ArrayList<>();  //TODO
			jwtUser = new JwtUser(userId, username, "", "", phone, authorities, created);
		} catch (Exception e) {
			jwtUser = null;
		}

		return jwtUser;
	}

	public String getUsernameFromToken(String token) {
		String username;
		try {
			final Claims claims = getClaimsFromToken(token);
			username = (String) claims.get(CLAIM_KEY_USERNAME);
		} catch (Exception e) {
			username = null;
		}
		return username;
	}

	public Long getUserIdFromToken(String token) {
		Long userId;
		try {
			final Claims claims = getClaimsFromToken(token);
			userId = (Long) claims.get(CLAIM_KEY_USER_ID);
		} catch (Exception e) {
			userId = null;
		}
		return userId;
	}

	public String getUserPhoneFromToken(String token) {
		String phone;
		try {
			final Claims claims = getClaimsFromToken(token);
			phone = (String) claims.get(CLAIM_KEY_PHONE);
		} catch (Exception e) {
			phone = null;
		}
		return phone;
	}

	public Date getCreatedDateFromToken(String token) {
		Date created;
		try {
			final Claims claims = getClaimsFromToken(token);
			created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
		} catch (Exception e) {
			created = null;
		}
		return created;
	}

	public Date getExpirationDateFromToken(String token) {
		Date expiration;
		try {
			final Claims claims = getClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (Exception e) {
			expiration = null;
		}
		return expiration;
	}

	/**
	 * 由token解析需要的内容
	 * 
	 * @param token
	 * @return
	 */
	private Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	private Date generateExpirationDate() {
		return new Date(System.currentTimeMillis() + expiration * 1000);
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
		return (lastPasswordReset != null && created.before(lastPasswordReset));
	}

	public String generateToken(Map<String, Object> claims) {
		return Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate())
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
		final Date created = getCreatedDateFromToken(token);
		return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset) && !isTokenExpired(token);
	}

	public String refreshToken(String token) {
		String refreshedToken;
		try {
			final Claims claims = getClaimsFromToken(token);
			claims.put(CLAIM_KEY_CREATED, new Date());
			refreshedToken = generateToken(claims);
		} catch (Exception e) {
			refreshedToken = null;
		}
		return refreshedToken;
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		JwtUser user = (JwtUser) userDetails;
		final String username = getUsernameFromToken(token);
		final Date created = getCreatedDateFromToken(token);
		// final Date expiration = getExpirationDateFromToken(token);
		return (username.equals(user.getUsername()) && !isTokenExpired(token)
				&& !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate()));
	}
	
    public String generateToken(JwtUser userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_PHONE, userDetails.getPhone());
        claims.put(CLAIM_KEY_EMAIL, userDetails.getEmail());
        claims.put(CLAIM_KEY_USER_ID, userDetails.getId());
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }
}
