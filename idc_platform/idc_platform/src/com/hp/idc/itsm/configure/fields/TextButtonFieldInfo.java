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
 * 带按钮的表单
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 *
 */
public class TextButtonFieldInfo extends FieldInfo  {
	
	/**
	 * 按钮文字
	 */
	protected String buttonText = "";
	
	/**
	 * 按钮点击的动作
	 */
	protected String buttonScript = ""; 
	
	public Object clone() {
		return new TextButtonFieldInfo();
	}
	
	public void cloneAttribute(FieldInfo info) {
		super.cloneAttribute(info);
		TextButtonFieldInfo info2 = (TextButtonFieldInfo) info;
		info2.setButtonText(this.buttonText);
		info2.setButtonScript(this.buttonScript);
	}
	
	public void parse() {
		super.parse();
		if (this.xmlDoc == null)
			return;
		Element root = this.xmlDoc.getRootElement();
		Element el = (Element) root.selectSingleNode("./buttonText");
		if (el!=null)
			this.buttonText = el.getText();
		el = (Element) root.selectSingleNode("./buttonScript");
		if (el!=null)
			this.buttonScript = el.getText();
	}

	/**
	 * 样式：<input name=""/><button>..</button>
	 * @param width
	 * @return
	 */
	public String getFormCode(int width) {
		StringBuffer sb = new StringBuffer();
		sb.append("var " + Consts.FLD_PREFIX + getId()
				+ " = new Ext.form.TextButtonField({");
		sb.append("fieldLabel:'" + StringUtil.escapeJavaScript(getName()) + "',");
		sb.append("name:'" + Consts.FLD_PREFIX + getId() + "',");
		if (buttonText!=null && !buttonText.equals("")) {
			sb.append("buttons:[{");
			sb.append("	text:'"+this.buttonText+"'");
			if (buttonScript!=null && !buttonScript.equals(""))
				sb.append(",handler:"+this.buttonScript);
			sb.append("}],");
		}
		String d_ = getDesc();
		if (d_.equals(getName()))
			d_ = "";
		sb.append("desc:'"+ StringUtil.escapeJavaScript(d_) +"',");
		sb.append("width:" + width);
		sb.append("});\n");
		return sb.toString();
	}
	
	public List getAttributes() {
		List returnList = super.getAttributes();
		StringFieldInfo field1 = new StringFieldInfo();
		field1.setName("按钮名称");
		field1.setId("buttonText");
		field1.setMutiline(false);
		if (this.buttonText != null && this.buttonText.length() > 0)
			field1.setValue(this.buttonText);
		returnList.add(field1);
		
		StringFieldInfo field2 = new StringFieldInfo();
		field2.setName("点击脚本");
		field2.setId("buttonScript");
		field2.setMutiline(true);
		if (this.buttonScript != null && this.buttonScript.length() > 0)
			field2.setValue(this.buttonScript);
		returnList.add(field2);
		
		return returnList;
	}
	
	public String getXmlConfig(Map map) throws DocumentException {
		String xml = super.getXmlConfig(map);
		Document doc = XmlUtil.parseString(xml);
		Element root = doc.getRootElement();

		String value = (String) map.get("fld_buttonText");
		if (value == null)
			value = "";
		Element el = (Element) root.selectSingleNode("./buttonText");
		if (el == null)
			el = root.addElement("buttonText");
		el.setText(value);

		value = (String) map.get("fld_buttonScript");
		if (value == null)
			value = "";
		el = (Element) root.selectSingleNode("./buttonScript");
		if (el == null)
			el = root.addElement("buttonScript");
		el.setText(value);
		return doc.asXML();
	}

	public String getButtonText() {
		return buttonText;
	}

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}

	public String getButtonScript() {
		return buttonScript;
	}

	public void setButtonScript(String buttonScript) {
		this.buttonScript = buttonScript;
	}
}
