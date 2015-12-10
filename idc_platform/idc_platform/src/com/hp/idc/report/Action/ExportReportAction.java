package com.hp.idc.report.Action;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.hp.idc.common.Constant;
import com.hp.idc.report.service.ReportService;
import com.hp.idc.report.util.Page;
import com.opensymphony.xwork2.ActionContext;

public class ExportReportAction extends BaseAction {
	private int start;
	private int limit;
	private String beginDate;
	private String endDate;
	private int recordSize;
	private String user ;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//�����ͻ�����ͳ�Ʊ���
	public String exportCustomerFuwuCountReport() throws Exception{
		Page page = new Page();
		page.setStartTime(beginDate);
		page.setEndTime(endDate);
		page.setLoginUser((String) ActionContext.getContext().getSession().get(Constant.SESSION_LOGIN));
		
		ReportService reportService= (ReportService) this.getBean("reportService");
		String fileName = reportService.exportCustomerFuwuCountReport(page);
		FileInputStream is = null;
		try {
			is = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return toXLS(is);
	}
	//�����ͻ������嵥����
	public String exportCustomerQingdanCountReport() throws Exception{
		Page page = new Page();
		page.setStartTime(beginDate);
		page.setEndTime(endDate);
		page.setLoginUser((String) ActionContext.getContext().getSession().get(Constant.SESSION_LOGIN));

		ReportService reportService= (ReportService) this.getBean("reportService");
		String fileName = reportService.exportCustomerQingdanCountReport(page);
		FileInputStream is = null;
		try {
			is = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return toXLS(is);
	}
	//�����ͻ��䶯��ϸ����
	public String exportCustomerBiandongmingxiCountReport() throws Exception{
		Page page = new Page();
		page.setStartTime(beginDate);
		page.setEndTime(endDate);
		page.setLoginUser((String) ActionContext.getContext().getSession().get(Constant.SESSION_LOGIN));

		ReportService reportService= (ReportService) this.getBean("reportService");
		String fileName = reportService.exportCustomerBiandongmingxiCountReport(page);
		FileInputStream is = null;
		try {
			is = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return toXLS(is);
	}
	
	//�����ͻ��䶯ͳ�Ʊ���
	public String exportCustomerBiandongtongjiCountReport() throws Exception{		
		Page page = new Page();
		page.setStartTime(beginDate);
		page.setEndTime(endDate);
		page.setLoginUser((String) ActionContext.getContext().getSession().get(Constant.SESSION_LOGIN));

		ReportService reportService= (ReportService) this.getBean("reportService");
		String fileName = reportService.exportCustomerBiandongtongjiCountReport(page);
		FileInputStream is = null;
		try {
			is = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return toXLS(is);
	}
	//�����ͻ�ҵ�����ͳ�Ʊ���
	public void exportCustomerBussnessTypeCountReport() throws Exception{
		System.out.println("�����ͻ�ҵ�����ͳ�Ʊ���");	
	}
	//�����ͻ���Ʒ����ͳ�Ʊ���
	public void exportCustomerWupinjinruCountReport() throws Exception{
		System.out.println("�����ͻ���Ʒ����ͳ�Ʊ���");
	}
	
	
	//����ҵ�񹤵�ͳ�Ʊ���
	public String exportBussnessOrderCountReport() throws Exception{
		Page page = new Page();
		page.setStartTime(beginDate);
		page.setEndTime(endDate);
		page.setLoginUser((String) ActionContext.getContext().getSession().get(Constant.SESSION_LOGIN));

		ReportService reportService= (ReportService) this.getBean("reportService");
		String fileName = reportService.exportBussnessOrderCountReport(page);
		FileInputStream is = null;
		try {
			is = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return toXLS(is);
	}
	
	//����ҵ��䶯ͳ�Ʊ���
	public String exportBussnessChangeCountReport() throws Exception{
		Page page = new Page();
		page.setStartTime(beginDate);
		page.setEndTime(endDate);
		page.setLoginUser((String) ActionContext.getContext().getSession().get(Constant.SESSION_LOGIN));
		ReportService reportService= (ReportService) this.getBean("reportService");
		String fileName = reportService.exportBussnessChangeCountReport(page);
		FileInputStream is = null;
		try {
			is = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return toXLS(is);
	}
	//��������ҵ����Ϣͳ�Ʊ���
	public String exportBasicBussnessInformationReport() throws Exception{
		Page page = new Page();
		page.setStartTime(beginDate);
		page.setEndTime(endDate);
		page.setLoginUser((String) ActionContext.getContext().getSession().get(Constant.SESSION_LOGIN));
		//ͳ�Ʊ������ͣ�����
		page.setReportType(0);
		page.setTitle("����ҵ����Ϣͳ�Ʊ���");
		
		ReportService reportService= (ReportService) this.getBean("reportService");
		String fileName = reportService.exportBasicBussnessInformationReport(page);
		FileInputStream is = null;
		try {
			is = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return toXLS(is);
	}
	//������ֵҵ����Ϣͳ�Ʊ���
	public String exportPriceBussnessInformationReport() throws Exception{
		Page page = new Page();
		page.setStartTime(beginDate);
		page.setEndTime(endDate);
		page.setLoginUser((String) ActionContext.getContext().getSession().get(Constant.SESSION_LOGIN));
		//ͳ�Ʊ������ͣ���ֵ
		page.setReportType(1);
		page.setTitle("��ֵҵ����Ϣͳ�Ʊ���");
		
		ReportService reportService= (ReportService) this.getBean("reportService");
		String fileName = reportService.exportBasicBussnessInformationReport(page);
		FileInputStream is = null;
		try {
			is = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return toXLS(is);
	}
	
	
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getRecordSize() {
		return recordSize;
	}
	public void setRecordSize(int recordSize) {
		this.recordSize = recordSize;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	
	
}
