package com.hp.idc.boss.service.impl;

import java.io.FilenameFilter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import com.hp.idc.boss.BOSSBase;
import com.hp.idc.boss.service.CusServantInsertService;
import com.hp.idc.customer.entity.CusServant;
import com.hp.idc.customer.entity.CusServantPK;

/**
 * 
 * 客户订购信息 
 * 
 * @author <a href="mailto:rui@hp.com">Wang Rui</a>
 * @version 1.0, 下午4:49:48 2011-8-4
 *
 */
public class CusServantInsertServiceImpl extends BOSSBase implements
		CusServantInsertService {

	@Override
	public boolean saveResult(Map<String, String> result) throws Exception {
		CusServant cusServant = new CusServant();
		try {
			CusServantPK cusServantPK = new CusServantPK();
			BeanUtils.populate(cusServantPK, result);
			cusServant = getCusServantDao().get(cusServantPK);
			BeanUtils.populate(cusServant, result);
			DateFormat df = new SimpleDateFormat(getTimeFormat());
			cusServant.setCreateDate(df.parse(result.get("create_date")));
			cusServant.setDoneDate(df.parse(result.get("done_date")));
			cusServant.setValidDate(df.parse(result.get("valid_date")));
			cusServant.setExpireDate(df.parse(result.get("expire_date")));
			getCusServantDao().save(cusServant);
			return true;
		} catch (Exception e) {
			setErrorMsg(e.getMessage());
			return false;
		}
	}

	FilenameFilter filenameFilter = new WildcardFileFilter("idccustomerservant*.unl");
    
	@Override
	public FilenameFilter getFilenameFilter() {
		return filenameFilter;
	}
}
