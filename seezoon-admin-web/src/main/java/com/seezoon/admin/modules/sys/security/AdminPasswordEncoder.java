package com.seezoon.admin.modules.sys.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import com.seezoon.boot.common.utils.CodecUtils;

public class AdminPasswordEncoder implements PasswordEncoder{

	@Override
	public String encode(CharSequence rawPassword) {
		
		Assert.notNull(rawPassword, "密码为空");
		return CodecUtils.sha256(rawPassword.toString());
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encode(rawPassword).equals(encodedPassword);
	}

}
