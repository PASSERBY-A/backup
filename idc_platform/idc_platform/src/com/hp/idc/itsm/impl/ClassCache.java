package com.hp.idc.itsm.impl;

import java.util.HashMap;
import java.util.Map;



public interface ClassCache {

	/**
	 * �̳��������["ITSM"="com.hp.idc.itsm.task.impl.ItsmTaskManagerImpl"..]
	 * ����ʵ��TaskManagerInterface�ӿڵ���ע��һ�£��Ϳ����Զ�ʵ�ַ�����ִ��
	 */
	public static Map classInsab = new HashMap();
	
	/**
	 * �̳����map����["ITSM"=TaskManagerInterface..]
	 * ����ʵ��TaskManagerInterface�ӿڵ���ע��һ�£��Ϳ����Զ�ʵ�ַ�����ִ��
	 */
	public static Map classIns = new HashMap();
	
}
