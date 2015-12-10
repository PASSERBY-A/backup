package com.cmsz.cloudplatform.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Repository;

import com.cmsz.cloudplatform.dao.WorkOrderDao;
import com.cmsz.cloudplatform.model.WorkItem;
import com.cmsz.cloudplatform.model.WorkOrder;
import com.cmsz.cloudplatform.model.request.ListWorkOrderReportRequest;
import com.cmsz.cloudplatform.model.request.ListWorkOrderRequest;
import com.hp.core.dao.hibernate.GenericDaoImpl;
import com.hp.util.StringUtil;

@Repository("orderDao")
public class WorkOrderDaoImpl extends GenericDaoImpl<WorkOrder, Long>  implements WorkOrderDao{

	public WorkOrderDaoImpl(Class<WorkOrder> clazz) {
		super(clazz);
	}

	public WorkOrderDaoImpl(){
		super(WorkOrder.class);
	}
	
	public List<WorkOrder> findWorkOrderList(final ListWorkOrderRequest example, final int pageSize, final int pageNumber){
			
		
			DetachedCriteria criteria = DetachedCriteria.forClass(WorkOrder.class);
			
			if(example.getId()!=null){
				criteria.add(Restrictions.eq("id", example.getId()));
			}
			//操作步骤
			if(example.getStep()!=null){
				criteria.createAlias("workItems", "item",JoinType.LEFT_OUTER_JOIN);
				criteria.add(Restrictions.eq("item.step", example.getStep()));
			}
			if(example.getStatus()!=null){
				
				criteria.add(Restrictions.eq("status", example.getStatus()));
			}
			
			if(example.getApplyUsers()!=null && example.getApplyUsers().length>0){
					criteria.add(Restrictions.in("createdBy", example.getApplyUsers()));
			}
			
			if(StringUtils.isNotBlank(example.getCreateBy())){
				criteria.add(Restrictions.like("createdBy", "%"+example.getCreateBy()+"%"));
			}
			
			if(example.getWorkOrderType()!=null){
				criteria.add(Restrictions.eq("workOrderType", example.getWorkOrderType()));
			}
			
			if(example.getStartDate()!=null){
				criteria.add(Restrictions.ge("createdOn",example.getStartDate()));
			}
			if(example.getEndDate()!=null){
				criteria.add(Restrictions.le("createdOn",example.getEndDate()));
			}
			criteria.addOrder(Order.desc("createdOn"));//降序排序
			List result =  (List<WorkOrder>)findByCriteria(criteria,pageSize, pageNumber);
			
			return result;
			
			
			
			
			/*List<Property> results = null;

			DetachedCriteria detachedCriteria = DetachedCriteria
					.forClass(Property.class);

			addLikeCriteria(detachedCriteria, "type", example.getType());
			addLikeCriteria(detachedCriteria, "vendor", example.getVendor());
			addLikeCriteria(detachedCriteria, "model", example.getModel());
			addLikeCriteria(detachedCriteria, "code", example.getCode());
			addLikeCriteria(detachedCriteria, "serial_num", example.getSerial_num());
			addLikeCriteria(detachedCriteria, "status", example.getStatus());
			addLikeCriteria(detachedCriteria, "owner", example.getOwner());

			// String belong = this.request.getParameter("belong_to");
			// String room = this.request.getParameter("room");
			// String machineCabinet = this.request.getParameter("machine_cabinet");
			//
			// addEqualQueryCriteria(detachedCriteria, "belong_to",belong);
			// addEqualQueryCriteria(detachedCriteria, "room", room);
			// addEqualQueryCriteria(detachedCriteria, "machine_cabinet",
			// machineCabinet);

			if (null != example.getStart_date()) {
				if (null != example.getEnd_date()) {
					detachedCriteria.add(Restrictions.between("start_date",
							example.getStart_date(), example.getEnd_date()));
				} else {
					detachedCriteria.add(Restrictions.gt("start_date",
							example.getStart_date()));
				}
			} else if (null != example.getEnd_date()) {
				detachedCriteria.add(Restrictions.lt("start_date",
						example.getEnd_date()));
			}

			results = (List<Property>) this.findByCriteria(detachedCriteria,
					pageNumber, pageSize);

			if (null == results) {
				results = Collections.emptyList();
			}

			return results;*/

	}
	
