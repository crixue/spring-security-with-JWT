package com.xrj.demo.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xrj.demo.common.mapper.MyBaseMapper;
import com.xrj.demo.user.model.Role;

@Mapper
public interface RoleMapper extends MyBaseMapper<Role> {
	
	List<String> queryRolesByUserPhone(String phone); 
	
	List<String> queryRolesByUserId(Long userId); 
}