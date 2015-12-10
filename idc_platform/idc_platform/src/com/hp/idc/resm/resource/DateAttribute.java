package com.hp.idc.resm.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 表示日期类型的属性
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class DateAttribute extends AttributeBase implements
		Comparable<DateAttribute> {
	/**
	 * 序列化UID
	 */
	private static final long serialVersionUID = -6032688907508586343L;
	/**
	 * 数据值
	 */
	protected long value = -1;

	/**
	 * 默认的格式
	 */
	public static final String DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * 获取日期格式
	 * 
	 * @return 日期格式
	 */
	protected SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat(DATE_FORMAT);
	}

	@Override
	public String getText() {
		if (this.value == -1)
			return "";
		return getDateFormat().format(this.value);
	}

	@Override
	public Object getValueAsObject() {
		return getValue();
	}

	/**
	 * 获取数据值
	 * 
	 * @return 数据值
	 * @see #value
	 */
	public long getValue() {
		return this.value;
	}

	/**
	 * 设置数据值
	 * 
	 * @param value
	 *            数据值
	 * @see #value
	 */
	public void setValue(long value) {
		this.value = value;
	}

	@Override
	public void setText(String text) throws Exception {
		if (text == null || text.length() == 0 || text.equals("-1")) {
			this.text = "";
			return;
		}
		this.text = text;
		// TODO 此处逻辑需要完善
		try {
			this.value = getDateFormat().parse(text).getTime();
		} catch (ParseException e) {
			throw new Exception("属性“" + this.getAttribute().getName()
					+ "”的格式不正确。");
		}
	}

	@Override
	public boolean isNull() {
		return (this.value == -1);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DateAttribute))
			return false;
		DateAttribute a = (DateAttribute) obj;
		if (!this.getAttribute().getAttrId().equals(
				a.getAttribute().getAttrId()))
			return false;

		return (this.value == a.value);
	}

	public int compareTo(DateAttribute a) {
		if (this.value > a.value)
			return 1;
		if (this.value < a.value)
			return -1;
		return 0;
	}

	@Override
	public AttributeBase clone() {
		DateAttribute attr = new DateAttribute();
		attr.setAttribute(this.getAttribute());
		try {
			attr.setText(this.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attr;
	}
}
