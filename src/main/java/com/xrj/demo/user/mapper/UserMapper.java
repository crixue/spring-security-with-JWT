package com.xrj.demo.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.xrj.demo.common.mapper.MyBaseMapper;
import com.xrj.demo.user.model.User;
import com.xrj.demo.user.vo.UserVO;

@Mapper
public interface UserMapper extends MyBaseMapper<User> {
	
	UserVO queryByPhone(String phone);
}