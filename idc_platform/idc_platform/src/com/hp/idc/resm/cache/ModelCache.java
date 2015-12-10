package com.hp.idc.resm.cache;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.hp.idc.resm.model.Model;

/**
 * 资源模型缓存
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ModelCache extends CacheBase<Model> {

	/**
	 * 获取模型树(给Flex使用)
	 * 
	 * @return 树结构XML文本
	 */
	public String getModelTree() {

		this.rlock.lock();

		// 获取根节点，并从根节点开始递归生成树的XML
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
	 * 内部函数，getModelTree专用
	 * 
	 * @param sb
	 *            用于拼接字符吕
	 * @param m
	 *            资源模型
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
	 * 获取指定模型的子模型列表
	 * 
	 * @param id
	 *            指定模型的id
	 * @return 子模型列表
	 */
	public List<Model> getChildModelsById(String id) {
		ModelCacheStore store = (ModelCacheStore) this.cacheStore;
		return store.getChildList(id);
	}

	/**
	 * 获取指定模型的子模型列表
	 * 
	 * @param id
	 *            指定模型的id
	 * @param recursive
	 *            递归查询所有子级
	 * @return 子模型列表
	 */
	public List<Model> getChildModelsById(String id, boolean recursive) {
		ModelCacheStore store = (ModelCacheStore) this.cacheStore;
		List<Model> list = store.getChildList(id);
		if (recursive) {
			// 设置一个堆栈，实现递归
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
		return "模型定义";
	}
}
