package com.hp.idc.report.excel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.chart.servlet.ChartDeleter;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.TextAnchor;

public class ChartUtil {

	// ����ʱ������ͼ������ͼƬ����

	public static String generatePieChart(DefaultPieDataset dataset,
			String title, int width, int height, HttpSession session,
			PrintWriter pw) {

		String filename = null;
		try {
			if (session != null) {
				ChartDeleter deleter = (ChartDeleter) session
						.getAttribute("JFreeChart_Deleter");
				session.removeAttribute("JFreeChart_Deleter");
				session.setAttribute("JFreeChart_Deleter", deleter);
			}
			JFreeChart chart = ChartFactory.createPieChart3D(title, // chart
																	// title
					dataset, // data
					true, // include legend
					true, false);
			// Write the chart image to the temporary directory
			ChartRenderingInfo info = new ChartRenderingInfo(
					new StandardEntityCollection());
			// If the last parameter is null, the chart is a "one time"-chart
			// and will be deleted after the first serving.
			// If the last parameter is a session object, the chart remains
			// until session time out.
			filename = ServletUtilities.saveChartAsPNG(chart, width, height,
					info, session);
			// Write the image map to the PrintWriter
			ChartUtilities.writeImageMap(pw, filename, info, true);
			pw.flush();
		} catch (Exception e) {
			System.out.println("Exception - " + e.toString());
			e.printStackTrace(System.out);
			filename = "picture_error.png";
		}
		return filename;
	}

