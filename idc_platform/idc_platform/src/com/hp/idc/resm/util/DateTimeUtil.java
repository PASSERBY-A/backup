/**
 * 
 */
package com.hp.idc.resm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * ����ʱ�乤��
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 *
 */
public class DateTimeUtil {

	/**
	 * ȥ�����ں����ʱ�䲿�֣���2011.1.25 5:00�����к�᷵��2011.1.25 00:00
	 * @param l ʱ��
	 * @return ֻ�������ڵ�ʱ��
	 */
	public static long getDate(long l) {
		long off = TimeZone.getDefault().getRawOffset();
		return (l + off) / 86400000 * 86400000 - off;
	}
	
	/**
	 * ָ�����ڵ����һ�룬��2011.1.25 5:00�����к�᷵��2011.1.25 23:59:59
	 * @param l ʱ��
	 * @return ָ�����ڵ����һ��
	 */
	public static long getDateEnd(long l) {
		return getDate(l) + (86400000 - 1);
	}
	
	/**
	 * ָ�����ڵ��³�����2011.1.25 5:00�����к�᷵��2011.1.1 00:00
	 * @param l ʱ��
	 * @return ָ�����ڵ��³�
	 */
	public static long getMonth(long l) {
		java.util.GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(getDate(l));
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTimeInMillis();
	}
	
	/**
	 * ָ�����ڵ��������2011.3.25 5:00�����к�᷵��2011.1.1 00:00
	 * @param l ʱ��
	 * @return ָ�����ڵ����
	 */
	public static long getYear(long l) {
		java.util.GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(getDate(l));
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, 0);
		return cal.getTimeInMillis();
	}
	
	/**
	 * ָ�����ڵ���ĩ����2011.3.25 5:00�����к�᷵��2011.12.31 23:59:59
	 * @param l ʱ��
	 * @return ָ�����ڵ���ĩ
	 */
	public static long getYearEnd(long l) {
		java.util.GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(getDate(l));
		cal.set(Calendar.DAY_OF_MONTH, 31);
		cal.set(Calendar.MONTH, 11);
		return cal.getTimeInMillis() + (86400000 - 1);
	}
	
	/**
	 * ָ�����ڵ���ĩ����2011.3.25 5:00�����к�᷵��2011.3.31 23:59:59
	 * @param l ʱ��
	 * @return ָ�����ڵ���ĩ
	 */
	public static long getMonthEnd(long l) {
		return getNextMonth(l) - 1;
	}
	
	/**
	 * ָ�����ڵ����³�����2011.3.25 5:00�����к�᷵��2011.4.1 00:00
	 * @param l ʱ��
	 * @return ָ�����ڵ����³�
	 */
	public static long getNextMonth(long l) {
		return getMonth(getMonth(l) + (32 * 86400000l));
	}
	
	/**
	 * ָ�����ڵ����³�����2011.3.25 5:00�����к�᷵��2011.2.1 00:00
	 * @param l ʱ��
	 * @return ָ�����ڵ����³�
	 */
	public static long getPrevMonth(long l) {
		return getMonth(getMonth(l) - 1);
	}
	
	/**
	 * ת����ʽΪyyyy-MM-dd HH:mm:ss��ʽ
	 * @param str1 Ҫת�����ַ���
	 * @return ת������ַ���
	 * @throws Exception ����ĸ�ʽ����ȷʱ����
	 */
	public static String format(String str1) throws Exception {
		if (StringUtil.isBlank(str1))
			return str1;
		String[] strs = str1.split("[\\./\\-������ ]");
		String str = strs[0];
		if (str.length() == 2) // ��4λ���
			str = "20" + str;
		for (int i = 1; i < strs.length; i++) {
			int j = strs[i].length();
			if (j == 0)
				continue;
			if (j == 1)
				str += "0" + strs[i];
			else if (j == 2)
				str += strs[i];
			else
				throw new Exception("��������ڲ���ȷ��" + str1);
		}
		SimpleDateFormat sdf = null;
		int n = str.length();
		if (n == 6) { // �� 091012
			str = "20" + str;
			n = 8;
		}
		switch (n) {
		case 8:
			sdf = new SimpleDateFormat("yyyyMMdd");
			return new SimpleDateFormat("yyyy-MM-dd").format(sdf.parse(str));
		case 14:
			sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(sdf.parse(str));
		case 10:
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			return new SimpleDateFormat("yyyy-MM-dd").format(sdf.parse(str));
		case 19:
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(sdf.parse(str));
		}
		throw new Exception("��������ڲ���ȷ��" + str1);
	}
	
	/**
	 * ��ʽ���ַ���Ϊʱ��
	 * @param str ʱ���ַ���
	 * @param format ʱ���ʽ, Ĭ��ΪStringUtil.DATE_TIME_FORMAT
	 * @return ����ʱ��
	 * @throws ParseException
	 */
	public static Date parseDate(String str, String format) throws ParseException {
		if(str == null)
			return null;
		if(format == null || format.length() == 0)
			format = StringUtil.DATE_TIME_FORMAT;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(str);
	}
	
	/**
	 * TEST
	 * @param args TEST
	 * @throws Exception  TEST
	 */
	public static void main(String[] args) throws Exception {
		long l = System.currentTimeMillis();
		System.out.println(format("10. 3.4"));
		System.out.println("��ǰʱ��:" + StringUtil.getDateTime(StringUtil.DATE_TIME_FORMAT, l));
		System.out.println("getDate:" + StringUtil.getDateTime(StringUtil.DATE_TIME_FORMAT, getDate(l)));
		System.out.println("getDateEnd:" + StringUtil.getDateTime(StringUtil.DATE_TIME_FORMAT, getDateEnd(l)));
		System.out.println("getMonth:" + StringUtil.getDateTime(StringUtil.DATE_TIME_FORMAT, getMonth(l)));
		System.out.println("getMonthEnd:" + StringUtil.getDateTime(StringUtil.DATE_TIME_FORMAT, getMonthEnd(l)));
		System.out.println("getNextMonth:" + StringUtil.getDateTime(StringUtil.DATE_TIME_FORMAT, getNextMonth(l)));
		System.out.println("getPrevMonth:" + StringUtil.getDateTime(StringUtil.DATE_TIME_FORMAT, getPrevMonth(l)));
		System.out.println("getYear:" + StringUtil.getDateTime(StringUtil.DATE_TIME_FORMAT, getYear(l)));
		System.out.println("getYearEnd:" + StringUtil.getDateTime(StringUtil.DATE_TIME_FORMAT, getYearEnd(l)));
	}
}
