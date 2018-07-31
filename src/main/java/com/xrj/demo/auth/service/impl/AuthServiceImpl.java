package com.xrj.demo.auth.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.xrj.demo.auth.service.AuthService;
import com.xrj.demo.common.util.JwtTokenUtil;
import com.xrj.demo.config.JwtUser;
import com.xrj.demo.user.mapper.UserAndRoleMapper;
import com.xrj.demo.user.mapper.UserMapper;
import com.xrj.demo.user.model.RoleEnum;
import com.xrj.demo.user.model.User;
import com.xrj.demo.user.model.UserAndRole;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
    private UserDetailsService userDetailsService;
	
	@Autowired
    private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
    private UserMapper userMapper;
	
	@Autowired
	private UserAndRoleMapper userAndRoleMapper;
    
    @Value("${jwt.tokenHead}")
    private String tokenHead;
	
//	@Override
//    public User register(User userToAdd) {
//        final String username = userToAdd.getUsername();
//        if(userRepository.findByUsername(username)!=null) {
//            return null;
//        }
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        final String rawPassword = userToAdd.getPassword();
//        userToAdd.setPassword(encoder.encode(rawPassword));
//        userToAdd.setLastPasswordResetDate(new Date());
//        userToAdd.setRoles(asList("ROLE_USER"));
//        return userRepository.insert(userToAdd);
//    }
	
	@Override
    public User registerByPhone(User userToAdd) {
        final String phone = userToAdd.getPhone();
        if(userMapper.queryByPhone(phone)!=null) {
            log.info("[registerByPhone]-电话为{}已被注册", phone);
            return null;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String rawPassword = userToAdd.getPassword();
        userToAdd.setPassword(encoder.encode(rawPassword));
        userToAdd.setLastPasswordResetDate(new Date());
        
        int result = userMapper.insertSelective(userToAdd);
        if(result == 0) {
        	log.error("[registerByPhone]-电话为{}未正常插入用户", phone);
        	return null;
        }
        
        User record = new User();
        record.setPhone(userToAdd.getPhone());
        record = userMapper.selectOne(record);
        if(record == null) {
        	return null;
        }
        
        UserAndRole userAndRole = new UserAndRole();
        userAndRole.setRoleId(RoleEnum.ROLE_USER.getCode());
        userAndRole.setUserId(record.getId());
        result = userAndRoleMapper.insertUseGeneratedKeys(userAndRole);
        if(result == 0) {
        	
        }
        return record;
    }

//    @Override
//    public String login(String username, String password) {
//        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
//        // Perform the security
//        final Authentication authentication = authenticationManager.authenticate(upToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        // Reload password post-security so we can generate token
//        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        final String token = jwtTokenUtil.generateToken(userDetails);
//        return token;
//    }
    
    @Override
    public String loginByPhone(String phone, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(phone, password);
        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final JwtUser user = (JwtUser)userDetailsService.loadUserByUsername(phone);
        final String token = jwtTokenUtil.generateToken(user);
        return token;
    }

    @Override
    public String refresh(String oldToken) {
        final String token = oldToken.substring(tokenHead.length());
        String phone = jwtTokenUtil.getUserPhoneFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(phone);
        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())){
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }

}
