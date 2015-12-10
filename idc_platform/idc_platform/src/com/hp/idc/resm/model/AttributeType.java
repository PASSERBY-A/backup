package com.hp.idc.resm.model;

import java.io.Serializable;

/**
 * 资源属性类型
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class AttributeType implements Serializable {

	/**
	 * 序列化UID
	 */
	private static final long serialVersionUID = 1201332440950917931L;

	/**
	 * 字符型
	 */
	public static final String TYPE_STRING = "string";
	
	/**
	 * 布尔型
	 */
	public static final String TYPE_BOOLEAN = "boolean";

	/**
	 * 整型，不带小数
	 */
	public static final String TYPE_INTEGER = "int";

	/**
	 * 数值型，带小数
	 */
	public static final String TYPE_NUMBER = "number";

	/**
	 * 日期型
	 */
	public static final String TYPE_DATE = "date";

	/**
	 * 时间型
	 */
	public static final String TYPE_TIME = "time";

	/**
	 * 日期时间型
	 */
	public static final String TYPE_DATETIME = "datetime";

	/**
	 * 引用型
	 */
	public static final String TYPE_REFRENCE = "reference";

	/**
	 * 表格型
	 */
	public static final String TYPE_TABLE = "table";

	/**
	 * 表达式型
	 */
	public static final String TYPE_EXPRESSION = "expression";

	/**
	 * 定义所有的属性类型
	 */
	public static final AttributeType[] ALL_TYPES = {
			new AttributeType(TYPE_STRING, "字符型", StringAttributeDefine.class,
					new String[] { "codeType", "codeId" }),
			new AttributeType(TYPE_BOOLEAN, "布尔型", BooleanAttributeDefine.class,
					new String[] { "yes", "no" }),
			new AttributeType(TYPE_INTEGER, "整型", IntegerAttributeDefine.class,
					new String[] {}),
			new AttributeType(TYPE_NUMBER, "数值型", NumberAttributeDefine.class,
					new String[] {}),
			new AttributeType(TYPE_DATE, "日期型", DateAttributeDefine.class,
					new String[] {}),
			new AttributeType(TYPE_TIME, "时间型", TimeAttributeDefine.class,
					new String[] {}),
			new AttributeType(TYPE_DATETIME, "日期时间型",
					DateTimeAttributeDefine.class, new String[] {}),
			new AttributeType(TYPE_REFRENCE, "引用型",
					ReferenceAttributeDefine.class,
					new String[] { "refModelId" }),
			new AttributeType(TYPE_TABLE, "表格型", TableAttributeDefine.class,
					new String[] { "primaryField", "columns" }),
			new AttributeType(TYPE_EXPRESSION, "表达式型",
					ExpressionAttributeDefine.class, new String[] { "class" }) };

	/**
	 * 类型ID
	 */
	private String id;

	/**
	 * 定义的Java类
	 */
	private Class<? extends AttributeDefine> defineClass;

	/**
	 * 类型名称
	 */
	private String name;

	/**
	 * 类型参数
	 */
	private String[] argumentNames;

	/**
	 * 构造函数，初始化AttributeType实例
	 * 
	 * @param id
	 *            类型ID
	 * @param name
	 *            名称
	 * @param defineClass
	 *            定义的Java类
	 * @param argumentNames
	 *            参数名称
	 */
	public AttributeType(String id, String name,
			Class<? extends AttributeDefine> defineClass, String[] argumentNames) {
		this.id = id;
		this.name = name;
		this.argumentNames = argumentNames;
		this.defineClass = defineClass;
	}

	/**
	 * 获取类型ID
	 * 
	 * @return 类型ID
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * 获取类型名称
	 * 
	 * @return 类型名称
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 获取类型的参数
	 * 
	 * @return 类型的参数
	 */
	public String[] getArgumentNames() {
		return this.argumentNames;
	}

	/**
	 * 获取定义的Java类
	 * 
	 * @return defineClass 定义的Java类
	 * @see #defineClass
	 */
	public Class<? extends AttributeDefine> getDefineClass() {
		return this.defineClass;
	}

	/**
	 * 设置id
	 * 
	 * @param id
	 *            id
	 * @see #id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 设置定义的java类
	 * 
	 * @param defineClass
	 *            定义的java类
	 * @see #defineClass
	 */
	public void setDefineClass(Class<? extends AttributeDefine> defineClass) {
		this.defineClass = defineClass;
	}

	/**
	 * 设置类型名称
	 * 
	 * @param name
	 *            类型名称
	 * @see #name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 设置类型参数
	 * 
	 * @param argumentNames
	 *            类型参数
	 * @see #argumentNames
	 */
	public void setArgumentNames(String[] argumentNames) {
		this.argumentNames = argumentNames;
	}
}
