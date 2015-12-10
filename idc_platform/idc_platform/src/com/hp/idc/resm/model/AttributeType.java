package com.hp.idc.resm.model;

import java.io.Serializable;

/**
 * ��Դ��������
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class AttributeType implements Serializable {

	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = 1201332440950917931L;

	/**
	 * �ַ���
	 */
	public static final String TYPE_STRING = "string";
	
	/**
	 * ������
	 */
	public static final String TYPE_BOOLEAN = "boolean";

	/**
	 * ���ͣ�����С��
	 */
	public static final String TYPE_INTEGER = "int";

	/**
	 * ��ֵ�ͣ���С��
	 */
	public static final String TYPE_NUMBER = "number";

	/**
	 * ������
	 */
	public static final String TYPE_DATE = "date";

	/**
	 * ʱ����
	 */
	public static final String TYPE_TIME = "time";

	/**
	 * ����ʱ����
	 */
	public static final String TYPE_DATETIME = "datetime";

	/**
	 * ������
	 */
	public static final String TYPE_REFRENCE = "reference";

	/**
	 * �����
	 */
	public static final String TYPE_TABLE = "table";

	/**
	 * ���ʽ��
	 */
	public static final String TYPE_EXPRESSION = "expression";

	/**
	 * �������е���������
	 */
	public static final AttributeType[] ALL_TYPES = {
			new AttributeType(TYPE_STRING, "�ַ���", StringAttributeDefine.class,
					new String[] { "codeType", "codeId" }),
			new AttributeType(TYPE_BOOLEAN, "������", BooleanAttributeDefine.class,
					new String[] { "yes", "no" }),
			new AttributeType(TYPE_INTEGER, "����", IntegerAttributeDefine.class,
					new String[] {}),
			new AttributeType(TYPE_NUMBER, "��ֵ��", NumberAttributeDefine.class,
					new String[] {}),
			new AttributeType(TYPE_DATE, "������", DateAttributeDefine.class,
					new String[] {}),
			new AttributeType(TYPE_TIME, "ʱ����", TimeAttributeDefine.class,
					new String[] {}),
			new AttributeType(TYPE_DATETIME, "����ʱ����",
					DateTimeAttributeDefine.class, new String[] {}),
			new AttributeType(TYPE_REFRENCE, "������",
					ReferenceAttributeDefine.class,
					new String[] { "refModelId" }),
			new AttributeType(TYPE_TABLE, "�����", TableAttributeDefine.class,
					new String[] { "primaryField", "columns" }),
			new AttributeType(TYPE_EXPRESSION, "���ʽ��",
					ExpressionAttributeDefine.class, new String[] { "class" }) };

	/**
	 * ����ID
	 */
	private String id;

	/**
	 * �����Java��
	 */
	private Class<? extends AttributeDefine> defineClass;

	/**
	 * ��������
	 */
	private String name;

	/**
	 * ���Ͳ���
	 */
	private String[] argumentNames;

	/**
	 * ���캯������ʼ��AttributeTypeʵ��
	 * 
	 * @param id
	 *            ����ID
	 * @param name
	 *            ����
	 * @param defineClass
	 *            �����Java��
	 * @param argumentNames
	 *            ��������
	 */
	public AttributeType(String id, String name,
			Class<? extends AttributeDefine> defineClass, String[] argumentNames) {
		this.id = id;
		this.name = name;
		this.argumentNames = argumentNames;
		this.defineClass = defineClass;
	}

	/**
	 * ��ȡ����ID
	 * 
	 * @return ����ID
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * ��ȡ���͵Ĳ���
	 * 
	 * @return ���͵Ĳ���
	 */
	public String[] getArgumentNames() {
		return this.argumentNames;
	}

	/**
	 * ��ȡ�����Java��
	 * 
	 * @return defineClass �����Java��
	 * @see #defineClass
	 */
	public Class<? extends AttributeDefine> getDefineClass() {
		return this.defineClass;
	}

	/**
	 * ����id
	 * 
	 * @param id
	 *            id
	 * @see #id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * ���ö����java��
	 * 
	 * @param defineClass
	 *            �����java��
	 * @see #defineClass
	 */
	public void setDefineClass(Class<? extends AttributeDefine> defineClass) {
		this.defineClass = defineClass;
	}

	/**
	 * ������������
	 * 
	 * @param name
	 *            ��������
	 * @see #name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * �������Ͳ���
	 * 
	 * @param argumentNames
	 *            ���Ͳ���
	 * @see #argumentNames
	 */
	public void setArgumentNames(String[] argumentNames) {
		this.argumentNames = argumentNames;
	}
}
