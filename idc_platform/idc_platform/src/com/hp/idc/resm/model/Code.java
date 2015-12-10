package com.hp.idc.resm.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.hp.idc.resm.cache.CacheException;
import com.hp.idc.resm.cache.CacheableObject;
import com.hp.idc.resm.util.ICompareKeyGetter;

/**
 * ��Դ����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class Code extends CacheableObject {

	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = -4778198918104489610L;

	/**
	 * ����
	 */
	private int oid;

	/**
	 * ����ID
	 */
	private String id;

	/**
	 * ��������
	 */
	private String name;

	/**
	 * ����˵��
	 */
	private String remark;

	/**
	 * ������oid
	 */
	private int parentOid;

	/**
	 * ����
	 */
	private int order;

	/**
	 * �Ӵ���
	 */
	private List<Code> childs = null;

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 * @see #order
	 */
	public int getOrder() {
		return this.order;
	}

	/**
	 * ��������
	 * 
	 * @param order
	 *            ����
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @see #order
	 */
	public void setOrder(int order) throws CacheException {
		checkSet();
		this.order = order;
	}

	/**
	 * ��ȡoid
	 * 
	 * @return oid oid
	 * @see #oid
	 */
	public int getOid() {
		return this.oid;
	}

	/**
	 * ����oid
	 * 
	 * @param oid
	 *            oid
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @see #oid
	 */
	public void setOid(int oid) throws CacheException {
		checkSet();
		this.oid = oid;
	}

	/**
	 * ��ȡ��oid
	 * 
	 * @return parentOid ��oid
	 * @see #parentOid
	 */
	public int getParentOid() {
		return this.parentOid;
	}

	/**
	 * ���ø�oid
	 * 
	 * @param parentOid
	 *            ��oid
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @see #parentOid
	 */
	public void setParentOid(int parentOid) throws CacheException {
		checkSet();
		this.parentOid = parentOid;
	}

	/**
	 * ��ȡ����id
	 * 
	 * @return ����id
	 * @see #id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * ����id
	 * 
	 * @param id
	 *            id
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @see #id
	 */
	public void setId(String id) throws CacheException {
		checkSet();
		this.id = id;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return name ��������
	 * @see #name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * ���ô�������
	 * 
	 * @param name
	 *            ��������
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @see #name
	 */
	public void setName(String name) throws CacheException {
		checkSet();
		this.name = name;
	}

	/**
	 * ��ȡ����˵��
	 * 
	 * @return remark ����˵��
	 * @see #remark
	 */
	public String getRemark() {
		return this.remark;
	}

	/**
	 * ���ô�������
	 * 
	 * @param remark
	 *            ����˵��
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @see #remark
	 */
	public void setRemark(String remark) throws CacheException {
		checkSet();
		this.remark = remark;
	}

	/**
	 * ��ȡ�����Ӵ���
	 * 
	 * @return �����Ӵ���
	 * @see #childs
	 */
	public List<Code> getChilds() {
		return this.childs;
	}

	/**
	 * �����Ӵ���
	 * 
	 * @param childs
	 *            �Ӵ���
	 * @see #childs
	 */
	public void setChilds(List<Code> childs) {
		// NOTHING TO DO
	}

	/**
	 * ����Ӵ���
	 * 
	 * @param c
	 *            �������
	 */
	public void addChild(Code c) {
		if (this.childs == null)
			this.childs = new ArrayList<Code>();
		this.childs.add(c);
	}

	@Override
	public String getPrimaryKey() {
		return "" + this.oid;
	}

	@Override
	public String getDatabaseTable() {
		return "resm_code";
	}

	@Override
	public String getPrimaryKeyField() {
		return "oid";
	}

	@Override
	public void updateResultSet(Connection conn, ResultSet rs) {
		// ��������
	}

	@Override
	public void readFromResultSet(ResultSet rs) throws Exception {
		this.id = rs.getString("id");
		this.name = rs.getString("name");
		this.remark = rs.getString("remark");
		this.oid = rs.getInt("oid");
		this.parentOid = rs.getInt("parentoid");
		this.order = rs.getInt("itemorder");
	}

	/**
	 * ���ɴ����id
	 * 
	 * @author ÷԰
	 * 
	 */
	public static class CodeIdGetter implements ICompareKeyGetter<String, Code> {

		public String getCompareKey(Code obj) {
			return obj.getId();
		}
	}
}
