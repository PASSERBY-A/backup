package com.hp.idc.resm.resource;

import java.text.SimpleDateFormat;

/**
 * 表示时间类型的属性
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class TimeAttribute extends DateAttribute {
	/**
	 * 序列化UID
	 */
	private static final long serialVersionUID = -3396082258241996897L;

	/**
	 * 默认的格式
	 */
	public static final String TIME_FORMAT = "HH:mm:ss";
	
	@Override
	protected SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat(TIME_FORMAT);
	}
}
