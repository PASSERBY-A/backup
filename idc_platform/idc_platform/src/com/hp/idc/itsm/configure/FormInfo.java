package com.hp.idc.itsm.configure;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.common.IObjectWithAttribute;
import com.hp.idc.itsm.configure.fields.PersonFieldInfo;
import com.hp.idc.itsm.configure.fields.StringFieldInfo;
import com.hp.idc.itsm.dbo.ResultSetOperation;
import com.hp.idc.itsm.util.StringUtil;
import com.hp.idc.itsm.util.XmlUtil;

public class FormInfo implements IObjectWithAttribute {

	protected int oid;
	
	protected String id;

	protected String name;

	protected String applyTo;
	
	protected String origin;

	//XML 数据
	protected Document xmlDoc = null;
	
	protected String xmlData = null;

	public void parse(ResultSet rs) throws SQLException, IOException, DocumentException {
		parse(rs.getInt("FORM_OID"),
				rs.getString("FORM_NAME"),
				rs.getString("FORM_APPLYTO"),
				ResultSetOperation.clobToString(rs.getClob("FORM_CONFIGURE")),
				rs.getString("FORM_ORIGIN"));
	}

	public void parse(int oid, String name, 
			String applyto, String configure,String origin) throws DocumentException {
		setOid(oid);
		setName(name);
		setApplyTo(applyto);
		setOrigin(origin);
		if (configure == null || configure.length() == 0)
			configure = "<attribute/>";
		setXmlData(configure);
	}
	
	/**
	 * 2008-10-30改版后，流程里面的应用的表单，不再是关联表单配置里面的id，
	 * 而是直接读取流程配置中的<form>节点配置
	 * 此时，FormInfo对象中的OID/origin/applyTo将没有用处，为了兼容，暂做保留
	 */
	public static FormInfo parse(Element el) throws DocumentException {
		FormInfo retInfo = new FormInfo();
		if (el == null)
			return retInfo;
		String id = el.attributeValue("id");
		retInfo.setId(id);
		String name = el.attributeValue("name");
		if (name==null || name.equals(""))
			retInfo.setName(id);
		else
			retInfo.setName(name);
		retInfo.setXmlData(el.asXML());
		return retInfo;
	}

