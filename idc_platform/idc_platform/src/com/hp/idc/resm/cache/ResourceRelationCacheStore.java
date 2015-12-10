package com.hp.idc.resm.cache;

import java.util.List;

import com.hp.idc.resm.resource.ResourceRelation;
import com.hp.idc.resm.resource.ResourceRelation.ResourceRelationItemId2Getter;
import com.hp.idc.resm.resource.ResourceRelation.ResourceRelationItemIdGetter;
import com.hp.idc.resm.util.IntegerCompareHandler;
import com.hp.idc.resm.util.SortedArrayList;

/**
 * 资源模型属性的缓存
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ResourceRelationCacheStore extends
		UniqueIndexedCacheStore<ResourceRelation> {

	/**
	 * 对象id索引
	 */
	public SortedArrayList<Integer, ResourceRelation> resIdIndex = null;
	/**
	 * 关联对象id索引
	 */
	public SortedArrayList<Integer, ResourceRelation> resId2Index = null;

	@Override
	public void initIndex() {
		ResourceRelationItemIdGetter getter = new ResourceRelationItemIdGetter();
		ResourceRelationItemId2Getter getter2 = new ResourceRelationItemId2Getter();
		IntegerCompareHandler compare = new IntegerCompareHandler();
		this.resIdIndex = new SortedArrayList<Integer, ResourceRelation>(
				getter, compare, this.data.values());
		this.resId2Index = new SortedArrayList<Integer, ResourceRelation>(
				getter2, compare, this.data.values());
	}

	/**
	 * 获取资源对象的关联关系
	 * 
	 * @param id
	 *            资源对象id
	 * @return 资源对象的关联关系
	 */
	public List<ResourceRelation> getRelationsByResourceId(int id) {
		List<ResourceRelation> list = this.resId2Index.getE(id);
		list.addAll(this.resIdIndex.getE(id));
		return list;
	}

	@Override
	public ResourceRelation put(ResourceRelation obj) {
		ResourceRelation r = super.put(obj);
		if (r != null) {
			this.resIdIndex.remove(r);
			this.resId2Index.remove(r);
		}
		this.resIdIndex.add(obj);
		this.resId2Index.add(obj);
		return r;
	}

	@Override
	public ResourceRelation remove(String key) {
		ResourceRelation r = super.remove(key);
		if (r != null) {
			this.resIdIndex.remove(r);
			this.resId2Index.remove(r);
		}
		return r;
	}

	@Override
	public void clear() {
		super.clear();
		this.resIdIndex.clear();
		this.resId2Index.clear();
	}
}
