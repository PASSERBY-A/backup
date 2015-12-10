package com.hp.idc.bulletin.dao.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hp.idc.bulletin.dao.BulletinInfoDao;
import com.hp.idc.bulletin.entity.BulletinInfo;
import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.jpa.GenericDaoJpa;
import com.hp.idc.common.util.StringUtil;


@Repository("bulletinInfoDao")
public class BulletinInfoDaoJpa extends GenericDaoJpa<BulletinInfo, Long>
		implements BulletinInfoDao {

	public BulletinInfoDaoJpa() {
		super(BulletinInfo.class);
	}
	private String where;
	public String sqlMaker(Map<String, Object> paramMap){
		StringBuffer whereSb=new StringBuffer();
		for(String key: paramMap.keySet()){
			if("title".equals(key)){
				whereSb.append(" and o.title like :title escape '^'");
				String title=(String)paramMap.get(key);
				title=StringUtil.escapeLikeSql(title, "^");
				paramMap.put(key, "%"+title+"%");
			}
			if("beginTimeBefore".equals(key)){
				whereSb.append(" and beginTime <=:beginTimeBefore ");
			}
			if("endTimeAfter".equals(key)){
				whereSb.append(" and endTime >=:endTimeAfter ");
			}
			if("today".equals(key)){
				whereSb.append("  and beginTime <:today and endTime >=:today");
			}
		}
		return whereSb.toString();
	}

	public Page<BulletinInfo> getBulletinList(
			Map<String,Object> paramMap,LinkedHashMap<String,String> sortMap,int pageNo, int pageSize) {
		where=sqlMaker(paramMap);
		return this.queryResultPage(new SqlBuilder() {
			public String buildJSql(Map<String, Object> paramMap) {
				StringBuffer sb = new StringBuffer();
				sb.append("select o from BulletinInfo o where 1=1");
				sb.append(where);
				return sb.toString();
			}
			public String buildCountSql(Map<String, Object> paramMap) {
				StringBuffer sb = new StringBuffer();
				sb.append("select count(o.id) from BulletinInfo o  where 1=1");
				sb.append(where);
				return sb.toString();
			}
		}, paramMap, sortMap, pageNo, pageSize);
	}

}
