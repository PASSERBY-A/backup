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
 * ��ʾ�ֶ���Ϣ
 * 
 * @author ÷԰
 * 
 */
public class FieldInfo implements IObjectWithAttribute {

	/**
	 * ϵͳ�ֶ�
	 */
	public static final int TYPE_SYSTEM_IS = 0;
	/**
	 * ��ϵͳ�ֶ�
	 */
	public static final int TYPE_SYSTEM_NO = 1;

	/**
	 * oid, ��Ӧ fld_oid �ֶ�
	 */
	protected int oid;

	/**
	 * �ֶ�id, ��Ӧ fld_id �ֶ�
	 */
	protected String id;

	protected String origin = "";

	/**
	 * �洢�ֶ�ֵ
	 */
	protected String value = "";

	/**
	 * �ֶ�����, ��Ӧ fld_name �ֶ�
	 */
	protected String name;

	/**
	 * �ֶ�ʹ�÷�Χ, ��Ӧ fld_applyto �ֶ�, �μ� ModuleName.java
	 */
	protected String applyTo;

	/**
	 * �ֶ�ʹ������, ��Ӧ fld_type �ֶ�
	 * 
	 * @see FieldManager#getFieldTypes()
	 */
	protected String type;

	/**
	 * �洢XML���õ��ĵ�����
	 */
	protected Document xmlDoc = null;

	/**
	 * �洢XML���õ�����
	 */
	protected String xmlData = null;

	/**
	 * �洢�ֶ��Ƿ�ֻ��
	 */
	protected boolean readOnly = false;

	/**
	 * �洢�ֶ��Ƿ�ǿ�
	 */
	protected boolean notNull = false;

	/**
	 * �Ƿ����Ԥ��ֵ������������ĳ�ڵ㣬�Ƿ���ʾ��ʷ�ڵ�����ͬ�ֶε�ֵ��
	 */
	protected boolean injectData = true;

	/**
	 * ָ���ֶ�Ӧ�õ����е�HTMLԪ��ID,nullΪ����Ӧ��
	 */
	protected String transform = null;

	/**
	 * �洢�ֶ�����
	 */
	protected String desc = "";

	/**
	 * �洢�ֶι�������
	 */
	protected String filter = null;

	/**
	 * �洢�ֶ���ʾ��ʽ
	 */
	protected String style = null;

	/**
	 * �洢�Ƿ�Ϊϵͳ�ֶ�
	 */
	protected boolean system = false;

	/**
	 * �ֶεĹ��������ʱ����ģ����統ֵ�ı��ʱ�򣬵��н����ʱ�򡣡�����Ҫ����һ������
	 */
	protected String regulation = "";

	/**
	 * ��ȡ�Ƿ�Ϊϵͳ�ֶ�
	 * 
	 * @return �����Ƿ�Ϊϵͳ�ֶ�
	 */
	public boolean isSystem() {
		return this.system;
	}

	/**
	 * �����Ƿ�Ϊϵͳ�ֶ�
	 * 
	 * @param system
	 *            �Ƿ�Ϊϵͳ�ֶ�
	 */
	public void setSystem(boolean system) {
		this.system = system;
	}

	/**
	 * ��ָ����������������ֵ�ıȽ�
	 * 
	 * @param operationCode
	 * @param val
	 * @param regex
	 *            ����ǽ���,val2�Էֺŷָ�����ֵ��
	 * @return �ȽϽ��
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
	 * ��ȡ�ֶ�����
	 * 
	 * @return �����ֶ�����
	 */
	public String getDesc() {
		return this.desc == null ? "" : this.desc;
	}

	/**
	 * �����ֶ�����
	 * 
	 * @param desc
	 *            �ֶ�����
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * ��ȡ�ֶι�������
	 * 
	 * @return �����ֶι�������
	 */
	public String getFilter() {
		return this.filter;
	}

	/**
	 * �����ֶι�������
	 * 
	 * @param filter
	 *            �ֶι�������
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * ��ʾ�ֶ��Ƿ���ܣ����������н�������
	 * 
	 * @return �����ֶ��Ƿ����
	 */
	public boolean isEncrypt() {
		return false;
	}

	/**
	 * ��ȡ�ֶ���ʾ��ʽ
	 * 
	 * @return �����ֶ���ʾ��ʽ
	 */
	public String getStyle() {
		return this.style;
	}

