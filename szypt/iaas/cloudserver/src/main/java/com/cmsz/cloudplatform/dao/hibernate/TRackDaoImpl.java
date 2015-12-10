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

import com.cmsz.cloudplatform.dao.TBayinfoDao;
import com.cmsz.cloudplatform.dao.TRackDao;
import com.cmsz.cloudplatform.model.THost;
import com.cmsz.cloudplatform.model.TRack;
import com.cmsz.cloudplatform.model.request.ListTRackRequest;
import com.hp.core.dao.hibernate.GenericDaoImpl;

@Repository("tRackDao")
public class TRackDaoImpl extends GenericDaoImpl<TRack, Long>
		implements TRackDao {
	
	@Autowired
	private TBayinfoDao tBayinfoDao;
	
	public TRackDaoImpl() {
		super(TRack.class);
	}

	public TRackDaoImpl(Class<TRack> clazz) {
		super(clazz);
	}

	@Override
	public TRack getTRackBySeriralNumber(String serialNumber) {
		SQLQuery query = this.getSession().createSQLQuery("SELECT * FROM T_RACK t WHERE t.status <> ? and t.serialNumber = ?");
		query.addEntity(TRack.class);
		query.setInteger(0, THost.STATUS_DELETE);
		query.setString(1, serialNumber);
		
		return (TRack)query.uniqueResult();
	}

	@Override
	public TRack getTRackByIp(String ip) {
		SQLQuery query = this.getSession().createSQLQuery("SELECT * FROM T_RACK t WHERE t.status <> ? and t.oaip = ?"); // hostname 主机名称或IP
		query.addEntity(TRack.class);
		query.setInteger(0, TRack.STATUS_DELETE);
		query.setString(1, ip);
		
		return (TRack)query.uniqueResult();
	}

	@Override
	public List<TRack> findTRackList(ListTRackRequest request, int pageSize,
			int page) {

		DetachedCriteria criteria = DetachedCriteria.forClass(TRack.class);
		
		if(request.getId()!=null){
			criteria.add(Restrictions.eq("id", request.getId()));
		}
		
		if(StringUtils.isNotBlank(request.getName())){
			criteria.add(Restrictions.ilike("name",request.getName().trim(), MatchMode.ANYWHERE));
		}
		
		if(request.getSaveStatus()!=null){
			criteria.add(Restrictions.eq("saveStatus",request.getSaveStatus()));
		}
		
		if(request.getStatus()!=null){
			criteria.add(Restrictions.eq("status",request.getStatus()));
		}
		
		criteria.addOrder(Order.desc("createdOn"));//降序排序
		List<TRack> result =  (List<TRack>)findByCriteria(criteria,page,pageSize);
		for(TRack h : result){
			h.setBayinfos(tBayinfoDao.getAllByRackId(h.getId()));
		}
		return result;
	
	}

	@Override
	public Integer countByExample(ListTRackRequest request) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(TRack.class);
		
		if(request.getId()!=null){
			criteria.add(Restrictions.eq("id", request.getId()));
		}
		
		if(StringUtils.isNotBlank(request.getName())){
			criteria.add(Restrictions.ilike("name",request.getName().trim(), MatchMode.ANYWHERE));
		}
		
		if(request.getSaveStatus()!=null){
			criteria.add(Restrictions.eq("saveStatus",request.getSaveStatus()));
		}
		
		if(request.getStatus()!=null){
			criteria.add(Restrictions.eq("status",request.getStatus()));
		}
		
		
		
		return this.countByCriteria(criteria);
	}

}
