package com.seezoon.admin.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.seezoon.admin.common.file.config.AliOssConfig;
import com.seezoon.admin.common.file.config.LocalConfig;
import com.seezoon.admin.common.file.handler.AliFileFileHandler;
import com.seezoon.admin.common.file.handler.FileHandler;
import com.seezoon.admin.common.file.handler.LocalFileHandler;

/**
 * 上传文件handler 配置
 * @author hdf
 *
 */
@Configuration
@EnableConfigurationProperties(value= {AliOssConfig.class,LocalConfig.class})
public class FileHandlerConfig {

	@Primary
	@Bean(initMethod="init")
	@ConditionalOnProperty(name="file.storage",havingValue="local")
	public FileHandler localFileHandler() {
		return new LocalFileHandler(); 
	}
	
	@Bean(initMethod="init")
	@ConditionalOnProperty(name="file.storage",havingValue="aliyun")
	public FileHandler aliFileFileHandler() {
		return new AliFileFileHandler();
	}
	
}
