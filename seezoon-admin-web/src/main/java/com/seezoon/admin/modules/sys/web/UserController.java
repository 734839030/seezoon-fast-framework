package com.seezoon.admin.modules.sys.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seezoon.admin.common.file.handler.FileHandler;
import com.seezoon.admin.common.utils.BtRemoteValidateResult;
import com.seezoon.admin.modules.sys.security.SecurityUtils;
import com.seezoon.admin.modules.sys.security.User;
import com.seezoon.boot.common.web.BaseController;
import com.seezoon.boot.context.dto.ResponeModel;
import com.seezoon.service.modules.sys.entity.SysLoginLog;
import com.seezoon.service.modules.sys.entity.SysMenu;
import com.seezoon.service.modules.sys.entity.SysUser;
import com.seezoon.service.modules.sys.service.SysLoginLogService;
import com.seezoon.service.modules.sys.service.SysMenuService;
import com.seezoon.service.modules.sys.service.SysUserService;

/**
 * 综合用户信息处理
 * 
 * @author hdf 2018年4月14日
 */
@RestController
@RequestMapping("${admin.path}/user")
public class UserController extends BaseController {

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysMenuService sysMenuService;
	@Autowired
	private SysLoginLogService sysLoginLogService;
	@Autowired
	private FileHandler fileHandler;
	@PostMapping("/getUserMenus.do")
	public ResponeModel getUserMenus() {
		User user = SecurityUtils.getUser();
		String userId = user.getUserId();
		List<SysMenu> menus = null;
		// 系统管理员，拥有最高权限
		if (SecurityUtils.isSuperAdmin()) {
			menus = sysMenuService.findShowMenuAll();
		} else {
			menus = sysMenuService.findShowMenuByUserId(userId);
		}
		return ResponeModel.ok(menus);
	}

	@PostMapping("/getUserInfo.do")
	public ResponeModel getUserInfo() {
		String userId = SecurityUtils.getUserId();
		SysUser sysUser = sysUserService.findById(userId);
		Assert.notNull(sysUser, "用户存在");
		sysUser.setPhotoFullUrl(fileHandler.getFullUrl(sysUser.getPhoto()));
		SysLoginLog lastLoginInfo = sysLoginLogService.findLastLoginInfo(userId);
		if (null != lastLoginInfo) {
			sysUser.setLastLoginIp(lastLoginInfo.getIp());
			sysUser.setLastLoginTime(lastLoginInfo.getLoginTime());
			sysUser.setLastLoginArea(lastLoginInfo.getArea());
		}
		return ResponeModel.ok(sysUser);
	}

	@PostMapping("/updateInfo.do")
	public ResponeModel updateInfo(SysUser user) {
		Assert.hasLength(user.getName(), "姓名为空");
		SysUser sysUser = new SysUser();
		sysUser.setPhoto(user.getPhoto());
		sysUser.setName(user.getName());
		sysUser.setEmail(user.getEmail());
		sysUser.setMobile(user.getMobile());
		sysUser.setId(SecurityUtils.getUserId());
		int cnt = sysUserService.updateSelective(sysUser);
		return ResponeModel.ok(cnt);
	}

	@PostMapping("/checkPassword.do")
	public BtRemoteValidateResult checkPassword(@RequestParam String oldPassword) {
		return BtRemoteValidateResult.valid(sysUserService.validatePwd(oldPassword, SecurityUtils.getUserId()));
	}

	@PostMapping("/updatePwd.do")
	public ResponeModel updatePwd(@RequestParam String password, @RequestParam String oldPassword) {
		String userId = SecurityUtils.getUserId();
		if (!sysUserService.validatePwd(oldPassword, userId)) {
			return ResponeModel.error("原密码错误");
		}
		SysUser sysUser = new SysUser();
		sysUser.setId(userId);
		sysUser.setPassword(password);
		int cnt = sysUserService.updateSelective(sysUser);
		return ResponeModel.ok(cnt);
	}

}
