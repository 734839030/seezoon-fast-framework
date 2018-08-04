package com.seezoon.service.modules.demo.service;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.seezoon.boot.context.test.SeezoonBootApplicationTests;

public class DemoGenServiceTest extends SeezoonBootApplicationTests{

	@Autowired
	private DemoGenService demoGenService;
	@Test
	public void testSave() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateSelective() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateById() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindById() {
		demoGenService.findById("dsds");
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteById() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindList() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindByPageTIntIntBoolean() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindByPageTIntInt() {
		demoGenService.findByPage(null, 1, 1);
		fail("Not yet implemented");
	}

}
