package com.hyc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hyc.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
