package com.cmsz.cloudplatform.service;

import org.junit.Before;
import org.junit.Test;

import com.cmsz.cloudplatform.BaseTestCase;
import com.cmsz.cloudplatform.service.schedule.PhysicalResourceSynSchedule;

public class PhysicalResourceSynManagerTestCase extends BaseTestCase {

	private PhysicalResourceSynManager synManager = null;
	
	private PhysicalResourceSynSchedule synSckedule = null;
	
	@Before
	public void setUp()
			throws Exception {
		synManager = (PhysicalResourceSynManager)getApplicationContext().getBean("physicalResourceSynManager");
		synSckedule = (PhysicalResourceSynSchedule)getApplicationContext().getBean("physicalResourceSynSchedule");
	}

	@Test
	public void testSynResouce() {
//		synManager.synResource();
		synSckedule.syn();
	}

}
