/**
 * KbmCategoryDao.java
 * @author fancy
 * @date 2011-7-14
 */
package com.hp.idc.kbm.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.GenericDao;
import com.hp.idc.kbm.entity.KbmCategory;

/**
 * 知识类别持久层接口
 * 
 * @author Fancy
 * @version 1.0, 9:55:54 PM Jul 14, 2011
 * 
 */
public interface KbmCategoryDao extends GenericDao<KbmCategory, Long> {

	/**
	 * 根据查询条件集和排序条件集返回列表
	 * 
	 * @Title:queryResultList
	 * @Desciption:TODO
	 * @param paramMap
	 * @param sortMap
	 * @return
	 */
	public List<KbmCategory> queryResultList(Map<String, Object> paramMap,
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
	public Page<KbmCategory> queryResultPage(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);

	/**
	 * 查询知识分类List
	 * 
	 * @return
	 */
	public List<KbmCategory> loadCategoryList();

	public KbmCategory saveCategory(KbmCategory cate);
}
