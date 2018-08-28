package com.seezoon.admin.modules.sys.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.seezoon.admin.modules.sys.security.SecurityUtils;
import com.seezoon.admin.modules.sys.security.User;
import com.seezoon.boot.context.dto.AdminUser;
import com.seezoon.boot.context.utils.AdminThreadContext;

/**
 * 后端通用拦截器
 * 
 * 
 * @author hdf 2017年9月24日
 */
public class AdminFilter implements HandlerInterceptor {
	/**
	 * 日志对象
	 */
	private Logger logger = LoggerFactory.getLogger(AdminFilter.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		User user = SecurityUtils.getUser();
		if (null != user) {
			AdminThreadContext.putUser(new AdminUser(user.getUserId(), user.getDsf()));
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		AdminThreadContext.removeUser();
	}
}
