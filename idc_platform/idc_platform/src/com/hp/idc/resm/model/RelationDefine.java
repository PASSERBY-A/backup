package com.hp.idc.resm.model;

import java.sql.Connection;
import java.sql.ResultSet;

import com.hp.idc.resm.cache.CacheException;
import com.hp.idc.resm.cache.CacheableObject;

/**
 * 关联关系类型定义
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class RelationDefine extends CacheableObject {

	/**
	 * 序列化UID
	 */
	private static final long serialVersionUID = -7388054676690812533L;

	/**
	 * 关联关系大类
	 */
	public static final String[] TYPES = new String[] { "关联", "包含" };

	/**
	 * 关系ID
	 */
	private String id;

	/**
	 * 关系名称，展示在拓扑图上
	 */
	private String name;

	/**
	 * 说明，在设置关系时使用
	 */
	private String remark;

	/**
	 * 关联关系大类，"关联"/"包含"
	 */
	private String type;
	
	/**
	 * 颜色, 显示在拓扑图上
	 */
	private String color;
	
	/**
	 * 图标, 显示在拓扑图上
	 */
	private String icon;

	/**
	 * 获取关联关系id
	 * 
	 * @return 关联关系id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * 设置关联关系id
	 * 
	 * @param id
	 *            关联关系id
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setId(String id) throws CacheException {
		checkSet();
		this.id = id.trim();
	}

	/**
	 * 获取关联关系名称
	 * 
	 * @return 关联关系名称
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 设置关联关系名称
	 * 
	 * @param name
	 *            关联关系名称
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setName(String name) throws CacheException {
		checkSet();
		this.name = name;
	}

	/**
	 * 获取关联关系描述
	 * 
	 * @return 关联关系描述
	 */
	public String getRemark() {
		return this.remark;
	}

	/**
	 * 设置关联关系描述
	 * 
	 * @param remark
	 *            关联关系描述
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setRemark(String remark) throws CacheException {
		checkSet();
		this.remark = remark;
	}

	/**
	 * 获取关联关系类型
	 * 
	 * @return 关联关系类型
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * 设置关联关系类型
	 * 
	 * @param type
	 *            关联关系类型
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setType(String type) throws CacheException {
		checkSet();
		this.type = type;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) throws CacheException {
		checkSet();
		this.color = color;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) throws CacheException {
		checkSet();
		this.icon = icon;
	}

	@Override
	public String getPrimaryKey() {
		return getId();
	}

	@Override
	public String getDatabaseTable() {
		return "resm_relation_define";
	}

	@Override
	public String getPrimaryKeyField() {
		return "id";
	}

	@Override
	public void updateResultSet(Connection conn, ResultSet rs) throws Exception {
		rs.updateString("type", this.type);
		rs.updateString("id", this.id);
		rs.updateString("name", this.name);
		rs.updateString("remark", this.remark);
		rs.updateString("color", this.color);
		rs.updateString("icon", this.icon);
	}

	@Override
	public void readFromResultSet(ResultSet rs) throws Exception {
		setType(rs.getString("type"));
		setId(rs.getString("id"));
		setName(rs.getString("name"));
		setRemark(rs.getString("remark"));
		setColor(rs.getString("color"));
		setIcon(rs.getString("icon"));
	}

}
