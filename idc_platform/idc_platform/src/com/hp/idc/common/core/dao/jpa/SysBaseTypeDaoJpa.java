package com.hp.idc.common.core.dao.jpa;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.SysBaseTypeDao;
import com.hp.idc.common.core.entity.SysBaseType;
import com.hp.idc.common.core.entity.SysBaseTypePK;
/**
 * 
 * @author <a href="mailto:si-qi.liang@hp.com">Liang,Siqi</a>
 * @version 1.0, ÏÂÎç05:01:11 2011-8-1
 *
 */
@Repository("sysBaseTypeDao")
public class SysBaseTypeDaoJpa extends
		GenericDaoJpa<SysBaseType, SysBaseTypePK> implements SysBaseTypeDao {

	
	public SysBaseTypeDaoJpa(){
		super(SysBaseType.class);
	}

	private String sqlMaker(Map<String, Object> paramMap){
		StringBuffer whereBuffer=new StringBuffer();
		for(String key : paramMap.keySet()){
			if("id".equals(key)){
				whereBuffer.append(" and o.id.id=:id");
			}
			if("type".equals(key)){
				whereBuffer.append(" and o.id.type=:type");
			}
			if("name".equals(key)){
				whereBuffer.append(" and o.name like :type");
				paramMap.put(key, "%"+(String)paramMap.get(key)+"%");
			}
			if("nls".equals(key)){
				whereBuffer.append(" and o.nls like :type");
				paramMap.put(key, "%"+(String)paramMap.get(key)+"%");
			}
		}
		return whereBuffer.toString();
	}
	
	@Override
	public List<SysBaseType> queryResultList(
			Map<String, Object> paramMap, LinkedHashMap<String, String> sortMap) {
		return super.queryResultList(new JsqlBuilder() {
			public String buildJSql(Map<String, Object> paramMap) {
				StringBuffer sb = new StringBuffer();
				sb.append("select o from SysBaseType o where 1=1");
				sb.append(sqlMaker(paramMap));
				return sb.toString();
			}}, paramMap, sortMap);
	}
	@Override
	public Page<SysBaseType> queryResultPage(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize) {
		return this.queryResultPage(new SqlBuilder() {
			public String buildJSql(Map<String, Object> paramMap) {
				StringBuffer sb = new StringBuffer();
				sb.append("select o from SysBaseType o where 1=1");
				sb.append(sqlMaker(paramMap));
				return sb.toString();
			}

			public String buildCountSql(Map<String, Object> paramMap) {
				StringBuffer sb = new StringBuffer();
				sb.append("select count(o.id.id) from SysBaseType o where 1=1");
				sb.append(sqlMaker(paramMap));
				return sb.toString();
			}
		}, paramMap, sortMap, pageNo, pageSize);
	}

}
