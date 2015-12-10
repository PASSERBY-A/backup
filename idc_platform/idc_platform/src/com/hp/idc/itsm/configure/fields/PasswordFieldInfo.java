package com.hp.idc.itsm.configure.fields;

import java.util.List;
import java.util.Map;

import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.configure.FieldInfo;
import com.hp.idc.itsm.security.PasswordUtil;
import com.hp.idc.itsm.util.StringUtil;

/**
 * √‹¬Î ‰»ÎøÚ
 * 
 * @author √∑‘∞
 * 
 */
public class PasswordFieldInfo extends FieldInfo {

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#clone()
	 */
	public Object clone() {
		return new PasswordFieldInfo();
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#parse()
	 */
	public void parse() {
		super.parse();
	}
	
	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#cloneAttribute(com.hp.idc.itsm.configure.FieldInfo)
	 */
	public void cloneAttribute(FieldInfo info) {
		super.cloneAttribute(info);
	}
	
	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getHtmlCode(java.lang.String)
	 */
	public String getHtmlCode(String value) {
		String ret = "";
		if (value != null) {
			for (int i = 0; i < value.length(); i++)
				ret += "*";
		}
		return ret;
	}
	
	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getFormCode(int)
	 */
	public String getFormCode(int width) {
		StringBuffer sb = new StringBuffer();
		sb.append("var " + Consts.FLD_PREFIX + getId()
				+ " = new Ext.form.TextField({");
		sb.append("fieldLabel:\"" + StringUtil.escapeJavaScript(getName()) + "\",");
		sb.append("inputType:'password',");
		sb.append("name:'" + Consts.FLD_PREFIX + getId() + "',");
		if (this.notNull)
			sb.append("allowBlank:false,");
		String d_ = getDesc();
		if (d_.equals(getName()))
			d_ = "";
		sb.append("desc:'"+ StringUtil.escapeJavaScript(d_) +"',");
		sb.append("width:" + width);
		sb.append("});\n");
		if (this.readOnly)
			sb.append(Consts.FLD_PREFIX + getId() + ".setReadOnly(true);\n");
		return sb.toString();
	}
	
	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getRequestValue(java.util.Map)
	 */
	public String getRequestValue(Map m) {
		String val = super.getRequestValue(m);
		return PasswordUtil.crypt(val);
	}
	
	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getFormValue()
	 */
	public String getFormValue() {
		return Consts.FLD_PREFIX + getId() + ".setValue(\""
				+ StringUtil.escapeJavaScript(PasswordUtil.decrypt(this.value)) + "\");";
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#isEncrypt()
	 */
	public boolean isEncrypt() {
		return true;
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getAttributes()
	 */
	public List getAttributes() {
		List returnList = super.getAttributes();
		return returnList;
	}
}
