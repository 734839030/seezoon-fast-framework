package com.seezoon.admin.modules.sys.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestMethod;

import com.seezoon.admin.modules.sys.utils.HttpStatus;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(proxyTargetClass=true,prePostEnabled=true,securedEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${admin.path}")
	private String adminPath;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/public/**").permitAll()
		.antMatchers((adminPath + "/**")).authenticated()
		.antMatchers(HttpMethod.OPTIONS).permitAll()//跨域的
		.and().formLogin().loginProcessingUrl(adminPath + "/login.do").permitAll()//.successHandler(new LoginResultHandler())
		.and().logout().logoutUrl(adminPath + "/user/logout.do").logoutSuccessHandler(loginResultHandler())
		//.failureHandler(new LoginResultHandler())
		.and().csrf().disable()
		.headers().xssProtection().disable().frameOptions().disable()
		.and().addFilterBefore(adminUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		http.exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
			@Override
			public void commence(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException authException) throws IOException, ServletException {
				response.setStatus(HttpStatus.NEED_LOGIN.getValue());
			}
		}).accessDeniedHandler(new AccessDeniedHandler() {
			@Override
			public void handle(HttpServletRequest request, HttpServletResponse response,
					AccessDeniedException accessDeniedException) throws IOException, ServletException {
				response.setStatus(HttpStatus.NEED_PERMISSION.getValue());
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
