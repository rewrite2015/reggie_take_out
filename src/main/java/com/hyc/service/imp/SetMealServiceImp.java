package com.hyc.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.mapper.CategoryMapper;
import com.hyc.mapper.DishMapper;
import com.hyc.mapper.SetMealMapper;
import com.hyc.pojo.*;
import com.hyc.pojo.dto.EmployeeDto;
import com.hyc.pojo.dto.SetmealDto;
import com.hyc.service.SetMealDishService;
import com.hyc.service.SetMealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetMealServiceImp implements SetMealService {
    @Autowired
    private SetMealMapper setMealMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SetMealDishService setMealDishService;
    /**
     *分页加模糊查询
     */
    public EmployeeDto page(int a, int b, String name){
        EmployeeDto efz=new EmployeeDto();
        Page<SetMeal> p=new Page(a,b);
        Page<SetmealDto> SetmealDtoPage=new Page<>();
        //条件封装
        QueryWrapper<SetMeal> queryWrapper = new QueryWrapper<>();
        //模糊查询
        queryWrapper.like(name!=null, "NAME", name);
        //添加排序条件
        queryWrapper.lambda().orderByDesc(SetMeal::getUpdateTime);
        //List<Dish> userList = dishMapper.selectList(queryWrapper);
        //执行分页查询
        setMealMapper.selectPage(p,queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(p,SetmealDtoPage,"records");
        List<SetMeal> records = p.getRecords();
        List<SetmealDto> list = new ArrayList<>();
        for (SetMeal setMeal:records){
            SetmealDto setmealDto=new SetmealDto();
            BeanUtils.copyProperties(setMeal,setmealDto);
            Long id = setMeal.getCategoryId();
            Category category = categoryMapper.selectById(id);
            String categoryName = category.getName();
            setmealDto.setCategoryName(categoryName);
            list.add(setmealDto);
        }
        efz.setTotal(p.getTotal());
        efz.setList(list);
        return efz;
    }
    /**
     * 删除
     */
    public Boolean delete(Long[] ids){
        QueryWrapper<SetMeal> wrapper=new QueryWrapper();
        wrapper.lambda().in(SetMeal::getId,ids);
        wrapper.lambda().eq(SetMeal::getStatus,1);
        int i = setMealMapper.delete(wrapper);
        return i>0;
    }
    /**
     * 修改
     */
    public Boolean update(SetmealDto setmealDto){
        int update = setMealMapper.updateById(setmealDto);
        return update>0;
    }
    /**
     * 添加
     */
    //开启事务有一张表出错都不运行
    @Transactional
    public boolean add(SetmealDto setmealDto){
        int insert = setMealMapper.insert(setmealDto);
        Long id = setmealDto.getId();
        Long id1=setmealDto.getId();
        List<SetMealDish> setmealDishes = setmealDto.getSetmealDishes();
        for(SetMealDish setMealDish:setmealDishes){
            setMealDish.setDishId(id);
            setMealDish.setSetmealId(id1);
        }
        System.out.println(setmealDishes+"----------------------------");
        boolean b = setMealDishService.saveBatch(setmealDishes);
        return insert>0&&b;
    }
    /**
     * 查询单个
     */
    public SetmealDto getById(Long id){
        SetmealDto setmealDto=new SetmealDto();
        //通过id获取套餐表信息
        SetMeal setMeal = setMealMapper.selectById(id);
        //通过套餐表的category_id获取菜品和套餐分类表信息
        Category category = categoryMapper.selectById(setMeal.getCategoryId());
        //条件封装
        LambdaQueryWrapper<SetMealDish> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.eq(SetMealDish::getSetmealId,id);
        List<SetMealDish> list = setMealDishService.list(queryWrapper);
        //工具类拷贝
        BeanUtils.copyProperties(setMeal,setmealDto);
        setmealDto.setSetmealDishes(list);
        setmealDto.setCategoryName(category.getName());
        return setmealDto;
    }
    /**
     * 停售起售
     */
    public Boolean status(Long[] id,Integer status){
        UpdateWrapper<SetMeal> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", id).set("status", status);
        int update = setMealMapper.update(null, updateWrapper);
        return update>0;
    }

    public List<SetMeal> list(SetMeal setMeal){
        LambdaQueryWrapper<SetMeal> wrapper=new LambdaQueryWrapper();
        wrapper.eq(setMeal.getCategoryId()!=null,SetMeal::getCategoryId,setMeal.getCategoryId())
                .eq(setMeal.getStatus()!=null,SetMeal::getStatus,setMeal.getStatus());
        List<SetMeal> setMeals = setMealMapper.selectList(wrapper);
        return setMeals;
    }
}
