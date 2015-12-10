package com.hp.idc.resm.resource;

import com.hp.idc.portal.security.PersonManager;
import com.hp.idc.resm.util.StringUtil;

/**
 * ��ʾ�ַ����͵�����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class StringAttribute extends AttributeBase implements
		Comparable<StringAttribute> {
	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = -5121306999593043912L;

	/**
	 * ����ֵ
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
			// TODO �˴��߼���Ҫ����
			this.value = text;
		}
	}

	/**
	 * ��ȡ����ֵ
	 * 
	 * @return ����ֵ
	 * @see #value
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * ��������ֵ
	 * 
	 * @param value
	 *            ����ֵ
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
			return "���ݳ���:" + this.value.length() + "/"
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
		// TODO ������, text��valueȡ�Ļ���һ��ֵ. ���Ƕ���select��tree�ͽṹ, ��Ҫ�ֿ�ȡֵ.��δ��, Ԥ������
		if(this.getAttribute().getAttrId().equals("last_update_by")){
			return PersonManager.getPersonNameById(this.value);
		}
		return super.getText();
	}
}
