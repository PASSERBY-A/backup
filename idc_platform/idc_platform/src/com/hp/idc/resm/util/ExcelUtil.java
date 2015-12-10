/*
 * @(#)ExcelUtil.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.resm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.hp.idc.resm.model.Model;
import com.hp.idc.resm.model.ModelAttribute;
import com.hp.idc.resm.resource.ResourceObject;
import com.hp.idc.resm.service.ServiceManager;

/**
 * 生成模型模板, 导入模板数据以及导出数据
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 9:10:35 AM Sep 16, 2011
 * 
 */

public class ExcelUtil {

	/**
	 * 生成指定模型的导入模板Excel文件
	 * 
	 * @param id
	 *            模型ID
	 * @return Excel文件名称
	 */
	public String getModelExcel(String id) {
		List<ModelAttribute> list = ServiceManager.getModelService()
				.getModelAttributesByModelId(id);

		// 在模板中不生成的属性
		String[] IGNORATTR = new String[] { "id", "create_time",
				"contract_start", "searchcode", "last_update_time",
				"contract_end", "task_link", "order_id", "customer_id",
				"status", "last_update_by" };
		List<String> l = new ArrayList<String>(Arrays.asList(IGNORATTR));

		Workbook wb = new HSSFWorkbook();
		// Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet(id);
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setColor(HSSFColor.RED.index);
		style.setFont(font);
		// Create a row and put some cells in it. Rows are 0 based.
		Row row = sheet.createRow((short) 0);
		int i = 0;
		Cell cell = null;
		HSSFRichTextString textString;
		for (ModelAttribute ma : list) {
			if (l.contains(ma.getAttrId()))
				continue;
			cell = row.createCell(i);
			textString = new HSSFRichTextString(ma.getName() + "/" + ma.getAttrId());
			cell.setCellValue(textString);
			if (!ma.isNullable())
				cell.setCellStyle(style);
			sheet.autoSizeColumn(i);
			i++;
		}
		for(int k=0;k<list.size();k++){
			sheet.autoSizeColumn(k);
		}
		// Write the output to a file
		FileOutputStream fileOut;
		String file;
		try {
			file = System.getProperty("user.dir") + "/../temp/" + id + new Random().nextLong() + ".xls";
			fileOut = new FileOutputStream(file);
			wb.write(fileOut);
			fileOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "路径不存在";
		} catch (IOException e) {
			e.printStackTrace();
			return "写入出错";
		}
		return file;
	}

	/**
	 * 读取模板文件内容,一行即为一个资源
	 * 
	 * @param fileName
	 *            excel文件, 必须是由getModelExcel必须是由生成的
	 * @return 资源数据
	 * @throws FileNotFoundException 
	 */
	public Map<String, String> readModelExcel(File file, String modelId) {
		if(modelId == null)
			return null;
		Map<String, String> m = new HashMap<String, String>();
		try {
			InputStream in = new FileInputStream(file);
			Workbook wb;
			try{
				wb = new HSSFWorkbook(in);
			} catch (IllegalArgumentException e) {
				wb = new XSSFWorkbook(in);
			}
			Sheet sheet = wb.getSheetAt(0);
			int total = sheet.getLastRowNum();
			Row row0 = sheet.getRow(0);
			String[] head = new String[row0.getLastCellNum()];
			for (int j = 0; j < row0.getLastCellNum(); j++) {
				String[] str = row0.getCell(j).getStringCellValue().split("/");
				if (str.length == 2) {
					head[j] = str[1];
				} else {
					head[j] = "";
				}
				System.out.println(head[j]);
			}
			Row row = null;
			Cell cell = null;
			for (int i = 1; i < total; i++) {
				m.clear();
				row = sheet.getRow(i);
				for (int j = 0; j < row.getLastCellNum(); j++) {
					cell = row.getCell(j);
					m.put(head[j], cell.getStringCellValue());
					System.out.println(head[j] + "--"
							+ cell.getStringCellValue());
				}
				// ServiceManager.getResourceUpdateService().addResource(modelId,
				// m, 1);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			file.delete();
		}
		return m;
	}

	/**
	 * 获取指定模型的资源数据, 生成Excel文件
	 * 
	 * @param modelId
	 *            模型Id
	 * @return Excel文件名称
	 */
	public String getResouceDataToExcel(String modelId) {
		Model m = ServiceManager.getModelService().getModelById(modelId);
		
		List<ResourceObject> l = ServiceManager.getResourceService()
				.getResourcesByModelId(modelId, 1);

		List<ModelAttribute> mas = m.getAttributes();
		Workbook wb = new HSSFWorkbook();
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short)12);
		font.setFontName( "黑体" ); 
		style.setFont(font);
		Sheet sheet = wb.createSheet(modelId);
		Row row = sheet.createRow(0);
		int i = 0;
		HSSFRichTextString textString;
		for(ModelAttribute ma : mas){
			Cell cell = row.createCell(i); 
			textString = new HSSFRichTextString(ma.getDefine().getName());
			cell.setCellStyle(style);
			cell.setCellValue(textString);
			i++;
		}
		i=1;
		for(ResourceObject ro : l){
			row = sheet.createRow(i);
			int j = 0;
			for(ModelAttribute ma : mas){
				textString = new HSSFRichTextString(ro.getAttributeValue(ma.getAttrId()));
				row.createCell(j).setCellValue(textString);
				j++;
			}
			i++;
		}
		
		for(int k = 0; k < mas.size(); k++){
			sheet.autoSizeColumn(k);
		}
		
		// Write the output to a file
		FileOutputStream fileOut;
		String file;
		try {
			file = System.getProperty("user.dir") + "/../temp/" + modelId+new Random().nextLong() + "_data.xls";
			fileOut = new FileOutputStream(file);
			wb.write(fileOut);
			fileOut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "路径不存在";
		} catch (IOException e) {
			e.printStackTrace();
			return "写入出错";
		}
		return file;
	}
}
