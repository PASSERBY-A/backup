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
 * ֪ʶ����Ϣ�־ò�ӿ�
 * 
 * @author Fancy
 * @version 1.0, 9:55:54 PM Jul 14, 2011
 * 
 */
public interface KbmKnowledgeDao extends GenericDao<KbmKnowledge, Long> {

	/**
	 * ���ݱ�Ų���֪ʶ����ϢDao
	 * 
	 * @param id
	 * @return
	 */
	public KbmKnowledge findKnowledgeById(long id);

	/**
	 * ����֪ʶ����Ϣ
	 * 
	 * @param knowledge
	 * @return
	 */
	public KbmKnowledge saveKnowledge(KbmKnowledge knowledge);

	/**
	 * ����֪ʶ����id����֪ʶ��
	 * 
	 * @param parentId
	 * @return
	 */
	public List<KbmKnowledge> findKnowledgeByParentId(long parentId);

	/**
	 * ���ݲ�ѯ�����������������������б�
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
	public Page<KbmKnowledge> queryResultPage(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);
}
