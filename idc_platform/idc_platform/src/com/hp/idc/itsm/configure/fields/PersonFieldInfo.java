package com.hp.idc.itsm.configure.fields;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.hp.idc.itsm.ci.CIInfo;
import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.common.MacroManager;
import com.hp.idc.itsm.common.OperationCode;
import com.hp.idc.itsm.configure.FieldInfo;
import com.hp.idc.itsm.configure.datasource.TextDataSource;
import com.hp.idc.itsm.security.LocalgroupManager;
import com.hp.idc.itsm.security.OrganizationManager;
import com.hp.idc.itsm.security.PersonManager;
import com.hp.idc.itsm.security.WorkgroupManager;
import com.hp.idc.itsm.util.StringUtil;
import com.hp.idc.itsm.util.XmlUtil;

/**
 * 表示个人的字段
 * 
 * @author 梅园
 * 
 */
public class PersonFieldInfo extends FieldInfo {
	/**
	 * 存储是否为单一选择模式
	 */
	protected boolean singleMode = true;

	/**
	 * 存储是否允许选择组
	 */
	protected boolean allowSelectGroup = false;

	/**
	 * 存储人员分组模式
	 */
	protected int groupPersonBy = Consts.RT_WORKGROUP_PERSON;

	/**
	 * 加载人员数据的页面
	 */
	protected String dataURL = Consts.ITSM_HOME + "/ci/ciPersonTree.jsp";

	protected boolean pathMode = false;

	/**
	 * 设置人员分组模式：按工作组分组
	 * 
	 */
	public void setGroupByWorkgroup() {
		this.groupPersonBy = Consts.RT_WORKGROUP_PERSON;
	}

	/**
	 * 设置人员分组模式：按组织分组
	 * 
	 */
	public void setGroupByOrganization() {
		this.groupPersonBy = Consts.RT_ORGANIZATION_PERSON;
	}

	public void setGroupByLocalgroup() {
		this.groupPersonBy = Consts.RT_LOCALGROUP_PERSON;
	}
	
	public void setGroupBy(int gb){
		this.groupPersonBy = gb;
	}

	/**
	 * 获取是否为单一选择模式
	 * 
	 * @return 返回是否为单一选择模式
	 */
	public boolean isSingleMode() {
		return this.singleMode;
	}

