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
 * ģ�����Զ���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public final class ModelAttribute extends CacheableObject {

	/**
	 * log4j��־
	 */
	private static final long serialVersionUID = 1996829904760110798L;

	/**
	 * ����ģ��ID
	 */
	private String modelId = null;

	/**
	 * ����ID
	 */
	private String attrId;

	/**
	 * ���Ա��̳�
	 */
	private boolean inheritable = true;

	/**
	 * Ĭ��ֵ
	 */
	private String defaultValue = null;

	/**
	 * ��ע
	 */
	private String remark = null;

	/**
	 * ����
	 */
	private String name = null;

	/**
	 * ����Ϊ��
	 */
	private boolean nullable = false;
	
	/**
	 * ����ģ����, ������ֵ��У��ֵ, �����ֵ, ��Сֵ, ������������ֵ�ıȽϵ�
	 * >2&&<3, >{minsize} || <{maxsize}
	 */
	private String condition = null;

	/**
	 * ��Դ���Զ��壬ֻ��Ϊ�����л�ʹ�õģ��˱���������ֵ
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
	 * ��ȡ���ݿ���ֶ���������varchar2(40)��number��
	 * 
	 * @return ���ݿ���ֶ�����
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
	 * ��ȡ��Դ���Զ���
	 * 
	 * @return ��Դ���Զ���
	 */
	public AttributeDefine getDefine() {
		return ServiceManager.getAttributeService()
				.getAttributeById(this.attrId);
	}
	
	/**
	 * ������Դ���Զ��壬�˷�����Ч����Դ���Զ����Ǵӻ����ж�ȡ��
	 * 
	 * @param define
	 *            ��Դ���Զ���
	 */
	public void setDefine(AttributeDefine define) {
		// �պ���
	}

	/**
	 * ��ȡ��Դ����ID
	 * 
	 * @return ��Դ����ID
	 */
	public String getAttrId() {
		return this.attrId;
	}

	/**
	 * ������Դ����ID
	 * 
	 * @param attrId
	 *            ��Դ����ID
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setAttrId(String attrId) throws CacheException {
		checkSet();
		this.attrId = attrId.toLowerCase().trim();
	}

	/**
	 * ��ȡ������Ϣ
	 * 
	 * @return ������Ϣ
	 */
	public String getRemark() {
		if (this.remark == null) {
			return getDefine().getRemark();
		}
		return this.remark;
	}

	/**
	 * ����������Ϣ
	 * 
	 * @param remark
	 *            ������Ϣ
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setRemark(String remark) throws CacheException {
		checkSet();
		this.remark = remark;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ��������
	 */
	public String getName() {
		if (this.name == null) {
			return getDefine().getName();
		}
		return this.name;
	}

	/**
	 * ������������
	 * 
	 * @param name
	 *            ��������
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setName(String name) throws CacheException {
		checkSet();
		this.name = name;
	}

	/**
	 * ��ʾ�Ƿ�ɼ̳�
	 * 
	 * @return trueΪ�ɼ̳У�falseΪ���ɼ̳�
	 */
	public boolean isInheritable() {
		return this.inheritable;
	}

	/**
	 * �����Ƿ�ɼ̳�
	 * 
	 * @param inheritable
	 *            trueΪ�ɼ̳У�falseΪ���ɼ̳�
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setInheritable(boolean inheritable) throws CacheException {
		checkSet();
		this.inheritable = inheritable;
	}

	/**
	 * ��ȡĬ��ֵ
	 * 
	 * @return Ĭ��ֵ
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
	 * ����Ĭ��ֵ
	 * 
	 * @param defaultValue
	 *            Ĭ��ֵ
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setDefaultValue(String defaultValue) throws CacheException {
		checkSet();
		this.defaultValue = defaultValue;
	}

	/**
	 * ��ʾ�Ƿ��Ϊ��ֵ
	 * 
	 * @return true=��Ϊ��ֵ��false=����Ϊ��ֵ
	 */
	public boolean isNullable() {
		return this.nullable;
	}

	/**
	 * �����Ƿ��Ϊ��ֵ
	 * 
	 * @param nullable
	 *            true=��Ϊ��ֵ��false=����Ϊ��ֵ
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setNullable(boolean nullable) throws CacheException {
		checkSet();
		this.nullable = nullable;
	}

	/**
	 * ��ȡģ����, ������ֵ��У��ֵ
	 * @return
	 */
	public String getCondition() {
		return condition;
	}

	/**
	 * ����ģ����, ������ֵ��У��ֵ
	 * @param condition
	 * @throws CacheException
	 */
	public void setCondition(String condition) throws CacheException {
		checkSet();
		this.condition = condition;
	}

	/**
	 * ������Դģ��id
	 * 
	 * @param modelId
	 *            ��Դģ��id
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setModelId(String modelId) throws CacheException {
		checkSet();
		if (modelId == null)
			this.modelId = "";
		else
			this.modelId = modelId.toLowerCase().trim();
	}

	/**
	 * ��ȡ��Դģ��id
	 * 
	 * @return ��Դģ��id
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
		// �ڴ˲�������
		return null;
	}

	@Override
	public void dump() {
		System.out.println("ģ�����ԣ�modelId=" + this.modelId + ",attrId="
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
	 * ����ģ�����Ե�modelId
	 * 
	 * @author ÷԰
	 * 
	 */
	public static class ModelAttributeModelIdGetter implements
			ICompareKeyGetter<String, ModelAttribute> {

		public String getCompareKey(ModelAttribute obj) {
			return obj.getModelId();
		}
	}
}
