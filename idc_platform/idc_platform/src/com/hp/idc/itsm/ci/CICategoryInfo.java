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
 * 表示配置项类别
 * @author 梅园
 */
public class CICategoryInfo extends CodeInfo {
	/**
	 * 存储配置项类别的关联代码JAVA类，本类型的配置项都会生成此类的实例
	 */
	protected Class objectClass = null;

	/**
	 * 存储此配置项类别下所有的配置项
	 * key = CIInfo.getId()
	 * value = CIInfo
	 */
	protected Map cis = new HashMap();

	/**
	 * 存储配置项列表的字段名(String)
	 */
	protected List fields = new ArrayList();

	/**
	 * 存储配置项使用的表单oid
	 */
	protected int formOid = -1;

	/**
	 * 重写TreeObject.isClickable()方法
	 * 当关联了配置项表单时，则可以点击此节点（在界面上表现为右侧出现配置项列表）
	 * @return 返回本节点是否可以点击
	 * @see com.hp.idc.itsm.common.TreeObject#isClickable()
	 */
	public boolean isClickable() {
		return (getFormOid() != -1);
	}
	
	/**
	 * 获取配置项使用的表单oid
	 * @return 返回配置项使用的表单oid
	 */
	public int getFormOid() {
		return this.formOid;
	}

	
	/**
	 * 在新的对象要在缓存中替换本对象时调用
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
	 * 获取此类别的配置项所有的关联关系类别
	 * @return 返回此类别的配置项所有的关联关系List<RelationTypeInfo>
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
	 * 获取本类别的所有配置项
	 * @return 返回本类别的所有配置项
	 */
	public List getCIs() {
		List l = new ArrayList();
		l.addAll(this.cis.values());
		return l;
	}

	/**
	 * 获取对id字段值满足过滤器的本类别的所有配置项
	 * @param filter 过滤使用的正则表达式
	 * @return 返回满足条件的配置项列表List<CIInfo>
	 */
	public List getCIs(String filter) {
		return getCIs(filter, "id");
	}

	/**
	 * 获取对指定字段值满足过滤器的本类别的所有配置项
	 * @param filter 过滤使用的正则表达式
	 * @param field 指定的字段id
	 * @return 返回满足条件的配置项列表List<CIInfo>
	 */
	public List getCIs(String filter, String field) {
		List l = new ArrayList();
		l.addAll(this.cis.values());
		ItsmUtil.filter(l, field, filter);
		return l;
	}

	/**
	 * 向本对象的缓存表中添加一个配置项
	 * @param info 配置项对象 
	 */
	protected void addCI(CIInfo info) {
		this.cis.put(info.getId(), info);
	}

	/**
	 * 从本对象的缓存表中删除一个配置项
	 * @param info 配置项对象 
	 */
	protected void removeCI(CIInfo info) {
		this.cis.remove(info.getId());
	}

	/**
	 * 获取配置项类别的关联代码JAVA类，本类型的配置项都会生成此类的实例
	 * 
	 * @return 返回配置项类别的关联代码JAVA类
	 */
	public Class getObjectClass() {
		return this.objectClass;
	}

	/**
	 * 设置配置项类别的关联代码JAVA类
	 * 
	 * @param className
	 *            java类的类名，如java.util.Map
	 * @throws ClassNotFoundException
	 *             类不存在时引发
	 */
	protected void setObjectClass(String className) throws ClassNotFoundException {
		if (className == null || className.length() == 0)
			this.objectClass = Class.forName("com.hp.idc.itsm.ci.CIInfo");
		else
			this.objectClass = Class.forName(className);
	}
	

	/**
	 * 设置配置项列表的字段名
	 * @param view 配置项列表的字段名，以“,”分隔
	 * @return 返回调整后的字段名
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
	 * 对数据库返回的结果集的当前记录进行分析
	 * 
	 * @param rs
	 *            结果集
	 * @throws SQLException
	 *             数据库操作异常时引发
	 * @throws DocumentException 数据异常时引发
	 * @throws IOException 读取异常时引发
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
	 * 设置配置项使用的表单oid 
	 * @param formOid 配置项使用的表单oid 
	 */
	public void setFormOid(int formOid) {
		this.formOid = formOid;
	}

	/**
	 * 获取存储配置项列表的字段名
	 * @return 返回存储配置项列表的字段名List<String>
	 */
	public List getFields() {
		return this.fields;
	}
	
	/**
	 * 前台jsp做代码更新时调用
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
	 * 前台jsp做代码编辑时，获取本代码的特殊表单字段
	 * @return 返回需要额外增加的表单字段
	 * @see com.hp.idc.itsm.ci.CodeInfo#getFormElements()
	 */
	public List getFormElements() {
		List returnList = new ArrayList();

		StringFieldInfo field1 = new StringFieldInfo();
		field1.setId("view");
		field1.setName("列表字段");
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
		field3.setName("配置项表单");
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
		field4.setName("映射类");
		field4.setId("class");
		ds4.load("com.hp.idc.itsm.ci.CIInfo=无|com.hp.idc.itsm.security.PersonInfo=个人|com.hp.idc.itsm.security.WorkgroupInfo=工作组|com.hp.idc.itsm.security.OrganizationInfo=组织");
		if (this.objectClass != null)
			field4.setValue(this.objectClass.getName());
		else
			field4.setValue("com.hp.idc.itsm.ci.CIInfo");
		returnList.add(field4);
		return returnList;
	}
}
