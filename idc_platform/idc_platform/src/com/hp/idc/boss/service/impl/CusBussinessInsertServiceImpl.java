package com.hp.idc.boss.service.impl;

import java.io.FilenameFilter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import com.hp.idc.boss.BOSSBase;
import com.hp.idc.boss.service.CusBussinessInsertService;
import com.hp.idc.customer.entity.CusBussiness;

/**
 * 
 * BOSS IDC业务信息同步
 * 
 * @author <a href="mailto:ruiw@hp.com">Wang Rui</a>
 * @version 1.0, 下午2:37:40 2011-8-5
 *
 */
public class CusBussinessInsertServiceImpl extends BOSSBase implements
		CusBussinessInsertService {

	@Override
	public boolean saveResult(Map<String, String> result) throws Exception {
		CusBussiness cusBussiness = new CusBussiness();
		try {
			Long Id = Long.parseLong(result.get("orderId"));
			Long orderDetailId = Long.parseLong(result.get("orderDetailId"));
			Long doneCode = Long.parseLong(result.get("doneCode"));
			cusBussiness = getCusBussinessDao().findCustomerBussiness(Id,
					orderDetailId, doneCode);
			BeanUtils.populate(cusBussiness, result);
			DateFormat df = new SimpleDateFormat(getTimeFormat());
			cusBussiness.setCreateDate(df.parse(result.get("create_date")));
			getCusBussinessDao().save(cusBussiness);
			return true;
		} catch (Exception e) {
			setErrorMsg(e.getMessage());
			return false;
		}
	}
	
	FilenameFilter filenameFilter = new WildcardFileFilter("idcboss*.unl"); 
    
	@Override
	public FilenameFilter getFilenameFilter() {
		return filenameFilter;
	}
}
