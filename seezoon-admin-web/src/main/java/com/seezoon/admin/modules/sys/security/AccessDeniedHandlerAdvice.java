package com.seezoon.admin.modules.sys.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.seezoon.admin.modules.sys.utils.HttpStatus;

@ControllerAdvice
public class AccessDeniedHandlerAdvice implements AccessDeniedHandler{

	@ExceptionHandler(AccessDeniedException.class)
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		response.setStatus(HttpStatus.NEED_PERMISSION.getValue());
	}
}

