package com.hp.idc.portal.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ����ת��������
 * @author meiy
 */
public class DateTimeUtil {

	public static String toChinese(long sec) {
		String ret = "";
		long d = sec / 86400;
		sec %= 86400;
		if (d > 0)
			ret += "" + d + "��";
		if (sec == 0 && ret.length() > 0)
			return ret;
		long h = sec / 3600;
		sec %= 3600;
		if (h > 0 || d > 0)
			ret += "" + h + "Сʱ";
		if (sec == 0 && ret.length() > 0)
			return ret;
		long m = sec / 60;
		sec %= 60;
		if (m > 0 || h > 0 || d > 0)
			ret += "" + m + "����";
		if (sec > 0 || ret.length() == 0)
			ret += "" + sec + "��";
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
	 * �� 20061001000000 תΪ 2006-10-01 00:00:00
	 * @param str Ҫת�����ַ���
	 * @return ת������ַ���
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
	 * �� 20061001 תΪ 2006-10-01
	 * @param str Ҫת�����ַ���
	 * @return ת������ַ���
	 */
	public static String formatDate(String str) {
		return str.substring(0, 4)
				+ "-" + str.substring(4, 6)
				+ "-" + str.substring(6, 8);
	}
	
	/**
	 * ��ָ���ĸ�ʽ��ʽ��ʱ��
	 * @param date Ҫת��������
	 * @param format ָ���ĸ�ʽ
	 * @return ת������ַ���
	 */
	public static String formatDate(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	/**
	 * ��ʽ��ʱ��ΪyyyyMMddHHmmss
	 * @param date Ҫת��������
	 * @return ת������ַ���
	 */
	public static String formatDate(Date date) {
		return formatDate(date, "yyyyMMddHHmmss");
	}

	/**
	 * ��ָ���ĸ�ʽ��ʽ��ʱ��
	 * @param date Ҫת��������
	 * @param format ָ���ĸ�ʽ
	 * @return ת������ַ���
	 */
	public static String formatDate(long date, String format) {
		return formatDate(new Date(date), format);
	}

	/**
	 * ��ʽ��ʱ��ΪyyyyMMddHHmmss
	 * @param date Ҫת��������
	 * @return ת������ַ���
	 */
	public static String formatDate(long date) {
		return formatDate(new Date(date));
	}
}
