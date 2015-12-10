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
 * ��ʾ������<br>
 * ������ݱ�itsm_ci<br>
 * ��Ӧ���ݿ��ֶΣ�<br>
 * ci_oid -> TreeObject.oid<br>
 * ci_name -> TreeObject.name<br>
 * ci_parent_oid -> TreeObject.parentOid
 * 
 * @author ÷԰
 * 
 */
public class CIInfo extends TreeObject {

	/**
	 * ������״̬������
	 */
	public static int STATUS_NORMAL = 0;

	/**
	 * ������״̬����ɾ��
	 */
	public static int STATUS_DELETED = 1;

	/**
	 * �洢������ID ��Ӧ���ݿ��ֶΣ�ci_id
	 */
	protected String id;

	/**
	 * �洢���������Ա ��Ӧ���ݿ��ֶΣ�ci_admin
	 */
	protected String admin;

	/**
	 * �洢������״̬ ��Ӧ���ݿ��ֶΣ�ci_status
	 * 
	 * @see #STATUS_NORMAL
	 * @see #STATUS_DELETED
	 */
	protected int status;

	/**
	 * �洢���������� ��Ӧ���ݿ��ֶΣ�ci_type_oid
	 */
	protected int type;

	/**
	 * �洢�������ʱ�� ��Ӧ���ݿ��ֶΣ�ci_createtime
	 */
	protected long createTime = -1;

	/**
	 * �洢���������ʱ�� ��Ӧ���ݿ��ֶΣ�ci_lastupdate
	 */
	protected long lastUpdate = -1;

	/**
	 * �洢������XML���� ��Ӧ���ݿ��ֶΣ�ci_data
	 */
	protected String xmlData;

	/**
	 * �洢�������������XML����
	 */
	protected Document xmlDoc;

	/**
	 * �洢����������
	 */
	protected Map attr = new HashMap();

	/**
	 * Ĭ�Ϲ��캯��
	 * 
	 */
	public CIInfo() {
		// Nothing to do here
	}

	/**
	 * ���� id ��ѯ���������<br>
	 * CIInfo ��������԰�����<br>
	 * ci_admin: ���������Ա<br>
	 * ci_category: ���������<br>
	 * ci_status: ������״̬<br>
	 * ci_parent: ��������<br>
	 * ci_id: ������id<br>
	 * �Լ��������������Ժʹ�TreeObject�̳е�����
	 * 
	 * @see TreeObject#getAttribute(String)
	 * @param id
	 *            ��ѯ��ʶ
	 * @return ����ֵ���Ҳ���ʱ���� null
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
	 * ����ָ�����������������
	 * 
	 * @param oldCI
	 *            ָ��������
	 * @throws DocumentException
	 *             ����ʱ�����쳣����
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
	 * ��ȡ�����������еĹ�����ϵ���
	 * 
	 * @return ���ش��������������еĹ�����ϵList<RelationTypeInfo>
	 * @see CICategoryInfo#getRelationTypes
	 */
	public List getRelationTypes() {
		CICategoryInfo info = CIManager.getCICategoryByOid(this.getType());
		return info.getRelationTypes();
	}

	/**
	 * �����ݿⷵ�صĽ�����ĵ�ǰ��¼���з���
	 * 
	 * @param rs
	 *            �����
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
	 * �ж��û��Ƿ��й�����������Ȩ��<br>
	 * Ĭ��������������Ĺ���Ա���Ի�ϵͳ����Ĺ���Ա���û�ID��ͬ
	 * 
	 * @param id
	 *            �û�ID
	 * @return ��Ȩ�޷���true, ���򷵻�false
	 */
	public boolean isAdmin(String id) {
		return Consts.ADMIN_ID.equals(id) || this.admin.equals(id);
	}

	/**
	 * ��ָ���Ĳ������Խ��з���
	 * 
	 * @param oid
	 *            ������oid
	 * @param parentOid
	 *            ��������oid
	 * @param id
	 *            ������id
	 * @param createTime
	 *            �������ʱ��
	 * @param lastUpdate
	 *            ������������ʱ��
	 * @param status
	 *            ������״̬
	 * @param type
	 *            ����������
	 * @param admin
	 *            ���������Ա
	 * @param name
	 *            ����������
	 * @param data
	 *            ����������(xml)
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
	 * ����XML����
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
	 * ��ȡ�������ʱ��
	 * 
	 * @return �����������ʱ��
	 */
	public long getCreateTime() {
		return this.createTime;
	}

