package com.hp.idc.itsm.configure.fields;

import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.configure.FieldDataSource;
import com.hp.idc.itsm.configure.FieldInfo;
import com.hp.idc.itsm.configure.datasource.CodeDataSource;
import com.hp.idc.itsm.configure.datasource.CodeTypeDataSource;
import com.hp.idc.itsm.configure.datasource.TextDataSource;
import com.hp.idc.itsm.util.XmlUtil;

/**
 * ������Դ���ֶΣ����ֶβ���ֱ��ʹ��
 * 
 * @author ÷԰
 * 
 */
public class FieldInfoWithDataSource extends FieldInfo {
	/**
	 * �洢����Դ����
	 */
	protected FieldDataSource dataSource;

	/**
	 * �洢����Դ��������
	 */
	protected String dataSourceClass = "";

	/**
	 * �洢����Դ����
	 */
	protected String dataSourceData = "";

	/**
	 * �洢�Ƿ�Ϊ��һѡ��ģʽ
	 */
	protected boolean singleMode = true;
	
	/**
	 * �Ƿ������Լ�����������֮�������
	 */
	protected boolean editable = false;

	/**
	 * ��ȡ�Ƿ�Ϊ��һѡ��ģʽ
	 * 
	 * @return �����Ƿ�Ϊ��һѡ��ģʽ
	 */
	public boolean isSingleMode() {
		return this.singleMode;
	}

	/**
	 * �����Ƿ�Ϊ��һѡ��ģʽ
	 * 
	 * @param singleMode
	 *            �Ƿ�Ϊ��һѡ��ģʽ
	 */
	public void setSingleMode(boolean singleMode) {
		this.singleMode = singleMode;
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#clone()
	 */
	public Object clone() {
		return new FieldInfoWithDataSource();
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#cloneAttribute(com.hp.idc.itsm.configure.FieldInfo)
	 */
	public void cloneAttribute(FieldInfo info) {
		super.cloneAttribute(info);
		FieldInfoWithDataSource info2 = (FieldInfoWithDataSource) info;
		info2.setDataSourceClass(this.dataSourceClass);
		info2.setDataSourceData(this.dataSourceData);
		info2.setDataSource(this.dataSource);
		info2.setEditable(this.editable);
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
		Element el = (Element) root.selectSingleNode("./dataSource");
		if (el == null) {
			el = root.addElement("dataSource");
		}
		String s0 = (String) map.get("fld_cs0_OK");
		String sSingle = (String) map.get("fld_single_OK");
		String s1 = (String) map.get("fld_cs1");
		String s2 = (String) map.get("fld_cs2");
		String s3 = (String) map.get("fld_cs3");
		String s4 = (String) map.get("fld_cs4");
		if (s0 != null && s0.length() > 0)
			root.addAttribute("editable", "true");
		else
			root.addAttribute("editable", "false");
		
		if (sSingle != null && sSingle.length() > 0)
			root.addAttribute("singleMode", "true");
		else
			root.addAttribute("singleMode", "false");
		
		
		if (s1 != null && s1.length() > 0) {
			el.addAttribute("type","com.hp.idc.itsm.configure.datasource.CodeDataSource");
			el.setText(s1);
		} else if (s2 != null && s2.length() > 0) {
			el.addAttribute("type","com.hp.idc.itsm.configure.datasource.CIDataSource");
			el.setText(s2);
		} else if (s3 != null && s3.length() > 0) {
			el.addAttribute("type","com.hp.idc.itsm.configure.datasource.RemoteDataSource");
			el.setText(s3);
		} else {
			if (s4 == null)
				s4 = "";
			el.addAttribute("type","com.hp.idc.itsm.configure.datasource.TextDataSource");
			el.setText(s4);
		}
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
		if ("true".equals(root.attributeValue("editable"))) {
			this.editable = true;
		}
		if ("false".equals(root.attributeValue("singleMode"))) {
			this.singleMode = false;
		}
		
		Element el = (Element) root.selectSingleNode("./dataSource");
		if (el != null) {
			this.dataSourceClass = el.attributeValue("type");
			this.dataSourceData = el.getText();
		} else {
			this.dataSourceClass = "com.hp.idc.itsm.configure.datasource.TextDataSource";
			this.dataSourceData = "";
		}
		setDataSource(FieldDataSource.create(this.dataSourceClass, this.dataSourceData));
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
			ret += this.dataSource.getDisplayText(vs[i]);
		}
		return ret;
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getAttributes()
	 */
	public List getAttributes() {
		List returnList = super.getAttributes();
		
		CheckboxFieldInfo field0 = new CheckboxFieldInfo();
		TextDataSource ds0 = new TextDataSource();
		ds0.load("OK=");
		field0.setDataSource(ds0);
		field0.setName("����༭");
		field0.setId("cs0");
		if (this.editable)
			field0.setValue("OK");
		returnList.add(field0);
		
		CheckboxFieldInfo fieldSingle = new CheckboxFieldInfo();
		TextDataSource dsSingle = new TextDataSource();
		dsSingle.load("OK=");
		fieldSingle.setDataSource(dsSingle);
		fieldSingle.setName("��ѡģʽ");
		fieldSingle.setId("single");
		if (this.singleMode)
			fieldSingle.setValue("OK");
		returnList.add(fieldSingle);
		
		SelectFieldInfo field3 = new SelectFieldInfo();
		CodeTypeDataSource ds3 = new CodeTypeDataSource();
		field3.setDataSource(ds3);
		field3.setName("��������");
		field3.setId("cs1");
		if (this.dataSourceClass
				.equals("com.hp.idc.itsm.configure.datasource.CodeDataSource"))
			field3.setValue(this.dataSourceData);
		returnList.add(field3);

		TreeFieldInfo field4 = new TreeFieldInfo();
		CodeDataSource ds4 = new CodeDataSource();
		ds4.load("" + Consts.CODETYPE_CICATEGORY);
		field4.setDataSource(ds4);
		field4.setName("����������");
		field4.setId("cs2");
		if (this.dataSourceClass
				.equals("com.hp.idc.itsm.configure.datasource.CIDataSource"))
			field4.setValue(this.dataSourceData);
		returnList.add(field4);

		StringFieldInfo field2 = new StringFieldInfo();
		field2.setName("URL��ַ");
		field2.setId("cs3");
		if (this.dataSourceClass
				.equals("com.hp.idc.itsm.configure.datasource.RemoteDataSource"))
			field2.setValue(this.dataSourceData);
		returnList.add(field2);
		
		StringFieldInfo field1 = new StringFieldInfo();
		field1.setName("�Զ�������");
		field1.setId("cs4");
		if (this.dataSourceClass
				.equals("com.hp.idc.itsm.configure.datasource.TextDataSource"))
			field1.setValue(this.dataSourceData);
		returnList.add(field1);

		return returnList;
	}

	/**
	 * ��ȡ����Դ����
	 * @return����������Դ����
	 */
	public FieldDataSource getDataSource() {
		return this.dataSource;
	}

	/**
	 * ��������Դ����
	 * @param dataSource ����Դ����
	 */
	public void setDataSource(FieldDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getDataSourceClass() {
		return dataSourceClass;
	}

	public void setDataSourceClass(String dataSourceClass) {
		this.dataSourceClass = dataSourceClass;
	}

	public String getDataSourceData() {
		return dataSourceData;
	}

	public void setDataSourceData(String dataSourceData) {
		this.dataSourceData = dataSourceData;
	}

	/**
	 * @return the editable
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * @param editable the editable to set
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
}
