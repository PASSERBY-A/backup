package com.hp.idc.business.dao.jpa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.hp.idc.business.dao.ServiceResourceDao;
import com.hp.idc.business.entity.ServiceResource;
import com.hp.idc.business.entity.ServiceResourcePK;
import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.jpa.GenericDaoJpa;
import com.hp.idc.common.util.StringUtil;
import com.hp.idc.resm.model.Model;
import com.hp.idc.resm.service.ServiceManager;
/**
 * 服务资源关联关系持久化类
 * 因为资源模型未使用hibernate维护，因此在联合查询时需要使用原生SQL
 * 请在本类实现的查询方法，而不要使用继承GenericDaoJpa的查询方法。
 * @author <a href="mailto:si-qi.liang@hp.com">Liang,Siqi</a>
 * @version 1.0, 上午10:07:58 2011-7-27
 *
 */
@Repository("serviceResourceDao")
public class ServiceResourceDaoJpa extends
		GenericDaoJpa<ServiceResource, ServiceResourcePK> implements
		ServiceResourceDao {
	
	public ServiceResourceDaoJpa(){
		super(ServiceResource.class);
	}
	
	public String sqlMaker(Map<String, Object> paramMap){
		StringBuffer whereSb=new StringBuffer();
		for(String key: paramMap.keySet()){
			if("amount".equals(key)){
				whereSb.append(" and o.amount=:amount");
			}
			if("serviceId".equals(key)){
				whereSb.append(" and o.service_id =:serviceId");
			}
			if("resModelId".equals(key)){
				whereSb.append(" and o.res_model_code=:resModelId ");
			}
			if("resName".equals(key)){
				whereSb.append(" and m.name like :resName escape '^'");
				String name=(String)paramMap.get(key);
				name=StringUtil.escapeLikeSql(name, "^");
				paramMap.put(key, "%"+name+"%");
			}
			if("resType".equals(key)){
				whereSb.append(" and m.type =:resType");
			}
		}
		return whereSb.toString();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ServiceResource> queryResultList(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap) {
		StringBuffer sb = new StringBuffer();
		sb.append("select o.* from business_service_resource o left join resm_model m on m.id=O.res_model_code where 1=1 ");
		sb.append(sqlMaker(paramMap));
		List<ServiceResource> list =new ArrayList<ServiceResource>();
		
		Query query=this.getEntityManager().
				createNativeQuery(sb.toString(), ServiceResource.class);
		setNamedParameters(query, paramMap);
		try{
			list=query.getResultList();
			//从资源缓存中加载资源模型
			for(ServiceResource sr:list){
				Model resModel=ServiceManager.getModelService().getModelById(sr.getId().getResModelId());
				sr.setResModel(resModel);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<ServiceResource> queryResultPage(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize) {
		String whereSql=sqlMaker(paramMap);
		StringBuffer sb = new StringBuffer();
		sb.append("select o.* from business_service_resource o left join resm_model m on m.id=O.res_model_code where 1=1  ");
		sb.append(whereSql);
		StringBuffer countSb=new StringBuffer();
		countSb.append("select count(*) from business_service_resource o left join resm_model m on m.id=O.res_model_code where 1=1 ");
		countSb.append(whereSql);
		
		EntityManager em = getEntityManager();
		Query countQuery=em.createNativeQuery(countSb.toString());
		setNamedParameters(countQuery, paramMap);
		int totalCount=0;
		try
    	{
			totalCount=((BigDecimal)countQuery.getSingleResult()).intValue();
    	}
    	catch(Exception e)
    	{
    		log.error("查询结果集总数出错！", e);
    	} 
    	if (totalCount < 1) 
			return new Page<ServiceResource>();
    	try{
    		if (pageNo < 1) {
				pageNo = 1;
			}
			int startIndex = Page.getStartOfAnyPage(pageNo, pageSize);
			Query q = em.createNativeQuery(sb.toString(), ServiceResource.class);
			setNamedParameters(q, paramMap);
			List<ServiceResource> list= q.setFirstResult(startIndex-1).setMaxResults(pageSize).getResultList();
			//从资源缓存中加载资源模型
			for(ServiceResource sr:list){
				Model resModel=ServiceManager.getModelService().getModelById(sr.getId().getResModelId());
				sr.setResModel(resModel);
			}
			int avaCount = (list == null) ? 0 : list.size();
			return new Page<ServiceResource>(startIndex, avaCount, totalCount, pageSize, list);
    	}catch(Exception e){
    		e.printStackTrace();
			return new Page<ServiceResource>();
    	}
	}

	

}
