package com.hyc.controller;


import com.hyc.common.BaseContext;
import com.hyc.common.Code;
import com.hyc.common.Result;
import com.hyc.pojo.Employee;
import com.hyc.service.imp.EmployeeServiceImp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    EmployeeServiceImp eSI;

    /**
     *
     * @param request
     * @param employee
     * @return
     * 登录
     */
    @PostMapping("/login")
    public Result<Employee> IogIn(HttpServletRequest request, @RequestBody Employee employee){
        Employee employee1 = eSI.IogIn(employee);
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if(employee1 != null){
            if (!password.equals(employee1.getPassword())){
                return new Result(Code.DL_ERR,"密码错误");
            }
            //查看员工状态，如果为已禁用状态，则返回员工已禁用结果
            if(employee1.getStatus() == 0){
                return new Result(Code.DL_ERR,"账号已禁用");
            }
            request.getSession().setAttribute("employee",employee1.getId());
            return new Result(Code.DL_OK,employee1,"登录成功");
        }
        return new Result(Code.DL_ERR, "没有该用户");
    }

    /**
     *
     * @param request
     * @return
     * 用户退出
     */
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request){

        request.getSession().removeAttribute("employee");
        return new Result(Code.DL_OK,"用户退出");

    }

    /**
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     * 分页查询加模糊查询
     */
    @GetMapping("/page")
    public Result<Employee> page(int page,int pageSize,String name){
        return new Result(Code.DL_OK,eSI.page(page, pageSize, name),"");
    }

    /**
     *
     * @param request
     * @param employee
     * @return
     * 添加用户
     */
    @PostMapping
    public Result add(HttpServletRequest request,@RequestBody Employee employee){
        //初始密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        /*//获取时间
        employee.setUpdateTime(LocalDateTime.now());
        employee.setCreateTime(LocalDateTime.now());
        //创建者和修改者的id
        HttpSession httpSession=request.getSession();
        Employee employee2 =(Employee) httpSession.getAttribute("employee");
        employee.setCreateUser(employee2.getId());
        employee.setUpdateUser(employee2.getId());*/

        if(eSI.add(employee)>0){
            return new Result(Code.DL_OK,"添加成功");
        }else{
            return new Result(Code.DL_ERR,"添加失败");
        }
    }

    /**
     *
     * @param request
     * @param employee
     * @return
     * 修改
     */
    @PutMapping
    public Result update(HttpServletRequest request,@RequestBody Employee employee){
        System.out.println(employee.getId());
        HttpSession httpSession=request.getSession();
        Employee employee2 =(Employee) httpSession.getAttribute("employee");
        employee.setUpdateUser(employee2.getId());
        employee.setUpdateTime(LocalDateTime.now());
        if(eSI.update(employee)>0){
            return new Result(Code.DL_OK,"修改成功");
        }else {
            return new Result(Code.DL_ERR,"修改失败");
        }
    }
    /**
     *
     * 查询单个
     */
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id) {
        System.out.println(id+"111111111111111111111");
        Employee byId = eSI.getById(id);
        Integer code=byId!=null?Code.DL_OK:Code.DL_ERR;
        String msg=byId!=null?"":"查询失败";
        return new Result(code,byId,msg);
    }
}
