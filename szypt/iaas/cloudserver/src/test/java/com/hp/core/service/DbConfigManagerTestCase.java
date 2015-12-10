package com.hp.core.service;

import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.util.Assert;

import com.hp.config.model.DbConfig;
import com.hp.config.service.DbConfigManager;
import com.hp.core.BaseTestCase;

public class DbConfigManagerTestCase extends BaseTestCase {
	private DbConfigManager manager = (DbConfigManager) getApplicationContext().getBean("dbConfigManager");

	private DbConfig instanceDemo(String msg) {
		DbConfig entity = new DbConfig();
		entity.setCreatedBy("pingze");
		entity.setCreatedOn(new Date());
		entity.setModifiedBy("pingze");
		entity.setModifiedOn(new Date());
		entity.setKey("pingze." + msg);
		entity.setValue("pingze" + msg);
		
		return entity;
	}
	
	@Test
	public final void testGetAll() {
		DbConfig entity = null;
		for (int i=0; i<100; i++) {
			entity = this.instanceDemo("" + i);
			this.manager.save(entity);
		} 
		
		List<DbConfig> el = manager.getAll();
		Assert.isTrue(el.size() >= 0);
		
	}
 
	@Test
	public final void testGet() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testExists() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSave() {
		DbConfig entity = new DbConfig();
		entity.setCreatedBy("pingze");
		entity.setCreatedOn(new Date());
		entity.setModifiedBy("pingze");
		entity.setModifiedOn(new Date());
		entity.setKey("pingze");
		entity.setValue("pingze");
		entity = this.manager.save(entity);
		
		Assert.isTrue(entity.getId() != null);
	}

	@Test
	public final void testRemoveT() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testRemovePK() {
		fail("Not yet implemented"); // TODO
	} 

	@Test
	public final void testSearch() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testReindex() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testReindexAll() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testFindByExampleObject() {
		DbConfig entity = null;
		for (int i=0; i<100; i++) {
			entity = this.instanceDemo("" + i);
			this.manager.save(entity);
		} 
		
		entity = new DbConfig();
		entity.setValue("pingze%");
		List<DbConfig> el = (List<DbConfig>) manager.findByExample(entity);
		System.out.println("------------------------------------------------");
		System.out.println(el);
		System.out.println("------------------------------------------------");
		Assert.isTrue(el.size() >= 100);
	}

	@Test
	public final void testFindByExampleStringObjectIntInt() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testFindByNamedQuery() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testRemoveByExample() {
		DbConfig dbConfig = new DbConfig();
		Integer result = this.manager.removeByExample(dbConfig);
		Assert.isTrue(result >= 0);
		
	}

	@Test
	public final void testCountByNamedQuery() {
		
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testCountByExampleObject() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testCountByExampleStringObject() {
		fail("Not yet implemented"); // TODO
	}
	
	public static void main(String[] args) {
		DbConfigManagerTestCase tc = new DbConfigManagerTestCase();
		System.out.println("-----1-----");
		tc.testGetAll();
	}

}
