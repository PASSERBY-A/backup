package com.hp.idc.itsm.configure.fields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.hp.idc.itsm.ci.CIInfo;
import com.hp.idc.itsm.ci.CIManager;
import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.configure.FieldInfo;
import com.hp.idc.itsm.configure.datasource.CodeDataSource;
import com.hp.idc.itsm.configure.datasource.TextDataSource;
import com.hp.idc.itsm.security.PersonManager;
import com.hp.idc.itsm.util.StringUtil;
import com.hp.idc.itsm.util.XmlUtil;

/**
 * 表示配置项类型的字段
 * 
 * @author FluteD
 * 
 */
public class CIFieldInfo extends FieldInfo {
	/**
	 * 存储是否为单一选择模式
	 */
	protected boolean singleMode = true;

	/**
	 * 存储配置项类别
	 */
	protected int ciCategory = -1;

	/**
	 * 获取是否为单一选择模式
	 * 
	 * @return 返回是否为单一选择模式
	 */
	public boolean isSingleMode() {
		return this.singleMode;
	}
	/**
	 * 设置是否为单一选择模式
	 * 
	 * @param singleMode
	 *            是否为单一选择模式
	 */
	public void setSingleMode(boolean singleMode) {
		this.singleMode = singleMode;
	}
	
	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#clone()
	 */
	public Object clone() {
		return new CIFieldInfo();
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#cloneAttribute(com.hp.idc.itsm.configure.FieldInfo)
	 */
	public void cloneAttribute(FieldInfo info) {
		super.cloneAttribute(info);
		CIFieldInfo info2 = (CIFieldInfo) info;
		info2.setCiCategory(this.ciCategory);
		info2.setSingleMode(this.singleMode);
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getXmlConfig(java.util.Map)
	 */
	public String getXmlConfig(Map map) throws DocumentException {
		String xml = super.getXmlConfig(map);
		Document doc = XmlUtil.parseString(xml);
		Element root = doc.getRootElement();
		String s1 = (String)map.get("fld_cs1_OK");
		if (s1 != null && s1.length() > 0)
			root.addAttribute("singleMode", "false");
		else
			root.addAttribute("singleMode", "true");
		String s2 = (String) map.get("fld_cs2");
		if (s2 == null)
			s2 = "";
		root.addAttribute("category", s2);
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
		if ("false".equals(root.attributeValue("singleMode")))
			this.singleMode = false;
		this.ciCategory = StringUtil.parseInt(root.attributeValue("category"),
				-1);
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getFormCode(int)
	 */
	public String getFormCode(int width) {
		StringBuffer sb = new StringBuffer();
		sb.append("var " + Consts.FLD_PREFIX + getId()
				+ " = new Ext.form.CIField({");
		sb.append("fieldLabel:\"" +  StringUtil.escapeJavaScript(getName()) + "\",");
		sb.append("selectUrl:'"+Consts.CMDB_HOME+"',");
		if (this.transform != null)
			sb.append("transform:" + this.transform + ",");
		if (!isSingleMode())
			sb.append("singleMode: false,");
		sb.append("hiddenName:'" + Consts.FLD_PREFIX + getId() + "',");
		if (this.filter != null && this.filter.length() > 0)
			sb.append("regexId: " + this.filter + ",");
		sb.append("emptyText:'请选择...',");
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
	 * @see com.hp.idc.itsm.configure.FieldInfo#getFormValue()
	 */
	public String getFormValue() {
		String[] vs = this.value.split(",");
		String ret = "";
		for (int i = 0; i < vs.length; i++) {
			if (vs[i].length() == 0)
				continue;
			if (ret.length() > 0)
				ret += ",";
			CIInfo info = CIManager.getCIById(vs[i]);
			if (info != null)
				ret += vs[i] + "=" + info.getName();
			else
				ret += vs[i] + "=" + vs[i];
		}
		return Consts.FLD_PREFIX + getId() + ".setValue(\"" +
			StringUtil.escapeJavaScript(ret) + "\");";
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getHtmlCode(java.lang.String)
	 */
	public String getHtmlCode(String value) {
		String[] vs = value.split(",");
		String ret = "";
		for (int i = 0; i < vs.length; i++) {
			if (vs[i].length() == 0)
				continue;
			if (ret.length() > 0)
				ret += ",";
			CIInfo info = CIManager.getCIById(vs[i]);
			if (info != null)
				ret += info.getName();
			else
				ret += vs[i];
		}
		return value;
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getAttributes()
	 */
	public List getAttributes() {
		ArrayList returnList = new ArrayList();
		CheckboxFieldInfo field3 = new CheckboxFieldInfo();
		TextDataSource ds3 = new TextDataSource();
		ds3.load("OK=");
		field3.setDataSource(ds3);
		field3.setName("允许多选");
		field3.setId("cs1");
		if (!isSingleMode())
			field3.setValue("OK");
		returnList.add(field3);
		
		TreeFieldInfo field4 = new TreeFieldInfo();
		CodeDataSource ds4 = new CodeDataSource();
		ds4.load("" + Consts.CODETYPE_CICATEGORY);
		field4.setDataSource(ds4);
		field4.setName("配置项类别");
		field4.setId("cs2");
		if (this.ciCategory != -1)
			field4.setValue("" + this.ciCategory);
		returnList.add(field4);
		return returnList;
	}

	/**
	 * 获取配置项类别
	 * 
	 * @return 返回配置项类别
	 */
	public int getCiCategory() {
		return this.ciCategory;
	}

	/**
	 * 设置配置项类别
	 * 
	 * @param ciCategory
	 *            配置项类别
	 */
	public void setCiCategory(int ciCategory) {
		this.ciCategory = ciCategory;
	}
}
