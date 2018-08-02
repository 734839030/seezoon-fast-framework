package com.seezoon.boot.context.exception;

import java.text.MessageFormat;

import com.seezoon.boot.context.utils.MdcUtil;

/**
 * 逻辑异常通用返回，一般不使用，异常成本高，适合层级太深时候直接抛出
 * 
 * @author hdf 2018年3月31日
 */
public class ResponeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 响应码
	 */
	private String responeCode;
	/**
	 * 响应信息
	 */
	private String responeMsg;

	/**
	 * 线程号
	 */
	private String requestId;
	
	public ResponeException(String responeCode) {
		super();
		this.responeCode = responeCode;
		requestId = MdcUtil.peek();
	}

	public ResponeException(String responeCode, String responeMsg) {
		super(responeMsg);
		this.responeCode = responeCode;
		this.responeMsg = responeMsg;
		requestId = MdcUtil.peek();
	}

	public ResponeException(String responeCode, String responeMsg, Object... params) {
		super();
		this.responeCode = responeCode;
		if (null != params) {
			MessageFormat mf = new MessageFormat(responeMsg);
			this.responeMsg = mf.format(params);
		} else {
			this.responeMsg = responeMsg;
		}
	}

	public String getResponeCode() {
		return responeCode;
	}

	public void setResponeCode(String responeCode) {
		this.responeCode = responeCode;
	}

	public String getResponeMsg() {
		return responeMsg;
	}

	public void setResponeMsg(String responeMsg) {
		this.responeMsg = responeMsg;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

}
