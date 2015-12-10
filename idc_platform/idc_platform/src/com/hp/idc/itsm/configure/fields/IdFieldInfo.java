package com.hp.idc.itsm.configure.fields;

import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.configure.FieldInfo;
import com.hp.idc.itsm.util.StringUtil;
import com.hp.idc.itsm.util.XmlUtil;

/**
 * 自动生成ID输入框
 * 
 * @author 梅园
 * 
 */
public class IdFieldInfo extends FieldInfo {

	/**
	 * 存储id的前缀
	 */
	protected String prefix = null;
	
	/**
	 * 存储生成id的编号id
	 */
	protected String autoId = "itsm";

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#clone()
	 */
	public Object clone() {
		return new IdFieldInfo();
	}
	
	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getXmlConfig(java.util.Map)
	 */
	public String getXmlConfig(Map map) throws DocumentException {
		String xml = super.getXmlConfig(map);
		Document doc = XmlUtil.parseString(xml);
		Element root = doc.getRootElement();

		String s3 = (String)map.get("fld_prefix");
		if (s3 == null)
			s3 = "";
		Element el = (Element)root.selectSingleNode("./prefix");
		if (el == null)
			el = root.addElement("prefix");
		el.setText(s3);

		s3 = (String)map.get("fld_autoid");
		if (s3 == null)
			s3 = "";
		el = (Element)root.selectSingleNode("./id");
		if (el == null)
			el = root.addElement("id");
		el.setText(s3);

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
		Element el = (Element)root.selectSingleNode("./prefix");
		if (el != null)
			this.prefix = el.getText();
		el = (Element)root.selectSingleNode("./id");
		if (el != null)
			this.autoId = el.getText();
	}
	
	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#cloneAttribute(com.hp.idc.itsm.configure.FieldInfo)
	 */
	public void cloneAttribute(FieldInfo info) {
		super.cloneAttribute(info);
		IdFieldInfo info2 = (IdFieldInfo)info;
		info2.setPrefix(this.prefix);
		info2.setAutoId(this.autoId);
	}
	
	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getFormCode(int)
	 */
	public String getFormCode(int width) {
		StringBuffer sb = new StringBuffer();
		sb.append("var " + Consts.FLD_PREFIX + getId()
				+ " = new Ext.form.IdField({");
		if (this.transform != null)
			sb.append("transform:" + this.transform + ",");
		sb.append("fieldLabel:\"" + StringUtil.escapeJavaScript(getName()) + "\",");
		sb.append("name:'" + Consts.FLD_PREFIX + getId() + "',");
		if (this.notNull)
			sb.append("allowBlank:false,");
		if (this.prefix != null && this.prefix.length() > 0)
			sb.append("prefix: \"" + StringUtil.escapeJavaScript(this.prefix) + "\",");
		sb.append("autoId:\"" + StringUtil.escapeJavaScript(this.autoId) + "\",");
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
	 * @see com.hp.idc.itsm.configure.FieldInfo#getAttributes()
	 */
	public List getAttributes() {
		List returnList = super.getAttributes();

		StringFieldInfo field1 = new StringFieldInfo();
		field1.setName("前缀");
		field1.setId("prefix");
		if (this.prefix != null && this.prefix.length() > 0)
			field1.setValue(this.prefix);
		returnList.add(field1);

		StringFieldInfo field2 = new StringFieldInfo();
		field2.setName("自动ID");
		field2.setId("autoid");
		if (this.autoId != null && this.autoId.length() > 0)
			field2.setValue(this.autoId);
		returnList.add(field2);

		return returnList;
	}

	/**
	 * 获取id的前缀
	 * @return 返回id的前缀
	 */
	public String getPrefix() {
		return this.prefix;
	}

	/**
	 * 设置id的前缀
	 * @param prefix id的前缀
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * 获取生成id的编号id
	 * @return 返回生成id的编号id 
	 */
	public String getAutoId() {
		return this.autoId;
	}

	/**
	 * 设置生成id的编号id 
	 * @param autoId 生成id的编号id 
	 */
	public void setAutoId(String autoId) {
		this.autoId = autoId;
	}


}
