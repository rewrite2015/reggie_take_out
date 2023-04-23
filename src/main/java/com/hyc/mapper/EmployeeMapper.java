package com.hyc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hyc.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
