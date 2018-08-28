package com.seezoon.boot.context.filter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * spring boot filter 顺序从小到大
 * @author hdf
 *
 */
@Configuration
public class FilterConfiguration {


	/**
	 * 这个也可以，但是最后利用spring boot 的跨域
	 * @return
	 */
//	@Bean
//	@Order(Ordered.HIGHEST_PRECEDENCE )
//	@ConditionalOnProperty(name="cors.switch",havingValue="true")
//	public FilterRegistrationBean<CustomCorsFilter> corsFilter() {
//		FilterRegistrationBean<CustomCorsFilter> registration = new FilterRegistrationBean<CustomCorsFilter>();
//        registration.setFilter(new CustomCorsFilter());
//        registration.addUrlPatterns("/*");
//        registration.setName("customCorsFilter");
//        return registration;
//	}
	@Bean
	@ConditionalOnProperty(name="cors.switch",havingValue="true")
	public FilterRegistrationBean<CorsFilter> corsFilter() {
	     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	     CorsConfiguration config = new CorsConfiguration();
	     config.addAllowedOrigin("*");
	     config.setAllowCredentials(true);
	     config.addAllowedHeader("*");
	     config.addAllowedMethod("*");
	     source.registerCorsConfiguration("/**", config);
	     FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<>(new CorsFilter(source));
	     registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
	     return registration;
	 }
	@Bean
	public FilterRegistrationBean<TraceFilter> traceFilter() {
		FilterRegistrationBean<TraceFilter> registration = new FilterRegistrationBean<TraceFilter>();
        registration.setFilter(new TraceFilter());
        registration.addUrlPatterns("/*");
        registration.setName("traceFilter");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        return registration;
	}
}
