package com.seezoon.admin.modules.sys.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.alibaba.fastjson.JSON;
import com.seezoon.admin.modules.sys.service.LoginSecurityService;
import com.seezoon.boot.context.dto.ResponeModel;

public class LoginResultHandler implements AuthenticationSuccessHandler,AuthenticationFailureHandler{

	@Autowired
	private LoginSecurityService loginSecurityService;
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		String loginName = request.getParameter("username");
		ResponeModel result = ResponeModel.error(exception.getMessage());
		if (exception instanceof UsernameNotFoundException) {
			result.setResponeMsg("账户密码错误,连续错误5次将锁定24小时");
			loginSecurityService.incrementLoginFailTimes(loginName);
		} else if (exception instanceof BadCredentialsException) {
			result.setResponeMsg("账户密码错误,连续错误5次将锁定24小时");
		} else if (exception instanceof LockedException) {
			result.setResponeMsg("账户已被禁用");
		} 
		this.print(response, JSON.toJSONString(result));
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		ResponeModel ok = ResponeModel.ok();
		this.print(response, JSON.toJSONString(ok));
	}

	private void print(HttpServletResponse response,String content) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter writer = response.getWriter();
		writer.print(content);
		writer.close();
	}
}
