package com.hp.idc.boss.service.impl;

import java.io.FilenameFilter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import com.hp.idc.boss.BOSSBase;
import com.hp.idc.boss.service.CusInfoInsertSerivce;
import com.hp.idc.customer.entity.CusInfo;

/**
 * 
 * 客户基本信息同步
 * 
 * @author <a href="mailto:ruiw@hp.com">Wang Rui</a>
 * @version 1.0, 下午3:54:40 2011-8-4
 *
 */
public class CusInfoInsertServiceImpl extends BOSSBase implements
		CusInfoInsertSerivce {

	@Override
	public boolean saveResult(Map<String, String> result) throws Exception {
		CusInfo cusInfo = new CusInfo();
		try {
			BeanUtils.populate(cusInfo, result);
			DateFormat df = new SimpleDateFormat(getTimeFormat());
			cusInfo.setOpenTime(df.parse(result.get("dt_opentime")));
			cusInfo.setCancelTime(df.parse(result.get("dt_canceltime")));
			cusInfo.setActiveTime(df.parse(result.get("dt_activetime")));
			getCusInfoDao().save(cusInfo);
			return true;
		} catch (Exception e) {
			setErrorMsg(e.getMessage());
			return false;
		}
	}
	
    FilenameFilter filenameFilter = new WildcardFileFilter("idccustomer*.unl");

    @Override
	public FilenameFilter getFilenameFilter() {
		return filenameFilter;
	}

	public void setFilenameFilter(FilenameFilter filenameFilter) {
		this.filenameFilter = filenameFilter;
	} 

}
