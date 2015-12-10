package com.cmsz.cloudplatform.hpvm;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cmsz.cloudplatform.spi.plugin.x86.X86PhysicalResourceService;

/**
 * 
 */
public class X86PhysicalResourceServiceTest extends TestCase {

	private static ApplicationContext context = null;

	public static ApplicationContext getApplicationContext() {
		if (null == context) {
			context = new ClassPathXmlApplicationContext(
					new String[] { "webclient.xml" });
		}
		return context;
	}
	
	public static void main(String[] args){
		context= getApplicationContext();
		//WebClient webClient = ()context.getBean("x86WebServiceClient");
		X86PhysicalResourceService  x86PhysicalResourceService = (X86PhysicalResourceService) context.getBean("x86PhysicalResourceService");
		System.out.println(x86PhysicalResourceService);
		//x86PhysicalResourceService.getRackmount("");
		x86PhysicalResourceService.getEnclosureInfo("192.168.21.14");
	}

	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public X86PhysicalResourceServiceTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(X86PhysicalResourceServiceTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(true);
	}
	public void testGetRackmount(){
		context = getApplicationContext();
		System.out.println(context);
	}
	
}
