package com.hp.idc.report.excel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import java.util.Map;

import javax.imageio.ImageIO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;

import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.tika.io.ByteArrayOutputStream;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

public class XlsWorkBook {
	private Workbook wb;
	private Sheet sheet;
	private Map<String, CellStyle> styles;
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	public XlsWorkBook() {
		this("��������");
	}

	// //////
	// ��ȡExcel�ļ�����
	public HSSFWorkbook readExcel(String fileName) {
		File file = new File(fileName);
		FileInputStream in = null;
		HSSFWorkbook workbook = null;
		try {
			// ������Execl�ļ�������
			in = new FileInputStream(file);
			workbook = new HSSFWorkbook(in);
			// HSSFSheet sheet = workbook.getSheetAt(0);
			System.out.println("Excel�ļ�" + file.getAbsolutePath() + "����");

			in.close();

		} catch (Exception e) {
			System.out.println("��ȡExcel�ļ�ʧ��" + file.getAbsolutePath() + "ʧ��"
					+ e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {

				}
			}
		}
		return workbook;
	}

	/**
	 * ����·����ȡexcel��workbook
	 * 
	 * @param path
	 * @return
	 */
	public Workbook getXlsWorkBook(String path) {
		FileInputStream in = null;
		try {
			File file = new File(path);
			in = new FileInputStream(file);
			this.wb = new HSSFWorkbook(in);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {

				}
			}
		}
		return this.wb;
	}

	// public void addHead(Workbook r_wb, Sheet r_sheet, String title
	// ) throws Exception {
	// ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
	// String filePath = this.getClass().getResource("").toString() + "ok.jpg";
	//
	//
	// Row titleRow = r_sheet.getRow(0);
	// titleRow.setHeightInPoints(30);
	// Cell cell = titleRow.getCell(0);
	// cell.setCellValue("");
	// cell.setCellStyle(styles.get("title"));
	// r_sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, head.length - 1));
	//
	// Cell cell1 = titleRow.getCell(1);
	// cell1.setCellValue(title);
	// cell1.setCellStyle(styles.get("title"));
	// r_sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, head.length - 1));
	//
	// Row reportRow = r_sheet.getRow(1);
	// reportRow.setHeightInPoints(18);
	// cell = reportRow.getCell(0);
	// cell.setCellValue("ͳ�Ƶ�λ");
	// cell.setCellStyle(styles.get("title1"));
	// cell = reportRow.getCell(1);
	// cell.setCellValue("�Ϻ��ƶ�ŭ��IDC");
	// cell.setCellStyle(styles.get("title1"));
	// cell = reportRow.getCell(2);
	// cell.setCellValue("ͳ������");
	// cell.setCellStyle(styles.get("title1"));
	// cell = reportRow.getCell(3);
	// cell.setCellValue(new Date());
	// cell.setCellStyle(styles.get("title1"));
	//
	// Row row3 = r_sheet.getRow(2);
	// row3.setHeightInPoints(18);
	// cell = row3.getCell(0);
	// cell.setCellValue("");
	// cell.setCellStyle(styles.get("title1"));
	// cell = row3.getCell(1);
	// cell.setCellValue("");
	// cell.setCellStyle(styles.get("title1"));
	// cell = row3.getCell(2);
	// cell.setCellValue("�Ʊ���");
	// cell.setCellStyle(styles.get("title1"));
	// cell = row3.getCell(3);
	// cell.setCellValue("");
	// cell.setCellStyle(styles.get("title1"));
	//
	// Row headRow = r_sheet.getRow(2);
	// headRow.setHeightInPoints(18);
	// for (int i = 0; i < head.length; i++) {
	// Cell c = headRow.getCell(i);
	// c.setCellValue(head[i]);
	// c.setCellStyle(styles.get("header"));
	// }
	//
	// r_sheet.setColumnWidth(1, 20 * 256);
	// r_sheet.setColumnWidth(2, 15 * 256);
	// r_sheet.setColumnWidth(3, 20 * 256);
	// }

	// public void addContent(HSSFSheet r_sheet, JSONArray ja, String[] keys,
	// Object[] types) {
	// for (int x = 0; x < ja.size(); x++) {
	// JSONObject jo = (JSONObject) ja.get(x);
	// Row r = r_sheet.getRow(x + 4);
	// for (int y = 0; y < keys.length; y++) {
	// Cell c = r.getCell(y);
	// try {
	// c.setCellValue(jo.getString(keys[y]));
	// } catch (Exception e) {
	// c.setCellValue("");
	// }
	// c.setCellStyle(styles.get("cell_normal"));
	// if (types != null && types.length > 0 && types[y] != null) {
	// if (x == 0 && types[y] instanceof Integer) {
	// sheet.setColumnWidth(y, (Integer) types[y] * 256);
	// } else if (types[y] != null && types[y] instanceof String) {
	// c.setCellStyle(styles.get(types[y]));
	// }
	// }
	// }
	// }
	// }

	// ///**END*********************************************//

	/**
	 * ��ʼ��wb��sheet
	 */
	public XlsWorkBook(String sheetName) {
		try {
			wb = new HSSFWorkbook();

			// wb = Workbook.getWorkbook(new File("E:/a.xls"));
			styles = createStyles(wb);

			sheet = wb.createSheet(sheetName);

			sheet.setDisplayGridlines(false);
			sheet.setPrintGridlines(false);
			sheet.setFitToPage(true);
			sheet.setHorizontallyCenter(true);
			PrintSetup printSetup = sheet.getPrintSetup();
			printSetup.setLandscape(true);

			sheet.setAutobreaks(true);
			printSetup.setFitHeight((short) 1);
			printSetup.setFitWidth((short) 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * дexcel��ͷ
	 * 
	 * @param title
	 * @param head
	 * @throws Exception
	 */
	public void addHead(String title, String[] head, String user)
			throws Exception {
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();

		String url = this.getClass().getResource("").getPath()
				.replaceAll("%20", " ");
		String path = url.substring(0, url.indexOf("WEB-INF")) + "report/style";
		BufferedImage bufferImg = ImageIO.read(new File(path + "/ok.jpg"));
		ImageIO.write(bufferImg, "jpg", byteArrayOut);

		HSSFPatriarch patriarch = (HSSFPatriarch) sheet
				.createDrawingPatriarch();
		HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 0,
				0, (short) 1, 1);
		patriarch.createPicture(anchor, wb.addPicture(
				byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));

		Row titleRow = sheet.createRow(0);
		titleRow.setHeightInPoints(30);
		Cell cell = null;
		// Cell cell = titleRow.createCell(2);
		// cell.setCellValue("");
		// cell.setCellStyle(styles.get("title"));
		// sheet.addMergedRegion(new CellRangeAddress(0,0,0, head.length-1));

		Cell cell1 = titleRow.createCell(2);
		cell1.setCellValue(title);
		cell1.setCellStyle(styles.get("title"));
		// sheet.addMergedRegion(new CellRangeAddress(0,1,0, head.length-1));

		Row reportRow = sheet.createRow(2);
		reportRow.setHeightInPoints(18);
		cell = reportRow.createCell(1);
		cell.setCellValue("ͳ�Ƶ�λ");
		cell.setCellStyle(styles.get("title1"));
		cell = reportRow.createCell(2);
		cell.setCellValue("�����ƶ�IDC����");
		cell.setCellStyle(styles.get("title1"));
		cell = reportRow.createCell(3);
		cell.setCellValue("ͳ������");
		cell.setCellStyle(styles.get("title1"));
		cell = reportRow.createCell(4);
		cell.setCellValue(df.format(new Date()));
		cell.setCellStyle(styles.get("title1"));

		Row row3 = sheet.createRow(3);
		row3.setHeightInPoints(18);
		cell = row3.createCell(1);
		cell.setCellValue("");
		cell.setCellStyle(styles.get("title1"));
		cell = row3.createCell(2);
		cell.setCellValue("");
		cell.setCellStyle(styles.get("title1"));
		cell = row3.createCell(3);
		cell.setCellValue("�Ʊ���");
		cell.setCellStyle(styles.get("title1"));
		cell = row3.createCell(4);
		cell.setCellValue(user);
		cell.setCellStyle(styles.get("title1"));

		Row headRow = sheet.createRow(5);
		headRow.setHeightInPoints(18);
		for (int i = 0; i < head.length; i++) {
			Cell c = headRow.createCell(i + 1);
			c.setCellValue(head[i]);
			c.setCellStyle(styles.get("header"));
		}

		sheet.setColumnWidth(1, 20 * 256);
		sheet.setColumnWidth(2, 20 * 256);
		sheet.setColumnWidth(3, 20 * 256);
		sheet.setColumnWidth(4, 20 * 256);
	}

	/**
	 * д�б�
	 * 
	 * @param ja
	 * @param keys
	 * @param types
	 */
	public void addContent(JSONArray ja, String[] keys, Object[] types) {
		for (int x = 0; x < ja.size(); x++) {
			JSONObject jo = (JSONObject) ja.get(x);
			Row r = sheet.createRow(x + 6);
			for (int y = 0; y < keys.length; y++) {
				Cell c = r.createCell(y + 1);
				try {
					c.setCellValue(jo.getString(keys[y]));
				} catch (Exception e) {
					c.setCellValue("");
				}
				c.setCellStyle(styles.get("cell_normal"));
				if (types != null && types.length > 0 && types[y] != null) {
					if (x == 0 && types[y] instanceof Integer) {
						sheet.setColumnWidth(y, (Integer) types[y] * 256);
					} else if (types[y] != null && types[y] instanceof String) {
						c.setCellStyle(styles.get(types[y]));
					}
				}
			}
		}
	}

	public Workbook getWorkbook() {
		return this.wb;
	}

	/**
	 * ��������excel style
	 * 
	 * @param wb
	 * @return
	 */
	private Map<String, CellStyle> createStyles(Workbook wb) {
		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
		DataFormat df = wb.createDataFormat();

		CellStyle style;
		Font titleFont = wb.createFont();
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		titleFont.setFontHeightInPoints((short) 13);

		Font titleFont1 = wb.createFont();
		titleFont1.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		titleFont1.setFontHeightInPoints((short) 13);

		style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setFont(titleFont);
		styles.put("title", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setVerticalAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(titleFont1);
		styles.put("title1", style);

		Font headerFont = wb.createFont();
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont.setFontHeightInPoints((short) 10);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFont(headerFont);
		styles.put("header", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE
				.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFont(headerFont);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("header_date", style);

		Font font1 = wb.createFont();
		font1.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setFont(font1);
		styles.put("cell_b", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font1);
		styles.put("cell_b_centered", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setFont(font1);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("cell_b_date", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setFont(font1);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("cell_g", style);

		Font font2 = wb.createFont();
		font2.setColor(IndexedColors.BLUE.getIndex());
		font2.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setFont(font2);
		styles.put("cell_bb", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setFont(font1);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("cell_bg", style);

		Font font3 = wb.createFont();
		font3.setFontHeightInPoints((short) 14);
		font3.setColor(IndexedColors.DARK_BLUE.getIndex());
		font3.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setFont(font3);
		style.setWrapText(true);
		styles.put("cell_h", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setWrapText(true);
		styles.put("cell_normal", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setWrapText(true);
		styles.put("cell_normal_centered", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setWrapText(true);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("cell_normal_date", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setIndention((short) 1);
		style.setWrapText(true);
		styles.put("cell_indented", style);

		style = createBorderedStyle(wb);
		style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styles.put("cell_blue", style);

		return styles;
	}

	private CellStyle createBorderedStyle(Workbook wb) {
		CellStyle style = wb.createCellStyle();
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		return style;
	}

	/*************************************************************/
	/**
	 * �����ͻ��䶯ͳ�Ʊ���(ҵ��䶯ͳ��)
	 */
	public HSSFWorkbook exportCustomerBiandongtongji(HSSFWorkbook r_wb,
			String title, JSONArray ja, String[] keys, String user) {

		HSSFSheet r_sheet = r_wb.getSheetAt(0);
		/** ��װhead */
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();

		HSSFRow reportRow = r_sheet.getRow(3);
		HSSFCell cell = reportRow.createCell(6);
		cell.setCellValue(df.format(new Date()));
		HSSFRow row = r_sheet.getRow(4);
		cell = row.createCell(2);
		cell.setCellValue(user);

		/** ��װcontent */
		for (int x = 0; x < ja.size(); x++) {
			JSONObject jo = (JSONObject) ja.get(x);
			HSSFRow r = r_sheet.getRow(x + 7);
			for (int y = 0; y < keys.length; y++) {
				HSSFCell c = r.getCell(y + 1);
				try {
					c.setCellValue(Integer.parseInt(jo.getString(keys[y])));

				} catch (Exception e) {
					c.setCellValue(jo.getString(keys[y]));
				}
				// c.setCellStyle(styles.get("cell_normal"));
				// if (types != null && types.length > 0 && types[y] != null) {
				// if (x == 0 && types[y] instanceof Integer) {
				// sheet.setColumnWidth(y, (Integer) types[y] * 256);
				// } else if (types[y] != null && types[y] instanceof String) {
				// c.setCellStyle(styles.get(types[y]));
				// }
				// }
			}
		}
		return r_wb;
	}

	/**
	 * ��������(��ֵ)ҵ����Ϣͳ�Ʊ���
	 * 
	 * @param r_wb
	 * @param title
	 * @param ja
	 * @param keys
	 * @param user
	 * @return
	 */
	public HSSFWorkbook exportBasicBussnessInformationReport(HSSFWorkbook r_wb,
			String title, JSONArray ja, String[] keys, String user) {
		try {
			styles = createStyles(r_wb);
			HSSFSheet r_sheet = r_wb.getSheetAt(0);
			/** ��װhead */

			HSSFRow row1 = r_sheet.getRow(1);
			HSSFCell cell = row1.getCell(2);
			cell.setCellValue(title);
			cell.setCellStyle((HSSFCellStyle) styles.get("title"));

			HSSFRow reportRow = r_sheet.getRow(3);
			cell = reportRow.createCell(5);
			cell.setCellValue(df.format(new Date()));
			HSSFRow row = r_sheet.getRow(4);
			cell = row.createCell(5);
			cell.setCellValue(user);

			HSSFRow row7 = r_sheet.getRow(7);
			cell = row7.getCell(2);
			cell.setCellValue(title);
			cell.setCellStyle((HSSFCellStyle) styles.get("title"));
			//
			/** ��װcontent */
			for (int x = 0; x < ja.size(); x++) {
				JSONObject jo = (JSONObject) ja.get(x);
				// //�ϲ���Ԫ��
				// r_sheet.addMergedRegion(new CellRangeAddress((x + 10),(x +
				// 10), 2,
				// 5));
				HSSFRow r = r_sheet.getRow(x + 10);

				for (int y = 0; y < keys.length; y++) {
					HSSFCell c = r.getCell(y + 2);
					try {
						c.setCellValue(Integer.parseInt(jo.getString(keys[y])));

					} catch (Exception e) {
						c.setCellValue(jo.getString(keys[y]));
					} finally {
						c.setCellStyle((HSSFCellStyle) styles
								.get("cell_normal"));
					}

				}
			}

			// // ����ͼ
			// JFreeChart chart = createValidityComparePimChar(
			// createDataset(ja, keys), "", "No data");
			ChartUtil chartUtil = new ChartUtil();
			JFreeChart chart = chartUtil.createBarChart(title, "��������",
					"����", "", createBarDataset(ja, keys));
			// File file = new File("c:/temp1.jpeg");
			// ����״ͼ

			// ChartUtilities.saveChartAsPNG(file, chart, 500, 300);
			String url = this.getClass().getResource("").getPath()
					.replaceAll("%20", " ");
			String path = url.substring(0, url.indexOf("WEB-INF"))
					+ "report/style";
			OutputStream os = new FileOutputStream(path + "/temp1.jpeg");

			// ��ChartUtilities�����ļ���һ����outputStream��ȥ
			ChartUtilities.writeChartAsJPEG(os, chart, 900, 700);

			os.close();

			// ����ͼƬ�ļ����Ա����ByteArray
			ByteArrayOutputStream handlePicture = new ByteArrayOutputStream();
			handlePicture = handlePicture(path + "/temp1.jpeg");

			HSSFPatriarch patriarch = r_sheet.createDrawingPatriarch();
			HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 100, 50,
					(short) 5, 11, (short) 11, 25);
			// ����ͼƬ
			patriarch.createPicture(anchor,
					r_wb.addPicture(handlePicture.toByteArray(),
							HSSFWorkbook.PICTURE_TYPE_JPEG));

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		return r_wb;
	}

	// ��ͼƬ
	public static ByteArrayOutputStream handlePicture(String pathOfPicture) {
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		try {
			BufferedImage bufferImg = ImageIO.read(new File(pathOfPicture));
			ImageIO.write(bufferImg, "jpeg", byteArrayOut);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return byteArrayOut;
	}

	// ��������Դ
	private PieDataset createDataset(JSONArray ja, String[] keys) {

		DefaultPieDataset pset = new DefaultPieDataset();
		for (int i = 0; i < ja.size(); i++) {
			JSONObject jo = (JSONObject) ja.get(i);
			pset.setValue(jo.getString(keys[0]),
					Integer.parseInt(jo.getString(keys[1])));

		}

		return pset;
	}

	// ������״ͼ������Դ
	private DefaultCategoryDataset createBarDataset(JSONArray ja, String[] keys) {

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < ja.size(); i++) {
			JSONObject jo = (JSONObject) ja.get(i);
			//if(Integer.parseInt(jo.getString(keys[1])) !=0){
				dataset.addValue(Integer.parseInt(jo.getString(keys[1])), "����", jo.getString(keys[0]));
			//}
			
		}

		return dataset;
	}
	/**
	 * ��״ͼ
	 * 
	 * @param dataset
	 *            ���ݼ�
	 * @param chartTitle
	 *            ͼ����
	 * @param charName
	 *            ����ͼ������
	 * @param pieKeys
	 *            �ֱ������ּ�
	 * @return
	 */
	public static JFreeChart createValidityComparePimChar(PieDataset dataset,
			String chartTitle, String nodatamess) {

		JFreeChart chart = ChartFactory.createPieChart3D(chartTitle, dataset,
				true, true, false);
		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290D);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5F);
		plot.setNoDataMessage("No data");

		// ʹ��˵����ǩ��������,ȥ��������ڵ�Ч��
		chart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		chart.setAntiAlias(false);
		// ͼƬ����ɫ

		// chart.setBackgroundPaint(Color.WHITE);
		// ����ͼ�����������������title

		// ���ñ������ɫ
		TextTitle text = new TextTitle(chartTitle);
		text.setPaint(new Color(102, 102, 102));
		chart.setTitle(text);

		plot.setBackgroundPaint(new Color(255, 253, 246));
		plot.setOutlineStroke(new BasicStroke(0));
		plot.setMaximumLabelWidth(0.25d);
		// ͼƬ����ʾ�ٷֱ�:Ĭ�Ϸ�ʽ

		plot.setShadowXOffset(1.0d);
		plot.setIgnoreZeroValues(false);
		plot.setStartAngle(90); // ������ת�Ƕ�
		plot.setDirection(Rotation.CLOCKWISE); // ������ת����
		plot.setForegroundAlpha(0.65f);

		plot.setLabelLinkMargin(0.1);
		plot.setSectionOutlinesVisible(false);
		plot.setDepthFactor(0.1d);// ��ͼ��Z��߶ȡ�

		// ����������ʱ����Ϣ
		plot.setNoDataMessage(nodatamess);
		plot.setNoDataMessageFont(new java.awt.Font("", java.awt.Font.BOLD, 20));

		// ����������ʱ����Ϣ��ʾ��ɫ
		plot.setNoDataMessagePaint(new Color(87, 149, 117));

		// ͼƬ����ʾ�ٷֱ�:�Զ��巽ʽ��{0} ��ʾѡ� {1} ��ʾ��ֵ�� {2} ��ʾ��ռ���� ,С�������λ
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
				"{0} ={1}({2})", NumberFormat.getNumberInstance(),
				new DecimalFormat("0.00%")));
		// ͼ����ʾ�ٷֱ�:�Զ��巽ʽ�� {0} ��ʾѡ� {1} ��ʾ��ֵ�� {2} ��ʾ��ռ����

		plot.setLabelFont(new java.awt.Font("����", java.awt.Font.BOLD, 12));

		// ����Legend�������壬�����������
		chart.getLegend().setItemFont(
				new java.awt.Font("����", java.awt.Font.PLAIN, 13));

		// ָ��ͼƬ��͸����(0.0-1.0)
		plot.setForegroundAlpha(0.65f);

		return chart;

	}

	// /////////////////////////////�ָ���
	// start/////////////////////////////////////
	/**
	 * �����ͻ��䶯ͳ�Ʊ���(��״ͼ)
	 * 
	 * @param title
	 * @param head
	 * @param ja
	 * @param keys
	 * @param types
	 */
	public void makeCustomerBiandongtongjiCountReport(String title,
			String[] head, JSONArray ja, String[] keys, Object[] types) {

		try {
			/** head */
			this.addHead("�ͻ��䶯ͳ�Ʊ���", head, "");
			/** content */
			this.addContent(ja, keys, types);

			// ��װJFreeCharts��״ͼ
			DefaultPieDataset dpd = new DefaultPieDataset();
			dpd.setValue("������Ա", 25);
			dpd.setValue("�г���Ա", 10);
			dpd.setValue("������Ա", 50);
			dpd.setValue("������Ա", 15);

			// ����ͼƬ
			JFreeChart chart = createPieChart(dpd);

			File file = new File("c:/temp1.jpg");
			final int width = 500;
			final int height = 300;
			try {
				ChartUtilities.saveChartAsPNG(file, chart, width, height);
			} catch (IOException e) {
				e.printStackTrace();
			}

			ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
			BufferedImage bufferImg = ImageIO.read(new File("c:/temp1.jpg"));
			ImageIO.write(bufferImg, "jpeg", byteArrayOut);

			HSSFPatriarch patriarch = (HSSFPatriarch) sheet
					.createDrawingPatriarch();
			HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 100, 100,
					(short) 1, 1, (short) 10, 19);
			patriarch
					.createPicture(anchor, wb.addPicture(
							byteArrayOut.toByteArray(),
							HSSFWorkbook.PICTURE_TYPE_JPEG));

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public JFreeChart createLineChart(DefaultCategoryDataset linedataset) {
		// ����ͼ�����

		JFreeChart chart = ChartFactory.createLineChart("����ͼ", // chart title
				"ʱ��", // domain axis label
				"���۶�(����)", // range axis label
				linedataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips
				false // urls
				);

		// customise the range axis...
		CategoryPlot plot = chart.getCategoryPlot();

		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setAutoRangeIncludesZero(true);
		rangeAxis.setUpperMargin(0.20);
		rangeAxis.setLabelAngle(Math.PI / 2.0);
		return chart;
	}

	// ����ͼ��������JFreeChart
	public JFreeChart createPieChart(PieDataset piedataset) {
		// ����ͼ�����
		JFreeChart jfreechart = ChartFactory.createPieChart("", piedataset,
				true, true, false);
		// ���ͼ����ʾ����
		PiePlot pieplot = (PiePlot) jfreechart.getPlot();
		jfreechart.setTitle(new TextTitle("", new java.awt.Font("����",
				java.awt.Font.BOLD, 20)));
		LegendTitle legend = jfreechart.getLegend(0);
		legend.setItemFont(new java.awt.Font("����", java.awt.Font.TYPE1_FONT, 16));
		pieplot.setLabelFont(new java.awt.Font("����",
				java.awt.Font.HANGING_BASELINE, 12));

		return jfreechart;
	}

	// ɾ��ͼƬ
	public static void deleteServerPic(String realPath) {
		String TEMP_PATH = realPath;
		File fileTemp = new File(TEMP_PATH);
		// �ж��ļ��Ƿ����
		boolean flag = false;
		flag = fileTemp.exists();
		if (flag) {
			if (fileTemp.isDirectory()) {
				String pngs[] = fileTemp.list();
				for (String png : pngs) {
					if (png.endsWith("png")) {
						File file = new File(TEMP_PATH + "\\" + png);
						if (file.isFile()) {
							file.delete();
						}
					}
				}
			}
		}
	}
}
