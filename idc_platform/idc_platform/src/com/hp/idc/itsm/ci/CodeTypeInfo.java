package com.hp.idc.itsm.ci;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hp.idc.itsm.common.ITSMInfo;

/**
 * ��ʾ��������
 * 
 */
public class CodeTypeInfo extends ITSMInfo {

	/**
	 * ��ʾ�б����
	 */
	public static int TYPE_CODE = 0;

	/**
	 * ��ʾ���ʹ���
	 */
	public static int TYPE_TREE = 1;

	/**
	 * �洢�������͵�����
	 * 
	 * @see #TYPE_CODE
	 * @see #TYPE_TREE
	 */
	protected int type;

	/**
	 * �����������[���ù�������������������������]
	 */
	protected int catalog;

	/**
	 * �洢�������͵��Ƿ�����
	 */
	protected boolean enabled;

	/**
	 * �洢�������͵Ĺ�������JAVA�࣬�����͵Ĵ��붼�����ɴ����ʵ��
	 * 
	 * @deprecated
	 */
	protected Class objectClass;

	/**
	 * Ĭ�Ϲ��캯��
	 * 
	 */
	public CodeTypeInfo() {
		// Nothing todo here
	}

	/**
	 * ����һ�������Ͷ����챾ʵ�������ƴ˶������������
	 * 
	 * @deprecated
	 * @param info
	 *            �������Ͷ���
	 */
	public CodeTypeInfo(CodeTypeInfo info) {
		setOid(info.getOid());
		setName(info.getName());
		setType(info.getType());
		setEnabled(info.isEnabled());
		this.objectClass = info.getObjectClass();
	}

	/**
	 * ��ȡ�������͵Ĺ�������JAVA�࣬�����͵Ĵ��붼�����ɴ����ʵ��
	 * 
	 * @deprecated
	 * @return ���ش������͵Ĺ�������JAVA��
	 */
	public Class getObjectClass() {
		return this.objectClass;
	}

	/**
	 * ���ô������͵Ĺ�������JAVA��
	 * 
	 * @deprecated
	 * @param className
	 *            java�����������java.util.Map
	 * @throws ClassNotFoundException
	 *             �಻����ʱ����
	 */
	public void setClass(String className) throws ClassNotFoundException {
		if (className == null || className.length() == 0)
			this.objectClass = Class.forName("com.hp.idc.itsm.ci.CodeInfo");
		else
			this.objectClass = Class.forName(className);
	}

	/**
	 * �����ݿⷵ�صĽ�����ĵ�ǰ��¼���з���
	 * 
	 * @param rs
	 *            �����
	 * @throws SQLException
	 *             ���ݿ�����쳣ʱ����
	 * @throws ClassNotFoundException
	 *             �಻����ʱ����
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
	 * ��ȡ�������͵��Ƿ�����
	 * 
	 * @return ���ش������͵��Ƿ�����
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * ���ô������͵��Ƿ�����
	 * 
	 * @param enabled
	 *            �������͵��Ƿ�����
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * ��ȡ�������͵�����
	 * 
	 * @return �������͵�����
	 * @see #TYPE_CODE
	 * @see #TYPE_TREE
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * ���ô������͵�����
	 * 
	 * @param type
	 *            �������͵�����
	 * @see #TYPE_CODE
	 * @see #TYPE_TREE
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * ��ȡ�������͵���������
	 * 
	 * @return ���ش������͵�������������"�б����"��"���ʹ���"
	 */
	public String getTypeDesc() {
		return (this.type == TYPE_CODE ? "�б����" : "���ʹ���");
	}

	/**
	 * ���� id ��ѯ��������� ������id/name/type/className�ĸ���������
	 * @deprecated
	 * @param id
	 *            ��ѯ��ʶ
	 * @return ����ֵ���Ҳ���ʱ���� null
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
