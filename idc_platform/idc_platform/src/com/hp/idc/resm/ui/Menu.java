package com.hp.idc.resm.ui;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;

import com.hp.idc.resm.cache.CacheableObject;

/**
 * 模块菜单
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class Menu extends CacheableObject implements Serializable {
	/**
	 * 序列化UID
	 */
	private static final long serialVersionUID = -4633207233603120952L;

	/**
	 * 菜单ID
	 */
	private int id;

	/**
	 * 菜单名称
	 */
	private String name;

	/**
	 * 菜单说明
	 */
	private String remark;

	/**
	 * 分组名称
	 */
	private String groupName;

	/**
	 * 所属模块ID
	 */
	private int moduleId;

	/**
	 * 图标
	 */
	private String icon;

	/**
	 * 分组图标
	 */
	private String groupIcon;

	/**
	 * 执行动作类名
	 */
	private String action;

	/**
	 * 动作参数类名
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
		throw new Exception("不支持更新方法!");
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
