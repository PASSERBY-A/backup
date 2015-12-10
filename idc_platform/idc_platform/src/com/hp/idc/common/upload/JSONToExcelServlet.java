/*
 * @(#)JSONToExcelServlet.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.common.upload;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.hp.idc.itsm.util.DateTimeUtil;
import com.hp.idc.json.JSONArray;
import com.hp.idc.json.JSONException;
import com.hp.idc.json.JSONObject;

/**
 * 
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 3:41:32 AM Sep 17, 2011
 * 
 */

public class JSONToExcelServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.setContentType("application/xml;charset=gbk");
		response.setCharacterEncoding("gbk");
		request.setCharacterEncoding("gbk");

		String JSONStr = request.getParameter("JSONStr");

		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject(JSONStr);

			String fileName = null;

			JSONArray headja = null;

			JSONArray valja = null;

			JSONArray sheetja = null;

			fileName = jsonObj.getString("fileName");
			if ((fileName == null) || (fileName.equals("")))
				fileName = "data.xls";
			//fileName = URLEncoder.encode(fileName, "utf-8");
			
			fileName = StringUtil.getEncodeStr(fileName
					+ DateTimeUtil.formatDate(new Date(), "yyyy-MM-dd")+".xls",
					"GB2312", "ISO8859-1");
			
			response.reset();
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);

			Workbook wb = new HSSFWorkbook();
			
			if (jsonObj.has("sheet"))
				sheetja = jsonObj.getJSONArray("sheet");
			else {
				sheetja = new JSONArray("[{name:'sheet',_sys_create:true}]");
			}
			for (int sheetIndex = 0; sheetIndex < sheetja.length(); sheetIndex++) {
				JSONObject sheetjo = sheetja.getJSONObject(sheetIndex);
				String sheetName = "sheet" + sheetIndex;
				if (sheetjo.has("sheetName"))
					sheetName = sheetjo.getString("sheetName");

				Sheet sheet = wb.createSheet(sheetName);
				if ((sheetjo.has("_sys_create"))
						&& (sheetjo.getBoolean("_sys_create"))) {
					headja = jsonObj.getJSONArray("head");
					valja = jsonObj.getJSONArray("value");
				} else {
					headja = sheetjo.getJSONArray("head");
					valja = sheetjo.getJSONArray("value");
				}
				try {
					JSONArray names = new JSONArray();
					Row row = sheet.createRow(0);
					if (headja != null) {
						for (int i = 0; i < headja.length(); i++) {
							JSONObject jo = headja.getJSONObject(i);
							if (jo != null)
								names.put(jo.getString("id"));
							writeHead(jo, row, i);
						}
					}
					if (valja != null)
						for (int i = 0; i < valja.length(); i++) {
							JSONObject jo = valja.getJSONObject(i);
							writeValue(names, jo, sheet, i + 1);
						}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			OutputStream out = response.getOutputStream();
			out.flush();
			wb.write(out);
			out.close();
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
	}

	private void writeHead(JSONObject heads, Row row, int col) {
		if ((heads == null) || (heads.length() == 0))
			return;
		try {
			String value = heads.optString("name");
			row.createCell(col).setCellValue(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void writeValue(JSONArray names, JSONObject values,
			Sheet sheet, int row) {
		if ((names == null) || (names.length() == 0))
			names = values.names();
		Row _row = sheet.createRow(row);		
		try {
			for (int j = 0; j < names.length(); j++) {
				String value = values.optString(names.getString(j));
				_row.createCell(j).setCellValue(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
