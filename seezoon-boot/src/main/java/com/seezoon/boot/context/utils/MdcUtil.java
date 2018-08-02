package com.seezoon.boot.context.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

public class MdcUtil {

	public static String THREAD_ID = "tid";
	public static void push() {
		MDC.put(THREAD_ID, randomThreadId());
	}
	public static void push(String tid) {
		if (StringUtils.isNotEmpty(tid)) {
			MDC.put(THREAD_ID, tid);
		} else {
			push();
		}
		 
	}
	public static String peek() {
		String peek = MDC.get(THREAD_ID);
		return StringUtils.isNotEmpty(peek) ? peek : null;
	}

	public static void clear() {
		MDC.remove(THREAD_ID);
	}

	private static String randomThreadId() {
		return RandomStringUtils.randomAlphanumeric(10);
	}
}
