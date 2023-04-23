package com.hyc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.common.BaseContext;
import com.hyc.common.Code;
import com.hyc.common.Result;
import com.hyc.pojo.AddressBook;
import com.hyc.pojo.SetMeal;
import com.hyc.pojo.User;
import com.hyc.service.imp.AddressBookServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookServiceImpl addressBookService;
    /**
     * 新增
     */
    @PostMapping()
    public Result<AddressBook> save(@RequestBody AddressBook addressBook){
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("AddressBookMapper:{}",addressBook);
        addressBookService.save(addressBook);
        return new Result<>(Code.DL_OK,addressBook);
    }
    /**
     * 设置默认地址
     */
    @PutMapping("default")
    public Result<AddressBook> setDefault(@RequestBody AddressBook addressBook){
        LambdaUpdateWrapper<AddressBook> wrapper=new LambdaUpdateWrapper();
        wrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        wrapper.set(AddressBook::getIsDefault,0);
        addressBookService.update(wrapper);

        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return new Result<>(Code.DL_OK,addressBook);
    }
    /**
     * 根据id查询信息
     */
    @GetMapping("/{id}")
    public Result<AddressBook> get(@PathVariable Long id){
        AddressBook byId = addressBookService.getById(id);
        if(byId!=null){
            return new Result<>(Code.DL_OK,byId);
        }
        return new Result<>(Code.DL_ERR,"没有该信息");
    }

    /**
     * 查询默认地址
     * @return
     */
    @GetMapping("/default")
    public Result<AddressBook> getDefault(){
        LambdaQueryWrapper<AddressBook> wrapper=new LambdaQueryWrapper();
        wrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        wrapper.eq(AddressBook::getIsDefault,1);
        AddressBook one = addressBookService.getOne(wrapper);
        if(one!=null){
            return new Result<>(Code.DL_OK,one);
        }
        return new Result<>(Code.DL_ERR,"没有该信息");
    }
    /**
     * 查询所有地址
     */
    @GetMapping("/list")
    public Result<List<AddressBook>> list(AddressBook addressBook){
        LambdaQueryWrapper<AddressBook> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(addressBook!=null,AddressBook::getUserId,BaseContext.getCurrentId());
        wrapper.orderByDesc(AddressBook::getUpdateTime);

        List<AddressBook> list = addressBookService.list(wrapper);
        if(list!=null){
            return new Result<>(Code.DL_OK,list);
        }
        return new Result<>(Code.DL_ERR,"没有该信息");
    }
    /**
     * 修改地址
     */
    @PutMapping
    public Result<String> update(@RequestBody AddressBook addressBook){

        boolean b = addressBookService.updateById(addressBook);
        if(b){
            return new Result<>(Code.DL_OK,"修改成功");
        }
        return new Result<>(Code.DL_ERR,"修改失败");
    }
    /**
     * 删除地址
     */
    @DeleteMapping
    public Result<String> delete(@PathVariable Long id){
        boolean b = addressBookService.removeById(id);
        if(b){
            return new Result<>(Code.DL_OK,"删除成功");
        }
        return new Result<>(Code.DL_ERR,"删除失败");
    }
    /**
     * 获取最新地址
     */
    @GetMapping("/lastUpdate")
    public Result<AddressBook> lastUpdate(){
        LambdaQueryWrapper<AddressBook> wrapper=new LambdaQueryWrapper();
        wrapper.orderByDesc(AddressBook::getUpdateTime);
        AddressBook one = addressBookService.getOne(wrapper);
        return new Result<>(Code.DL_OK,one);

    }
}
