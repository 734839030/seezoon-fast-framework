package com.seezoon.service.modules.sys.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.github.pagehelper.PageInfo;
import com.seezoon.boot.common.Constants;
import com.seezoon.boot.common.service.CrudService;
import com.seezoon.service.modules.sys.dao.SysLoginLogDao;
import com.seezoon.service.modules.sys.entity.SysLoginLog;
import com.seezoon.service.modules.sys.entity.SysUser;

/**
 * 登录日志Service
 * Copyright &copy; 2018 powered by huangdf, All rights reserved.
 * @author hdf 2018-5-31 21:34:17
 */
@Service
public class SysLoginLogService extends CrudService<SysLoginLogDao, SysLoginLog>{

	@Autowired
	private SysUserService sysUserService;
	
	public void loginLogByUserId(String status,String userId,String ip,String userAgent,String browser,String deviceName, String area) {
			Assert.hasLength(userId,"用户id 为空");
			Assert.hasLength(userAgent,"userAgent 为空");
			Assert.hasLength(userAgent,"userAgent 为空");
			Assert.hasLength(userAgent,"userAgent 为空");
			SysLoginLog loginLog = new SysLoginLog();
			loginLog.setStatus(status);
			loginLog.setBrowserName(browser);
			loginLog.setDeviceName(deviceName);
			loginLog.setIp(ip);
			loginLog.setArea(area);
			loginLog.setUserId(userId);
			loginLog.setUserAgent(userAgent);
			loginLog.setLoginTime(new Date());
			loginLog.setCreateBy(userId);
			this.save(loginLog);
	}
	public void loginLogByLoginName(String status,String loginName,String ip,String userAgent,String browser,String deviceName, String area) {
		SysUser user = sysUserService.findByLoginName(loginName);
		if (null != user) {
			this.loginLogByUserId(status, user.getId(), ip, userAgent,browser,deviceName,area);
		}
	}
	public SysLoginLog findLastLoginInfo(String userId) {
		Assert.hasLength(userId,"用户id 为空");
		SysLoginLog loginLog = new SysLoginLog();
		loginLog.setUserId(userId);
		loginLog.setStatus(SysLoginLog.SUCCESS);
		loginLog.setSortField("l.login_time");
		loginLog.setDirection(Constants.DESC);
		PageInfo<SysLoginLog> findByPage = this.findByPage(loginLog, 1, 2);
		List<SysLoginLog> list = findByPage.getList();
		if (!list.isEmpty()) {
			return list.get(list.size()-1);
		}
		return null;
	}
	
}
