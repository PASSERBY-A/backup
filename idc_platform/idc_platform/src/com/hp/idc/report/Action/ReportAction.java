package com.hp.idc.report.Action;

import java.util.List;
import java.util.Map;


import org.jfree.chart.JFreeChart;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.category.DefaultCategoryDataset;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.hp.idc.report.excel.ChartUtil;
import com.hp.idc.report.excel.XlsWorkBook;
import com.hp.idc.report.service.ReportService;
import com.hp.idc.report.util.Page;
import com.opensymphony.xwork2.ActionContext;

public class ReportAction extends BaseAction {
	private int start;
	private int limit;
	private String startTime;
	private String endTime;
	private String path;
	private ChartUtil chartUtil = new ChartUtil();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 绘制业务工单统计图表
	 * 
	 * @return
	 */
	public String generateB1Chart() {
		Page page = new Page();
		page.setFirst(start + 1);
		page.setLast(start + limit);
		page.setStartTime(startTime);
		page.setEndTime(endTime);
		try {
			ReportService reportService = (ReportService) this
					.getBean("reportService");
			JSONObject JResults = reportService
					.getBussnessOrderCountReport(page);
			List<Map<String, Object>> results = (List<Map<String, Object>>) JResults
					.get("results");

			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			for (Map<String, Object> result : results) {

				// 组装图表数据集
				dataset.addValue(Double.parseDouble(String.valueOf(result
						.get("TOTAL_NUM"))), "总数", String.valueOf(result
						.get("FLD_CATEGORY")));
				dataset.addValue(Double.parseDouble(String.valueOf(result
						.get("FINISH_NUM"))), "完成数量", String.valueOf(result
						.get("FLD_CATEGORY")));
				dataset.addValue(Double.parseDouble(String.valueOf(result
						.get("OVER_TIME_NUM"))), "超时数量", String.valueOf(result
						.get("FLD_CATEGORY")));
			}

			// XlsWorkBook.deleteServerPic(req.getContextPath() + "/servlet/");
			JFreeChart chart = chartUtil.createBarChart("业务工单统计报告", "业务分类",
					"数量", "", dataset);

			String filename = ServletUtilities.saveChartAsPNG(chart, 530, 390,
					null);
			String graphURL = req.getContextPath()
					+ "/servlet/DisplayChart?filename=" + filename;
			req.setAttribute("graphURL", graphURL);
			// ChartUtilities.writeChartAsJPEG(res.getOutputStream(),chart,500,300);

		} catch (Exception e) {
		}

		return SUCCESS;
	}

	/**
	 * 绘制业务变动统计图表
	 * 
	 * @return
	 */
	public String generateB2Chart() {
		Page page = new Page();
		page.setFirst(start + 1);
		page.setLast(start + limit);
		page.setStartTime(startTime);
		page.setEndTime(endTime);
		try {
			ReportService reportService = (ReportService) this
					.getBean("reportService");
			JSONObject JResults = reportService
					.getBussnessChangeCountReport(page);
			List<Map<String, Object>> results = (List<Map<String, Object>>) JResults
					.get("results");

			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			for (Map<String, Object> result : results) {
				// 组装图表数据集
				dataset.addValue(Integer.parseInt(String.valueOf(result
						.get("TOTAL_NUM"))), "总量", String.valueOf(result
						.get("CREATE_MONTH")));
				dataset.addValue(
						Integer.parseInt(String.valueOf(result.get("ADD_NUM"))),
						"新增业务", String.valueOf(result.get("CREATE_MONTH")));
				dataset.addValue(Integer.parseInt(String.valueOf(result
						.get("UPDATE_NUM"))), "变更业务", String.valueOf(result
						.get("CREATE_MONTH")));
				dataset.addValue(
						Integer.parseInt(String.valueOf(result.get("END_NUM"))),
						"终止业务", String.valueOf(result.get("CREATE_MONTH")));
			}

			// XlsWorkBook.deleteServerPic(req.getContextPath() + "/servlet/");
			JFreeChart chart = chartUtil.createLineChart("业务变动统计报告", "", "",
					"", dataset);

			String filename = ServletUtilities.saveChartAsPNG(chart, 530, 390,
					null);
			String graphURL = req.getContextPath()
					+ "/servlet/DisplayChart?filename=" + filename;
			req.setAttribute("graphURL", graphURL);
			// ChartUtilities.writeChartAsJPEG(res.getOutputStream(),chart,500,300);

		} catch (Exception e) {
		}

		return SUCCESS;
	}

