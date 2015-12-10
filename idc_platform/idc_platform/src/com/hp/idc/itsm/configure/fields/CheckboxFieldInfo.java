package com.hp.idc.itsm.configure.fields;

import java.util.List;

import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.util.StringUtil;

/**
 * ¶àÑ¡¿òÐÍ×Ö¶Î
 * @author Ã·Ô°
 *
 */
public class CheckboxFieldInfo extends FieldInfoWithDataSource {
	/**
	 * 
	 * @see com.hp.idc.itsm.configure.fields.FieldInfoWithDataSource#clone()
	 */
	public Object clone() {
		return new CheckboxFieldInfo();
	}
	
	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getFormCode(int)
	 */
	public String getFormCode(int width) {
		List keys = this.dataSource.getKeys(this.filter);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < keys.size(); i++) {
			sb.append("var " + Consts.FLD_PREFIX + getId() + "_" + (String)keys.get(i) +
				" = new Ext.form.Checkbox({");
			if (i == 0)
				sb.append("fieldLabel:\"" + StringUtil.escapeJavaScript(getName()) + "\",");
			sb.append("boxLabel:\"" + StringUtil.escapeHtml(this.dataSource.getDisplayText((String)keys.get(i))) + "\",");
			sb.append("name:'" + Consts.FLD_PREFIX + getId() + "_" + (String)keys.get(i) + "',");
			String d_ = getDesc();
			if (d_.equals(getName()))
				d_ = "";
			sb.append("desc:'"+ StringUtil.escapeJavaScript(d_) +"',");
			sb.append("width:'auto'");
			sb.append(",msgTarget:'title'});\n");
			if (this.readOnly)
				sb.append(Consts.FLD_PREFIX + getId() + "_" + (String)keys.get(i) + ".setReadOnly(true);\n");
		}
		return sb.toString();
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getFormValue()
	 */
	public String getFormValue() { 
		if (this.value == null || this.value.length() == 0)
			return "";
		String vals[] = this.value.split("\\|");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < vals.length; i++) {
			sb.append(Consts.FLD_PREFIX + getId() + "_" + vals[i] + ".setValue(true);\n");
		}
		return sb.toString();
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getFormFields()
	 */
	public String[] getFormFields() {
		List keys = this.dataSource.getKeys(this.filter);
		String ret[] = new String[keys.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = Consts.FLD_PREFIX + getId() + "_" + (String)keys.get(i);
		}
		return ret;
	}
}
