package com.seezoon.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.seezoon.boot.context.utils.MdcUtil;

@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass=true)
public class SeezoonBootApplication extends  SpringBootServletInitializer{
	public static void main(String[] args) {
		MdcUtil.push();
		SpringApplication.run(SeezoonBootApplication.class, args);
	}
}
