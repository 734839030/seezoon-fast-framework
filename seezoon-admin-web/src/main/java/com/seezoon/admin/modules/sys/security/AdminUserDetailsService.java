package com.seezoon.admin.modules.sys.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import com.seezoon.admin.modules.sys.service.LoginSecurityService;
import com.seezoon.admin.modules.sys.utils.DataPermissionBuilder;
import com.seezoon.boot.common.Constants;
import com.seezoon.service.modules.sys.entity.SysMenu;
import com.seezoon.service.modules.sys.entity.SysRole;
import com.seezoon.service.modules.sys.entity.SysUser;
import com.seezoon.service.modules.sys.service.SysMenuService;
import com.seezoon.service.modules.sys.service.SysRoleService;
import com.seezoon.service.modules.sys.service.SysUserService;

public class AdminUserDetailsService implements UserDetailsService{

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysMenuService sysMenuService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private LoginSecurityService loginSecurityService;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Assert.hasLength(username, "username为空");
		SysUser sysUser = sysUserService.findByLoginName(username);
		if (null == sysUser) {
			throw new UsernameNotFoundException(username + "账号不存在");
		}
		User user = new User(sysUser.getId(), sysUser.getDeptId(), sysUser.getDeptName(), sysUser.getLoginName(), sysUser.getName(), sysUser.getStatus());
		List<SysRole> roles = sysRoleService.findByUserId(sysUser.getId());
		user.setRoles(roles);
		String dsf =DataPermissionBuilder.build(user);
		user.setDsf(dsf);
		AdminUserDetails adminUserDetails = new AdminUserDetails(user, sysUser.getPassword(), getAuthorities(sysUser.getId()), !SecurityUtils.isSuperAdmin(user.getUserId()) && loginSecurityService.isLocked(username));
		return adminUserDetails;
	}
	
	public List<GrantedAuthority> getAuthorities(String userId){
		List<GrantedAuthority> authorities = new ArrayList<>();
		Assert.hasLength(userId, "userId 为空");
		List<SysMenu> menus = null;
		if (SecurityUtils.isSuperAdmin(userId)) {
			menus = sysMenuService.findShowMenuAll();
		} else {
			menus = sysMenuService.findShowMenuByUserId(userId);
		}
		if (null != menus) {
			menus.forEach((v)->{
				String permissions = v.getPermission();
				if (StringUtils.isNotEmpty(permissions)) {
					List<String> asList = Arrays.asList(StringUtils.split(permissions.trim(), Constants.SEPARATOR));
					for (String p :asList) {
						authorities.add(new PermissionGrantedAuthority(p));
					}
				}
			});
		}
		return authorities;
	}

}
