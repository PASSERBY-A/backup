package com.hp.idc.itsm.configure.fields;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.common.OperationCode;
import com.hp.idc.itsm.configure.FieldInfo;
import com.hp.idc.itsm.util.ItsmUtil;
import com.hp.idc.itsm.util.StringUtil;

/**
 * 日期时间型输入框
 * 
 * @author 李会争
 * 
 */
public class DateTimeFieldInfo extends FieldInfo {
	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#clone()
	 */
	public Object clone() {
		return new DateTimeFieldInfo();
	}
	
	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getFormCode(int)
	 */
	public String getFormCode(int width) {
		StringBuffer sb = new StringBuffer();
		sb.append("var " + Consts.FLD_PREFIX + getId() +
				" = new Ext.ux.form.DateTime({");
		if (this.transform != null)
			sb.append("transform:" + this.transform + ",");
		sb.append("fieldLabel:\"" + StringUtil.escapeJavaScript(getName()) + "\",");
		sb.append("name:'" + Consts.FLD_PREFIX + getId() + "',");
		sb.append("hiddenFormat:'Y-m-d H:i:s',");
		sb.append("dateFormat:'Y-m-d',");
		sb.append("timeFormat:'H:i:s',");
		if (this.notNull)
			sb.append("allowBlank:false,");
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
	
	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#match(int, java.lang.String, java.lang.String)
	 */
	public boolean match(int operationCode, String val_1, String val_2) {
		String val3 = "";
		String val = val_1;
		String val2 = val_2;
		if (operationCode == OperationCode.BETWEEN) {
			if (val2.indexOf(";")!=-1) {
				val3 = val2.substring(val2.indexOf(";")+1);
				val2 = val2.substring(0,val2.indexOf(";"));
			}
			if (val3.equals(""))
				return (val.compareTo(val2)>=0);
			if (val2.equals(""))
				return (val.compareTo(val3)<=0);
			return (val.compareTo(val2)>=0 && val.compareTo(val3)<=0);
		}
		if (operationCode == OperationCode.CURRENT_MONTH) {
			val = val.substring(0,7);
			val2 = ItsmUtil.replaceVariant("${month}","");
			return val.equals(val2);
		}
		if (operationCode == OperationCode.CURRENT_WEEK) {
			Calendar cal = Calendar.getInstance();
			int date = cal.get(Calendar.DATE);
			int week = cal.get(Calendar.DAY_OF_WEEK)-1;
			cal.set(Calendar.DATE, date-week+1);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			val2 = sdf.format(cal.getTime());
			return (val.compareTo(val2)>=0);
		}
		if (operationCode == OperationCode.CURRENT_YEAR) {
			val = val.substring(0,4);
			val2 = ItsmUtil.replaceVariant("${year}","");
			return val.equals(val2);
		}
		return super.match(operationCode, val, val2);
	}
	
}
