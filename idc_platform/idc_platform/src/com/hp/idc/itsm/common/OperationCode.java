package com.hp.idc.itsm.common;

import java.util.ArrayList;
import java.util.List;

/**
 * ��ʾ������
 * @author ÷԰
 *
 */
public class OperationCode {
	/**
	 * δ֪
	 */
	public static int UNKNOW = -1;
	
	/**
	 * ����
	 */
	public static int EQUAL = 0;
	
	/**
	 * ������
	 */
	public static int NOT_EQUAL = 1;
	
	/**
	 * С��
	 */
	public static int LESS_THAN = 2;
	
	/**
	 * ����
	 */
	public static int GREATE_THAN = 3;
	
	/**
	 * С�ڵ���
	 */
	public static int LESS_OR_EQUAL = 4;
	
	/**
	 * ���ڵ���
	 */
	public static int GREATE_OR_EQUAL = 5;
	
	/**
	 * ����
	 */
	public static int INCLUDE = 6;
	
	/**
	 * ������
	 */
	public static int NOT_INCLUDE = 7;

	/**
	 * ƥ��
	 */
	public static int MATCH = 8;

	/**
	 * ��ƥ��
	 */
	public static int NOT_MATCH = 9;
	
	/**
	 * Ϊ��
	 */
	public static int NULL = 10;
	
	/**
	 * ��Ϊ��
	 */
	public static int NOT_NULL = 11;
	
	/**
	 * ����
	 */
	public static int BETWEEN = 12;
	
	/**
	 * ������
	 */
	public static int CURRENT_WEEK=13;
	
	/**
	 * ������
	 */
	public static int CURRENT_MONTH=14;
	
	/**
	 * ������
	 */
	public static int CURRENT_YEAR=15;
	
	/**
	 * ��ʼ��
	 */
	public static int STRING_BEGIN=16;
	
	/**
	 * ������
	 */
	public static int STRING_END=17;

	/**
	 * ����������������������Ӧ����
	 * @param str ����������
	 * @return ������Ӧ����
	 */
	public static int parse(String str) {
		if (str == null || str.length() == 0)
			return UNKNOW;
		else if (str.equals("����"))
			return EQUAL;
		else if (str.equals("������"))
			return NOT_EQUAL;
		else if (str.equals("С��"))
			return LESS_THAN;
		else if (str.equals("����"))
			return GREATE_THAN;
		else if (str.equals("С�ڵ���") || str.equals("������"))
			return LESS_OR_EQUAL;
		else if (str.equals("���ڵ���") || str.equals("��С��"))
			return GREATE_OR_EQUAL;
		else if (str.equals("����"))
			return INCLUDE;
		else if (str.equals("������"))
			return NOT_INCLUDE;
		else if (str.equals("ƥ��"))
			return MATCH;
		else if (str.equals("��ƥ��"))
			return NOT_MATCH;
		else if (str.equals("Ϊ��"))
			return NULL;
		else if (str.equals("��Ϊ��") || str.equals("�ǿ�"))
			return NOT_NULL;
		else if (str.equals("����"))
			return BETWEEN;
		else if (str.equals("������"))
			return CURRENT_WEEK;
		else if (str.equals("������"))
			return CURRENT_MONTH;
		else if (str.equals("������"))
			return CURRENT_YEAR;
		else if (str.equals("��ʼ��"))
			return STRING_BEGIN;
		else if (str.equals("������"))
			return STRING_END;
		return UNKNOW;
	}
	
	/**
	 * ��ȡ�������б�
	 * @return ���ز������б�List<String>
	 */
	public static List getOperations(){
		List ret = new ArrayList();
		ret.add("����");
		ret.add("������");
		ret.add("С��");
		ret.add("����");
		ret.add("������");
		ret.add("��С��");
		ret.add("����");
		ret.add("������");
		ret.add("ƥ��");
		ret.add("��ƥ��");
		ret.add("Ϊ��");
		ret.add("��Ϊ��");
		ret.add("����");
		ret.add("������");
		ret.add("������");
		ret.add("������");
		ret.add("��ʼ��");
		ret.add("������");
		
		return ret;
	}
}
