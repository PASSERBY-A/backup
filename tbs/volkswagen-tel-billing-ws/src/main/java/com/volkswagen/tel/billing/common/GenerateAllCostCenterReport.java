package com.volkswagen.tel.billing.common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.rpc.ServiceException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.ibm.itim.ws.exceptions.WSApplicationException;
import com.ibm.itim.ws.exceptions.WSLoginServiceException;
import com.ibm.itim.ws.exceptions.WSUnsupportedVersionException;
import com.volkswagen.tel.billing.billcall.biz.ReportBizService;
import com.volkswagen.tel.billing.billcall.biz.ReportLocalBizService;
import com.volkswagen.tel.billing.billcall.jpa.domain.CostCenterReportEntity;
import com.volkswagen.tel.billing.billcall.jpa.domain.CostCenterType;
import com.volkswagen.tel.billing.billcall.jpa.service.CostCenterReportDaoService;
import com.volkswagen.tel.billing.ldap.TIMService;
@Component
public class GenerateAllCostCenterReport {

	@Autowired
	private ReportBizService reportBiz;
	
	@Autowired
	private CostCenterReportDaoService costCenterReportDaoService;
	
	@Autowired
	private TIMService timService;
	
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryData(String type,int year,int month) {
		
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		
		 List<CostCenterReportEntity> l = costCenterReportDaoService.findByCostCenter(type, year, month);
		
		 for(CostCenterReportEntity entity:l)
		 {
			 
			    Map<String, String> map = new HashMap<String, String>();
			 	map.put("cell0", entity.getCostCenter()); 
				map.put("cell1",String.valueOf(entity.getFixedPhoneCost())); 
				map.put("cell2", String.valueOf(entity.getCellphoneCost())); 
				result.add(map);
		 }
 
		return result;

	}

//	public static void main(String[] args) throws Exception {
//
//		GenerateALLHostReport g = new GenerateALLHostReport();
//
//		HSSFWorkbook wb = g.generate("2014-11-12", "2014-11-12");
//
//		// 这里定义你自己需要的输出流
//		FileOutputStream os = new FileOutputStream("c:\\a.xls");
//
//		wb.write(os);
//
//		os.flush();
//		os.close();
//
//	}
	
	public static void main(String[] args) throws Exception {
		
		GenerateAllCostCenterReport g = new GenerateAllCostCenterReport();
		
		ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		g = app.getBean(GenerateAllCostCenterReport.class);
		
		FileOutputStream os = new FileOutputStream("d:\\zz.zip");
		
		/* HSSFWorkbook wb = g.generate(CostCenterType.VCRA.toString(),2015,8);
		
		FileOutputStream os = new FileOutputStream("d:\\vcra.xls");
		
		wb.write(os);
		os.flush();
		os.close();*/
		
		OutputStream out =  g.generateZip(os,2015,8);
		
		out.flush();
		out.close();
		
		/*List<File> f = new ArrayList<File>();
		f.add(new File("d://test.pdf"));
		f.add(new File("d://aa.properties"));
		f.add(new File(new String("d://PuHua考勤表9月.doc".getBytes(),"utf-8")));
		f.add(new File(new String("d://PuHua考勤表10月 - .doc".getBytes(),"utf-8")));
		
		
		zip("d://a.zip",f);*/
	}
	
	
	
	public OutputStream generateZip(OutputStream out ,int year,int month) throws Exception
	{
		
		List<File> files = new ArrayList<File>();
		
		String filePath = FileUtils.getTempDirectoryPath(); 
		
		File vcic = new File(filePath+"vcic.xls");
		File vcra = new File(filePath+"vcra.xls");
		File vgic = new File(filePath+"vgic.xls");
		File audi = new File(filePath+"audi.xls");
		
		files.add(vcic);
		files.add(vcra);
		files.add(vgic);
		files.add(audi);
		
		
		FileOutputStream b1 = new FileOutputStream(vcic);
		FileOutputStream b2 = new FileOutputStream(vcra);
		FileOutputStream b3 = new FileOutputStream(vgic);
		FileOutputStream b4 = new FileOutputStream(audi);
		
		
		HSSFWorkbook wb1 = generate(CostCenterType.VCIC.toString(),year,month);
		HSSFWorkbook wb2 = generate(CostCenterType.VCRA.toString(),year,month);
		HSSFWorkbook wb3 = generate(CostCenterType.VGIC.toString(),year,month);
		HSSFWorkbook wb4 = generate(CostCenterType.AUDI.toString(),year,month);
		
		wb1.write(b1);
		wb2.write(b2);
		wb3.write(b3);
		wb4.write(b4);
		 
		b1.close();
		b2.close();
		b3.close();
		b4.close();
		
		return zip(out, files);
		
	}
	
	

