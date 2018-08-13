package com.seezoon.admin.modules.sys.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestMethod;

import com.seezoon.admin.modules.sys.utils.HttpStatus;
import com.seezoon.service.modules.sys.service.SysUserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${admin.path}")
	private String adminPath;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/public/**").permitAll()
		.antMatchers((adminPath + "/**")).authenticated()
		.and().formLogin().loginProcessingUrl(adminPath + "/login.do").permitAll()//.successHandler(new LoginResultHandler())
		//.failureHandler(new LoginResultHandler())
		.and().csrf().disable()
		.addFilterBefore(adminUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		http.exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
			@Override
			public void commence(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException authException) throws IOException, ServletException {
				String method = request.getMethod();
				//如果是跨域的options 请求则放过，复杂contentType跨域，先发一个options 请求
				if (!RequestMethod.OPTIONS.name().equalsIgnoreCase(method)) {
					response.sendError(HttpStatus.NEED_LOGIN.getValue(), "未登录");
				}
			}
		}).accessDeniedHandler(new AccessDeniedHandler() {
			@Override
			public void handle(HttpServletRequest request, HttpServletResponse response,
					AccessDeniedException accessDeniedException) throws IOException, ServletException {
				response.sendError(HttpStatus.NEED_PERMISSION.getValue(), "无权限");
			}
		});
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(adminUserDetailsService()).passwordEncoder(new AdminPasswordEncoder());
	}
	@Bean
	public AdminUsernamePasswordAuthenticationFilter adminUsernamePasswordAuthenticationFilter() throws Exception {
		AdminUsernamePasswordAuthenticationFilter adminUsernamePasswordAuthenticationFilter = new AdminUsernamePasswordAuthenticationFilter(adminPath + "/login.do");
		adminUsernamePasswordAuthenticationFilter.setAuthenticationManager(this.authenticationManager());
		adminUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(loginResultHandler());
		adminUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(loginResultHandler());
		return adminUsernamePasswordAuthenticationFilter;
	}
	@Bean
	public AdminUserDetailsService adminUserDetailsService() {
		AdminUserDetailsService adminUserDetailsService = new AdminUserDetailsService();
		return adminUserDetailsService;
	}
	@Bean
	public LoginResultHandler loginResultHandler() {
		return new LoginResultHandler();
	}
}
