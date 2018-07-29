package com.xrj.demo.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.xrj.demo.common.mapper.MyBaseMapper;
import com.xrj.demo.user.model.UserAndRole;

@Mapper
public interface UserAndRoleMapper extends MyBaseMapper<UserAndRole> {
}