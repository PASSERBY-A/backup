package com.hp.idc.resm.cache;

import java.sql.ResultSet;
import java.util.List;

import org.apache.log4j.Logger;

import com.hp.idc.resm.model.Model;
import com.hp.idc.resm.resource.ResourceObject;
import com.hp.idc.resm.service.ServiceManager;

/**
 * ��Դ���󻺴�
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ResourceObjectCache extends CacheBase<ResourceObject> {
	/**
	 * ������־����
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
				logger.error("�Ƿ���ģ��id: " + modelId);
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
		return "��Դ����";
	}

	/**
	 * ��������Դ�в�����ָ������ֵ�Ķ���
	 * 
	 * @param attrId
	 *            ����id
	 * @param attrValue
	 *            ����ֵ
	 * @return �����������б�
	 */
	public List<ResourceObject> findInGlobal(String attrId, String attrValue) {
		ResourceObjectCacheStore store = (ResourceObjectCacheStore) this
				.getCacheStore();
		return store.findInGlobal(attrId, attrValue);
	}

	/**
	 * ��ָ��ģ���в�����ָ������ֵ�Ķ���
	 * 
	 * @param modelId
	 *            ģ��id
	 * @param attrId
	 *            ����id
	 * @param attrValue
	 *            ����ֵ
	 * @return �����������б�
	 */
	public List<ResourceObject> findInModel(String modelId, String attrId,
			String attrValue) {
		ResourceObjectCacheStore store = (ResourceObjectCacheStore) this
				.getCacheStore();
		return store.findInModel(modelId, attrId, attrValue);
	}
}
