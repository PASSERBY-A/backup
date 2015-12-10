package com.hp.idc.report.service;

import java.util.List;
import java.util.Map;

import com.hp.idc.report.util.Page;

import net.sf.json.JSONObject;

public interface ReportService {
	//客户服务统计报告
	JSONObject getCustomerFuwuCountReport(Page page);
	//客户服务清单报告
	JSONObject getCustomerQingdanCountReport(Page page);
	//客户变动明细报告
	JSONObject getCustomerBiandongmingxiCountReport(Page page);
	//客户变动统计报告
	JSONObject getCustomerBiandongtongjiCountReport(Page page);
	//客户业务类别统计报告
	void getCustomerBussnessTypeCountReport();
	//客户物品进入统计报告
	void getCustomerWupinjinruCountReport();
	
	
	
	//业务工单统计报告
	JSONObject getBussnessOrderCountReport(Page page);
	//业务变动统计报告
	JSONObject getBussnessChangeCountReport(Page page);
	//基本业务信息统计报告
	JSONObject getBasicBussnessInformationReport(Page page);
	//增值业务信息统计报告
	void getPriceBussnessInformationReport();
	
	
	//导出客户服务统计报告
	String exportCustomerFuwuCountReport(Page page);
	//导出客户服务清单报告
	String exportCustomerQingdanCountReport(Page page);
	//导出客户变动明细报告
	String exportCustomerBiandongmingxiCountReport(Page page);
	//导出客户变动统计报告
	String exportCustomerBiandongtongjiCountReport(Page page);
	//导出客户业务类别统计报告
	void exportCustomerBussnessTypeCountReport();
	//导出客户物品进入统计报告
	void exportCustomerWupinjinruCountReport();
	
	
	
	//导出业务工单统计报告
	String exportBussnessOrderCountReport(Page page);
	//导出业务变动统计报告
	String exportBussnessChangeCountReport(Page page);
	//导出基本业务信息统计报告
	String exportBasicBussnessInformationReport(Page page);
	//导出增值业务信息统计报告
	String exportPriceBussnessInformationReport();
	
}
