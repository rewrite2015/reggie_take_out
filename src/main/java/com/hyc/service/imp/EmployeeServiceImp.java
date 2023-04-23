package com.hyc.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyc.mapper.EmployeeMapper;
import com.hyc.pojo.Dish;
import com.hyc.pojo.Employee;
import com.hyc.pojo.dto.EmployeeDto;
import com.hyc.service.EmployeeService;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EmployeeServiceImp implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     *
     * @param employee
     * @return
     * 登录用户信息
     */
    @Override
    public Employee IogIn(Employee employee) {
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        return employeeMapper.selectOne(queryWrapper);
    }
    /**
     *分页加模糊查询
     */
    public EmployeeDto page(int a, int b, String name){
        EmployeeDto efz=new EmployeeDto();
        Page<Employee> p=new Page(a,b);
        //条件封装
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        //模糊查询
        queryWrapper.like(name!=null, "NAME", name);
        //添加排序条件
        queryWrapper.lambda().orderByDesc(Employee::getUpdateTime);
        //List<Dish> userList = dishMapper.selectList(queryWrapper);
        //执行分页查询
        employeeMapper.selectPage(p,queryWrapper);
        efz.setTotal(p.getTotal());
        efz.setList(p.getRecords());
        return efz;
    }

    /**
     *
     * @param employee
     * @return
     * 添加用户
     */
    public Integer add(Employee employee){
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getName,employee.getName());
        Employee employee1 = employeeMapper.selectOne(queryWrapper);
        if (employee1!=null){
            return 0;
        }else {
            return employeeMapper.insert(employee);
        }
    }

    /**
     * 修改
     * @param employee
     * @return
     */
    public Integer update(Employee employee){
        int update = employeeMapper.updateById(employee);
        return update;
    }
    /**
     * 查询单个
     */
    public Employee getById(Long id){
        return employeeMapper.selectById(id);
    }
}
