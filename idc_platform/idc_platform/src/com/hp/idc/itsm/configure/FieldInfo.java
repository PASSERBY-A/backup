package com.hp.idc.itsm.configure;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.common.IObjectWithAttribute;
import com.hp.idc.itsm.common.OperationCode;
import com.hp.idc.itsm.dbo.ResultSetOperation;
import com.hp.idc.itsm.util.StringUtil;
import com.hp.idc.itsm.util.XmlUtil;

/**
 * 表示字段信息
 * 
 * @author 梅园
 * 
 */
public class FieldInfo implements IObjectWithAttribute {

	/**
	 * 系统字段
	 */
	public static final int TYPE_SYSTEM_IS = 0;
	/**
	 * 非系统字段
	 */
	public static final int TYPE_SYSTEM_NO = 1;

	/**
	 * oid, 对应 fld_oid 字段
	 */
	protected int oid;

	/**
	 * 字段id, 对应 fld_id 字段
	 */
	protected String id;

	protected String origin = "";

	/**
	 * 存储字段值
	 */
	protected String value = "";

	/**
	 * 字段名称, 对应 fld_name 字段
	 */
	protected String name;

	/**
	 * 字段使用范围, 对应 fld_applyto 字段, 参见 ModuleName.java
	 */
	protected String applyTo;

	/**
	 * 字段使用类型, 对应 fld_type 字段
	 * 
	 * @see FieldManager#getFieldTypes()
	 */
	protected String type;

	/**
	 * 存储XML配置的文档对象
	 */
	protected Document xmlDoc = null;

	/**
	 * 存储XML配置的数据
	 */
	protected String xmlData = null;

	/**
	 * 存储字段是否只读
	 */
	protected boolean readOnly = false;

	/**
	 * 存储字段是否非空
	 */
	protected boolean notNull = false;

	/**
	 * 是否填充预定值（用于流程中某节点，是否显示历史节点中相同字段的值）
	 */
	protected boolean injectData = true;

	/**
	 * 指定字段应用到现有的HTML元素ID,null为不做应用
	 */
	protected String transform = null;

	/**
	 * 存储字段描述
	 */
	protected String desc = "";

	/**
	 * 存储字段过滤条件
	 */
	protected String filter = null;

	/**
	 * 存储字段显示样式
	 */
	protected String style = null;

	/**
	 * 存储是否为系统字段
	 */
	protected boolean system = false;

	/**
	 * 字段的规则，配表单的时候配的，比如当值改变的时候，当有焦点的时候。。。。要引发一个动作
	 */
	protected String regulation = "";

	/**
	 * 获取是否为系统字段
	 * 
	 * @return 返回是否为系统字段
	 */
	public boolean isSystem() {
		return this.system;
	}

	/**
	 * 设置是否为系统字段
	 * 
	 * @param system
	 *            是否为系统字段
	 */
	public void setSystem(boolean system) {
		this.system = system;
	}

	/**
	 * 对指定操作符进行两个值的比较
	 * 
	 * @param operationCode
	 * @param val
	 * @param regex
	 *            如果是介于,val2以分号分割两个值；
	 * @return 比较结果
	 */
	public boolean match(int operationCode, String val, String regex) {
		if (operationCode == OperationCode.EQUAL)
			return val.equals(regex);
		else if (operationCode == OperationCode.NOT_EQUAL)
			return !val.equals(regex);
		else if (operationCode == OperationCode.LESS_THAN)
			return (val.compareTo(regex) < 0);
		else if (operationCode == OperationCode.GREATE_THAN)
			return (val.compareTo(regex) > 0);
		else if (operationCode == OperationCode.LESS_OR_EQUAL)
			return (val.compareTo(regex) <= 0);
		else if (operationCode == OperationCode.GREATE_OR_EQUAL)
			return (val.compareTo(regex) >= 0);
		else if (operationCode == OperationCode.INCLUDE)
			return val.indexOf(regex) != -1;
		else if (operationCode == OperationCode.NOT_INCLUDE)
			return val.indexOf(regex) == -1;
		else if (operationCode == OperationCode.NULL)
			return (val.length() == 0);
		else if (operationCode == OperationCode.NOT_NULL)
			return (val.length() > 0);
		else if (operationCode == OperationCode.MATCH)
			return Pattern.matches(".*" + regex + ".*", val);
		else if (operationCode == OperationCode.NOT_MATCH)
			return !Pattern.matches(".*" + regex + ".*", val);
		return true;
	}

