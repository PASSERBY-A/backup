package com.hp.idc.resm.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.hp.idc.resm.cache.CacheException;
import com.hp.idc.resm.cache.CacheableObject;
import com.hp.idc.resm.service.ServiceManager;

/**
 * ģ�ͼ�Ĺ�ϵ����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ModelRelation extends CacheableObject {

	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = -7429794653726364810L;

	/**
	 * id
	 */
	private int id;

	/**
	 * ģ��id
	 */
	private String modelId = null;

	/**
	 * ��ϵid
	 */
	private String relationId = null;

	/**
	 * ������ģ��id
	 */
	private String modelId2 = null;

	/**
	 * �������ĸ���
	 */
	private int num = -1;

	/**
	 * ģ�����ƣ�ֻ��Ϊ�����л�ʹ�õģ��˱���������ֵ
	 */
	@SuppressWarnings("unused")
	private String modelName = null;

	/**
	 * ������ģ�����ƣ�ֻ��Ϊ�����л�ʹ�õģ��˱���������ֵ
	 */
	@SuppressWarnings("unused")
	private String modelName2 = null;
	
	/**
	 * ������ϵ�����ƣ�ֻ��Ϊ�����л�ʹ�õģ��˱���������ֵ
	 */
	@SuppressWarnings("unused")
	private String relationName = null;

	@Override
	public Map<String, String> getLogExtendInfo() {
		Map<String, String> m = new HashMap<String, String>();
		m.put("objectid1", this.modelId);
		m.put("id", "" + this.id);
		m.put("objectid2", this.modelId2);
		m.put("relationid", this.relationId);
		return m;
	}

	/**
	 * ����ģ�����ƣ��˷�����Ч��ģ�������Ǵӻ����ж�ȡ��
	 * 
	 * @param modelName
	 *            ģ������
	 */
	public void setModelName(String modelName) {
		// �պ���
	}

	/**
	 * ���ù�����ģ�����ƣ��˷�����Ч��������ģ�������Ǵӻ����ж�ȡ��
	 * 
	 * @param modelName2
	 *            ����ģ������
	 */
	public void setModelName2(String modelName2) {
		// �պ���
	}

	@Override
	public String toString() {
		return "id=" + this.id + ",modelId=" + this.modelId + ",relationId="
				+ this.relationId + ",modelId2=" + this.modelId2;
	}

	/**
	 * ��ȡid
	 * 
	 * @return id
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
	 */
	public void setId(int id) throws CacheException {
		checkSet();
		this.id = id;
	}

	/**
	 * ��ȡ�������ĸ���
	 * 
	 * @return �������ĸ���
	 * @see #num
	 */
	public int getNum() {
		return this.num;
	}

	/**
	 * �����������ĸ�����-1��ʾ������
	 * 
	 * @param num
	 *            �������ĸ���
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @see #num
	 */
	public void setNum(int num) throws CacheException {
		checkSet();
		this.num = num;
	}

	/**
	 * ��ȡ��Դģ��id
	 * 
	 * @return ��Դģ��id
	 */
	public String getModelId() {
		return this.modelId;
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
		this.modelId = modelId;
	}

	/**
	 * ���ù�����ϵid
	 * 
	 * @return ������ϵid
	 */
	public String getRelationId() {
		return this.relationId;
	}

	/**
	 * ���ù�����ϵid
	 * 
	 * @param relationId
	 *            ������ϵid
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setRelationId(String relationId) throws CacheException {
		checkSet();
		this.relationId = relationId;
	}

	/**
	 * ��ȡ��������Դģ��id
	 * 
	 * @return ��������Դģ��id
	 */
	public String getModelId2() {
		return this.modelId2;
	}

	/**
	 * ���ù�������Դģ��id
	 * 
	 * @param modelId2
	 *            ��������Դģ��id
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setModelId2(String modelId2) throws CacheException {
		checkSet();
		this.modelId2 = modelId2;
	}

	/**
	 * ��ȡģ�͵�����
	 * 
	 * @return ģ�͵�����
	 */
	public String getModelName() {
		Model m = ServiceManager.getModelService().getModelById(this.modelId);
		if (m == null)
			return "?????";
		return m.getName();
	}

	/**
	 * ��ȡ������ģ�͵�����
	 * 
	 * @return �������͵�����
	 */
	public String getModelName2() {
		Model m = ServiceManager.getModelService().getModelById(this.modelId2);
		if (m == null)
			return "?????";
		return m.getName();
	}

	/**
	 * ��ȡ������ϵ������
	 * @return ������ϵ����
	 */
	public String getRelationName() {
		RelationDefine rd = ServiceManager.getRelationService().getRelationDefineById(this.relationId);
		if (rd == null)
			return "?????";
		return rd.getName();
	}

	/**
	 * ���ù�����ϵ���ƣ��˷�����Ч��������ϵ�������Ǵӻ����ж�ȡ��
	 * @param relationName
	 */
	public void setRelationName(String relationName) {
		// �պ���
	}

	@Override
	public String getPrimaryKey() {
		return "" + getId();
	}

	@Override
	public String getDatabaseTable() {
		return "resm_model_relation";
	}

	@Override
	public String getPrimaryKeyField() {
		return "id";
	}

	@Override
	public void updateResultSet(Connection conn, ResultSet rs) throws Exception {
		rs.updateInt("id", this.id);
		rs.updateString("modelid", this.modelId);
		rs.updateString("modelid2", this.modelId2);
		rs.updateString("relationid", this.relationId);
		rs.updateInt("num", this.num);
	}

	@Override
	public void readFromResultSet(ResultSet rs) throws Exception {
		setId(rs.getInt("id"));
		setModelId(rs.getString("modelid"));
		setRelationId(rs.getString("relationid"));
		setModelId2(rs.getString("modelid2"));
		setNum(rs.getInt("num"));
	}
}