	/**
	 * �����ֶ���ʾ��ʽ
	 * 
	 * @param style
	 *            �ֶ���ʾ��ʽ
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * �����ֶ��Ƿ����
	 * 
	 * @param notNull
	 *            �ֶ��Ƿ����
	 */
	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}

	/**
	 * �����ֶ��Ƿ�ֻ��
	 * 
	 * @param readOnly
	 *            �ֶ��Ƿ�ֻ��
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	/**
	 * ���ɱ������һ������
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		return new FieldInfo();
	}

	/**
	 * ��ȡ�ֶεĹ�����ϵ����
	 * 
	 * @see Consts#RELATION_CI_CI
	 * @return �����ֶεĹ�����ϵ����
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
	 * ��ָ���Ĳ������ɱ�����ĸ���
	 * 
	 * @param readOnly
	 *            �Ƿ�ֻ��
	 * @param notNull
	 *            �Ƿ����
	 * @param filter
	 *            ��������
	 * @param style
	 *            ��ʾ��ʽ
	 * @return ���ɱ�����ĸ���
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
	 * �������Ե�ָ���Ķ�����, �������������ش˺���
	 * 
	 * @param info
	 *            ָ���Ķ���
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
	 * �����ݿ�Ľ�����ĵ�ǰ��¼���з���
	 * 
	 * @param rs
	 *            �����
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
	 * ��ָ���Ĳ������з���
	 * 
	 * @param oid
	 *            �ֶ�oid
	 * @param id
	 *            �ֶ�id
	 * @param name
	 *            �ֶ�����
	 * @param type
	 *            �ַ�����
	 * @param applyto
	 *            ����ģ��
	 * @param configure
	 *            ����XML��Ϣ
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
	 * ��XML�������ݽ��з���<br>
	 * �������������ش˺���
	 */
	public void parse() {
		// �������������ش˺���
		if (this.xmlDoc == null)
			return;
		Element root = this.xmlDoc.getRootElement();

		Element el = (Element) root.selectSingleNode("./desc");
		if (el != null)
			this.desc = el.getText();
	}

	/**
	 * �ж��ֶ��Ƿ���ָ����ģ����
	 * 
	 * @param moduleOid
	 *            ģ��OID
	 * @return �����ֶ��Ƿ���ָ����ģ����
	 * @deprecated
	 */
	public boolean inModule(int moduleOid) {
		return this.applyTo.equals("" + ModuleName.ALL) || this.applyTo.equals("" + moduleOid)
				|| this.applyTo.startsWith("" + moduleOid + ",") || this.applyTo.endsWith("," + moduleOid)
				|| (this.applyTo.indexOf("," + moduleOid + ",") != -1);
	}

	/**
	 * �ж��ֶ��Ƿ���ָ����ģ����
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
	 * ��MAP������������Ϣ<br>
	 * �������������ش˺���
	 * 
	 * @param map
	 *            ǰ̨jsp�ύ�Ĳ�����
	 * @return �������õ�XML��Ϣ
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
	 * �����ֶ���WEB�ϱ���HTML�ִ�, js���
	 * 
	 * @param width
	 *            �����
	 * 
	 * @return �����ֶ���WEB�ϱ���HTML�ִ�, js���
	 */
	public String getFormCode(int width) {
		return "";
	}

	public String getFormCode(int width, HttpServletRequest req) {
		return getFormCode(width);
	}

	/**
	 * ��ȡ���ñ�ֵ��js���
	 * 
	 * @return �������ñ�ֵ��js���
	 */
	public String getFormValue() {
		return Consts.FLD_PREFIX + getId() + ".setValue(\"" + StringUtil.escapeJavaScript(this.value)
				+ "\");";
	}

	/**
	 * ��ǰ̨�ύ�ı�map�л�ȡ�ֶ�ֵ
	 * 
	 * @param m
	 *            ǰ̨�ύ�ı�map
	 * @return �����ύ���ֶ�ֵ
	 */
	public String getRequestValue(Map m) {
		return (String) m.get(Consts.FLD_PREFIX + getId());
	}

	/**
	 * ��ȡ���е�htmlԪ��id�б�
	 * 
	 * @return ���ر��е�htmlԪ��id�б�
	 */
	public String[] getFormFields() {
		return new String[] { Consts.FLD_PREFIX + getId() };
	}

	/**
	 * ��ȡHTMLҳ����루����ʾ�ã�
	 * 
	 * @param value
	 *            �ֶ�ֵ
	 * @return ����HTMLҳ����루����ʾ�ã�
	 */
	public String getHtmlCode(String value) {
		return StringUtil.escapeHtml(value);
	}

	/**
	 * ��clob��ı���ֵת��Ϊ�洢�����ֶε�ֵ ���磺������clob��洢����id���ⲿ���ֶδ洢����name
	 * 
	 * @param value
	 * @return
	 */
	public String getRestoreCode(String value) {
		return getHtmlCode(value);
	}

	/**
	 * ��ȡ�ֶ����õ������б�
	 * 
	 * @return �����ֶ����õ������б�List<FieldInfo>
	 */
	public List getAttributes() {
		return new ArrayList();
	}

	/**
	 * �ж������ֵ�Ƿ�Ϸ�
	 * 
	 * @param value
	 *            ����ֵ
	 * @return ���������ֵ�Ƿ�Ϸ�
	 */
	public boolean isValid(String value) {
		return true;
	}

	/**
	 * �����ֶ��Ƿ����
	 * 
	 * @param notNull
	 *            �ֶ��Ƿ����
	 */
	public void setNotNull(String notNull) {
		if (notNull != null && notNull.toLowerCase().equals("true"))
			this.notNull = true;
		else
			this.notNull = false;
	}

	/**
	 * ��ȡ�ֶ��Ƿ����
	 * 
	 * @return �����ֶ��Ƿ����
	 */
	public boolean isNotNull() {
		return this.notNull;
	}

	/**
	 * �����ֶ��Ƿ�ֻ��
	 * 
	 * @param readOnly
	 *            �ֶ��Ƿ�ֻ��
	 */
	public void setReadOnly(String readOnly) {
		if (readOnly != null && readOnly.toLowerCase().equals("true"))
			this.readOnly = true;
		else
			this.readOnly = false;
	}

	/**
	 * ��ȡ�ֶ��Ƿ�ֻ��<br>
	 * ���Ϊϵͳ�ֶΣ���Ĭ��Ϊֻ��
	 * 
	 * @return �����ֶ��Ƿ�ֻ��
	 */
	public boolean isReadOnly() {
//		if (this.system)
//			return true;
		return this.readOnly;
	}

	/**
	 * ��ȡĬ�ϵ�XML��������
	 * 
	 * @return ����Ĭ�ϵ�XML��������
	 */
	public static String getDefaultConfigure() {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\"?>");
		sb.append("<attribute>");
		sb.append("</attribute>");

		return sb.toString();
	}

	/**
	 * ��ȡ����ģ�������
	 * 
	 * @return ���ع���ģ�������
	 */
	public String getApplyToName() {
		return ModuleName.getModuleName(this.applyTo);
	}

	/**
	 * ��ȡ����ģ��
	 * 
	 * @return ���ع���ģ��
	 */
	public String getApplyTo() {
		return this.applyTo;
	}

	/**
	 * ���ù���ģ��
	 * 
	 * @param applyTo
	 *            ����ģ��
	 */
	public void setApplyTo(String applyTo) {
		/* ��֤���ϼ���,���º�ɾ�� */
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
			/* ��֤���ϼ���,���º�ɾ�� end */

			this.applyTo = applyTo;
	}

	/**
	 * ��ȡ�ֶ�id
	 * 
	 * @return �����ֶ�id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * �����ֶ�id
	 * 
	 * @param id
	 *            �ֶ�id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * ��ȡ�ֶ�����
	 * 
	 * @return �����ֶ�����
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * �����ֶ�����
	 * 
	 * @param name
	 *            �ֶ�����
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * ��ȡ�ֶ�oid
	 * 
	 * @return �����ֶ�oid
	 */
	public int getOid() {
		return this.oid;
	}

	/**
	 * �����ֶ�oid
	 * 
	 * @param oid
	 *            �ֶ�oid
	 */
	public void setOid(int oid) {
		this.oid = oid;
	}

	/**
	 * ��ȡ�ֶ�����
	 * 
	 * @return �����ֶ�����
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * ��ȡ�ֶ���������
	 * 
	 * @return �����ֶ���������
	 */
	public String getTypeDesc() {
		return FieldManager.getTypeDesc(this.origin, this.type);
	}

	/**
	 * �����ֶ�����
	 * 
	 * @param type
	 *            �ֶ�����
	 */
	public void setType(String type) {
		this.type = type;
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
	 * ��ȡ�ֶ�ֵ
	 * 
	 * @return �����ֶ�ֵ
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * �����ֶ�ֵ
	 * 
	 * @param value
	 *            �ֶ�ֵ
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * ��ȡ�ֶ�Ӧ�õ����е�htmlԪ�ص�id
	 * 
	 * @return �����ֶ�Ӧ�õ����е�htmlԪ�ص�id
	 */
	public String getTransform() {
		return this.transform;
	}

	/**
	 * �����ֶ�Ӧ�õ����е�htmlԪ�ص�id
	 * 
	 * @param tansform
	 *            �ֶ�Ӧ�õ����е�htmlԪ�ص�id
	 */
	public void setTransform(String tansform) {
		this.transform = tansform;
	}

	/**
	 * ���� id ��ѯ���������<br>
	 * FieldInfo ��������԰�����<br>
	 * id: �ֶ�id<br>
	 * name: �ֶ�����<br>
	 * type: �ֶ�����<br>
	 * category: �ֶ�����ģ��<br>
	 * 
	 * @param id
	 *            ��ѯ��ʶ
	 * @return ����ֵ���Ҳ���ʱ���� null
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
	 * �����ֶεĹ���,���統ֵ�ı��ʱ�򣬵������뿪��ʱ��ִ��һ������
	 * 
	 * @return List[String["name","value"],.....] <br>
	 *         name:������<br>
	 *         value:��������
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