	/**
	 * 绘制基本业务信息统计图
	 * 
	 * @return
	 */
	public String generateB3Chart() {
		Page page = new Page();
		page.setFirst(start + 1);
		page.setLast(start + limit);
		page.setStartTime(startTime);
		page.setEndTime(endTime);
		// 统计报表类型：基本
		page.setReportType(0);
		try {

			ReportService reportService = (ReportService) this
					.getBean("reportService");
			JSONObject JResults = reportService
					.getBasicBussnessInformationReport(page);
			List<Map<String, Object>> results = (List<Map<String, Object>>) JResults
					.get("results");

			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			for (Map<String, Object> result : results) {
				////若数量为0，则该类别在图表中不显示
				//if (Double.parseDouble(String.valueOf(result.get("TOTAL_NUM"))) != 0) {
					// 组装图表数据集
					dataset.addValue(Double.parseDouble(String.valueOf(result
							.get("TOTAL_NUM"))), "数量", String.valueOf(result
							.get("FLD_CATEGORY")));
				//}

			}

			// // 画饼图
			// JFreeChart chart = XlsWorkBook.createValidityComparePimChar(pset,
			// "基本业务信息统计", "No data");
			// 画柱状图
			JFreeChart chart = chartUtil.createBarChart("基本业务信息统计", "业务类别",
					"数量", "", dataset);

			String filename = ServletUtilities.saveChartAsPNG(chart, 530, 390,
					null);
			String graphURL = req.getContextPath()
					+ "/servlet/DisplayChart?filename=" + filename;
			req.setAttribute("graphURL", graphURL);
			// ChartUtilities.writeChartAsJPEG(res.getOutputStream(),chart,500,300);

		} catch (Exception e) {
		}

		return SUCCESS;
	}

	/**
	 * 绘制增值业务信息统计图
	 * 
	 * @return
	 */
	public String generateB4Chart() {
		Page page = new Page();
		page.setFirst(start + 1);
		page.setLast(start + limit);
		page.setStartTime(startTime);
		page.setEndTime(endTime);
		// 统计报表类型：增值
		page.setReportType(1);
		try {

			ReportService reportService = (ReportService) this
					.getBean("reportService");
			JSONObject JResults = reportService
					.getBasicBussnessInformationReport(page);
			List<Map<String, Object>> results = (List<Map<String, Object>>) JResults
					.get("results");

			// DefaultPieDataset pset = new DefaultPieDataset();
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			for (Map<String, Object> result : results) {
				
				//if (Double.parseDouble(String.valueOf(result.get("TOTAL_NUM"))) != 0) {
					// 组装图表数据集
					dataset.addValue(Double.parseDouble(String.valueOf(result
							.get("TOTAL_NUM"))), "数量", String.valueOf(result
							.get("FLD_CATEGORY")));
				//}
			}

			// // 画饼图
			// JFreeChart chart = XlsWorkBook.createValidityComparePimChar(pset,
			// "增值业务信息统计", "No data");
			// 画柱状图
			JFreeChart chart = chartUtil.createBarChart("增值业务信息统计", "业务类别",
					"数量", "", dataset);

			String filename = ServletUtilities.saveChartAsPNG(chart, 530, 390,
					null);
			String graphURL = req.getContextPath()
					+ "/servlet/DisplayChart?filename=" + filename;
			req.setAttribute("graphURL", graphURL);
			// ChartUtilities.writeChartAsJPEG(res.getOutputStream(),chart,500,300);

		} catch (Exception e) {
		}

		return SUCCESS;
	}

	/**
	 * 绘制客户服务统计报告图
	 * 
	 * @return
	 */
	public String generateU1Chart() {
		Page page = new Page();
		page.setFirst(start + 1);
		page.setLast(start + limit);
		page.setStartTime(startTime);
		page.setEndTime(endTime);
		try {
			ReportService reportService = (ReportService) this
					.getBean("reportService");
			JSONObject JResults = reportService
					.getCustomerFuwuCountReport(page);
			List<Map<String, Object>> results = (List<Map<String, Object>>) JResults
					.get("results");

			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			for (Map<String, Object> result : results) {

				// 组装图表数据集
				dataset.addValue(Double.parseDouble(String.valueOf(result
						.get("SHOULICOUNT"))), "受理数量", String.valueOf(result
						.get("SERVICETYPE")));
				dataset.addValue(Integer.parseInt(String.valueOf(result
						.get("WANCHENGCOUNT"))), "完成数量", String.valueOf(result
						.get("SERVICETYPE")));
			}

			// 绘制柱状统计图
			// XlsWorkBook.deleteServerPic(req.getContextPath() + "/servlet/");
			JFreeChart chart = chartUtil.createBarChart("客户服务统计报告", "服务类别",
					"数量", "", dataset);

			String filename = ServletUtilities.saveChartAsPNG(chart, 530, 390,
					null);
			String graphURL = req.getContextPath()
					+ "/servlet/DisplayChart?filename=" + filename;
			req.setAttribute("graphURL", graphURL);

		} catch (Exception e) {
		}

		return SUCCESS;
	}

