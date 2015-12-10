package com.hp.idc.itsm.ci;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.common.HistoryLine;
import com.hp.idc.itsm.common.TreeObject;
import com.hp.idc.itsm.configure.FieldInfo;
import com.hp.idc.itsm.configure.FieldManager;
import com.hp.idc.itsm.dbo.ResultSetOperation;
import com.hp.idc.itsm.util.DateTimeUtil;
import com.hp.idc.itsm.util.XmlUtil;

/**
 * 表示配置项<br>
 * 相关数据表：itsm_ci<br>
 * 对应数据库字段：<br>
 * ci_oid -> TreeObject.oid<br>
 * ci_name -> TreeObject.name<br>
 * ci_parent_oid -> TreeObject.parentOid
 * 
 * @author 梅园
 * 
 */
public class CIInfo extends TreeObject {

	/**
	 * 配置项状态：正常
	 */
	public static int STATUS_NORMAL = 0;

	/**
	 * 配置项状态：已删除
	 */
	public static int STATUS_DELETED = 1;

	/**
	 * 存储配置项ID 对应数据库字段：ci_id
	 */
	protected String id;

	/**
	 * 存储配置项管理员 对应数据库字段：ci_admin
	 */
	protected String admin;

	/**
	 * 存储配置项状态 对应数据库字段：ci_status
	 * 
	 * @see #STATUS_NORMAL
	 * @see #STATUS_DELETED
	 */
	protected int status;

	/**
	 * 存储配置项类型 对应数据库字段：ci_type_oid
	 */
	protected int type;

	/**
	 * 存储配置项创建时间 对应数据库字段：ci_createtime
	 */
	protected long createTime = -1;

	/**
	 * 存储配置项更新时间 对应数据库字段：ci_lastupdate
	 */
	protected long lastUpdate = -1;

	/**
	 * 存储配置项XML数据 对应数据库字段：ci_data
	 */
	protected String xmlData;

	/**
	 * 存储分析后的配置项XML数据
	 */
	protected Document xmlDoc;

	/**
	 * 存储配置项属性
	 */
	protected Map attr = new HashMap();

	/**
	 * 默认构造函数
	 * 
	 */
	public CIInfo() {
		// Nothing to do here
	}

