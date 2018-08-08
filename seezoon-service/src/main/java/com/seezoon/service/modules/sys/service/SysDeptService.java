package com.seezoon.service.modules.sys.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.seezoon.boot.common.Constants;
import com.seezoon.boot.common.service.CrudService;
import com.seezoon.service.modules.sys.dao.SysDeptDao;
import com.seezoon.service.modules.sys.entity.SysDept;

@Service
public class SysDeptService extends CrudService<SysDeptDao, SysDept>{

	public List<SysDept> findByParentIds(String parentIds){
		Assert.hasLength(parentIds,"parentIds 不能为空");
		SysDept sysDept = new SysDept();
		sysDept.setSortField("sort");
		sysDept.setDirection(Constants.ASC);
		sysDept.setParentIds(parentIds);
		return this.findList(sysDept);
	}
	@Override
	public int deleteById(Serializable id) {
		//删除下级部门
		SysDept sysDept = this.findById(id);
		Assert.notNull(sysDept,"部门不能为空");
		List<SysDept> list = this.findByParentIds(sysDept.getParentIds() + sysDept.getId());
		for (SysDept sDept : list) {
			super.deleteById(sDept.getId());
			this.d.deleteRoleDeptByDeptId(sDept.getId());
		}
		this.d.deleteRoleDeptByDeptId(id);
		return super.deleteById(id);
	}
}
