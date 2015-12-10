package com.hp.idc.resm.resource;

import com.hp.idc.json.JSONArray;
import com.hp.idc.json.JSONException;

/**
 * ��ʾ������͵�����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class TableAttribute extends AttributeBase {
	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = -5121306999593043912L;

	/**
	 * ����ֵ
	 */
	private JSONArray value = null;

	@Override
	public String getText() {
		if (this.value == null)
			return "[]";
		return this.value.toString();
	}

	@Override
	public JSONArray getValueAsObject() {
		return this.value;
	}

	/**
	 * ��ȡ����ֵ
	 * 
	 * @return ����ֵ
	 * @see #value
	 */
	public JSONArray getValue() {
		return this.value;
	}

	/**
	 * ��������ֵ
	 * 
	 * @param value
	 *            ����ֵ
	 * @see #value
	 */
	public void setValue(JSONArray value) {
		this.value = value;
	}

	@Override
	public void setText(String text) throws JSONException {
		if (text == null) {
			this.text = "";
		} else {
			this.text = text;
			// TODO �˴��߼���Ҫ����
			this.value = new JSONArray(text);
		}
	}

	@Override
	public boolean isNull() {
		return (this.value == null) || (this.value.length() == 0);
	}

	@Override
	public AttributeBase clone() {
		TableAttribute attr = new TableAttribute();
		attr.setAttribute(this.getAttribute());
		try {
			attr.setText(this.getText());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return attr;
	}

}