	/**
	 * 绘制客户变动统计报告图
	 * 
	 * @return
	 */
	public String generateU4Chart() {
		Page page = new Page();
		page.setFirst(start + 1);
		page.setLast(start + limit);
		page.setStartTime(startTime);
		page.setEndTime(endTime);
		try {
			ReportService reportService = (ReportService) this
					.getBean("reportService");
			JSONObject JResults = reportService
					.getCustomerBiandongtongjiCountReport(page);
			List<Map<String, Object>> results = (List<Map<String, Object>>) JResults
					.get("results");

			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			for (Map<String, Object> result : results) {

				// 组装图表数据集
				dataset.addValue(Double.parseDouble(String.valueOf(result
						.get("CUSTOMER_COUNT"))), "客户数量", String.valueOf(result
						.get("OPENTIME")));
				dataset.addValue(Double.parseDouble(String.valueOf(result
						.get("QIANZAI_COUNT"))), "新增客户", String.valueOf(result
						.get("OPENTIME")));
				dataset.addValue(Double.parseDouble(String.valueOf(result
						.get("ZHUXIAO_COUNT"))), "撤销客户", String.valueOf(result
						.get("OPENTIME")));
			}

			// 绘制柱状统计图
			// XlsWorkBook.deleteServerPic(req.getContextPath() + "/servlet/");
			JFreeChart chart = chartUtil.createLineChart("客戶变动统计报告", "", "",
					"", dataset);

			String filename = ServletUtilities.saveChartAsPNG(chart, 530, 390,
					null);
			String graphURL = req.getContextPath()
					+ "/servlet/DisplayChart?filename=" + filename;
			req.setAttribute("graphURL", graphURL);

		} catch (Exception e) {
		}

		return SUCCESS;
	}

	/*********************** 图表绘制方法END ******************************/
	// 客户服务统计报告
	public String getCustomerFuwuCountReport() throws Exception {
		Page page = new Page();
		page.setFirst(start + 1);
		page.setLast(start + limit);
		page.setStartTime(startTime);
		page.setEndTime(endTime);

		ReportService reportService = (ReportService) this
				.getBean("reportService");
		JSONObject JResults = reportService.getCustomerFuwuCountReport(page);
		List<Map<String, Object>> results = (List<Map<String, Object>>) JResults
				.get("results");

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		JSONObject jsonResult = new JSONObject();
		JSONArray ja = new JSONArray();
		for (Map<String, Object> result : results) {
			JSONObject temp = new JSONObject();
			temp.put("serviceType", result.get("SERVICETYPE"));
			temp.put("shoulicount", result.get("SHOULICOUNT"));
			temp.put("wanchengcount", result.get("WANCHENGCOUNT"));
			ja.add(temp);

			// 组装图表数据集
			dataset.addValue(temp.getDouble("shoulicount"), "受理数量",
					temp.getString("serviceType"));
			dataset.addValue(temp.getDouble("wanchengcount"), "完成数量",
					temp.getString("serviceType"));
		}
		// 绘制柱状统计图
		String url = this.getClass().getResource("").getPath()
				.replaceAll("%20", " ");
		String path = url.substring(0, url.indexOf("WEB-INF"))
				+ "report/style/temp1.jpeg";
		chartUtil.createBarChart("客户服务统计报告", "服务类别", "数量", path, dataset);

		jsonResult.put("results", ja);
		jsonResult.put("recordSize", JResults.get("count"));
		System.out.println(jsonResult.toString());
		return toSTR(jsonResult.toString());
	}

