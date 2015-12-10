package com.cmsz.cloudplatform.dao.hibernate;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cmsz.cloudplatform.dao.THbaDao;
import com.cmsz.cloudplatform.dao.THostDao;
import com.cmsz.cloudplatform.model.THost;
import com.cmsz.cloudplatform.model.request.ListTHostRequest;
import com.hp.core.dao.hibernate.GenericDaoImpl;

@Repository("tHostDao")
public class THostDaoImpl extends GenericDaoImpl<THost, Long>
		implements THostDao {
	
	
	@Autowired private THbaDao tHbaDao = null;
	public THostDaoImpl() {
		super(THost.class);
	}

	public THostDaoImpl(Class<THost> clazz) {
		super(clazz);
	}

	@Override
	public THost getTHostBySeriralNumber(String serialNumber) {
		SQLQuery query = this.getSession().createSQLQuery("SELECT * FROM T_HOST t WHERE t.status <> ? and t.serialNumber = ?");
		query.addEntity(THost.class);
		query.setInteger(0, THost.STATUS_DELETE);
		query.setString(1, serialNumber);
		
		
		
		return (THost)query.uniqueResult();
	}

	@Override
	public THost getTHostBySeriralNumber(String serialNumber, Integer type) {
		SQLQuery query = this.getSession().createSQLQuery("SELECT * FROM T_HOST t WHERE t.status <> ? and t.serialNumber = ? and t.type = ?");
		query.addEntity(THost.class);
		query.setInteger(0, THost.STATUS_DELETE);
		query.setString(1, serialNumber);
		query.setInteger(2, type);
		
		return (THost)query.uniqueResult();
	}

	@Override
	public THost getTHostByIp(String ip) {
		SQLQuery query = this.getSession().createSQLQuery("SELECT * FROM T_HOST t WHERE t.status <> ? and t.hostname = ?"); // hostname 主机名称或IP
		query.addEntity(THost.class);
		query.setInteger(0, THost.STATUS_DELETE);
		query.setString(1, ip);
		
		return (THost)query.uniqueResult();
	}

	@Override
	public List<THost> findTHostList(ListTHostRequest queryRequest,
			int pageSize, int pageNumber) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(THost.class);
		
		if(StringUtils.isNotBlank(queryRequest.getHostName())){
			criteria.add(Restrictions.ilike("hostname", queryRequest.getHostName().trim(), MatchMode.ANYWHERE));
		}
		
		
		if(StringUtils.isNotBlank(queryRequest.getResourcePoolId())){
			criteria.add(Restrictions.eq("resourcepoolid", queryRequest.getResourcePoolId().trim()));
		}
		
		if(queryRequest.getSaveStatus()!=null){
			criteria.add(Restrictions.eq("saveStatus",queryRequest.getSaveStatus()));
		}
		
		if(queryRequest.getStatus()!=null){
			criteria.add(Restrictions.eq("status",queryRequest.getStatus()));
		}
		
		criteria.add(Restrictions.ne("status",THost.STATUS_DELETE));

		if(StringUtils.isNotBlank(queryRequest.getType())){
			String[] temps = queryRequest.getType().split(",");
			Integer[] types = new Integer[temps.length];
			for(int i=0;i<types.length;++i){
				if(StringUtils.isNotBlank(temps[i])){
					types[i] = Integer.valueOf(temps[i]);
				}
			}
			criteria.add(Restrictions.in("type", types));
		}
		
		criteria.addOrder(Order.desc("createdOn"));//降序排序
		List<THost> result =  (List<THost>)findByCriteria(criteria,pageNumber,pageSize);
		
		for(THost h : result){
			h.setHbaList(tHbaDao.listBySN(h.getSerialnumber()));
		}
		return result;
	}

	@Override
	public Integer countByExample(ListTHostRequest queryRequest) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(THost.class);
		
		if(StringUtils.isNotBlank(queryRequest.getHostName())){
			criteria.add(Restrictions.ilike("hostname", queryRequest.getHostName().trim(), MatchMode.ANYWHERE));
		}
		
		
		if(StringUtils.isNotBlank(queryRequest.getResourcePoolId())){
			criteria.add(Restrictions.eq("resourcepoolid", queryRequest.getResourcePoolId().trim()));
		}
		
		if(queryRequest.getSaveStatus()!=null){
			criteria.add(Restrictions.eq("saveStatus",queryRequest.getSaveStatus()));
		}
		
		
		if(queryRequest.getStatus()!=null){
			criteria.add(Restrictions.eq("status",queryRequest.getStatus()));
		}
		
		criteria.add(Restrictions.ne("status",THost.STATUS_DELETE));
		if(StringUtils.isNotBlank(queryRequest.getType())){
			String[] temps = queryRequest.getType().split(","); 
			Integer[] types = new Integer[temps.length];
			for(int i=0;i<types.length;++i){
				if(StringUtils.isNotBlank(temps[i])){
					types[i] = Integer.valueOf(temps[i]);
				}
			}
			criteria.add(Restrictions.in("type", types));
		}
		
		
		return this.countByCriteria(criteria);
	}
}
