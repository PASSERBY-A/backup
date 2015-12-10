package com.hp.idc.resm.resource;

import com.hp.idc.portal.security.PersonManager;
import com.hp.idc.resm.util.StringUtil;

/**
 * 表示字符类型的属性
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class StringAttribute extends AttributeBase implements
		Comparable<StringAttribute> {
	/**
	 * 序列化UID
	 */
	private static final long serialVersionUID = -5121306999593043912L;

	/**
	 * 数据值
	 */
	private String value = null;

	@Override
	public String getValueAsObject() {
		return getValue();
	}

	@Override
	public void setText(String text) {
		if (text == null) {
			this.text = "";
		} else {
			this.text = text;
			// TODO 此处逻辑需要完善
			this.value = text;
		}
	}

	/**
	 * 获取数据值
	 * 
	 * @return 数据值
	 * @see #value
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * 设置数据值
	 * 
	 * @param value
	 *            数据值
	 * @see #value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean isNull() {
		return (this.value == null) || (this.value.trim().length() == 0);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof StringAttribute))
			return false;
		StringAttribute a = (StringAttribute) obj;
		if (!this.getAttribute().getAttrId().equals(
				a.getAttribute().getAttrId()))
			return false;

		return (StringUtil.compare(this.value, a.value) == 0);
	}

	public int compareTo(StringAttribute a) {
		if (this.value == null) {
			if (a.value == null)
				return 0;
			return -1;
		} else if (a.value == null)
			return 1;
		return this.value.compareTo(a.value);
	}

	@Override
	public String isValid() {
		String s = super.isValid();
		if (s != null)
			return s;
		if (this.value != null
				&& this.value.length() > this.getAttribute().getDefine()
						.getLength())
			return "内容超长:" + this.value.length() + "/"
					+ this.getAttribute().getDefine().getLength();
		return null;
	}
	
	@Override
	public StringAttribute clone(){
		StringAttribute attr = new StringAttribute();
		attr.setAttribute(this.getAttribute());
		attr.setText(this.getText());
		return attr;
	}
	
	@Override
	public String getText() {
		// TODO 在这里, text和value取的还是一个值. 但是对于select和tree型结构, 需要分开取值.暂未做, 预留下来
		if(this.getAttribute().getAttrId().equals("last_update_by")){
			return PersonManager.getPersonNameById(this.value);
		}
		return super.getText();
	}
}
