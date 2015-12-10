package com.hp.idc.report.dao;

import java.util.List;
import java.util.Map;

import com.hp.idc.report.util.Page;

import net.sf.json.JSONObject;

public interface ReportDao {
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
	JSONObject getCustomerWupinjinruCountReport(Page page);
	
	
	
	//业务工单统计报告
	JSONObject getBussnessOrderCountReport(Page page);
	//业务变动统计报告
	JSONObject getBussnessChangeCountReport(Page page);
	//基本业务信息统计报告
	JSONObject getBasicBussnessInformationReport(Page page);
	//增值业务信息统计报告
	List<Map<String, Object>> getPriceBussnessInformationReport();
	
	
	//导出客户服务统计报告
	List<Map<String, Object>> exportCustomerFuwuCountReport(Page page);
	//导出客户服务清单报告
	List<Map<String, Object>> exportCustomerQingdanCountReport(Page page);
	//导出客户变动明细报告
	List<Map<String, Object>> exportCustomerBiandongmingxiCountReport(Page page);
	//导出客户变动统计报告
	List<Map<String, Object>> exportCustomerBiandongtongjiCountReport(Page page);
	//导出客户业务类别统计报告
	List<Map<String, Object>> exportCustomerBussnessTypeCountReport(Page page);
	//导出客户物品进入统计报告
	List<Map<String, Object>> exportCustomerWupinjinruCountReport(Page page);
	
	
	
	//导出业务工单统计报告
	List<Map<String, Object>> exportBussnessOrderCountReport(Page page);
	//导出业务变动统计报告
	List<Map<String, Object>> exportBussnessChangeCountReport(Page page);
	//导出基本业务信息统计报告
	List<Map<String, Object>> exportBasicBussnessInformationReport(Page page);
	//导出增值业务信息统计报告
	List<Map<String, Object>> exportPriceBussnessInformationReport(Page page);
	
	
	
}