	/**
	 * �����������ʱ��
	 * 
	 * @param createTime
	 *            �������ʱ��
	 */
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	/**
	 * ��ȡ���������ʱ��
	 * 
	 * @return �������������ʱ��
	 */
	public long getLastUpdate() {
		return this.lastUpdate;
	}

	/**
	 * ����������������ʱ��
	 * 
	 * @param lastUpdate
	 *            ������������ʱ��
	 */
	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * �洢������״̬
	 * 
	 * @see #STATUS_NORMAL
	 * @see #STATUS_DELETED
	 * @return ����������״̬
	 */
	public int getStatus() {
		return this.status;
	}

	/**
	 * ����������״̬
	 * 
	 * @param status
	 *            ������״̬
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * ��ȡ���������ݵ�XML�ĵ�
	 * 
	 * @return �������������ݵ�XML�ĵ�
	 */
	public Document getXmlDoc() {
		return this.xmlDoc;
	}

	/**
	 * ��ȡ���������ݵ�XML�ĵ��ַ���
	 * 
	 * @return ���ػ�ȡ���������ݵ�XML�ĵ��ַ���
	 */
	public String getXmlData() {
		return this.xmlDoc.asXML();
	}

	/**
	 * ����������XML����
	 * 
	 * @param str
	 *            ������XML����
	 * @throws DocumentException
	 */
	public void setXmlData(String str) throws DocumentException {
		this.xmlData = str;
		Document doc = XmlUtil.parseString(this.xmlData);
		this.xmlDoc = doc;
	}

	/**
	 * ��ȡ���������Ա
	 * 
	 * @return �������������Ա
	 */
	public String getAdmin() {
		return this.admin;
	}

	/**
	 * �������������Ա
	 * 
	 * @param admin
	 *            ���������Ա
	 */
	public void setAdmin(String admin) {
		if (admin == null || admin.length() == 0)
			this.admin = Consts.ADMIN_ID;
		else
			this.admin = admin;
	}

	/**
	 * ��ȡ������ID
	 * 
	 * @return ����������ID
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * ����������ID
	 * 
	 * @param id
	 *            ������ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * ������������Ա����ʷ��¼
	 * 
	 * @param id
	 *            �ֶ�ID
	 * @param oldVal
	 *            ԭֵ
	 * @param newVal
	 *            ����ֵ
	 * @param operName
	 *            ������
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
	 * ��ȡ��ʷ��¼��Ϣ
	 * @param id ����ֶΣ�Ϊ�ջ�ȡ�����ֶε���ʷ��¼
	 * @return ������ʷ��¼��Ϣ List<HistoryLine>
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
	 * ��������������ֵ
	 * 
	 * @param id
	 *            �ֶ�ID
	 * @param value
	 *            �ֶ�ֵ
	 * @param operName
	 *            ������
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
	 * ��ȡ����������
	 * 
	 * @return ��������������
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * ��������������
	 * 
	 * @param type
	 *            ����������
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * ��ȡ������Ĺ�����ϵ
	 * 
	 * @see CIManager#getCIRelations(int, int, boolean)
	 * @param type
	 *            ������ϵ����
	 * @param includeRev
	 *            �Ƿ��������Ĺ�ϵ
	 * @return ����������Ĺ�����ϵ List<RelationInfo>
	 */
	public List getRelations(int type, boolean includeRev) {
		return CIManager.getCIRelations(this.oid, type, includeRev);
	}

	/**
	 * ��ȡ������Ĺ�����ϵ����
	 * 
	 * @return ����������Ĺ�����ϵ����
	 */
	public String getRelationDesc() {
		Element el = (Element) this.xmlDoc.getRootElement().selectSingleNode(
				"relations");
		if (el != null)
			return el.getText();
		return "";
	}

	/**
	 * ����������Ĺ�����ϵ����
	 * 
	 * @param desc
	 *            ������Ĺ�����ϵ����
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
	 * ��ȡ������Ĺ���������
	 * 
	 * @param type
	 *            ������ϵID
	 * @return ����������Ĺ��������� List<CIInfo>
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
	 * ���������Ĺ�����ϵ<br>
	 * �����������ʱ����
	 * 
	 * @param oldInfo
	 *            ԭ������
	 * @return null��ʾ�ɹ�,ʧ��ʱ���ش�������
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
					return "���ܺ��Լ����й���";
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
							return "�ظ��Ĺ�����ϵ:" + rtInfo.getCaption(item);
					}
					l0.add("" + b);
					if (l0.size() > 1 && rtInfo.getFlag().endsWith("1"))
						return "������ϵֻ��һ��һ����:" + rtInfo.getCaption(item);
				}
			}
		}

		return null;
	}
}
