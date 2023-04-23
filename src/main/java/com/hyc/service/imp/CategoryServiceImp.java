package com.hyc.service.imp;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.mapper.CategoryMapper;
import com.hyc.pojo.Category;
import com.hyc.pojo.dto.EmployeeDto;
import com.hyc.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImp implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    /**
     * *分页查询
     */
    public EmployeeDto page(int a, int b){
        EmployeeDto efz=new EmployeeDto();
        //条件封装
        IPage p=new Page(a,b);
        LambdaQueryWrapper<Category> wrapper=new LambdaQueryWrapper<>();
        // 按 sort 字段进行升序
        wrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        categoryMapper.selectPage(p,wrapper);
        efz.setTotal(p.getTotal());
        efz.setList(p.getRecords());
        return efz;
    }

    /**
     * 删除
     */
    public Boolean delete(Long id){
        System.out.println(id+"************************************");
        int i = categoryMapper.deleteById(id);
        return i>0;
    }
    /**
     * 查询单个
     */
    public Category getById(Long id){
        return categoryMapper.selectById(id);
    }
    /**
     * 修改
     */
    public Boolean update(Category category){
        int update = categoryMapper.updateById(category);
        return update>0;
    }
    /**
     * 添加
     */
    public boolean add(Category category){
        int insert = categoryMapper.insert(category);
        return insert>0;
    }
    /**
     * 查询全部
     */
    public List<Category> list(Category type){
        //条件构造器
        QueryWrapper<Category> wrapper=new QueryWrapper();
        //添加条件
        wrapper.lambda().eq(type.getType()!=null,Category::getType,type.getType());
        //添加排序条件   先通过sort字段来排序如果sort相同就通过更新时间来进行排序
        wrapper.lambda().orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        return categoryMapper.selectList(wrapper);
    }
}
