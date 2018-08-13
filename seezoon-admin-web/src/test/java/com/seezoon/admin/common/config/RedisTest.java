package com.seezoon.admin.common.config;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.alibaba.fastjson.JSON;
import com.seezoon.boot.context.test.SeezoonBootApplicationTests;
import com.seezoon.service.modules.sys.entity.SysMenu;
import com.seezoon.service.modules.sys.service.SysMenuService;

public class RedisTest extends SeezoonBootApplicationTests{

	@Resource(name="redisTemplate")
	private ValueOperations<String, String> valueOperations;
	@Resource(name="redisTemplate")
	private ValueOperations<String, Long> longOperations;
	@Autowired
	private RedisTemplate<String, List<SysMenu>> redisTemplate;
	@Autowired
	private RedisTemplate<String, Long> redisTemplateLong;
	@Autowired
	private SysMenuService sysMenuService;
	@Test
	public void test() {
		valueOperations.set("aaaa", "1");
		System.err.println(valueOperations.get("aaaa"));
		redisTemplate.opsForValue().set("m", sysMenuService.findList(new SysMenu()));
		List<SysMenu> m = redisTemplate.opsForValue().get("m");
		System.err.println(JSON.toJSONString(m));
	}
	@Test
	public void t2() {
		Long increment = longOperations.increment("i", 1);
		System.err.println(increment);
	}

	
	@Test
	public void t3() {
		Long increment = longOperations.increment("i", 1);
		System.err.println(increment);
	}
	
	
}
