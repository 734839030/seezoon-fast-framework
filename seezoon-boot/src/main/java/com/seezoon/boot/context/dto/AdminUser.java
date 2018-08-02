package com.seezoon.boot.context.dto;

import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 后台用户线程上下文对象
 * @author hdf
 *
 */
public class AdminUser extends SpringBootServletInitializer{

	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 数据权限
	 */
	private String dsf;

	
	public AdminUser(String userId,String dsf) {
		super();
		this.userId = userId;
		this.dsf = dsf;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDsf() {
		return dsf;
	}

	public void setDsf(String dsf) {
		this.dsf = dsf;
	}
	
}
