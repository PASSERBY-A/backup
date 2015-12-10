package com.hp.idc.kbm.dao.jpa;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hp.idc.common.Const;
import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.jpa.GenericDaoJpa;
import com.hp.idc.kbm.dao.KbmKnowledgeDao;
import com.hp.idc.kbm.entity.KbmKnowledge;

/**
 * 
 * 
 * 知识信息DAO实现
 * 
 * @author Fancy
 * @version 1.0, 2:16:13 PM Jul 18, 2011
 * 
 */
@Repository("kbmKnowledgeDao")
public class KbmKnowledgeDaoJpa extends GenericDaoJpa<KbmKnowledge, Long>
		implements KbmKnowledgeDao {
	public KbmKnowledgeDaoJpa() {
		super(KbmKnowledge.class);
	}

	/**
	 * 根据编号查找知识点信息Dao实现
	 * 
	 * @return
	 */
	@Override
	public KbmKnowledge findKnowledgeById(long id) {
		KbmKnowledge ret = null;
		try {
			ret = this.get(id);
		} catch (Exception e) {
			log.debug(e);
		}
		return ret;
	}

	/**
	 * 保存知识点信息
	 */
	@Override
	public KbmKnowledge saveKnowledge(KbmKnowledge knowledge) {
		KbmKnowledge ret = null;
		try {
			ret = this.save(knowledge);
		} catch (Exception e) {
			log.debug(e);
		}
		return ret;
	}

	/**
	 * 根据知识分类id查找知识点
	 */
	@Override
	public List<KbmKnowledge> findKnowledgeByParentId(long parentId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		LinkedHashMap<String, String> sortMap = new LinkedHashMap<String, String>();
		StringBuffer sb = new StringBuffer();
		List<KbmKnowledge> list = null;
		try {
			sb.append("select o from KbmKnowledge o left join FETCH o.categoryId e where 1=1");
			sb.append(" and o.categoryId.id = :categoryId ");
			// 组装查询参数
			paramMap.put("categoryId", parentId);

			list = this.queryResultList(sb.toString(), paramMap, sortMap);
		} catch (Exception e) {
			log.debug(e);
		}

		return list;
	}

	/**
	 * 知识信息列表分页查询
	 */
	@Override
	public Page<KbmKnowledge> queryResultPage(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize) {
		StringBuffer sb = new StringBuffer();
		StringBuffer countSb = new StringBuffer();

		sb.append("select o from KbmKnowledge o left join FETCH o.categoryId e where 1=1");
		countSb.append("select count(o.id) from KbmKnowledge o join o.categoryId e where 1=1");

		for (String key : paramMap.keySet()) {
			if (paramMap.get(key) != null) {
				if (key.equals("categoryId")) {
					sb.append(" and o.categoryId.id = :categoryId");
					countSb.append(" and o.categoryId.id = :categoryId");
					paramMap.put(key, paramMap.get(key));
				}
				if(key.equals("cateId")){
					sb.append(" and o.categoryId.baseType = :cateId");
					countSb.append(" and o.categoryId.baseType = :cateId");
					paramMap.put(key, paramMap.get(key));
				}
				if (key.equals("isRetired")) {
					sb.append(" and o.isRetired = :isRetired");
					countSb.append(" and o.isRetired = :isRetired");
					paramMap.put(key, paramMap.get(key));
				}
				if(key.equals("id")){
					sb.append(" and o.id = :id");
					countSb.append(" and o.id = :id");
				}
				if(key.equals("title")){
					sb.append(" and o.title like :title");
					countSb.append(" and o.title like :title");
					paramMap.put(key,"%"+ (String)paramMap.get(key)+"%");
				}
				if (key.equals("keywords")) {
					String keywords = (String) paramMap.get(key);
					String[] keywordArr = keywords.split(",");
					if (keywordArr.length > 0) {
						StringBuffer kb = new StringBuffer();
						for (String keyword : keywordArr) {
							keyword = keyword.trim();
							if (!"".equals(kb.toString()))
								kb.append(" or ");
							kb.append(" o.keywords like :keywords");
							paramMap.put(key, "%" + keyword + "%");
						}
						// 组装查询列表和计数sql
						sb.append(" and ( ").append(kb).append(" )");
						countSb.append(" and ( ").append(kb).append(" )");
					}
				}
			}
		}
		return this.queryResultPage(sb.toString(), countSb.toString(),
				paramMap, sortMap, pageNo, pageSize);
	}

	@Override
	public List<KbmKnowledge> queryResultList(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap) {
		return this.queryResultList(new JsqlBuilder() {
			public String buildJSql(Map<String, Object> paramMap) {
				StringBuffer sb = new StringBuffer();
				sb.append("select o from KbmKnowledge o left join FETCH o.categoryId e where 1=1");
				for (String key : paramMap.keySet()) {
					if (key.equals("id")) {
						sb.append(" and o.id =:id");
						paramMap.put(key, (Long) paramMap.get(key));
					}
					if (key.equals("title")) {
						sb.append(" and o.title like :title");
						paramMap.put(key, "%" + (String) paramMap.get(key)
								+ "%");
					}
					if (key.equals("description")) {
						sb.append(" and o.description like :description");
						paramMap.put(key, "%" + (String) paramMap.get(key)
								+ "%");
					}
					if (key.equals("solution")) {
						sb.append(" and o.solution like :solution");
						paramMap.put(key, "%" + (String) paramMap.get(key)
								+ "%");
					}
					if (key.equals("keywords")) {
						String keywords = (String) paramMap.get(key);
						String[] keywordArr = keywords.split(",");
						if (keywordArr.length > 0) {
							StringBuffer kb = new StringBuffer();
							for (String keyword : keywordArr) {
								keyword = keyword.trim();
								if (!"".equals(kb.toString()))
									kb.append(" or ");
								kb.append(" o.keywords like '%" + keywords
										+ "%'");
							}
							sb.append(" and ( ").append(kb).append(" )");
						}
					}
					if (key.equals("categoryId")) {
						sb.append(" and o.categoryId =:categoryId");
					}
					if (key.equals("isRetired")) {
						sb.append(" and o.isRetired =:isRetired");
					}
				}
				return sb.toString();
			}
		}, paramMap, sortMap);
	}

}
