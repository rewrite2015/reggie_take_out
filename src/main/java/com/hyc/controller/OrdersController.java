package com.hyc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.common.Code;
import com.hyc.common.Result;
import com.hyc.pojo.Orders;
import com.hyc.pojo.dto.EmployeeDto;
import com.hyc.service.imp.OrdersServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {
    @Autowired
    private OrdersServiceImp osi;
    @GetMapping("/userPage")
    public Result<Orders> userPage(int page, int pageSize){
        return new Result<>(Code.DL_OK,osi.page(page,pageSize));
    }

    @PostMapping("/submit")
    public Result<String> submit(@RequestBody Orders orders){
        log.info("订单数据{}",orders);
        if(osi.submit(orders)) {
            return new Result<>(Code.DL_OK, "");
        }
        return new Result<>(Code.DL_ERR, "");
    }
}
