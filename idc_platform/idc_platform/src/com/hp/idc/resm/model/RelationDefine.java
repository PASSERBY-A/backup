package com.hp.idc.resm.model;

import java.sql.Connection;
import java.sql.ResultSet;

import com.hp.idc.resm.cache.CacheException;
import com.hp.idc.resm.cache.CacheableObject;

/**
 * ������ϵ���Ͷ���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class RelationDefine extends CacheableObject {

	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = -7388054676690812533L;

	/**
	 * ������ϵ����
	 */
	public static final String[] TYPES = new String[] { "����", "����" };

	/**
	 * ��ϵID
	 */
	private String id;

	/**
	 * ��ϵ���ƣ�չʾ������ͼ��
	 */
	private String name;

	/**
	 * ˵���������ù�ϵʱʹ��
	 */
	private String remark;

	/**
	 * ������ϵ���࣬"����"/"����"
	 */
	private String type;
	
	/**
	 * ��ɫ, ��ʾ������ͼ��
	 */
	private String color;
	
	/**
	 * ͼ��, ��ʾ������ͼ��
	 */
	private String icon;

	/**
	 * ��ȡ������ϵid
	 * 
	 * @return ������ϵid
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * ���ù�����ϵid
	 * 
	 * @param id
	 *            ������ϵid
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setId(String id) throws CacheException {
		checkSet();
		this.id = id.trim();
	}

	/**
	 * ��ȡ������ϵ����
	 * 
	 * @return ������ϵ����
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * ���ù�����ϵ����
	 * 
	 * @param name
	 *            ������ϵ����
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setName(String name) throws CacheException {
		checkSet();
		this.name = name;
	}

	/**
	 * ��ȡ������ϵ����
	 * 
	 * @return ������ϵ����
	 */
	public String getRemark() {
		return this.remark;
	}

	/**
	 * ���ù�����ϵ����
	 * 
	 * @param remark
	 *            ������ϵ����
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setRemark(String remark) throws CacheException {
		checkSet();
		this.remark = remark;
	}

	/**
	 * ��ȡ������ϵ����
	 * 
	 * @return ������ϵ����
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * ���ù�����ϵ����
	 * 
	 * @param type
	 *            ������ϵ����
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
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
