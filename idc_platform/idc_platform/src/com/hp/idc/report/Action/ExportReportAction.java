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
	
	//导出客户服务统计报告
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
	//导出客户服务清单报告
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
	//导出客户变动明细报告
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
	
	//导出客户变动统计报告
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
	//导出客户业务类别统计报告
	public void exportCustomerBussnessTypeCountReport() throws Exception{
		System.out.println("导出客户业务类别统计报告");	
	}
	//导出客户物品进入统计报告
	public void exportCustomerWupinjinruCountReport() throws Exception{
		System.out.println("导出客户物品进入统计报告");
	}
	
	
	//导出业务工单统计报告
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
	
	//导出业务变动统计报告
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
	//导出基本业务信息统计报告
	public String exportBasicBussnessInformationReport() throws Exception{
		Page page = new Page();
		page.setStartTime(beginDate);
		page.setEndTime(endDate);
		page.setLoginUser((String) ActionContext.getContext().getSession().get(Constant.SESSION_LOGIN));
		//统计报表类型：基本
		page.setReportType(0);
		page.setTitle("基本业务信息统计报告");
		
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
	//导出增值业务信息统计报告
	public String exportPriceBussnessInformationReport() throws Exception{
		Page page = new Page();
		page.setStartTime(beginDate);
		page.setEndTime(endDate);
		page.setLoginUser((String) ActionContext.getContext().getSession().get(Constant.SESSION_LOGIN));
		//统计报表类型：增值
		page.setReportType(1);
		page.setTitle("增值业务信息统计报告");
		
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
