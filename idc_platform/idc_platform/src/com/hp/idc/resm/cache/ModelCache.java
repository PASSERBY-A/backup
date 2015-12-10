package com.hp.idc.resm.cache;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.hp.idc.resm.model.Model;

/**
 * ��Դģ�ͻ���
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ModelCache extends CacheBase<Model> {

	/**
	 * ��ȡģ����(��Flexʹ��)
	 * 
	 * @return ���ṹXML�ı�
	 */
	public String getModelTree() {

		this.rlock.lock();

		// ��ȡ���ڵ㣬���Ӹ��ڵ㿪ʼ�ݹ���������XML
		List<Model> list = getChildModelsById(Model.ROOT_PARENT_ID);
		StringBuilder sb = new StringBuilder();
		sb.append("<treeRoot>\n");
		for (int i = 0; i < list.size(); i++) {
			getModelTree(sb, list.get(i));
		}
		sb.append("</treeRoot>\n");
		this.rlock.unlock();
		return sb.toString();
	}

	/**
	 * �ڲ�������getModelTreeר��
	 * 
	 * @param sb
	 *            ����ƴ���ַ���
	 * @param m
	 *            ��Դģ��
	 */
	private void getModelTree(StringBuilder sb, Model m) {
		sb.append("<node label=\"").append(m.getName()).append("\" id=\"")
				.append(m.getId()).append("\" icon=\"").append(m.getIcon()).append("\" enable=\"").append(m.isEnabled())
				.append("\">\n");
		List<Model> list = getChildModelsById(m.getId());
		if (list != null) {
			for (int i = 0; i < list.size(); i++)
				getModelTree(sb, list.get(i));
		}
		sb.append("</node>\n");
	}

	/**
	 * ��ȡָ��ģ�͵���ģ���б�
	 * 
	 * @param id
	 *            ָ��ģ�͵�id
	 * @return ��ģ���б�
	 */
	public List<Model> getChildModelsById(String id) {
		ModelCacheStore store = (ModelCacheStore) this.cacheStore;
		return store.getChildList(id);
	}

	/**
	 * ��ȡָ��ģ�͵���ģ���б�
	 * 
	 * @param id
	 *            ָ��ģ�͵�id
	 * @param recursive
	 *            �ݹ��ѯ�����Ӽ�
	 * @return ��ģ���б�
	 */
	public List<Model> getChildModelsById(String id, boolean recursive) {
		ModelCacheStore store = (ModelCacheStore) this.cacheStore;
		List<Model> list = store.getChildList(id);
		if (recursive) {
			// ����һ����ջ��ʵ�ֵݹ�
			List<Model> l = new ArrayList<Model>();
			l.addAll(list);
			while (l.size() > 0) {
				Model m = l.remove(l.size() - 1);
				List<Model> l2 = store.getChildList(m.getId());
				list.addAll(l2);
				l.addAll(l2);
			}
		}
		return list;
	}

	@Override
	protected CacheStore<Model> createStore() {
		return new ModelCacheStore();
	}

	@Override
	protected Model createNewObject(ResultSet rs) throws Exception {
		Model m = new Model();
		if (rs != null)
			m.readFromResultSet(rs);
		return m;
	}

	@Override
	public String getCacheName() {
		return "ģ�Ͷ���";
	}
}
