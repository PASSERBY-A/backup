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
 * 角色权限
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class RolePermission extends CacheableObject {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 3609629630053251804L;

	/**
	 * id
	 */
	protected int id;

	/**
	 * 权限id
	 */
	protected String roleId;

	/**
	 * 权限id
	 */
	protected String permId = null;

	/**
	 * 权限参数
	 */
	protected String permParam = null;

	/**
	 * 描述
	 */
	protected String remark;

	/**
	 * 优先级
	 */
	protected int priority;

	/**
	 * 参数说明
	 */
	@SuppressWarnings("unused")
	private String paramDesc;

	/**
	 * 基本权限定义
	 */
	protected BasicPermission permisson = null;

	/**
	 * 获取基本权限定义
	 * 
	 * @return 基本权限定义
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
	 * 设置基本权限定义
	 * 
	 * @param permisson
	 *            基本权限定义
	 * @see #permisson
	 */
	public void setPermisson(BasicPermission permisson) {
		// 不设置值
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
	 * 获取参数说明，在派生类中实现
	 * 
	 * @return 参数说明
	 * @see #paramDesc
	 */
	public String getParamDesc() {
		if (this.permisson == null)
			return "";
		return this.permisson.getParamDesc();
	}

	/**
	 * 设置参数说明
	 * 
	 * @param paramDesc
	 *            参数说明
	 * @see #paramDesc
	 */
	public void setParamDesc(String paramDesc) {
		// 不操作
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
	public int getId() {
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
	public void setId(int id) throws CacheException {
		checkSet();
		this.id = id;
	}

	/**
	 * 获取描述
	 * 
	 * @return 描述
	 * @see #remark
	 */
	public String getRemark() {
		if (this.remark == null || this.remark.length() == 0)
			return "无备注信息";
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

	/**
	 * 获取角色id
	 * 
	 * @return 角色id
	 * @see #roleId
	 */
	public String getRoleId() {
		return this.roleId;
	}

	/**
	 * 设置角色id
	 * 
	 * @param roleId
	 *            角色id
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #roleId
	 */
	public void setRoleId(String roleId) throws CacheException {
		checkSet();
		this.roleId = roleId;
	}

	/**
	 * 获取权限id
	 * 
	 * @return 权限id
	 * @see #permId
	 */
	public String getPermId() {
		return this.permId;
	}

	/**
	 * 设置权限id
	 * 
	 * @param permId
	 *            权限id
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #permId
	 */
	public void setPermId(String permId) throws CacheException {
		checkSet();
		this.permId = permId;
		this.getPermisson(); // 生成权限对象
		if (this.permParam != null && this.permParam.length() > 0)
			this.permisson.setParameter(this.permParam);
	}

	/**
	 * 获取权限参数
	 * 
	 * @return 权限参数
	 * @see #permParam
	 */
	public String getPermParam() {
		return this.permParam;
	}

	/**
	 * 设置权限参数
	 * 
	 * @param permParam
	 *            权限参数
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #permParam
	 */
	public void setPermParam(String permParam) throws CacheException {
		checkSet();
		this.permParam = permParam;
		this.getPermisson(); // 生成权限对象
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
