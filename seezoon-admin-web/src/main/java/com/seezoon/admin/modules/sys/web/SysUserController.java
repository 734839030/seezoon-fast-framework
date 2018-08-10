package com.seezoon.admin.modules.sys.web;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.seezoon.admin.common.file.handler.FileHandler;
import com.seezoon.admin.common.utils.BtRemoteValidateResult;
import com.seezoon.admin.modules.sys.security.SecurityUtils;
import com.seezoon.admin.modules.sys.service.LoginSecurityService;
import com.seezoon.boot.common.web.BaseController;
import com.seezoon.boot.context.dto.ResponeModel;
import com.seezoon.service.modules.sys.entity.SysRole;
import com.seezoon.service.modules.sys.entity.SysUser;
import com.seezoon.service.modules.sys.service.SysRoleService;
import com.seezoon.service.modules.sys.service.SysUserService;

@RestController
@RequestMapping("${admin.path}/sys/user")
public class SysUserController extends BaseController {

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private LoginSecurityService loginSecurityService;
	@Autowired
	private FileHandler fileHandler;
	
	@PreAuthorize("hasAuthority('sys:user:qry')")
	@PostMapping("/qryPage.do")
	public ResponeModel qryPage(SysUser sysUser,HttpServletRequest request) {
		PageInfo<SysUser> page = sysUserService.findByPage(sysUser, sysUser.getPage(), sysUser.getPageSize());
		for (SysUser user: page.getList()) {
			user.setPhotoFullUrl(fileHandler.getFullUrl(user.getPhoto()));
		}
		return ResponeModel.ok(page);
	}
	@PreAuthorize("hasAuthority('sys:user:qry')")
	@RequestMapping("/get.do")
	public ResponeModel get(@RequestParam Serializable id) {
		SysUser sysUser = sysUserService.findById(id);
		Assert.notNull(sysUser,"用户不存在");
		sysUser.setPhotoFullUrl(fileHandler.getFullUrl(sysUser.getPhoto()));
		//用户所拥有的角色
		List<SysRole> roleList = sysRoleService.findByUserId(sysUser.getId());
		List<String> roleIds = Lists.newArrayList();
		for (SysRole sysRole:roleList) {
			roleIds.add(sysRole.getId());
		}
		sysUser.setRoleIds(roleIds);
		return ResponeModel.ok(sysUser);
	}
	@PreAuthorize("hasAuthority('sys:user:save')")
	@PostMapping("/save.do")
	public ResponeModel save(@Validated SysUser sysUser, BindingResult bindingResult) {
		int cnt = sysUserService.save(sysUser);
		return ResponeModel.ok(cnt);
	}
	@PreAuthorize("hasAuthority('sys:user:update')")
	@PostMapping("/update.do")
	public ResponeModel update(@Validated SysUser sysUser, BindingResult bindingResult) {
		if (SecurityUtils.isSuperAdmin(sysUser.getId()) && SysUser.STATUS_STOP.equals(sysUser.getStatus())) {
			return ResponeModel.error("超级管理员不允许修改为禁用状态");
		}
		//密码为空则不更新
		sysUser.setPassword(StringUtils.trimToNull(sysUser.getPassword()));
		int cnt = sysUserService.updateUserRoleSelective(sysUser);
		return ResponeModel.ok(cnt);
	}
	@PreAuthorize("hasAuthority('sys:user:delete')")
	@PostMapping("/delete.do")
	public ResponeModel delete(@RequestParam String id) {
		if (SecurityUtils.isSuperAdmin(id)) {
			return ResponeModel.error("超级管理员不允许删除");
		}
		if (SecurityUtils.getUserId().equals(id)) {
			return ResponeModel.error("自己不能删除自己");
		}
		int cnt = sysUserService.deleteById(id);
		return ResponeModel.ok(cnt);
	}
	
	@PostMapping("/checkLoginName.do")
	public BtRemoteValidateResult checkLoginName(@RequestParam(required=false) String id,@RequestParam  String loginName){
		if(StringUtils.isEmpty(loginName)) {
			return BtRemoteValidateResult.valid(Boolean.TRUE);
		} else {
			SysUser sysUser = sysUserService.findByLoginName(loginName);
			return BtRemoteValidateResult.valid(sysUser == null || sysUser.getId().equals(id));
		}
	}
	@PreAuthorize("hasAuthority('sys:user:update')")
	@PostMapping("/setStatus.do")
	public ResponeModel setStatus(@RequestParam String id, @RequestParam String status) {
		if (SecurityUtils.isSuperAdmin(id)) {
			return ResponeModel.error("超级管理员不允许修改");
		}
		if (SecurityUtils.getUserId().equals(id)) {
			return ResponeModel.error("自己不能修改自己");
		}
		SysUser sysUser = new SysUser();
		sysUser.setId(id);
		sysUser.setStatus(status);
		this.sysUserService.updateSelective(sysUser);
		return ResponeModel.ok();
	}
	
	/**
	 * 账户锁定24小时 解锁
	 * 用户信息修改panel 双击图像解锁
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasAuthority('sys:user:update')")
	@PostMapping("/unlock.do")
	public ResponeModel setStatus(@RequestParam String id) {
		SysUser findById = sysUserService.findById(id);
		Assert.notNull(findById, "解锁用户不存在");
		loginSecurityService.unLock(findById.getLoginName());
		return ResponeModel.ok();
	}
}
