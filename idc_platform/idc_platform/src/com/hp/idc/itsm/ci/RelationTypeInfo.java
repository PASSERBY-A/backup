package com.hp.idc.itsm.ci;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ��ʾ������ϵ���� ������ݱ� itsm_ci_relation_type
 * 
 * @author ÷԰
 * 
 */
public class RelationTypeInfo {
	/**
	 * �洢������ϵ���͵� oid
	 */
	protected int oid;

	/**
	 * �洢������ϵ���͵�����
	 */
	protected String name;

	/**
	 * �洢������������й���ʱ��������Ϣ
	 */
	protected String caption;

	/**
	 * �洢������ϵ���͵�����
	 */
	protected String desc;

	/**
	 * �洢������ϵ���͵������ϵ���͵� oid
	 */
	protected int reverseOid;

	/**
	 * �洢��������A������
	 */
	protected int typeA;

	/**
	 * �洢��������B������
	 */
	protected int typeB;

	/**
	 * �洢������ϵ�ı�־
	 */
	protected String flag;

	/**
	 * ��ʾ����Ĺ�����ϵ����
	 */
	protected RelationTypeInfo reverse = null;

	/**
	 * �����ݿⷵ�صĽ�����ĵ�ǰ��¼���з���
	 * 
	 * @param rs
	 *            �����
	 * @throws SQLException
	 *             ���ݿ�����쳣ʱ����
	 */
	protected void parse(ResultSet rs) throws SQLException {
		setOid(rs.getInt("rt_oid"));
		setName(rs.getString("rt_name"));
		setCaption(rs.getString("rt_caption"));
		setDesc(rs.getString("rt_desc"));
		setReverseOid(rs.getInt("rt_rev_oid"));
		setTypeA(rs.getInt("rt_type_a"));
		setTypeB(rs.getInt("rt_type_b"));
		setFlag(rs.getString("rt_flag"));
		// ���ܺ��Լ����������ϵ
		if (this.reverseOid == this.oid)
			this.reverseOid = -1;
	}

	/**
	 * ��ȡ��������A������
	 * 
	 * @return ���ع�������A������
	 */
	public int getTypeA() {
		return this.typeA;
	}

	/**
	 * ���ù�������A������
	 * 
	 * @param typeA
	 *            ��������A������
	 */
	public void setTypeA(int typeA) {
		this.typeA = (typeA == 0) ? -1 : typeA;
	}

	/**
	 * ��ȡ��������B������
	 * 
	 * @return ���ع�������B������
	 */
	public int getTypeB() {
		return this.typeB;
	}

	/**
	 * ���ù�������B������
	 * 
	 * @param typeB
	 *            ��������B������
	 */
	public void setTypeB(int typeB) {
		this.typeB = (typeB == 0) ? -1 : typeB;
	}

	/**
	 * ��ȡ��ָ������Ĺ�ϵ������
	 * 
	 * @param info
	 *            ָ���Ķ���
	 * @return ����������Ϣ��������Ϣ�ɱ����Ͷ����caption�еġ�#���滻Ϊָ���������������
	 */
	public String getCaption(CIInfo info) {
		String str = this.caption;
		if (info != null)
			str = this.caption.replaceAll("#", info.getName());
		return str;
	}

	/**
	 * ����������������й���ʱ��������Ϣ
	 * 
	 * @param caption
	 *            ������������й���ʱ��������Ϣ
	 * @see #getCaption(CIInfo)
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * ��ȡ������ϵ���͵�����
	 * 
	 * @return ���ع�����ϵ���͵�����
	 */
	public String getDesc() {
		return this.desc;
	}

	/**
	 * ���ù�����ϵ���͵�����
	 * 
	 * @param desc
	 *            ������ϵ���͵�����
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * ��ȡ������ϵ���͵�����
	 * 
	 * @return ���ع�����ϵ���͵�����
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * ���ù�����ϵ���͵�����
	 * 
	 * @param name
	 *            ������ϵ���͵�����
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * ��ȡ��ʾ�����ϵ�Ķ���
	 * 
	 * @return ���ر�ʾ�����ϵ�Ķ���
	 */
	public RelationTypeInfo getReverse() {
		return this.reverse;
	}

	/**
	 * ���ñ�ʾ�����ϵ�Ķ���
	 * 
	 * @param reverse
	 *            ��ʾ�����ϵ�Ķ���
	 */
	public void setReverse(RelationTypeInfo reverse) {
		this.reverse = reverse;
	}

	/**
	 * ��ȡ������ϵ���͵� oid
	 * 
	 * @return ���ع�����ϵ���͵� oid
	 */
	public int getOid() {
		return this.oid;
	}

	/**
	 * ���ù�����ϵ���͵� oid
	 * 
	 * @param oid
	 *            ������ϵ���͵� oid
	 */
	public void setOid(int oid) {
		this.oid = oid;
	}

	/**
	 * ��ȡ�����ϵ���͵� oid
	 * 
	 * @return ���������ϵ���͵� oid
	 */
	public int getReverseOid() {
		return this.reverseOid;
	}

	/**
	 * ���������ϵ���͵� oid
	 * 
	 * @param reverseOid
	 *            �����ϵ���͵� oid
	 */
	public void setReverseOid(int reverseOid) {
		this.reverseOid = reverseOid;
	}

	/**
	 * ��ȡ������ϵ�ı�־
	 * 
	 * @return ���ع�����ϵ�ı�־��1:1��ʾһ��һ�Ĺ�ϵ��1:n��ʾһ�Զ�Ĺ�ϵ
	 */
	public String getFlag() {
		return this.flag;
	}

	/**
	 * ���ù�����ϵ�ı�־
	 * 
	 * @param flag
	 *            ������ϵ�ı�־��1:1��ʾһ��һ�Ĺ�ϵ��1:n��ʾһ�Զ�Ĺ�ϵ
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}
}
