/*
 * @(#)LogType.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.unitvelog;

/**
 * ��־����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, May 17, 2011 2011
 * 
 */

public class LogType {

	/**
	 * ��־����OID
	 */
	private int oid;				
	
	/**
	 * ��־��������
	 */
	private String name;			
	
	/**
	 * ����Ӧ������
	 */
	private String appType;			
	
	/**
	 * ����ģ������
	 */
	private String moduleType;			
	
	/**
	 * ��־��������
	 */
	private String operatorType;
	
	
	public int getOid() {
		return oid;
	}
	public void setOid(int oid) {
		this.oid = oid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
	public String getModuleType() {
		return moduleType;
	}
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}
	public String getOperatorType() {
		return operatorType;
	}
	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}
}
