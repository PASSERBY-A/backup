package com.hp.idc.itsm.ci;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hp.idc.itsm.common.ITSMInfo;

/**
 * 表示代码类型
 * 
 */
public class CodeTypeInfo extends ITSMInfo {

	/**
	 * 表示列表代码
	 */
	public static int TYPE_CODE = 0;

	/**
	 * 表示树型代码
	 */
	public static int TYPE_TREE = 1;

	/**
	 * 存储代码类型的类型
	 * 
	 * @see #TYPE_CODE
	 * @see #TYPE_TREE
	 */
	protected int type;

	/**
	 * 代码所属类别[配置管理、需求管理、变更管理。。。。。]
	 */
	protected int catalog;

	/**
	 * 存储代码类型的是否启用
	 */
	protected boolean enabled;

	/**
	 * 存储代码类型的关联代码JAVA类，本类型的代码都会生成此类的实例
	 * 
	 * @deprecated
	 */
	protected Class objectClass;

	/**
	 * 默认构造函数
	 * 
	 */
	public CodeTypeInfo() {
		// Nothing todo here
	}

	/**
	 * 以另一代码类型对象构造本实例，复制此对象的所有属性
	 * 
	 * @deprecated
	 * @param info
	 *            代码类型对象
	 */
	public CodeTypeInfo(CodeTypeInfo info) {
		setOid(info.getOid());
		setName(info.getName());
		setType(info.getType());
		setEnabled(info.isEnabled());
		this.objectClass = info.getObjectClass();
	}

	/**
	 * 获取代码类型的关联代码JAVA类，本类型的代码都会生成此类的实例
	 * 
	 * @deprecated
	 * @return 返回代码类型的关联代码JAVA类
	 */
	public Class getObjectClass() {
		return this.objectClass;
	}

	/**
	 * 设置代码类型的关联代码JAVA类
	 * 
	 * @deprecated
	 * @param className
	 *            java类的类名，如java.util.Map
	 * @throws ClassNotFoundException
	 *             类不存在时引发
	 */
	public void setClass(String className) throws ClassNotFoundException {
		if (className == null || className.length() == 0)
			this.objectClass = Class.forName("com.hp.idc.itsm.ci.CodeInfo");
		else
			this.objectClass = Class.forName(className);
	}

	/**
	 * 对数据库返回的结果集的当前记录进行分析
	 * 
	 * @param rs
	 *            结果集
	 * @throws SQLException
	 *             数据库操作异常时引发
	 * @throws ClassNotFoundException
	 *             类不存在时引发
	 */
	public void parse(ResultSet rs) throws SQLException, ClassNotFoundException {
		setOid(rs.getInt("type_oid"));
		setType(rs.getInt("type_type"));
		setName(rs.getString("type_name"));
		// String className = rs.getString("type_classname");
		// setClass(className);
		setEnabled(rs.getInt("type_enabled") == 1);
		setCatalog(rs.getInt("type_catalog"));
	}

	/**
	 * 获取代码类型的是否启用
	 * 
	 * @return 返回代码类型的是否启用
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * 设置代码类型的是否启用
	 * 
	 * @param enabled
	 *            代码类型的是否启用
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * 获取代码类型的类型
	 * 
	 * @return 代码类型的类型
	 * @see #TYPE_CODE
	 * @see #TYPE_TREE
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * 设置代码类型的类型
	 * 
	 * @param type
	 *            代码类型的类型
	 * @see #TYPE_CODE
	 * @see #TYPE_TREE
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 获取代码类型的类型描述
	 * 
	 * @return 返回代码类型的类型描述，即"列表代码"或"树型代码"
	 */
	public String getTypeDesc() {
		return (this.type == TYPE_CODE ? "列表代码" : "树型代码");
	}

	/**
	 * 根据 id 查询对象的属性 对象有id/name/type/className四个基本属性
	 * @deprecated
	 * @param id
	 *            查询标识
	 * @return 属性值，找不到时返回 null
	 */
	public String getAttribute(String id) {
		if (id == null)
			return null;
		if (id.equals("id"))
			return "" + getOid();
		if (id.equals("name"))
			return getName();
		if (id.equals("type"))
			return getTypeDesc();
//		if (id.equals("className"))
//			return this.getObjectClass().getName();
		return null;
	}

	public int getCatalog() {
		return catalog;
	}

	public void setCatalog(int catalog) {
		this.catalog = catalog;
	}

}
