package com.hp.idc.resm.ui;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;

import com.hp.idc.resm.cache.CacheableObject;

/**
 * 图标
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class Icon extends CacheableObject implements Serializable {

	/**
	 * 序列化UID
	 */
	private static final long serialVersionUID = 2816641670922477790L;

	/**
	 * ID
	 */
	private int id;

	/**
	 * 路径
	 */
	private String path;

	/**
	 * 宽度
	 */
	private int width;

	/**
	 * 高度
	 */
	private int height;

	/**
	 * 类型
	 */
	private int type;

	/**
	 * 获取图标id
	 * 
	 * @return 图标id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * 设置图标id
	 * 
	 * @param id
	 *            图标id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 获取图标路径
	 * 
	 * @return 图标路径
	 */
	public String getPath() {
		return this.path;
	}

	/**
	 * 设置图标路径
	 * 
	 * @param path
	 *            图标路径
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * 获取图标宽度
	 * 
	 * @return 图标宽度
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * 设置图标宽度
	 * 
	 * @param width
	 *            图标宽度
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * 获取图标高度
	 * 
	 * @return 图标高度
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * 设置图标高度
	 * 
	 * @param height
	 *            图标高度
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * 获取图标类型
	 * 
	 * @return 图标类型
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * 设置图标类型
	 * 
	 * @param type
	 *            图标类型
	 */
	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String getPrimaryKey() {
		return "" + this.id;
	}

	@Override
	public String getDatabaseTable() {
		return "resm_icon";
	}

	@Override
	public String getPrimaryKeyField() {
		return "id";
	}

	@Override
	public void updateResultSet(Connection conn, ResultSet rs) throws Exception {
		rs.updateInt("id", this.id);
		rs.updateString("path", this.path);
		rs.updateInt("width", this.width);
		rs.updateInt("height", this.height);
		rs.updateInt("type", this.type);
	}

	@Override
	public void readFromResultSet(ResultSet rs) throws Exception {
		this.id = rs.getInt("id");
		this.path = rs.getString("path");
		this.width = rs.getInt("width");
		this.height = rs.getInt("height");
		this.type = rs.getInt("type");
	}

}
