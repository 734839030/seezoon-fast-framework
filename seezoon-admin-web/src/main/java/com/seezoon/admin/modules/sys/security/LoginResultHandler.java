package com.seezoon.admin.modules.sys.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.seezoon.admin.common.http.HttpRequestUtils;
import com.seezoon.admin.common.utils.IpUtils;
import com.seezoon.admin.modules.sys.service.LoginSecurityService;
import com.seezoon.boot.context.dto.ResponeModel;
import com.seezoon.service.modules.sys.entity.SysLoginLog;
import com.seezoon.service.modules.sys.service.SysLoginLogService;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

public class LoginResultHandler implements AuthenticationSuccessHandler,AuthenticationFailureHandler,LogoutSuccessHandler {

	private static Logger logger = LoggerFactory.getLogger(LoginResultHandler.class);
	@Autowired
	private LoginSecurityService loginSecurityService;
	@Autowired
	private SysLoginLogService sysLoginLogService;
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		String loginName = this.obtainUsername(request);
		ResponeModel result = ResponeModel.error(exception.getMessage());
		if (exception instanceof UsernameNotFoundException) {
			loginSecurityService.incrementLoginFailTimes(loginName);
			result.setResponeMsg("账户密码错误,连续错误5次将锁定24小时");
			loginLog(SysLoginLog.PASSWORD_WRONG, loginName, request);
		} else if (exception instanceof BadCredentialsException) {
			loginSecurityService.incrementLoginFailTimes(loginName);
			result.setResponeMsg("账户密码错误,连续错误5次将锁定24小时");
			loginLog(SysLoginLog.PASSWORD_WRONG, loginName, request);
		} else if (exception instanceof LockedException) {
			result.setResponeMsg("账户已被锁定");
			loginLog(SysLoginLog.LOCK_24, loginName, request);
		} else if (exception instanceof DisabledException) {
			result.setResponeMsg("账户已被禁用");
			loginLog(SysLoginLog.USER_STAUTS_STOP, loginName, request);
		} 
		this.print(response, JSON.toJSONString(result));
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		ResponeModel ok = ResponeModel.ok();
		String loginName = this.obtainUsername(request);
		loginLog(SysLoginLog.SUCCESS, loginName, request);
		loginSecurityService.clearLoginFailTimes(loginName);
		this.print(response, JSON.toJSONString(ok));
	}

	private void print(HttpServletResponse response,String content) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter writer = response.getWriter();
		writer.print(content);
		writer.close();
	}
	private String obtainUsername(HttpServletRequest request) {
		return request.getParameter("username");
	}

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		ResponeModel ok = ResponeModel.ok();
		this.print(response, JSON.toJSONString(ok));
	}

	private void loginLog(String status,String loginName,HttpServletRequest request) {
		String ip = IpUtils.getIpAddr(request);
		String userAgentStr = request.getHeader("User-Agent");
		UserAgent userAgent = UserAgent.parseUserAgentString(userAgentStr);  
		Browser browser = userAgent.getBrowser();
		OperatingSystem os = userAgent.getOperatingSystem();
		String deviceName = os.getName() + " "+ os.getDeviceType();
		String area = "";
		String browserStr = browser.getName() +" "+ browser.getVersion(userAgentStr);
		try {
			if (StringUtils.isNotEmpty(ip)) {
				HashMap<String, String> params = Maps.newHashMap();
				params.put("ip",ip);
				String ipInfo = HttpRequestUtils.doGet("http://ip.taobao.com/service/getIpInfo.php",params);
				if (StringUtils.isNotEmpty(ipInfo)) {
					JSONObject parseObject = JSON.parseObject(ipInfo);
					if (parseObject.containsKey("data")) {
						JSONObject data = parseObject.getJSONObject("data");
						area = data.getString("region") + data.getString("city");
					}
				}
			}
		} catch (Exception e) {
			logger.error("userId login log get location fail ",e);
		}
		sysLoginLogService.loginLogByLoginName(SysLoginLog.SUCCESS, loginName, ip, userAgentStr, browserStr, deviceName, area);
	}
}
