package com.hp.idc.portal.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期转换工具类
 * @author meiy
 */
public class DateTimeUtil {

	public static String toChinese(long sec) {
		String ret = "";
		long d = sec / 86400;
		sec %= 86400;
		if (d > 0)
			ret += "" + d + "天";
		if (sec == 0 && ret.length() > 0)
			return ret;
		long h = sec / 3600;
		sec %= 3600;
		if (h > 0 || d > 0)
			ret += "" + h + "小时";
		if (sec == 0 && ret.length() > 0)
			return ret;
		long m = sec / 60;
		sec %= 60;
		if (m > 0 || h > 0 || d > 0)
			ret += "" + m + "分钟";
		if (sec > 0 || ret.length() == 0)
			ret += "" + sec + "秒";
		return ret;
	}
	
	public static Date parseDate(String str, String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(str);
	}
	
	public static Date parseDate(String str) throws ParseException {
		return parseDate(str, "yyyyMMddHHmmss");
	}

	/**
	 * 将 20061001000000 转为 2006-10-01 00:00:00
	 * @param str 要转换的字符串
	 * @return 转换后的字符串
	 */
	public static String formatDateTime(String str) {
		return str.substring(0, 4)
				+ "-" + str.substring(4, 6)
				+ "-" + str.substring(6, 8)
				+ " " + str.substring(8, 10)
				+ ":" + str.substring(10, 12)
				+ ":" + str.substring(12, 14);
	}
	
	/**
	 * 将 20061001 转为 2006-10-01
	 * @param str 要转换的字符串
	 * @return 转换后的字符串
	 */
	public static String formatDate(String str) {
		return str.substring(0, 4)
				+ "-" + str.substring(4, 6)
				+ "-" + str.substring(6, 8);
	}
	
	/**
	 * 用指定的格式格式化时间
	 * @param date 要转换的日期
	 * @param format 指定的格式
	 * @return 转换后的字符串
	 */
	public static String formatDate(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	/**
	 * 格式化时间为yyyyMMddHHmmss
	 * @param date 要转换的日期
	 * @return 转换后的字符串
	 */
	public static String formatDate(Date date) {
		return formatDate(date, "yyyyMMddHHmmss");
	}

	/**
	 * 用指定的格式格式化时间
	 * @param date 要转换的日期
	 * @param format 指定的格式
	 * @return 转换后的字符串
	 */
	public static String formatDate(long date, String format) {
		return formatDate(new Date(date), format);
	}

	/**
	 * 格式化时间为yyyyMMddHHmmss
	 * @param date 要转换的日期
	 * @return 转换后的字符串
	 */
	public static String formatDate(long date) {
		return formatDate(new Date(date));
	}
}
