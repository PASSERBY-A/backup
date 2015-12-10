package com.hp.idc.itsm.configure.fields;

import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.common.OperationCode;
import com.hp.idc.itsm.configure.FieldInfo;
import com.hp.idc.itsm.configure.datasource.TextDataSource;
import com.hp.idc.itsm.util.StringUtil;
import com.hp.idc.itsm.util.XmlUtil;

/**
 * �ַ��������
 * 
 * @author �����
 * 
 */
public class StringFieldInfo extends FieldInfo {

	/**
	 * �洢�Ƿ�Ϊ����ģʽ
	 */
	protected boolean mutiline = false;

	/**
	 * �洢�Ƿ��Զ�ȥ���հ�
	 */
	protected boolean autoTrim = false;

	/**
	 * �洢�Ƿ�Ϊ���ı�ģʽ
	 */
	protected boolean html = false;

	/**
	 * �洢�ı����麯��
	 */
	protected String validator = null;
	
	/**
	 * �����ı�������,ֻ��mutiline = trueʱ����
	 */
	protected String rowNum = "3";
	
	/**
	 * wheathe grow auto
	 */
	protected boolean autoGrow = false;

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#clone()
	 */
	public Object clone() {
		return new StringFieldInfo();
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getRequestValue(java.util.Map)
	 */
	public String getRequestValue(Map m) {
		String str = super.getRequestValue(m);
		if (this.autoTrim) {
			if (str != null)
				str = str.trim();
			else
				str = "";
		}
		return str;
	}
	
	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getXmlConfig(java.util.Map)
	 */
	public String getXmlConfig(Map map) throws DocumentException {
		String xml = super.getXmlConfig(map);
		Document doc = XmlUtil.parseString(xml);
		Element root = doc.getRootElement();
		String s1 = (String) map.get("fld_cs1_OK");
		if (s1 != null && s1.length() > 0)
			root.addAttribute("mutiline", "true");
		else
			root.addAttribute("mutiline", "false");
		String s2 = (String) map.get("fld_cs2_OK");
		if (s2 != null && s2.length() > 0)
			root.addAttribute("html", "true");
		else
			root.addAttribute("html", "false");

		String s3 = (String) map.get("fld_validator");
		if (s3 == null)
			s3 = "";
		Element el = (Element) root.selectSingleNode("./validator");
		if (el == null)
			el = root.addElement("validator");
		el.setText(s3);

		String s5 = (String) map.get("fld_cs5_OK");
		if (s5 != null && s5.length() > 0)
			root.addAttribute("autoTrim", "true");
		else
			root.addAttribute("autoTrim", "false");
		
		String s6 = (String) map.get("fld_cs4_OK");
		if (s6 != null && s6.length() > 0)
			root.addAttribute("autoGrow", "true");
		else
			root.addAttribute("autoGrow", "false");
		
		String s4 = (String) map.get("fld_rowNum");
		if (s4 != null && s4.length() > 0)
			root.addAttribute("rowNum", s4);
		else
			root.addAttribute("rowNum", "3");

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
		if ("true".equals(root.attributeValue("mutiline")))
			this.mutiline = true;
		if ("true".equals(root.attributeValue("html")))
			this.html = true;
		if ("true".equals(root.attributeValue("autoTrim")))
			this.autoTrim = true;
		if ("true".equals(root.attributeValue("autoGrow")))
			this.autoGrow = true;
		if (root.attributeValue("rowNum")!=null)
			this.rowNum = root.attributeValue("rowNum");
		Element el = (Element) root.selectSingleNode("./validator");
		if (el != null)
			this.validator = el.getText();
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#match(int,
	 *      java.lang.String, java.lang.String)
	 */
	public boolean match(int operationCode, String val, String val2) {

		if (operationCode == OperationCode.STRING_BEGIN) {
			return val.startsWith(val2);
		}
		if (operationCode == OperationCode.STRING_END) {
			return val.endsWith(val2);
		}
		return super.match(operationCode, val, val2);
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#cloneAttribute(com.hp.idc.itsm.configure.FieldInfo)
	 */
	public void cloneAttribute(FieldInfo info) {
		super.cloneAttribute(info);
		StringFieldInfo info2 = (StringFieldInfo) info;
		info2.setMutiline(this.mutiline);
		info2.setHtml(this.html);
		info2.setValidator(this.validator);
		info2.setAutoTrim(this.autoTrim);
		info2.setAutoGrow(this.autoGrow);
		info2.setRowNum(this.rowNum);
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getHtmlCode(java.lang.String)
	 */
	public String getHtmlCode(String value) {
		String str = StringUtil.escapeHtml(value);
		if (this.mutiline)
			str = str.replaceAll("\n", "<br/>");
		return str;
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getFormCode(int)
	 */
	public String getFormCode(int width) {
		StringBuffer sb = new StringBuffer();
		if (this.html)
			sb.append("var " + Consts.FLD_PREFIX + getId()
					+ " = new Ext.form.HtmlEditor({ height: 200,");
		else if (this.mutiline)
			sb.append("var " + Consts.FLD_PREFIX + getId()
					+ " = new Ext.form.TextArea({");
		else
			sb.append("var " + Consts.FLD_PREFIX + getId()
					+ " = new Ext.form.TextField({");
		if (this.transform != null)
			sb.append("transform:" + this.transform + ",");
		sb.append("fieldLabel:\"" + StringUtil.escapeJavaScript(getName()) + "\",");
		if (this.html)
			sb.append("id:'" + Consts.FLD_PREFIX + getId() + "',");
		else
			sb.append("name:'" + Consts.FLD_PREFIX + getId() + "',");
		if (this.notNull)
			sb.append("allowBlank:false,");
		if (this.validator != null && this.validator.length() > 0)
			sb.append("validator:" + this.validator + ",");
		if (this.mutiline) {
			sb.append("height:"+(Integer.parseInt(this.rowNum)*16)+",");
		}
		if (this.autoGrow) {
			sb.append("grow:true,");
		}
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

		CheckboxFieldInfo field5 = new CheckboxFieldInfo();
		TextDataSource ds5 = new TextDataSource();
		ds5.load("OK=");
		field5.setDataSource(ds5);
		field5.setName("�Զ�ȥ�հ�");
		field5.setId("cs5");
		if (this.autoTrim)
			field5.setValue("OK");
		returnList.add(field5);

		CheckboxFieldInfo field4 = new CheckboxFieldInfo();
		TextDataSource ds4 = new TextDataSource();
		ds4.load("OK=");
		field4.setDataSource(ds4);
		field4.setName("�������");
		field4.setId("cs1");
		if (this.mutiline)
			field4.setValue("OK");
		returnList.add(field4);
		
		CheckboxFieldInfo field3 = new CheckboxFieldInfo();
		TextDataSource ds3 = new TextDataSource();
		ds3.load("OK=");
		field3.setDataSource(ds3);
		field3.setName("�Զ�����");
		field3.setId("cs4");
		if (this.autoGrow)
			field3.setValue("OK");
		returnList.add(field3);

		/*
		 * CheckboxFieldInfo field2 = new CheckboxFieldInfo(); TextDataSource
		 * ds2 = new TextDataSource(); ds2.load("OK=");
		 * field2.setDataSource(ds2); field2.setName("���ı�ģʽ");
		 * field2.setId("cs2"); if (html) field2.setValue("OK");
		 * returnList.add(field2);
		 */
		StringFieldInfo field2 = new StringFieldInfo();
		field2.setName("����");
		field2.setId("rowNum");
		field2.setMutiline(false);
		field2.setValidator("function(v){var c=/^[-]{0,1}[1-9][0-9]*$/;if (c.test(v)) return true; return '������һ����ȷ����ֵ';}");
		if (this.rowNum != null && this.rowNum.length() > 0)
			field2.setValue(this.rowNum);
		returnList.add(field2);

		StringFieldInfo field1 = new StringFieldInfo();
		field1.setName("��֤����");
		field1.setId("validator");
		field1.setMutiline(true);
		if (this.validator != null && this.validator.length() > 0)
			field1.setValue(this.validator);
		returnList.add(field1);

		return returnList;
	}

	/**
	 * ��ȡ�Ƿ�Ϊ����ģʽ
	 * 
	 * @return �����Ƿ�Ϊ����ģʽ
	 */
	public boolean isMutiline() {
		return this.mutiline;
	}

	/**
	 * �����Ƿ�Ϊ����ģʽ
	 * 
	 * @param mutiline
	 *            �Ƿ�Ϊ����ģʽ
	 */
	public void setMutiline(boolean mutiline) {
		this.mutiline = mutiline;
	}

	/**
	 * ��ȡ�Ƿ�Ϊ���ı�ģʽ
	 * 
	 * @return �����Ƿ�Ϊ���ı�ģʽ
	 */
	public boolean isHtml() {
		return this.html;
	}

	/**
	 * �����Ƿ�Ϊ���ı�ģʽ
	 * 
	 * @param html
	 *            �Ƿ�Ϊ���ı�ģʽ
	 */
	public void setHtml(boolean html) {
		this.html = html;
	}

	/**
	 * ��ȡ�ı����麯��
	 * 
	 * @return �����ı����麯��
	 */
	public String getValidator() {
		return this.validator;
	}

	/**
	 * �����ı����麯��
	 * 
	 * @param validator
	 *            �ı����麯��
	 */
	public void setValidator(String validator) {
		this.validator = validator;
	}

	/**
	 * ��ȡ�Ƿ��Զ�ȥ���հ�
	 * @return �����Ƿ��Զ�ȥ���հ�
	 */
	public boolean isAutoTrim() {
		return this.autoTrim;
	}

	/**
	 * �����Ƿ��Զ�ȥ���հ�
	 * @param autoTrim �Ƿ��Զ�ȥ���հ�
	 */
	public void setAutoTrim(boolean autoTrim) {
		this.autoTrim = autoTrim;
	}

	public String getRowNum() {
		return rowNum;
	}

	public void setRowNum(String rowNum) {
		this.rowNum = rowNum;
	}

	public boolean isAutoGrow() {
		return autoGrow;
	}

	public void setAutoGrow(boolean autoGrow) {
		this.autoGrow = autoGrow;
	}

}
