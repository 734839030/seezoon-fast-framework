package com.seezoon.admin.common.file.handler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.seezoon.admin.common.file.config.AliOssConfig;

/**
 * 阿里云文件处理
 * 
 * @author hdf 2018年4月15日
 */
public class AliFileFileHandler implements FileHandler{

	private OSSClient ossClient;
	private String bucketName;
	private String urlPrefix;
	@Autowired
	private AliOssConfig aliOssConfig;
	
	public AliFileFileHandler() { 
	}
	public void init() {
		ossClient = new OSSClient(aliOssConfig.getEndpoint(), aliOssConfig.getAccessKeyId(), aliOssConfig.getAccessKeySecret());
		this.bucketName = aliOssConfig.getBucketName();
		this.urlPrefix = aliOssConfig.getUrlPrefix();
	}
	/**
	 * 
	 * @param endpoint
	 * @param accessKeyId
	 * @param accessKeySecret
	 */
	public AliFileFileHandler(String endpoint, String accessKeyId, String accessKeySecret,String bucketName,String urlPrefix) {
		ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		this.bucketName = bucketName;
		this.urlPrefix = urlPrefix;
	}

	/**
	 * storePath 在阿里云oss 中即为 BucketName
	 */
	@Override
	public void upload(String relativePath, InputStream in) throws IOException {
		ossClient.putObject(bucketName, handleRelativePath(relativePath), in);
		if (null != in) {
			in.close();
		}
	}

	@Override
	public InputStream download(String relativePath) throws FileNotFoundException {
		OSSObject object = ossClient.getObject(bucketName, handleRelativePath(relativePath));
		return object.getObjectContent();
	}

	/**
	 * 阿里云路径不能以/ 开始
	 * @param relativePath
	 * @return
	 */
	private String handleRelativePath(String relativePath) {
		Assert.hasLength(relativePath,"路径为空");
		if (relativePath.startsWith("/")) {
			relativePath = relativePath.substring(1);
		}
		return relativePath;
	}
	@Override
	public void destroy() {
		if (null != ossClient) {
			ossClient.shutdown();
		}
	}
	@Override
	public String getFullUrl(String relativePath) {
		Assert.hasLength(relativePath,"相对路径为空");
		return urlPrefix + relativePath;
	}

}
