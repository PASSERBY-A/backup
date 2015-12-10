package com.cmsz.cloudplatform.dao;

import java.util.List;

import com.cmsz.cloudplatform.model.ProvisionAttribute;
import com.hp.core.dao.GenericDao;

public interface AttributeDao extends GenericDao<ProvisionAttribute,Long> {

	/**
	 * @author Li Manxin
	 * @param example
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	 List<ProvisionAttribute> findWorkOrderListByExample(final ProvisionAttribute example);
}
