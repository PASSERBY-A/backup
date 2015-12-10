package com.hp.idc.ADC.service;

import java.util.Map;

import com.hp.idc.cusrelation.entity.CusManageContact;
import com.hp.idc.cusrelation.entity.CusManageInfo;
import com.hp.idc.cusrelation.entity.CusManageServant;

public interface CusCollect {
	
	/**
	 * �õ� �ͻ���Ϣbean
	 * 
	 * @param recordMap ���Ľ��ת����map
	 * @return
	 */
	public CusManageInfo getCusInfo(Map<String, String> recordMap);
	
	/**
	 * ��ɾ�����ݿ��пͻ���Ϣ
	 * 
	 * @param cusInfo �ͻ���Ϣbean
	 * @param operType ���� ������־ 01 : ���� 02 : ��ͣ 03 : �ָ� 04 : ��� 05 : �˶�
	 */
	public String[] dealCusInfo(CusManageInfo cusInfo, String operType);
	
	
	/**
	 * �õ��ͻ�����bean
	 * 
	 * @param recordMap
	 * @return 
	 */
	public CusManageServant getCusServant(Map<String, String> recordMap);
	
	/**
	 * ��ɾ�����ݿ��пͻ�������Ϣ
	 * 
	 * @param cusServant �ͻ�����bean
	 * @param operType ���� ������־ 01 : ���� 02 : ��ͣ 03 : �ָ� 04 : ��� 05 : �˶�
	 */
	public String[] dealCusServant(CusManageServant cusServant, String operType);
	
	
	/**
	 * �õ��ͻ���ϵ��bean
	 * 
	 * @param recordMap
	 * @return 
	 */
	public CusManageContact getCusContact(Map<String, String> recordMap);
	
	
	/**
	 * ��ɾ�Ŀͻ���ϵ����Ϣ
	 * 
	 * @param cusContact
	 * @param operType ������־��01 : ���� 02 : ��ͣ 03 : �ָ� 04 : ������ϵ��� 05 : �˶� 06 : ������Ϣ��� 11 ������ 12 ���˳�
	 * @return
	 */
	public String[] dealCusContact(CusManageContact cusContact, String operType);
	
	
}
