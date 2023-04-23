package com.hyc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hyc.common.Code;
import com.hyc.common.Result;
import com.hyc.pojo.User;
import com.hyc.service.imp.UserServiceImp;
import com.hyc.utils.SMSUtils;
import com.hyc.utils.ValidateCodeUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserServiceImp userServiceImp;
    /**
     * 发送手机验证码
     */
    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)) {
            //生成随机的4位数验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);
            //调用阿里云提供的短信服务API完成发送短信
            //SMSUtils.sendMessage("瑞吉外卖","SMS_276286013",phone,code);
            //将生成的验证码保存到Session
            session.setAttribute(phone,code);
            return new Result<>(Code.DL_OK,code,"手机验证码短信发送成功");
        }
        return new Result<>(Code.DL_ERR,"短信验证码发送失败");
    }

    /**
     * 移动端用户登录
     * @return
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody Map user,HttpSession session){
        log.info(user.toString());
        String phone = user.get("phone").toString();
        String code = user.get("code").toString();
        String attribute = session.getAttribute(phone).toString();
        if(attribute!=null&&attribute.equals(code)){
            LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone,phone);
            //查询是否有该用户
            User one = userServiceImp.getOne(wrapper);
            //没有该用户自动创建为新用户
            if(one==null){
                one=new User();
                one.setPhone(phone);
                one.setStatus(1);
                userServiceImp.save(one);
            }
            session.setAttribute("user",one.getId());
            return new Result<>(Code.DL_OK,one);
        }
        return new Result<>(Code.DL_ERR,"登录失败");
    }
    /**
     *
     * @param request
     * @return
     * 用户退出
     */
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request){

        request.getSession().removeAttribute("user");
        return new Result(Code.DL_OK,"用户退出");

    }
}
