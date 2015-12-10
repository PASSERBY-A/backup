package com.cmsz.cloudplatform.service;

import java.util.List;

import com.cmsz.cloudplatform.exception.ServiceException;
import com.cmsz.cloudplatform.model.TemplateAccount;
import com.hp.core.service.GenericManager;


public interface TemplateAccountManager extends GenericManager<TemplateAccount, Long> {
	/**
	 * 通过模版ID或帐号ID
	 * @param templateId 模版 ID
	 * @param accountId   帐号ID
	 * @return
	 * @throws ServiceException
	 */
	public List<TemplateAccount> getByTemplateIdOrAccoutId(String templateId, String accountId) throws ServiceException;
	
	public List<TemplateAccount> getByTemplateId(String templateId) throws ServiceException;
}