	/**
	 * 设置是否为单一选择模式
	 * 
	 * @param singleMode
	 *            是否为单一选择模式
	 */
	public void setSingleMode(boolean singleMode) {
		this.singleMode = singleMode;
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#clone()
	 */
	public Object clone() {
		return new PersonFieldInfo();
	}

	/**
	 * operationCode=OperationCode.INCLUDE或NOT_INCLUDE时，regex值以“/”分隔后，再与val匹配
	 */
	public boolean match(int operationCode, String val, String regex) {
		if (operationCode == OperationCode.MATCH) {
			regex = regex.replaceAll("/", "/|/");
			if (regex.startsWith("/|/"))
				regex = regex.substring(2);
			if (regex.endsWith("/|/"))
				regex = regex.substring(0,regex.length()-2);
			return Pattern.matches(".*(" + regex + "){1}.*", val);
		} else if (operationCode == OperationCode.NOT_MATCH) {
			regex = regex.replaceAll("/", "/|/");
			if (regex.startsWith("/|/"))
				regex = regex.substring(2);
			if (regex.endsWith("/|/"))
				regex = regex.substring(0,regex.length()-2);
			return !Pattern.matches(".*(" + regex + "){1}.*", val);
		}
		return super.match(operationCode, val, regex);
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#cloneAttribute(com.hp.idc.itsm.configure.FieldInfo)
	 */
	public void cloneAttribute(FieldInfo info) {
		super.cloneAttribute(info);
		PersonFieldInfo info2 = (PersonFieldInfo) info;
		info2.setSingleMode(this.singleMode);
		info2.setPathMode(this.pathMode);
		info2.groupPersonBy = this.groupPersonBy;
		info2.setAllowSelectGroup(this.allowSelectGroup);
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
			root.addAttribute("singleMode", "false");
		else
			root.addAttribute("singleMode", "true");

		String s_path = (String) map.get("fld_cs_path_OK");
		if (s_path != null && s_path.length() > 0)
			root.addAttribute("pathMode", "true");
		else
			root.addAttribute("pathMode", "false");

		String s2 = (String) map.get("fld_cs2");
		root.addAttribute("groupBy", s2);

		String s3 = (String) map.get("fld_cs3_OK");
		if (s3 != null && s3.length() > 0)
			root.addAttribute("allowSelectGroup", "true");
		else
			root.addAttribute("allowSelectGroup", "false");
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
		if ("false".equals(root.attributeValue("singleMode")))
			this.singleMode = false;
		if ("true".equals(root.attributeValue("pathMode")))
			this.pathMode = true;
		this.groupPersonBy = StringUtil.parseInt(
				root.attributeValue("groupBy"), Consts.RT_WORKGROUP_PERSON);
		if ("true".equals(root.attributeValue("allowSelectGroup")))
			this.allowSelectGroup = true;
	}

	public String getFormCode(int width) {
		return getFormCode(width, null);
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getFormCode(int)
	 */
	public String getFormCode(int width, HttpServletRequest req) {
		if (this.style == null || this.style.equals(""))
			this.style = "person";
		StringBuffer sb = new StringBuffer();
		sb.append("var " + Consts.FLD_PREFIX + getId()
				+ " = new Ext.form.SelectDialogField({");
		sb.append("fieldLabel:\"" + StringUtil.escapeJavaScript(getName())
				+ "\",");
		sb.append("hiddenName :'" + Consts.FLD_PREFIX + getId() + "',");
		
		//流程OID
		String wfOid = "-1";
		if (req != null)
			wfOid = req.getParameter("wfOid");
		
		sb.append("selectUrl:'" + Consts.ITSM_HOME+"/configure/userSelectDialog.jsp',");
		sb.append("typeAheadUrl:'" + Consts.ITSM_HOME+"/configure/userSelectTypeAhead.jsp',");
		
		sb.append("forceSelection:true,");
		sb.append("editable:false,");
		sb.append("params: {groupType:" + this.groupPersonBy + ",origin:'"
				+ this.origin + "',wfOid:'"+wfOid+"'");
		if (!isSingleMode())
			sb.append(",singleMode: false");
		if (isPathMode())
			sb.append(",pathMode:true");
		if (isAllowSelectGroup())
			sb.append(",selectGroup:1");
		if (this.transform != null)
			sb.append(",transform:" + this.transform + "");
		if (this.filter != null && this.filter.length() > 0)
			sb.append(", filter: '"
					+ MacroManager.replaceMacro(this.filter, req) + "'");
		sb.append("},");
		sb.append("emptyText:'请选择...',");
		if (this.notNull)
			sb.append("allowBlank:false,");
		String d_ = getDesc();
		if (d_.equals(getName()))
			d_ = "";
		sb.append("desc:'" + StringUtil.escapeJavaScript(d_) + "',");
		sb.append("width:" + width);
		sb.append(",msgTarget:'title'});\n");
		if (this.readOnly)
			sb.append(Consts.FLD_PREFIX + getId() + ".setReadOnly(true);\n");
		return sb.toString();
	}

	public int getGroupPersonBy() {
		return groupPersonBy;
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getFormValue()
	 */
	public String getFormValue() {
		String[] vs = this.value.split(",");
		String ret = "";
		for (int i = 0; i < vs.length; i++) {
			if (vs[i].length() == 0)
				continue;
			if (ret.length() > 0)
				ret += ",";
			int pos = vs[i].lastIndexOf("/");
			String personId = vs[i].substring(pos + 1);
			if (pos == -1) {
				personId = vs[i];
			} else {
				personId = vs[i].substring(pos + 1);
			}
			String personName = personId;
			CIInfo ci = PersonManager.getPersonById(origin, personId);
			if (ci == null)
				ci = WorkgroupManager.getWorkgroupById(origin, personId);
			if (ci == null) {
				ci = OrganizationManager.getOrganizationById(origin, personId);
			}
			if (ci != null)
				personName = ci.getName();

			ret += vs[i] + "=" + personName;

		}
		return Consts.FLD_PREFIX + getId() + ".setValue(\""
				+ StringUtil.escapeJavaScript(ret) + "\");";
	}

	public String getPathValue() {
		return getPathValue(this.value);
	}

	public String getPathValue(String v) {
		if (v == null)
			return "";
		String ret = "";
		String[] vs = v.split(",");
		for (int i = 0; i < vs.length; i++) {
			if (vs[i].length() == 0)
				continue;
			if (ret.length() > 0)
				ret += ",";

			String[] subvs = vs[i].split("/");
			String subret = "";
			for (int j = 0; j < subvs.length; j++) {
				if (subvs[j] == null || subvs[j].length() == 0)
					continue;
				if (subvs[j].equals("_") || subvs[j].equals("-1"))
					continue;
				subret += "/";
				CIInfo ci = PersonManager.getPersonById(origin, subvs[j]);
				if (ci != null)
					subret += ci.getName();
				else {
					ci = WorkgroupManager.getWorkgroupById(origin, subvs[j]);
					if (ci != null)
						subret += ci.getName();
					else {
						ci = OrganizationManager.getOrganizationById(origin,
								subvs[j]);
						if (ci != null)
							subret += ci.getName();
						else
							subret += subvs[j];
					}
				}
			}
			ret += subret;
		}
		return ret;
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
			CIInfo ci = PersonManager.getPersonById(origin, vs[i]);
			if (ci != null)
				ret += ci.getName();
			else {
				ci = WorkgroupManager.getWorkgroupById(origin, vs[i]);
				if (ci != null)
					ret += ci.getName() + "(工作组)";
				else {
					ci = OrganizationManager.getOrganizationById(origin, vs[i]);
					if (ci != null)
						ret += ci.getName() + "(组织)";
					else {
						ci = LocalgroupManager.getGroupById(vs[i]);
						if (ci != null)
							ret += ci.getName() + "(工作组)";
						else
							ret += vs[i];

					}
				}
			}
			// else if (ci instanceof WorkgroupInfo)
			// ret += ci.getName() + "(工作组)";
			// else if (ci instanceof OrganizationInfo)
			// ret += ci.getName() + "(组织)";
			// else
			// ret += ci.getName();
		}
		return ret;
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getAttributes()
	 */
	public List getAttributes() {
		List returnList = super.getAttributes();

		CheckboxFieldInfo field3 = new CheckboxFieldInfo();
		TextDataSource ds3 = new TextDataSource();
		ds3.load("OK=");
		field3.setDataSource(ds3);
		field3.setName("允许多选");
		field3.setId("cs1");
		if (!isSingleMode())
			field3.setValue("OK");
		returnList.add(field3);

		CheckboxFieldInfo field2 = new CheckboxFieldInfo();
		TextDataSource ds2 = new TextDataSource();
		ds2.load("OK=");
		field2.setDataSource(ds2);
		field2.setName("包含路径");
		field2.setId("cs_path");
		if (isPathMode())
			field2.setValue("OK");
		returnList.add(field2);

		CheckboxFieldInfo field1 = new CheckboxFieldInfo();
		TextDataSource ds1 = new TextDataSource();
		ds1.load("OK=");
		field1.setDataSource(ds1);
		field1.setName("允许分配到组");
		field1.setId("cs3");
		if (isAllowSelectGroup())
			field1.setValue("OK");
		returnList.add(field1);

		SelectFieldInfo field4 = new SelectFieldInfo();
		TextDataSource ds4 = new TextDataSource();
		ds4.load("" + Consts.RT_WORKGROUP_PERSON + "=按工作组分组|"
				+ Consts.RT_ORGANIZATION_PERSON + "=按组织分组|"
				+ Consts.RT_LOCALGROUP_PERSON + "=按本地组");
		field4.setDataSource(ds4);
		field4.setName("分组模式");
		field4.setId("cs2");
		field4.setValue("" + this.groupPersonBy);
		returnList.add(field4);

		return returnList;
	}

	/**
	 * 获取是否允许选择组
	 * 
	 * @return 返回是否允许选择组
	 */
	public boolean isAllowSelectGroup() {
		return this.allowSelectGroup;
	}

	/**
	 * 设置是否允许选择组
	 * 
	 * @param allowSelectGroup
	 *            是否允许选择组
	 */
	public void setAllowSelectGroup(boolean allowSelectGroup) {
		this.allowSelectGroup = allowSelectGroup;
	}

	public String getDataURL() {
		return dataURL;
	}

	public void setDataURL(String dataURL) {
		this.dataURL = dataURL;
	}

	public boolean isPathMode() {
		return pathMode;
	}

	public void setPathMode(boolean pathMode) {
		this.pathMode = pathMode;
	}
}
