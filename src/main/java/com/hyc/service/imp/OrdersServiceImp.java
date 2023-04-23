package com.hyc.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.common.*;
import com.hyc.mapper.OrdersMapper;
import com.hyc.pojo.*;
import com.hyc.pojo.dto.EmployeeDto;
import com.hyc.pojo.dto.OrdersDto;
import com.hyc.service.OrdersService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImp implements OrdersService {
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private ShoppingCartServiceImp ssi;
    @Autowired
    private UserServiceImp usi;
    @Autowired
    private AddressBookServiceImpl asi;
    @Autowired
    private OrderDetailServiceImp osi;

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    public EmployeeDto page(int page, int pageSize){
        EmployeeDto efz=new EmployeeDto();
        Page<Orders> p=new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getUserId,BaseContext.getCurrentId());
        wrapper.orderByDesc(Orders::getOrderTime);
        ordersMapper.selectPage(p,wrapper);

        LambdaQueryWrapper<OrderDetail> queryWrapper=new LambdaQueryWrapper<>();
        List<OrdersDto> ordersDtoList=new ArrayList<>();
        OrdersDto ordersDto=new OrdersDto();
        List<Orders> records = p.getRecords();
        for(Orders orders:records){
            queryWrapper.eq(OrderDetail::getOrderId,orders.getId());
            List<OrderDetail> list = osi.list(queryWrapper);
            BeanUtils.copyProperties(orders,ordersDto);
            ordersDto.setOrderDetails(list);
            ordersDtoList.add(ordersDto);
        }


        efz.setList(ordersDtoList);
        //efz.setTotal(p.getTotal());
        return efz;
    }

    /**
     * 用户下单
     * @return
     */
    public Boolean submit(Orders orders){
        Long currentId = BaseContext.getCurrentId();
        orders.setUserId(currentId);
        Long id=IdWorker.getId();//订单号随机生成
        //查询购物车
        LambdaQueryWrapper<ShoppingCart> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,currentId);
        List<ShoppingCart> shoppingCarts = ssi.list(wrapper);
        if(shoppingCarts==null||shoppingCarts.size()==0){
            throw new BusinessException("购物车为空不能下单");
        }
        AtomicInteger amount=new AtomicInteger(0);
        //遍历购物车取出金额
        List<OrderDetail> orderDetails=shoppingCarts.stream().map((item)->{
            OrderDetail orderDetail=new OrderDetail();
            orderDetail.setOrderId(id);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            //累加            item.getAmount()金额.multiply乘以(new BigDecimal(item.getNumber())).intValue())数量.intValue()转成int
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());
        //查询用户信息
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId,currentId);
        User user = usi.getOne(queryWrapper);
        //查询地址信息
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = asi.getById(addressBookId);
        if(addressBook==null){
            throw new BusinessException("用户地址信息有误，不能下单");
        }
        //向订单表添加数据
        orders.setId(id);
        orders.setNumber(id.toString());
        orders.setUserId(currentId);
        orders.setPhone(user.getPhone());
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setAddress((addressBook.getProvinceName()==null?"":addressBook.getProvinceName())
        +(addressBook.getCityName()==null?"":addressBook.getCityName())
        +(addressBook.getDistrictName()==null?"":addressBook.getDistrictName())
        +(addressBook.getDetail()==null?"":addressBook.getDetail()));
        ordersMapper.insert(orders);
        //订单详情
        osi.saveBatch(orderDetails);
        //清空购物车数据
        boolean remove = ssi.remove(wrapper);
        return remove;
    }
}
