package com.seezoon.admin.modules.sys.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.seezoon.admin.modules.sys.filter.AdminFilter;

@Configuration
public class CustomerWebMvcConfigurerAdapter implements WebMvcConfigurer{
	@Autowired
	private Environment environment;
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//针对后端生效
		registry.addInterceptor(new AdminFilter()).addPathPatterns(environment.getProperty("admin.path") + "/**");
		WebMvcConfigurer.super.addInterceptors(registry);
	}
	
}
