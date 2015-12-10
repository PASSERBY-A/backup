package com.hp.idc.resm.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.hp.idc.cas.log.LoginLogManager;
import com.hp.idc.portal.security.PersonManager;
import com.hp.idc.resm.cache.CacheException;
import com.hp.idc.resm.cache.CacheableObject;
import com.hp.idc.resm.service.ServiceManager;
import com.hp.idc.resm.util.ICompareKeyGetter;
import com.hp.idc.resm.util.StringUtil;

/**
 * 模型属性定义
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public final class ModelAttribute extends CacheableObject {

	/**
	 * log4j日志
	 */
	private static final long serialVersionUID = 1996829904760110798L;

	/**
	 * 所属模型ID
	 */
	private String modelId = null;

	/**
	 * 属性ID
	 */
	private String attrId;

	/**
	 * 可以被继承
	 */
	private boolean inheritable = true;

	/**
	 * 默认值
	 */
	private String defaultValue = null;

	/**
	 * 备注
	 */
	private String remark = null;

	/**
	 * 名称
	 */
	private String name = null;

	/**
	 * 允许为空
	 */
	private boolean nullable = false;
	
	/**
	 * 设置模型内, 此属性值的校验值, 如最大值, 最小值, 可与其他属性值的比较等
	 * >2&&<3, >{minsize} || <{maxsize}
	 */
	private String condition = null;

	/**
	 * 资源属性定义，只是为了序列化使用的，此变量不设置值
	 */
	@SuppressWarnings("unused")
	private AttributeDefine define = null;

	@Override
	public Map<String, String> getLogExtendInfo() {
		Map<String, String> m = new HashMap<String, String>();
		m.put("modelId", this.modelId);
		m.put("attrId", this.attrId);
		m.put("inheritable", this.inheritable ? "1" : "0");
		m.put("name", this.name);
		m.put("remark", this.remark);
		m.put("defaultValue", this.defaultValue);
		m.put("nullable", this.nullable ? "1" : "0");
		m.put("condition", this.condition);
		return m;
	}

	/**
	 * 获取数据库的字段描述，如varchar2(40)或number等
	 * 
	 * @return 数据库的字段描述
	 */
	public String getDatabaseField() {
		AttributeDefine d = getDefine();
		String t = d.getDataType();
		if (t != null) {
			int n = d.getLength();
			if (n > 0)
				t = t + "(" + n + ")";
		}
		return t;
	}

	/**
	 * 获取资源属性定义
	 * 
	 * @return 资源属性定义
	 */
	public AttributeDefine getDefine() {
		return ServiceManager.getAttributeService()
				.getAttributeById(this.attrId);
	}
	
	/**
	 * 设置资源属性定义，此方法无效，资源属性定义是从缓存中读取的
	 * 
	 * @param define
	 *            资源属性定义
	 */
	public void setDefine(AttributeDefine define) {
		// 空函数
	}

	/**
	 * 获取资源属性ID
	 * 
	 * @return 资源属性ID
	 */
	public String getAttrId() {
		return this.attrId;
	}

	/**
	 * 设置资源属性ID
	 * 
	 * @param attrId
	 *            资源属性ID
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setAttrId(String attrId) throws CacheException {
		checkSet();
		this.attrId = attrId.toLowerCase().trim();
	}

	/**
	 * 获取描述信息
	 * 
	 * @return 描述信息
	 */
	public String getRemark() {
		if (this.remark == null) {
			return getDefine().getRemark();
		}
		return this.remark;
	}

	/**
	 * 设置描述信息
	 * 
	 * @param remark
	 *            描述信息
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setRemark(String remark) throws CacheException {
		checkSet();
		this.remark = remark;
	}

	/**
	 * 获取属性名称
	 * 
	 * @return 属性名称
	 */
	public String getName() {
		if (this.name == null) {
			return getDefine().getName();
		}
		return this.name;
	}

	/**
	 * 设置属性名称
	 * 
	 * @param name
	 *            属性名称
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setName(String name) throws CacheException {
		checkSet();
		this.name = name;
	}

	/**
	 * 表示是否可继承
	 * 
	 * @return true为可继承，false为不可继承
	 */
	public boolean isInheritable() {
		return this.inheritable;
	}

	/**
	 * 设置是否可继承
	 * 
	 * @param inheritable
	 *            true为可继承，false为不可继承
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setInheritable(boolean inheritable) throws CacheException {
		checkSet();
		this.inheritable = inheritable;
	}

	/**
	 * 获取默认值
	 * 
	 * @return 默认值
	 */
	public String getDefaultValue() {
		if (this.defaultValue != null) {
			if (this.defaultValue.equals("{user}")) {
				return PersonManager.getPersonNameById(LoginLogManager.currentUser);
			}
			if (this.defaultValue.equals("{date}")) {
				return StringUtil.getDateTime("yyyy-MM-dd", System.currentTimeMillis());
			}
			if (this.defaultValue.equals("{datetime}")) {
				return StringUtil.getDateTime("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis());
			}
		}
		return this.defaultValue;
	}

	/**
	 * 设置默认值
	 * 
	 * @param defaultValue
	 *            默认值
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setDefaultValue(String defaultValue) throws CacheException {
		checkSet();
		this.defaultValue = defaultValue;
	}

	/**
	 * 表示是否可为空值
	 * 
	 * @return true=可为空值，false=不可为空值
	 */
	public boolean isNullable() {
		return this.nullable;
	}

	/**
	 * 设置是否可为空值
	 * 
	 * @param nullable
	 *            true=可为空值，false=不可为空值
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setNullable(boolean nullable) throws CacheException {
		checkSet();
		this.nullable = nullable;
	}

	/**
	 * 获取模型内, 此属性值的校验值
	 * @return
	 */
	public String getCondition() {
		return condition;
	}

	/**
	 * 设置模型内, 此属性值的校验值
	 * @param condition
	 * @throws CacheException
	 */
	public void setCondition(String condition) throws CacheException {
		checkSet();
		this.condition = condition;
	}

	/**
	 * 设置资源模型id
	 * 
	 * @param modelId
	 *            资源模型id
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setModelId(String modelId) throws CacheException {
		checkSet();
		if (modelId == null)
			this.modelId = "";
		else
			this.modelId = modelId.toLowerCase().trim();
	}

	/**
	 * 获取资源模型id
	 * 
	 * @return 资源模型id
	 */
	public String getModelId() {
		return this.modelId;
	}

	@Override
	protected PreparedStatement getStatement(Connection conn) throws Exception {
		PreparedStatement stmt = null;
		stmt = conn.prepareStatement("select t.* from " + getDatabaseTable()
				+ " t where " + "modelid=? and attrid=?",
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		stmt.setString(1, getModelId());
		stmt.setString(2, getAttrId());
		return stmt;
	}

	@Override
	public String getPrimaryKey() {
		return this.modelId + "." + this.attrId;
	}

	@Override
	public String getDatabaseTable() {
		return "resm_model_attribute";
	}

	@Override
	public String getPrimaryKeyField() {
		// 在此不起作用
		return null;
	}

	@Override
	public void dump() {
		System.out.println("模型属性：modelId=" + this.modelId + ",attrId="
				+ this.attrId);
	}

	@Override
	public void readFromResultSet(ResultSet rs) throws Exception {
		setModelId(rs.getString("MODELID"));
		setAttrId(rs.getString("ATTRID"));
		this.name = rs.getString("NAME");
		this.remark = rs.getString("REMARK");
		this.defaultValue = rs.getString("DEFAULTVALUE");
		this.inheritable = rs.getInt("INHERITABLE") == 1;
		this.nullable = rs.getInt("NULLABLE") == 1;
		this.condition = rs.getString("CONDITION");
	}

	@Override
	public void updateResultSet(Connection conn, ResultSet rs)
			throws SQLException {
		rs.updateString("MODELID", this.modelId);
		rs.updateString("ATTRID", this.attrId);
		//if (this.name != null && this.name.equals(getDefine().getName()))
			//rs.updateNull("name");
		//else
		rs.updateString("NAME", this.name);
		if (this.remark != null && this.remark.equals(getDefine().getRemark()))
			rs.updateNull("remark");
		else
			rs.updateString("REMARK", this.remark);
		rs.updateString("DEFAULTVALUE", this.defaultValue);
		rs.updateInt("INHERITABLE", this.inheritable ? 1 : 0);
		rs.updateInt("NULLABLE", this.nullable ? 1 : 0);
		rs.updateString("CONDITION", this.condition);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof ModelAttribute))
			return false;
		ModelAttribute m = (ModelAttribute) obj;
		if (StringUtil.compare(m.modelId, this.modelId) != 0
				|| StringUtil.compare(m.attrId, this.attrId) != 0
				|| StringUtil.compare(m.getName(), this.getName()) != 0
				|| StringUtil.compare(m.getRemark(), this.getRemark()) != 0
				|| StringUtil.compare(m.defaultValue, this.defaultValue) != 0
				|| m.inheritable != this.inheritable
				|| m.nullable != this.nullable
				|| StringUtil.compare(m.getCondition(), this.condition) != 0)
			return false;
		return true;
	}

	/**
	 * 生成模型属性的modelId
	 * 
	 * @author 梅园
	 * 
	 */
	public static class ModelAttributeModelIdGetter implements
			ICompareKeyGetter<String, ModelAttribute> {

		public String getCompareKey(ModelAttribute obj) {
			return obj.getModelId();
		}
	}
}
