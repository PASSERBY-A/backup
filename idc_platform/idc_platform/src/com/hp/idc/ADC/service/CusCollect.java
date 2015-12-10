package com.hp.idc.ADC.service;

import java.util.Map;

import com.hp.idc.cusrelation.entity.CusManageContact;
import com.hp.idc.cusrelation.entity.CusManageInfo;
import com.hp.idc.cusrelation.entity.CusManageServant;

public interface CusCollect {
	
	/**
	 * 得到 客户信息bean
	 * 
	 * @param recordMap 报文结果转换的map
	 * @return
	 */
	public CusManageInfo getCusInfo(Map<String, String> recordMap);
	
	/**
	 * 增删改数据库中客户信息
	 * 
	 * @param cusInfo 客户信息bean
	 * @param operType 操作 操作标志 01 : 订购 02 : 暂停 03 : 恢复 04 : 变更 05 : 退订
	 */
	public String[] dealCusInfo(CusManageInfo cusInfo, String operType);
	
	
	/**
	 * 得到客户订购bean
	 * 
	 * @param recordMap
	 * @return 
	 */
	public CusManageServant getCusServant(Map<String, String> recordMap);
	
	/**
	 * 增删改数据库中客户订购信息
	 * 
	 * @param cusServant 客户订购bean
	 * @param operType 操作 操作标志 01 : 订购 02 : 暂停 03 : 恢复 04 : 变更 05 : 退订
	 */
	public String[] dealCusServant(CusManageServant cusServant, String operType);
	
	
	/**
	 * 得到客户联系人bean
	 * 
	 * @param recordMap
	 * @return 
	 */
	public CusManageContact getCusContact(Map<String, String> recordMap);
	
	
	/**
	 * 增删改客户联系人信息
	 * 
	 * @param cusContact
	 * @param operType 操作标志：01 : 订购 02 : 暂停 03 : 恢复 04 : 订购关系变更 05 : 退订 06 : 基本信息变更 11 ：加入 12 ：退出
	 * @return
	 */
	public String[] dealCusContact(CusManageContact cusContact, String operType);
	
	
}
