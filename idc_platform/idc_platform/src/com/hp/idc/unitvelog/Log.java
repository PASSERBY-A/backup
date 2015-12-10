/*
 * @(#)Log.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.unitvelog;

import java.util.Map;

/**
 * 日志的基本信息
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, May 17, 2011 2011
 * 
 */

public class Log implements Cloneable {

	/**
	 * 日志的基本信息oid
	 */
	private int oid; 
	
	/**
	 * 日志类型OID
	 */
	private int typeOid;  
	
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
	 * 日志开始时间
	 */
	private long beginTime = -1;
	
	/**
	 * 日志结束时间
	 */
	private long endTime = -1;
	
	/**
	 * 日志关联对象
	 */
	private String object;
	
	/**
	 * 日志关联的对象名称
	 */
	private String objectName;
	
	/**
	 * 日志的处理人（默认为管理员，数字ID为0）
	 */
	private int operator = 0;
	
	/**
	 * 日志的处理类型，各模块自己定义
	 */
	private int operatorType = 0;
	
	/**
	 * 日志的处理结果 1成功 0失败
	 */
	private int operatorResult = 0;
	
	/**
	 * 日志的具体内容
	 */
	private String content;
	
	/**
	 * 日志的扩展信息
	 */
	private Map<String, String> extendInfo; 

	public int getOid() {
		return oid;
	}

	/**
	 * 内部使用,请不要调用此方法
	 * 
	 * @param oid
	 */
	public void setOid(int oid) {
		this.oid = oid;
	}

	public String getName() {
		return name;
	}

	public int getTypeOid() {
		return typeOid;
	}

	/**
	 * 日志类型 添加日志的所属类型，必填，各模块的日志类型在TF_UL_SEQ中维护
	 * 
	 * @param typeOid
	 */
	public void setTypeOid(int typeOid) {
		this.typeOid = typeOid;
	}

	/**
	 * 内部使用,请不要调用此方法
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getAppType() {
		return appType;
	}

	/**
	 * 内部使用,请不要调用此方法
	 * 
	 * @param appType
	 */
	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getModuleType() {
		return moduleType;
	}

	/**
	 * 内部使用,请不要调用此方法
	 * 
	 * @param moduleType
	 */
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	/**
	 * 日志开始时间 long类型，必填
	 * 默认为-1，若值小于0，会导致日志插入操作失败
	 * 
	 * @param beginTime
	 */
	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public long getEndTime() {
		return endTime;
	}

	/**
	 * 日志结束时间 long类型，若不填会自动填充日志开始时间
	 * 若日志时间小于0或者日志结束时间小于日志开始时间，会导致日志插入操作失败
	 * 
	 * @param endTime
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getObject() {
		return object;
	}

	/**
	 * 日志关联对象，可为空
	 * 
	 * @param object
	 */
	public void setObject(String object) {
		this.object = object;
	}

	public String getObjectName() {
		return objectName;
	}

	/**
	 * 日志关联对象名称，可为空
	 * 
	 * @param objectName
	 */
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public int getOperator() {
		return operator;
	}

	/**
	 * 日志处理人，默认为0表示系统管理员
	 * 
	 * @param operator
	 */
	public void setOperator(int operator) {
		this.operator = operator;
	}

	public int getOperatorType() {
		return operatorType;
	}

	/**
	 * 日志操作类型，由各模块自己定义，默认为0
	 * 
	 * @param operatorType
	 */
	public void setOperatorType(int operatorType) {
		this.operatorType = operatorType;
	}

	public int getOperatorResult() {
		return operatorResult;
	}

	/**
	 * 日志操作结果:1：正确/成功  0错误/异常,必填,默认为0
	 * 
	 * 
	 * @param operatorResult
	 */
	public void setOperatorResult(int operatorResult) {
		this.operatorResult = operatorResult;
	}

	public String getContent() {
		return content;
	}

	/**
	 * 日志处理内容
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	public Map<String, String> getExtendInfo() {
		return extendInfo;
	}

	/**
	 * 日志扩展信息(Map类型，key值为数据库对应字段名，value为对应字段值，日志类型的配置信息中如果没有扩展表名或者Map为空、Map长度为0，则不插入扩展表信息)
	 * 
	 * @param extendInfo
	 */
	public void setExtendInfo(Map<String, String> extendInfo) {
		this.extendInfo = extendInfo;
	}

	public long getBeginTime() {
		return beginTime;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
