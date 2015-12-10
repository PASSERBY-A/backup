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
	 * ����ҵ�񹤵�ͳ��ͼ��
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

				// ��װͼ�����ݼ�
				dataset.addValue(Double.parseDouble(String.valueOf(result
						.get("TOTAL_NUM"))), "����", String.valueOf(result
						.get("FLD_CATEGORY")));
				dataset.addValue(Double.parseDouble(String.valueOf(result
						.get("FINISH_NUM"))), "�������", String.valueOf(result
						.get("FLD_CATEGORY")));
				dataset.addValue(Double.parseDouble(String.valueOf(result
						.get("OVER_TIME_NUM"))), "��ʱ����", String.valueOf(result
						.get("FLD_CATEGORY")));
			}

			// XlsWorkBook.deleteServerPic(req.getContextPath() + "/servlet/");
			JFreeChart chart = chartUtil.createBarChart("ҵ�񹤵�ͳ�Ʊ���", "ҵ�����",
					"����", "", dataset);

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
	 * ����ҵ��䶯ͳ��ͼ��
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
				// ��װͼ�����ݼ�
				dataset.addValue(Integer.parseInt(String.valueOf(result
						.get("TOTAL_NUM"))), "����", String.valueOf(result
						.get("CREATE_MONTH")));
				dataset.addValue(
						Integer.parseInt(String.valueOf(result.get("ADD_NUM"))),
						"����ҵ��", String.valueOf(result.get("CREATE_MONTH")));
				dataset.addValue(Integer.parseInt(String.valueOf(result
						.get("UPDATE_NUM"))), "���ҵ��", String.valueOf(result
						.get("CREATE_MONTH")));
				dataset.addValue(
						Integer.parseInt(String.valueOf(result.get("END_NUM"))),
						"��ֹҵ��", String.valueOf(result.get("CREATE_MONTH")));
			}

			// XlsWorkBook.deleteServerPic(req.getContextPath() + "/servlet/");
			JFreeChart chart = chartUtil.createLineChart("ҵ��䶯ͳ�Ʊ���", "", "",
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
	 * ���ƻ���ҵ����Ϣͳ��ͼ
	 * 
	 * @return
	 */
	public String generateB3Chart() {
		Page page = new Page();
		page.setFirst(start + 1);
		page.setLast(start + limit);
		page.setStartTime(startTime);
		page.setEndTime(endTime);
		// ͳ�Ʊ������ͣ�����
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
				////������Ϊ0����������ͼ���в���ʾ
				//if (Double.parseDouble(String.valueOf(result.get("TOTAL_NUM"))) != 0) {
					// ��װͼ�����ݼ�
					dataset.addValue(Double.parseDouble(String.valueOf(result
							.get("TOTAL_NUM"))), "����", String.valueOf(result
							.get("FLD_CATEGORY")));
				//}

			}

			// // ����ͼ
			// JFreeChart chart = XlsWorkBook.createValidityComparePimChar(pset,
			// "����ҵ����Ϣͳ��", "No data");
			// ����״ͼ
			JFreeChart chart = chartUtil.createBarChart("����ҵ����Ϣͳ��", "ҵ�����",
					"����", "", dataset);

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
	 * ������ֵҵ����Ϣͳ��ͼ
	 * 
	 * @return
	 */
	public String generateB4Chart() {
		Page page = new Page();
		page.setFirst(start + 1);
		page.setLast(start + limit);
		page.setStartTime(startTime);
		page.setEndTime(endTime);
		// ͳ�Ʊ������ͣ���ֵ
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
					// ��װͼ�����ݼ�
					dataset.addValue(Double.parseDouble(String.valueOf(result
							.get("TOTAL_NUM"))), "����", String.valueOf(result
							.get("FLD_CATEGORY")));
				//}
			}

			// // ����ͼ
			// JFreeChart chart = XlsWorkBook.createValidityComparePimChar(pset,
			// "��ֵҵ����Ϣͳ��", "No data");
			// ����״ͼ
			JFreeChart chart = chartUtil.createBarChart("��ֵҵ����Ϣͳ��", "ҵ�����",
					"����", "", dataset);

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
	 * ���ƿͻ�����ͳ�Ʊ���ͼ
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

				// ��װͼ�����ݼ�
				dataset.addValue(Double.parseDouble(String.valueOf(result
						.get("SHOULICOUNT"))), "��������", String.valueOf(result
						.get("SERVICETYPE")));
				dataset.addValue(Integer.parseInt(String.valueOf(result
						.get("WANCHENGCOUNT"))), "�������", String.valueOf(result
						.get("SERVICETYPE")));
			}

			// ������״ͳ��ͼ
			// XlsWorkBook.deleteServerPic(req.getContextPath() + "/servlet/");
			JFreeChart chart = chartUtil.createBarChart("�ͻ�����ͳ�Ʊ���", "�������",
					"����", "", dataset);

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
	 * ���ƿͻ��䶯ͳ�Ʊ���ͼ
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

				// ��װͼ�����ݼ�
				dataset.addValue(Double.parseDouble(String.valueOf(result
						.get("CUSTOMER_COUNT"))), "�ͻ�����", String.valueOf(result
						.get("OPENTIME")));
				dataset.addValue(Double.parseDouble(String.valueOf(result
						.get("QIANZAI_COUNT"))), "�����ͻ�", String.valueOf(result
						.get("OPENTIME")));
				dataset.addValue(Double.parseDouble(String.valueOf(result
						.get("ZHUXIAO_COUNT"))), "�����ͻ�", String.valueOf(result
						.get("OPENTIME")));
			}

			// ������״ͳ��ͼ
			// XlsWorkBook.deleteServerPic(req.getContextPath() + "/servlet/");
			JFreeChart chart = chartUtil.createLineChart("�͑��䶯ͳ�Ʊ���", "", "",
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

	/*********************** ͼ����Ʒ���END ******************************/
	// �ͻ�����ͳ�Ʊ���
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

			// ��װͼ�����ݼ�
			dataset.addValue(temp.getDouble("shoulicount"), "��������",
					temp.getString("serviceType"));
			dataset.addValue(temp.getDouble("wanchengcount"), "�������",
					temp.getString("serviceType"));
		}
		// ������״ͳ��ͼ
		String url = this.getClass().getResource("").getPath()
				.replaceAll("%20", " ");
		String path = url.substring(0, url.indexOf("WEB-INF"))
				+ "report/style/temp1.jpeg";
		chartUtil.createBarChart("�ͻ�����ͳ�Ʊ���", "�������", "����", path, dataset);

		jsonResult.put("results", ja);
		jsonResult.put("recordSize", JResults.get("count"));
		System.out.println(jsonResult.toString());
		return toSTR(jsonResult.toString());
	}

	// �ͻ������嵥����
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

	// �ͻ��䶯��ϸ����
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

	// �ͻ��䶯ͳ�Ʊ���
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

			// ��װͼ�����ݼ�
			dataset.addValue(temp.getDouble("customer_count"), "�ͻ�����",
					temp.getString("openTime"));
			dataset.addValue(temp.getDouble("qianzai_count"), "�����ͻ�",
					temp.getString("openTime"));
			dataset.addValue(temp.getDouble("zhuxiao_count"), "�����ͻ�",
					temp.getString("openTime"));
		}

		// ������״ͳ��ͼ
		String url = this.getClass().getResource("").getPath()
				.replaceAll("%20", " ");
		String path = url.substring(0, url.indexOf("WEB-INF"))
				+ "report/style/temp1.jpeg";
		chartUtil.createLineChart("�͑��䶯ͳ�Ʊ���", "", "", path, dataset);

		jsonResult.put("results", ja);
		jsonResult.put("recordSize", JResults.get("count"));

		return toSTR(jsonResult.toString());

	}

	/** delete */
	// //�ͻ�ҵ�����ͳ�Ʊ���
	// public void getCustomerBussnessTypeCountReport() throws Exception{
	// System.out.println("�ͻ�ҵ�����ͳ�Ʊ���");
	// }
	// //�ͻ���Ʒ����ͳ�Ʊ���
	// public void getCustomerWupinjinruCountReport() throws Exception{
	// System.out.println("�ͻ���Ʒ����ͳ�Ʊ���");
	// }
	/** delete end */

	// ҵ�񹤵�ͳ�Ʊ��� -Fancy
	public String getBussnessOrderCountReport() throws Exception {
		System.out.println("ҵ�񹤵�ͳ�Ʊ���");
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

			// // ��װͼ�����ݼ�
			// dataset.addValue(temp.getDouble("total_num"), "����",
			// temp.getString("category"));
			// dataset.addValue(temp.getDouble("finish_num"), "�������",
			// temp.getString("category"));
			// dataset.addValue(temp.getDouble("over_time_num"), "��ʱ����",
			// temp.getString("category"));
		}

		// ������״ͳ��ͼ
		// String url = this.getClass().getResource("").getPath()
		// .replaceAll("%20", " ");
		// String path = url.substring(0, url.indexOf("WEB-INF"))
		// + "report/style/temp1.jpeg";
		// chartUtil.createBarChart("ҵ�񹤵�ͳ�Ʊ���", "ҵ�����", "����", path, dataset);
		// this.path = "../style/temp1.jpeg";
		jsonResult.put("results", ja);
		jsonResult.put("recordSize", JResults.get("count"));

		return toSTR(jsonResult.toString());
	}

	// ҵ��䶯ͳ�Ʊ��� --Fancy
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

			// // ��װͼ�����ݼ�
			// dataset.addValue(temp.getInt("total_num"), "����",
			// temp.getString("create_month"));
			// dataset.addValue(temp.getInt("add_num"), "����ҵ��",
			// temp.getString("create_month"));
			// dataset.addValue(temp.getInt("update_num"), "���ҵ��",
			// temp.getString("create_month"));
			// dataset.addValue(temp.getInt("end_num"), "��ֹҵ��",
			// temp.getString("create_month"));
		}
		// // ��������ͳ��ͼ
		// String url = this.getClass().getResource("").getPath()
		// .replaceAll("%20", " ");
		// String path = url.substring(0, url.indexOf("WEB-INF"))
		// + "report/style/temp1.jpeg";
		// chartUtil.createLineChart("ҵ��䶯ͳ�Ʊ���", "", "", path, dataset);

		jsonResult.put("results", ja);
		jsonResult.put("recordSize", JResults.get("count"));

		return toSTR(jsonResult.toString());
	}

	// ����ҵ����Ϣͳ�Ʊ��� --Fancy
	public String getBasicBussnessInformationReport() throws Exception {
		Page page = new Page();
		page.setFirst(start + 1);
		page.setLast(start + limit);
		page.setStartTime(startTime);
		page.setEndTime(endTime);
		// ͳ�Ʊ������ͣ�����
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
			// // ��װͼ������Դ
			// pset.setValue(String.valueOf(result.get("FLD_CATEGORY")),
			// Integer.parseInt(String.valueOf(result.get("TOTAL_NUM"))));
			ja.add(temp);
		}

		// /** ��װJFreechartͼ�� */
		// // ����ͼ
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
		// // ��ChartUtilities�����ļ���һ����outputStream��ȥ
		// ChartUtilities.writeChartAsJPEG(os, chart, 800, 600);
		// os.flush();
		// os.close();
		// this.path = path + "/temp1.jpeg";
		// req.setAttribute("graphURL", path + "/temp1.jpeg");
		jsonResult.put("results", ja);
		jsonResult.put("recordSize", JResults.get("count"));

		// //////////////////
		// ����һ����״ͼ ����2

		// JFreeChart chart = XlsWorkBook.createValidityComparePimChar(pset,
		// "����ҵ����Ϣͳ�Ʊ���", "No data");
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

	// ��ֵҵ����Ϣͳ�Ʊ��� --Fancy
	public String getPriceBussnessInformationReport() throws Exception {
		Page page = new Page();
		page.setFirst(start + 1);
		page.setLast(start + limit);
		page.setStartTime(startTime);
		page.setEndTime(endTime);
		// ͳ�Ʊ������ͣ���ֵҵ��
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
				// // ��װͼ������Դ
				// pset.setValue(
				// String.valueOf(result.get("FLD_CATEGORY")),
				// Integer.parseInt(String.valueOf(result.get("TOTAL_NUM"))));

				temp.put("category", result.get("FLD_CATEGORY"));
				temp.put("total_num", result.get("TOTAL_NUM"));
				ja.add(temp);
			}
			// /** ��װJFreechartͼ�� */
			// // ����ͼ
			// JFreeChart chart = XlsWorkBook.createValidityComparePimChar(pset,
			// "��ֵҵ����Ϣͳ��", "No data");
			// // File file = new File("c:/temp1.jpeg");
			//
			// // ChartUtilities.saveChartAsPNG(file, chart, 500, 300);
			// String url = this.getClass().getResource("").getPath()
			// .replaceAll("%20", " ");
			// String path = url.substring(0, url.indexOf("WEB-INF"))
			// + "report/style";
			// os = new FileOutputStream(path + "/temp1.jpeg");
			// XlsWorkBook.deleteServerPic(path);
			// // ��ChartUtilities�����ļ���һ����outputStream��ȥ
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
