package com.hp.idc.itsm.configure.fields;

import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.configure.FieldInfo;
import com.hp.idc.itsm.util.StringUtil;

/**
 * Á´½Ó×Ö¶Î¿ò
 * @author FluteD
 *
 */
public class URLFieldInfo extends FieldInfo{
	
	private String linkURL = "";

	public Object clone() {
		return new URLFieldInfo();
	}
	
	public void cloneAttribute(FieldInfo info) {
		super.cloneAttribute(info);
		URLFieldInfo info2 = (URLFieldInfo)info;
		info2.setLinkURL(this.linkURL);
	}
	
	public String getHtmlCode(String value) {
		if (value == null)
			value = "";
		String[] vs = value.split("\\|_\\|");
		String str = "";
		if (vs.length ==2){
			str = "<a href=\"" + vs[1] + "\" target=\"_blank\">" + vs[0] + "</a>";
		} else if (vs.length >0)
			str = "<a href=\"" + vs[0] + "\" target=\"_blank\">" + vs[0] + "</a>";
		return str;
	}
	
	public String getRestoreCode(String value){
		if (value == null)
			value = "";
		return value;
	}

	
	public String getFormCode(int width) {
		StringBuffer sb = new StringBuffer();
		sb.append("var " + Consts.FLD_PREFIX + getId()
				+ " = new Ext.form.URLField({");
		sb.append("fieldLabel:\"" + StringUtil.escapeJavaScript(getName()) + "\",");
		sb.append("name:'" + Consts.FLD_PREFIX + getId() + "',");
		if (this.notNull)
			sb.append("allowBlank:false,");
		if (this.linkURL!=null)
			sb.append("linkURL:\"" + linkURL + "\",");
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

	public String getLinkURL() {
		return linkURL;
	}

	public void setLinkURL(String linkURL) {
		this.linkURL = linkURL;
	}
}
