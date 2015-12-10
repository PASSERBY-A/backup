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
 * @function �ͻ���ϵ����ҵ���߼���ʵ��
 * @author Fancy
 * @version 1.0, 2:35:52 PM Jul 29, 2011
 * 
 */


public class CustomerManageServiceJpa implements CustomerManageService {

	// �˻����ñ�dao
	@Resource
	private CusAccountDao cusAccountDao;

	// boss idcҵ����Ϣ��dao
	@Resource
	private CusBussinessDao cusBussinessDao;
	// ��ϵ�˱�
	@Resource
	private CusContactDao cusContactDao;
	// �ͻ�������Ϣ��dao
	@Resource
	private CusInfoDao cusInfoDao;
	// �ͻ�������Ϣ��dao
	@Resource
	private CusServantDao cusServantDao;
	// �ͻ����Ѽ�¼��dao
	@Resource
	private CusServiceDao cusServiceDao;

	/**
	 * @function ��ҳ��ѯ�ͻ�������Ϣdaoʵ��
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
	 * @function ����Boss�ͻ���Ż�ȡ�ͻ�������Ϣdaoʵ��
	 * @author Fancy
	 * @date 2011-7-31
	 */
	@Override
	public CusInfo queryCusInfoById(long customerid) {
		return cusInfoDao.get(customerid);
	}

	/**
	 * @function ��ҳ��ѯ�ͻ���ϵ����Ϣdaoʵ��
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
	 * @function ��ҳ��ѯ�ͻ����������Ϣdaoʵ��
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
	 * @function ��ҳ��ѯ�ͻ�������Ϣ��daoʵ��
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
	 * @function ��ҳ��ѯ�ͻ����Ѽ�¼��daoʵ��
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
	 * @function ��ҳ��ѯboss IDC ҵ����Ϣ��daoʵ��
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
