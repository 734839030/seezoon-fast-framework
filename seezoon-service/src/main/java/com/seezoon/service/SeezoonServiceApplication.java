package com.seezoon.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.seezoon.boot.common.dao.BaseDao;
import com.seezoon.boot.context.utils.MdcUtil;

@SpringBootApplication
@EnableTransactionManagement(proxyTargetClass=true)
@MapperScan(basePackages= {"com.seezoon"},basePackageClasses= {BaseDao.class})
public class SeezoonServiceApplication extends  SpringBootServletInitializer{
	public static void main(String[] args) {
		MdcUtil.push();
		SpringApplication.run(SeezoonServiceApplication.class, args);
	}
}
