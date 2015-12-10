package com.hp.idc.cusrelation.service.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hp.idc.common.core.bo.Page;
import com.hp.idc.cusrelation.dao.CusManageContactDao;
import com.hp.idc.cusrelation.dao.CusManageInfoDao;
import com.hp.idc.cusrelation.dao.CusManageServantDao;
import com.hp.idc.cusrelation.entity.CusManageContact;
import com.hp.idc.cusrelation.entity.CusManageInfo;
import com.hp.idc.cusrelation.entity.CusManageServant;
import com.hp.idc.cusrelation.service.CusRelationManageService;


/**
 * 
 * 
 * @function �ͻ���ϵ����ҵ���߼���ʵ��
 * @author Fancy
 * @version 1.0, 2:35:52 PM Jul 29, 2011
 * 
 */

@Service("cusRelationManageService")
public class CusRelationManageServiceJpa implements CusRelationManageService {

	// ��ϵ�˱�
	@Resource
	private CusManageContactDao cusManageContactDao;
	// �ͻ�������Ϣ��dao
	@Resource
	private CusManageInfoDao cusManageInfoDao;
	// �ͻ�������Ϣ��dao
	@Resource
	private CusManageServantDao cusManageServantDao;

	/**
	 * @function ��ҳ��ѯ�ͻ�������Ϣdaoʵ��
	 * @author Fancy
	 * @date 2011-7-31
	 */
	@Override
	public Page<CusManageInfo> queryCustomerInfo(Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize) {
		return cusManageInfoDao.queryCustomerInfo(paramMap, sortMap, pageNo,
				pageSize);
	}

	/**
	 * @function ����Boss�ͻ���Ż�ȡ�ͻ�������Ϣdaoʵ��
	 * @author Fancy
	 * @date 2011-7-31
	 */
	@Override
	public CusManageInfo queryCusInfoById(long customerid) {
		return cusManageInfoDao.get(customerid);
	}

	/**
	 * @function ��ҳ��ѯ�ͻ���ϵ����Ϣdaoʵ��
	 * @author Fancy
	 * @date 2011-7-31
	 */
	@Override
	public Page<CusManageContact> queryCustomerContact(
			Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize) {
		return cusManageContactDao.queryCustomerContact(paramMap, sortMap,
				pageNo, pageSize);
	}

	/**
	 * @function ��ҳ��ѯ�ͻ�������Ϣ��daoʵ��
	 * @author Fancy
	 * @date 2011-7-31
	 */
	@Override
	public Page<CusManageServant> queryCustomerServant(
			Map<String, Object> paramMap,
			LinkedHashMap<String, String> sortMap, int pageNo, int pageSize) {
		return cusManageServantDao.queryCustomerServant(paramMap, sortMap,
				pageNo, pageSize);
	}

	public CusManageContactDao getCusManageContactDao() {
		return cusManageContactDao;
	}

	public void setCusManageContactDao(CusManageContactDao cusManageContactDao) {
		this.cusManageContactDao = cusManageContactDao;
	}

	public CusManageInfoDao getCusManageInfoDao() {
		return cusManageInfoDao;
	}

	public void setCusManageInfoDao(CusManageInfoDao cusManageInfoDao) {
		this.cusManageInfoDao = cusManageInfoDao;
	}

	public CusManageServantDao getCusManageServantDao() {
		return cusManageServantDao;
	}

	public void setCusManageServantDao(CusManageServantDao cusManageServantDao) {
		this.cusManageServantDao = cusManageServantDao;
	}

}
