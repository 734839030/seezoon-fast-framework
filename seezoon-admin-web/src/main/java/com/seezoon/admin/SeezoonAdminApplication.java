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

/**
 * 加载文件最好写上classpath:/  避免通过class 获取资源相对路径问题
 * @author hdf
 *
 */
@SpringBootApplication(scanBasePackages= {"com.seezoon"})
@MapperScan(basePackages= {"com.seezoon"},markerInterface=BaseDao.class)
@EnableAspectJAutoProxy(proxyTargetClass=true)
@EnableTransactionManagement(proxyTargetClass=true)
@PropertySource(value= {"classpath:/seezoon.properties"})
@ImportResource(locations= {"classpath:/elastic-job.xml"})
public class SeezoonAdminApplication extends  SpringBootServletInitializer{
	public static void main(String[] args) {
		MdcUtil.push();
		SpringApplication.run(SeezoonAdminApplication.class, args);
	}
}
