package com.hyc.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyc.pojo.DishFlavor;
import com.hyc.mapper.DishFlavorMapper;
import com.hyc.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImp extends ServiceImpl<DishFlavorMapper,DishFlavor> implements DishFlavorService {

}
