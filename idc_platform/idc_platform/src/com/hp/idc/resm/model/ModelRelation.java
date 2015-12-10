package com.hp.idc.resm.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.hp.idc.resm.cache.CacheException;
import com.hp.idc.resm.cache.CacheableObject;
import com.hp.idc.resm.service.ServiceManager;

/**
 * 模型间的关系类型
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ModelRelation extends CacheableObject {

	/**
	 * 序列化UID
	 */
	private static final long serialVersionUID = -7429794653726364810L;

	/**
	 * id
	 */
	private int id;

	/**
	 * 模型id
	 */
	private String modelId = null;

	/**
	 * 关系id
	 */
	private String relationId = null;

	/**
	 * 关联的模型id
	 */
	private String modelId2 = null;

	/**
	 * 最多关联的个数
	 */
	private int num = -1;

	/**
	 * 模型名称，只是为了序列化使用的，此变量不设置值
	 */
	@SuppressWarnings("unused")
	private String modelName = null;

	/**
	 * 关联的模型名称，只是为了序列化使用的，此变量不设置值
	 */
	@SuppressWarnings("unused")
	private String modelName2 = null;
	
	/**
	 * 关联关系的名称，只是为了序列化使用的，此变量不设置值
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
	 * 设置模型名称，此方法无效，模型名称是从缓存中读取的
	 * 
	 * @param modelName
	 *            模型名称
	 */
	public void setModelName(String modelName) {
		// 空函数
	}

	/**
	 * 设置关联的模型名称，此方法无效，关联的模型名称是从缓存中读取的
	 * 
	 * @param modelName2
	 *            关联模型名称
	 */
	public void setModelName2(String modelName2) {
		// 空函数
	}

	@Override
	public String toString() {
		return "id=" + this.id + ",modelId=" + this.modelId + ",relationId="
				+ this.relationId + ",modelId2=" + this.modelId2;
	}

	/**
	 * 获取id
	 * 
	 * @return id
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
	 */
	public void setId(int id) throws CacheException {
		checkSet();
		this.id = id;
	}

	/**
	 * 获取最多关联的个数
	 * 
	 * @return 最多关联的个数
	 * @see #num
	 */
	public int getNum() {
		return this.num;
	}

	/**
	 * 设置最多关联的个数，-1表示不限制
	 * 
	 * @param num
	 *            最多关联的个数
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #num
	 */
	public void setNum(int num) throws CacheException {
		checkSet();
		this.num = num;
	}

	/**
	 * 获取资源模型id
	 * 
	 * @return 资源模型id
	 */
	public String getModelId() {
		return this.modelId;
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
		this.modelId = modelId;
	}

	/**
	 * 设置关联关系id
	 * 
	 * @return 关联关系id
	 */
	public String getRelationId() {
		return this.relationId;
	}

	/**
	 * 设置关联关系id
	 * 
	 * @param relationId
	 *            关联关系id
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setRelationId(String relationId) throws CacheException {
		checkSet();
		this.relationId = relationId;
	}

	/**
	 * 获取关联的资源模型id
	 * 
	 * @return 关联的资源模型id
	 */
	public String getModelId2() {
		return this.modelId2;
	}

	/**
	 * 设置关联的资源模型id
	 * 
	 * @param modelId2
	 *            关联的资源模型id
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setModelId2(String modelId2) throws CacheException {
		checkSet();
		this.modelId2 = modelId2;
	}

	/**
	 * 获取模型的名称
	 * 
	 * @return 模型的名称
	 */
	public String getModelName() {
		Model m = ServiceManager.getModelService().getModelById(this.modelId);
		if (m == null)
			return "?????";
		return m.getName();
	}

	/**
	 * 获取关联的模型的名称
	 * 
	 * @return 关联的型的名称
	 */
	public String getModelName2() {
		Model m = ServiceManager.getModelService().getModelById(this.modelId2);
		if (m == null)
			return "?????";
		return m.getName();
	}

	/**
	 * 获取关联关系的名称
	 * @return 关联关系名称
	 */
	public String getRelationName() {
		RelationDefine rd = ServiceManager.getRelationService().getRelationDefineById(this.relationId);
		if (rd == null)
			return "?????";
		return rd.getName();
	}

	/**
	 * 设置关联关系名称，此方法无效，关联关系的名称是从缓存中读取的
	 * @param relationName
	 */
	public void setRelationName(String relationName) {
		// 空函数
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
