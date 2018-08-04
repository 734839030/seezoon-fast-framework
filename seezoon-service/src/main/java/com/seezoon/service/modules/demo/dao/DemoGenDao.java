package com.seezoon.service.modules.demo.dao;

import org.apache.ibatis.annotations.Mapper;

import com.seezoon.boot.common.dao.CrudDao;
import com.seezoon.service.modules.demo.entity.DemoGen;

/**
 * 生成案例Dao
 * Copyright &copy; 2018 powered by huangdf, All rights reserved.
 * @author hdf 2018-5-13 20:48:16
 */
@Mapper
public interface DemoGenDao extends CrudDao<DemoGen> {

}
