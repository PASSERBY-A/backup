/**   
 * @Title: KnowledgeServiceImpl.java 
 * @Description: TODO
 * @date 2011-7-13 下午01:55:30   
 * @version 1.0  
 */
package com.hp.idc.kbm.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Service;

import com.hp.idc.common.Const;
import com.hp.idc.common.core.bo.Page;
import com.hp.idc.kbm.dao.KbmCategoryDao;
import com.hp.idc.kbm.dao.KbmKnowledgeDao;
import com.hp.idc.kbm.entity.KbmCategory;
import com.hp.idc.kbm.entity.KbmKnowledge;
import com.hp.idc.kbm.service.KnowledgeService;

@Service("knowledgeService")
public class KnowledgeServiceImpl implements KnowledgeService {

	@Resource
	private KbmCategoryDao kbmCategoryDao;

	@Resource
	private KbmKnowledgeDao kbmKnowledgeDao;

	/**
	 * 删除知识分类
	 * 
	 * @author Fancy
	 * @param id
	 * @return
	 */
	@Override
	public void removeCategoryById(long id) {
		try {
			kbmCategoryDao.remove(id);
		} catch(ObjectRetrievalFailureException ex){
			throw ex;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除知识点
	 * 
	 * @author Fancy
	 * @param id
	 * @return
	 */
	@Override
	public void removeKnowledgeById(long id) {
		KbmKnowledge knowledge = null;
		try {
			// 根据id编号,获取该实体
			knowledge = kbmKnowledgeDao.get(id);
			if (knowledge != null) {
				// 若为已废弃知识点，则物理删除
				if (knowledge.getIsRetired() == Const.KBM_ISRETIRED_YES) {
					kbmKnowledgeDao.remove(knowledge.getId());
				} else {
					// 置是否废弃=1
					knowledge.setIsRetired(Const.KBM_ISRETIRED_YES);
					kbmKnowledgeDao.save(knowledge);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据编号查找知识点信息
	 * 
	 * @author Fancy
	 * @param id
	 * @return
	 */
	@Override
	public KbmKnowledge findKnowledgeById(long id) {
		return kbmKnowledgeDao.findKnowledgeById(id);
	}

	/**
	 * 判断分类是否有对应明细
	 * 
	 * @author Fancy
	 * @param parentId
	 * @return
	 */
	@Override
	public boolean findKnowledgeByParentId(long parentId) {
		boolean flag = true;
		List<KbmKnowledge> list = kbmKnowledgeDao
				.findKnowledgeByParentId(parentId);
		if (list == null || list.size() < 1) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 保存知识类别
	 * 
	 * @param cate
	 */
	@Override
	public KbmCategory saveKbmCategory(KbmCategory cate) {
		return kbmCategoryDao.saveCategory(cate);
	}

	@Override
	public KbmKnowledge saveKnowledge(int baseType, KbmKnowledge knowledge) {

		if (baseType == Const.KBM_BASETYPE_EVENT) {
			knowledge.setDeviceName(null);
			knowledge.setProfessionType(null);
			knowledge.setSolutionContent(null);

		}
		// // 注入该知识点对应知识类别对象
		// KbmCategory cate = kbmCategoryDao
		// .get(knowledge.getCategoryId().getId());
		// knowledge.setCategoryId(cate);
		return kbmKnowledgeDao.saveKnowledge(knowledge);
	}

	/**
	 * 删除知识分类信息
	 * 
	 * @param id
	 */
	@Override
	public void deleteKbmCategory(long id) {

	}

	/**
	 * 保存知识信息(知识点和案例)
	 * 
	 * @param knowledge
	 */
	@Override
	public void saveKbmKnowledge(KbmKnowledge knowledge) {

	}

	/**
	 * 删除知识信息
	 * 
	 * @param id
	 */
	@Override
	public void deleteKbmKnowledge(long id) {

	}

	@Override
	public Page<KbmKnowledge> queryKnowledgePage(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize) {
		return kbmKnowledgeDao.queryResultPage(paramMap, sortMap, pageNo,
				pageSize);
	}

	@Override
	public List<KbmCategory> queryCategory(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap) {
		return kbmCategoryDao.queryResultList(paramMap, sortMap);
	}

	/**
	 * 
	 */
	@Override
	public List<KbmCategory> loadCategoryList() {

		return kbmCategoryDao.loadCategoryList();
	}

	// //////////////////////////////////////

	@Override
	public JSONArray queryCatalogTree(String id, String type) {
		if (type != null && !"".equals(type)) {

		}
		return null;
	}

	// ///set method//////
	public void setKbmCategoryDao(KbmCategoryDao kbmCategoryDao) {
		this.kbmCategoryDao = kbmCategoryDao;
	}

	public void setKbmKnowledgeDao(KbmKnowledgeDao kbmKnowledgeDao) {
		this.kbmKnowledgeDao = kbmKnowledgeDao;
	}

}
