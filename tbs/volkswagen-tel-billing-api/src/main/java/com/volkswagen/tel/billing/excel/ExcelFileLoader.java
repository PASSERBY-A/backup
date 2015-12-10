package com.volkswagen.tel.billing.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tool class for reading and parsing excel file. 
 * Supporting excel version 2003 and 2007 above.
 * @author Zhixiong
 *
 */
public class ExcelFileLoader {
	private static final Logger log = LoggerFactory.getLogger(ExcelFileLoader.class);
	
	private Workbook workbook;
	private FormulaEvaluator evaluator;
	private Map<String, Sheet> sheetMap;
	private SimpleDateFormat dateFormat;
	public static int defaultDecimal = 2;
	
	public ExcelFileLoader() {
		this.sheetMap = new HashMap<String, Sheet>();
		this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	}
	
	public ExcelFileLoader(SimpleDateFormat defaultDateFormat, Integer decimal) {
		this.sheetMap = new HashMap<String, Sheet>();
		if (defaultDateFormat == null) {
			this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		} else {
			this.dateFormat = defaultDateFormat;
		}
		if (decimal != null) {
			defaultDecimal = decimal.intValue();
		}
	}
	
	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * Load an excel file by path
	 * @param filePath
	 * @throws Exception
	 */
	public void load(String filePath) throws Exception {
		if (filePath != null && !filePath.isEmpty()) {
			File file = new File(filePath);
			load(file);
		} else {
			throw new Exception("Excel file path is null or empty.");
		}
	}
	
