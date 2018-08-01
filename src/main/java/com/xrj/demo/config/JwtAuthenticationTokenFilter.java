package com.xrj.demo.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.xrj.demo.common.util.JwtTokenUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter{

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader(this.tokenHeader);
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            final String authToken = authHeader.substring(tokenHead.length()); // The part after "Bearer "
            // Long userId = jwtTokenUtil.getUserIdFromToken(authToken);
            String phone = jwtTokenUtil.getUserPhoneFromToken(authToken);

            log.info("checking authentication " + phone);

            if (phone != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 如果我们足够相信token中的数据，也就是我们足够相信签名token的secret的机制足够好
                // 这种情况下，我们可以不用再查询数据库，而直接采用token中的数据
                // 本例中，我们还是通过Spring Security的 @UserDetailsService 进行了数据查询
                // 但简单验证的话，你可以采用直接验证token是否合法来避免昂贵的数据查询
                UserDetails userDetails = userDetailsService.loadUserByUsername(phone);

                if (jwtTokenUtil.validateTokenByPhone(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                            request));
                    log.info("authenticated user: " + phone + ", setting security context");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                
                //方法2
//                JwtUser jwtUser = jwtTokenUtil.getJwtUser(authHeader);
//                if (jwtTokenUtil.validateToken(authToken, jwtUser)) {
//                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                            userDetails, null, userDetails.getAuthorities());
//                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
//                            request));
//                    log.info("authenticated user " + userId + ", setting security context");
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                }
                
            }
        }

        filterChain.doFilter(request, response);
	}
	
	private JwtUser parseToken(String authHeader) {
		JwtUser jwtUser = jwtTokenUtil.getJwtUser(authHeader);
		return jwtUser;
	}

}
