/**
 * 
 */
package com.hp.idc.resm.security;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.hp.idc.resm.cache.CacheException;
import com.hp.idc.resm.cache.CacheableObject;
import com.hp.idc.resm.util.RoleUtil;

/**
 * Ȩ��
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class BasicPermission extends CacheableObject {

	/**
	 * ���л�id
	 */
	private static final long serialVersionUID = 1101065225362372052L;

	/**
	 * ����
	 */
	public static final int PERM_ALLOW = 1;

	/**
	 * ����
	 */
	public static final int PERM_PASS = 0;

	/**
	 * �ܾ�
	 */
	public static final int PERM_DENY = -1;

	/**
	 * id
	 */
	protected String id;

	/**
	 * ����
	 */
	protected String name;

	/**
	 * ����
	 */
	protected String remark;

	/**
	 * ��ȡid
	 * 
	 * @return id
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
	 * ��ȡ����
	 * 
	 * @return ����
	 * @see #name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * ��������
	 * 
	 * @param name
	 *            ����
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @see #name
	 */
	public void setName(String name) throws CacheException {
		checkSet();
		this.name = name;
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @see #remark
	 */
	public String getRemark() throws CacheException {
		checkSet();
		return this.remark;
	}

	/**
	 * ��������
	 * 
	 * @param remark
	 *            ����
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @see #remark
	 */
	public void setRemark(String remark) throws CacheException {
		checkSet();
		this.remark = remark;
	}

	/**
	 * ��Ȩ�޽��н���
	 * 
	 * @param userId
	 *            �û�id
	 * @param obj
	 *            ����Ķ���
	 * @return PERM_ALLOW=��Ȩ��, PERM_PASS=����, PERM_DENY=��Ȩ��/�ܾ�����
	 * @throws Exception
	 *             �������쳣ʱ����
	 * @see #PERM_ALLOW
	 * @see #PERM_PASS
	 * @see #PERM_DENY
	 */
	public int valid(int userId, Object obj) throws Exception {
		return PERM_ALLOW; // Ĭ�Ͽ��Է���
	}

	/**
	 * ���б���й��ˣ�����Ȩ�ޡ���Ȩ�޵Ķ�����б������
	 * @param <T> ��������
	 * 
	 * @param userId
	 *            �û�id
	 * @param list
	 *            �����б��������������б���ֻ������ƥ��Ķ���
	 * @return ��Ȩ�޵Ķ���
	 * @throws Exception
	 *             �������쳣ʱ����
	 */
	public<T> List<T> filter(int userId, List<T> list)
			throws Exception {
		List<T> ret = new ArrayList<T>();
		if (RoleUtil.isSystemAdmin(userId))
			ret.addAll(list);
		list.clear();
		return ret;
	}

	/**
	 * ���ò���������������ʵ�ִ˺���
	 * 
	 * @param param
	 *            ��������
	 */
	public void setParameter(String param) {
		// ����������ʵ�ִ˺���
	}

	/**
	 * ��ȡ����˵��
	 * 
	 * @return ����˵��
	 */
	public String getParamDesc() {
		return "";
	}

	@Override
	public String getPrimaryKey() {
		return this.id;
	}

	@Override
	public String getDatabaseTable() {
		return "resm_permission";
	}

	@Override
	public String getPrimaryKeyField() {
		return "id";
	}

	@Override
	public void updateResultSet(Connection conn, ResultSet rs) {
		// �����£�����ЩȨ���ں�̨ά��
	}

	@Override
	public void readFromResultSet(ResultSet rs) throws Exception {
		this.id = rs.getString("id");
		this.name = rs.getString("name");
		this.remark = rs.getString("remark");
	}
}
