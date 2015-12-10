package com.hp.idc.report.service;

import java.util.List;
import java.util.Map;

import com.hp.idc.report.util.Page;

import net.sf.json.JSONObject;

public interface ReportService {
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
	void getCustomerWupinjinruCountReport();
	
	
	
	//ҵ�񹤵�ͳ�Ʊ���
	JSONObject getBussnessOrderCountReport(Page page);
	//ҵ��䶯ͳ�Ʊ���
	JSONObject getBussnessChangeCountReport(Page page);
	//����ҵ����Ϣͳ�Ʊ���
	JSONObject getBasicBussnessInformationReport(Page page);
	//��ֵҵ����Ϣͳ�Ʊ���
	void getPriceBussnessInformationReport();
	
	
	//�����ͻ�����ͳ�Ʊ���
	String exportCustomerFuwuCountReport(Page page);
	//�����ͻ������嵥����
	String exportCustomerQingdanCountReport(Page page);
	//�����ͻ��䶯��ϸ����
	String exportCustomerBiandongmingxiCountReport(Page page);
	//�����ͻ��䶯ͳ�Ʊ���
	String exportCustomerBiandongtongjiCountReport(Page page);
	//�����ͻ�ҵ�����ͳ�Ʊ���
	void exportCustomerBussnessTypeCountReport();
	//�����ͻ���Ʒ����ͳ�Ʊ���
	void exportCustomerWupinjinruCountReport();
	
	
	
	//����ҵ�񹤵�ͳ�Ʊ���
	String exportBussnessOrderCountReport(Page page);
	//����ҵ��䶯ͳ�Ʊ���
	String exportBussnessChangeCountReport(Page page);
	//��������ҵ����Ϣͳ�Ʊ���
	String exportBasicBussnessInformationReport(Page page);
	//������ֵҵ����Ϣͳ�Ʊ���
	String exportPriceBussnessInformationReport();
	
}
