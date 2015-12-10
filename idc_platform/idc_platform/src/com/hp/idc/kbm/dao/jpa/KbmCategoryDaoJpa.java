/**
 * KbmCategoryDaoJpa.java
 * @author fancy
 * @date 2011-7-14
 */
package com.hp.idc.kbm.dao.jpa;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.jpa.GenericDaoJpa;
import com.hp.idc.kbm.dao.KbmCategoryDao;
import com.hp.idc.kbm.entity.KbmCategory;

/**
 * 知识类别持久层实现类
 * 
 * @author Fancy
 * @version 1.0, 9:55:54 PM Jul 14, 2011
 * 
 */
@Repository("kbmCategoryDao")
public class KbmCategoryDaoJpa extends GenericDaoJpa<KbmCategory, Long>
		implements KbmCategoryDao {

	public KbmCategoryDaoJpa() {
		super(KbmCategory.class);
	}

	/**
	 * 获取知识分类列表
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<KbmCategory> loadCategoryList() {

		Query query = getEntityManager().createQuery(
				"select o from KbmCategory o where 1=1 order by o.id");
		List<KbmCategory> list = query.getResultList();
		return list;
	}

	@Override
	public List<KbmCategory> queryResultList(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap) {
		return this.queryResultList(new JsqlBuilder() {
			public String buildJSql(Map<String, Object> paramMap) {
				StringBuffer sb = new StringBuffer();
				sb.append("select o from KbmCategory o where 1=1");
				for (String key : paramMap.keySet()) {
					if (key.equals("id")) {
						sb.append(" and o.id =:id");
					}
					if (key.equals("categoryName")) {
						sb.append(" and o.categoryName like :categoryName");
						paramMap.put(key, "%" + (String) paramMap.get(key)
								+ "%");
					}
					if (key.equals("parentCategoryId")) {
						sb.append(" and o.parentCategoryId =:parentCategoryId");
					}
				}
				return sb.toString();
			}
		}, paramMap, sortMap);
	}

	@Override
	public Page<KbmCategory> queryResultPage(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize) {
		return this.queryResultPage(new SqlBuilder() {
			public String buildJSql(Map<String, Object> paramMap) {
				StringBuffer sb = new StringBuffer();
				sb.append("select o from KbmCategory o where 1=1");
				for (String key : paramMap.keySet()) {
					if (key.equals("id")) {
						sb.append(" and o.id =:id");
					}
					if (key.equals("categoryName")) {
						sb.append(" and o.categoryName like :categoryName");
						paramMap.put(key, "%" + (String) paramMap.get(key)
								+ "%");
					}
					if (key.equals("parentCategoryId")) {
						sb.append(" and o.parentCategoryId =:parentCategoryId");
					}
				}
				return sb.toString();
			}

			public String buildCountSql(Map<String, Object> paramMap) {
				StringBuffer sb = new StringBuffer();
				sb.append("select count(o.id) from KbmCategory o where 1=1");
				for (String key : paramMap.keySet()) {
					if (key.equals("id")) {
						sb.append(" and o.id =:id");
					}
					if (key.equals("categoryName")) {
						sb.append(" and o.categoryName like :categoryName");
						paramMap.put(key, "%" + (String) paramMap.get(key)
								+ "%");
					}
					if (key.equals("parentCategoryId")) {
						sb.append(" and o.parentCategoryId =:parentCategoryId");
					}
				}
				return sb.toString();
			}
		}, paramMap, sortMap, pageNo, pageSize);
	}

	@Override
	public KbmCategory saveCategory(KbmCategory cate) {
		KbmCategory category = this.save(cate);
		return category;
	}
}
