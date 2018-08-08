package com.seezoon.service.modules.sys.dao;

import java.util.List;

import com.seezoon.boot.common.dao.CrudDao;
import com.seezoon.service.modules.sys.entity.SysDict;

public interface SysDictDao extends CrudDao<SysDict>{
    public List<String> findTypes();
}