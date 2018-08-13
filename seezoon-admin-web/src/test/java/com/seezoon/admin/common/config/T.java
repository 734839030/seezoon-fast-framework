package com.seezoon.admin.common.config;

import org.apache.commons.lang.StringUtils;

public class T {

	public static void main(String[] args) {
		String s1 = "1,2;3:4|5";
		String[] split = StringUtils.split(s1, ",;:");
		for (String v : split) {
			System.out.println(v);
		}
	}
}
