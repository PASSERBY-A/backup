package com.hp.idc.kbm.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.kbm.entity.KbmCategory;
import com.hp.idc.kbm.entity.KbmKnowledge;

/**
 * 知识库管理业务逻辑层接口
 * 
 * @ClassName: KnowledgeService
 * @Descprition: TODO
 * @author Fancy
 * @version 1.0
 */
public interface KnowledgeService {

	/**
	 * 根据编号查找知识点信息
	 * 
	 * @param id
	 * @return
	 */
	public KbmKnowledge findKnowledgeById(long id);

	/**
	 * 保存知识点信息
	 * 
	 * @param baseType
	 *            知识类型
	 * @param knowledge
	 * @return
	 */
	public KbmKnowledge saveKnowledge(int baseType, KbmKnowledge knowledge);

	/**
	 * 判断分类是否有对应明细
	 * 
	 * @author Fancy
	 * @param parentId
	 * @return
	 */
	public boolean findKnowledgeByParentId(long parentId);

	/**
	 * 保存知识类别
	 * 
	 * @param cate
	 */
	public KbmCategory saveKbmCategory(KbmCategory cate);

	/**
	 * 删除知识分类信息
	 * 
	 * @param id
	 */
	public void deleteKbmCategory(long id);

	/**
	 * 保存知识信息(知识点和案例)
	 * 
	 * @param knowledge
	 */
	public void saveKbmKnowledge(KbmKnowledge knowledge);

	/**
	 * 删除知识分类
	 * 
	 * @param id
	 */
	public void removeCategoryById(long id);

	/**
	 * 删除知识点
	 * 
	 * @author Fancy
	 * @param id
	 */
	public void removeKnowledgeById(long id);

	/**
	 * 删除知识信息
	 * 
	 * @param id
	 */
	public void deleteKbmKnowledge(long id);

	public Page<KbmKnowledge> queryKnowledgePage(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);

	public List<KbmCategory> queryCategory(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap);

	/**
	 * 获取知识分类Tree
	 * 
	 * @param
	 */
	public List<KbmCategory> loadCategoryList();

	public JSONArray queryCatalogTree(String id, String type);
}