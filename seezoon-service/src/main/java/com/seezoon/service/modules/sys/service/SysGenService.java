package com.seezoon.service.modules.sys.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.seezoon.boot.common.service.CrudService;
import com.seezoon.service.modules.sys.dao.SysGenDao;
import com.seezoon.service.modules.sys.dto.DbTable;
import com.seezoon.service.modules.sys.dto.DbTableColumn;
import com.seezoon.service.modules.sys.dto.GenColumnInfo;
import com.seezoon.service.modules.sys.entity.SysGen;
import com.seezoon.service.modules.sys.utils.GenTypeMapping;

/**
 * 生成方案
 * 
 * @author hdf 2018年4月26日
 */
@Service
public class SysGenService extends CrudService<SysGenDao, SysGen> {

	@Autowired
	private GeneratorService generatorService;
	
	@Override
	public SysGen findById(Serializable id) {
		 SysGen sysGen = super.findById(id);
		 Assert.notNull(sysGen,"生成方案不存在");
		 sysGen.setColumnInfos(JSON.parseArray(sysGen.getColumns(), GenColumnInfo.class));
		 return sysGen;
	}
	public List<DbTable> findTables() {
		return this.d.findTable(null);
	}

	public DbTable findTableByName(String tableName) {
		Assert.hasLength(tableName, "表名为空");
		List<DbTable> list = this.d.findTable(tableName);
		return list.isEmpty() ? null : list.get(0);
	}
	public List<DbTableColumn> findColumnByTableName(String tableName) {
		Assert.hasLength(tableName, "表名为空");
		return this.d.findColumnByTableName(tableName);
	}
	public String findPkType(String tableName) {
		Assert.hasLength(tableName, "表名为空");
		String dbPkType = this.d.findPkType(tableName);
		Assert.hasLength(dbPkType,tableName + " 需要主键，名为id");
		return GenTypeMapping.getDbJavaMapping(dbPkType);
	}
	/**
	 * 根据表名获得默认的生成方案
	 * @param tableName
	 * @return
	 */
	public SysGen getDefaultGenInfoByTableName(String tableName) {
		DbTable table = this.findTableByName(tableName);
		Assert.notNull(table,tableName + " 不存在");
		List<DbTableColumn> columns = this.findColumnByTableName(tableName);
		SysGen sysGen = generatorService.getDefaultGenInfo(table, columns);
		sysGen.setPkType(this.findPkType(tableName));
		return sysGen;
	}
	
}
