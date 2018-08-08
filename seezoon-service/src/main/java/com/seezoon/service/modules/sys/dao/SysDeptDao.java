package com.seezoon.service.modules.sys.dao;

import java.io.Serializable;

import com.seezoon.boot.common.dao.CrudDao;
import com.seezoon.service.modules.sys.entity.SysDept;

public interface SysDeptDao extends CrudDao<SysDept> {
	public int deleteRoleDeptByDeptId(Serializable deptId);
}