	/**
	 * 获取字段描述
	 * 
	 * @return 返回字段描述
	 */
	public String getDesc() {
		return this.desc == null ? "" : this.desc;
	}

	/**
	 * 设置字段描述
	 * 
	 * @param desc
	 *            字段描述
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * 获取字段过滤条件
	 * 
	 * @return 返回字段过滤条件
	 */
	public String getFilter() {
		return this.filter;
	}

	/**
	 * 设置字段过滤条件
	 * 
	 * @param filter
	 *            字段过滤条件
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * 表示字段是否加密，在派生类中进行重载
	 * 
	 * @return 返回字段是否加密
	 */
	public boolean isEncrypt() {
		return false;
	}

	/**
	 * 获取字段显示样式
	 * 
	 * @return 返回字段显示样式
	 */
	public String getStyle() {
		return this.style;
	}

	/**
	 * 设置字段显示样式
	 * 
	 * @param style
	 *            字段显示样式
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * 设置字段是否必填
	 * 
	 * @param notNull
	 *            字段是否必填
	 */
	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}

	/**
	 * 设置字段是否只读
	 * 
	 * @param readOnly
	 *            字段是否只读
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	/**
	 * 生成本对象的一个副本
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		return new FieldInfo();
	}

	/**
	 * 获取字段的关联关系类型
	 * 
	 * @see Consts#RELATION_CI_CI
	 * @return 返回字段的关联关系类型
	 */
	public int getRelationType() {
		return -1;
	}

	public FieldInfo cloneFieldInfo() {
		FieldInfo info = (FieldInfo) clone();
		cloneAttribute(info);
		info.setFilter(this.filter);
		info.setNotNull(this.notNull);
		info.setReadOnly(this.readOnly);
		info.setStyle(this.style);
		info.setInjectData(this.injectData);
		return info;
	}

	/**
	 * 以指定的参数生成本对象的副本
	 * 
	 * @param readOnly
	 *            是否只读
	 * @param notNull
	 *            是否必填
	 * @param filter
	 *            过滤条件
	 * @param style
	 *            显示样式
	 * @return 生成本对象的副本
	 */
	public FieldInfo clone(boolean readOnly, boolean notNull, String filter, String style, boolean injectData) {
		FieldInfo info = (FieldInfo) clone();
		cloneAttribute(info);
		info.setFilter(filter);
		info.setNotNull(notNull);
		info.setReadOnly(readOnly);
		info.setStyle(style);
		info.setInjectData(injectData);
		return info;
	}

	/**
	 * 复制属性到指定的对象中, 在派生类中重载此函数
	 * 
	 * @param info
	 *            指定的对象
	 */
	public void cloneAttribute(FieldInfo info) {
		info.setDesc(this.desc);
		info.setId(this.id);
		info.setName(this.name);
		info.setType(this.type);
		info.setSystem(this.system);
		info.setOrigin(this.origin);
		info.setApplyTo(this.applyTo);
		info.setOid(this.oid);
		info.setXmlData(this.xmlData);
		info.setRegulation(this.regulation);
		info.setTransform(this.transform);
		info.setValue(this.value);

	}

	/**
	 * 对数据库的结果集的当前记录进行分析
	 * 
	 * @param rs
	 *            结果集
	 * @throws SQLException 
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void parse(ResultSet rs) throws SQLException, DocumentException, IOException{

		boolean isSystem = (rs.getInt("FLD_EDITABLE") == FieldInfo.TYPE_SYSTEM_IS) ? true : false;
		parse(rs.getInt("FLD_OID"), rs.getString("FLD_ID"), rs.getString("FLD_NAME"), rs
				.getString("FLD_TYPE"), rs.getString("FLD_APPLYTO"), isSystem, ResultSetOperation
				.clobToString(rs.getClob("FLD_CONFIGURE")));
	}

	/**
	 * 用指定的参数进行分析
	 * 
	 * @param oid
	 *            字段oid
	 * @param id
	 *            字段id
	 * @param name
	 *            字段名称
	 * @param type
	 *            字符类型
	 * @param applyto
	 *            所属模块
	 * @param configure
	 *            配置XML信息
	 * @throws DocumentException
	 */
	public void parse(int oid, String id, String name, String type, String applyto, boolean isSystem,
			String configure) throws DocumentException {
		setOid(oid);
		setId(id);
		setName(name);
		setType(type);
		setApplyTo(applyto);
		setSystem(isSystem);
		if (configure == null || configure.length() == 0)
			setXmlData("<attribute/>");
		else
			setXmlData(configure);
		parse();
	}

	/**
	 * 对XML配置数据进行分析<br>
	 * 在派生类中重载此函数
	 */
	public void parse() {
		// 在派生类中重载此函数
		if (this.xmlDoc == null)
			return;
		Element root = this.xmlDoc.getRootElement();

		Element el = (Element) root.selectSingleNode("./desc");
		if (el != null)
			this.desc = el.getText();
	}

	/**
	 * 判断字段是否在指定的模块中
	 * 
	 * @param moduleOid
	 *            模块OID
	 * @return 返回字段是否在指定的模块中
	 * @deprecated
	 */
	public boolean inModule(int moduleOid) {
		return this.applyTo.equals("" + ModuleName.ALL) || this.applyTo.equals("" + moduleOid)
				|| this.applyTo.startsWith("" + moduleOid + ",") || this.applyTo.endsWith("," + moduleOid)
				|| (this.applyTo.indexOf("," + moduleOid + ",") != -1);
	}

	/**
	 * 判断字段是否在指定的模块中
	 * 
	 * @param moduleOid
	 * @return
	 */
	public boolean inModule(int moduleOid, boolean includeAll) {
		boolean ret = false;
		if (includeAll)
			ret = this.applyTo.equals("" + ModuleName.ALL);
		ret = ret || this.applyTo.equals("" + moduleOid) || this.applyTo.startsWith("" + moduleOid + ",")
				|| this.applyTo.endsWith("," + moduleOid)
				|| (this.applyTo.indexOf("," + moduleOid + ",") != -1);

		return ret;
	}

	/**
	 * 从MAP中生成配置信息<br>
	 * 在派生类中重载此函数
	 * 
	 * @param map
	 *            前台jsp提交的参数表
	 * @return 返回配置的XML信息
	 * @throws DocumentException
	 */
	public String getXmlConfig(Map map) throws DocumentException {
		String xml = getDefaultConfigure();
		Document doc = XmlUtil.parseString(xml);
		Element root = doc.getRootElement();
		Element el = (Element) root.selectSingleNode("./desc");
		if (el == null)
			el = root.addElement("desc");
		String dsc = (String) map.get("fld_desc");
		dsc = dsc == null ? "" : dsc;
		el.setText(dsc);
		return doc.asXML();
	}

	/**
	 * 返回字段在WEB上表单的HTML字串, js语句
	 * 
	 * @param width
	 *            表单宽度
	 * 
	 * @return 返回字段在WEB上表单的HTML字串, js语句
	 */
	public String getFormCode(int width) {
		return "";
	}

	public String getFormCode(int width, HttpServletRequest req) {
		return getFormCode(width);
	}

	/**
	 * 获取设置表单值的js语句
	 * 
	 * @return 返回设置表单值的js语句
	 */
	public String getFormValue() {
		return Consts.FLD_PREFIX + getId() + ".setValue(\"" + StringUtil.escapeJavaScript(this.value)
				+ "\");";
	}

	/**
	 * 从前台提交的表单map中获取字段值
	 * 
	 * @param m
	 *            前台提交的表单map
	 * @return 返回提交的字段值
	 */
	public String getRequestValue(Map m) {
		return (String) m.get(Consts.FLD_PREFIX + getId());
	}

	/**
	 * 获取表单中的html元素id列表
	 * 
	 * @return 返回表单中的html元素id列表
	 */
	public String[] getFormFields() {
		return new String[] { Consts.FLD_PREFIX + getId() };
	}

	/**
	 * 获取HTML页面代码（仅显示用）
	 * 
	 * @param value
	 *            字段值
	 * @return 返回HTML页面代码（仅显示用）
	 */
	public String getHtmlCode(String value) {
		return StringUtil.escapeHtml(value);
	}

	/**
	 * 把clob里的表单域值转换为存储到表字段的值 例如：下拉框，clob里存储的是id，外部表字段存储的是name
	 * 
	 * @param value
	 * @return
	 */
	public String getRestoreCode(String value) {
		return getHtmlCode(value);
	}

	/**
	 * 获取字段配置的属性列表
	 * 
	 * @return 返回字段配置的属性列表List<FieldInfo>
	 */
	public List getAttributes() {
		return new ArrayList();
	}

	/**
	 * 判断输入的值是否合法
	 * 
	 * @param value
	 *            输入值
	 * @return 返回输入的值是否合法
	 */
	public boolean isValid(String value) {
		return true;
	}

	/**
	 * 设置字段是否必填
	 * 
	 * @param notNull
	 *            字段是否必填
	 */
	public void setNotNull(String notNull) {
		if (notNull != null && notNull.toLowerCase().equals("true"))
			this.notNull = true;
		else
			this.notNull = false;
	}

	/**
	 * 获取字段是否必填
	 * 
	 * @return 返回字段是否必填
	 */
	public boolean isNotNull() {
		return this.notNull;
	}

	/**
	 * 设置字段是否只读
	 * 
	 * @param readOnly
	 *            字段是否只读
	 */
	public void setReadOnly(String readOnly) {
		if (readOnly != null && readOnly.toLowerCase().equals("true"))
			this.readOnly = true;
		else
			this.readOnly = false;
	}

	/**
	 * 获取字段是否只读<br>
	 * 如果为系统字段，则默认为只读
	 * 
	 * @return 返回字段是否只读
	 */
	public boolean isReadOnly() {
//		if (this.system)
//			return true;
		return this.readOnly;
	}

	/**
	 * 获取默认的XML配置数据
	 * 
	 * @return 返回默认的XML配置数据
	 */
	public static String getDefaultConfigure() {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\"?>");
		sb.append("<attribute>");
		sb.append("</attribute>");

		return sb.toString();
	}

	/**
	 * 获取归属模块的描述
	 * 
	 * @return 返回归属模块的描述
	 */
	public String getApplyToName() {
		return ModuleName.getModuleName(this.applyTo);
	}

	/**
	 * 获取归属模块
	 * 
	 * @return 返回归属模块
	 */
	public String getApplyTo() {
		return this.applyTo;
	}

	/**
	 * 设置归属模块
	 * 
	 * @param applyTo
	 *            归属模块
	 */
	public void setApplyTo(String applyTo) {
		/* 保证湖南兼容,更新后删除 */
		if (applyTo.equals("all"))
			this.applyTo = "" + ModuleName.ALL;
		else if (applyTo.equals("chg"))
			this.applyTo = "" + ModuleName.CHANGE;
		else if (applyTo.equals("cfg"))
			this.applyTo = "" + ModuleName.CONFIGURE;
		else if (applyTo.equals("inc"))
			this.applyTo = "" + ModuleName.INCIDENT;
		else if (applyTo.equals("prb"))
			this.applyTo = "" + ModuleName.PROBLEM;
		else if (applyTo.equals("req"))
			this.applyTo = "" + ModuleName.REQUIRE;
		else
			/* 保证湖南兼容,更新后删除 end */

			this.applyTo = applyTo;
	}

	/**
	 * 获取字段id
	 * 
	 * @return 返回字段id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * 设置字段id
	 * 
	 * @param id
	 *            字段id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取字段名称
	 * 
	 * @return 返回字段名称
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 设置字段名称
	 * 
	 * @param name
	 *            字段名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取字段oid
	 * 
	 * @return 返回字段oid
	 */
	public int getOid() {
		return this.oid;
	}

	/**
	 * 设置字段oid
	 * 
	 * @param oid
	 *            字段oid
	 */
	public void setOid(int oid) {
		this.oid = oid;
	}

	/**
	 * 获取字段类型
	 * 
	 * @return 返回字段类型
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * 获取字段类型描述
	 * 
	 * @return 返回字段类型描述
	 */
	public String getTypeDesc() {
		return FieldManager.getTypeDesc(this.origin, this.type);
	}

	/**
	 * 设置字段类型
	 * 
	 * @param type
	 *            字段类型
	 */
	public void setType(String type) {
		this.type = type;
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
	public void setXmlData(String str) {
		this.xmlData = str;
		Document doc = null;
		try {
			if (this.xmlData != null && !this.xmlData.equals(""))
				doc = XmlUtil.parseString(this.xmlData);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		this.xmlDoc = doc;
	}

	/**
	 * 获取字段值
	 * 
	 * @return 返回字段值
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * 设置字段值
	 * 
	 * @param value
	 *            字段值
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * 获取字段应用到现有的html元素的id
	 * 
	 * @return 返回字段应用到现有的html元素的id
	 */
	public String getTransform() {
		return this.transform;
	}

	/**
	 * 设置字段应用到现有的html元素的id
	 * 
	 * @param tansform
	 *            字段应用到现有的html元素的id
	 */
	public void setTransform(String tansform) {
		this.transform = tansform;
	}

	/**
	 * 根据 id 查询对象的属性<br>
	 * FieldInfo 对象的属性包括：<br>
	 * id: 字段id<br>
	 * name: 字段名称<br>
	 * type: 字段类型<br>
	 * category: 字段所属模块<br>
	 * 
	 * @param id
	 *            查询标识
	 * @return 属性值，找不到时返回 null
	 */
	public String getAttribute(String id) {
		if (id == null)
			return null;
		if (id.equals("id"))
			return getId();
		if (id.equals("name"))
			return getName();
		if (id.equals("type"))
			return getTypeDesc();
		if (id.equals("category"))
			return getApplyToName();
		return null;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getRegulation() {
		return regulation;
	}

	/**
	 * 返回字段的规则,比如当值改变的时候，当焦点离开的时候，执行一个动作
	 * 
	 * @return List[String["name","value"],.....] <br>
	 *         name:动作名<br>
	 *         value:动作内容
	 */
	public List getRegulationL() {
		List ret = new ArrayList();
		if (this.regulation != null && !regulation.equals("")) {
			String[] rules = regulation.split("&__&");
			for (int i = 0; i < rules.length; i++) {
				String[] rule = new String[2];
				rule[0] = rules[i].substring(0, rules[i].indexOf("="));
				rule[1] = rules[i].substring(rules[i].indexOf("=") + 1);
				ret.add(rule);
			}
		}
		return ret;
	}

	public void setRegulation(String regulation) {
		this.regulation = regulation;
	}

	public boolean isInjectData() {
		return injectData;
	}

	public void setInjectData(boolean injectData) {
		this.injectData = injectData;
	}
}
