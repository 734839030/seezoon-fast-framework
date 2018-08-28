package com.seezoon.admin.modules.sys.security;

import java.io.Serializable;
import java.util.List;

import com.seezoon.service.modules.sys.entity.SysRole;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userId;
	private String deptId;
	private String deptName;
	private String loginName;
	private String name;
	private String status;
	private String dsf;
	
	private List<SysRole> roles;

	public User(String userId, String deptId, String deptName, String loginName, String name,String status) {
		super();
		this.userId = userId;
		this.deptId = deptId;
		this.deptName = deptName;
		this.loginName = loginName;
		this.name = name;
		this.status = status;
	}


	public List<SysRole> getRoles() {
		return roles;
	}


	public void setRoles(List<SysRole> roles) {
		this.roles = roles;
	}


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getDsf() {
		return dsf;
	}


	public void setDsf(String dsf) {
		this.dsf = dsf;
	}

}
