package com.seezoon.service.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.seezoon.boot.common.service.CrudService;
import com.seezoon.service.modules.sys.dao.SysParamDao;
import com.seezoon.service.modules.sys.entity.SysParam;

@Service
public class SysParamService extends CrudService<SysParamDao, SysParam>{

	public SysParam findByParamKey(String paramKey) {
		Assert.hasLength("paramKey","paramKey 不能为空");
		SysParam sysParam = new SysParam();
		sysParam.setParamKey(paramKey);
		sysParam.setOpenDsf(false);
		List<SysParam> list = this.findList(sysParam);
		return list.isEmpty()?null:list.get(0);
	}
}
