package com.cmsz.cloudplatform.dao;

import java.util.List;

import com.cmsz.cloudplatform.exception.DaoException;
import com.cmsz.cloudplatform.model.TemplateAccount;
import com.hp.core.dao.GenericDao;

public interface TemplateAccountDao extends GenericDao<TemplateAccount, Long> {
	
	public List<TemplateAccount> getByTemplateId(String templateId, Long id) throws DaoException;
}
