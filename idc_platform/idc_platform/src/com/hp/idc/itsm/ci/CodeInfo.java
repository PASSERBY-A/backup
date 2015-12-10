package com.hp.idc.itsm.ci;

import java.io.IOException;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.hp.idc.itsm.common.TreeObject;
import com.hp.idc.itsm.dbo.ResultSetOperation;
import com.hp.idc.itsm.util.StringUtil;
import com.hp.idc.itsm.util.XmlUtil;

/**
 * ��ʾ������Ϣ
 * 
 * @author ÷԰
 * 
 */
public class CodeInfo extends TreeObject {

	/**
	 * �洢��������
	 */
	protected int typeOid = -1;

	/**
	 * �洢��������
	 */
	protected String desc = "";

	/**
	 * �洢����ͼ��
	 */
	protected int iconOid = -1;

	/**
	 * �洢�����Ƿ�ʹ��
	 */
	protected boolean enabled = true;

	/**
	 * �洢�������ʾ���ȼ�,����ʱʹ��
	 */
	protected int order = 0;

	/**
	 * �洢�����XML����
	 */
	protected String xmlData;

	/**
	 * �洢�����XML�ĵ�����
	 */
	protected Document xmlDoc;

	/**
	 * �����Ӣ�ı�ʶ
	 */
	protected String codeId = "";

	protected boolean clickable = true;

	// ѡ�д˴���ʱ����ʾ����Ϣ��
	protected String alertMsg = "";

	public String getAlertMsg() {
		return alertMsg == null ? "" : alertMsg;
	}

	public void setAlertMsg(String alertMsg) {
		this.alertMsg = alertMsg;
		Element amEle = (Element) this.xmlDoc.getRootElement()
				.selectSingleNode("./alertMsg");
		if (amEle == null) {
			amEle = this.xmlDoc.getRootElement().addElement("alertMsg");
			amEle = amEle.addCDATA(alertMsg);
		} else {
			amEle.setText("");
			amEle.addCDATA(alertMsg);
		}
	}

	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	/**
	 * ���µĶ���Ҫ�ڻ������滻������ʱ����
	 * 
	 * @param info
	 *            �µĴ������
	 */
	protected void onUpdateCache(CodeInfo info) {
		for (int i = 0; i < this.subItems.size(); i++) {
			TreeObject obj = (TreeObject) this.subItems.get(i);
			obj.setParent(info);
		}
	}

	/**
	 * ǰ̨jsp������༭ʱ����ȡ�������������ֶ�
	 * 
	 * @return ������Ҫ�������ӵı��ֶ�
	 */
	public List getFormElements() {
		return new ArrayList();
	}

	/**
	 * ǰ̨jsp���������ʱ����
	 * 
	 * @param map
	 *            jspҳ������в�����
	 * @throws DocumentException
	 * @throws ClassNotFoundException
	 */
	protected void processPost(Map map) throws DocumentException,
			ClassNotFoundException {
		setXmlData("<code/>");
	}

	/**
	 * Ĭ�Ϲ��캯��
	 * 
	 */
	public CodeInfo() {
		// Nothing to do here
	}

	/**
	 * ���ɴ������״̬���ݣ�ǰ̨jspʹ��
	 * 
	 * @return ���ش������״̬����
	 */
	public String getData() {
		return getData("ID");
	}

