package com.hyc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.hyc.common.BaseContext;
import com.hyc.common.Code;
import com.hyc.common.Result;
import com.hyc.pojo.ShoppingCart;
import com.hyc.service.imp.ShoppingCartServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartServiceImp shoppingCartServiceImp;

    /**
     * 查询购物车的商品
     * @return
     */
    @GetMapping("/list")
    public Result<ShoppingCart> list(){
        LambdaQueryWrapper<ShoppingCart> wrapper=new LambdaQueryWrapper();
        wrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartServiceImp.list(wrapper);

        return new Result<>(Code.DL_OK,list);
    }

    /**
     * 添加菜品到购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public Result<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        shoppingCart.setUserId(BaseContext.getCurrentId());
        Long dishId = shoppingCart.getDishId();


        LambdaQueryWrapper<ShoppingCart> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        if(dishId!=null) {
            wrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }else {
            wrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCartServiceImp.getOne(wrapper);
        if(one!=null) {
            shoppingCart.setId(one.getId());
            shoppingCart.setNumber(one.getNumber()+1);
            shoppingCartServiceImp.updateById(shoppingCart);
        }else{
            shoppingCart.setNumber(1);
            shoppingCartServiceImp.save(shoppingCart);
            one=shoppingCart;
        }
        return new Result<>(Code.DL_OK,one,"添加成功");
        //return new Result<>(Code.DL_ERR,shoppingCart,"添加失败");
    }

    /**
     * 修改购物车菜品
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public Result update(@RequestBody ShoppingCart shoppingCart){
        log.info("**************{}",shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        Long dishId = shoppingCart.getDishId();


        LambdaQueryWrapper<ShoppingCart> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        if(dishId!=null) {
            wrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }else {
            wrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCartServiceImp.getOne(wrapper);
        if(one!=null) {
            shoppingCart.setId(one.getId());
            shoppingCart.setNumber(one.getNumber()-1);
            shoppingCartServiceImp.updateById(shoppingCart);
        }
        one = shoppingCartServiceImp.getOne(wrapper);
        log.info("++++++++++++++++++++{}",one);
        /*LambdaUpdateWrapper<ShoppingCart> wrapper=new LambdaUpdateWrapper<>();
        wrapper.eq(shoppingCart!=null,ShoppingCart::getId,shoppingCart.getId());

        boolean update = shoppingCartServiceImp.update(wrapper);*/
        return new Result<>(Code.DL_OK,one,"修改成功");
        //return new Result<>(Code.DL_ERR,"修改失败");
    }
    /**
     * 清空购物车商品
     */
    @DeleteMapping("/clean")
    public Result<String> clean(){
        LambdaUpdateWrapper<ShoppingCart> wrapper=new LambdaUpdateWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        shoppingCartServiceImp.remove(wrapper);
        return new Result<>(Code.DL_OK,"删除成功");
    }

}
