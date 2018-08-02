package com.seezoon.boot.common.web;

import java.beans.PropertyEditorSupport;
import java.util.Date;

import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;

import com.seezoon.boot.common.utils.DateUtils;
import com.seezoon.boot.context.dto.ResponeModel;
import com.seezoon.boot.context.exception.ExceptionCode;
import com.seezoon.boot.context.exception.ResponeException;

/**
 * controller 额外逻辑处理类 常用作异常处理
 * 
 * @author hdf 2017年12月11日
 */
@Component
@ControllerAdvice
public class AdviceController {
	/**
	 * 日志对象
	 */
	private static Logger logger = LoggerFactory.getLogger(AdviceController.class);
	/**
	 * 初始化数据绑定 1. 将所有传递进来的String进行HTML编码，防止XSS攻击 2. 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
			}

			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtils.parseDate(text));
			}
		});
	}

	@ResponseBody
	@ExceptionHandler(ResponeException.class)
	public ResponeModel responeException(ResponeException e) {
		logger.error("respone exception ", e);
		ResponeModel responeModel = ResponeModel.error(e.getResponeCode(), e.getResponeMsg());
		return responeModel;
	}

	@ResponseBody
	@ExceptionHandler(BindException.class)
	public ResponeModel bindException(BindException e) {
		logger.error("bind exception ", e);
		ResponeModel responeModel = ResponeModel.error(ExceptionCode.PARAM_BIND_ERROR, "参数绑定错误:{0}",e.getMessage());
		return responeModel;
	}
	
	/**
	 * 可以细化异常，spring 从小异常抓，抓到就不往后走
	 * 
	 * @param e
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(Exception.class)
	public ResponeModel exception(Exception e) {
		logger.error("global exception ", e);
		ResponeModel responeModel = ResponeModel.error(ExceptionCode.UNKNOWN, e.getMessage());
		return responeModel;
	}
}
