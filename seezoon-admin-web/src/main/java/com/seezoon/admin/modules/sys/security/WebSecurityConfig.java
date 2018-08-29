package com.seezoon.admin.modules.sys.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

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
		.and().rememberMe().key("seezoon").rememberMeParameter("rememberMe").tokenValiditySeconds(7 * 24 * 60 * 60).useSecureCookie(true).userDetailsService(adminUserDetailsService())
		.and().formLogin().loginProcessingUrl(adminPath + "/login.do").permitAll()//.successHandler(new LoginResultHandler())
		.and().logout().logoutUrl(adminPath + "/user/logout.do").logoutSuccessHandler(loginResultHandler()).deleteCookies("remember-me")
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
		}).accessDeniedHandler(new AccessDeniedHandlerAdvice());//加了全局的@exceptionHandler  这个导致无效了,内部使用adviceController解决
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(adminUserDetailsService()).passwordEncoder(new AdminPasswordEncoder());
	}
	/**
	 * 自定义了UsernamePasswordAuthenticationToken filter 可以添加自定功能
	 * @return
	 * @throws Exception
	 */
	@Bean
	public AdminUsernamePasswordAuthenticationFilter adminUsernamePasswordAuthenticationFilter() throws Exception {
		AdminUsernamePasswordAuthenticationFilter adminUsernamePasswordAuthenticationFilter = new AdminUsernamePasswordAuthenticationFilter(adminPath + "/login.do");
		adminUsernamePasswordAuthenticationFilter.setAuthenticationManager(this.authenticationManager());
		adminUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(loginResultHandler());
		adminUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(loginResultHandler());
		adminUsernamePasswordAuthenticationFilter.setRememberMeServices(tokenBasedRememberMeServices());
		return adminUsernamePasswordAuthenticationFilter;
	}
	/**
	 * 由于自定义了UsernamePasswordAuthenticationFilter 所有要把remember的策略传进去
	 * @return
	 */
    public TokenBasedRememberMeServices tokenBasedRememberMeServices() {
        TokenBasedRememberMeServices tbrms = new TokenBasedRememberMeServices("seezoon", adminUserDetailsService());
        // 设置cookie过期时间为2天
        tbrms.setTokenValiditySeconds(60 * 60 * 24 * 7);
        tbrms.setParameter("rememberMe");
        return tbrms;
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
