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
 * 表示代码信息
 * 
 * @author 梅园
 * 
 */
public class CodeInfo extends TreeObject {

	/**
	 * 存储代码类型
	 */
	protected int typeOid = -1;

	/**
	 * 存储代码描述
	 */
	protected String desc = "";

	/**
	 * 存储代码图标
	 */
	protected int iconOid = -1;

	/**
	 * 存储代码是否使用
	 */
	protected boolean enabled = true;

	/**
	 * 存储代码的显示优先级,排序时使用
	 */
	protected int order = 0;

	/**
	 * 存储代码的XML数据
	 */
	protected String xmlData;

	/**
	 * 存储代码的XML文档对象
	 */
	protected Document xmlDoc;

	/**
	 * 代码的英文标识
	 */
	protected String codeId = "";

	protected boolean clickable = true;

	// 选中此代码时，提示的信息；
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
	 * 在新的对象要在缓存中替换本对象时调用
	 * 
	 * @param info
	 *            新的代码对象
	 */
	protected void onUpdateCache(CodeInfo info) {
		for (int i = 0; i < this.subItems.size(); i++) {
			TreeObject obj = (TreeObject) this.subItems.get(i);
			obj.setParent(info);
		}
	}

	/**
	 * 前台jsp做代码编辑时，获取本代码的特殊表单字段
	 * 
	 * @return 返回需要额外增加的表单字段
	 */
	public List getFormElements() {
		return new ArrayList();
	}

	/**
	 * 前台jsp做代码更新时调用
	 * 
	 * @param map
	 *            jsp页面的所有参数表
	 * @throws DocumentException
	 * @throws ClassNotFoundException
	 */
	protected void processPost(Map map) throws DocumentException,
			ClassNotFoundException {
		setXmlData("<code/>");
	}

	/**
	 * 默认构造函数
	 * 
	 */
	public CodeInfo() {
		// Nothing to do here
	}

	/**
	 * 生成代码的树状态数据，前台jsp使用
	 * 
	 * @return 返回代码的树状态数据
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
	 * 对数据库返回的结果集的当前记录进行分析
	 * 
	 * @param rs
	 *            结果集
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
	 * 获取代码的XML文档对象
	 * 
	 * @return 返回代码的XML文档对象
	 */
	public Document getXmlDoc() {
		return this.xmlDoc;
	}

	/**
	 * 获取代码相关数据的XML文档字符串
	 * 
	 * @return 返回获取代码相关数据的XML文档字符串
	 */
	public String getXmlData() {
		return this.xmlDoc.asXML();
	}

	/**
	 * 设置代码相关的XML数据
	 * 
	 * @param str
	 *            代码相关的XML数据
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
	 * 获取代码的显示优先级
	 * 
	 * @return 返回代码的显示优先级
	 */
	public int getOrder() {
		return this.order;
	}

	/**
	 * 设置代码的显示优先级
	 * 
	 * @param order
	 *            代码的显示优先级
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * 获取代码描述
	 * 
	 * @return 返回代码描述
	 */
	public String getDesc() {
		return this.desc == null ? this.name : this.desc;
	}

	/**
	 * 设置代码描述
	 * 
	 * @param desc
	 *            代码描述
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
	 * 获取代码是否使用
	 * 
	 * @return 返回代码是否使用
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * 设置代码是否使用
	 * 
	 * @param enabled
	 *            代码是否使用
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * 获取代码图标
	 * 
	 * @return 返回代码图标
	 */
	public int getIconOid() {
		return this.iconOid;
	}

	/**
	 * 设置代码图标
	 * 
	 * @param iconOid
	 *            代码图标
	 */
	public void setIconOid(int iconOid) {
		this.iconOid = iconOid;
	}

	/**
	 * 获取代码类型
	 * 
	 * @return 返回代码类型
	 */
	public int getTypeOid() {
		return this.typeOid;
	}

	/**
	 * 设置代码类型
	 * 
	 * @param typeOid
	 *            代码类型
	 */
	public void setTypeOid(int typeOid) {
		this.typeOid = typeOid;
	}
}
