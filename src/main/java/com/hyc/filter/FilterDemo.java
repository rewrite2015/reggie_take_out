package com.hyc.filter;

import com.alibaba.druid.support.json.JSONUtils;
import com.hyc.common.BaseContext;
import com.hyc.common.Code;
import com.hyc.common.Result;
import com.hyc.pojo.Employee;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;

@WebFilter("/*")
@Slf4j
public class FilterDemo implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;
        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login",
        };
        //获取当前访问网址的目录和路径
        String uri = req.getRequestURI();
        //将需要访问的网址字符数组进行遍历
        for(String u:urls){
            //当前访问的网址和字符数组中的相同则放行
            boolean match = PATH_MATCHER.match(u, uri);
            if(match){
                filterChain.doFilter(req, response);
                return;
            }
        }
        HttpSession httpSession=req.getSession();
        if(httpSession.getAttribute("employee")!=null){
            Long id =(Long) httpSession.getAttribute("employee");
            BaseContext.setCurrentId(id);
            filterChain.doFilter(req, response);
            return;
        }
        if(httpSession.getAttribute("user")!=null){
            Long id=(Long) httpSession.getAttribute("user");
            BaseContext.setCurrentId(id);
            filterChain.doFilter(req, response);
            return;
        }
        log.info("拦截请求:{}",req.getRequestURI());
        req.setAttribute("dl","你尚未登录");
        req.getRequestDispatcher("/backend/page/login/login.html").forward(req,response);
        response.getWriter().write(JSONUtils.toJSONString(new Result<>(Code.DL_ERR,"NOTLOGIN")));
    }



    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}

