package com.hyc.controller;


import com.hyc.common.Code;
import com.hyc.common.Result;
import com.hyc.pojo.Category;
import com.hyc.pojo.Employee;
import com.hyc.service.imp.CategoryServiceImp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryServiceImp csi;

    /**
     * 分页查询全部
     */
    @GetMapping("/page")
    public Result<Category> page(int page, int pageSize){
        return new Result(Code.DL_OK,csi.page(page, pageSize),"");
    }
    /**
     * 查询单个
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Category> getById(@PathVariable("id") Long id) {
        System.out.println(id+"111111111111111111111");
        Category byId = csi.getById(id);
        Integer code=byId!=null?Code.DL_OK:Code.DL_ERR;
        String msg=byId!=null?"":"查询失败";
        return new Result(code,byId,msg);
    }
    /**
     * 删除
     */
    @DeleteMapping
    public Result delete(Long ids){
        if(csi.delete(ids)) {
            return new Result(Code.DL_OK,"删除成功");
        }else{
            return new Result(Code.DL_ERR,"失败");
        }
    }


    /**
     * 修改
     */
    @PutMapping
    public Result update(HttpServletRequest request, @RequestBody Category category){
        System.out.println(category.getId());
        /*HttpSession httpSession=request.getSession();
        Employee employee2 =(Employee) httpSession.getAttribute("employee");
        //修改者
        category.setUpdateUser(employee2.getId());
        //更新时间
        category.setUpdateTime(LocalDateTime.now());*/
        if(csi.update(category)){
            return new Result(Code.DL_OK,"修改成功");
        }else {
            return new Result(Code.DL_ERR,"修改失败");
        }
    }
    /**
     * 添加
     */
    @PostMapping
    public Result add(HttpServletRequest request,@RequestBody Category category){
        /*//获取时间
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateTime(LocalDateTime.now());
        //创建者和修改者的id
        HttpSession httpSession=request.getSession();
        Employee employee2 =(Employee) httpSession.getAttribute("employee");
        category.setCreateUser(employee2.getId());
        category.setUpdateUser(employee2.getId());*/

        if(csi.add(category)){
            return new Result(Code.DL_OK,"添加成功");
        }else{
            return new Result(Code.DL_ERR,"添加失败");
        }
    }
    /**
     * 菜品表添加分类查询
     */
    @GetMapping("/list")
    public Result list(Category type){
        return new Result<>(Code.DL_OK,csi.list(type));
    }
}
