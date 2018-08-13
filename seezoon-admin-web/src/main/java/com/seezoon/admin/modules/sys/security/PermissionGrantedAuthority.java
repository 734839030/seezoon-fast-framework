package com.seezoon.admin.modules.sys.security;

import org.springframework.security.core.GrantedAuthority;

public class PermissionGrantedAuthority implements GrantedAuthority {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String permission;
	
	public PermissionGrantedAuthority(String permission) {
		this.permission = permission;
	}

	@Override
	public String getAuthority() {
		return permission;
	}

}
