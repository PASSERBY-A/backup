package com.hp.idc.resm.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.apache.log4j.Logger;

import com.hp.idc.resm.model.AttributeDefine;
import com.hp.idc.resm.resource.ResourceObject;
import com.hp.idc.resm.service.ServiceManager;
import com.hp.idc.resm.util.ICompareKeyGetter;
import com.hp.idc.resm.util.SortedArrayList;
import com.hp.idc.resm.util.StringCompareHandler;
import com.hp.idc.resm.util.StringUtil;

/**
 * 资源对象缓存库
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ResourceObjectCacheStore extends
		UniqueIndexedCacheStore<ResourceObject> {
	/**
	 * log4j日志
	 */
	private static Logger logger = Logger
			.getLogger(ResourceObjectCacheStore.class);
	
	/**
	 * 生成资源对象的modelId
	 * 
	 * @author 梅园
	 * 
	 */
	public static class ResourceObjectModelIdGetter implements
			ICompareKeyGetter<String, ResourceObject> {

		public String getCompareKey(ResourceObject obj) {
			return obj.getModelId();
		}

	}
	
	/**
	 * 读写锁
	 */
	protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	/**
	 * 读锁
	 */
	protected ReadLock rlock = this.lock.readLock();

	/**
	 * 写锁
	 */
	protected WriteLock wlock = this.lock.writeLock();

	/**
	 * 模型id索引
	 */
	public SortedArrayList<String, ResourceObject> modelIdIndex = null;

	/**
	 * 对属性的索引
	 */
	public List<ResourceIndex> attrIndexs = null;

	@Override
	public void initIndex() {
		ResourceObjectModelIdGetter getter = new ResourceObjectModelIdGetter();
		// modelId 没有中文，所以不用中文比较，提高效率
		StringCompareHandler compare = new StringCompareHandler();

		List<ResourceObject> all = this.data.values();
		this.modelIdIndex = new SortedArrayList<String, ResourceObject>(getter,
				compare, all);

		this.attrIndexs = new ArrayList<ResourceIndex>();
		List<AttributeDefine> attrs = ServiceManager.getAttributeService()
				.getAllAttributes();
		for (AttributeDefine a : attrs) {
			if (a.isGlobalUnique()) {
				logger.info("创建资源对象的全局索引：" + a.getId());
				ResourceIndex ri = new ResourceIndex(a.getId(), 0, all);
				this.attrIndexs.add(ri);
			} else if (a.isModelUnique()) {
				logger.info("创建资源对象的模型索引：" + a.getId());
				ResourceIndex ri = new ResourceIndex(a.getId(), 1, all);
				this.attrIndexs.add(ri);
			}
		}
	}

	/**
	 * 获取modelId=key的列表
	 * 
	 * @param key
	 *            参数
	 * @return 满足条件的列表
	 */
	public List<ResourceObject> getListByModelId(String key) {
		this.rlock.lock();
		List<ResourceObject> r = this.modelIdIndex.getE(key);
		this.rlock.unlock();
		return r;
	}

	@Override
	public ResourceObject put(ResourceObject obj) {
		this.wlock.lock();
		ResourceObject m = super.put(obj);
		if (m != null)
			this.modelIdIndex.remove(m);
		this.modelIdIndex.add(obj);
		for (ResourceIndex r : this.attrIndexs) {
			if (m != null)
				r.remove(m);
			r.add(obj);
		}
		this.wlock.unlock();
		return m;
	}

	@Override
	public ResourceObject remove(String key) {
		this.wlock.lock();
		ResourceObject m = super.remove(key);
		if (m != null)
			this.modelIdIndex.remove(m);
		for (ResourceIndex r : this.attrIndexs) {
			if (m != null)
				r.remove(m);
		}
		this.wlock.unlock();
		return m;
	}

	@Override
	public void clear() {
		this.wlock.lock();
		super.clear();
		this.modelIdIndex.clear();
		for (ResourceIndex r : this.attrIndexs)
			r.clear();
		this.wlock.unlock();
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
		List<ResourceObject> l;
		this.rlock.lock();

		// 从索引中找
		for (ResourceIndex r : this.attrIndexs) {
			if (r.getAttrId().equals(attrId) && r.getType() == 0) {
				l = r.getE(attrValue);
				this.rlock.unlock();
				return l;
			}
		}

		// 字段不是索引
		List<ResourceObject> all = this.values();
		l = new ArrayList<ResourceObject>();
		for (ResourceObject r : all) {
			if (StringUtil.compare(attrValue, r.getAttributeValue(attrId)) == 0)
				l.add(r);
		}
		this.rlock.unlock();
		return l;
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
		List<ResourceObject> l;
		this.rlock.lock();

		// 从索引中找
		for (ResourceIndex r : this.attrIndexs) {
			if (r.getAttrId().equals(attrId) && r.getType() == 1) {
				l = r.getE(modelId + "." + attrValue);
				this.rlock.unlock();
				return l;
			}
		}

		// 字段不是索引
		List<ResourceObject> all = getListByModelId(modelId);
		l = new ArrayList<ResourceObject>();
		for (ResourceObject r : all) {
			if (StringUtil.compare(attrValue, r.getAttributeValue(attrId)) == 0)
				l.add(r);
		}
		this.rlock.unlock();
		return l;
	}
}
