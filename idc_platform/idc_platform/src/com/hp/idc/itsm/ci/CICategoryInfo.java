package com.hp.idc.itsm.ci;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.hp.idc.itsm.configure.FieldInfo;
import com.hp.idc.itsm.configure.FieldManager;
import com.hp.idc.itsm.configure.FormInfo;
import com.hp.idc.itsm.configure.FormManager;
import com.hp.idc.itsm.configure.ModuleName;
import com.hp.idc.itsm.configure.datasource.TextDataSource;
import com.hp.idc.itsm.configure.fields.SelectFieldInfo;
import com.hp.idc.itsm.configure.fields.StringFieldInfo;
import com.hp.idc.itsm.util.ItsmUtil;
import com.hp.idc.itsm.util.StringUtil;

/**
 * ��ʾ���������
 * @author ÷԰
 */
public class CICategoryInfo extends CodeInfo {
	/**
	 * �洢���������Ĺ�������JAVA�࣬�����͵�����������ɴ����ʵ��
	 */
	protected Class objectClass = null;

	/**
	 * �洢����������������е�������
	 * key = CIInfo.getId()
	 * value = CIInfo
	 */
	protected Map cis = new HashMap();

	/**
	 * �洢�������б���ֶ���(String)
	 */
	protected List fields = new ArrayList();

	/**
	 * �洢������ʹ�õı�oid
	 */
	protected int formOid = -1;

	/**
	 * ��дTreeObject.isClickable()����
	 * ���������������ʱ������Ե���˽ڵ㣨�ڽ����ϱ���Ϊ�Ҳ�����������б�
	 * @return ���ر��ڵ��Ƿ���Ե��
	 * @see com.hp.idc.itsm.common.TreeObject#isClickable()
	 */
	public boolean isClickable() {
		return (getFormOid() != -1);
	}
	
	/**
	 * ��ȡ������ʹ�õı�oid
	 * @return ����������ʹ�õı�oid
	 */
	public int getFormOid() {
		return this.formOid;
	}

	
	/**
	 * ���µĶ���Ҫ�ڻ������滻������ʱ����
	 * @see com.hp.idc.itsm.ci.CodeInfo#onUpdateCache(com.hp.idc.itsm.ci.CodeInfo)
	 */
	protected void onUpdateCache(CodeInfo info) {
		super.onUpdateCache(info);
		if (info.getClass().getName() != this.getClass().getName())
			return;
		CICategoryInfo c = (CICategoryInfo)info;
		c.cis = this.cis;
	}
	
	/**
	 * ��ȡ���������������еĹ�����ϵ���
	 * @return ���ش��������������еĹ�����ϵList<RelationTypeInfo>
	 */
	public List getRelationTypes() {
		List ret = CIManager.getRelationTypes();
		for (int i = 0; i < ret.size(); i++) {
			RelationTypeInfo info = (RelationTypeInfo)ret.get(i);
			if (info.getTypeA() != this.getOid() && info.getTypeA() > 0) {
				ret.remove(i);
				i--;
			}
		}
		return ret;
	}

	/**
	 * ��ȡ����������������
	 * @return ���ر���������������
	 */
	public List getCIs() {
		List l = new ArrayList();
		l.addAll(this.cis.values());
		return l;
	}

	/**
	 * ��ȡ��id�ֶ�ֵ����������ı���������������
	 * @param filter ����ʹ�õ�������ʽ
	 * @return ���������������������б�List<CIInfo>
	 */
	public List getCIs(String filter) {
		return getCIs(filter, "id");
	}

	/**
	 * ��ȡ��ָ���ֶ�ֵ����������ı���������������
	 * @param filter ����ʹ�õ�������ʽ
	 * @param field ָ�����ֶ�id
	 * @return ���������������������б�List<CIInfo>
	 */
	public List getCIs(String filter, String field) {
		List l = new ArrayList();
		l.addAll(this.cis.values());
		ItsmUtil.filter(l, field, filter);
		return l;
	}

	/**
	 * �򱾶���Ļ���������һ��������
	 * @param info ��������� 
	 */
	protected void addCI(CIInfo info) {
		this.cis.put(info.getId(), info);
	}

	/**
	 * �ӱ�����Ļ������ɾ��һ��������
	 * @param info ��������� 
	 */
	protected void removeCI(CIInfo info) {
		this.cis.remove(info.getId());
	}

	/**
	 * ��ȡ���������Ĺ�������JAVA�࣬�����͵�����������ɴ����ʵ��
	 * 
	 * @return �������������Ĺ�������JAVA��
	 */
	public Class getObjectClass() {
		return this.objectClass;
	}

	/**
	 * �������������Ĺ�������JAVA��
	 * 
	 * @param className
	 *            java�����������java.util.Map
	 * @throws ClassNotFoundException
	 *             �಻����ʱ����
	 */
	protected void setObjectClass(String className) throws ClassNotFoundException {
		if (className == null || className.length() == 0)
			this.objectClass = Class.forName("com.hp.idc.itsm.ci.CIInfo");
		else
			this.objectClass = Class.forName(className);
	}
	

