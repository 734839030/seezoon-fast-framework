package com.seezoon.admin.modules.sys.service;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.seezoon.boot.common.service.BaseService;


@Service
public class LoginSecurityService extends BaseService {
	
	@Resource(name="redisTemplate")
	private ValueOperations<String, Long> valueOperations;
	private String LOCK_PREFIX = "login_cnt_";

	public void unLock(String loginName) {
		Assert.hasLength(loginName, "loginName 为空");
		valueOperations.getOperations().delete(LOCK_PREFIX + loginName);
	}

	public Long getLoginFailCount(String loginName) {
		try {
			Long cnt = valueOperations.get(LOCK_PREFIX + loginName);
			return null == cnt ? 0:cnt;
		} finally {
			valueOperations.getOperations().expire(LOCK_PREFIX + loginName, 24, TimeUnit.HOURS);
		}
	}

	public boolean isLocked(String loginName) {
		return  getLoginFailCount(loginName) >= 5;
	}
	public Long incrementLoginFailTimes(String loginName) {
		try {
			Long increment = valueOperations.increment(LOCK_PREFIX + loginName, 1);
			return increment;
		} finally {
			valueOperations.getOperations().expire(LOCK_PREFIX + loginName, 24, TimeUnit.HOURS);
		}
	}

	public void clearLoginFailTimes(String loginName) {
		valueOperations.getOperations().delete(LOCK_PREFIX + loginName);
	}
}
