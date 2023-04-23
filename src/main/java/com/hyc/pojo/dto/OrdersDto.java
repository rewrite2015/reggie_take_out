package com.hyc.pojo.dto;

import com.hyc.pojo.OrderDetail;
import com.hyc.pojo.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrdersDto extends Orders{
    private List<OrderDetail> orderDetails;
}