	/**
	 * Load an excel from File
	 * @param file
	 * @throws Exception 
	 */
	public void load(File file) throws Exception {
		if (file == null) {
			throw new Exception("excel file is null");
		} else if (!file.exists()) {
			throw new Exception("excel file does not exist, path: ["
					+ file.getPath() + "]");
		} else if (!file.isFile()) {
			throw new Exception("Destination file is not valid file, path: [" + file.getPath()
					+ "]");
		}

		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			load(inputStream);
		} catch (Exception e1) {
			log.error(e1.getMessage(), e1);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e1) {
				log.error(e1.getMessage(), e1);
			}
		}
	}
	
	/**
	 * Load an excel from InputStream
	 * @param inputStream
	 * @throws Exception
	 */
	public void load(InputStream inputStream) throws Exception{
        if(!inputStream.markSupported()){  
        	inputStream = new PushbackInputStream(inputStream,8);           
        }
		
		if(POIFSFileSystem.hasPOIFSHeader(inputStream)){
			this.workbook = (HSSFWorkbook) WorkbookFactory.create(inputStream);
			this.evaluator = new HSSFFormulaEvaluator((HSSFWorkbook)this.workbook);
		}else if (POIXMLDocument.hasOOXMLHeader(inputStream)){
			this.workbook = (XSSFWorkbook) WorkbookFactory.create(inputStream);
			this.evaluator = new XSSFFormulaEvaluator((XSSFWorkbook)this.workbook);
		}else{
			throw new Exception("Unknown excel file version");
		}
		
		if(this.workbook!=null){
			int sheetNumber = this.workbook.getNumberOfSheets();
			
			//clear used map
			this.sheetMap.clear();
			for (int i = 0; i < sheetNumber; i++) {
				Sheet sheet = this.workbook.getSheetAt(i);
				String useName = sheet.getSheetName();
				this.sheetMap.put(useName, sheet);
			}
		}
	}
	
	/**
	 * Remove zero border from double
	 * @param s
	 * @return
	 */
	public static String removeZeroBorder(String s) {
		if (s != null && !"".equals(s)) {
			// replace xxx.000 to xxx
			String result = s;
			Pattern pattern = Pattern.compile("\\.0+\\b");
			Matcher matcher = pattern.matcher(result);
			if (matcher.find()) {
				result = matcher.replaceAll("");
			}

			// replace xxx.xxx000 to xxx.xxx
			pattern = Pattern.compile("\\-?\\d+\\.0*[^0]+");
			matcher = pattern.matcher(result);
			if (matcher.find()) {
				result = matcher.group(0);
			}

			return result;
		}

		return null;
	}
	
	/**
	 * 
	 * @param value
	 * @param decimals
	 * @return
	 */
	public static String processNumericCellValue(double value, Integer decimals) {
		int useDecimal = defaultDecimal;
		if (decimals != null) {
			useDecimal = decimals.intValue();
		}
		StringBuffer s = new StringBuffer("0");
		if (useDecimal > 0) {
			s.append(".");
			for (int i = 0; i < useDecimal; i++) {
				s.append("0");
			}
		}
		DecimalFormat format = new DecimalFormat(s.toString());
		String result = format.format(value);

		// remove zero border
		result = removeZeroBorder(result);
		return result;
	}
	
	/**
	 * Get data from particular cell
	 * @param cell
	 * @param decimals
	 * @return
	 * @throws Exception
	 */
	public String getCellData(Cell cell, Integer decimals) throws Exception {
		if (cell != null) {
			String cellValue = null;

			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING: // - String
				cellValue = cell.getRichStringCellValue().getString();
				break;
			case Cell.CELL_TYPE_NUMERIC: // - Numeric
				boolean isDate = false;
				try {
					if (DateUtil.isCellDateFormatted(cell)) {
						Date date = cell.getDateCellValue();
						cellValue = this.dateFormat.format(date);
						isDate = true;
					}
				} catch (Exception e) {
					log.error("Can not get value use Date format");
					log.error(e.getMessage(), e);
				}
				if (!isDate) {
					cellValue = processNumericCellValue(
							cell.getNumericCellValue(), decimals);
				}
				break;
			// boolean
			case Cell.CELL_TYPE_BOOLEAN:
				cellValue = String.valueOf(cell.getBooleanCellValue());
				break;
			// formula
			case Cell.CELL_TYPE_FORMULA:
				// compute cell value
				CellValue computedValue = this.evaluator.evaluate(cell);
				int cellType = computedValue.getCellType();

				// ***************get cell value from computed result***********
				switch (cellType) {
				// string
				case Cell.CELL_TYPE_STRING:
					cellValue = computedValue.getStringValue();
					break;
				// numeric
				case Cell.CELL_TYPE_NUMERIC:
					cellValue = processNumericCellValue(
							computedValue.getNumberValue(), decimals);
					break;
				// boolean
				case Cell.CELL_TYPE_BOOLEAN:
					cellValue = String.valueOf(computedValue.getBooleanValue());
					break;
				// formula
				case Cell.CELL_TYPE_FORMULA:
					// compute cell value
					cellValue = computedValue.getStringValue();
					break;
				// blank
				default:
					cellValue = "";
				}
				// ***************************************************************
				break;
			// blank
			default:
				cellValue = "";
			}
			return cellValue;
		} else {
			throw new Exception("Cannot get value from a null cell");
		}
	} 
	
	/**
	 * Read data from particular period of rows
	 * @param sheet
	 * @param beginRowNum
	 * @param endRowNum
	 * @param decimals
	 * @return
	 * @throws Exception
	 */
	public List<List<String>> getDataBetweenRows(Sheet sheet, int beginRowNum,
			int endRowNum, Integer decimals) throws Exception {
		if (sheet != null) {
			sheet.getFirstRowNum();

			List<List<String>> list = new ArrayList<List<String>>();
			for (int i = beginRowNum; i <= endRowNum; i++) {
				// Iterate lines from the 1st non-empty to the last.
				try {
					Row row = sheet.getRow(i);
					List<String> lineData = new ArrayList<String>();
					for (int j = 0; j < row.getLastCellNum(); j++) {
						// iterate cells from the 1st to the last non-empty.
						Cell cell = row.getCell(j);
						if (cell != null) {
							String cellValue = getCellData(cell, decimals);
							lineData.add(cellValue);
						} else {
							lineData.add(null);
						}
					}
					list.add(lineData);
				} catch (Exception e) {
					// Skip the current to the next, when hitting empty line.
					continue;
				}
			}
			return list;
		} else {
			throw new Exception("Cannot get value from a null sheet");
		}
	}
	
	/**
	 * Read all data from first non-empty line to the last time.
	 * @param sheetName
	 * @param decimals
	 * @return
	 * @throws Exception
	 */
	public List<List<String>> getAllDataByRow(String sheetName, Integer decimals)
			throws Exception {
		Sheet sheet = null;
		sheet = this.sheetMap.get(sheetName);

		if (sheet != null) {
			int beginRowNum = sheet.getFirstRowNum();
			int endRowNum = sheet.getLastRowNum();
			return getDataBetweenRows(sheet, beginRowNum, endRowNum, decimals);
		} else {
			throw new Exception("Unknown sheet name [" + sheetName + "]");
		}
	}
	
	/**
	 * Read all data from first non-empty line to the last time.
	 * @param sheetIndex
	 * @param decimals
	 * @return
	 * @throws Exception
	 */
	public List<List<String>> getAllDataByRow(int sheetIndex, Integer decimals)
			throws Exception {
		Sheet sheet = null;
		sheet = this.workbook.getSheetAt(sheetIndex);

		if (sheet != null) {
			int beginRowNum = sheet.getFirstRowNum();
			int endRowNum = sheet.getLastRowNum();
			return getDataBetweenRows(sheet, beginRowNum, endRowNum, decimals);
		} else {
			throw new Exception("Unknown sheet on [" + sheetIndex + "]");
		}
	}

}