	// 客户服务清单报告
	public String getCustomerQingdanCountReport() throws Exception {
		Page page = new Page();
		page.setFirst(start + 1);
		page.setLast(start + limit);
		page.setStartTime(startTime);
		page.setEndTime(endTime);

		ReportService reportService = (ReportService) this
				.getBean("reportService");
		JSONObject JResults = reportService.getCustomerQingdanCountReport(page);
		List<Map<String, Object>> results = (List<Map<String, Object>>) JResults
				.get("results");
		JSONObject jsonResult = new JSONObject();
		JSONArray ja = new JSONArray();
		for (Map<String, Object> result : results) {
			JSONObject temp = new JSONObject();
			temp.put("tid", result.get("TID"));
			temp.put("updateTime", result.get("UPDATETIME"));
			temp.put("taskUser", result.get("TASKUSER"));
			temp.put("serviceType", result.get("SERVICETYPE"));
			temp.put("processresult", result.get("PROCESSRESULT"));
			temp.put("excuteUser", result.get("EXCUTEUSER"));
			ja.add(temp);
		}
		jsonResult.put("results", ja);
		jsonResult.put("recordSize", JResults.get("count"));
		System.out.println(jsonResult.toString());
		return toSTR(jsonResult.toString());
	}

	// 客户变动明细报告
	public String getCustomerBiandongmingxiCountReport() throws Exception {
		Page page = new Page();
		page.setFirst(start + 1);
		page.setLast(start + limit);
		page.setStartTime(startTime);
		page.setEndTime(endTime);

		ReportService reportService = (ReportService) this
				.getBean("reportService");
		JSONObject JResults = reportService
				.getCustomerBiandongmingxiCountReport(page);
		List<Map<String, Object>> results = (List<Map<String, Object>>) JResults
				.get("results");
		JSONObject jsonResult = new JSONObject();
		JSONArray ja = new JSONArray();
		for (Map<String, Object> result : results) {
			JSONObject temp = new JSONObject();
			temp.put("cid", result.get("CID"));
			temp.put("cname", result.get("CNAME"));
			temp.put("status", result.get("STATUS"));
			ja.add(temp);
		}
		jsonResult.put("results", ja);
		jsonResult.put("recordSize", JResults.get("count"));
		return toSTR(jsonResult.toString());
	}

	// 客户变动统计报告
	public String getCustomerBiandongtongjiCountReport() throws Exception {
		Page page = new Page();
		page.setFirst(start + 1);
		page.setLast(start + limit);
		page.setStartTime(startTime);
		page.setEndTime(endTime);

		ReportService reportService = (ReportService) this
				.getBean("reportService");
		JSONObject JResults = reportService
				.getCustomerBiandongtongjiCountReport(page);
		List<Map<String, Object>> results = (List<Map<String, Object>>) JResults
				.get("results");

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		JSONObject jsonResult = new JSONObject();
		JSONArray ja = new JSONArray();
		for (Map<String, Object> result : results) {
			JSONObject temp = new JSONObject();
			temp.put("openTime", result.get("OPENTIME"));
			temp.put("customer_count", result.get("CUSTOMER_COUNT"));
			temp.put("zhuxiao_count", result.get("ZHUXIAO_COUNT"));
			temp.put("qianzai_count", result.get("QIANZAI_COUNT"));
			ja.add(temp);

			// 组装图表数据集
			dataset.addValue(temp.getDouble("customer_count"), "客户数量",
					temp.getString("openTime"));
			dataset.addValue(temp.getDouble("qianzai_count"), "新增客户",
					temp.getString("openTime"));
			dataset.addValue(temp.getDouble("zhuxiao_count"), "撤销客户",
					temp.getString("openTime"));
		}

		// 绘制柱状统计图
		String url = this.getClass().getResource("").getPath()
				.replaceAll("%20", " ");
		String path = url.substring(0, url.indexOf("WEB-INF"))
				+ "report/style/temp1.jpeg";
		chartUtil.createLineChart("客戶变动统计报告", "", "", path, dataset);

		jsonResult.put("results", ja);
		jsonResult.put("recordSize", JResults.get("count"));

		return toSTR(jsonResult.toString());

	}

	/** delete */
	// //客户业务类别统计报告
	// public void getCustomerBussnessTypeCountReport() throws Exception{
	// System.out.println("客户业务类别统计报告");
	// }
	// //客户物品进入统计报告
	// public void getCustomerWupinjinruCountReport() throws Exception{
	// System.out.println("客户物品进入统计报告");
	// }
	/** delete end */

