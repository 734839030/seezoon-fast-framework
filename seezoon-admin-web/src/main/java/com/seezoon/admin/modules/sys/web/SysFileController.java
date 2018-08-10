package com.seezoon.admin.modules.sys.web;

import java.io.Serializable;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.seezoon.admin.common.file.handler.FileHandler;
import com.seezoon.boot.common.web.BaseController;
import com.seezoon.boot.context.dto.ResponeModel;
import com.seezoon.service.modules.sys.entity.SysFile;
import com.seezoon.service.modules.sys.service.SysFileService;

@RestController
@RequestMapping("${admin.path}/sys/file")
public class SysFileController extends BaseController {

	@Autowired
	private SysFileService sysFileService;
	@Autowired
	private FileHandler fileHandler;
	
	@PreAuthorize("hasAuthority('sys:file:qry')")
	@PostMapping("/qryPage.do")
	public ResponeModel qryPage(SysFile sysFile,@RequestParam(required=false) Date startDate,@RequestParam(required=false) Date endDate) {
		sysFile.addProperty("startDate", startDate);
		sysFile.addProperty("endDate", endDate);
		PageInfo<SysFile> page = sysFileService.findByPage(sysFile, sysFile.getPage(), sysFile.getPageSize());
		for (SysFile file: page.getList()) {
			file.setFullUrl(fileHandler.getFullUrl(file.getRelativePath()));
		}
		return ResponeModel.ok(page);
	}
	
	@PreAuthorize("hasAuthority('sys:file:delete')")
	@PostMapping("/delete.do")
	public ResponeModel delete(@RequestParam Serializable id) {
		int cnt = sysFileService.deleteById(id);
		return ResponeModel.ok(cnt);
	}
	
	
}
