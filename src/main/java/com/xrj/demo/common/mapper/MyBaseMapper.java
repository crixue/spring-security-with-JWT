package com.xrj.demo.common.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@org.apache.ibatis.annotations.Mapper
public interface MyBaseMapper<T> extends Mapper<T>, MySqlMapper<T> {

}
