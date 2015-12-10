package com.hp.idc.itsm.configure.fields;

import com.hp.idc.itsm.ci.CIInfo;
import com.hp.idc.itsm.ci.CIManager;
import com.hp.idc.itsm.ci.RelationTypeInfo;
import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.configure.FieldInfo;
import com.hp.idc.itsm.util.StringUtil;
/**
 * 配置项关联关系字段
 * 
 * @author 梅园
 * 
 */
public class CIRelationFieldInfo extends FieldInfo {

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getRelationType()
	 */
	public int getRelationType() {
		return Consts.RELATION_CI_CI;
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#clone()
	 */
	public Object clone() {
		return new CIRelationFieldInfo();
	}
	
	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getFormCode(int)
	 */
	public String getFormCode(int width) {
		StringBuffer sb = new StringBuffer();
		sb.append("var " + Consts.FLD_PREFIX + getId() +
				" = new Ext.form.CIRelationField({");
		if (this.transform != null)
			sb.append("transform:" + this.transform + ",");
		sb.append("fieldLabel:\"" + StringUtil.escapeJavaScript(getName()) + "\",");
		sb.append("name:'" + Consts.FLD_PREFIX + getId() + "',");
		String d_ = getDesc();
		if (d_.equals(getName()))
			d_ = "";
		sb.append("desc:'"+ StringUtil.escapeJavaScript(d_) +"',");
		sb.append("width:" + width);
		sb.append("});\n");
		if (this.readOnly)
			sb.append(Consts.FLD_PREFIX + getId() + ".disable();\n");
		return sb.toString();
	}
	
	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getFormValue()
	 */
	public String getFormValue() {
		if (this.value == null || this.value.length() == 0)
			return "";
		String[] vals = this.value.split(";");
		StringBuffer sb = new StringBuffer();
		for (int i = 0, count = 0, pos; i < vals.length; i++) {
			if (vals[i].length() == 0)
				continue;
			String s = vals[i];
			if ((pos = s.lastIndexOf("-")) == -1)
				continue;
			int a = Integer.parseInt(s.substring(0, pos));
			int b = Integer.parseInt(s.substring(pos + 1));
			CIInfo item = CIManager.getCIByOid(b);
			RelationTypeInfo info = CIManager.getRelationTypeByOid(a);
			if (count++ > 0)
				sb.append(";");
			if (item != null && info != null)
				sb.append(vals[i] + "," + info.getCaption(item));
			count++;
		}
		return Consts.FLD_PREFIX + getId() + ".setValue(\"" +
				StringUtil.escapeJavaScript(sb.toString()) + "\");";
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getHtmlCode(java.lang.String)
	 */
	public String getHtmlCode(String value){
		if (value == null || value.length() == 0)
			return "";
		String[] vals = value.split(";");
		StringBuffer sb = new StringBuffer();
		for (int i = 0, count = 0, pos; i < vals.length; i++) {
			if (vals[i].length() == 0)
				continue;
			String s = vals[i];
			if ((pos = s.lastIndexOf("-")) == -1)
				continue;
			int a = Integer.parseInt(s.substring(0, pos));
			int b = Integer.parseInt(s.substring(pos + 1));
			CIInfo item = CIManager.getCIByOid(b);
			RelationTypeInfo info = CIManager.getRelationTypeByOid(a);
			if (count++ > 0)
				sb.append("<br/>");
			if (item != null && info != null)
				sb.append(info.getCaption(item));
			count++;
		}
		return sb.toString();
	}
}
