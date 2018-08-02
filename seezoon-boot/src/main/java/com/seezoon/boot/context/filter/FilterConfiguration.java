package com.seezoon.boot.context.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * spring boot filter 顺序从小到大
 * @author hdf
 *
 */
@Configuration
public class FilterConfiguration {

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public FilterRegistrationBean<TraceFilter> traceFilter() {
		FilterRegistrationBean<TraceFilter> registration = new FilterRegistrationBean<TraceFilter>();
        registration.setFilter(new TraceFilter());
        registration.addUrlPatterns("/*");
        registration.setName("traceFilter");
        return registration;
	}
}
