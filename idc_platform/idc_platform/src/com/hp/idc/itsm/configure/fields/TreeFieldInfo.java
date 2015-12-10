package com.hp.idc.itsm.configure.fields;

import java.util.regex.Pattern;

import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.common.OperationCode;
import com.hp.idc.itsm.configure.datasource.RemoteDataSource;
import com.hp.idc.itsm.util.StringUtil;

/**
 * 下拉树型选择框
 * 
 * @author 梅园
 * 
 */
public class TreeFieldInfo extends FieldInfoWithDataSource {
	/**
	 * 
	 * @see com.hp.idc.itsm.configure.fields.FieldInfoWithDataSource#clone()
	 */
	public Object clone() {
		return new TreeFieldInfo();
	}

	public boolean match(int operationCode, String val, String regex) {
//		if (operationCode == OperationCode.EQUAL)
//			return Pattern.matches("^"+regex+"$",val);
//		if (operationCode == OperationCode.NOT_EQUAL)
//			return !Pattern.matches("^"+regex+"$",val);
//		if (operationCode == OperationCode.INCLUDE) {
//			if (val.indexOf("/")==-1)
//				return (Pattern.matches("^"+regex+"$",val));
//			else {
//				boolean ret = (Pattern.matches(".*/"+regex+"$",val)) || (Pattern.matches(".*/"+regex+"/.*",val));
//				return ret;
//			}
//		}
//		if (operationCode == OperationCode.NOT_INCLUDE) {
//			if (val.indexOf("/")==-1)
//				return !(Pattern.matches("^"+regex+"$",val));
//			else {
//				boolean ret = (Pattern.matches(".*/"+regex+"$",val)) || (Pattern.matches(".*/"+regex+"/.*",val));
//				return !ret;
//			}
//		}
		return super.match(operationCode, val, regex);
	}
	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getFormCode(int)
	 */
	public String getFormCode(int width) {
		StringBuffer sb = new StringBuffer();
		sb.append("var " + Consts.FLD_PREFIX + getId() +
			" = new Ext.form.TreeBox({");
		if (this.transform != null)
			sb.append("transform:" + this.transform + ",");
		sb.append("fieldLabel:\"" + StringUtil.escapeJavaScript(getName()) + "\",");
		sb.append("hiddenName:'" + Consts.FLD_PREFIX + getId() + "',");
		if (this.filter != null && this.filter.length() > 0)
			sb.append("regexId: " + this.filter + ",");
		if (this.dataSourceClass.equals(RemoteDataSource.class.getName())){
			sb.append("viewLoader: new Ext.tree.FilterTreeLoader( {");
			sb.append("baseParams: {child:1,");
			if (this.filter != null && this.filter.length() > 0)
				sb.append(", regexId: '" + this.filter + "'");
			sb.append("},");
			sb.append("dataUrl:'"+this.dataSourceData+"'");
			sb.append("}),");
		} else {
			sb.append("treeData: [");
			sb.append(this.dataSource.getData(this.filter, "tree","ID"));
			sb.append("],");
		}
		sb.append("emptyText:'请选择...',");
		if (this.notNull)
			sb.append("allowBlank:false,");
		if (!this.singleMode)
			sb.append("singleMode: false,");
		String d_ = getDesc();
		if (d_.equals(getName()))
			d_ = "";
		sb.append("desc:'"+ StringUtil.escapeJavaScript(d_) +"',");
		sb.append("width:" + width + ",");
		sb.append("listeners:{");
		sb.append("	select:function(c,n) {");
		sb.append("		var alertMsg = n.alertMsg;");
		sb.append("		if (alertMsg && alertMsg!=''){alert(alertMsg);}");
		sb.append("	}");
		sb.append("}");
		sb.append(",msgTarget:'title'});\n");
		if (this.readOnly)
			sb.append(Consts.FLD_PREFIX + getId() + ".setReadOnly(true);\n");
		return sb.toString();
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getFormValue()
	 */
	public String getFormValue() {
		String id = this.value;
		if (id == null)
			id = "";
		id = id + "=" + this.dataSource.getDisplayText(this.value);
		return Consts.FLD_PREFIX + getId() + ".setValue(\"" +
				StringUtil.escapeJavaScript(id) + "\");";
	}
	
	/**
	 * 
	 * @see com.hp.idc.itsm.configure.fields.FieldInfoWithDataSource#getHtmlCode(java.lang.String)
	 */
	public String getHtmlCode(String value){
		String ret = this.dataSource.getDisplayText(value);
		return ret;
	}
}
