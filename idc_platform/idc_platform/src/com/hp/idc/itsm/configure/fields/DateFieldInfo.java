package com.hp.idc.itsm.configure.fields;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.common.OperationCode;
import com.hp.idc.itsm.configure.FieldInfo;
import com.hp.idc.itsm.util.ItsmUtil;
import com.hp.idc.itsm.util.StringUtil;
import com.hp.idc.itsm.util.XmlUtil;
/**
 * 日期型输入框
 * 
 * @author 李会争
 * 
 */
public class DateFieldInfo extends FieldInfo {
	
	/**
	 * store the max date
	 */
	protected String max_date = null;
	
	/**
	 * store the min date
	 */
	protected String min_date = null;
	
	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#clone()
	 */
	public Object clone() {
		return new DateFieldInfo();
	}
	
	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getFormCode(int)
	 */
	public String getFormCode(int width) {
		StringBuffer sb = new StringBuffer();
		sb.append("var " + Consts.FLD_PREFIX + getId() +
				" = new Ext.form.DateField({");
		if (this.transform != null)
			sb.append("transform:" + this.transform + ",");
		sb.append("fieldLabel:\"" + StringUtil.escapeJavaScript(getName()) + "\",");
		sb.append("name:'" + Consts.FLD_PREFIX + getId() + "',");
		sb.append("altFormats:'y/m/d|y-m-d|Y/m/d|Y-m-d|m-d|m/d',");
		sb.append("format:'Y-m-d',");
		if (this.notNull)
			sb.append("allowBlank:false,");
		String d_ = getDesc();
		if (d_.equals(getName()))
			d_ = "";
		sb.append("desc:'"+ StringUtil.escapeJavaScript(d_) +"',");
		if (this.max_date != null && this.max_date.length() > 0) 
			sb.append("maxValue: " + this.max_date + ",");
		if (this.min_date != null && this.min_date.length() > 0) 
			sb.append("minValue: " + this.min_date + ",");
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
		if (val.length()>7)
			val = val.substring(0,7);
		if (operationCode == OperationCode.LESS_THAN)
			return (val.compareTo(val2) < 0);
		if (operationCode == OperationCode.GREATE_THAN)
			return (val.compareTo(val2) > 0);
		if (operationCode == OperationCode.LESS_OR_EQUAL)
			return (val.compareTo(val2) <= 0);
		if (operationCode == OperationCode.GREATE_OR_EQUAL)
			return (val.compareTo(val2) >= 0);
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

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getXmlConfig(java.util.Map)
	 */
	public String getXmlConfig(Map map) throws DocumentException {
		String xml = getDefaultConfigure();
		Document doc = XmlUtil.parseString(xml);
		Element root = doc.getRootElement();
		
		String max = (String) map.get("fld_max_date");
		if (max == null)
			max = "";
		Element el = (Element) root.selectSingleNode("./max_date");
		if (el == null)
			el = root.addElement("max_date");
		el.setText(max);
		
		String min = (String) map.get("fld_min_date");
		if (min == null)
			min = "";
		Element el1 = (Element) root.selectSingleNode("./min_date");
		if (el1 == null)
			el1 = root.addElement("min_date");
		el.setText(min);
		
		return doc.asXML();
	}
	
	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#parse()
	 */
	public void parse() {
		super.parse();
		if (this.xmlDoc == null)
			return;
		Element root = this.xmlDoc.getRootElement();
		Element el = (Element) root.selectSingleNode("./max_date");
		if (el != null)
			this.max_date = el.getText();
		Element el1 = (Element) root.selectSingleNode("./min_date");
		if (el1 != null)
			this.min_date = el.getText();
	}
	
	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getAttributes()
	 */
	public List getAttributes() {
		List returnList = super.getAttributes();

		StringFieldInfo field2 = new StringFieldInfo();
		field2.setName("最大值");
		field2.setId("max_date");
		field2.setMutiline(false);
		if (this.max_date != null && this.max_date.length() > 0)
			field2.setValue(this.max_date);
		returnList.add(field2);

		StringFieldInfo field1 = new StringFieldInfo();
		field1.setName("最小值");
		field1.setId("min_date");
		field1.setMutiline(true);
		if (this.min_date != null && this.min_date.length() > 0)
			field1.setValue(this.min_date);
		returnList.add(field1);

		return returnList;
	}
	
	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#cloneAttribute(com.hp.idc.itsm.configure.FieldInfo)
	 */
	public void cloneAttribute(FieldInfo info) {
		super.cloneAttribute(info);
		DateFieldInfo info2 = (DateFieldInfo) info;
		info2.setMax_date(this.max_date);
		info2.setMin_date(this.min_date);
	}

	public String getMax_date() {
		return max_date;
	}

	public void setMax_date(String maxDate) {
		max_date = maxDate;
	}

	public String getMin_date() {
		return min_date;
	}

	public void setMin_date(String minDate) {
		min_date = minDate;
	}
}
