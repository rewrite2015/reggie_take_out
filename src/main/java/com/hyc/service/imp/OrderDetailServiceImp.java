package com.hyc.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyc.mapper.OrderDetailMapper;
import com.hyc.pojo.OrderDetail;
import com.hyc.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImp extends ServiceImpl<OrderDetailMapper, OrderDetail>implements OrderDetailService {

}
