package com.hp.idc.itsm.configure.fields;

import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.common.OperationCode;
import com.hp.idc.itsm.configure.FieldInfo;
import com.hp.idc.itsm.util.StringUtil;

/**
 * 数字型输入框
 * 
 * @author 李会争
 * 
 */
public class IntegerFieldInfo extends FieldInfo {

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#match(int, java.lang.String, java.lang.String)
	 */
	public boolean match(int operationCode, String val, String val2) {
		try {
			if (operationCode == OperationCode.LESS_THAN)
				return (Integer.parseInt(val) < Integer.parseInt(val2));
			else if (operationCode == OperationCode.GREATE_THAN)
				return (Integer.parseInt(val) > Integer.parseInt(val2));
			else if (operationCode == OperationCode.LESS_OR_EQUAL)
				return (Integer.parseInt(val) <= Integer.parseInt(val2));
			else if (operationCode == OperationCode.GREATE_OR_EQUAL)
				return (Integer.parseInt(val) >= Integer.parseInt(val2));
		} catch (Exception e) {
			return false;
		}
		return super.match(operationCode, val, val2);
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#clone()
	 */
	public Object clone() {
		return new IntegerFieldInfo();
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getFormCode(int)
	 */
	public String getFormCode(int width) {
		StringBuffer sb = new StringBuffer();
		sb.append("var " + Consts.FLD_PREFIX + getId()
				+ " = new Ext.form.TextField({");
		if (this.transform != null)
			sb.append("transform:" + this.transform + ",");
		sb.append("fieldLabel:\"" + StringUtil.escapeJavaScript(getName()) + "\",");
		sb.append("name:'" + Consts.FLD_PREFIX + getId() + "',");
		if (this.notNull)
			sb.append("allowBlank:false,");
		sb
				.append("validator:function(v){var c=/^-?\\d+$/;if (c.test(v)) return true; return '请输入一个正确的数值';},");
		String d_ = getDesc();
		if (d_.equals(getName()))
			d_ = "";
		sb.append("desc:'"+ StringUtil.escapeJavaScript(d_) +"',");
		sb.append("width:" + width);
		sb.append(",msgTarget:'title'});\n");
		if (this.readOnly)
			sb.append(Consts.FLD_PREFIX + getId() + ".setReadOnly(true);\n");
		return sb.toString();
	}
}
