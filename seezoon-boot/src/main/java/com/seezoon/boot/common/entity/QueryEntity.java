package com.seezoon.boot.common.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seezoon.boot.common.utils.SQLFilterUtils;
import com.seezoon.boot.context.dto.AdminUser;
import com.seezoon.boot.context.utils.AdminThreadContext;

public class QueryEntity implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 排序字段，对应db字段名
	 */
	private String sortField;
	/**
	 * 升降序
	 */
	private String direction;

	/**
	 * 页码
	 */
	@JsonIgnore
	private Integer page = 1;
	/**
	 * 每页大小
	 */
	@JsonIgnore
	private Integer pageSize = 20;
	/**
	 * 自定义查询字段
	 */
	private Map<String, Object> ext;
	/**
	 * 当前表的别名，默认sql语句没有别名，当有别名时候，为了避免冲突需要子类指定
	 */
	@JsonIgnore
	private String tableAlias;
	/**
	 * dataScopeFilter
	 */
	@JsonIgnore
	private String dsf;
	/**
	 * 是否启用数据权限 默认开启
	 */
	private boolean openDsf = true;
			

	public String getDsf() {
		// /a 路径的后端请求需要后端需要，前端不需要
		AdminUser user = AdminThreadContext.getUser();
		if (user != null && StringUtils.isNotEmpty(user.getDsf()) && StringUtils.isEmpty(dsf) && this.openDsf()) {
			dsf = user.getDsf();
			//填充别名
			if (StringUtils.isNotEmpty(this.getTableAlias())) {
				dsf = dsf.replace("{TABLE_ALIAS}", this.getTableAlias()+ ".");
			}  else {
				dsf = dsf.replace("{TABLE_ALIAS}", "");
			}
		}
		return dsf;
	}

	
	public void setDsf(String dsf) {
		this.dsf = dsf;
	}
	
	public boolean openDsf() {
		return openDsf;
	}


	public void setOpenDsf(boolean openDsf) {
		this.openDsf = openDsf;
	}


	public String getTableAlias(){
		return tableAlias;
	}
	/**
	 * 添加自定义参数
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Map<String, Object> addProperty(String key, Object value) {
		if (null == value) {
			return ext;
		}
		if (ext == null) {
			ext = new HashMap<>(1);
		}
		ext.put(key, value);
		return ext;
	}

	public String getSortField() {
		return SQLFilterUtils.sqlFilter(sortField);
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getDirection() {
		return SQLFilterUtils.sqlFilter(direction);
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public Map<String, Object> getExt() {
		return ext;
	}

	public void setExt(Map<String, Object> ext) {
		this.ext = ext;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		if (null != pageSize && pageSize > 1000) {
			pageSize = 1000;
		}
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}
