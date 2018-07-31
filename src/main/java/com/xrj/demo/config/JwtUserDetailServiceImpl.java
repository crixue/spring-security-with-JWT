package com.xrj.demo.config;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.xrj.demo.user.mapper.RoleMapper;
import com.xrj.demo.user.mapper.UserMapper;
import com.xrj.demo.user.model.User;
import com.xrj.demo.user.vo.UserVO;

@Service("userDetailsService")
public class JwtUserDetailServiceImpl implements UserDetailsService {
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private RoleMapper roleMapper;
	
//	@Override
//	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
//		User user = userMapper.selectByPrimaryKey(Long.valueOf(userId));
//		if(user != null) {
//			UserVO userVO = new UserVO();
//			BeanUtils.copyProperties(user, userVO);
//			List<String> roles = roleMapper.queryRolesByUserId(user.getId());
//			userVO.setRoles(roles);
//			return JwtUserFactory.create(userVO);
//		}
//		throw new UsernameNotFoundException(String.format("No user found with username '%s'.", userId));
//	}
	
	@Override
	public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
		User record = new User();
		record.setPhone(phone);
		
		User user = userMapper.selectOne(record);
		if(user != null) {
			UserVO userVO = new UserVO();
			BeanUtils.copyProperties(user, userVO);
			List<String> roles = roleMapper.queryRolesByUserId(user.getId());
			userVO.setRoles(roles);
			return JwtUserFactory.create(userVO);
		}
		throw new UsernameNotFoundException(String.format("No user found with username '%s'.", phone));
	}

}
