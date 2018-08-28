package com.seezoon.admin.modules.sys.web;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.seezoon.admin.common.utils.BtRemoteValidateResult;
import com.seezoon.boot.common.web.BaseController;
import com.seezoon.boot.context.dto.ResponeModel;
import com.seezoon.service.modules.sys.entity.SysParam;
import com.seezoon.service.modules.sys.service.SysParamService;

@RestController
@RequestMapping("${admin.path}/sys/param")
public class SysParamController extends BaseController {

	@Autowired
	private SysParamService sysParamService;

	@PreAuthorize("hasAuthority('sys:param:qry')")
	@PostMapping("/qryPage.do")
	public ResponeModel qryPage(SysParam sysParam) {
		PageInfo<SysParam> page = sysParamService.findByPage(sysParam, sysParam.getPage(), sysParam.getPageSize());
		return ResponeModel.ok(page);
	}
	@PreAuthorize("hasAuthority('sys:param:qry')")
	@RequestMapping("/get.do")
	public ResponeModel get(@RequestParam Serializable id) {
		SysParam sysParam = sysParamService.findById(id);
		return ResponeModel.ok(sysParam);
	}
	@PreAuthorize("hasAuthority('sys:param:save')")
	@PostMapping("/save.do")
	public ResponeModel save(@Validated SysParam sysParam, BindingResult bindingResult) {
		int cnt = sysParamService.save(sysParam);
		return ResponeModel.ok(cnt);
	}
	@PreAuthorize("hasAuthority('sys:param:update')")
	@PostMapping("/update.do")
	public ResponeModel update(@Validated SysParam sysParam, BindingResult bindingResult) {
		int cnt = sysParamService.updateSelective(sysParam);
		return ResponeModel.ok(cnt);
	}
	@PreAuthorize("hasAuthority('sys:param:delete')")
	@PostMapping("/delete.do")
	public ResponeModel delete(@RequestParam Serializable id) {
		int cnt = sysParamService.deleteById(id);
		return ResponeModel.ok(cnt);
	}
	
	@PostMapping("/checkParamKey.do")
	public BtRemoteValidateResult checkParamKey(@RequestParam(required=false) String id,@RequestParam  String paramKey){
		SysParam sysParam = sysParamService.findByParamKey(paramKey.trim());
		return BtRemoteValidateResult.valid(sysParam == null || sysParam.getId().equals(id));
	}
	
}
