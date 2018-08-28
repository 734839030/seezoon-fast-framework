package com.seezoon.admin.modules.sys.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.seezoon.boot.common.Constants;

public class SecurityUtils {

	public static String getUserId() {
		return getUser().getUserId();
	}
	public static User getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (null != authentication) {
			Object principal = authentication.getPrincipal();
			AdminUserDetails adminUserDetails =  (AdminUserDetails) principal;
			return adminUserDetails.getUser();
		}
		return null;
	}
	public static boolean isSuperAdmin(String userId) {
		return Constants.SUPER_ADMIN_ID.equals(userId);
	}
	public static boolean isSuperAdmin() {
		return isSuperAdmin(getUserId());
	}

}
