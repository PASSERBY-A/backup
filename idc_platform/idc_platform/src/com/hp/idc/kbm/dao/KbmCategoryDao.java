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
 * ֪ʶ���־ò�ӿ�
 * 
 * @author Fancy
 * @version 1.0, 9:55:54 PM Jul 14, 2011
 * 
 */
public interface KbmCategoryDao extends GenericDao<KbmCategory, Long> {

	/**
	 * ���ݲ�ѯ�����������������������б�
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
	 * ���ݲ�ѯ����������������������ҳ���������ط�ҳ�б�
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
	 * ��ѯ֪ʶ����List
	 * 
	 * @return
	 */
	public List<KbmCategory> loadCategoryList();

	public KbmCategory saveCategory(KbmCategory cate);
}
