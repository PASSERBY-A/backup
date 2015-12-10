package com.hp.idc.resm.ui;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;

import com.hp.idc.resm.cache.CacheableObject;

/**
 * ����ģ��ʵ��
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class Module extends CacheableObject implements Serializable {
	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = 4229331019086584026L;

	/**
	 * ģ��ID
	 */
	private int id;

	/**
	 * ģ������
	 */
	private String name;

	/**
	 * ģ��˵��
	 */
	private String remark;

	/**
	 * ģ���Ӧ������������
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
		throw new Exception("��֧�ָ��·���!");
	}

	@Override
	public void readFromResultSet(ResultSet rs) throws Exception {
		setClassName(rs.getString("classname"));
		setId(rs.getInt("id"));
		setName(rs.getString("name"));
		setRemark(rs.getString("remark"));
	}

}
