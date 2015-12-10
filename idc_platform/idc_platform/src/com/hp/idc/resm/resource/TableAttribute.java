package com.hp.idc.resm.resource;

import com.hp.idc.json.JSONArray;
import com.hp.idc.json.JSONException;

/**
 * 表示表格类型的属性
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class TableAttribute extends AttributeBase {
	/**
	 * 序列化UID
	 */
	private static final long serialVersionUID = -5121306999593043912L;

	/**
	 * 数据值
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
	 * 获取数据值
	 * 
	 * @return 数据值
	 * @see #value
	 */
	public JSONArray getValue() {
		return this.value;
	}

	/**
	 * 设置数据值
	 * 
	 * @param value
	 *            数据值
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
			// TODO 此处逻辑需要完善
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
