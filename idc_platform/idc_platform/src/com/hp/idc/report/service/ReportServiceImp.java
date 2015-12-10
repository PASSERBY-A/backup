package com.hp.idc.report.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.tika.io.ByteArrayOutputStream;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.hp.idc.report.dao.ReportDaoImp;
import com.hp.idc.report.excel.ChartUtil;
import com.hp.idc.report.excel.XlsWorkBook;
import com.hp.idc.report.util.Page;

public class ReportServiceImp implements ReportService {

	private ReportDaoImp reportDao;

	public ReportDaoImp getReportDao() {
		return reportDao;
	}

	public void setReportDao(ReportDaoImp reportDao) {
		this.reportDao = reportDao;
	}

	// 导出基本业务信息统计报告
	@Override
	public String exportBasicBussnessInformationReport(Page page) {
		List<Map<String, Object>> results = reportDao
				.exportBasicBussnessInformationReport(page);
		JSONObject jsonResult = new JSONObject();
		JSONArray ja = new JSONArray();
		for (Map<String, Object> result : results) {
			JSONObject temp = new JSONObject();
			temp.put("bname", result.get("FLD_CATEGORY"));
			temp.put("count", result.get("TOTAL_NUM"));
			ja.add(temp);
		}
		jsonResult.put("rows", ja);
		final String[] headers = { "服务类型", "数量" };
		String[] keys = { "bname", "count" };
		Object[] types = { 20, 20 };

		// XlsWorkBook xls = new XlsWorkBook();
		// try {
		// xls.addHead("基本业务信息统计报告", headers);
		// } catch (Exception e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// xls.addContent(jsonResult.getJSONArray("rows"), keys, types);
		// Workbook wb = xls.getXlsWorkBook("E:/test.xls");
		//
		// Calendar cal = Calendar.getInstance();
		// cal.setTime(new Date());
		// Integer name = cal.get(Calendar.MILLISECOND);
		// URL path = this.getClass().getClassLoader().getResource("");
		// String file = path.toString().substring(6).replaceAll("%20", "\\ ")
		// + "../xls/" + name + ".xls";
		// FileOutputStream out;
		// try {
		// out = new FileOutputStream(file);
		// wb.write(out);
		// out.close();
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		/** 导出 */
		String file = null;
		FileOutputStream out = null;
		try {
			// 获取模板路径
			String url = this.getClass().getResource("").getPath()
					.replaceAll("%20", " ");
			String tempPath = url.substring(0, url.indexOf("WEB-INF"))
					+ "report/style";
			// 读取导出excel模板
			XlsWorkBook xls = new XlsWorkBook();
			HSSFWorkbook wb = xls.readExcel(tempPath + "/report3.xls");

			// 往excel表中写入数据和图表
			wb = xls.exportBasicBussnessInformationReport(wb, page.getTitle(),
					jsonResult.getJSONArray("rows"), keys, page.getLoginUser());

			// 生成excel
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());

			Integer name = cal.get(Calendar.MILLISECOND);
			URL path = this.getClass().getClassLoader().getResource("");
			file = path.toString().substring(6).replaceAll("%20", "\\ ")
					+ "../xls/" + name + ".xls";
			out = new FileOutputStream(file);
			wb.write(out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {

				}
			}
		}
		return file;
	}

	// 导出业务变动统计报表
	@Override
	public String exportBussnessChangeCountReport(Page page) {
		List<Map<String, Object>> results = reportDao
				.exportBussnessChangeCountReport(page);
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
		}
		jsonResult.put("rows", ja);
		final String[] headers = { "时间", "业务总量", "新增业务", "变更业务", "终止业务" };
		String[] keys = { "create_month", "total_num", "add_num", "update_num",
				"end_num" };

		/** 导出 */
		String file = null;
		FileOutputStream out = null;
		try {
			// 获取模板路径
			String url = this.getClass().getResource("").getPath()
					.replaceAll("%20", " ");
			String tempPath = url.substring(0, url.indexOf("WEB-INF"))
					+ "report/style";
			// 读取导出excel模板
			XlsWorkBook xls = new XlsWorkBook();
			HSSFWorkbook wb = xls.readExcel(tempPath + "/report2.xls");

			// 往excel表中写入数据和图表
			wb = xls.exportCustomerBiandongtongji(wb, "业务变动统计报告",
					jsonResult.getJSONArray("rows"), keys, page.getLoginUser());

			// 生成excel
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());

			Integer name = cal.get(Calendar.MILLISECOND);
			URL path = this.getClass().getClassLoader().getResource("");
			file = path.toString().substring(6).replaceAll("%20", "\\ ")
					+ "../xls/" + name + ".xls";
			out = new FileOutputStream(file);
			wb.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;

	}

	// 导出业务工单统计报告
	@Override
	public String exportBussnessOrderCountReport(Page page) {
		List<Map<String, Object>> results = reportDao
				.exportBussnessOrderCountReport(page);
		JSONObject jsonResult = new JSONObject();
		JSONArray ja = new JSONArray();
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (Map<String, Object> result : results) {
			JSONObject temp = new JSONObject();
			temp.put("category", result.get("FLD_CATEGORY") == null ? ""
					: result.get("FLD_CATEGORY"));
			temp.put("total_num", result.get("TOTAL_NUM"));
			temp.put("finish_num", result.get("FINISH_NUM"));
			temp.put("over_time_num", result.get("OVER_TIME_NUM"));
			ja.add(temp);
			
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
		jsonResult.put("rows", ja);
		final String[] headers = { "业务类别", "受理数量", "完成数量", "超时" };
		String[] keys = { "category", "total_num", "finish_num",
				"over_time_num" };
		Object[] types = { 20, 20, 20, 20 };

		/** 导出 */
		XlsWorkBook xls = new XlsWorkBook("业务工单统计");
		String file = null;
		FileOutputStream out = null;
		try {
			xls.addHead("业务工单统计报告", headers, page.getLoginUser());

			xls.addContent(jsonResult.getJSONArray("rows"), keys, types);
			Workbook wb = xls.getWorkbook();

			// 导出柱状图
			ChartUtil chartUtil = new ChartUtil();

			JFreeChart chart = chartUtil.createBarChart("业务工单统计报告", "业务分类",
					"数量", "", dataset);

			String url = this.getClass().getResource("").getPath()
					.replaceAll("%20", " ");
			String picPath = url.substring(0, url.indexOf("WEB-INF"))
					+ "report/style";
			OutputStream os = new FileOutputStream(picPath + "/temp1.jpeg");

			// 由ChartUtilities生成文件到一个体outputStream中去
			ChartUtilities.writeChartAsJPEG(os, chart, 900, 700);

			os.close();

			// 处理图片文件，以便产生ByteArray
			ByteArrayOutputStream handlePicture = new ByteArrayOutputStream();
			handlePicture = XlsWorkBook.handlePicture(picPath + "/temp1.jpeg");

			HSSFSheet r_sheet = (HSSFSheet) wb.getSheetAt(0);
			HSSFPatriarch patriarch = r_sheet.createDrawingPatriarch();
			HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 100, 50,
					(short) 5, 11, (short) 11, 25);
			// 插入图片
			patriarch.createPicture(anchor,
					wb.addPicture(handlePicture.toByteArray(),
							HSSFWorkbook.PICTURE_TYPE_JPEG));
			/*****************/
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			Integer name = cal.get(Calendar.MILLISECOND);
			URL path = this.getClass().getClassLoader().getResource("");
			file = path.toString().substring(6).replaceAll("%20", "\\ ")
					+ "../xls/" + name + ".xls";

			out = new FileOutputStream(file);
			wb.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {

				}
			}
		}
		return file;

	}

	// 导出客户变动明细报告
	@Override
	public String exportCustomerBiandongmingxiCountReport(Page page) {
		List<Map<String, Object>> results = reportDao
				.exportCustomerBiandongmingxiCountReport(page);
		JSONObject jsonResult = new JSONObject();
		JSONArray ja = new JSONArray();
		for (Map<String, Object> result : results) {
			JSONObject temp = new JSONObject();
			temp.put("cid", result.get("CID"));
			temp.put("cname", result.get("CNAME"));
			temp.put("status", result.get("STATUS"));
			ja.add(temp);
		}
		jsonResult.put("rows", ja);
		final String[] headers = { "客户编号", "客户名称", "客户类别" };
		String[] keys = { "cid", "cname", "status" };
		Object[] types = { 20, 20, 20 };

		XlsWorkBook xls = new XlsWorkBook();
		try {
			xls.addHead("客户变动明细报告", headers, page.getLoginUser());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		xls.addContent(jsonResult.getJSONArray("rows"), keys, types);
		Workbook wb = xls.getWorkbook();

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		Integer name = cal.get(Calendar.MILLISECOND);
		URL path = this.getClass().getClassLoader().getResource("");
		String file = path.toString().substring(6).replaceAll("%20", "\\ ")
				+ "../xls/" + name + ".xls";
		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
			wb.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	// 导出客户变动统计报告
	@Override
	public String exportCustomerBiandongtongjiCountReport(Page page) {
		List<Map<String, Object>> results = reportDao
				.exportCustomerBiandongtongjiCountReport(page);
		JSONObject jsonResult = new JSONObject();
		JSONArray ja = new JSONArray();
		for (Map<String, Object> result : results) {
			JSONObject temp = new JSONObject();
			temp.put("openTime", result.get("OPENTIME"));
			temp.put("customer_count", result.get("CUSTOMER_COUNT"));
			temp.put("qianzai_count", result.get("QIANZAI_COUNT"));
			temp.put("zhuxiao_count", result.get("ZHUXIAO_COUNT"));
			
			ja.add(temp);
		}
		jsonResult.put("rows", ja);
		final String[] headers = { "时间", "客户数量", "新增用户", "撤销用户" };
		String[] keys = { "openTime", "customer_count", "qianzai_count",
				"zhuxiao_count" };

		/** 导出 */
		String file = null;
		FileOutputStream out = null;
		try {
			XlsWorkBook xls = new XlsWorkBook();
			String url = this.getClass().getResource("").getPath()
					.replaceAll("%20", " ");
			String tempPath = url.substring(0, url.indexOf("WEB-INF"))
					+ "report/style";
			HSSFWorkbook wb = xls.readExcel(tempPath + "/report1.xls");
			wb = xls.exportCustomerBiandongtongji(wb, "客户变动统计报告",
					jsonResult.getJSONArray("rows"), keys, page.getLoginUser());
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());

			Integer name = cal.get(Calendar.MILLISECOND);
			URL path = this.getClass().getClassLoader().getResource("");
			file = path.toString().substring(6).replaceAll("%20", "\\ ")
					+ "../xls/" + name + ".xls";
			out = new FileOutputStream(file);
			wb.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;

	}

	@Override
	public void exportCustomerBussnessTypeCountReport() {
		// TODO Auto-generated method stub

	}

	// 导出客户服务统计报告
	@Override
	public String exportCustomerFuwuCountReport(Page page) {
		List<Map<String, Object>> results = reportDao
				.exportCustomerFuwuCountReport(page);
		JSONObject jsonResult = new JSONObject();
		JSONArray ja = new JSONArray();
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (Map<String, Object> result : results) {
			JSONObject temp = new JSONObject();
			temp.put("servicetype", result.get("SERVICETYPE"));
			temp.put("shoulicount", result.get("SHOULICOUNT"));
			temp.put("wanchengcount", result.get("WANCHENGCOUNT"));
			ja.add(temp);

			// 组装图表数据集
			dataset.addValue(Double.parseDouble(String.valueOf(result
					.get("SHOULICOUNT"))), "受理数量", String.valueOf(result
					.get("SERVICETYPE")));
			dataset.addValue(Integer.parseInt(String.valueOf(result
					.get("WANCHENGCOUNT"))), "完成数量", String.valueOf(result
					.get("SERVICETYPE")));
		}
		jsonResult.put("rows", ja);
		final String[] headers = { "服务类别", "受理数量", "完成数量" };
		String[] keys = { "servicetype", "shoulicount", "wanchengcount" };
		Object[] types = { 40, 20, 20 };

		XlsWorkBook xls = new XlsWorkBook("客户服务统计");
		FileOutputStream out = null;
		String file = null;
		try {
			xls.addHead("客户服务统计报告", headers, page.getLoginUser());

			xls.addContent(jsonResult.getJSONArray("rows"), keys, types);
			Workbook wb = xls.getWorkbook();
			// 导出柱状图
			ChartUtil chartUtil = new ChartUtil();

			JFreeChart chart = chartUtil.createBarChart("客户服务统计报告", "服务类别",
					"数量", "", dataset);

			String url = this.getClass().getResource("").getPath()
					.replaceAll("%20", " ");
			String picPath = url.substring(0, url.indexOf("WEB-INF"))
					+ "report/style";
			OutputStream os = new FileOutputStream(picPath + "/temp1.jpeg");

			// 由ChartUtilities生成文件到一个体outputStream中去
			ChartUtilities.writeChartAsJPEG(os, chart, 900, 700);

			os.close();

			// 处理图片文件，以便产生ByteArray
			ByteArrayOutputStream handlePicture = new ByteArrayOutputStream();
			handlePicture = XlsWorkBook.handlePicture(picPath + "/temp1.jpeg");

			HSSFSheet r_sheet = (HSSFSheet) wb.getSheetAt(0);
			HSSFPatriarch patriarch = r_sheet.createDrawingPatriarch();
			HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 100, 50,
					(short) 5, 11, (short) 11, 25);
			// 插入图片
			patriarch.createPicture(anchor,
					wb.addPicture(handlePicture.toByteArray(),
							HSSFWorkbook.PICTURE_TYPE_JPEG));
			/*****************/

			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			Integer name = cal.get(Calendar.MILLISECOND);
			URL path = this.getClass().getClassLoader().getResource("");
			file = path.toString().substring(6).replaceAll("%20", "\\ ")
					+ "../xls/" + name + ".xls";

			out = new FileOutputStream(file);
			wb.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {

				}
			}
		}
		return file;
	}

	// 客户服务清单报告
	@Override
	public String exportCustomerQingdanCountReport(Page page) {
		List<Map<String, Object>> results = reportDao
				.exportCustomerQingdanCountReport(page);
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
		jsonResult.put("rows", ja);
		final String[] headers = { "序号", "处理时间", "客户名称", "处理内容", "处理结果", "处理人" };
		String[] keys = { "tid", "updateTime", "taskUser", "serviceType",
				"processresult", "excuteUser" };
		Object[] types = { 20, 20, 20, 40, 20, 20 };

		/** 导出 */
		XlsWorkBook xls = new XlsWorkBook("客户服务清单");
		String file = null;
		FileOutputStream out = null;
		try {
			xls.addHead("客户服务清单报告", headers, page.getLoginUser());

			xls.addContent(jsonResult.getJSONArray("rows"), keys, types);
			Workbook wb = xls.getWorkbook();

			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			Integer name = cal.get(Calendar.MILLISECOND);
			URL path = this.getClass().getClassLoader().getResource("");
			file = path.toString().substring(6).replaceAll("%20", "\\ ")
					+ "../xls/" + name + ".xls";

			out = new FileOutputStream(file);
			wb.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {

				}
			}
		}
		return file;
	}

	@Override
	public void exportCustomerWupinjinruCountReport() {
		// TODO Auto-generated method stub

	}

	@Override
	public String exportPriceBussnessInformationReport() {
		return null;
	}

	// 基本业务信息统计报表
	@Override
	public JSONObject getBasicBussnessInformationReport(Page page) {
		return reportDao.getBasicBussnessInformationReport(page);
	}

	@Override
	public JSONObject getBussnessChangeCountReport(Page page) {
		return reportDao.getBussnessChangeCountReport(page);

	}

	@Override
	public JSONObject getBussnessOrderCountReport(Page page) {
		return reportDao.getBussnessOrderCountReport(page);

	}

	@Override
	public JSONObject getCustomerBiandongmingxiCountReport(Page page) {
		return reportDao.getCustomerBiandongmingxiCountReport(page);
	}

	// 客户变动统计报告
	@Override
	public JSONObject getCustomerBiandongtongjiCountReport(Page page) {
		return reportDao.getCustomerBiandongtongjiCountReport(page);
	}

	@Override
	public void getCustomerBussnessTypeCountReport() {
		// TODO Auto-generated method stub

	}

	@Override
	public JSONObject getCustomerFuwuCountReport(Page page) {
		return reportDao.getCustomerFuwuCountReport(page);
	}

	@Override
	public JSONObject getCustomerQingdanCountReport(Page page) {
		return reportDao.getCustomerQingdanCountReport(page);
	}

	@Override
	public void getCustomerWupinjinruCountReport() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getPriceBussnessInformationReport() {
	}

}
