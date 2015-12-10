package com.hp.idc.boss.service.impl;

import java.io.FilenameFilter;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import com.hp.idc.boss.BOSSBase;
import com.hp.idc.boss.service.CusAccountInsertService;
import com.hp.idc.customer.entity.CusAccount;
import com.hp.idc.customer.entity.CusAccountPK;

/**
 * 
 * 客户帐务费同步
 * 
 * @author <a href="mailto:ruiw@hp.com">Wang Rui</a>
 * @version 1.0, 下午5:11:30 2011-8-4
 *
 */
public class CusAccountInsertServiceImpl extends BOSSBase implements
		CusAccountInsertService {

	@Override
	public boolean saveResult(Map<String, String> result) throws Exception {
		CusAccount cusAccount = new CusAccount();
		try {
			BeanUtils.populate(cusAccount, result);
			CusAccountPK cusAccountPK = new CusAccountPK();
			BeanUtils.populate(cusAccountPK, result);
			cusAccount.setId(cusAccountPK);
			getCusAccountDao().save(cusAccount);
			return true;
		} catch (Exception e) {
			setErrorMsg(e.getMessage());
			return false;
		}
	}
	
	FilenameFilter filenameFilter = new WildcardFileFilter("idccustomeraccountfee*.unl");

	@Override
	public FilenameFilter getFilenameFilter() {
		return filenameFilter;
	}

}
