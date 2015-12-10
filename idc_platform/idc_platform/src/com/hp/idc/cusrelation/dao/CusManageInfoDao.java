/**
 * CusInfoDao.java
 * @author fancy
 * @date 2011-7-31
 */
package com.hp.idc.cusrelation.dao;

import java.util.LinkedHashMap;
import java.util.Map;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.dao.GenericDao;
import com.hp.idc.cusrelation.entity.CusManageInfo;


public interface CusManageInfoDao extends GenericDao<CusManageInfo, Long> {

	/**
	 * 分页查询客户基本信息表Dao
	 * 
	 * @Title:queryCustomerInfo
	 * @Desciption:TODO
	 * @param paramMap
	 * @param sortMap
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page<CusManageInfo> queryCustomerInfo(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize);
}
