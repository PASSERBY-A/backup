package com.hp.idc.resm.resource;

import com.hp.idc.resm.util.StringUtil;

/**
 * ��ʾ�������͵�����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class NumberAttribute extends AttributeBase implements
		Comparable<NumberAttribute> {
	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = 9096348016235069731L;
	/**
	 * ����ֵ
	 */
	private Double value = null;

	@Override
	public String getText() {
		if (!text.matches("\\d*[.]*\\d*"))
			return "";
		return this.value.toString();
	}

	@Override
	public Double getValueAsObject() {
		if (!text.matches("\\d*[.]*\\d*"))
			return null;
		return getValue();
	}

	/**
	 * ��ȡ����ֵ
	 * 
	 * @return ����ֵ
	 * @see #value
	 */
	public Double getValue() {
		return this.value;
	}

	/**
	 * ��������ֵ
	 * 
	 * @param value
	 *            ����ֵ
	 * @see #value
	 */
	public void setValue(Double value) {
		this.value = value;
	}

	@Override
	public void setText(String text) {
			this.text = (text == null?"":text);
			// TODO �˴��߼���Ҫ����
			this.value = StringUtil.parseDouble(text, 0);
	}

	@Override
	public boolean isNull() {
		return (this.value == null);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof NumberAttribute))
			return false;
		NumberAttribute a = (NumberAttribute) obj;
		if (!this.getAttribute().getAttrId().equals(
				a.getAttribute().getAttrId()))
			return false;

		if (this.value == null) {
			if (a.value == null)
				return true;
			return false;
		} else if (a.value == null)
			return false;
		return (this.value.compareTo(a.value) == 0);
	}

	public int compareTo(NumberAttribute a) {
		if (this.value == null) {
			if (a.value == null)
				return 0;
			return -1;
		} else if (a.value == null)
			return 1;
		return this.value.compareTo(a.value);
	}

	@Override
	public AttributeBase clone() {
		NumberAttribute attr = new NumberAttribute();
		attr.setAttribute(this.getAttribute());
		attr.setText(this.getText());
		return attr;
	}
}
