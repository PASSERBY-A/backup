/**
 * 
 */
package com.hp.idc.resm.security;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import oracle.sql.CLOB;

import com.hp.idc.json.JSONArray;
import com.hp.idc.json.JSONException;
import com.hp.idc.json.JSONObject;
import com.hp.idc.resm.cache.CacheException;
import com.hp.idc.resm.cache.CacheableObject;
import com.hp.idc.resm.resource.ResourceObject;
import com.hp.idc.resm.service.ServiceManager;
import com.hp.idc.resm.util.DbUtil;

/**
 * ��ɫ
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class Role extends CacheableObject {

	/**
	 * ����Ա��ɫid
	 */
	public static final String ROLE_ADMIN = "admin";

	/**
	 * ��ͨ�û���ɫid
	 */
	public static final String ROLE_USER = "user";
	
	/**
	 * û����Ȩ
	 */
	public static final int AUTH_NONE = 0;

	/**
	 * ͨ���û���Ȩ
	 */
	public static final int AUTH_BY_PERSON = 1;

	/**
	 * ͨ����֯��Ȩ
	 */
	public static final int AUTH_BY_ORGANIZATION = 2;
	
	/**
	 * ���л�id
	 */
	private static final long serialVersionUID = -8336725740586624232L;

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
	 * ���ȼ�
	 */
	protected int priority;

	/**
	 * ��¼��������֯
	 */
	protected int[] organizations = null;

	/**
	 * ��¼��������Ա
	 */
	protected int[] persons = null;

	/**
	 * ��������֯����Ա���壬ֻ��Ϊ�����л�ʹ�õģ��˱���������ֵ
	 */
	@SuppressWarnings("unused")
	private String relationDesc = null;
	
	/**
	 * �Ƴ�һ���û�����
	 * @param userId �û�id
	 * @throws CacheException ���󱻻���ʱ����
	 */
	public void removePerson(int userId) throws CacheException {
		checkSet();
		if (this.persons != null) {
			for (int i = 0; i < this.persons.length; i++)
				if (this.persons[i] == userId)
					this.persons[i] = -1;
		}
	}

	@Override
	public Map<String, String> getLogExtendInfo() {
		Map<String, String> m = new HashMap<String, String>();
		m.put("id", this.id);
		m.put("name", this.name);
		m.put("remark", this.remark);
		m.put("priority", "" + this.priority);
		String owner = getOwner();
		if (owner.length() > 4000)
			owner = owner.substring(0, 4000);
		m.put("owner", owner);
		return m;
	}
	
	/**
	 * ��ȡ��������֯����Ա����
	 * 
	 * @return ��������֯����Ա����
	 * @see #relationDesc
	 */
	public String getRelationDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("��������֯��");
		int count = 0;
		if (this.organizations != null) {
			for (int i = 0; i < this.organizations.length; i++) {
				ResourceObject obj = ServiceManager.getResourceService()
						.getResourceById(this.organizations[i]);
				if (obj != null && obj instanceof Organization) {
					if (count > 0)
						sb.append("��");
					count++;
					sb.append(obj.getName());
				}
			}
		}
		if (count == 0)
			sb.append("��");
		count = 0;
		sb.append("����������Ա��");
		if (this.persons != null) {
			for (int i = 0; i < this.persons.length; i++) {
				ResourceObject obj = ServiceManager.getResourceService()
						.getResourceById(this.persons[i]);
				if (obj != null && obj instanceof Person) {
					if (count > 0)
						sb.append("��");
					count++;
					sb.append(obj.getName());
				}
			}
		}
		if (count == 0)
			sb.append("��");
		return sb.toString();
	}

	/**
	 * ���ù�������֯����Ա����
	 * 
	 * @param relationDesc
	 *            ��������֯����Ա����
	 * @see #relationDesc
	 */
	public void setRelationDesc(String relationDesc) {
		// ��������
	}

	/**
	 * �жϽ�ɫ���Ƿ�����û�
	 * 
	 * @param userId
	 *            �û�id
	 * @return true=������false=������
	 */
	public boolean hasUser(int userId) {
		if (this.persons == null)
			return false;
		for (int p : this.persons) {
			if (p == userId)
				return true;
		}
		return false;
	}

	/**
	 * �жϽ�ɫ���Ƿ�����û�
	 * 
	 * @param org
	 *            ��֯
	 * @return true=������false=������
	 */
	public boolean hasOrganization(Organization org) {
		if (this.organizations == null || org == null)
			return false;
		if (org.isChildOf(this.organizations))
			return true;
		return false;
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
	 * @see #remark
	 */
	public String getRemark() {
		if (this.remark == null || this.remark.length() == 0)
			return this.name;
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

	@Override
	public String getPrimaryKey() {
		return this.id;
	}

	@Override
	public String getDatabaseTable() {
		return "resm_role";
	}

	@Override
	public String getPrimaryKeyField() {
		return "id";
	}
	
	/**
	 * ��ȡ�����ߵ��ַ�������
	 * @return �����ߵ��ַ�������
	 */
	private String getOwner() {
		JSONObject json = new JSONObject();
		JSONArray arr = new JSONArray();
		if (this.organizations != null) {
			for (int i = 0; i < this.organizations.length; i++)
				if (this.organizations[i] > 0)
					arr.put(this.organizations[i]);
		}
		try {
			json.put("organizations", arr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		arr = new JSONArray();
		if (this.persons != null) {
			for (int i = 0; i < this.persons.length; i++)
				if (this.persons[i] > 0)
					arr.put(this.persons[i]);
		}
		try {
			json.put("persons", arr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	@Override
	public void updateResultSet(Connection conn, ResultSet rs) throws Exception {
		rs.updateString("id", this.id);
		rs.updateString("name", this.name);
		rs.updateString("remark", this.remark);
		rs.updateInt("priority", this.priority);

		CLOB clob = DbUtil.createTemporaryCLOB(conn, true,
				oracle.sql.CLOB.DURATION_SESSION);
		clob.setString(1, getOwner());
		rs.updateClob("owner", clob);
	}

	@Override
	public void readFromResultSet(ResultSet rs) throws Exception {
		this.id = rs.getString("id");
		this.name = rs.getString("name");
		this.remark = rs.getString("remark");
		this.priority = rs.getInt("priority");

		String owner = DbUtil.clobToString(rs.getClob("owner"));
		if (owner == null || owner.length() == 0)
			owner = "{}";
		JSONObject json = new JSONObject(owner);
		if (json.has("organizations")) {
			JSONArray arr = json.getJSONArray("organizations");
			this.organizations = new int[arr.length()];
			for (int i = 0; i < arr.length(); i++)
				this.organizations[i] = arr.getInt(i);
		}
		if (json.has("persons")) {
			JSONArray arr = json.getJSONArray("persons");
			this.persons = new int[arr.length()];
			for (int i = 0; i < arr.length(); i++)
				this.persons[i] = arr.getInt(i);
		}

	}

	/**
	 * ��ȡ��������֯
	 * @return ��������֯
	 * @see #organizations
	 */
	public int[] getOrganizations() {
		return this.organizations;
	}

	/**
	 * ���ù�������֯
	 * @param organizations ��������֯
	 * @see #organizations
	 */
	public void setOrganizations(int[] organizations) {
		this.organizations = organizations;
	}

	/**
	 * ��ȡ��������Ա
	 * @return ��������Ա
	 * @see #persons
	 */
	public int[] getPersons() {
		return this.persons;
	}

	/**
	 * ���ù�������Ա
	 * @param persons ��������Ա
	 * @see #persons
	 */
	public void setPersons(int[] persons) {
		this.persons = persons;
	}
}
