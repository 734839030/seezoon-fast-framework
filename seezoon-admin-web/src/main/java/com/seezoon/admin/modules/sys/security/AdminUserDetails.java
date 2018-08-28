package com.seezoon.admin.modules.sys.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.seezoon.service.modules.sys.entity.SysUser;

public class AdminUserDetails implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private User user;
	private String passowrd;
	private boolean locked; 
	private List<GrantedAuthority> authorities;
	
	public AdminUserDetails(User user, String passowrd, List<GrantedAuthority> authorities,boolean locked) {
		this.user = user;
		this.passowrd = passowrd;
		this.authorities = authorities;
		this.locked = locked;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return passowrd;
	}

	@Override
	public String getUsername() {
		return user.getLoginName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !locked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return !SysUser.STATUS_STOP.equals(user.getStatus());
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


}
