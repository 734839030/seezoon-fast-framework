package com.seezoon.boot.common.service;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seezoon.boot.common.Constants;
import com.seezoon.boot.common.dao.CrudDao;
import com.seezoon.boot.common.entity.BaseEntity;
import com.seezoon.boot.common.utils.IdGen;
import com.seezoon.boot.context.dto.AdminUser;
import com.seezoon.boot.context.utils.AdminThreadContext;

/**
 * 增删改查
 * 
 * @author hdf 2018年3月31日
 * @param <D>
 * @param <T>
 */
@Transactional(rollbackFor = Exception.class)
public class CrudService<D extends CrudDao<T>, T extends BaseEntity<? extends Serializable>> extends BaseService {

	@Autowired
	protected D d;
	
	public int save(T t) {
		t.setCreateDate(new Date());
		t.setUpdateDate(t.getCreateDate());
		if (StringUtils.isEmpty(t.getCreateBy())) {
			t.setCreateBy(this.getOperatorUserId());
		}
		t.setUpdateBy(t.getCreateBy());
		//自增主键的insert sql 不能出现插入id 
		if (null == t.getId() || StringUtils.isEmpty(t.getId().toString())) {
			Class<?> clazz = t.getClass();  
		    Type type = clazz.getGenericSuperclass();  
		    ParameterizedType parameterizedType = (ParameterizedType) type;  
		    if (parameterizedType.getActualTypeArguments()[0].equals(String.class)) {
		    		//t.setId(IdGen.uuid()); 这个要是编译可以过去就不用这么麻烦去获取主键类型
		    		Field findField = ReflectionUtils.findField(clazz, "id");
		    		try {
		    			    findField.setAccessible(true);
						findField.set(t, IdGen.uuid());
				} catch (Exception e) {
					logger.error("set id error:",e);
				} 
		    }
		}
		
		int cnt = d.insert(t);
		if (t.isNeedBak()) {
			this.saveBak(t.getId());
		}
		return cnt;
	}

	public int updateSelective(T t) {
		Assert.notNull(t, "更新对象为空");
		Assert.notNull(t.getId(), "更新对象id为空");
		Assert.hasLength(t.getId().toString(), "更新对象id为空");
		t.setUpdateDate(new Date());
		if (StringUtils.isEmpty(t.getUpdateBy())) {
			t.setUpdateBy(this.getOperatorUserId());
		}
		int cnt = d.updateByPrimaryKeySelective(t);
		if (t.isNeedBak()) {
			this.saveBak(t.getId());
		}
		return cnt;
	}

	public int updateById(T t) {
		Assert.notNull(t, "更新对象为空");
		Assert.notNull(t.getId(), "更新对象id为空");
		Assert.hasLength(t.getId().toString(), "更新对象id为空");
		t.setUpdateDate(new Date());
		if (StringUtils.isEmpty(t.getUpdateBy())) {
			t.setUpdateBy(this.getOperatorUserId());
		}
		int cnt = d.updateByPrimaryKey(t);
		if (t.isNeedBak()) {
			this.saveBak(t.getId());
		}
		return cnt;
	}

	@Transactional(readOnly = true)
	public T findById(Serializable id) {
		Assert.notNull(id, "id为空");
		Assert.hasLength(id.toString(), "id为空");
		return d.selectByPrimaryKey(id);
	}

	public int deleteById(Serializable id) {
		Assert.notNull(id, "id为空");
		Assert.hasLength(id.toString(), "id为空");
		T t = this.findById(id);
		if (null != t) {
			t.setUpdateDate(new Date());
			t.setUpdateBy(this.getOperatorUserId());
			if (t.isNeedBak()) {
				this.d.insertBak(t);
			}
			return d.deleteByPrimaryKey(id,t.getDsf());
		}
		return 0;
	}

	@Transactional(readOnly = true)
	public List<T> findList(T t) {
		return d.findList(t);
	}

	/**
	 * 
	 * @param t
	 * @param pageNum
	 * @param pageSize
	 * @param count
	 *            是否统计总数
	 * @return
	 */
	@Transactional(readOnly = true)
	public PageInfo<T> findByPage(T t, int pageNum, int pageSize, boolean count) {
		PageHelper.startPage(pageNum, pageSize, count);
		List<T> list = this.findList(t);
		PageInfo<T> pageInfo = new PageInfo<T>(list);
		return pageInfo;
	}

	@Transactional(readOnly = true)
	public PageInfo<T> findByPage(T t, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize, Boolean.TRUE);
		List<T> list = this.findList(t);
		PageInfo<T> pageInfo = new PageInfo<T>(list);
		return pageInfo;
	}

	/**
	 * 备份数据
	 * 
	 * @param id
	 * @return
	 */
	public int saveBak(Serializable id) {
		return this.saveBak(this.findById(id));
	}

	/**
	 * 备份数据
	 * 
	 * @param t
	 * @return
	 */
	public int saveBak(T t) {
		if (null == t) {
			return 0;
		}
		return this.d.insertBak(t);
	}

	/**
	 * 获取操作人Id
	 * 
	 * @return
	 */
	public String getOperatorUserId() {
		String userId = Constants.SUPER_ADMIN_ID;
		AdminUser user = AdminThreadContext.getUser();
		if (null != user) {
			userId = user.getUserId();
		}
		return userId;
	}
}
