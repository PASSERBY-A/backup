package com.cmsz.cloudplatform.dao;

import java.util.List;


import com.cmsz.cloudplatform.model.Property;
import com.hp.core.dao.GenericDao;

public interface PropertyDao extends GenericDao<Property,Long> {
    List<Property> findPropertyListByExample(final Property example, final int pageSize, final int pageNumber);
}
