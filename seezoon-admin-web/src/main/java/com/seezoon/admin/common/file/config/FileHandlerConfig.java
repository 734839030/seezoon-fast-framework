package com.seezoon.admin.common.file.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.seezoon.admin.common.file.handler.AliFileFileHandler;
import com.seezoon.admin.common.file.handler.LocalFileHandler;

/**
 * 上传文件handler 配置
 * @author hdf
 *
 */
@Configuration
@EnableConfigurationProperties(value= {AliOssConfig.class,LocalConfig.class})
public class FileHandlerConfig {

	@Bean(initMethod="init")
	@ConditionalOnProperty(name="file.storage",havingValue="local")
	public LocalFileHandler localFileHandler() {
		return new LocalFileHandler();
	}
	
	@Bean(initMethod="init")
	@ConditionalOnProperty(name="file.storage",havingValue="aliyun")
	public AliFileFileHandler aliFileFileHandler() {
		return new AliFileFileHandler();
	}
	
}
