package com.hp.idc.resm.cache;

import java.util.List;

import com.hp.idc.resm.model.ModelAttribute;
import com.hp.idc.resm.model.ModelAttribute.ModelAttributeModelIdGetter;
import com.hp.idc.resm.util.SortedArrayList;
import com.hp.idc.resm.util.StringCompareHandler;

/**
 * ��Դģ�����ԵĻ���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ModelAttributeCacheStore extends
		UniqueIndexedCacheStore<ModelAttribute> {

	/**
	 * ����ģ��id����
	 */
	public SortedArrayList<String, ModelAttribute> modelIdIndex = null;

	@Override
	public void initIndex() {
		ModelAttributeModelIdGetter getter = new ModelAttributeModelIdGetter();
		// modelId û�����ģ����Բ������ıȽϣ����Ч��
		StringCompareHandler compare = new StringCompareHandler();
		this.modelIdIndex = new SortedArrayList<String, ModelAttribute>(getter,
				compare, this.data.values());
	}

	/**
	 * ��ȡmodelId=key���б�
	 * 
	 * @param key
	 *            ����
	 * @return �����������б�
	 */
	public List<ModelAttribute> getListByModelId(String key) {
		return this.modelIdIndex.getE(key);
	}

	@Override
	public ModelAttribute put(ModelAttribute obj) {
		ModelAttribute m = this.data.put(obj);
		if (m != null)
			this.modelIdIndex.remove(m);
		this.modelIdIndex.add(obj);
		return m;
	}

	@Override
	public ModelAttribute remove(String key) {
		ModelAttribute m = this.data.remove(key);
		if (m != null)
			this.modelIdIndex.remove(m);
		return m;
	}

	@Override
	public void clear() {
		super.clear();
		this.modelIdIndex.clear();
	}
}