	/**
	 * �����������б���ֶ���
	 * @param view �������б���ֶ������ԡ�,���ָ�
	 * @return ���ص�������ֶ���
	 */
	protected String setFields(String view) {
		String ret = (view == null) ? "" : view;
		ArrayList l = new ArrayList();
		String fs[] = ret.split(",");
		ret = "ci_id";
		l.add(ret);
		for (int i = 0; i < fs.length; i++) {
			String s = fs[i].trim(); 
			if (s.length() > 0 && !s.equals("ci_id")) {
				FieldInfo field = FieldManager.getFieldById("ITSM",s);
				if (field != null && !field.isEncrypt()) {
					l.add(s);
					ret += "," + s;
				}
			}
		}
		if (l.size() == 1) {
			l.add("name");
			ret += ",name"; 
		}
		this.fields = l;
		return ret;
	}
	/**
	 * �����ݿⷵ�صĽ�����ĵ�ǰ��¼���з���
	 * 
	 * @param rs
	 *            �����
	 * @throws SQLException
	 *             ���ݿ�����쳣ʱ����
	 * @throws DocumentException �����쳣ʱ����
	 * @throws IOException ��ȡ�쳣ʱ����
	 */
	public void parse(ResultSet rs) throws SQLException, DocumentException, IOException
	{
		super.parse(rs);
		
		Element el = getXmlDoc().getRootElement();
		String className = el.attributeValue("className");
		try {
			setObjectClass(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		String f = el.attributeValue("view");
		setFields(f);

		String fm = el.attributeValue("form");
		if (fm != null && fm.length() > 0)
			this.formOid = Integer.parseInt(fm);
	}

	/**
	 * ����������ʹ�õı�oid 
	 * @param formOid ������ʹ�õı�oid 
	 */
	public void setFormOid(int formOid) {
		this.formOid = formOid;
	}

	/**
	 * ��ȡ�洢�������б���ֶ���
	 * @return ���ش洢�������б���ֶ���List<String>
	 */
	public List getFields() {
		return this.fields;
	}
	
	/**
	 * ǰ̨jsp���������ʱ����
	 * @see com.hp.idc.itsm.ci.CodeInfo#processPost(java.util.Map)
	 */
	public void processPost(Map map) throws DocumentException, ClassNotFoundException {
		super.processPost(map);
		int fm = StringUtil.parseInt((String)map.get("fld_form"), -1);
		setFormOid(fm);
		Element el = getXmlDoc().getRootElement();
		el.addAttribute("form", "" + this.formOid);
		String v = (String)map.get("fld_view");
		v = setFields(v);
		el.addAttribute("view", v);
		v = (String)map.get("fld_class");
		setObjectClass(v);
		el.addAttribute("className", v);
	}

	/**
	 * ǰ̨jsp������༭ʱ����ȡ�������������ֶ�
	 * @return ������Ҫ�������ӵı��ֶ�
	 * @see com.hp.idc.itsm.ci.CodeInfo#getFormElements()
	 */
	public List getFormElements() {
		List returnList = new ArrayList();

		StringFieldInfo field1 = new StringFieldInfo();
		field1.setId("view");
		field1.setName("�б��ֶ�");
		String s1 = "";
		for (int i = 0; i < this.fields.size(); i++) {
			if (i > 0)
				s1 += ",";
			s1 += this.fields.get(i).toString();
		}
		field1.setValue(s1);
		returnList.add(field1);

		SelectFieldInfo field3 = new SelectFieldInfo();
		TextDataSource ds3 = new TextDataSource();
		field3.setDataSource(ds3);
		field3.setName("�������");
		field3.setId("form");
		StringBuffer data = new StringBuffer();
		List l = FormManager.getFormsOfModule(ModuleName.CONFIGURE);
		for (int i = 0; i < l.size(); i++) {
			if (i > 0)
				data.append("|");
			FormInfo f = (FormInfo)l.get(i);
			data.append(f.getOid());
			data.append("=");
			data.append(f.getName());
		}
		ds3.load(data.toString());
		field3.setValue("" + getFormOid());
		returnList.add(field3);
		
		SelectFieldInfo field4 = new SelectFieldInfo();
		TextDataSource ds4 = new TextDataSource();
		field4.setDataSource(ds4);
		field4.setName("ӳ����");
		field4.setId("class");
		ds4.load("com.hp.idc.itsm.ci.CIInfo=��|com.hp.idc.itsm.security.PersonInfo=����|com.hp.idc.itsm.security.WorkgroupInfo=������|com.hp.idc.itsm.security.OrganizationInfo=��֯");
		if (this.objectClass != null)
			field4.setValue(this.objectClass.getName());
		else
			field4.setValue("com.hp.idc.itsm.ci.CIInfo");
		returnList.add(field4);
		return returnList;
	}
}
