package com.hp.idc.resm.resource;

import java.text.SimpleDateFormat;

/**
 * ��ʾ����ʱ�����͵�����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class DateTimeAttribute extends DateAttribute {
	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = -5302820137834946418L;
	
	/**
	 * Ĭ�ϵĸ�ʽ
	 */
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Override
	protected SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat(DATE_TIME_FORMAT);
	}
}
