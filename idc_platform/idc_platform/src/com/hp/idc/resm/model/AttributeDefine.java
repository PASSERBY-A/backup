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
 * ��Դ���Զ���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public abstract class AttributeDefine extends CacheableObject {

	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = -7655613666235600261L;

	/**
	 * ����ID
	 */
	private String id;

	/**
	 * ��������
	 */
	private String name;

	/**
	 * ��������
	 */
	private String type;

	/**
	 * ���Բ�������JSON��ʽ�������� { size: 40, format: "aabbcc" }
	 */
	private String arguments;

	/**
	 * ��λ�������磺GB
	 */
	private String unitName;

	/**
	 * �ֶγ���
	 */
	protected int length;

	/**
	 * ����������
	 */
	private String checker;

	/**
	 * ת���ı�����
	 */
	private String converterClass;

	/**
	 * �Ƿ���Ч
	 */
	private boolean enabled;

	/**
	 * ά�ȱ�־
	 */
	private boolean dimension;

	/**
	 * �Ƿ�Ϊ�ؼ�����
	 */
	private boolean important;

	/**
	 * ��ע
	 */
	private String remark;
	
	/**
	 * ���������壬ֻ��Ϊ�����л�ʹ�õģ��˱���������ֵ
	 */
	@SuppressWarnings("unused")
	private List<AttributeOperator> operators = null;

	/**
	 * �������壬ֻ��Ϊ�����л�ʹ�õģ��˱���������ֵ
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
	 * ��ȡ���ݿ��ֶ�����
	 * 
	 * @return �ֶ���������
	 */
	public abstract String getDataType();

	/**
	 * ��ȡ�Ƿ�Ϊ�ؼ�����
	 * 
	 * @return important �Ƿ�Ϊ�ؼ�����
	 * @see #important
	 */
	public boolean isImportant() {
		return this.important;
	}

	/**
	 * �����Ƿ�Ϊ�ؼ�����
	 * 
	 * @param important
	 *            �Ƿ�Ϊ�ؼ�����
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @see #important
	 */
	public void setImportant(boolean important) throws CacheException {
		checkSet();
		this.important = important;
	}

	/**
	 * ��ȡ�Ƿ�Ϊά��
	 * 
	 * @return dimension �Ƿ�Ϊά��
	 * @see #dimension
	 */
	public boolean isDimension() {
		return this.dimension;
	}

	/**
	 * �����Ƿ�Ϊά��
	 * 
	 * @param dimension
	 *            �Ƿ�Ϊά��
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @see #dimension
	 */
	public void setDimension(boolean dimension) throws CacheException {
		checkSet();
		this.dimension = dimension;
	}

	/**
	 * ����AttributeBaseʵ��
	 * 
	 * @param modelId
	 *            ģ��id
	 * 
	 * @return AttributeBaseʵ��
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
	 * ����AttributeBaseʵ��
	 * @return AttributeBaseʵ��
	 */
	protected abstract AttributeBase createInstance();

	/**
	 * ��ȡ������ֶγ���
	 * 
	 * @return �ֶγ���
	 */
	public abstract int getLength();

	/**
	 * �����ֶγ���
	 * 
	 * @param length
	 *            �ֶγ���
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @throws Exception ����ֵ�Ƿ�ʱ����
	 */
	public void setLength(int length) throws CacheException, Exception {
		checkSet();
		if (length <= 0 || length > 4000)
			throw new Exception("length������1-4000֮��");
		this.length = length;
	}

	/**
	 * ��ȡ����������
	 * 
	 * @return ����������
	 */
	public String getChecker() {
		return this.checker;
	}

	/**
	 * ���ý���������
	 * 
	 * @param checker
	 *            ����������
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setChecker(String checker) throws CacheException {
		checkSet();
		this.checker = checker;
	}

	/**
	 * ��ȡת���ı�����
	 * 
	 * @return ת���ı�����
	 */
	public String getConverterClass() {
		return this.converterClass;
	}

	/**
	 * ����ת���ı�����
	 * 
	 * @param converterClass
	 *            ת���ı�����
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setConverterClass(String converterClass) throws CacheException {
		checkSet();
		this.converterClass = converterClass;
	}

	/**
	 * �Ƿ���Ч
	 * 
	 * @return �Ƿ���Ч
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * �����Ƿ���Ч
	 * 
	 * @param enabled
	 *            �Ƿ���Ч
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setEnabled(boolean enabled) throws CacheException {
		checkSet();
		this.enabled = enabled;
	}

	/**
	 * ��ȡ˵����Ϣ
	 * 
	 * @return ˵����Ϣ
	 */
	public String getRemark() {
		return this.remark;
	}

	/**
	 * ����˵����Ϣ
	 * 
	 * @param remark
	 *            ˵����Ϣ
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setRemark(String remark) throws CacheException {
		checkSet();
		this.remark = remark;
	}

	/**
	 * ��ȡ��Դ����id
	 * 
	 * @return ��Դ����id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * ������Դ����id
	 * 
	 * @param id
	 *            ��Դ����id
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setId(String id) throws CacheException {
		checkSet();
		this.id = id.toLowerCase().trim();
	}

	/**
	 * ��ȡ��Դ��������
	 * 
	 * @return ��Դ��������
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * ������Դ��������
	 * 
	 * @param name
	 *            ��Դ��������
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setName(String name) throws CacheException {
		checkSet();
		this.name = name;
	}

	/**
	 * ��ȡ��Դ��������
	 * 
	 * @return ��Դ��������
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * ������Դ��������
	 * 
	 * @param type
	 *            ��Դ��������
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setType(String type) throws CacheException {
		checkSet();
		this.type = type;
	}

	/**
	 * ��ȡ��Դ���Բ���
	 * 
	 * @return ��Դ���Բ���
	 */
	public String getArguments() {
		return this.arguments;
	}

	/**
	 * ������Դ���Բ���
	 * 
	 * @param arguments
	 *            ��Դ���Բ���
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
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
	 * ��ȡ��Դ���Ե�λ
	 * 
	 * @return ��Դ���Ե�λ
	 */
	public String getUnitName() {
		return this.unitName;
	}

	/**
	 * ������Դ���Ե�λ
	 * 
	 * @param unitName
	 *            ��Դ���Ե�λ
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
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
		System.out.println("����(" + this.id + ") = " + this.name);
		System.out.println("type = " + this.type);
	}

	/**
	 * ���ֵ�Ƿ�Ϸ�
	 * 
	 * @param value
	 *            ��ֵ
	 * @param op
	 * 			  ������
	 * @return null��ʾͨ�������򷵻ش�������
	 */
	public String valid(String value, String value1, String attrId, String op) {
		return null;
	}

	/**
	 * �Ƿ�ȫ��Ψһ
	 * 
	 * @return true=�ֶ���ȫ��Ψһ��
	 */
	public boolean isGlobalUnique() {
		if (this.checker != null && this.checker.indexOf("/GU/") >= 0)
			return true;
		return false;
	}

	/**
	 * �Ƿ���ģ����Ψһ
	 * 
	 * @return true=�ֶ�����ģ����Ψһ��
	 */
	public boolean isModelUnique() {
		if (this.checker != null && this.checker.indexOf("/MU/") >= 0)
			return true;
		return false;
	}
	
	/**
	 * ��ȡ������
	 * @return ������
	 */
	public abstract List<AttributeOperator> getOperators();
	
	/**
	 * ���ò�����
	 * @param operators ������
	 */
	public void setOperators(List<AttributeOperator> operators) {
		// ������
	}
	
	/**
	 * ��ȡ����
	 * @return ����
	 */
	public abstract List<AttributeConst> getConsts();
	
	/**
	 * ���ó���
	 * @param consts ����
	 */
	public void setConsts(List<AttributeConst> consts) {
		// ������
	}
	
	/**
	 * ��ȡ��������
	 * @param op ������
	 * @return ��������
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
	 * ��ȡ����
	 * @param c ����id
	 * @return ����
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
	 * ��ȡ�����Ƚϵı��ʽ
	 * @param cond ��������
	 * @return �����Ƚϵı��ʽ
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
