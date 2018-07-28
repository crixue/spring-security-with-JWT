package com.xrj.demo.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.alibaba.fastjson.JSON;
import com.xrj.demo.common.model.RestResp;


public class NoAuthenticationEntryPoint implements AuthenticationEntryPoint {

    //当访问的资源没有权限，会调用这里
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        //返回json形式的错误信息
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        RestResp<?> restResp = RestResp.error("403-forbidden");

        response.getWriter().println(JSON.toJSONString(restResp));
        response.getWriter().flush();
    }
}