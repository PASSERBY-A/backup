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
 * ��Դ���󻺴��
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ResourceObjectCacheStore extends
		UniqueIndexedCacheStore<ResourceObject> {
	/**
	 * log4j��־
	 */
	private static Logger logger = Logger
			.getLogger(ResourceObjectCacheStore.class);
	
	/**
	 * ������Դ�����modelId
	 * 
	 * @author ÷԰
	 * 
	 */
	public static class ResourceObjectModelIdGetter implements
			ICompareKeyGetter<String, ResourceObject> {

		public String getCompareKey(ResourceObject obj) {
			return obj.getModelId();
		}

	}
	
	/**
	 * ��д��
	 */
	protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	/**
	 * ����
	 */
	protected ReadLock rlock = this.lock.readLock();

	/**
	 * д��
	 */
	protected WriteLock wlock = this.lock.writeLock();

	/**
	 * ģ��id����
	 */
	public SortedArrayList<String, ResourceObject> modelIdIndex = null;

	/**
	 * �����Ե�����
	 */
	public List<ResourceIndex> attrIndexs = null;

	@Override
	public void initIndex() {
		ResourceObjectModelIdGetter getter = new ResourceObjectModelIdGetter();
		// modelId û�����ģ����Բ������ıȽϣ����Ч��
		StringCompareHandler compare = new StringCompareHandler();

		List<ResourceObject> all = this.data.values();
		this.modelIdIndex = new SortedArrayList<String, ResourceObject>(getter,
				compare, all);

		this.attrIndexs = new ArrayList<ResourceIndex>();
		List<AttributeDefine> attrs = ServiceManager.getAttributeService()
				.getAllAttributes();
		for (AttributeDefine a : attrs) {
			if (a.isGlobalUnique()) {
				logger.info("������Դ�����ȫ��������" + a.getId());
				ResourceIndex ri = new ResourceIndex(a.getId(), 0, all);
				this.attrIndexs.add(ri);
			} else if (a.isModelUnique()) {
				logger.info("������Դ�����ģ��������" + a.getId());
				ResourceIndex ri = new ResourceIndex(a.getId(), 1, all);
				this.attrIndexs.add(ri);
			}
		}
	}

	/**
	 * ��ȡmodelId=key���б�
	 * 
	 * @param key
	 *            ����
	 * @return �����������б�
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
	 * ��������Դ�в�����ָ������ֵ�Ķ���
	 * 
	 * @param attrId
	 *            ����id
	 * @param attrValue
	 *            ����ֵ
	 * @return �����������б�
	 */
	public List<ResourceObject> findInGlobal(String attrId, String attrValue) {
		List<ResourceObject> l;
		this.rlock.lock();

		// ����������
		for (ResourceIndex r : this.attrIndexs) {
			if (r.getAttrId().equals(attrId) && r.getType() == 0) {
				l = r.getE(attrValue);
				this.rlock.unlock();
				return l;
			}
		}

		// �ֶβ�������
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
		List<ResourceObject> l;
		this.rlock.lock();

		// ����������
		for (ResourceIndex r : this.attrIndexs) {
			if (r.getAttrId().equals(attrId) && r.getType() == 1) {
				l = r.getE(modelId + "." + attrValue);
				this.rlock.unlock();
				return l;
			}
		}

		// �ֶβ�������
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
