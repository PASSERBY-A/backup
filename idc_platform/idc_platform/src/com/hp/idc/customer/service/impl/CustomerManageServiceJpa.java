package com.hp.idc.customer.service.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hp.idc.business.entity.ProductCatalog;
import com.hp.idc.common.core.bo.Page;
import com.hp.idc.customer.dao.CusAccountDao;
import com.hp.idc.customer.dao.CusBussinessDao;
import com.hp.idc.customer.dao.CusContactDao;
import com.hp.idc.customer.dao.CusInfoDao;
import com.hp.idc.customer.dao.CusServantDao;
import com.hp.idc.customer.dao.CusServiceDao;
import com.hp.idc.customer.entity.CusAccount;
import com.hp.idc.customer.entity.CusBussiness;
import com.hp.idc.customer.entity.CusContact;
import com.hp.idc.customer.entity.CusInfo;
import com.hp.idc.customer.entity.CusServant;
import com.hp.idc.customer.entity.CusService;
import com.hp.idc.customer.service.CustomerManageService;
import com.hp.idc.kbm.entity.KbmKnowledge;

/**
 * 
 * 
 * @function 客户关系管理业务逻辑层实现
 * @author Fancy
 * @version 1.0, 2:35:52 PM Jul 29, 2011
 * 
 */


public class CustomerManageServiceJpa implements CustomerManageService {

	// 账户费用表dao
	@Resource
	private CusAccountDao cusAccountDao;

	// boss idc业务信息表dao
	@Resource
	private CusBussinessDao cusBussinessDao;
	// 联系人表
	@Resource
	private CusContactDao cusContactDao;
	// 客户基本信息表dao
	@Resource
	private CusInfoDao cusInfoDao;
	// 客户订购信息表dao
	@Resource
	private CusServantDao cusServantDao;
	// 客户消费记录表dao
	@Resource
	private CusServiceDao cusServiceDao;

	/**
	 * @function 分页查询客户基本信息dao实现
	 * @author Fancy
	 * @date 2011-7-31
	 */
	@Override
	public Page<CusInfo> queryCustomerInfo(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize) {
		return cusInfoDao
				.queryCustomerInfo(paramMap, sortMap, pageNo, pageSize);
	}

	/**
	 * @function 根据Boss客户编号获取客户基本信息dao实现
	 * @author Fancy
	 * @date 2011-7-31
	 */
	@Override
	public CusInfo queryCusInfoById(long customerid) {
		return cusInfoDao.get(customerid);
	}

	/**
	 * @function 分页查询客户联系人信息dao实现
	 * @author Fancy
	 * @date 2011-7-31
	 */
	@Override
	public Page<CusContact> queryCustomerContact(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize) {
		return cusContactDao.queryCustomerContact(paramMap, sortMap, pageNo,
				pageSize);
	}

	/**
	 * @function 分页查询客户账务费用信息dao实现
	 * @author Fancy
	 * @date 2011-7-31
	 */
	@Override
	public Page<CusAccount> queryCustomerAccount(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize) {
		return cusAccountDao.queryCustomerAccount(paramMap, sortMap, pageNo,
				pageSize);
	}

	/**
	 * @function 分页查询客户订购信息表dao实现
	 * @author Fancy
	 * @date 2011-7-31
	 */
	@Override
	public Page<CusServant> queryCustomerServant(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize) {
		return cusServantDao.queryCustomerServant(paramMap, sortMap, pageNo,
				pageSize);
	}

	/**
	 * @function 分页查询客户消费记录表dao实现
	 * @author Fancy
	 * @date 2011-7-31
	 */
	@Override
	public Page<CusService> queryCustomerService(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize) {
		return cusServiceDao.queryCustomerService(paramMap, sortMap, pageNo,
				pageSize);
	}

	/**
	 * @function 分页查询boss IDC 业务信息表dao实现
	 * @author Fancy
	 * @date 2011-7-31
	 */
	@Override
	public Page<CusBussiness> queryCustomerBussiness(
			Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize) {
		return cusBussinessDao.queryCustomerBussiness(paramMap, sortMap,
				pageNo, pageSize);
	}

	public CusAccountDao getCusAccountDao() {
		return cusAccountDao;
	}

	public void setCusAccountDao(CusAccountDao cusAccountDao) {
		this.cusAccountDao = cusAccountDao;
	}

	public CusBussinessDao getCusBussinessDao() {
		return cusBussinessDao;
	}

	public void setCusBussinessDao(CusBussinessDao cusBussinessDao) {
		this.cusBussinessDao = cusBussinessDao;
	}

	public CusContactDao getCusContactDao() {
		return cusContactDao;
	}

	public void setCusContactDao(CusContactDao cusContactDao) {
		this.cusContactDao = cusContactDao;
	}

	public CusInfoDao getCusInfoDao() {
		return cusInfoDao;
	}

	public void setCusInfoDao(CusInfoDao cusInfoDao) {
		this.cusInfoDao = cusInfoDao;
	}

	public CusServantDao getCusServantDao() {
		return cusServantDao;
	}

	public void setCusServantDao(CusServantDao cusServantDao) {
		this.cusServantDao = cusServantDao;
	}

	public CusServiceDao getCusServiceDao() {
		return cusServiceDao;
	}

	public void setCusServiceDao(CusServiceDao cusServiceDao) {
		this.cusServiceDao = cusServiceDao;
	}

}
