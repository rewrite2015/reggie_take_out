package com.hyc.controller;

import com.hyc.common.Code;
import com.hyc.common.Result;
import com.hyc.pojo.Category;
import com.hyc.pojo.Dish;
import com.hyc.pojo.Employee;
import com.hyc.pojo.SetMeal;
import com.hyc.pojo.dto.SetmealDto;
import com.hyc.service.imp.SetMealServiceImp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {
    /**
     * 分页加模糊查询
     */
    @Autowired
    private SetMealServiceImp ssi;
    @GetMapping("/page")
    public Result<SetMeal> page(int page, int pageSize, String name){
        return new Result(Code.DL_OK,ssi.page(page, pageSize, name),"");
    }
    /**
     * 删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result delete(@RequestParam Long[] ids){
        if(ssi.delete(ids)) {
            return new Result(Code.DL_OK, "删除成功");
        }
        else {
            return new Result(Code.DL_ERR, "套餐正在售卖不能删除");

        }
    }
    /**
     * 修改
     */
    @PutMapping
    public Result update(HttpServletRequest request, @RequestBody SetmealDto SetmealDto){
        System.out.println(SetmealDto.getId());
        if(ssi.update(SetmealDto)){
            return new Result(Code.DL_OK,"修改成功");
        }else {
            return new Result(Code.DL_ERR,"修改失败");
        }
    }
    /**
     * 添加
     */
    @PostMapping
    public Result add(HttpServletRequest request, @RequestBody SetmealDto setmealDto){
        if(ssi.add(setmealDto)){
            return new Result(Code.DL_OK,"添加成功");
        }else{
            return new Result(Code.DL_ERR,"添加失败");
        }
    }
    /**
     * 查询单个
     */
    @GetMapping("/{id}")
    public Result<SetMeal> getById(@PathVariable("id") Long id) {
        System.out.println(id+"111111111111111111111");
        SetmealDto byId = ssi.getById(id);
        Integer code=byId!=null?Code.DL_OK:Code.DL_ERR;
        String msg=byId!=null?"":"查询失败";
        return new Result(code,byId,msg);
    }
    /**
     * 停售起售
     */
    @PostMapping("/status/{status}")
    public Result status(Long[] ids, @PathVariable Integer status){
        if(ssi.status(ids,status)) {
            return new Result(Code.DL_OK, "修改成功");
        }else {
            return new Result(Code.DL_ERR, "失败");
        }
    }
    /**
     *查询菜品
     */
    @GetMapping("/dish/{id}")
    public Result<Dish> dish(@PathVariable Long id){
        SetmealDto byId = ssi.getById(id);
        return new Result<>(Code.DL_OK,byId,"");
    }

    @GetMapping("/list")
    public Result<SetMeal> list(SetMeal setMeal){
        return new Result(Code.DL_OK,ssi.list(setMeal),"");
    }
}
