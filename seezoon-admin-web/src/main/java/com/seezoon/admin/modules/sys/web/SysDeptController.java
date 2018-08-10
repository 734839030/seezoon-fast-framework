package com.seezoon.admin.modules.sys.web;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seezoon.admin.common.utils.TreeHelper;
import com.seezoon.admin.modules.sys.security.SecurityUtils;
import com.seezoon.admin.modules.sys.security.User;
import com.seezoon.boot.common.Constants;
import com.seezoon.boot.common.web.BaseController;
import com.seezoon.boot.context.dto.ResponeModel;
import com.seezoon.service.modules.sys.entity.SysDept;
import com.seezoon.service.modules.sys.service.SysDeptService;

@RestController
@RequestMapping("${admin.path}/sys/dept")
public class SysDeptController extends BaseController {

	@Autowired
	private SysDeptService sysDeptService;
	private TreeHelper<SysDept> treeHelper = new TreeHelper<>();

	/**
	 * 用户管理那里的部门查询,只能看到自己及以下部门
	 * @param sysDept
	 * @return
	 */
	@PostMapping("/qryAllWithScope.do")
	public ResponeModel qryAllWithScope(SysDept sysDept) {
		User user = SecurityUtils.getUser();
		String deptId = user.getDeptId();
		if (SecurityUtils.isSuperAdmin(user.getUserId())) {
			return this.qryAll(sysDept);
		}
		//如果是非超级管理员返回自己部门及以下
		SysDept currentDept = sysDeptService.findById(deptId);
		Assert.notNull(currentDept, "当前所属部门为空");
		List<SysDept> list = sysDeptService.findByParentIds(currentDept.getParentIds() + deptId);
		list.add(currentDept);
		return ResponeModel.ok(list);
	}
	
	
	@PostMapping("/qryAll.do")
	public ResponeModel qryAll(SysDept sysDept) {
		sysDept.setSortField("sort");
		sysDept.setDirection(Constants.ASC);
		List<SysDept> list = sysDeptService.findList(sysDept);
		//数据机构调整
		return ResponeModel.ok(treeHelper.treeGridList(list));
	}
	@PreAuthorize("hasAuthority('sys:dept:qry')")
	@RequestMapping("/get.do")
	public ResponeModel get(@RequestParam Serializable id) {
		SysDept sysDept = sysDeptService.findById(id);
		return ResponeModel.ok(sysDept);
	}
	@PreAuthorize("hasAuthority('sys:dept:save')")
	@PostMapping("/save.do")
	public ResponeModel save(@Validated SysDept sysDept, BindingResult bindingResult) {
		SysDept parent = null;
		if (StringUtils.isNotEmpty(sysDept.getParentId())) {
			parent = sysDeptService.findById(sysDept.getParentId());
		} 
		treeHelper.setParent(sysDept, parent);
		int cnt = sysDeptService.save(sysDept);
		return ResponeModel.ok(cnt);
	}
	@PreAuthorize("hasAuthority('sys:dept:update')")
	@PostMapping("/update.do")
	public ResponeModel update(@Validated SysDept sysDept, BindingResult bindingResult) {
		SysDept parent = null;
		if (StringUtils.isNotEmpty(sysDept.getParentId())) {
			parent = sysDeptService.findById(sysDept.getParentId());
		} 
		treeHelper.setParent(sysDept, parent);
		int cnt = sysDeptService.updateSelective(sysDept);
		return ResponeModel.ok(cnt);
	}

	@PreAuthorize("hasAuthority('sys:dept:delete')")
	@PostMapping("/delete.do")
	public ResponeModel delete(@RequestParam Serializable id) {
		int cnt = sysDeptService.deleteById(id);
		return ResponeModel.ok(cnt);
	}
}
