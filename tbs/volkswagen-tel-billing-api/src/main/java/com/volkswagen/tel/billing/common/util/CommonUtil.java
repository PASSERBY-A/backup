package com.volkswagen.tel.billing.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonUtil {

	private static final Logger log = LoggerFactory.getLogger(CommonUtil.class);

	public static String getDateStr() {
		return formatDate(Calendar.getInstance().getTime(), "yyyy/MM/dd");
	}

	public static String formatDate(Date date, String format) {
		if (date == null) {
			return "";
		}
		DateFormat df = new SimpleDateFormat(format);
		String str = df.format(date);
		return str;
	}

	public static Date toDate(String dateStr, String format) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		Date rtn = null;
		try {
			rtn = simpleDateFormat.parse(dateStr);
		} catch (ParseException e) {
			log.error(e.getMessage(), e);
		}
		return rtn;
	}

	public static Calendar getCalendar(int year, int month, int date, int hour,
			int minute, int second) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DATE, date);
		cal.set(Calendar.HOUR, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);

		return cal;
	}

	public static String convertSecondsToHHMMSS(long input) {
		int day = (int) TimeUnit.SECONDS.toDays(input);
		long hours = TimeUnit.SECONDS.toHours(input) - (day * 24);
		long minute = TimeUnit.SECONDS.toMinutes(input)
				- (TimeUnit.SECONDS.toHours(input) * 60);
		long second = TimeUnit.SECONDS.toSeconds(input)
				- (TimeUnit.SECONDS.toMinutes(input) * 60);

		String hh = (hours < 10) ? "0".concat(Long.toString(hours)) : Long
				.toString(hours);
		String mm = (minute < 10) ? "0".concat(Long.toString(minute)) : Long
				.toString(minute);
		String ss = (second < 10) ? "0".concat(Long.toString(second)) : Long
				.toString(second);
		return hh.concat(":").concat(mm).concat(":").concat(ss);
	}

	public static String prettyXMLFormat(String input, int indent) {
		String rtn = null;
		try {
			Source xmlInput = new StreamSource(new StringReader(input));
			StringWriter stringWriter = new StringWriter();
			StreamResult xmlOutput = new StreamResult(stringWriter);
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			transformerFactory.setAttribute("indent-number", indent);
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "html");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(xmlInput, xmlOutput);
			rtn = xmlOutput.getWriter().toString();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return rtn;
	}

	public static String readFromFile(String filePath) {
		File file = new File(filePath);
		FileReader reader = null;
		char[] bb = new char[1024];
		StringBuilder str = new StringBuilder();
		int n;// length of bytes to read every time.

		try {
			reader = new FileReader(file);
			while ((n = reader.read(bb)) != -1) {
				str.append(new String(bb, 0, n));
			}
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		log.info(">>> " + str);

		return str.toString();
	}

	public static boolean writeToFile(String filePath, String content) {
		boolean rtn = false;

		File file = new File(filePath);
		if (!file.exists()) { // - if file does not exist, create it.
			try {
				file.createNewFile();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}

		FileWriter writer = null;
		try {
			writer = new FileWriter(file);

			writer.write(content);
			writer.flush();
			rtn = true;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		}

		return rtn;
	}

	public static int generateRandomNumber(int min, int max) {
		return (int) (Math.random() * (max - min) + min);
	}

	public static int getCurrentMonth() {
		Calendar c = Calendar.getInstance();

		c.setTime(new Date());

		int month = c.get(Calendar.MONTH) + 1;

		return month;

	}

	public static int getCurrentYear() {

		Calendar c = Calendar.getInstance();

		c.setTime(new Date());

		int year = c.get(Calendar.YEAR);

		return year;

	}
	public static String getLastMonth(String format) {

		Calendar c = Calendar.getInstance();

		c.setTime(new Date());
		
		c.add(Calendar.MONTH, -1);
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		return sdf.format(c.getTime());

	}

	
	
	

	public static void main(String[] args) {
		
		
		
		System.out.println(getLastMonth("yyyy-MM"));
		

	}

}
