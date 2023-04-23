package com.hyc.controller;


import com.hyc.common.Code;
import com.hyc.common.Result;
import com.hyc.mapper.DishMapper;
import com.hyc.pojo.Category;
import com.hyc.pojo.Dish;
import com.hyc.pojo.Employee;
import com.hyc.pojo.dto.DishDto;
import com.hyc.service.imp.DishServiceImp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishServiceImp dsi;
    /**
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     * 分页查询加模糊查询
     */
    @GetMapping("/page")
    public Result<Employee> page(int page, int pageSize, String name){
        return new Result(Code.DL_OK,dsi.page(page, pageSize, name),"");
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result delete(Long[] ids){
        if(dsi.delete(ids)) {
            return new Result(Code.DL_OK, "删除成功");
        }else {
            return new Result(Code.DL_ERR, "失败");
        }

        /*if(dsi.delete(ids)) {
            return new Result(Code.DL_OK,"删除成功");
        }else{
            return new Result(Code.DL_ERR,"失败");
        }*/
    }

    /**
     * 修改
     * @param request
     * @return
     */
    @PutMapping
    public Result update(HttpServletRequest request, @RequestBody Dish dish){
        System.out.println(dish.getId());
        if(dsi.update(dish)){
            return new Result(Code.DL_OK,"修改成功");
        }else {
            return new Result(Code.DL_ERR,"修改失败");
        }
    }
    /**
     * 添加
     */
    @PostMapping
    public Result add(HttpServletRequest request,@RequestBody DishDto dishDto){
        if(dsi.add(dishDto)){
            return new Result(Code.DL_OK,"添加成功");
        }else{
            return new Result(Code.DL_ERR,"添加失败");
        }
    }

    /**
     * 查询单个
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Category> getById(@PathVariable("id") Long id) {
        System.out.println(id+"111111111111111111111");
        DishDto byId = dsi.getById(id);
        Integer code=byId!=null?Code.DL_OK:Code.DL_ERR;
        String msg=byId!=null?"":"查询失败";
        return new Result(code,byId,msg);
    }
    /**
     * 停售起售
     */
    @PostMapping("/status/{status}")
    public Result status(Long[] ids, @PathVariable Integer status){
        if(dsi.status(ids,status)) {
            return new Result(Code.DL_OK, "修改成功");
        }else {
            return new Result(Code.DL_ERR, "失败");
        }

    }
    @GetMapping("/list")
    public Result<DishDto> list(Dish dish){
        List<DishDto> list = dsi.list(dish);
        return new Result(Code.DL_OK,list,"");
    }
}
