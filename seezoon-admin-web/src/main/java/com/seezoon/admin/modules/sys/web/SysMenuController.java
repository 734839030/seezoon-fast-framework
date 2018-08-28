package com.seezoon.admin.modules.sys.web;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seezoon.admin.common.utils.TreeHelper;
import com.seezoon.boot.common.Constants;
import com.seezoon.boot.common.web.BaseController;
import com.seezoon.boot.context.dto.ResponeModel;
import com.seezoon.service.modules.sys.entity.SysMenu;
import com.seezoon.service.modules.sys.service.SysMenuService;

@RestController
@RequestMapping("${admin.path}/sys/menu")
public class SysMenuController extends BaseController {

	@Autowired
	private SysMenuService sysMenuService;
	private TreeHelper<SysMenu> treeHelper = new TreeHelper<>();

	@PostMapping("/qryAll.do")
	public ResponeModel qryAll(SysMenu sysMenu) {
		sysMenu.setIsShow(sysMenu.getIsShow());
		sysMenu.setSortField("sort");
		sysMenu.setDirection(Constants.ASC);
		List<SysMenu> list = sysMenuService.findList(sysMenu);
		return ResponeModel.ok(treeHelper.treeGridList(list));
	}
	@PreAuthorize("hasAuthority('sys:menu:qry')")
	@RequestMapping("/get.do")
	public ResponeModel get(@RequestParam Serializable id) {
		SysMenu sysMenu = sysMenuService.findById(id);
		return ResponeModel.ok(sysMenu);
	}
	@PreAuthorize("hasAuthority('sys:menu:save')")
	@PostMapping("/save.do")
	public ResponeModel save(@Validated SysMenu sysMenu, BindingResult bindingResult) {
		SysMenu parent = null;
		if (StringUtils.isNotEmpty(sysMenu.getParentId())) {
			parent = sysMenuService.findById(sysMenu.getParentId());
		} 
		treeHelper.setParent(sysMenu, parent);
		int cnt = sysMenuService.save(sysMenu);
		return ResponeModel.ok(cnt);
	}
	@PreAuthorize("hasAuthority('sys:menu:update')")
	@PostMapping("/update.do")
	public ResponeModel update(@Validated SysMenu sysMenu, BindingResult bindingResult) {
		SysMenu parent = null;
		if (StringUtils.isNotEmpty(sysMenu.getParentId())) {
			parent = sysMenuService.findById(sysMenu.getParentId());
		} 
		treeHelper.setParent(sysMenu, parent);
		int cnt = sysMenuService.updateSelective(sysMenu);
		return ResponeModel.ok(cnt);
	}
	@PreAuthorize("hasAuthority('sys:menu:delete')")
	@PostMapping("/delete.do")
	public ResponeModel delete(@RequestParam Serializable id) {
		int cnt = sysMenuService.deleteById(id);
		return ResponeModel.ok(cnt);
	}
	@PreAuthorize("hasAuthority('sys:menu:save')")
	@PostMapping("/batchSave.do")
	public ResponeModel batchSave(@RequestBody List<SysMenu> list ) {
		sysMenuService.batchSave(list);
		return ResponeModel.ok();
	}
	@PostMapping("/qryByRoleId.do")
	public ResponeModel qryMenuByRoleId(@RequestParam(required=false) String roleId) {
		return ResponeModel.ok(sysMenuService.findByRoleId(roleId));
	}
}