	/**
	 * 根据 id 查询对象的属性<br>
	 * CIInfo 对象的属性包括：<br>
	 * ci_admin: 配置项管理员<br>
	 * ci_category: 配置项类别<br>
	 * ci_status: 配置项状态<br>
	 * ci_parent: 父配置项<br>
	 * ci_id: 配置项id<br>
	 * 以及：配置项本身的属性和从TreeObject继承的属性
	 * 
	 * @see TreeObject#getAttribute(String)
	 * @param id
	 *            查询标识
	 * @return 属性值，找不到时返回 null
	 */
	public String getAttribute(String id) {
		if (id == null)
			return null;
		if (id.equals("ci_admin"))
			return this.admin;
		if (id.equals("ci_category"))
			return "" + this.type;
		if (id.equals("ci_status"))
			return "" + this.status;
		if (id.equals("ci_parent")) {
			if (this.parent != null)
				return ((CIInfo)this.parent).getId();
			return "";
		}
		if (id.equals("ci_id"))
			return this.id;
		String str = (String) this.attr.get(id);
		if (str != null)
			return str;
		return super.getAttribute(id);
	}


	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof CIInfo))
			return false;
		return (getOid() == ((CIInfo)obj).getOid());
	}
	/**
	 * 复制指定配置项的所有属性
	 * 
	 * @param oldCI
	 *            指定配置项
	 * @throws DocumentException
	 *             复制时发生异常引发
	 */
	public void cloneAttribute(CIInfo oldCI) throws DocumentException {
		if (oldCI == null)
			return;
		parse(oldCI.getOid(), oldCI.getParentOid(), oldCI.getId(), oldCI
				.getCreateTime(), oldCI.getLastUpdate(), oldCI.getStatus(),
				oldCI.getType(), oldCI.getAdmin(), oldCI.getName(), oldCI
						.getXmlData());
	}

	/**
	 * 获取此配置项所有的关联关系类别
	 * 
	 * @return 返回此类别的配置项所有的关联关系List<RelationTypeInfo>
	 * @see CICategoryInfo#getRelationTypes
	 */
	public List getRelationTypes() {
		CICategoryInfo info = CIManager.getCICategoryByOid(this.getType());
		return info.getRelationTypes();
	}

	/**
	 * 对数据库返回的结果集的当前记录进行分析
	 * 
	 * @param rs
	 *            结果集
	 * @throws SQLException
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ParseException
	 */
	protected void parse(ResultSet rs) throws SQLException, IOException,
			DocumentException, ParseException {
		long t1 = -1;
		long t2 = -1;
		if (rs.getTimestamp("CI_CREATETIME") != null)
			t1 = rs.getTimestamp("CI_CREATETIME").getTime();
		if (rs.getTimestamp("CI_LASTUPDATE") != null)
			t2 = rs.getTimestamp("CI_LASTUPDATE").getTime();
		parse(rs.getInt("CI_OID"), rs.getInt("CI_PARENT_OID"), rs
				.getString("CI_ID"), t1, t2, rs.getInt("CI_STATUS"), rs
				.getInt("CI_TYPE_OID"), rs.getString("CI_ADMIN"), rs
				.getString("CI_NAME"), ResultSetOperation.clobToString(rs
				.getClob("CI_DATA")));
	}

	/**
	 * 判断用户是否有管理此配置项的权限<br>
	 * 默认条件：配置项的管理员属性或系统定义的管理员与用户ID相同
	 * 
	 * @param id
	 *            用户ID
	 * @return 有权限返回true, 否则返回false
	 */
	public boolean isAdmin(String id) {
		return Consts.ADMIN_ID.equals(id) || this.admin.equals(id);
	}

	/**
	 * 对指定的参数属性进行分析
	 * 
	 * @param oid
	 *            配置项oid
	 * @param parentOid
	 *            父配置项oid
	 * @param id
	 *            配置项id
	 * @param createTime
	 *            配置项创建时间
	 * @param lastUpdate
	 *            配置项最后更新时间
	 * @param status
	 *            配置项状态
	 * @param type
	 *            配置项类型
	 * @param admin
	 *            配置项管理员
	 * @param name
	 *            配置项名称
	 * @param data
	 *            配置项数据(xml)
	 * @throws DocumentException
	 */
	protected void parse(int oid, int parentOid, String id, long createTime,
			long lastUpdate, int status, int type, String admin, String name,
			String data) throws DocumentException {
		setOid(oid);
		setParentOid(parentOid);
		setId(id);
		setCreateTime(createTime);
		setLastUpdate(lastUpdate);
		setStatus(status);
		setType(type);
		setAdmin(admin);
		setName(name);
		if (data == null || data.length() == 0)
			setXmlData("<ci/>");
		else
			setXmlData(data);
		parseXmlData();
	}

	/**
	 * 分析XML数据
	 * 
	 */
	protected void parseXmlData() {
		Element el = this.xmlDoc.getRootElement();
		List attrList = el.selectNodes("./fields/field");
		for (int i = 0; i < attrList.size(); i++) {
			Element e = (Element) attrList.get(i);
			this.attr.put(e.attributeValue("id"), e.getText());
		}
	}

	/**
	 * 获取配置项创建时间
	 * 
	 * @return 返回配置项创建时间
	 */
	public long getCreateTime() {
		return this.createTime;
	}

	/**
	 * 设置配置项创建时间
	 * 
	 * @param createTime
	 *            配置项创建时间
	 */
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取配置项更新时间
	 * 
	 * @return 返回配置项更新时间
	 */
	public long getLastUpdate() {
		return this.lastUpdate;
	}

	/**
	 * 设置配置项最后更新时间
	 * 
	 * @param lastUpdate
	 *            配置项最后更新时间
	 */
	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * 存储配置项状态
	 * 
	 * @see #STATUS_NORMAL
	 * @see #STATUS_DELETED
	 * @return 返回配置项状态
	 */
	public int getStatus() {
		return this.status;
	}

	/**
	 * 设置配置项状态
	 * 
	 * @param status
	 *            配置项状态
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 获取配置项数据的XML文档
	 * 
	 * @return 返回配置项数据的XML文档
	 */
	public Document getXmlDoc() {
		return this.xmlDoc;
	}

	/**
	 * 获取配置项数据的XML文档字符串
	 * 
	 * @return 返回获取配置项数据的XML文档字符串
	 */
	public String getXmlData() {
		return this.xmlDoc.asXML();
	}

	/**
	 * 设置配置项XML数据
	 * 
	 * @param str
	 *            配置项XML数据
	 * @throws DocumentException
	 */
	public void setXmlData(String str) throws DocumentException {
		this.xmlData = str;
		Document doc = XmlUtil.parseString(this.xmlData);
		this.xmlDoc = doc;
	}

	/**
	 * 获取配置项管理员
	 * 
	 * @return 返回配置项管理员
	 */
	public String getAdmin() {
		return this.admin;
	}

	/**
	 * 设置配置项管理员
	 * 
	 * @param admin
	 *            配置项管理员
	 */
	public void setAdmin(String admin) {
		if (admin == null || admin.length() == 0)
			this.admin = Consts.ADMIN_ID;
		else
			this.admin = admin;
	}

	/**
	 * 获取配置项ID
	 * 
	 * @return 返回配置项ID
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * 设置配置项ID
	 * 
	 * @param id
	 *            配置项ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 添加配置项属性变更历史记录
	 * 
	 * @param id
	 *            字段ID
	 * @param oldVal
	 *            原值
	 * @param newVal
	 *            更新值
	 * @param operName
	 *            更新人
	 */
	protected void addHistory(String id, String oldVal, String newVal,
			String operName) {
		Element el = (Element) this.xmlDoc.getRootElement().selectSingleNode(
				"./historys");
		if (el == null) {
			el = this.xmlDoc.getRootElement().addElement("historys");
		}
		Element el2 = el.addElement("history");
		el2.addAttribute("operName", operName);
		el2.addAttribute("operDate", DateTimeUtil.formatDate(System
				.currentTimeMillis()));
		if (oldVal != null)
			el2.addAttribute("oldVal", oldVal);
		el2.addAttribute("newVal", newVal);
		el2.addAttribute("id", id);
	}

	/**
	 * 获取历史记录信息
	 * @param id 相关字段，为空获取所有字段的历史记录
	 * @return 返回历史记录信息 List<HistoryLine>
	 */
	public List getHistoryLines(String id) {
		List list = new ArrayList();
		Element el = (Element) this.xmlDoc.getRootElement().selectSingleNode(
				"./historys");
		if (el == null)
			return list;
		List els = el.selectNodes("./history");
		for (int i = 0; i < els.size(); i++) {
			Element el2 = (Element)els.get(i);
			if (id != null && !id.equals(el2.attributeValue("id")))
				continue;
			HistoryLine line = new HistoryLine();
			line.setDate(el2.attributeValue("operDate"));
			line.setId(el2.attributeValue("id"));
			line.setNewValue(el2.attributeValue("newVal"));
			line.setOldValue(el2.attributeValue("oldVal"));
			line.setOperName(el2.attributeValue("operName"));
			list.add(line);
		}
		return list;
	}

	/**
	 * 设置配置项属性值
	 * 
	 * @param id
	 *            字段ID
	 * @param value
	 *            字段值
	 * @param operName
	 *            操作人
	 */
	protected void setAttribute(String id, String value, String operName) {

		Element el = (Element) this.xmlDoc.getRootElement().selectSingleNode(
				"./fields");
		if (el == null) {
			el = this.xmlDoc.getRootElement().addElement("fields");
		}
		String oldVal = null;
		String newVal = (value == null) ? "" : value;
		Element el2 = (Element) el
				.selectSingleNode("./field[@id='" + id + "']");
		if (el2 == null) {
			el2 = el.addElement("field");
			el2.addAttribute("id", id);
		} else {
			oldVal = el2.getText();
		}
		if (newVal.equals(oldVal))
			return;
		el2.setText(newVal);
		this.attr.put(id, newVal);
		FieldInfo field = FieldManager.getFieldById("ITSM",id);
		if (field != null) {
			if (oldVal != null) {
				if (!field.isEncrypt())
					oldVal = field.getHtmlCode(oldVal);
			}
			if (!field.isEncrypt())
				newVal = field.getHtmlCode(newVal);
		}
		addHistory(id, oldVal, newVal, operName);
	}

	/**
	 * 获取配置项类型
	 * 
	 * @return 返回配置项类型
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * 设置配置项类型
	 * 
	 * @param type
	 *            配置项类型
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 获取配置项的关联关系
	 * 
	 * @see CIManager#getCIRelations(int, int, boolean)
	 * @param type
	 *            关联关系类型
	 * @param includeRev
	 *            是否包括逆向的关系
	 * @return 返回配置项的关联关系 List<RelationInfo>
	 */
	public List getRelations(int type, boolean includeRev) {
		return CIManager.getCIRelations(this.oid, type, includeRev);
	}

	/**
	 * 获取配置项的关联关系描述
	 * 
	 * @return 返回配置项的关联关系描述
	 */
	public String getRelationDesc() {
		Element el = (Element) this.xmlDoc.getRootElement().selectSingleNode(
				"relations");
		if (el != null)
			return el.getText();
		return "";
	}

	/**
	 * 设置配置项的关联关系描述
	 * 
	 * @param desc
	 *            配置项的关联关系描述
	 */
	protected void setRelationDesc(String desc) {
		Element el = (Element) this.xmlDoc.getRootElement().selectSingleNode(
				"relations");
		if (el == null) {
			el = this.xmlDoc.getRootElement().addElement("relations");
		}
		el.setText(desc);
	}

	/**
	 * 获取配置项的关联配置项
	 * 
	 * @param type
	 *            关联关系ID
	 * @return 返回配置项的关联配置项 List<CIInfo>
	 */
	public List getRelationObjects(int type) {
		List ret = new ArrayList();
		List l = getRelations(type, true);
		for (int i = 0; i < l.size(); i++) {
			RelationInfo info = (RelationInfo) l.get(i);
			if (info.getObjectA().getOid() == this.oid)
				ret.add(info.getObjectB());
			else
				ret.add(info.getObjectA());
		}
		return ret;
	}

	/**
	 * 检查配置项的关联关系<br>
	 * 在配置项更新时调用
	 * 
	 * @param oldInfo
	 *            原配置项
	 * @return null表示成功,失败时返回错误描述
	 */
	protected String checkRelations(CIInfo oldInfo) {
		Map m = new HashMap();
		if (oldInfo != null) {
			List l2 = CIManager.getCIRelations(oldInfo.getOid(), -1, true);
			for (int i = 0; i < l2.size(); i++) {
				RelationInfo r = (RelationInfo) l2.get(i);
				if (r.getObjectB() == oldInfo) {
					int n = r.getType().getReverseOid();
					if (n > 0) {
						List l0 = (List) m.get("" + n);
						if (l0 == null) {
							l0 = new ArrayList();
							m.put("" + n, l0);
						}
						l0.add("" + r.getObjectA().getOid());
					}
				}
			}
		}
		String str = getRelationDesc();
		if (str.length() > 0) {
			String[] vals = str.split(";");
			for (int i = 0, pos; i < vals.length; i++) {
				if (vals[i].length() == 0)
					continue;
				String s = vals[i];
				if ((pos = s.lastIndexOf("-")) == -1)
					continue;
				int a = Integer.parseInt(s.substring(0, pos));
				int b = Integer.parseInt(s.substring(pos + 1));
				if (b == this.oid || (oldInfo != null && oldInfo.getOid() == b))
					return "不能和自己进行关联";
				CIInfo item = CIManager.getCIByOid(b);
				RelationTypeInfo rtInfo = CIManager.getRelationTypeByOid(a);
				if (item != null && rtInfo != null) {
					List l0 = (List) m.get("" + a);
					if (l0 == null) {
						l0 = new ArrayList();
						m.put("" + a, l0);
					}
					for (int j = 0; j < l0.size(); j++) {
						if (l0.get(j).equals("" + b))
							return "重复的关联关系:" + rtInfo.getCaption(item);
					}
					l0.add("" + b);
					if (l0.size() > 1 && rtInfo.getFlag().endsWith("1"))
						return "关联关系只能一对一进行:" + rtInfo.getCaption(item);
				}
			}
		}

		return null;
	}
}
