package com.hp.idc.resm.ui;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;

import com.hp.idc.resm.cache.CacheableObject;

/**
 * ģ��˵�
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class Menu extends CacheableObject implements Serializable {
	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = -4633207233603120952L;

	/**
	 * �˵�ID
	 */
	private int id;

	/**
	 * �˵�����
	 */
	private String name;

	/**
	 * �˵�˵��
	 */
	private String remark;

	/**
	 * ��������
	 */
	private String groupName;

	/**
	 * ����ģ��ID
	 */
	private int moduleId;

	/**
	 * ͼ��
	 */
	private String icon;

	/**
	 * ����ͼ��
	 */
	private String groupIcon;

	/**
	 * ִ�ж�������
	 */
	private String action;

	/**
	 * ������������
	 */
	private String actionParam;

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

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getModuleId() {
		return moduleId;
	}

	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getActionParam() {
		return actionParam;
	}

	public void setActionParam(String actionParam) {
		this.actionParam = actionParam;
	}

	public void setGroupIcon(String groupIcon) {
		this.groupIcon = groupIcon;
	}

	public String getGroupIcon() {
		return groupIcon;
	}

	@Override
	public String getPrimaryKey() {
		return "" + getId();
	}

	@Override
	public String getDatabaseTable() {
		return "resm_menu";
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
		setId(rs.getInt("id"));
		setName(rs.getString("name"));
		setRemark(rs.getString("remark"));
		setAction(rs.getString("action"));
		setActionParam(rs.getString("actionparam"));
		setGroupName(rs.getString("groupname"));
		setModuleId(rs.getInt("moduleid"));
		setIcon(rs.getString("iconid"));
		setGroupIcon(rs.getString("groupiconid"));
	}

}
