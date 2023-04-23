package com.hyc.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyc.mapper.ShoppingCartMapper;
import com.hyc.pojo.ShoppingCart;
import com.hyc.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImp extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {



}
