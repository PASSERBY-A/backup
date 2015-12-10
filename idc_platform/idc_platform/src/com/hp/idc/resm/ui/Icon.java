package com.hp.idc.resm.ui;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;

import com.hp.idc.resm.cache.CacheableObject;

/**
 * ͼ��
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class Icon extends CacheableObject implements Serializable {

	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = 2816641670922477790L;

	/**
	 * ID
	 */
	private int id;

	/**
	 * ·��
	 */
	private String path;

	/**
	 * ���
	 */
	private int width;

	/**
	 * �߶�
	 */
	private int height;

	/**
	 * ����
	 */
	private int type;

	/**
	 * ��ȡͼ��id
	 * 
	 * @return ͼ��id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * ����ͼ��id
	 * 
	 * @param id
	 *            ͼ��id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * ��ȡͼ��·��
	 * 
	 * @return ͼ��·��
	 */
	public String getPath() {
		return this.path;
	}

	/**
	 * ����ͼ��·��
	 * 
	 * @param path
	 *            ͼ��·��
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * ��ȡͼ����
	 * 
	 * @return ͼ����
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * ����ͼ����
	 * 
	 * @param width
	 *            ͼ����
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * ��ȡͼ��߶�
	 * 
	 * @return ͼ��߶�
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * ����ͼ��߶�
	 * 
	 * @param height
	 *            ͼ��߶�
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * ��ȡͼ������
	 * 
	 * @return ͼ������
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * ����ͼ������
	 * 
	 * @param type
	 *            ͼ������
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
