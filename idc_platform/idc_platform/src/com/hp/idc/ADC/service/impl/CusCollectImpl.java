package com.hp.idc.ADC.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import com.hp.idc.ADC.service.CusCollect;
import com.hp.idc.cusrelation.dao.CusManageContactDao;
import com.hp.idc.cusrelation.dao.CusManageInfoDao;
import com.hp.idc.cusrelation.dao.CusManageServantDao;
import com.hp.idc.cusrelation.entity.CusManageContact;
import com.hp.idc.cusrelation.entity.CusManageInfo;
import com.hp.idc.cusrelation.entity.CusManageServant;
import com.hp.idc.cusrelation.entity.CusManageServantPK;

/**
 * ADC���Ĵ���
 * 
 * 
 * @author <a href="mailto:ruiw@hp.com">Wang rui</a>
 * @version 1.0, ����9:03:49 2011-9-13
 *
 */
@Service("cusCollect")
public class CusCollectImpl implements CusCollect {

	// �ͻ�������Ϣ��dao
	@Resource
	private CusManageInfoDao cusManageInfoDao;

	// �ͻ�������Ϣ��dao
	@Resource
	private CusManageServantDao cusManageServantDao;
	
	//�ͻ���ϵ����Ϣ��dao
	@Resource
	private CusManageContactDao cusManageContactDao;

	/**
	 * �����ͻ���Ϣ�ͱ��ļ��Mapping
	 * 
	 * @return
	 */
	private Map<String, String> getCusInfoMap(){
	   Map<String, String> cusInfoMap = new HashMap<String, String>(); 
	   cusInfoMap.put("ECID", "id");
	   cusInfoMap.put("ECNAME", "name");
	   cusInfoMap.put("CORP_SHORTNAME", "abbrName");
	   cusInfoMap.put("CORP_CMSTAFFNO", "managerName");
	   cusInfoMap.put("CORP_DOMAINID", "vocation");
	   cusInfoMap.put("CORP_STATE", "status");
	   cusInfoMap.put("CORP_ADDR", "billAddress");
	   cusInfoMap.put("CORP_ADDR", "address");
	   cusInfoMap.put("CORP_ZIP", "zipCode");
	   cusInfoMap.put("CORP_LINKMAN", "majorContact");
	   cusInfoMap.put("CORP_LINKMAIL", "email");
	   cusInfoMap.put("CORP_FAX", "fax");
	   cusInfoMap.put("CORP_LINKMOBILE", "phoneNo");
	   cusInfoMap.put("CORP_REGDATE", "openTime");
	   cusInfoMap.put("CORP_CANCELDATE", "cancelTime");
	   return cusInfoMap;
	}
	
	/**
	 * �ͻ���ϵ����Ϣ�ͱ��ļ��Mapping
	 * 
	 * @return
	 */
	private Map<String, String> getCusContactMap(){
	   Map<String, String> cusContactMap = new HashMap<String, String>(); 
	   cusContactMap.put("STAFF_USERNO", "id");
	   cusContactMap.put("ECID", "customerId");
	   cusContactMap.put("STAFF_NAME", "contactName");
	   cusContactMap.put("STAFF_TITLE", "position");
	   cusContactMap.put("STAFF_PHONE", "phone");
	   cusContactMap.put("STAFF_MOBILE", "mobile");
	   cusContactMap.put("STAFF_EMAIL", "address");
	   return cusContactMap;
	}
	
	/**
	 * ����mapת����beanMap
	 * 
	 * @param recordMap ����map
	 * @param beanMap
	 * @return
	 */
	private Map<String, String> mapConvert(Map<String, String> recordMap,
			Map<String, String> beanMap) {
		Map<String,String> result = new HashMap<String,String>();
		for(String beanKey:beanMap.keySet()){
			if(recordMap.containsKey(beanKey)){
				result.put(beanMap.get(beanKey),recordMap.get(beanKey));
				//����û�����ݵ��ֶ�
				if("".equals(recordMap.get(beanKey))&& !beanKey.contains("Time")){
					result.put(beanMap.get(beanKey),"��");
				}
			}
			if(recordMap.containsKey("CORP_LINKMAN")){
				result.put("majorContact",recordMap.get(beanKey));
			}
			// userNo ���⴦��
			if("STAFF_USERNO".equals(beanKey)){
				String userNo = recordMap.get(beanKey);
				userNo = userNo.substring(0,userNo.indexOf("@"));
				result.put(beanMap.get(beanKey),userNo);
			}
		}
        return result;
	}

	@Override
	public CusManageInfo getCusInfo(Map<String, String> recordMap) {
		CusManageInfo cusManageInfo = new CusManageInfo();
		try {
			Map<String, String> cusInfoMap = mapConvert(recordMap,
					getCusInfoMap());
			BeanUtils.populate(cusManageInfo, cusInfoMap);
		} catch (Exception e) {
			return cusManageInfo;
		}
		return cusManageInfo;
	}

