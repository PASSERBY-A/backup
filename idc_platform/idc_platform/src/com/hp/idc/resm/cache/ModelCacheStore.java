package com.hp.idc.resm.cache;

import java.util.List;

import com.hp.idc.resm.model.Model;
import com.hp.idc.resm.model.Model.ModelParentIdGetter;
import com.hp.idc.resm.util.SortedArrayList;
import com.hp.idc.resm.util.StringCompareHandler;

/**
 * �洢ģ�ͻ���Ŀ�
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ModelCacheStore extends UniqueIndexedCacheStore<Model> {

	/**
	 * ��ģ������
	 */
	public SortedArrayList<String, Model> parentIndex = null;

	@Override
	public void initIndex() {
		ModelParentIdGetter getter = new ModelParentIdGetter();
		// parentId û�����ģ����Բ������ıȽϣ����Ч��
		StringCompareHandler compare = new StringCompareHandler();
		this.parentIndex = new SortedArrayList<String, Model>(getter, compare,
				this.data.values());
	}

	/**
	 * ��ȡparentId=key���б�
	 * 
	 * @param key
	 *            ����
	 * @return �����������б�
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
