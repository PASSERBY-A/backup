package com.cmsz.cloudplatform.dao.hibernate;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cmsz.cloudplatform.dao.TEquipmenteventDao;
import com.cmsz.cloudplatform.model.TEquipmentevent;
import com.cmsz.cloudplatform.model.request.ListTEquipmenteventRequest;
import com.hp.core.dao.hibernate.GenericDaoImpl;

@Repository("tEquipmenteventDao")
public class TEquipmenteventDaoImpl extends GenericDaoImpl<TEquipmentevent, Long>
		implements TEquipmenteventDao {
	public TEquipmenteventDaoImpl() {
		super(TEquipmentevent.class);
	}

	public TEquipmenteventDaoImpl(Class<TEquipmentevent> clazz) {
		super(clazz);
	}

	@Override
	public int removeUnProcessed(String serialNumber) {
		SQLQuery query = getSession()
				.createSQLQuery("DELETE FROM T_EQUIPMENTEVENT WHERE EVENTSTATUS = ? AND SERIALNUMBER = ?");
		query.setInteger(0, TEquipmentevent.EVENT_STATUS_PENDING);
		query.setString(1, serialNumber);
		return query.executeUpdate();
	}
	
	
	public List<TEquipmentevent> findList(ListTEquipmenteventRequest queryRequest, int pageSize, int pageNumber){
		
		DetachedCriteria criteria = DetachedCriteria.forClass(TEquipmentevent.class);
		if(StringUtils.isNotBlank(queryRequest.getSerialnumber())){
			criteria.add(Restrictions.ilike("serialnumber", queryRequest.getSerialnumber().trim(), MatchMode.ANYWHERE));
		}
		if(queryRequest.getEventstatus()!=null){
			criteria.add(Restrictions.eq("eventstatus", queryRequest.getEventstatus()));
		}
		
		criteria.addOrder(Order.desc("createdOn"));//降序排序
		List<TEquipmentevent> result =  (List<TEquipmentevent>)findByCriteria(criteria,pageNumber, pageSize);
		return result;
	}
	public Integer countByExample(ListTEquipmenteventRequest queryRequest){
		DetachedCriteria criteria = DetachedCriteria.forClass(TEquipmentevent.class);
		if(StringUtils.isNotBlank(queryRequest.getSerialnumber())){
			criteria.add(Restrictions.ilike("serialnumber", queryRequest.getSerialnumber().trim(), MatchMode.ANYWHERE));
		}

		if(queryRequest.getEventstatus()!=null){
			criteria.add(Restrictions.eq("eventstatus", queryRequest.getEventstatus()));
		}
		return this.countByCriteria(criteria);
	}

}