	public HSSFWorkbook generate(String type,int year,int month) throws IOException {

		// 创建Excel的工作书册 Workbook,对应到一个excel文档
		HSSFWorkbook wb = new HSSFWorkbook();
		// 创建Excel的工作sheet,对应到一个excel文档的tab
		HSSFSheet sheet = wb.createSheet("sheet1");
		// 设置excel每列宽度
		sheet.setColumnWidth(0, 4000);
		sheet.setColumnWidth(1, 3500);

		HSSFFont font = setFont(wb);

		HSSFCellStyle style = setStyle(wb, font);

		createTitle(sheet, style);
		
			sheet.autoSizeColumn((short)0); //调整第一列宽度
	        sheet.autoSizeColumn((short)1); //调整第二列宽度
	        sheet.autoSizeColumn((short)2); //调整第三列宽度
	        sheet.autoSizeColumn((short)3); //调整第四列宽度
	        sheet.autoSizeColumn((short)4);
	        sheet.autoSizeColumn((short)5);
	        sheet.autoSizeColumn((short)6);
	        sheet.autoSizeColumn((short)7);
	        sheet.autoSizeColumn((short)8);
	        sheet.autoSizeColumn((short)9);
	        sheet.autoSizeColumn((short)10);
	        sheet.autoSizeColumn((short)11);
	        sheet.autoSizeColumn((short)12);
	        sheet.autoSizeColumn((short)13);
	        sheet.autoSizeColumn((short)14);
	        sheet.autoSizeColumn((short)15);
	        
		List<Map<String, String>> data = queryData(type,year,month);

		HSSFRow contentRow = sheet.createRow(1);
		// 设置单元格的样式格式
		HSSFCellStyle style1 = wb.createCellStyle();
		for (int i = 0; i < data.size(); i++) {

			// 设置单元格内容格式
			style1 = wb.createCellStyle();
			// style1.setDataFormat(HSSFDataFormat.getBuiltinFormat("h:mm:ss"));
			style1.setWrapText(true);// 自动换行
			contentRow = sheet.createRow(i + 1);
			style1.setVerticalAlignment((short) 1);

			for (int j = 0; j < data.get(i).size(); j++) {
				// 设置单元格的样式格式
				HSSFCell cell = contentRow.createCell(j);
				cell.setCellStyle(style1);
				String content = data.get(i).get("cell" + String.valueOf(j));

				if ((j == 9 || j == 8 || j == 4) && !"".equals(content)
						&& null != content) {
					String[] c = content.split(",");
					StringBuffer sb = new StringBuffer();
					for (int ii = 0; ii < c.length; ii++) {
						sb.append(c[ii] + "\n");

					}
					content = sb.toString();
				}
				cell.setCellValue(content);
			}

		}/**/

		return wb;

	}

	private static void createTitle(HSSFSheet sheet, HSSFCellStyle style) {

		// 创建Excel的sheet的一行
		HSSFRow row = sheet.createRow(0);

		row.setHeight((short) 500);// 设定行的高度

		// 创建一个Excel的单元格
		HSSFCell cell0 = row.createCell(0);
		// 合并单元格(startRow，endRow，startColumn，endColumn)
		// sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
		// 给Excel的单元格设置样式和赋值
		cell0.setCellStyle(style);
		cell0.setCellValue("Cost Center");

		HSSFCell cell1 = row.createCell(1);
		cell1.setCellStyle(style); 
		cell1.setCellValue("Fixed Phone Cost");

		HSSFCell cell2 = row.createCell(2);
		cell2.setCellStyle(style);
		cell2.setCellValue("Cellphone Cost");

 

	}

	private static HSSFFont setFont(HSSFWorkbook wb) {
		// 创建字体样式
		HSSFFont font = wb.createFont();
		font.setFontName("SansSerif");
		font.setBoldweight((short) 100);
		font.setFontHeight((short) 200);
		// font.setColor(HSSFColor.GREY_25_PERCENT.i);
		return font;
	}

	private static HSSFCellStyle setStyle(HSSFWorkbook wb, HSSFFont font) {
		// 创建单元格样式
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// style.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
		style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		// 设置边框
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		style.setTopBorderColor(HSSFColor.BLACK.index);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setFont(font);// 设置字体
		return style;
	}
	
	
	public OutputStream  zip(OutputStream out, List<File> inputFiles) throws Exception 
    {  
			ZipOutputStream result = new ZipOutputStream(out);  
		    zip(result, inputFiles);  
		    return result;
		    
    }  
  
	public static void zip(ZipOutputStream out, List<File> files)  throws Exception {  
		
    	for(File f:files)
    	{
    		out.putNextEntry(new ZipEntry(new String(f.getName().getBytes(),"utf-8")));  
    		FileInputStream in = new FileInputStream(f);  
    		IOUtils.copy(in, out);
    		IOUtils.closeQuietly(in);
    		FileUtils.deleteQuietly(f);
    	}
       
    	
    	
    	
    }  
  
    
    
	
	
}