	// 业务工单统计报告 -Fancy
	public String getBussnessOrderCountReport() throws Exception {
		System.out.println("业务工单统计报告");
		Page page = new Page();
		page.setFirst(start + 1);
		page.setLast(start + limit);
		page.setStartTime(startTime);
		page.setEndTime(endTime);

		ReportService reportService = (ReportService) this
				.getBean("reportService");
		JSONObject JResults = reportService.getBussnessOrderCountReport(page);
		List<Map<String, Object>> results = (List<Map<String, Object>>) JResults
				.get("results");
		JSONObject jsonResult = new JSONObject();
		JSONArray ja = new JSONArray();
		String a;
		// DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (Map<String, Object> result : results) {
			JSONObject temp = new JSONObject();

			temp.put("category", result.get("FLD_CATEGORY"));
			temp.put("total_num", result.get("TOTAL_NUM"));
			temp.put("finish_num", result.get("FINISH_NUM"));
			temp.put("over_time_num", result.get("OVER_TIME_NUM"));
			ja.add(temp);

			// // 组装图表数据集
			// dataset.addValue(temp.getDouble("total_num"), "总数",
			// temp.getString("category"));
			// dataset.addValue(temp.getDouble("finish_num"), "完成数量",
			// temp.getString("category"));
			// dataset.addValue(temp.getDouble("over_time_num"), "超时数量",
			// temp.getString("category"));
		}

		// 绘制柱状统计图
		// String url = this.getClass().getResource("").getPath()
		// .replaceAll("%20", " ");
		// String path = url.substring(0, url.indexOf("WEB-INF"))
		// + "report/style/temp1.jpeg";
		// chartUtil.createBarChart("业务工单统计报告", "业务分类", "数量", path, dataset);
		// this.path = "../style/temp1.jpeg";
		jsonResult.put("results", ja);
		jsonResult.put("recordSize", JResults.get("count"));

