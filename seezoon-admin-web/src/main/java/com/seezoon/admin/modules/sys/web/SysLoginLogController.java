package com.seezoon.admin.modules.sys.web;

import java.io.Serializable;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.seezoon.boot.common.web.BaseController;
import com.seezoon.boot.context.dto.ResponeModel;
import com.seezoon.service.modules.sys.entity.SysLoginLog;
import com.seezoon.service.modules.sys.service.SysLoginLogService;

/**
 * 登录日志controller
 * Copyright &copy; 2018 powered by huangdf, All rights reserved.
 * @author hdf 2018-5-31 21:34:17
 */
@RestController
@RequestMapping("${admin.path}/sys/loginlog")
public class SysLoginLogController extends BaseController {

	@Autowired
	private SysLoginLogService sysLoginLogService;

	@PreAuthorize("hasAuthority('sys:loginlog:qry')")
	@PostMapping("/qryPage.do")
	public ResponeModel qryPage(SysLoginLog sysLoginLog,@RequestParam(required=false) Date startDate,@RequestParam(required=false) Date endDate) {
		sysLoginLog.addProperty("startDate", startDate);
		sysLoginLog.addProperty("endDate", endDate);
		PageInfo<SysLoginLog> page = sysLoginLogService.findByPage(sysLoginLog, sysLoginLog.getPage(), sysLoginLog.getPageSize());
		return ResponeModel.ok(page);
	}
	@PreAuthorize("hasAuthority('sys:loginlog:qry')")
	@RequestMapping("/get.do")
	public ResponeModel get(@RequestParam Serializable id) {
		SysLoginLog sysLoginLog = sysLoginLogService.findById(id);
		//富文本处理
		return ResponeModel.ok(sysLoginLog);
	}
	@PreAuthorize("hasAuthority('sys:loginlog:save')")
	@PostMapping("/save.do")
	public ResponeModel save(@Validated SysLoginLog sysLoginLog, BindingResult bindingResult) {
		int cnt = sysLoginLogService.save(sysLoginLog);
		return ResponeModel.ok(cnt);
	}
	@PreAuthorize("hasAuthority('sys:loginlog:update')")
	@PostMapping("/update.do")
	public ResponeModel update(@Validated SysLoginLog sysLoginLog, BindingResult bindingResult) {
		int cnt = sysLoginLogService.updateSelective(sysLoginLog);
		return ResponeModel.ok(cnt);
	}
	@PreAuthorize("hasAuthority('sys:loginlog:delete')")
	@PostMapping("/delete.do")
	public ResponeModel delete(@RequestParam Serializable id) {
		int cnt = sysLoginLogService.deleteById(id);
		return ResponeModel.ok(cnt);
	}
}
