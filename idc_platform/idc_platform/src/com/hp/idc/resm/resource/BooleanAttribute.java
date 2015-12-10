package com.hp.idc.resm.resource;

import com.hp.idc.resm.model.BooleanAttributeDefine;

/**
 * 表示布尔类型的属性
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class BooleanAttribute extends AttributeBase implements
		Comparable<BooleanAttribute> {
	/**
	 * 序列化UID
	 */
	private static final long serialVersionUID = 8324885122609212171L;

	/**
	 * 数据值
	 */
	private Boolean value = true;

	@Override
	public String getText() {
		if (this.value == null)
			return "";
		BooleanAttributeDefine ad = (BooleanAttributeDefine) this
				.getAttribute().getDefine();
		if (this.value)
			return ad.getYes();
		return ad.getNo();
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
	public Boolean getValue() {
		return this.value;
	}

	/**
	 * 设置数据值
	 * 
	 * @param value
	 *            数据值
	 * @see #value
	 */
	public void setValue(Boolean value) {
		this.value = value;
	}

	@Override
	public void setText(String text) {
		if (text == null || text.length() == 0) {
			this.text = "";
			return;
		}
		this.text = text;
		// TODO 此处逻辑需要完善
		if (text.equals("true") || text.equals("1")) {
			this.value = true;
		} else if (text.equals("false") || text.equals("0")) {
			this.value = false;
		} else {
			BooleanAttributeDefine ad = (BooleanAttributeDefine) this
					.getAttribute().getDefine();
			if (ad.getYes().equals(text))
				this.value = true;
			else if (ad.getYes().equals(text))
				this.value = false;
			else
				this.value = null;
		}
	}

	@Override
	public boolean isNull() {
		return (this.value == null);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof BooleanAttribute))
			return false;
		BooleanAttribute a = (BooleanAttribute) obj;
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

	public int compareTo(BooleanAttribute a) {
		if (this.value == null) {
			if (a.value == null)
				return 0;
			return -1;
		} else if (a.value == null)
			return 1;
		return (this.value ? 1 : 0) - (a.value ? 1 : 0);
	}

	@Override
	public AttributeBase clone() {
		BooleanAttribute attr = new BooleanAttribute();
		attr.setAttribute(this.getAttribute());
		attr.setText(this.getText());
		return attr;
	}
}