	// ������״ͼ
	public JFreeChart createBarChart(String title, String xname, String yname,
			String path, CategoryDataset dataset) {
		JFreeChart chart = ChartFactory.createBarChart3D(title, // ����3D��״ͼ
				xname,// ��������
				yname,// ��������
				dataset,// ���ݼ�
				PlotOrientation.VERTICAL,// ������ʾ
				true,// ��ʾÿ����ɫ���ӵ�����
				false, false);
		// CategoryPlot plot = chart.getCategoryPlot();// ����ͼ�ĸ߼�����
		// BarRenderer3D renderer = new BarRenderer3D();// 3D�����޸�
		// renderer.setBaseOutlinePaint(Color.BLACK);// ���ñ߿���ɫΪblack
		// renderer.setWallPaint(Color.gray); // ����wall����ɫΪgray
		// renderer.setItemLabelGenerator(new
		// StandardCategoryItemLabelGenerator());//
		// ������������,API�о�Ȼû��StandardCategoryItemLabelGenerator�����
		// // renderer.setItemLabelFont(new Font("����",Font.PLAIN,12));//����������������
		// renderer.setItemLabelsVisible(true);// ��ItemLabel����
		// plot.setRenderer(renderer);// ���޸ĺ������ֵ���浽ͼ��
		// plot.setForegroundAlpha(0.6f);// ����͸����
		/*----------������������ľ����Ⱦ������������⣩--------------*/
		chart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		// �ײ��������������
		chart.getLegend().setItemFont(new Font("����", Font.PLAIN, 18));
		// ���ñ�������
		TextTitle textTitle = chart.getTitle();
		textTitle.setFont(new Font("����", Font.PLAIN, 20));
		// textTitle.setBackgroundPaint(Color.LIGHT_GRAY);//���ⱳ��ɫ
		// textTitle.setPaint(Color.cyan);//����������ɫ
		textTitle.setText(title);// ��������
		CategoryPlot plot = chart.getCategoryPlot();// ����ͼ�ĸ߼�����
		BarRenderer3D renderer = new BarRenderer3D();// 3D�����޸�
		CategoryAxis domainAxis = plot.getDomainAxis();// ��X��������
		ValueAxis rAxis = plot.getRangeAxis();// ��Y��������
		/***
		 * domainAxis����(x��һЩ����)
		 **/
		// ����X�������ϵ�����
		domainAxis.setTickLabelFont(new Font("����", Font.PLAIN, 12));
		// ����X��ı�������
		domainAxis.setLabelFont(new Font("����", Font.PLAIN, 12));
		domainAxis.setLabel("");// X��ı�������
		domainAxis.setTickLabelPaint(Color.black);// X��ı���������ɫ
		domainAxis.setTickLabelsVisible(true);// X��ı��������Ƿ���ʾ
		domainAxis.setAxisLinePaint(Color.darkGray);// X�������ɫ
		domainAxis.setTickMarksVisible(true);// ������Ƿ���ʾ
		domainAxis.setTickMarkOutsideLength(3);// ��������ⳤ��
		domainAxis.setTickMarkInsideLength(3);// ��������ڳ���
		domainAxis.setTickMarkPaint(Color.darkGray);// �������ɫ
		domainAxis.setUpperMargin(0.2);// ���þ���ͼƬ��˾���
		domainAxis.setLowerMargin(0.2); // ���þ���ͼƬ�Ҷ˾���
		// �����ϵ� Lable �Ƿ�������ʾ
		domainAxis.setMaximumCategoryLabelWidthRatio(0.6f);
		// �����ϵ� Lable 45����б
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
		/**
		 * rAxis���� Y������
		 * 
		 **/
		// ����Y�������ϵ�����
		rAxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 12));
		// ����Y��ı�������
		rAxis.setLabelFont(new Font("����", Font.PLAIN, 12));
		// Y��ȡֵ��Χ�����治�ܳ��� rAxis.setAutoRange(true) ���������ã�
		rAxis.setRange(0, 50);
		rAxis.setLowerBound(0); //Y���Կ�ʼ����Сֵ
		// rAxis.setUpperBound(600);//Y������ֵ
		rAxis.setLabel(yname);// Y������
		rAxis.setLabelAngle(1.6);// ����������ʾ�Ƕȣ�1.6ʱ��ˮƽ��
		rAxis.setLabelPaint(Color.BLACK);// ����������ɫ
		//rAxis.setMinorTickMarksVisible(true);// ������Ƿ���ʾ
		rAxis.setMinorTickCount(5);// �ڶ��еĿ̶���
		rAxis.setMinorTickMarkInsideLength(3);// �ڿ̶������ڳ���
		rAxis.setMinorTickMarkOutsideLength(3);// �ڿ̶ȼ������ⳤ��
		rAxis.setTickMarkInsideLength(3);// ��̶������ڳ���
		rAxis.setTickMarkPaint(Color.black);// �̶�����ɫ
		rAxis.setTickLabelsVisible(true);// �̶���ֵ�Ƿ���ʾ
		// ����Y������Ƿ���ʾ�����ǰ������rAxis.setMinorTickMarksVisible(true); ����������ʾ��
		rAxis.setTickMarksVisible(false);
		rAxis.setAxisLinePaint(Color.black);// Y��������ɫ
		rAxis.setAxisLineVisible(true);// Y�������Ƿ���ʾ
		// ������ߵ�һ�� Item ��ͼƬ���˵ľ��� (������rAxis.setRange(100, 600);����²�������)
		rAxis.setUpperMargin(0.15);
		// ������͵�һ�� Item ��ͼƬ�׶˵ľ���
		rAxis.setLowerMargin(0.15);
		//rAxis.setAutoRange(true);// �Ƿ��Զ���Ӧ��Χ
		rAxis.setVisible(true);// Y�������Ƿ���ʾ
		// �����ᾫ��
		NumberAxis na = (NumberAxis) plot.getRangeAxis();
		//��������������Ϊ5
		na.setTickUnit(new NumberTickUnit(5));

		//na.setAutoRangeIncludesZero(false);
		DecimalFormat df = new DecimalFormat("#0");
		// ���������ݱ�ǩ����ʾ��ʽ
		na.setNumberFormatOverride(df);
		/**
		 * renderer���� ���������������
		 */
		renderer.setBaseOutlinePaint(Color.ORANGE); // �߿���ɫ
		renderer.setDrawBarOutline(true);
		renderer.setWallPaint(Color.gray);// ����ǽ����ɫ
		renderer.setMaximumBarWidth(0.05); // �������ӿ��
		renderer.setMinimumBarLength(0.1); // �������Ӹ߶�
		renderer.setSeriesPaint(0, Color.ORANGE); // ����������ɫ
		renderer.setItemMargin(0.0); // ����ÿ��������������ƽ������֮�����

		// ����������ʾ��Ӧ��Ϣ
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setBaseItemLabelsVisible(true);
		renderer.setBaseItemLabelPaint(Color.BLACK);// ������ֵ��ɫ��Ĭ�Ϻ�ɫ
		// ����ItemLabelAnchor TextAnchor
		// �������ܴﵽ��ͬ��Ч��������ItemLabelAnchor���ѡOUTSIDE����ΪINSIDE��ʾ������
		 renderer.setBasePositiveItemLabelPosition(new
		 ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
		 TextAnchor.CENTER_LEFT));
		// ������Խ�һ��������ֵ��λ�ã����ǵø���ItemLabelAnchorѡ�����.
		renderer.setItemLabelAnchorOffset(10);
		/**
		 * plot ����
		 ***/
		// ��������������ɫ
		plot.setDomainGridlinePaint(Color.blue);
		plot.setDomainGridlinesVisible(true);
		// �������������ɫ
		plot.setRangeGridlinePaint(Color.blue);
		plot.setRangeGridlinesVisible(true);
		// ͼƬ����ɫ
		plot.setBackgroundPaint(Color.LIGHT_GRAY);
		plot.setOutlineVisible(true);
		// ͼ�߿���ɫ
		plot.setOutlinePaint(Color.magenta);
		// ��������͸����
		plot.setForegroundAlpha(0.8f);
		plot.setNoDataMessage("No data");
		// //�����ͷŵ�����
		// plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
		// //��Ĭ�Ϸŵ���ߵ���ֵ�ŵ��ұ�
		// plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
		plot.setRenderer(renderer);// ���޸ĺ������ֵ���浽ͼ��

		// OutputStream os = null;
		// try {
		// // File file = new File(path);
		// os = new FileOutputStream(path);
		// XlsWorkBook.deleteServerPic(path);
		// // ��ChartUtilities�����ļ���һ����outputStream��ȥ
		// ChartUtilities.writeChartAsJPEG(os, chart, 800, 600);
		// os.flush();
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// if (os != null) {
		// try {
		// os.close();
		// } catch (IOException e) {
		//
		// }
		// }
		// }
		return chart;
	}

	// �����۾Q�D1
	public JFreeChart createLineChart(String chartTitle, String x, String y,
			String path, CategoryDataset xyDataset) {
		OutputStream os = null;
		JFreeChart chart = null;
		try {
			chart = ChartFactory.createLineChart(chartTitle, x, y, xyDataset,
					PlotOrientation.VERTICAL, true, true, false);

			chart.setTextAntiAlias(false);
			chart.setBackgroundPaint(Color.WHITE);
			// ����ͼ�����������������title
			Font font = new Font("����", Font.BOLD, 18);
			TextTitle title = new TextTitle(chartTitle);
			title.setFont(font);
			chart.setTitle(title);
			// �ײ��������������
			chart.getLegend().setItemFont(new Font("����", Font.PLAIN, 15));

			// �����������
			Font labelFont = new Font("SansSerif", Font.TRUETYPE_FONT, 12);

			chart.setBackgroundPaint(Color.WHITE);

			CategoryPlot categoryplot = (CategoryPlot) chart.getPlot();
			// x�� // �����������Ƿ�ɼ�
			categoryplot.setDomainGridlinesVisible(true);
			// y�� //�����������Ƿ�ɼ�
			categoryplot.setRangeGridlinesVisible(true);

			categoryplot.setRangeGridlinePaint(Color.DARK_GRAY);// ����ɫ��

			categoryplot.setDomainGridlinePaint(Color.DARK_GRAY);// ����ɫ��

			categoryplot.setBackgroundPaint(Color.lightGray);

			categoryplot.setNoDataMessage("No data");

			// ����������֮��ľ���
			// categoryplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));

			CategoryAxis domainAxis = categoryplot.getDomainAxis(); // ��X��������
			ValueAxis yAxis = categoryplot.getRangeAxis();// ��Y��������
			// ����Y�������ϵ�����
			yAxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 12));
			// ����Y��ı�������
			yAxis.setLabelFont(new Font("����", Font.PLAIN, 15));

			domainAxis.setLabelFont(labelFont);// �����
			domainAxis.setTickLabelFont(labelFont);// ����ֵ

			// Lable
			// ���þ���ͼƬ��˾���
			domainAxis.setLowerMargin(0.0);
			// ���þ���ͼƬ�Ҷ˾���
			domainAxis.setUpperMargin(0.0);

			NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
			numberaxis
					.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
			numberaxis.setAutoRangeIncludesZero(true);

			// ���renderer ע���������������͵�lineandshaperenderer����
			LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) categoryplot
					.getRenderer();

			lineandshaperenderer.setBaseShapesVisible(true); // series �㣨�����ݵ㣩�ɼ�
			lineandshaperenderer.setBaseLinesVisible(true); // series
															// �㣨�����ݵ㣩�������߿ɼ�

			// ��ʾ�۵�����
			// lineandshaperenderer.setBaseItemLabelGenerator(new
			// StandardCategoryItemLabelGenerator());
			// lineandshaperenderer.setBaseItemLabelsVisible(true);

			// os = new FileOutputStream(path);
			// XlsWorkBook.deleteServerPic(path);
			// ��ChartUtilities�����ļ���һ����outputStream��ȥ
			// ChartUtilities.writeChartAsJPEG(os, chart, 800, 600);
			// os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// if (os != null) {
			// try {
			// os.close();
			// } catch (IOException e) {
			//
			// }
			// }
		}
		return chart;
	}

	// �����۾Q�D2
	public JFreeChart createLineChart1(String chartTitle, String x, String y,
			String path, CategoryDataset xyDataset) {

		JFreeChart jfreechart = ChartFactory.createXYLineChart(chartTitle, x,
				y, (XYDataset) xyDataset, PlotOrientation.VERTICAL, true, true,
				false);

		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		xyplot.setBackgroundPaint(Color.white);
		xyplot.setDomainGridlinesVisible(false);
		xyplot.setRangeGridlinePaint(Color.gray);
		xyplot.setRangeGridlinesVisible(true);
		XYStepRenderer xysteprenderer = new XYStepRenderer();
		xysteprenderer.setSeriesStroke(0, new BasicStroke(2.0F));
		xysteprenderer
				.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
		xysteprenderer.setDefaultEntityRadius(6);
		xyplot.setRenderer(xysteprenderer);

		OutputStream os = null;
		try {

			os = new FileOutputStream(path);
			XlsWorkBook.deleteServerPic(path);
			// ��ChartUtilities�����ļ���һ����outputStream��ȥ
			ChartUtilities.writeChartAsJPEG(os, jfreechart, 800, 600);
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {

				}
			}
		}
		return jfreechart;
	}

	public static void main(String[] args) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		dataset.addValue(60, "����", "����");
		dataset.addValue(1, "�������", "����");
		dataset.addValue(5, "�������", "ͨ��");
		dataset.addValue(4, "����", "ͨ��");
		dataset.addValue(0, "��ʱ����", "����");
//		dataset.addValue(1, "����", "����");
//		dataset.addValue(13, "�������", "����");
//		dataset.addValue(100, "�������", "ͨ��");
//		dataset.addValue(45, "����", "ͨ��");
//		dataset.addValue(66, "��ʱ����", "����");
		ChartUtil chartUtil = new ChartUtil();
		JFreeChart chart = chartUtil.createBarChart("��ֵҵ����Ϣͳ��", "ҵ�����",
				"����", "", dataset);
		 OutputStream os = null;
		 try {
		 // File file = new File(path);
		 os = new FileOutputStream("c:/qqqq.jpeg");
		 // ��ChartUtilities�����ļ���һ����outputStream��ȥ
		 ChartUtilities.writeChartAsJPEG(os, chart, 800, 600);
		 os.flush();
		 } catch (Exception e) {
		 e.printStackTrace();
		 } finally {
		 if (os != null) {
		 try {
		 os.close();
		 } catch (IOException e) {
		
		 }

	}
		 }
	}
}