	/**
	 * 返回form表单中configure字段的xml文件中记录的id字段.对应于field中的oid
	 */
	public List getFields(){
		List returnList = new ArrayList();
		try{
			List list = new ArrayList();
			
			List list3 = xmlDoc.getRootElement().selectNodes("./field");
			// 保持兼容
			List list2 = xmlDoc.getRootElement().selectNodes("./fields");
			list.addAll(list2);
			list.addAll(list3);
			
			for (int i = 0; i < list.size();i++){
				Element el = (Element)list.get(i);
				String id = el.attributeValue("id");
				String name = el.attributeValue("name");
				String origin = el.attributeValue("origin");
				origin = (origin==null||origin.equals(""))?"ITSM":origin;
				
				boolean readOnly = "true".equals(el.attributeValue("readonly"));
				boolean notNull = "true".equals(el.attributeValue("notnull"));
				String els = el.attributeValue("injectData");
				if (els == null || els.equals(""))
					els = "true";
				boolean injectData = "true".equals(els);
				String filter = el.attributeValue("filter");
				String style = el.attributeValue("style");
				String rowNum = el.attributeValue("rowNum");
				if (rowNum==null || rowNum.length()==0 || Integer.parseInt(rowNum)<=0)
					rowNum = "3";
				String groupType = el.attributeValue("groupBy");
				String regulation = el.getText();
				if (filter == null)
					filter = "";
				if (style == null)
					style = "";
				if (id != null && id.length() > 0) {
					FieldInfo fieldInfo = null;
					// 保持兼容，如果是数字则按 oid 取
					if (id.charAt(0) >= '0' && id.charAt(0) <= '9')
						fieldInfo = FieldManager.getFieldByOid(origin,StringUtil.parseInt(id, -1));
					if (fieldInfo == null)
						fieldInfo = FieldManager.getFieldById(origin,id);
					if (fieldInfo == null)
						continue;
					fieldInfo = fieldInfo.clone(readOnly, notNull, filter, style,injectData);
					fieldInfo.setRegulation(regulation);
					if (name!=null && !name.equals(""))
						fieldInfo.setName(name);
					if (fieldInfo instanceof StringFieldInfo){
						((StringFieldInfo)fieldInfo).setRowNum(rowNum);
					}
					if (fieldInfo instanceof PersonFieldInfo) {
						if (groupType!=null && !groupType.equals("")) {
							((PersonFieldInfo)fieldInfo).setGroupBy(Integer.parseInt(groupType));
//							if (groupType.equals(Consts.RT_WORKGROUP_PERSON+""))
//								((PersonFieldInfo)fieldInfo).setGroupByWorkgroup();
//							else if (groupType.equals(Consts.RT_ORGANIZATION_PERSON+""))
//								((PersonFieldInfo)fieldInfo).setGroupByOrganization();
//							else if (groupType.equals(Consts.RT_ORGANIZATION_PERSON+""))
//								((PersonFieldInfo)fieldInfo).setGroupByOrganization();
						}
						
					}
					returnList.add(fieldInfo);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return returnList;
	}
	
	public String getScript() {
		Element el = (Element)xmlDoc.getRootElement().selectSingleNode("./script");
		if (el != null)
			return el.getText();
		return "";
	}

	public static String defaultConfig() {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\"?>");
		sb.append("<attribute>");
		sb.append("</attribute>");

		return sb.toString();
	}
	
	/**
	 * 返回归属模块的名字
	 * 
	 * @return
	 */
	public String getApplyToName() {
		return ModuleName.getModuleName(applyTo);
	}
	
	public String getApplyTo() {
		return applyTo;
	}
	public void setApplyTo(String applyTo) {
		/* 保证湖南兼容,更新后删除 */
		if (applyTo.equals("all"))
			applyTo = "" + ModuleName.ALL;
		else if (applyTo.equals("chg"))
			applyTo = "" + ModuleName.CHANGE;
		else if (applyTo.equals("cfg"))
			applyTo = "" + ModuleName.CONFIGURE;
		else if (applyTo.equals("inc"))
			applyTo = "" + ModuleName.INCIDENT;
		else if (applyTo.equals("prb"))
			applyTo = "" + ModuleName.PROBLEM;
		else if (applyTo.equals("req"))
			applyTo = "" + ModuleName.REQUIRE;
		/* 保证湖南兼容,更新后删除 end */

		this.applyTo = applyTo;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOid() {
		return oid;
	}
	public void setOid(int oid) {
		this.oid = oid;
	}

	public Document getXmlDoc() {
		return xmlDoc;
	}

	public void setXmlDoc(Document dataD) {
		this.xmlDoc = dataD;
	}

	public String getXmlData() {
		return xmlDoc.asXML();
	}

	public void setXmlData(String str) throws DocumentException {
		this.xmlData = str;
		Document doc = XmlUtil.parseString(xmlData);
		setXmlDoc(doc);
	}	

	public boolean inModule(int moduleOid) {
		return applyTo.equals("" + ModuleName.ALL)
			|| applyTo.equals("" + moduleOid)
			|| applyTo.startsWith("" + moduleOid + ",")
			|| applyTo.endsWith("," + moduleOid)
			|| (applyTo.indexOf("," + moduleOid + ",") != -1);
	}

	public String getAttribute(String id) {
		if (id == null)
			return null;
		if (id.equals("id"))
			return "" + getOid();
		if (id.equals("name"))
			return getName();
		if (id.equals("category"))
			return getApplyToName();
		return null;
	}

	public String getOrigin() {
		return origin==null||origin.equals("")?"ITSM":origin;
	}

	public void setOrigin(String origin) {
		origin = origin==null||origin.equals("")?"ITSM":origin;
		this.origin = origin;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
