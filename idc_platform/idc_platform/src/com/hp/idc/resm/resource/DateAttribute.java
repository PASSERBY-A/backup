package com.hp.idc.resm.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * ��ʾ�������͵�����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class DateAttribute extends AttributeBase implements
		Comparable<DateAttribute> {
	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = -6032688907508586343L;
	/**
	 * ����ֵ
	 */
	protected long value = -1;

	/**
	 * Ĭ�ϵĸ�ʽ
	 */
	public static final String DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * ��ȡ���ڸ�ʽ
	 * 
	 * @return ���ڸ�ʽ
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
	 * ��ȡ����ֵ
	 * 
	 * @return ����ֵ
	 * @see #value
	 */
	public long getValue() {
		return this.value;
	}

	/**
	 * ��������ֵ
	 * 
	 * @param value
	 *            ����ֵ
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
		// TODO �˴��߼���Ҫ����
		try {
			this.value = getDateFormat().parse(text).getTime();
		} catch (ParseException e) {
			throw new Exception("���ԡ�" + this.getAttribute().getName()
					+ "���ĸ�ʽ����ȷ��");
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
