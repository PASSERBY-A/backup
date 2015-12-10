package com.cmsz.cloudplatform.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.cmsz.cloudplatform.dao.WorkItemDao;
import com.cmsz.cloudplatform.model.WorkItem;
import com.hp.core.dao.hibernate.GenericDaoImpl;

@Repository("itemDao")
public class WorkItemDaoImpl extends GenericDaoImpl<WorkItem, Long>  implements WorkItemDao{

	public WorkItemDaoImpl(Class<WorkItem> clazz) {
		super(clazz);
	}

	public WorkItemDaoImpl(){
		super(WorkItem.class);
	}
}
