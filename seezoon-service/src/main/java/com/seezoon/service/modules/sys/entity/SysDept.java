package com.seezoon.service.modules.sys.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.seezoon.boot.common.entity.TreeEntity;

public class SysDept extends TreeEntity<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 部门名称
	 */
	@NotNull
	@Length(min=1,max=50)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

}