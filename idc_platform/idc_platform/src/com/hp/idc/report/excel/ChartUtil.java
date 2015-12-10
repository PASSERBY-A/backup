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

	// 产生时间序列图，返回图片名称

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

	// 生成柱状图
	public JFreeChart createBarChart(String title, String xname, String yname,
			String path, CategoryDataset dataset) {
		JFreeChart chart = ChartFactory.createBarChart3D(title, // 建立3D柱状图
				xname,// 横轴名称
				yname,// 纵轴名称
				dataset,// 数据集
				PlotOrientation.VERTICAL,// 纵向显示
				true,// 显示每个颜色柱子的柱名
				false, false);
		// CategoryPlot plot = chart.getCategoryPlot();// 设置图的高级属性
		// BarRenderer3D renderer = new BarRenderer3D();// 3D属性修改
		// renderer.setBaseOutlinePaint(Color.BLACK);// 设置边框颜色为black
		// renderer.setWallPaint(Color.gray); // 设置wall的颜色为gray
		// renderer.setItemLabelGenerator(new
		// StandardCategoryItemLabelGenerator());//
		// 设置柱顶数据,API中居然没有StandardCategoryItemLabelGenerator这个类
		// // renderer.setItemLabelFont(new Font("黑体",Font.PLAIN,12));//设置柱顶数据字体
		// renderer.setItemLabelsVisible(true);// 打开ItemLabel开关
		// plot.setRenderer(renderer);// 将修改后的属性值保存到图中
		// plot.setForegroundAlpha(0.6f);// 柱的透明度
		/*----------设置消除字体的锯齿渲染（解决中文问题）--------------*/
		chart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		// 底部汉字乱码的问题
		chart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 18));
		// 设置标题字体
		TextTitle textTitle = chart.getTitle();
		textTitle.setFont(new Font("黑体", Font.PLAIN, 20));
		// textTitle.setBackgroundPaint(Color.LIGHT_GRAY);//标题背景色
		// textTitle.setPaint(Color.cyan);//标题字体颜色
		textTitle.setText(title);// 标题内容
		CategoryPlot plot = chart.getCategoryPlot();// 设置图的高级属性
		BarRenderer3D renderer = new BarRenderer3D();// 3D属性修改
		CategoryAxis domainAxis = plot.getDomainAxis();// 对X轴做操作
		ValueAxis rAxis = plot.getRangeAxis();// 对Y轴做操作
		/***
		 * domainAxis设置(x轴一些设置)
		 **/
		// 设置X轴坐标上的文字
		domainAxis.setTickLabelFont(new Font("宋体", Font.PLAIN, 12));
		// 设置X轴的标题文字
		domainAxis.setLabelFont(new Font("宋体", Font.PLAIN, 12));
		domainAxis.setLabel("");// X轴的标题内容
		domainAxis.setTickLabelPaint(Color.black);// X轴的标题文字颜色
		domainAxis.setTickLabelsVisible(true);// X轴的标题文字是否显示
		domainAxis.setAxisLinePaint(Color.darkGray);// X轴横线颜色
		domainAxis.setTickMarksVisible(true);// 标记线是否显示
		domainAxis.setTickMarkOutsideLength(3);// 标记线向外长度
		domainAxis.setTickMarkInsideLength(3);// 标记线向内长度
		domainAxis.setTickMarkPaint(Color.darkGray);// 标记线颜色
		domainAxis.setUpperMargin(0.2);// 设置距离图片左端距离
		domainAxis.setLowerMargin(0.2); // 设置距离图片右端距离
		// 横轴上的 Lable 是否完整显示
		domainAxis.setMaximumCategoryLabelWidthRatio(0.6f);
		// 横轴上的 Lable 45度倾斜
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
		/**
		 * rAxis设置 Y轴设置
		 * 
		 **/
		// 设置Y轴坐标上的文字
		rAxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 12));
		// 设置Y轴的标题文字
		rAxis.setLabelFont(new Font("黑体", Font.PLAIN, 12));
		// Y轴取值范围（下面不能出现 rAxis.setAutoRange(true) 否则不起作用）
		rAxis.setRange(0, 50);
		rAxis.setLowerBound(0); //Y轴以开始的最小值
		// rAxis.setUpperBound(600);//Y轴的最大值
		rAxis.setLabel(yname);// Y轴内容
		rAxis.setLabelAngle(1.6);// 标题内容显示角度（1.6时候水平）
		rAxis.setLabelPaint(Color.BLACK);// 标题内容颜色
		//rAxis.setMinorTickMarksVisible(true);// 标记线是否显示
		rAxis.setMinorTickCount(5);// 节段中的刻度数
		rAxis.setMinorTickMarkInsideLength(3);// 内刻度线向内长度
		rAxis.setMinorTickMarkOutsideLength(3);// 内刻度记线向外长度
		rAxis.setTickMarkInsideLength(3);// 外刻度线向内长度
		rAxis.setTickMarkPaint(Color.black);// 刻度线颜色
		rAxis.setTickLabelsVisible(true);// 刻度数值是否显示
		// 所有Y标记线是否显示（如果前面设置rAxis.setMinorTickMarksVisible(true); 则其照样显示）
		rAxis.setTickMarksVisible(false);
		rAxis.setAxisLinePaint(Color.black);// Y轴竖线颜色
		rAxis.setAxisLineVisible(true);// Y轴竖线是否显示
		// 设置最高的一个 Item 与图片顶端的距离 (在设置rAxis.setRange(100, 600);情况下不起作用)
		rAxis.setUpperMargin(0.15);
		// 设置最低的一个 Item 与图片底端的距离
		rAxis.setLowerMargin(0.15);
		//rAxis.setAutoRange(true);// 是否自动适应范围
		rAxis.setVisible(true);// Y轴内容是否显示
		// 数据轴精度
		NumberAxis na = (NumberAxis) plot.getRangeAxis();
		//将纵坐标间距设置为5
		na.setTickUnit(new NumberTickUnit(5));

		//na.setAutoRangeIncludesZero(false);
		DecimalFormat df = new DecimalFormat("#0");
		// 数据轴数据标签的显示格式
		na.setNumberFormatOverride(df);
		/**
		 * renderer设置 柱子相关属性设置
		 */
		renderer.setBaseOutlinePaint(Color.ORANGE); // 边框颜色
		renderer.setDrawBarOutline(true);
		renderer.setWallPaint(Color.gray);// 设置墙体颜色
		renderer.setMaximumBarWidth(0.05); // 设置柱子宽度
		renderer.setMinimumBarLength(0.1); // 设置柱子高度
		renderer.setSeriesPaint(0, Color.ORANGE); // 设置柱的颜色
		renderer.setItemMargin(0.0); // 设置每个地区所包含的平行柱的之间距离

		// 在柱子上显示相应信息
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setBaseItemLabelsVisible(true);
		renderer.setBaseItemLabelPaint(Color.BLACK);// 设置数值颜色，默认黑色
		// 搭配ItemLabelAnchor TextAnchor
		// 这两项能达到不同的效果，但是ItemLabelAnchor最好选OUTSIDE，因为INSIDE显示不出来
		 renderer.setBasePositiveItemLabelPosition(new
		 ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
		 TextAnchor.CENTER_LEFT));
		// 下面可以进一步调整数值的位置，但是得根据ItemLabelAnchor选择情况.
		renderer.setItemLabelAnchorOffset(10);
		/**
		 * plot 设置
		 ***/
		// 设置网格竖线颜色
		plot.setDomainGridlinePaint(Color.blue);
		plot.setDomainGridlinesVisible(true);
		// 设置网格横线颜色
		plot.setRangeGridlinePaint(Color.blue);
		plot.setRangeGridlinesVisible(true);
		// 图片背景色
		plot.setBackgroundPaint(Color.LIGHT_GRAY);
		plot.setOutlineVisible(true);
		// 图边框颜色
		plot.setOutlinePaint(Color.magenta);
		// 设置柱的透明度
		plot.setForegroundAlpha(0.8f);
		plot.setNoDataMessage("No data");
		// //将类型放到上面
		// plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
		// //将默认放到左边的数值放到右边
		// plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
		plot.setRenderer(renderer);// 将修改后的属性值保存到图中

		// OutputStream os = null;
		// try {
		// // File file = new File(path);
		// os = new FileOutputStream(path);
		// XlsWorkBook.deleteServerPic(path);
		// // 由ChartUtilities生成文件到一个体outputStream中去
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

	// 生成折綫圖1
	public JFreeChart createLineChart(String chartTitle, String x, String y,
			String path, CategoryDataset xyDataset) {
		OutputStream os = null;
		JFreeChart chart = null;
		try {
			chart = ChartFactory.createLineChart(chartTitle, x, y, xyDataset,
					PlotOrientation.VERTICAL, true, true, false);

			chart.setTextAntiAlias(false);
			chart.setBackgroundPaint(Color.WHITE);
			// 设置图标题的字体重新设置title
			Font font = new Font("宋体", Font.BOLD, 18);
			TextTitle title = new TextTitle(chartTitle);
			title.setFont(font);
			chart.setTitle(title);
			// 底部汉字乱码的问题
			chart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 15));

			// 设置面板字体
			Font labelFont = new Font("SansSerif", Font.TRUETYPE_FONT, 12);

			chart.setBackgroundPaint(Color.WHITE);

			CategoryPlot categoryplot = (CategoryPlot) chart.getPlot();
			// x轴 // 分类轴网格是否可见
			categoryplot.setDomainGridlinesVisible(true);
			// y轴 //数据轴网格是否可见
			categoryplot.setRangeGridlinesVisible(true);

			categoryplot.setRangeGridlinePaint(Color.DARK_GRAY);// 虚线色彩

			categoryplot.setDomainGridlinePaint(Color.DARK_GRAY);// 虚线色彩

			categoryplot.setBackgroundPaint(Color.lightGray);

			categoryplot.setNoDataMessage("No data");

			// 设置轴和面板之间的距离
			// categoryplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));

			CategoryAxis domainAxis = categoryplot.getDomainAxis(); // 对X轴做操作
			ValueAxis yAxis = categoryplot.getRangeAxis();// 对Y轴做操作
			// 设置Y轴坐标上的文字
			yAxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 12));
			// 设置Y轴的标题文字
			yAxis.setLabelFont(new Font("宋体", Font.PLAIN, 15));

			domainAxis.setLabelFont(labelFont);// 轴标题
			domainAxis.setTickLabelFont(labelFont);// 轴数值

			// Lable
			// 设置距离图片左端距离
			domainAxis.setLowerMargin(0.0);
			// 设置距离图片右端距离
			domainAxis.setUpperMargin(0.0);

			NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
			numberaxis
					.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
			numberaxis.setAutoRangeIncludesZero(true);

			// 获得renderer 注意这里是下嗍造型到lineandshaperenderer！！
			LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) categoryplot
					.getRenderer();

			lineandshaperenderer.setBaseShapesVisible(true); // series 点（即数据点）可见
			lineandshaperenderer.setBaseLinesVisible(true); // series
															// 点（即数据点）间有连线可见

			// 显示折点数据
			// lineandshaperenderer.setBaseItemLabelGenerator(new
			// StandardCategoryItemLabelGenerator());
			// lineandshaperenderer.setBaseItemLabelsVisible(true);

			// os = new FileOutputStream(path);
			// XlsWorkBook.deleteServerPic(path);
			// 由ChartUtilities生成文件到一个体outputStream中去
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

	// 生成折綫圖2
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
			// 由ChartUtilities生成文件到一个体outputStream中去
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
		dataset.addValue(60, "总数", "短信");
		dataset.addValue(1, "完成数量", "短信");
		dataset.addValue(5, "完成数量", "通话");
		dataset.addValue(4, "总数", "通话");
		dataset.addValue(0, "超时数量", "短信");
//		dataset.addValue(1, "总数", "短信");
//		dataset.addValue(13, "完成数量", "短信");
//		dataset.addValue(100, "完成数量", "通话");
//		dataset.addValue(45, "总数", "通话");
//		dataset.addValue(66, "超时数量", "短信");
		ChartUtil chartUtil = new ChartUtil();
		JFreeChart chart = chartUtil.createBarChart("增值业务信息统计", "业务类别",
				"数量", "", dataset);
		 OutputStream os = null;
		 try {
		 // File file = new File(path);
		 os = new FileOutputStream("c:/qqqq.jpeg");
		 // 由ChartUtilities生成文件到一个体outputStream中去
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
