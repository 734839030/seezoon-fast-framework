package com.seezoon.admin.modules.sys.security;

import org.springframework.security.core.context.SecurityContextHolder;

import com.seezoon.boot.common.Constants;

public class SecurityUtils {

	public static String getUserId() {
		return getUser().getUserId();
	}
	public static User getUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		AdminUserDetails adminUserDetails =  (AdminUserDetails) principal;
		return adminUserDetails.getUser();
	}
	public static boolean isSuperAdmin(String userId) {
		return Constants.SUPER_ADMIN_ID.equals(userId);
	}
	public static boolean isSuperAdmin() {
		return isSuperAdmin(getUserId());
	}

}
