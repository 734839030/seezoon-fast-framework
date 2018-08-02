package com.seezoon.boot.context.filter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seezoon.boot.context.utils.MdcUtil;

public class TraceFilter implements Filter {

	/**
	 * 日志对象
	 */
	private Logger logger = LoggerFactory.getLogger(TraceFilter.class);

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String tid = httpServletRequest.getHeader(MdcUtil.THREAD_ID);
		MdcUtil.push(tid);
		StopWatch watch = new StopWatch();
		watch.start();
		chain.doFilter(request, response);
		watch.stop();
		String requestURI = httpServletRequest.getRequestURI();
		logger.info("request:{} comleted use {} ms", requestURI, watch.getTime(TimeUnit.MILLISECONDS));
		MdcUtil.clear();
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}
}
