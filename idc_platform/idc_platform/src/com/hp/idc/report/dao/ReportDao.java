package com.hp.idc.report.dao;

import java.util.List;
import java.util.Map;

import com.hp.idc.report.util.Page;

import net.sf.json.JSONObject;

public interface ReportDao {
	//�ͻ�����ͳ�Ʊ���
	JSONObject getCustomerFuwuCountReport(Page page);
	//�ͻ������嵥����
	JSONObject getCustomerQingdanCountReport(Page page);
	//�ͻ��䶯��ϸ����
	JSONObject getCustomerBiandongmingxiCountReport(Page page);
	//�ͻ��䶯ͳ�Ʊ���
	JSONObject getCustomerBiandongtongjiCountReport(Page page);
	//�ͻ�ҵ�����ͳ�Ʊ���
	void getCustomerBussnessTypeCountReport();
	//�ͻ���Ʒ����ͳ�Ʊ���
	JSONObject getCustomerWupinjinruCountReport(Page page);
	
	
	
	//ҵ�񹤵�ͳ�Ʊ���
	JSONObject getBussnessOrderCountReport(Page page);
	//ҵ��䶯ͳ�Ʊ���
	JSONObject getBussnessChangeCountReport(Page page);
	//����ҵ����Ϣͳ�Ʊ���
	JSONObject getBasicBussnessInformationReport(Page page);
	//��ֵҵ����Ϣͳ�Ʊ���
	List<Map<String, Object>> getPriceBussnessInformationReport();
	
	
	//�����ͻ�����ͳ�Ʊ���
	List<Map<String, Object>> exportCustomerFuwuCountReport(Page page);
	//�����ͻ������嵥����
	List<Map<String, Object>> exportCustomerQingdanCountReport(Page page);
	//�����ͻ��䶯��ϸ����
	List<Map<String, Object>> exportCustomerBiandongmingxiCountReport(Page page);
	//�����ͻ��䶯ͳ�Ʊ���
	List<Map<String, Object>> exportCustomerBiandongtongjiCountReport(Page page);
	//�����ͻ�ҵ�����ͳ�Ʊ���
	List<Map<String, Object>> exportCustomerBussnessTypeCountReport(Page page);
	//�����ͻ���Ʒ����ͳ�Ʊ���
	List<Map<String, Object>> exportCustomerWupinjinruCountReport(Page page);
	
	
	
	//����ҵ�񹤵�ͳ�Ʊ���
	List<Map<String, Object>> exportBussnessOrderCountReport(Page page);
	//����ҵ��䶯ͳ�Ʊ���
	List<Map<String, Object>> exportBussnessChangeCountReport(Page page);
	//��������ҵ����Ϣͳ�Ʊ���
	List<Map<String, Object>> exportBasicBussnessInformationReport(Page page);
	//������ֵҵ����Ϣͳ�Ʊ���
	List<Map<String, Object>> exportPriceBussnessInformationReport(Page page);
	
	
	
}
