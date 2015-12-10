package com.hp.idc.resm.resource;

import java.text.SimpleDateFormat;

/**
 * 表示日期时间类型的属性
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class DateTimeAttribute extends DateAttribute {
	/**
	 * 序列化UID
	 */
	private static final long serialVersionUID = -5302820137834946418L;
	
	/**
	 * 默认的格式
	 */
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Override
	protected SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat(DATE_TIME_FORMAT);
	}
}
