package com.hp.idc.resm.cache;

import java.util.List;

import com.hp.idc.resm.model.Model;
import com.hp.idc.resm.model.Model.ModelParentIdGetter;
import com.hp.idc.resm.util.SortedArrayList;
import com.hp.idc.resm.util.StringCompareHandler;

/**
 * 存储模型缓存的库
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ModelCacheStore extends UniqueIndexedCacheStore<Model> {

	/**
	 * 父模型索引
	 */
	public SortedArrayList<String, Model> parentIndex = null;

	@Override
	public void initIndex() {
		ModelParentIdGetter getter = new ModelParentIdGetter();
		// parentId 没有中文，所以不用中文比较，提高效率
		StringCompareHandler compare = new StringCompareHandler();
		this.parentIndex = new SortedArrayList<String, Model>(getter, compare,
				this.data.values());
	}

	/**
	 * 获取parentId=key的列表
	 * 
	 * @param key
	 *            参数
	 * @return 满足条件的列表
	 */
	public List<Model> getChildList(String key) {
		return this.parentIndex.getE(key);
	}

	@Override
	public Model put(Model obj) {
		Model m = this.data.put(obj);
		if (m != null)
			this.parentIndex.remove(m);
		this.parentIndex.add(obj);
		return m;
	}

	@Override
	public Model remove(String key) {
		Model m = this.data.remove(key);
		if (m != null)
			this.parentIndex.remove(m);
		return m;
	}

	@Override
	public void clear() {
		super.clear();
		this.parentIndex.clear();
	}

}
