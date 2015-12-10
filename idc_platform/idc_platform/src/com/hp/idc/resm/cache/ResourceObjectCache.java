package com.hp.idc.resm.cache;

import java.sql.ResultSet;
import java.util.List;

import org.apache.log4j.Logger;

import com.hp.idc.resm.model.Model;
import com.hp.idc.resm.resource.ResourceObject;
import com.hp.idc.resm.service.ServiceManager;

/**
 * 资源对象缓存
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ResourceObjectCache extends CacheBase<ResourceObject> {
	/**
	 * 定义日志变量
	 */
	private static Logger logger = Logger.getLogger(ResourceObjectCache.class);

	@Override
	protected CacheStore<ResourceObject> createStore() {
		return new ResourceObjectCacheStore();
	}
	
	@Override
	public List<ResourceObject> getAll() {
		ResourceObjectCacheStore store = (ResourceObjectCacheStore)this.getCacheStore();
		return store.modelIdIndex.values();
	}

	@Override
	protected ResourceObject createNewObject(ResultSet rs) throws Exception {
		ResourceObject r;
		if (rs != null) {
			String modelId = rs.getString("modelid");
			Model m = ServiceManager.getModelService().getModelById(modelId);
			if (m == null) {
				logger.error("非法的模型id: " + modelId);
				return null;
			}
			r = m.createObject();

			r.readFromResultSet(rs);
		} else {
			r = new ResourceObject();
		}
		return r;
	}

	/*
	@Override
	protected String getInitSql() {
		return "select id, modelid, enabled, to_char(data) data from resm_item";
	}*/

	@Override
	public String getCacheName() {
		return "资源对象";
	}

	/**
	 * 从所有资源中查找有指定属性值的对象
	 * 
	 * @param attrId
	 *            属性id
	 * @param attrValue
	 *            属性值
	 * @return 满足条件的列表
	 */
	public List<ResourceObject> findInGlobal(String attrId, String attrValue) {
		ResourceObjectCacheStore store = (ResourceObjectCacheStore) this
				.getCacheStore();
		return store.findInGlobal(attrId, attrValue);
	}

	/**
	 * 从指定模型中查找有指定属性值的对象
	 * 
	 * @param modelId
	 *            模型id
	 * @param attrId
	 *            属性id
	 * @param attrValue
	 *            属性值
	 * @return 满足条件的列表
	 */
	public List<ResourceObject> findInModel(String modelId, String attrId,
			String attrValue) {
		ResourceObjectCacheStore store = (ResourceObjectCacheStore) this
				.getCacheStore();
		return store.findInModel(modelId, attrId, attrValue);
	}
}
