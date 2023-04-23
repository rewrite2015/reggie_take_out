package com.hyc.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.mapper.CategoryMapper;
import com.hyc.mapper.DishFlavorMapper;
import com.hyc.mapper.DishMapper;
import com.hyc.pojo.Category;
import com.hyc.pojo.Dish;
import com.hyc.pojo.DishFlavor;
import com.hyc.pojo.SetMeal;
import com.hyc.pojo.dto.DishDto;
import com.hyc.pojo.dto.EmployeeDto;
import com.hyc.service.DishFlavorService;
import com.hyc.service.DishService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImp implements DishService{
    @Autowired
    private DishFlavorService dishService;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    /**
     *分页加模糊查询
     */
    public EmployeeDto page(int a, int b, String name){
        EmployeeDto efz=new EmployeeDto();
        Page<Dish> p=new Page(a,b);
        Page<DishDto> dishDtoPage=new Page<>();
        //条件封装
        QueryWrapper<Dish> queryWrapper = new QueryWrapper<>();
        //模糊查询
        queryWrapper.like(name!=null, "NAME", name);
        //添加排序条件
        queryWrapper.lambda().orderByDesc(Dish::getUpdateTime);
        //List<Dish> userList = dishMapper.selectList(queryWrapper);
        //执行分页查询
        dishMapper.selectPage(p,queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(p,dishDtoPage,"records");
        List<Dish> records = p.getRecords();
        List<DishDto> list = new ArrayList<>();
        for (Dish dish:records){
            DishDto dishDto=new DishDto();
            BeanUtils.copyProperties(dish,dishDto);
            Long id = dish.getCategoryId();
            Category category = categoryMapper.selectById(id);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            list.add(dishDto);
        }
        efz.setTotal(p.getTotal());
        efz.setList(list);
        return efz;
    }
    /**
     * 删除
     */
    public Boolean delete(Long[] ids){
        QueryWrapper<Dish> wrapper=new QueryWrapper();
        wrapper.lambda().in(Dish::getId,ids);
        wrapper.lambda().eq(Dish::getStatus,1);
        int i = dishMapper.delete(wrapper);
        return i>0;
    }
    /**
     * 修改
     */
    public Boolean update(Dish dish){
        int update = dishMapper.updateById(dish);
        return update>0;
    }
    /**
     * 添加
     */
    //开启事务有一张表出错都不运行
    @Transactional
    public boolean add(DishDto dishDto){
        int insert = dishMapper.insert(dishDto);
        Long id = dishDto.getId();
        List<DishFlavor> flavorList = dishDto.getFlavors();
        for(DishFlavor dishFlavor:flavorList){
            dishFlavor.setDishId(id);
        }
        System.out.println(flavorList+"----------------------------");
        boolean b = dishService.saveBatch(flavorList);
        return insert>0&&b;
    }
    /**
     * 查询单个
     */
    public DishDto getById(Long id){
        DishDto dishDto=new DishDto();
        //查询菜品基本信息，从dish表查询
        Dish dish = dishMapper.selectById(id);

        //查询菜品类型
        //Category category = categoryMapper.selectById(dish.getCategoryId());

        //条件封装
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId,id);

        //通过口味表的dish_id获取口味信息
        List<DishFlavor> list = dishService.list(queryWrapper);

        //工具类拷贝
        BeanUtils.copyProperties(dish,dishDto);

        //将查询到的数据封装到dishdto类
        dishDto.setFlavors(list);
        //dishDto.setCategoryName(category.getName());

        return dishDto;
    }
    /**
     * 停售起售
     */
    public boolean status(Long[] id,Integer status){
        UpdateWrapper<Dish> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", id).set("status", status);
        int update = dishMapper.update(null, updateWrapper);
        return update>0;
    }


    public List<DishDto> list(Dish dish){
        LambdaQueryWrapper<Dish> wrapper=new LambdaQueryWrapper();
        wrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId())
                .eq(Dish::getStatus,1);
        wrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishes = dishMapper.selectList(wrapper);
        List<DishDto> list=new ArrayList<>();
        for (Dish dish1:dishes){
            DishDto dishDto=new DishDto();
            BeanUtils.copyProperties(dish1,dishDto);
            Long id = dish1.getCategoryId();
            Category category = categoryMapper.selectById(id);
            if(category!=null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            Long id1 = dish1.getId();
            LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId,id1);
            List<DishFlavor> dishFlavors = dishService.list(queryWrapper);
            dishDto.setFlavors(dishFlavors);
            list.add(dishDto);
        }
        return list;
    }
}
