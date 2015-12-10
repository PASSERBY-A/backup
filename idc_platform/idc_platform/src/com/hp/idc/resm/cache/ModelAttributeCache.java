package com.hp.idc.resm.cache;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.hp.idc.resm.model.ModelAttribute;

/**
 * ��Դģ�����ԵĻ���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ModelAttributeCache extends CacheBase<ModelAttribute> {

	/**
	 * ��ȡָ����Դģ���У��ɼ̳е������б�<br/>
	 * ע���Ӹ�ģ�ͼ̳е����Բ��������ڴ��б��С�
	 * 
	 * @param id
	 *            ��Դģ��id
	 * @return �ɼ̳е������б�
	 */
	public List<ModelAttribute> getInheritableAttributesById(String id) {
		List<ModelAttribute> list = new ArrayList<ModelAttribute>();
		List<ModelAttribute> l0 = ((ModelAttributeCacheStore) this.cacheStore)
				.getListByModelId(id);
		for (ModelAttribute a : l0) {
			if (a.isInheritable())
				list.add(a);
		}
		return list;
	}

	/**
	 * ��ȡָ����Դģ���е������б�<br/>
	 * ע���Ӹ�ģ�ͼ̳е����Բ��������ڴ��б��С�
	 * 
	 * @param id
	 *            ��Դģ��id
	 * @return �����б�
	 */
	public List<ModelAttribute> getAttributesById(String id) {
		List<ModelAttribute> list = ((ModelAttributeCacheStore) this.cacheStore)
				.getListByModelId(id);
		return list;
	}

	@Override
	protected ModelAttribute createNewObject(ResultSet rs) throws Exception {
		ModelAttribute m = new ModelAttribute();
		if (rs != null)
			m.readFromResultSet(rs);
		return m;
	}

	@Override
	public String getCacheName() {
		return "ģ������";
	}

	@Override
	protected CacheStore<ModelAttribute> createStore() {
		return new ModelAttributeCacheStore();
	}
}
