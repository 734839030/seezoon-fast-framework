package com.seezoon.admin.common.config;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.seezoon.boot.context.test.SeezoonBootApplicationTests;

public class EnvironmentTests extends SeezoonBootApplicationTests{

	@Autowired
	private Environment environment;
	
	@Test
	public void t1() {
		System.out.println(environment.getProperty("admin.path") + "/**");
	}
}
