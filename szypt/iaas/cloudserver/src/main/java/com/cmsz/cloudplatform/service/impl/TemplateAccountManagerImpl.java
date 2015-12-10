package com.cmsz.cloudplatform.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmsz.cloudplatform.dao.TemplateAccountDao;
import com.cmsz.cloudplatform.exception.DaoException;
import com.cmsz.cloudplatform.exception.ServiceException;
import com.cmsz.cloudplatform.model.TemplateAccount;
import com.cmsz.cloudplatform.service.TemplateAccountManager;
import com.cmsz.cloudplatform.utils.ServiceErrorCode;
import com.hp.core.service.impl.GenericManagerImpl;
@Service("templateAccountManager")
public class TemplateAccountManagerImpl extends GenericManagerImpl<TemplateAccount, Long> implements TemplateAccountManager {
	
	private TemplateAccountDao templateAccountDao;
	
	public TemplateAccountManagerImpl() {
		super();
	}
	
	public TemplateAccountManagerImpl(TemplateAccountDao templateAccountDao) {
		super(templateAccountDao);
		this.templateAccountDao = templateAccountDao;
	}
	
	
	public TemplateAccountDao getTemplateAccountDao() {
		return templateAccountDao;
	}
	
	
	
	@Autowired
	public void setTemplateAccountDao(TemplateAccountDao templateAccountDao) {
		this.templateAccountDao = templateAccountDao;
		this.dao = templateAccountDao;
	}
	
	/**
	 * 通过模版ID或帐号ID
	 * @param templateId 模版 ID
	 * @param accountId   帐号ID
	 * @return
	 * @throws ServiceException
	 */
	public List<TemplateAccount> getByTemplateIdOrAccoutId(String templateId, String accountId) throws ServiceException {
		if(StringUtils.isBlank(templateId) && StringUtils.isBlank(accountId)){
			throw new ServiceException("参数错误",ServiceErrorCode.PARAMETER_EXCEPTION);
		}
		List<TemplateAccount> templateAccounts  = null;
		try{
			Long _accountId = StringUtils.isBlank(accountId)?null:Long.valueOf(accountId);
			templateAccounts = templateAccountDao.getByTemplateId(templateId, _accountId);
		
		}catch(DaoException ex){
			log.error(ex.getMessage(),ex);
			ex.printStackTrace();
		}catch(Exception ex){
			log.error(ex.getMessage(),ex);
			ex.printStackTrace();
		}
		
		return templateAccounts;
	}

	public List<TemplateAccount> getByTemplateId(String templateId) throws ServiceException {
		return getByTemplateIdOrAccoutId(templateId,"");
	}
	
}