	public String getData(String idType) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		if (this.subItems.size() > 0) {
			sb.append("children : [");
			for (int i = 0; i < this.subItems.size(); i++) {
				CodeInfo info = (CodeInfo) this.subItems.get(i);
				if (i > 0)
					sb.append(",");
				sb.append(info.getData(idType));
			}
			sb.append("],");
		}
		if (this.getIconOid() != -1)
			sb.append("icon : Ext.BLANK_IMAGE_URL,");
		if (this.isClickable())
			sb.append("_click : true,");
		if (idType != null && idType.equals("ID"))
			sb.append("id : '" + getCodeId() + "',");
		else
			sb.append("id : '" + getOid() + "',");
		sb.append("text : \"" + StringUtil.escapeHtml(getName()) + "\",");
		sb.append("alertMsg:\"" + StringUtil.escapeJavaScript(getAlertMsg())
				+ "\"}");
		return sb.toString();
	}

	/**
	 * �����ݿⷵ�صĽ�����ĵ�ǰ��¼���з���
	 * 
	 * @param rs
	 *            �����
	 * @throws SQLException
	 * @throws DocumentException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	protected void parse(ResultSet rs) throws SQLException, DocumentException,
			IOException {
		setOid(rs.getInt("code_oid"));
		setParentOid(rs.getInt("code_parent_oid"));
		setTypeOid(rs.getInt("code_type_oid"));
		setName(rs.getString("code_name"));
		setDesc(rs.getString("code_desc"));
		setIconOid(rs.getInt("code_icon_oid"));
		setOrder(rs.getInt("code_order"));
		setEnabled(rs.getInt("code_enabled") == 1);
		setClickable(rs.getInt("code_click") == 1);
		String codeId = rs.getString("code_id");
		if (codeId == null || codeId.equals(""))
			codeId = rs.getString("code_oid");
		setCodeId(codeId);
		Clob clob = rs.getClob("code_data");
		String str = ResultSetOperation.clobToString(clob);
		if (str == null || str.length() == 0)
			str = "<code/>";
		setXmlData(str);
	}

	/**
	 * ��ȡ�����XML�ĵ�����
	 * 
	 * @return ���ش����XML�ĵ�����
	 */
	public Document getXmlDoc() {
		return this.xmlDoc;
	}

	/**
	 * ��ȡ����������ݵ�XML�ĵ��ַ���
	 * 
	 * @return ���ػ�ȡ����������ݵ�XML�ĵ��ַ���
	 */
	public String getXmlData() {
		return this.xmlDoc.asXML();
	}

	/**
	 * ���ô�����ص�XML����
	 * 
	 * @param str
	 *            ������ص�XML����
	 * @throws DocumentException
	 */
	public void setXmlData(String str) throws DocumentException {
		this.xmlData = str;
		Document doc = XmlUtil.parseString(this.xmlData);
		this.xmlDoc = doc;

		Element amEle = (Element) doc.getRootElement().selectSingleNode(
				"./alertMsg");
		if (amEle != null)
			this.alertMsg = amEle.getText();
	}

	/**
	 * ��ȡ�������ʾ���ȼ�
	 * 
	 * @return ���ش������ʾ���ȼ�
	 */
	public int getOrder() {
		return this.order;
	}

	/**
	 * ���ô������ʾ���ȼ�
	 * 
	 * @param order
	 *            �������ʾ���ȼ�
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ���ش�������
	 */
	public String getDesc() {
		return this.desc == null ? this.name : this.desc;
	}

	/**
	 * ���ô�������
	 * 
	 * @param desc
	 *            ��������
	 */
	public void setDesc(String desc) {
		if (desc == null)
			this.desc = "";
		else
			this.desc = desc;
	}

	public boolean isClickable() {
		return this.clickable;
	}

	public void setClickable(boolean clickable) {
		this.clickable = clickable;
	}

	/**
	 * ��ȡ�����Ƿ�ʹ��
	 * 
	 * @return ���ش����Ƿ�ʹ��
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * ���ô����Ƿ�ʹ��
	 * 
	 * @param enabled
	 *            �����Ƿ�ʹ��
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * ��ȡ����ͼ��
	 * 
	 * @return ���ش���ͼ��
	 */
	public int getIconOid() {
		return this.iconOid;
	}

	/**
	 * ���ô���ͼ��
	 * 
	 * @param iconOid
	 *            ����ͼ��
	 */
	public void setIconOid(int iconOid) {
		this.iconOid = iconOid;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @return ���ش�������
	 */
	public int getTypeOid() {
		return this.typeOid;
	}

	/**
	 * ���ô�������
	 * 
	 * @param typeOid
	 *            ��������
	 */
	public void setTypeOid(int typeOid) {
		this.typeOid = typeOid;
	}
}