		return toSTR(jsonResult.toString());
	}

	// 业务变动统计报告 --Fancy
	public String getBussnessChangeCountReport() throws Exception {
		Page page = new Page();
		page.setFirst(start + 1);
		page.setLast(start + limit);
		page.setStartTime(startTime);
		page.setEndTime(endTime);

		ReportService reportService = (ReportService) this
				.getBean("reportService");
		JSONObject JResults = reportService.getBussnessChangeCountReport(page);
		List<Map<String, Object>> results = (List<Map<String, Object>>) JResults
				.get("results");
		// DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		JSONObject jsonResult = new JSONObject();
		JSONArray ja = new JSONArray();

		for (Map<String, Object> result : results) {
			JSONObject temp = new JSONObject();

			temp.put("create_month", result.get("CREATE_MONTH"));
			temp.put("total_num", result.get("TOTAL_NUM"));
			temp.put("add_num", result.get("ADD_NUM"));
			temp.put("update_num", result.get("UPDATE_NUM"));
			temp.put("end_num", result.get("END_NUM"));
			ja.add(temp);

			// // 组装图表数据集
			// dataset.addValue(temp.getInt("total_num"), "总量",
			// temp.getString("create_month"));
			// dataset.addValue(temp.getInt("add_num"), "新增业务",
			// temp.getString("create_month"));
			// dataset.addValue(temp.getInt("update_num"), "变更业务",
			// temp.getString("create_month"));
			// dataset.addValue(temp.getInt("end_num"), "终止业务",
			// temp.getString("create_month"));
		}
		// // 绘制折线统计图
		// String url = this.getClass().getResource("").getPath()
		// .replaceAll("%20", " ");
		// String path = url.substring(0, url.indexOf("WEB-INF"))
		// + "report/style/temp1.jpeg";
		// chartUtil.createLineChart("业务变动统计报告", "", "", path, dataset);

		jsonResult.put("results", ja);
		jsonResult.put("recordSize", JResults.get("count"));

		return toSTR(jsonResult.toString());
	}

	// 基本业务信息统计报告 --Fancy
	public String getBasicBussnessInformationReport() throws Exception {
		Page page = new Page();
		page.setFirst(start + 1);
		page.setLast(start + limit);
		page.setStartTime(startTime);
		page.setEndTime(endTime);
		// 统计报表类型：基本
		page.setReportType(0);

		ReportService reportService = (ReportService) this
				.getBean("reportService");
		JSONObject JResults = reportService
				.getBasicBussnessInformationReport(page);
		List<Map<String, Object>> results = (List<Map<String, Object>>) JResults
				.get("results");
		JSONObject jsonResult = new JSONObject();
		// DefaultPieDataset pset = new DefaultPieDataset();
		JSONArray ja = new JSONArray();

		for (Map<String, Object> result : results) {
			JSONObject temp = new JSONObject();

			temp.put("category", result.get("FLD_CATEGORY"));
			temp.put("total_num", result.get("TOTAL_NUM"));
			// // 组装图表数据源
			// pset.setValue(String.valueOf(result.get("FLD_CATEGORY")),
			// Integer.parseInt(String.valueOf(result.get("TOTAL_NUM"))));
			ja.add(temp);
		}

		// /** 组装JFreechart图表 */
		// // 画饼图
		// JFreeChart chart = XlsWorkBook.createValidityComparePimChar(pset, "",
		// "No data");
		// // File file = new File("c:/temp1.jpeg");
		//
		// // ChartUtilities.saveChartAsPNG(file, chart, 500, 300);
		// String url = this.getClass().getResource("").getPath()
		// .replaceAll("%20", " ");
		// String path = url.substring(0, url.indexOf("WEB-INF")) +
		// "report/style";
		// // String path = System.getProperty( "java.io.tmpdir ");
		// // System.out.print(path);
		// // System.out.print(req.getContextPath());
		// OutputStream os = new FileOutputStream(path + "/temp1.jpeg");
		// XlsWorkBook.deleteServerPic(path);
		// // 由ChartUtilities生成文件到一个体outputStream中去
		// ChartUtilities.writeChartAsJPEG(os, chart, 800, 600);
		// os.flush();
		// os.close();
		// this.path = path + "/temp1.jpeg";
		// req.setAttribute("graphURL", path + "/temp1.jpeg");
		jsonResult.put("results", ja);
		jsonResult.put("recordSize", JResults.get("count"));

		// //////////////////
		// 创建一个柱状图 方法2

		// JFreeChart chart = XlsWorkBook.createValidityComparePimChar(pset,
		// "基本业务信息统计报表", "No data");
		// try {
		// // ActionContext ctx = ActionContext.getContext();
		// // HttpSession session =(HttpSession) ctx.getSession();
		// String filename = ServletUtilities.saveChartAsPNG(chart, 500, 300,
		// null, req.getSession());
		// String graphURL = req.getContextPath()
		// + "/servlet/DisplayChart?filename=" + filename;
		// req.setAttribute("graphURL", graphURL);

		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		return toSTR(jsonResult.toString());
	}

	// 增值业务信息统计报告 --Fancy
	public String getPriceBussnessInformationReport() throws Exception {
		Page page = new Page();
		page.setFirst(start + 1);
		page.setLast(start + limit);
		page.setStartTime(startTime);
		page.setEndTime(endTime);
		// 统计报表类型：增值业务
		page.setReportType(1);

		// DefaultPieDataset pset = new DefaultPieDataset();
		JSONObject jsonResult = new JSONObject();
		JSONArray ja = new JSONArray();
		// OutputStream os = null;

		try {
			ReportService reportService = (ReportService) this
					.getBean("reportService");
			JSONObject JResults = reportService
					.getBasicBussnessInformationReport(page);

			List<Map<String, Object>> results = (List<Map<String, Object>>) JResults
					.get("results");

			for (Map<String, Object> result : results) {
				JSONObject temp = new JSONObject();
				// // 组装图表数据源
				// pset.setValue(
				// String.valueOf(result.get("FLD_CATEGORY")),
				// Integer.parseInt(String.valueOf(result.get("TOTAL_NUM"))));

				temp.put("category", result.get("FLD_CATEGORY"));
				temp.put("total_num", result.get("TOTAL_NUM"));
				ja.add(temp);
			}
			// /** 组装JFreechart图表 */
			// // 画饼图
			// JFreeChart chart = XlsWorkBook.createValidityComparePimChar(pset,
			// "增值业务信息统计", "No data");
			// // File file = new File("c:/temp1.jpeg");
			//
			// // ChartUtilities.saveChartAsPNG(file, chart, 500, 300);
			// String url = this.getClass().getResource("").getPath()
			// .replaceAll("%20", " ");
			// String path = url.substring(0, url.indexOf("WEB-INF"))
			// + "report/style";
			// os = new FileOutputStream(path + "/temp1.jpeg");
			// XlsWorkBook.deleteServerPic(path);
			// // 由ChartUtilities生成文件到一个体outputStream中去
			// ChartUtilities.writeChartAsJPEG(os, chart, 800, 600);
			//
			// os.flush();

			jsonResult.put("results", ja);
			jsonResult.put("recordSize", JResults.get("count"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// if (os != null) {
			// os.close();
			// }
		}
		return toSTR(jsonResult.toString());
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

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
