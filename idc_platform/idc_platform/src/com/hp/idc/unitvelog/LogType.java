/*
 * @(#)LogType.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.unitvelog;

/**
 * 日志类型
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, May 17, 2011 2011
 * 
 */

public class LogType {

	/**
	 * 日志类型OID
	 */
	private int oid;				
	
	/**
	 * 日志类型名称
	 */
	private String name;			
	
	/**
	 * 所属应用名称
	 */
	private String appType;			
	
	/**
	 * 所属模块名称
	 */
	private String moduleType;			
	
	/**
	 * 日志处理类型
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
