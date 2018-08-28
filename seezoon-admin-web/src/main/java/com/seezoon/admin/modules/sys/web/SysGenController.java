package com.seezoon.admin.modules.sys.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.seezoon.boot.common.utils.CodecUtils;
import com.seezoon.boot.common.utils.FreeMarkerUtils;
import com.seezoon.boot.common.web.BaseController;
import com.seezoon.boot.context.dto.ResponeModel;
import com.seezoon.service.modules.sys.dto.GenColumnInfo;
import com.seezoon.service.modules.sys.entity.SysGen;
import com.seezoon.service.modules.sys.service.GeneratorService;
import com.seezoon.service.modules.sys.service.SysGenService;

@RestController
@RequestMapping("${admin.path}/sys/gen")
public class SysGenController extends BaseController {

	@Autowired
	private SysGenService sysGenService;
	@Autowired
	private GeneratorService generatorService;

	@PreAuthorize("hasAuthority('sys:gen:qry')")
	@PostMapping("/qryPage.do")
	public ResponeModel qryPage(SysGen sysGen) {
		PageInfo<SysGen> page = sysGenService.findByPage(sysGen, sysGen.getPage(), sysGen.getPageSize());
		return ResponeModel.ok(page);
	}

	@PreAuthorize("hasAuthority('sys:gen:qry')")
	@RequestMapping("/get.do")
	public ResponeModel get(@RequestParam Serializable id) {
		SysGen sysGen = sysGenService.findById(id);
		return ResponeModel.ok(sysGen);
	}

	@PreAuthorize("hasAuthority('sys:gen:save')")
	@PostMapping("/save.do")
	public ResponeModel save(@Validated @RequestBody SysGen sysGen, BindingResult bindingResult) {
		List<GenColumnInfo> columnInfos = sysGen.getColumnInfos();
		sysGen.setColumns(JSON.toJSONString(columnInfos));
		Collections.sort(columnInfos);
		int cnt = sysGenService.save(sysGen);
		return ResponeModel.ok(cnt);
	}

	@PreAuthorize("hasAuthority('sys:gen:update')")
	@PostMapping("/update.do")
	public ResponeModel update(@Validated @RequestBody SysGen sysGen, BindingResult bindingResult) {
		List<GenColumnInfo> columnInfos = sysGen.getColumnInfos();
		sysGen.setColumns(JSON.toJSONString(columnInfos));
		Collections.sort(columnInfos);
		int cnt = sysGenService.updateSelective(sysGen);
		return ResponeModel.ok(cnt);
	}

	@PreAuthorize("hasAuthority('sys:gen:delete')")
	@PostMapping("/delete.do")
	public ResponeModel delete(@RequestParam Serializable id) {
		int cnt = sysGenService.deleteById(id);
		return ResponeModel.ok(cnt);
	}

	@PreAuthorize("hasAuthority('sys:gen:qry')")
	@PostMapping("/qryTables.do")
	public ResponeModel qryTables() {
		return ResponeModel.ok(sysGenService.findTables());
	}
	@PreAuthorize("hasAuthority('sys:gen:qry')")
	@PostMapping("/qryTableInfo.do")
	public ResponeModel qryTableInfo(@RequestParam String tableName) {
		return ResponeModel.ok(sysGenService.getDefaultGenInfoByTableName(tableName));
	}
	@PreAuthorize("hasAuthority('sys:gen:qry')")
	@RequestMapping("/codeGen.do")
	public void codeGen(@RequestParam String id,HttpServletResponse response) throws IOException {
		SysGen sysGen = this.sysGenService.findById(id);
		Assert.notNull(sysGen, "生成方案不存在");
		sysGen = this.generatorService.preCodeGen(sysGen);
		byte[] codeGen = this.zipCode(sysGen);
		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment;filename="+ CodecUtils.urlEncode("代码生成.zip")); 
		response.setContentLength(codeGen.length);
		ServletOutputStream output = response.getOutputStream();
		IOUtils.write(codeGen, output);
		output.close();
	}
	private byte[] zipCode(SysGen sysGen) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(bos);
		for (String ftl : GeneratorService.ftls) {
			String content = FreeMarkerUtils.renderTemplate(ftl, sysGen);
			logger.info("ftl {}:\r\n{}",ftl,content);
			zos.putNextEntry(new ZipEntry(generatorService.getZipEntryName(ftl,sysGen)));
			IOUtils.write(content, zos,Charset.forName("UTF-8"));
			zos.closeEntry();
		}
		//这个地方有点反人类，按道理应该在取到byte[] 后关闭，测试，zos.flush 也无效，顾提前关闭，将流都刷入到数组中。
		zos.close();
		byte[] byteArray = bos.toByteArray();
		return byteArray;
	}
}
