package com.hp.idc.kbm.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.kbm.entity.KbmCategory;
import com.hp.idc.kbm.entity.KbmKnowledge;

/**
 * ֪ʶ�����ҵ���߼���ӿ�
 * 
 * @ClassName: KnowledgeService
 * @Descprition: TODO
 * @author Fancy
 * @version 1.0
 */
public interface KnowledgeService {

	/**
	 * ���ݱ�Ų���֪ʶ����Ϣ
	 * 
	 * @param id
	 * @return
	 */
	public KbmKnowledge findKnowledgeById(long id);

	/**
	 * ����֪ʶ����Ϣ
	 * 
	 * @param baseType
	 *            ֪ʶ����
	 * @param knowledge
	 * @return
	 */
	public KbmKnowledge saveKnowledge(int baseType, KbmKnowledge knowledge);

	/**
	 * �жϷ����Ƿ��ж�Ӧ��ϸ
	 * 
	 * @author Fancy
	 * @param parentId
	 * @return
	 */
	public boolean findKnowledgeByParentId(long parentId);

	/**
	 * ����֪ʶ���
	 * 
	 * @param cate
	 */
	public KbmCategory saveKbmCategory(KbmCategory cate);

	/**
	 * ɾ��֪ʶ������Ϣ
	 * 
	 * @param id
	 */
	public void deleteKbmCategory(long id);

	/**
	 * ����֪ʶ��Ϣ(֪ʶ��Ͱ���)
	 * 
	 * @param knowledge
	 */
	public void saveKbmKnowledge(KbmKnowledge knowledge);

	/**
	 * ɾ��֪ʶ����
	 * 
	 * @param id
	 */
	public void removeCategoryById(long id);

	/**
	 * ɾ��֪ʶ��
	 * 
	 * @author Fancy
	 * @param id
	 */
	public void removeKnowledgeById(long id);

	/**
	 * ɾ��֪ʶ��Ϣ
	 * 
	 * @param id
	 */
	public void deleteKbmKnowledge(long id);

	public Page<KbmKnowledge> queryKnowledgePage(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);

	public List<KbmCategory> queryCategory(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap);

	/**
	 * ��ȡ֪ʶ����Tree
	 * 
	 * @param
	 */
	public List<KbmCategory> loadCategoryList();

	public JSONArray queryCatalogTree(String id, String type);
}