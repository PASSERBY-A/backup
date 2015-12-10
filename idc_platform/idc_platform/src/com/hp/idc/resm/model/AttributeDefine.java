package com.hp.idc.resm.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.json.JSONException;
import com.hp.idc.json.JSONObject;
import com.hp.idc.resm.cache.CacheException;
import com.hp.idc.resm.cache.CacheableObject;
import com.hp.idc.resm.expression.Condition;
import com.hp.idc.resm.resource.AttributeBase;
import com.hp.idc.resm.service.ServiceManager;

/**
 * 资源属性定义
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public abstract class AttributeDefine extends CacheableObject {

	/**
	 * 序列化UID
	 */
	private static final long serialVersionUID = -7655613666235600261L;

	/**
	 * 属性ID
	 */
	private String id;

	/**
	 * 属性名称
	 */
	private String name;

	/**
	 * 属性类型
	 */
	private String type;

	/**
	 * 属性参数，以JSON格式描述，如 { size: 40, format: "aabbcc" }
	 */
	private String arguments;

	/**
	 * 单位描述，如：GB
	 */
	private String unitName;

	/**
	 * 字段长度
	 */
	protected int length;

	/**
	 * 较验器类名
	 */
	private String checker;

	/**
	 * 转换文本类名
	 */
	private String converterClass;

	/**
	 * 是否生效
	 */
	private boolean enabled;

	/**
	 * 维度标志
	 */
	private boolean dimension;

	/**
	 * 是否为关键属性
	 */
	private boolean important;

	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 操作符定义，只是为了序列化使用的，此变量不设置值
	 */
	@SuppressWarnings("unused")
	private List<AttributeOperator> operators = null;

	/**
	 * 常量定义，只是为了序列化使用的，此变量不设置值
	 */
	@SuppressWarnings("unused")
	private List<AttributeConst> consts = null;
	
	@Override
	public Map<String, String> getLogExtendInfo() {
		Map<String, String> m = new HashMap<String, String>();
		m.put("id", this.id);
		m.put("name", this.name);
		m.put("unit", this.unitName);
		m.put("type", this.type);
		m.put("length", "" + this.length);
		m.put("arguments", this.arguments);
		m.put("converter", this.converterClass);
		m.put("enabled", this.enabled ? "1" : "0");
		m.put("checker", this.checker);
		m.put("remark", this.remark);
		m.put("important", this.important ? "1" : "0");
		m.put("dimflag", this.dimension ? "1" : "0");
		return m;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id=" + this.id);
		sb.append(",name=" + this.name);
		sb.append(",type=" + this.type);
		sb.append(",arguments=" + this.arguments);
		sb.append(",unitName=" + this.unitName);
		sb.append(",length=" + this.length);
		sb.append(",checker=" + this.checker);
		sb.append(",converterClass=" + this.converterClass);
		sb.append(",enabled=" + this.enabled);
		sb.append(",important=" + this.important);
		sb.append(",remark=" + this.remark);
		sb.append(",dimension=" + this.dimension);
		return sb.toString();
	}

	/**
	 * 获取数据库字段类型
	 * 
	 * @return 字段类型描述
	 */
	public abstract String getDataType();

	/**
	 * 获取是否为关键属性
	 * 
	 * @return important 是否为关键属性
	 * @see #important
	 */
	public boolean isImportant() {
		return this.important;
	}

	/**
	 * 设置是否为关键属性
	 * 
	 * @param important
	 *            是否为关键属性
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #important
	 */
	public void setImportant(boolean important) throws CacheException {
		checkSet();
		this.important = important;
	}

	/**
	 * 获取是否为维度
	 * 
	 * @return dimension 是否为维度
	 * @see #dimension
	 */
	public boolean isDimension() {
		return this.dimension;
	}

	/**
	 * 设置是否为维度
	 * 
	 * @param dimension
	 *            是否为维度
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #dimension
	 */
	public void setDimension(boolean dimension) throws CacheException {
		checkSet();
		this.dimension = dimension;
	}

	/**
	 * 创建AttributeBase实例
	 * 
	 * @param modelId
	 *            模型id
	 * 
	 * @return AttributeBase实例
	 */
	public AttributeBase createInstance(String modelId) {
		ModelAttribute ma = ServiceManager.getModelService().getModelAttribute(
				modelId, this.id);
		if (ma == null)
			return null;
		AttributeBase ab = createInstance();
		ab.setAttribute(ma);
		return ab;
	}
	
	/**
	 * 创建AttributeBase实例
	 * @return AttributeBase实例
	 */
	protected abstract AttributeBase createInstance();

	/**
	 * 获取定义的字段长度
	 * 
	 * @return 字段长度
	 */
	public abstract int getLength();

	/**
	 * 设置字段长度
	 * 
	 * @param length
	 *            字段长度
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @throws Exception 参数值非法时发生
	 */
	public void setLength(int length) throws CacheException, Exception {
		checkSet();
		if (length <= 0 || length > 4000)
			throw new Exception("length必须在1-4000之间");
		this.length = length;
	}

	/**
	 * 获取较验器类名
	 * 
	 * @return 较验器类名
	 */
	public String getChecker() {
		return this.checker;
	}

	/**
	 * 设置较验器类名
	 * 
	 * @param checker
	 *            较验器类名
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setChecker(String checker) throws CacheException {
		checkSet();
		this.checker = checker;
	}

	/**
	 * 获取转换文本类名
	 * 
	 * @return 转换文本类名
	 */
	public String getConverterClass() {
		return this.converterClass;
	}

	/**
	 * 设置转换文本类名
	 * 
	 * @param converterClass
	 *            转换文本类名
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setConverterClass(String converterClass) throws CacheException {
		checkSet();
		this.converterClass = converterClass;
	}

	/**
	 * 是否生效
	 * 
	 * @return 是否生效
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * 设置是否生效
	 * 
	 * @param enabled
	 *            是否生效
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setEnabled(boolean enabled) throws CacheException {
		checkSet();
		this.enabled = enabled;
	}

	/**
	 * 获取说明信息
	 * 
	 * @return 说明信息
	 */
	public String getRemark() {
		return this.remark;
	}

	/**
	 * 设置说明信息
	 * 
	 * @param remark
	 *            说明信息
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setRemark(String remark) throws CacheException {
		checkSet();
		this.remark = remark;
	}

	/**
	 * 获取资源属性id
	 * 
	 * @return 资源属性id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * 设置资源属性id
	 * 
	 * @param id
	 *            资源属性id
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setId(String id) throws CacheException {
		checkSet();
		this.id = id.toLowerCase().trim();
	}

	/**
	 * 获取资源属性名称
	 * 
	 * @return 资源属性名称
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 设置资源属性名称
	 * 
	 * @param name
	 *            资源属性名称
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setName(String name) throws CacheException {
		checkSet();
		this.name = name;
	}

	/**
	 * 获取资源属性类型
	 * 
	 * @return 资源属性类型
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * 设置资源属性类型
	 * 
	 * @param type
	 *            资源属性类型
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setType(String type) throws CacheException {
		checkSet();
		this.type = type;
	}

	/**
	 * 获取资源属性参数
	 * 
	 * @return 资源属性参数
	 */
	public String getArguments() {
		return this.arguments;
	}

	/**
	 * 设置资源属性参数
	 * 
	 * @param arguments
	 *            资源属性参数
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setArguments(String arguments) throws CacheException {
		checkSet();
		try {
			if(arguments==null){
				this.arguments = "{ }";
				return;
			}
			JSONObject obj = new JSONObject(arguments);
			this.arguments = obj.toString();
		} catch (JSONException e) {
			e.printStackTrace();
			this.arguments = "{ }";
		}
	}

	/**
	 * 获取资源属性单位
	 * 
	 * @return 资源属性单位
	 */
	public String getUnitName() {
		return this.unitName;
	}

	/**
	 * 设置资源属性单位
	 * 
	 * @param unitName
	 *            资源属性单位
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setUnitName(String unitName) throws CacheException {
		checkSet();
		this.unitName = unitName;
	}

	@Override
	public String getPrimaryKey() {
		return getId();
	}

	@Override
	public String getDatabaseTable() {
		return "resm_attribute_define";
	}

	@Override
	public String getPrimaryKeyField() {
		return "id";
	}

	@Override
	public void updateResultSet(Connection conn, ResultSet rs) throws Exception {
		rs.updateString("arguments", this.arguments);
		rs.updateString("checker", this.checker);
		rs.updateString("converter", this.converterClass);
		rs.updateInt("enabled", this.enabled ? 1 : 0);
		rs.updateString("id", this.id);
		rs.updateInt("length", this.length);
		rs.updateString("name", this.name);
		rs.updateString("remark", this.remark);
		rs.updateString("unit", this.unitName);
		rs.updateString("type", this.type);
		rs.updateInt("important", this.important ? 1 : 0);
		rs.updateInt("dimflag", this.dimension ? 1 : 0);
	}

	@Override
	public void readFromResultSet(ResultSet rs) throws Exception {
		setChecker(rs.getString("checker"));
		setConverterClass(rs.getString("converter"));
		setEnabled(rs.getInt("enabled") == 1);
		setImportant(rs.getInt("important") == 1);
		setDimension(rs.getInt("dimflag") == 1);
		setId(rs.getString("id"));
		setLength(rs.getInt("length"));
		setName(rs.getString("name"));
		setRemark(rs.getString("remark"));
		setType(rs.getString("type"));
		setUnitName(rs.getString("unit"));

		String args = rs.getString("arguments");
		if (args == null || args.length() == 0)
			args = "{ }";
		setArguments(args);
	}

	@Override
	public void dump() {
		System.out.println("属性(" + this.id + ") = " + this.name);
		System.out.println("type = " + this.type);
	}

	/**
	 * 检查值是否合法
	 * 
	 * @param value
	 *            数值
	 * @param op
	 * 			  操作符
	 * @return null表示通过，否则返回错误描述
	 */
	public String valid(String value, String value1, String attrId, String op) {
		return null;
	}

	/**
	 * 是否全局唯一
	 * 
	 * @return true=字段是全局唯一的
	 */
	public boolean isGlobalUnique() {
		if (this.checker != null && this.checker.indexOf("/GU/") >= 0)
			return true;
		return false;
	}

	/**
	 * 是否在模型中唯一
	 * 
	 * @return true=字段是在模型中唯一的
	 */
	public boolean isModelUnique() {
		if (this.checker != null && this.checker.indexOf("/MU/") >= 0)
			return true;
		return false;
	}
	
	/**
	 * 获取操作符
	 * @return 操作符
	 */
	public abstract List<AttributeOperator> getOperators();
	
	/**
	 * 设置操作符
	 * @param operators 操作符
	 */
	public void setOperators(List<AttributeOperator> operators) {
		// 不操作
	}
	
	/**
	 * 获取常量
	 * @return 常量
	 */
	public abstract List<AttributeConst> getConsts();
	
	/**
	 * 设置常量
	 * @param consts 常量
	 */
	public void setConsts(List<AttributeConst> consts) {
		// 不操作
	}
	
	/**
	 * 获取操作对象
	 * @param op 操作符
	 * @return 操作对象
	 */
	public AttributeOperator getOperator(String op) {
		List<AttributeOperator> ops = getOperators();
		for (AttributeOperator a : ops) {
			if (a.getOp().equals(op))
				return a;
		}
		return null;
	}
	
	/**
	 * 获取常量
	 * @param c 常量id
	 * @return 常量
	 */
	public AttributeConst getConst(String c) {
		List<AttributeConst> ops = getConsts();
		for (AttributeConst a : ops) {
			if (a.getId().equals(c))
				return a;
		}
		return null;
	}

	/**
	 * 获取条件比较的表达式
	 * @param cond 条件对象
	 * @return 条件比较的表达式
	 */
	public String getOperatorExpr(Condition cond) {
		AttributeOperator ao = getOperator(cond.getOp());
		String tpl = ao.getTemplate();
		tpl = tpl.replaceAll("\\$0", "$" + this.id);
		if (ao.isNeedOpValue()) {
			tpl.replaceAll("\\$1", cond.getOpValue(this));
		}
		return tpl;
	}
}
