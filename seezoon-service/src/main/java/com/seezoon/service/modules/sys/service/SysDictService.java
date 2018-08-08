package com.seezoon.service.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.seezoon.boot.common.Constants;
import com.seezoon.boot.common.service.CrudService;
import com.seezoon.service.modules.sys.dao.SysDictDao;
import com.seezoon.service.modules.sys.entity.SysDict;

@Service
public class SysDictService extends CrudService<SysDictDao, SysDict> {

	public List<SysDict> findByType(String type) {
		Assert.hasLength(type, "type 不能为空");
		SysDict sysDict = new SysDict();
		sysDict.setType(type);
		sysDict.setSortField("sort");
		sysDict.setDirection(Constants.ASC);
		return this.findList(sysDict);
	}

	public List<String> findTypes() {
		return this.d.findTypes();
	}
}
