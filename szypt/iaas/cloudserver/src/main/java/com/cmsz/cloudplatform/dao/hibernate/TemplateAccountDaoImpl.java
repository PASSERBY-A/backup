package com.cmsz.cloudplatform.dao.hibernate;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.cmsz.cloudplatform.dao.TemplateAccountDao;
import com.cmsz.cloudplatform.exception.DaoException;
import com.cmsz.cloudplatform.model.TemplateAccount;
import com.hp.core.dao.hibernate.GenericDaoImpl;


@Repository("templateAccountDao")
public class TemplateAccountDaoImpl extends GenericDaoImpl<TemplateAccount, Long> implements TemplateAccountDao {
	
	public TemplateAccountDaoImpl(){
		super(TemplateAccount.class);
	}
	
	public TemplateAccountDaoImpl(Class<TemplateAccount> persistentClass) {
		super(persistentClass);
	}

	public List<TemplateAccount> getByTemplateId(String templateId, Long id)
			throws DaoException {
		List<TemplateAccount> templateAccounts = null;
		try{
			Criteria cri = getSession().createCriteria(TemplateAccount.class);
			if(StringUtils.isNotBlank(templateId)){
				cri.add(Restrictions.eq("templateId", templateId));
			}
			if(id!=null){
				cri.add(Restrictions.eq("id", id));
			}
			templateAccounts = cri.list(); 
		}catch(Exception ex){
			log.error("查询模版帐号异常", ex);
			ex.printStackTrace();
			throw new DaoException("查询模版帐号异常", ex);
		}
		return templateAccounts;
	}

}