	@SuppressWarnings({ "unchecked" })
	public List<Object> findWorkOrderReport(ListWorkOrderReportRequest req) {
		if (req == null) {
			return null;
		}

		StringBuffer sbf = new StringBuffer();
		sbf.append("select to_char(t.created_on, 'yyyy-MM') ym,");
		sbf.append("   sum(decode(t.status, 1, 1, 0)) s1,");
		sbf.append("   sum(decode(t.status, 2, 1, 0)) s2,");
		sbf.append("   sum(decode(t.status, 3, 1, 0)) s3,");
		sbf.append("   sum(decode(t.status, 4, 1, 0)) s4,");
		sbf.append("   sum(decode(t.status, 5, 1, 0)) s5,");
		sbf.append("   sum(decode(t.status, 6, 1, 0)) s6,");
//		sbf.append("   sum(decode(t.status, 7, 1, 0)) s7,");
		sbf.append("   sum(1) total");
		sbf.append("  from T_WORKORDER t");
		if (req.getWorkOrderType() != null) {
			sbf.append(" where t.workorder_type = ").append(req.getWorkOrderType());
		}
		sbf.append(" group by to_char(t.created_on, 'yyyy-MM') having 1=1 ");
		if (StringUtil.isNotEmpty(req.getStartDate())) {
			sbf.append("and to_char(t.created_on, 'yyyy-MM') >= '").append(req.getStartDate()).append("'");
		}
		if (StringUtil.isNotEmpty(req.getEndDate())) {
			sbf.append("and to_char(t.created_on, 'yyyy-MM') <= '").append(req.getEndDate()).append("'");
		}
		sbf.append(" order by ym asc");

		return this.getSession().createSQLQuery(sbf.toString()).list();
		// return (List<WorkOrder>) this.findByCriteria(detachedCriteria);
	}
	
	
	public Integer countByExample(ListWorkOrderRequest example) {
		DetachedCriteria criteria = DetachedCriteria.forClass(WorkOrder.class);
		
		if(example.getStatus()!=null){
			
			criteria.add(Restrictions.eq("status", example.getStatus()));
		}
		
		
		if(example.getApplyUsers()!=null && example.getApplyUsers().length>0){
			criteria.add(Restrictions.in("createdBy", example.getApplyUsers()));
		}
		
		if(StringUtils.isNotBlank(example.getCreateBy())){
			criteria.add(Restrictions.like("createdBy", "%"+example.getCreateBy()+"%"));
		}
		
		if(example.getWorkOrderType()!=null){
			criteria.add(Restrictions.eq("workOrderType", example.getWorkOrderType()));
		}
		
		if(example.getStartDate()!=null){
			criteria.add(Restrictions.ge("createdOn",example.getStartDate()));
		}
		if(example.getEndDate()!=null){
			criteria.add(Restrictions.le("createdOn",example.getEndDate()));
		}
		return this.countByCriteria(criteria);
	}
	
	private void addLikeCriteria(DetachedCriteria detachedCriteria, String fieldName, String criteria){
		if (StringUtils.isNotBlank(criteria) && StringUtils.isNotBlank(criteria)) {				
			detachedCriteria.add(Restrictions.eq(fieldName ,  criteria));
		}
	}
	
	private void addEqCriteria(DetachedCriteria detachedCriteria, String fieldName, Integer criteria){
		if (criteria!=null) {				
			detachedCriteria.add(Restrictions.eq(fieldName ,  criteria));
		}
	}
	private void addListCriteria(DetachedCriteria detachedCriteria, String fieldName, List<Integer> list){
		if (list.size()>0) {				
			detachedCriteria.add(Restrictions.in(fieldName ,  list));
		}
	}

	
	@Override
	public WorkOrder checkStatus(int key,String name,String value) {
		List<Integer> l=new ArrayList<Integer>();
		l.add(WorkOrder.STATUS_WAITINGFORAPPROVAL);
		l.add(WorkOrder.STATUS_APPROVED);
		l.add(WorkOrder.STATUS_PROVISIONING);
		if(key==3){
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(WorkOrder.class);
			
			addLikeCriteria(detachedCriteria, "domainId",  value);
			addEqCriteria(detachedCriteria, "workOrderType", key);
			addListCriteria(detachedCriteria,"status",l);
			@SuppressWarnings("unchecked")
			List<WorkOrder> list= (List<WorkOrder>) this.findByCriteria(detachedCriteria);
			if(list.size()>0){
				return list.get(0);
			}
		}else{
			
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(WorkItem.class);
			
			addLikeCriteria(detachedCriteria, "attributeName",  name);
			addLikeCriteria(detachedCriteria, "attributeValue", value);
			@SuppressWarnings("unchecked")
			List<WorkItem> list= (List<WorkItem>) this.findByCriteria(detachedCriteria);
			if(list.size()>0){
				Long orderId=list.get(list.size()-1).getWorkOrderId();
				WorkOrder order=this.get(orderId);
				if(order.getWorkOrderType()==key){
					return  order;
				}else{
					return new WorkOrder();
				}
			}
		}
		
		/*StringBuilder sql1 = new StringBuilder("select * from WORKITEM ");
		if(StringUtils.isNotBlank(name)){
			sql1.append(" where  attributeName='").append(name).append("'");
		}
		if(StringUtils.isNotBlank(name)){
			sql1.append(" where  attributeName='").append(name).append("'");
		}
		Query query = this.getSession().createQuery(sql.toString());
		
		
		StringBuilder sql = new StringBuilder("from WorkOrder as a where a.workOrderType="+key);
		if(StringUtils.isNotBlank(name)){
			sql.append(" and a.id in (select max(id) from WorkItem  where  attributeName='").append(name).append("' and attributeValue='").append(value).append("')");
		}
		Query query = this.getSessionFactory().getCurrentSession().createQuery(sql.toString());
		WorkOrder order =(WorkOrder)query.list().get(0);
		*/
		return new WorkOrder();
	}
	
	
	
	public List<WorkOrder> listWorkOrderByTypeAndStatus(List<Integer> typeList,List<Integer> statusList){
		DetachedCriteria criteria = DetachedCriteria.forClass(WorkOrder.class);

		this.addListCriteria(criteria,"status",statusList);
	    this.addListCriteria(criteria, "workOrderType", typeList);
		
		@SuppressWarnings("unchecked")
		List<WorkOrder> list= (List<WorkOrder>) this.findByCriteria(criteria);

		return list;
		
		

}
}