	/*
	 * (non-Javadoc)
	 * @see com.hp.idc.ADC.sync.action.CusCollect#dealCusInfo(com.hp.idc.customer.entity.CusInfo, java.lang.String)
	 */
	public String[] dealCusInfo(CusManageInfo cusInfo, String operType) {
		String[] result = { "0", "�ɹ���" };
		boolean isExist = getCusManageInfoDao().exists(cusInfo.getId());
		if ("01".equals(operType)) {
			if (!isExist) {
				cusInfo.setStatus(1);
				cusInfo.setOpenTime(new Date());
//				System.out.println("��Ҫ��ϵ�ˣ�"+cusInfo.getMajorContact());
				getCusManageInfoDao().save(cusInfo);
			}else{
				result[0] = "2110";
				result[1] = "������ϵ�Ѵ��ڣ�����������Ķ���������SIϵͳ������һ�¡�";
				return result;
			}
		}
		if ("04".equals(operType)) {
			if (!isExist) {
				result[0] = "2112";
				result[1] = "���ʱ������ϵ�����ڡ�";
				getCusManageInfoDao().save(cusInfo);
				return result;
			} else {
				getCusManageInfoDao().save(cusInfo);
			}
		}
		if ("05".equals(operType)) {
			if (!isExist) {
				result[0] = "2113";
				result[1] = "�˶�ʱ������ϵ�����ڡ�";
				return result;
			} else {
				cusInfo.setStatus(3);
				cusInfo.setCancelTime(new Date());
				getCusManageInfoDao().save(cusInfo);
			}
		}
		return result;
	}

	@Override
	public CusManageServant getCusServant(Map<String, String> recordMap) {
		CusManageServant cusServant = new CusManageServant();
		CusManageServantPK cusManageServantPK = new CusManageServantPK();
		cusManageServantPK.setCustomerId(Long.valueOf(recordMap.get("ECID")));
		cusManageServantPK
				.setServiceId(Long.valueOf(recordMap.get("SERVICEID")));
		//������Ʒ���
//		cusManageServantPK
//		.setServiceId(Long.valueOf(recordMap.get("POINTVALUE")));
		cusServant.setId(cusManageServantPK);
		//������Ʒ����
//		cusServant.setServiceValue(recordMap.get("POINTNAME"));
		return cusServant;
	}
	
	@Override
	public String[] dealCusServant(CusManageServant cusServant, String operType) {
		String[] result = { "0", "�ɹ�" };
		boolean isExist = getCusManageServantDao().exists(cusServant.getId());
		if ("01".equals(operType)) {
			if (!isExist) {
				cusServant.setCreateDate(new Date());
				getCusManageServantDao().save(cusServant);
			}else{
				result[0] = "2110";
				result[1] = "������ϵ�Ѵ��ڣ�����������Ķ���������SIϵͳ������һ�¡�";
				return result;
			}
		}
		if ("04".equals(operType)) {
			if (!isExist){
				result[0] = "2112";
				result[1] = "���ʱ������ϵ�����ڡ�";
				getCusManageServantDao().save(cusServant);
				return result;
			} else {
				getCusManageServantDao().save(cusServant);
			}
		}
		if ("05".equals(operType)) {
			if (!isExist) {
				result[0] = "2113";
				result[1] = "�˶�ʱ������ϵ�����ڡ�";
				return result;
			} else {
				cusServant.setExpireDate(new Date());
				getCusManageServantDao().save(cusServant);
			}
		}
		return result;
	}

	@Override
	public CusManageContact getCusContact(Map<String, String> recordMap) {
		CusManageContact cusContact = new CusManageContact();
		try {
			Map<String, String> cusContactMap = mapConvert(recordMap,
					getCusContactMap());
			BeanUtils.populate(cusContact, cusContactMap);
		} catch (Exception e) {
			return cusContact;
		}
		return cusContact;
	}
	
	@Override
	public String[] dealCusContact(CusManageContact cusContact, String operType) {
		String[] result = { "0", "�ɹ�" };
		boolean isExist = getCusManageContactDao().exists(cusContact.getId());
		if ("01".equals(operType)) {
			if(!isExist){
				getCusManageContactDao().save(cusContact);
			}else{
				result[0] = "2210";
				result[1] = "Ա���Ѵ�������SIϵͳ������һ�¡�";
				return result;
			}
		}
		if ("04".equals(operType)) {
			if(!isExist){
				result[0] = "2112";
				result[1] = "���ʱԱ�������ڡ�";
				getCusManageContactDao().save(cusContact);
				return result;
			} else {
				getCusManageContactDao().save(cusContact);
			}
		}
		if ("05".equals(operType)) {
			if(!isExist){
				result[0] = "2213";
				result[1] = "�˶�ʱԱ�������ڡ�";
				return result;
			} else {
				getCusManageContactDao().save(cusContact);
			}
		}
		// 06 : ������Ϣ���
		if ("06".equals(operType)) {
			if (!isExist) {
				result[0] = "2112";
				result[1] = "���ʱԱ�������ڡ�";
				getCusManageContactDao().save(cusContact);
				return result;
			} else {
				getCusManageContactDao().save(cusContact);
			}
		}
		// 11 ������
		if ("11".equals(operType)) {
			if(!isExist){
				getCusManageContactDao().save(cusContact);
			}else{
				result[0] = "2210";
				result[1] = "Ա���Ѵ�������SIϵͳ������һ�¡�";
				return result;
			}
		}
		// 12 ���˳�
		if ("12".equals(operType)) {
			if(!isExist){
				result[0] = "2213";
				result[1] = "�˳�ʱԱ�������ڡ�";
				return result;
			} else {
				getCusManageContactDao().remove(cusContact);
			}
		}
		return result;
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

	public CusManageContactDao getCusManageContactDao() {
		return cusManageContactDao;
	}

	public void setCusManageContactDao(CusManageContactDao cusManageContactDao) {
		this.cusManageContactDao = cusManageContactDao;
	}

}
