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
 * 角色
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class Role extends CacheableObject {

	/**
	 * 管理员角色id
	 */
	public static final String ROLE_ADMIN = "admin";

	/**
	 * 普通用户角色id
	 */
	public static final String ROLE_USER = "user";
	
	/**
	 * 没有授权
	 */
	public static final int AUTH_NONE = 0;

	/**
	 * 通过用户授权
	 */
	public static final int AUTH_BY_PERSON = 1;

	/**
	 * 通过组织授权
	 */
	public static final int AUTH_BY_ORGANIZATION = 2;
	
	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = -8336725740586624232L;

	/**
	 * id
	 */
	protected String id;

	/**
	 * 名称
	 */
	protected String name;

	/**
	 * 描述
	 */
	protected String remark;

	/**
	 * 优先级
	 */
	protected int priority;

	/**
	 * 记录关联的组织
	 */
	protected int[] organizations = null;

	/**
	 * 记录关联的人员
	 */
	protected int[] persons = null;

	/**
	 * 关联的组织、人员定义，只是为了序列化使用的，此变量不设置值
	 */
	@SuppressWarnings("unused")
	private String relationDesc = null;
	
	/**
	 * 移除一个用户关联
	 * @param userId 用户id
	 * @throws CacheException 对象被缓存时发生
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
	 * 获取关联的组织、人员定义
	 * 
	 * @return 关联的组织、人员定义
	 * @see #relationDesc
	 */
	public String getRelationDesc() {
		StringBuilder sb = new StringBuilder();
		sb.append("关联的组织：");
		int count = 0;
		if (this.organizations != null) {
			for (int i = 0; i < this.organizations.length; i++) {
				ResourceObject obj = ServiceManager.getResourceService()
						.getResourceById(this.organizations[i]);
				if (obj != null && obj instanceof Organization) {
					if (count > 0)
						sb.append("，");
					count++;
					sb.append(obj.getName());
				}
			}
		}
		if (count == 0)
			sb.append("无");
		count = 0;
		sb.append("，关联的人员：");
		if (this.persons != null) {
			for (int i = 0; i < this.persons.length; i++) {
				ResourceObject obj = ServiceManager.getResourceService()
						.getResourceById(this.persons[i]);
				if (obj != null && obj instanceof Person) {
					if (count > 0)
						sb.append("，");
					count++;
					sb.append(obj.getName());
				}
			}
		}
		if (count == 0)
			sb.append("无");
		return sb.toString();
	}

	/**
	 * 设置关联的组织、人员定义
	 * 
	 * @param relationDesc
	 *            关联的组织、人员定义
	 * @see #relationDesc
	 */
	public void setRelationDesc(String relationDesc) {
		// 不作更新
	}

	/**
	 * 判断角色中是否包含用户
	 * 
	 * @param userId
	 *            用户id
	 * @return true=包含，false=不包含
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
	 * 判断角色中是否包含用户
	 * 
	 * @param org
	 *            组织
	 * @return true=包含，false=不包含
	 */
	public boolean hasOrganization(Organization org) {
		if (this.organizations == null || org == null)
			return false;
		if (org.isChildOf(this.organizations))
			return true;
		return false;
	}

	/**
	 * 获取优先级
	 * 
	 * @return 优先级
	 * @see #priority
	 */
	public int getPriority() {
		return this.priority;
	}

	/**
	 * 设置优先级
	 * 
	 * @param priority
	 *            优先级
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @throws Exception
	 *             参数值非法时发生
	 * @see #priority
	 */
	public void setPriority(int priority) throws CacheException, Exception {
		checkSet();
		if (priority <= 0 || priority > 32767)
			throw new Exception("priority必须在1-32767之间");
		this.priority = priority;
	}

	/**
	 * 获取id
	 * 
	 * @return id
	 * @see #id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * 设置id
	 * 
	 * @param id
	 *            id
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #id
	 */
	public void setId(String id) throws CacheException {
		checkSet();
		this.id = id;
	}

	/**
	 * 获取名称
	 * 
	 * @return 名称
	 * @see #name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 设置名称
	 * 
	 * @param name
	 *            名称
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #name
	 */
	public void setName(String name) throws CacheException {
		checkSet();
		this.name = name;
	}

	/**
	 * 获取描述
	 * 
	 * @return 描述
	 * @see #remark
	 */
	public String getRemark() {
		if (this.remark == null || this.remark.length() == 0)
			return this.name;
		return this.remark;
	}

	/**
	 * 设置描述
	 * 
	 * @param remark
	 *            描述
	 * @throws CacheException
	 *             对象已被缓存时发生
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
	 * 获取所有者的字符串描述
	 * @return 所有者的字符串描述
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
	 * 获取关联的组织
	 * @return 关联的组织
	 * @see #organizations
	 */
	public int[] getOrganizations() {
		return this.organizations;
	}

	/**
	 * 设置关联的组织
	 * @param organizations 关联的组织
	 * @see #organizations
	 */
	public void setOrganizations(int[] organizations) {
		this.organizations = organizations;
	}

	/**
	 * 获取关联的人员
	 * @return 关联的人员
	 * @see #persons
	 */
	public int[] getPersons() {
		return this.persons;
	}

	/**
	 * 设置关联的人员
	 * @param persons 关联的人员
	 * @see #persons
	 */
	public void setPersons(int[] persons) {
		this.persons = persons;
	}
}
