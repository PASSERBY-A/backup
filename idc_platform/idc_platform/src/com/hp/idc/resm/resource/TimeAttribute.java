package com.hp.idc.resm.resource;

import java.text.SimpleDateFormat;

/**
 * ��ʾʱ�����͵�����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class TimeAttribute extends DateAttribute {
	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = -3396082258241996897L;

	/**
	 * Ĭ�ϵĸ�ʽ
	 */
	public static final String TIME_FORMAT = "HH:mm:ss";
	
	@Override
	protected SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat(TIME_FORMAT);
	}
}
