package com.seezoon.service.modules.sys.dao;

import java.util.List;

import com.seezoon.boot.common.dao.CrudDao;
import com.seezoon.service.modules.sys.entity.SysUser;
import com.seezoon.service.modules.sys.entity.SysUserRole;

public interface SysUserDao extends CrudDao<SysUser> {

	public int deleteUserRoleByUserId(String userId);
	public int insertUserRole(List<SysUserRole> list);
}