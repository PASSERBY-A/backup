package com.cmsz.cloudplatform.dao.hibernate;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.cmsz.cloudplatform.dao.DimResourceDao;
import com.cmsz.cloudplatform.model.DimResource;
import com.hp.core.dao.hibernate.GenericDaoImpl;

@Repository("dimResourceDao")
public class DimResourceDaoImpl extends GenericDaoImpl<DimResource, Long> implements DimResourceDao {
	public DimResourceDaoImpl(){
		super(DimResource.class);
	}
	
	
	public DimResourceDaoImpl(Class<DimResource> clazz) {
		super(clazz);
	}


	@Override
	public void deleteAll() {
		Query query = this.getSession().createSQLQuery("DELETE FROM T_DIM_RESOURCE");
		query.executeUpdate();
	}
	
}
