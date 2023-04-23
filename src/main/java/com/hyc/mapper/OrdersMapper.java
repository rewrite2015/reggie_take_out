package com.hyc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hyc.pojo.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
