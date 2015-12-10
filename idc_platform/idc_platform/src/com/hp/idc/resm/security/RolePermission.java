/**
 * 
 */
package com.hp.idc.resm.security;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import oracle.sql.CLOB;

import com.hp.idc.resm.cache.CacheException;
import com.hp.idc.resm.cache.CacheableObject;
import com.hp.idc.resm.service.CachedRoleService;
import com.hp.idc.resm.util.DbUtil;

/**
 * ��ɫȨ��
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class RolePermission extends CacheableObject {

	/**
	 * ���л�id
	 */
	private static final long serialVersionUID = 3609629630053251804L;

	/**
	 * id
	 */
	protected int id;

	/**
	 * Ȩ��id
	 */
	protected String roleId;

	/**
	 * Ȩ��id
	 */
	protected String permId = null;

	/**
	 * Ȩ�޲���
	 */
	protected String permParam = null;

	/**
	 * ����
	 */
	protected String remark;

	/**
	 * ���ȼ�
	 */
	protected int priority;

	/**
	 * ����˵��
	 */
	@SuppressWarnings("unused")
	private String paramDesc;

	/**
	 * ����Ȩ�޶���
	 */
	protected BasicPermission permisson = null;

	/**
	 * ��ȡ����Ȩ�޶���
	 * 
	 * @return ����Ȩ�޶���
	 * @see #permisson
	 */
	public BasicPermission getPermisson() {
		if (this.permisson == null && this.permId != null
				&& this.permId.length() > 0) {
			try {
				this.permisson = (BasicPermission) CachedRoleService.basicPermissionCache
						.get(this.permId).clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			if (this.permParam != null && this.permParam.length() > 0)
				this.permisson.setParameter(this.permParam);
		}
		return this.permisson;
	}

	/**
	 * ���û���Ȩ�޶���
	 * 
	 * @param permisson
	 *            ����Ȩ�޶���
	 * @see #permisson
	 */
	public void setPermisson(BasicPermission permisson) {
		// ������ֵ
	}

	@Override
	public Map<String, String> getLogExtendInfo() {
		Map<String, String> m = new HashMap<String, String>();
		m.put("id", "" + this.id);
		m.put("roleid", this.roleId);
		m.put("remark", this.remark);
		m.put("priority", "" + this.priority);
		m.put("permid", this.permId);
		String p = this.permParam;
		if (p == null)
			p = "";
		if (p.length() > 4000)
			p = p.substring(0, 4000);
		m.put("permparam", p);
		return m;
	}

	/**
	 * ��ȡ����˵��������������ʵ��
	 * 
	 * @return ����˵��
	 * @see #paramDesc
	 */
	public String getParamDesc() {
		if (this.permisson == null)
			return "";
		return this.permisson.getParamDesc();
	}

	/**
	 * ���ò���˵��
	 * 
	 * @param paramDesc
	 *            ����˵��
	 * @see #paramDesc
	 */
	public void setParamDesc(String paramDesc) {
		// ������
	}

	/**
	 * ��ȡ���ȼ�
	 * 
	 * @return ���ȼ�
	 * @see #priority
	 */
	public int getPriority() {
		return this.priority;
	}

	/**
	 * �������ȼ�
	 * 
	 * @param priority
	 *            ���ȼ�
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @throws Exception
	 *             ����ֵ�Ƿ�ʱ����
	 * @see #priority
	 */
	public void setPriority(int priority) throws CacheException, Exception {
		checkSet();
		if (priority <= 0 || priority > 32767)
			throw new Exception("priority������1-32767֮��");
		this.priority = priority;
	}

	/**
	 * ��ȡid
	 * 
	 * @return id
	 * @see #id
	 */
	public int getId() {
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
	public void setId(int id) throws CacheException {
		checkSet();
		this.id = id;
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 * @see #remark
	 */
	public String getRemark() {
		if (this.remark == null || this.remark.length() == 0)
			return "�ޱ�ע��Ϣ";
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
	 * ��ȡ��ɫid
	 * 
	 * @return ��ɫid
	 * @see #roleId
	 */
	public String getRoleId() {
		return this.roleId;
	}

	/**
	 * ���ý�ɫid
	 * 
	 * @param roleId
	 *            ��ɫid
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @see #roleId
	 */
	public void setRoleId(String roleId) throws CacheException {
		checkSet();
		this.roleId = roleId;
	}

	/**
	 * ��ȡȨ��id
	 * 
	 * @return Ȩ��id
	 * @see #permId
	 */
	public String getPermId() {
		return this.permId;
	}

	/**
	 * ����Ȩ��id
	 * 
	 * @param permId
	 *            Ȩ��id
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @see #permId
	 */
	public void setPermId(String permId) throws CacheException {
		checkSet();
		this.permId = permId;
		this.getPermisson(); // ����Ȩ�޶���
		if (this.permParam != null && this.permParam.length() > 0)
			this.permisson.setParameter(this.permParam);
	}

	/**
	 * ��ȡȨ�޲���
	 * 
	 * @return Ȩ�޲���
	 * @see #permParam
	 */
	public String getPermParam() {
		return this.permParam;
	}

	/**
	 * ����Ȩ�޲���
	 * 
	 * @param permParam
	 *            Ȩ�޲���
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @see #permParam
	 */
	public void setPermParam(String permParam) throws CacheException {
		checkSet();
		this.permParam = permParam;
		this.getPermisson(); // ����Ȩ�޶���
		if (this.permisson != null)
			this.permisson.setParameter(permParam);
	}

	@Override
	public String getPrimaryKey() {
		return "" + this.id;
	}

	@Override
	public String getDatabaseTable() {
		return "resm_role_permission";
	}

	@Override
	public String getPrimaryKeyField() {
		return "id";
	}

	@Override
	public void updateResultSet(Connection conn, ResultSet rs) throws Exception {
		rs.updateInt("id", this.id);
		rs.updateString("roleid", this.roleId);
		rs.updateString("remark", this.remark);
		rs.updateString("permid", this.permId);
		rs.updateInt("priority", this.priority);

		CLOB clob = DbUtil.createTemporaryCLOB(conn, true,
				oracle.sql.CLOB.DURATION_SESSION);
		clob.setString(1, this.permParam);
		rs.updateClob("permparam", clob);
	}

	@Override
	public void readFromResultSet(ResultSet rs) throws Exception {
		this.id = rs.getInt("id");
		this.roleId = rs.getString("roleid");
		setPermId(rs.getString("permid"));
		this.remark = rs.getString("remark");
		this.priority = rs.getInt("priority");
		setPermParam(DbUtil.clobToString(rs.getClob("permparam")));
	}
}
