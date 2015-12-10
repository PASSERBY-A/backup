package com.hp.idc.boss.service.impl;

import java.io.FilenameFilter;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import com.hp.idc.boss.BOSSBase;
import com.hp.idc.boss.service.CusServiceInsertService;
import com.hp.idc.customer.entity.CusService;

/**
 * 
 * 客户消费记录同步
 * 
 * @author <a href="mailto:ruiw@hp.com">Wang Rui</a>
 * @version 1.0, 下午5:22:39 2011-8-4
 *
 */
public class CusServiceInsertServiceImpl extends BOSSBase implements
		CusServiceInsertService {

	@Override
	public boolean saveResult(Map<String, String> result) throws Exception {
		CusService cusAccount = new CusService();
		try {
			BeanUtils.populate(cusAccount, result);
			getCusServiceDao().save(cusAccount);
			return true;
		} catch (Exception e) {
			setErrorMsg(e.getMessage());
			return false;
		}
	}

	FilenameFilter filenameFilter = new WildcardFileFilter("idccustomerservicerec*.unl");
    
	@Override
	public FilenameFilter getFilenameFilter() {
		return filenameFilter;
	}
}
