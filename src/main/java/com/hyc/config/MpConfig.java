package com.hyc.config;


import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class MpConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        //添加拦截器
        MybatisPlusInterceptor m=new MybatisPlusInterceptor();
        //在添加具体的拦截器
        m.addInnerInterceptor(new PaginationInnerInterceptor());
        return  m;
    }
}
