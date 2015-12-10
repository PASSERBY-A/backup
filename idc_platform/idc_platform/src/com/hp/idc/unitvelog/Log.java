/*
 * @(#)Log.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.unitvelog;

import java.util.Map;

/**
 * ��־�Ļ�����Ϣ
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, May 17, 2011 2011
 * 
 */

public class Log implements Cloneable {

	/**
	 * ��־�Ļ�����Ϣoid
	 */
	private int oid; 
	
	/**
	 * ��־����OID
	 */
	private int typeOid;  
	
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
	 * ��־��ʼʱ��
	 */
	private long beginTime = -1;
	
	/**
	 * ��־����ʱ��
	 */
	private long endTime = -1;
	
	/**
	 * ��־��������
	 */
	private String object;
	
	/**
	 * ��־�����Ķ�������
	 */
	private String objectName;
	
	/**
	 * ��־�Ĵ����ˣ�Ĭ��Ϊ����Ա������IDΪ0��
	 */
	private int operator = 0;
	
	/**
	 * ��־�Ĵ������ͣ���ģ���Լ�����
	 */
	private int operatorType = 0;
	
	/**
	 * ��־�Ĵ����� 1�ɹ� 0ʧ��
	 */
	private int operatorResult = 0;
	
	/**
	 * ��־�ľ�������
	 */
	private String content;
	
	/**
	 * ��־����չ��Ϣ
	 */
	private Map<String, String> extendInfo; 

	public int getOid() {
		return oid;
	}

	/**
	 * �ڲ�ʹ��,�벻Ҫ���ô˷���
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
	 * ��־���� �����־���������ͣ������ģ�����־������TF_UL_SEQ��ά��
	 * 
	 * @param typeOid
	 */
	public void setTypeOid(int typeOid) {
		this.typeOid = typeOid;
	}

	/**
	 * �ڲ�ʹ��,�벻Ҫ���ô˷���
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
	 * �ڲ�ʹ��,�벻Ҫ���ô˷���
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
	 * �ڲ�ʹ��,�벻Ҫ���ô˷���
	 * 
	 * @param moduleType
	 */
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	/**
	 * ��־��ʼʱ�� long���ͣ�����
	 * Ĭ��Ϊ-1����ֵС��0���ᵼ����־�������ʧ��
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
	 * ��־����ʱ�� long���ͣ���������Զ������־��ʼʱ��
	 * ����־ʱ��С��0������־����ʱ��С����־��ʼʱ�䣬�ᵼ����־�������ʧ��
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
	 * ��־�������󣬿�Ϊ��
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
	 * ��־�����������ƣ���Ϊ��
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
	 * ��־�����ˣ�Ĭ��Ϊ0��ʾϵͳ����Ա
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
	 * ��־�������ͣ��ɸ�ģ���Լ����壬Ĭ��Ϊ0
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
	 * ��־�������:1����ȷ/�ɹ�  0����/�쳣,����,Ĭ��Ϊ0
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
	 * ��־��������
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
	 * ��־��չ��Ϣ(Map���ͣ�keyֵΪ���ݿ��Ӧ�ֶ�����valueΪ��Ӧ�ֶ�ֵ����־���͵�������Ϣ�����û����չ��������MapΪ�ա�Map����Ϊ0���򲻲�����չ����Ϣ)
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
