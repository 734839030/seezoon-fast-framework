package com.seezoon.admin.common.utils;

public class FileUtils {

	public static String getFileId(String relativePath) {
		return relativePath.substring(relativePath.lastIndexOf("/")+1, relativePath.lastIndexOf("."));
	}
}
