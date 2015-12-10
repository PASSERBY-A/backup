package com.cmsz.cloudplatform.dao.hibernate;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cmsz.cloudplatform.dao.ResourcePoolPermissionDao;
import com.cmsz.cloudplatform.model.ResourcePoolPermission;
import com.hp.core.dao.hibernate.GenericDaoImpl;

@Repository("resourcePoolPermissionDao")
public class ResourcePoolPermissionDaoImpl extends GenericDaoImpl<ResourcePoolPermission, Long> implements ResourcePoolPermissionDao {

	public ResourcePoolPermissionDaoImpl(Class<ResourcePoolPermission> persistentClass) {
		super(persistentClass);
	}

	public ResourcePoolPermissionDaoImpl() {
		super(ResourcePoolPermission.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ResourcePoolPermission> getResourcePoolPermissions(Integer ownerType, List<String> ownerIds) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ResourcePoolPermission.class);
		detachedCriteria.add(Restrictions.eq("ownerType", ownerType));
		if (CollectionUtils.isNotEmpty(ownerIds)) {
			detachedCriteria.add(Restrictions.in("ownerId", ownerIds));
		}
		
		return (List<ResourcePoolPermission>) this.findByCriteria(detachedCriteria);
	}
	public ResourcePoolPermission getPool(Integer type,String value){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ResourcePoolPermission.class);
		detachedCriteria.add(Restrictions.eq("ownerType", type));
		detachedCriteria.add(Restrictions.eq("ownerId", value));
		
		List<ResourcePoolPermission> list = (List<ResourcePoolPermission>) this.findByCriteria(detachedCriteria);
		if(list!=null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
}
