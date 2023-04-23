package com.hyc.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@ControllerAdvice不是Result风格的异常处理
//表示代表Result风格的异常处理
//需要springmvc配置加载到包
@RestControllerAdvice
public class ProjectExceptionAdvice {
    //系统异常
    @ExceptionHandler(SystemException.class)
    public Result doSystemException(SystemException ex){
        //记录日志
        //发送消息给运维
        //发送邮件给开发人员
        return new Result(ex.getCode(), null, ex.getMessage());
    }
    //系统异常
    @ExceptionHandler(BusinessException.class)
    public Result doBusinessException(BusinessException ex){
        return new Result(ex.getCode(), null, ex.getMessage());
    }
    //拦截所有异常
    @ExceptionHandler(Exception.class)
    public Result doException(Exception e){
        //在后台打印异常
        e.printStackTrace();
        //出现异常后返回给前端的数据
        return new Result(Code.DL_ERR,null,"出现异常");
    }
}
