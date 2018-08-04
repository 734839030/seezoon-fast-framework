package com.seezoon.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.seezoon.boot.common.dao.BaseDao;
import com.seezoon.boot.context.utils.MdcUtil;

@SpringBootApplication(scanBasePackages= {"com.seezoon"})
@MapperScan(basePackages= {"com.seezoon"},markerInterface=BaseDao.class)
@EnableAspectJAutoProxy(proxyTargetClass=true)
@EnableTransactionManagement(proxyTargetClass=true)
@PropertySource(value= {"seezoon.properties"})
@ImportResource(locations= {"elastic-job.xml"})
public class SeezoonBootApplication extends  SpringBootServletInitializer{
	public static void main(String[] args) {
		MdcUtil.push();
		SpringApplication.run(SeezoonBootApplication.class, args);
	}
}
