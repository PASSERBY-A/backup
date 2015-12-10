package com.hp.idc.resm.ui;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;

import com.hp.idc.resm.cache.CacheableObject;

/**
 * 功能模块实体
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class Module extends CacheableObject implements Serializable {
	/**
	 * 序列化UID
	 */
	private static final long serialVersionUID = 4229331019086584026L;

	/**
	 * 模块ID
	 */
	private int id;

	/**
	 * 模块名称
	 */
	private String name;

	/**
	 * 模块说明
	 */
	private String remark;

	/**
	 * 模块对应的主窗体类名
	 */
	private String className;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public String getPrimaryKey() {
		return "" + getId();
	}

	@Override
	public String getDatabaseTable() {
		return "resm_module";
	}

	@Override
	public String getPrimaryKeyField() {
		return "id";
	}

	@Override
	public void updateResultSet(Connection conn, ResultSet rs) throws Exception {
		throw new Exception("不支持更新方法!");
	}

	@Override
	public void readFromResultSet(ResultSet rs) throws Exception {
		setClassName(rs.getString("classname"));
		setId(rs.getInt("id"));
		setName(rs.getString("name"));
		setRemark(rs.getString("remark"));
	}

}
