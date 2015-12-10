/**
 * KbmKnowledgeDao.java
 * @author fancy
 * @date 2011-7-14
 */
package com.hp.idc.kbm.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.GenericDao;
import com.hp.idc.kbm.entity.KbmKnowledge;

/**
 * 知识点信息持久层接口
 * 
 * @author Fancy
 * @version 1.0, 9:55:54 PM Jul 14, 2011
 * 
 */
public interface KbmKnowledgeDao extends GenericDao<KbmKnowledge, Long> {

	/**
	 * 根据编号查找知识点信息Dao
	 * 
	 * @param id
	 * @return
	 */
	public KbmKnowledge findKnowledgeById(long id);

	/**
	 * 保存知识点信息
	 * 
	 * @param knowledge
	 * @return
	 */
	public KbmKnowledge saveKnowledge(KbmKnowledge knowledge);

	/**
	 * 根据知识分类id查找知识点
	 * 
	 * @param parentId
	 * @return
	 */
	public List<KbmKnowledge> findKnowledgeByParentId(long parentId);

	/**
	 * 根据查询条件集和排序条件集返回列表
	 * 
	 * @Title:queryResultList
	 * @Desciption:TODO
	 * @param paramMap
	 * @param sortMap
	 * @return
	 */
	public List<KbmKnowledge> queryResultList(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap);

	/**
	 * 根据查询条件集、排序条件集、分页条件，返回分页列表
	 * 
	 * @Title:queryResultPage
	 * @Desciption:TODO
	 * @param paramMap
	 * @param sortMap
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page<KbmKnowledge> queryResultPage(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);
}
