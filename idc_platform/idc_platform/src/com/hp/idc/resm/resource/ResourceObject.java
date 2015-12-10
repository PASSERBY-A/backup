package com.hp.idc.resm.resource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import oracle.sql.CLOB;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.hp.idc.resm.cache.CacheException;
import com.hp.idc.resm.cache.CacheableObject;
import com.hp.idc.resm.model.AttributeDefine;
import com.hp.idc.resm.model.Model;
import com.hp.idc.resm.resource.rule.BasicRule;
import com.hp.idc.resm.resource.rule.IResourceRule;
import com.hp.idc.resm.service.ServiceManager;
import com.hp.idc.resm.util.DateTimeUtil;
import com.hp.idc.resm.util.DbUtil;
import com.hp.idc.resm.util.XmlUtil;

/**
 * ��Դ����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ResourceObject extends CacheableObject {

	/**
	 * log4j��־
	 */
	private static Logger logger = Logger.getLogger(ResourceObject.class);

	/**
	 * ���л�UID
	 */
	private static final long serialVersionUID = -1121652367754998440L;

	/**
	 * ��Դ����id
	 */
	protected int id = -1;

	/**
	 * ��Դ���������
	 */
	protected String modelId;

	/**
	 * �Ƿ�ʹ��
	 */
	protected boolean enabled;

	/**
	 * ��Դ�������ݵ�XML�ĵ����ڷ�����������ʱʵ������
	 * 
	 * @see #data
	 * @see #parseData()
	 */
	protected Document doc = null;

	/**
	 * ��Դ��������ԡ��ڷ�����������ʱʵ������
	 * 
	 * @see #data
	 * @see #parseData()
	 */
	protected Map<String, AttributeBase> attributes = null;

	/**
	 * ����
	 */
	protected String data;

	/**
	 * ��Դչʾʱ�ı�ͷ��Ϣ�����л�ʱʹ��
	 */
	protected List<AttributeBase> header;

	/**
	 * ��������Ϣ
	 */
	protected String simpleDescription;
	
	/**
	 * ���л�ʹ��, ����ֵ
	 */
	protected String name;

	/**
	 * ����id��ȡ��Դ���������
	 * 
	 * @param attrId
	 *            ����id
	 * @return ��Դ���������
	 */
	public AttributeBase getAttribute(String attrId) {
		AttributeBase ret = null;
		if (this.attributes != null)
			ret = this.attributes.get(attrId);
		if (ret == null) {
			if (this.getModel().hasAttribute(attrId)) {
				ret = createAttribute(attrId);
			}
		}
		return ret;
	}

	/**
	 * ��Դ��������
	 * 
	 * @return ��Դ��������
	 * @see Model#TYPE_OTHER
	 * @see Model#TYPE_PHYSICS
	 * @see Model#TYPE_BUSSINESS
	 */
	public int getResourceType() {
		return this.getModel().getResourceType();
	}

	/**
	 * ��ȡ��Դ�������������
	 * 
	 * @return ��Դ�������������
	 */
	public List<AttributeBase> getAllAttributes() {
		List<AttributeBase> l = new ArrayList<AttributeBase>();
		l.addAll(this.attributes.values());
		return l;
	}

	/**
	 * ����id��ȡ��Դ���������ֵ
	 * 
	 * @param attrId
	 *            ����id
	 * @return ��Դ���������ֵ
	 */
	public String getAttributeValue(String attrId) {
		AttributeBase b = getAttribute(attrId);
		if (b == null)
			return null;
		return b.getText();
	}

	/**
	 * ��ȡ��Դ����
	 * 
	 * @return ��Դ����
	 */
	public String getName() {
		return getAttributeValue("name");
	}

	public void setName(String name) {
		//
	}

	/**
	 * @return ��Դ����id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * ������Դ����id
	 * 
	 * @param id
	 *            ��Դ����id
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setId(int id) throws CacheException {
		checkSet();
		this.id = id;
	}

	/**
	 * @return ��Դ�����ģ��id
	 * @see #modelId
	 */
	public String getModelId() {
		return this.modelId;
	}

	/**
	 * ������Դ�����ģ��id
	 * 
	 * @param modelId
	 *            ��Դ�����ģ��id
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 * @see #modelId
	 */
	public void setModelId(String modelId) throws CacheException {
		checkSet();
		this.modelId = modelId;
	}

	/**
	 * ָʾ��Դ�����Ƿ���ʹ����
	 * 
	 * @return trueΪʹ���У�falseΪ��ɾ��
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * ������Դ�����Ƿ���ʹ����
	 * 
	 * @param enabled
	 *            trueΪʹ���У�falseΪ��ɾ��
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setEnabled(boolean enabled) throws CacheException {
		checkSet();
		this.enabled = enabled;
	}

	/**
	 * ��Դ��������ݡ���ʽΪxml��ʽ�����������ĵ���
	 * 
	 * @return ��Դ���������
	 */
	public String getData() {
		return this.data;
	}

	/**
	 * ������Դ���������
	 * 
	 * @param data
	 *            ��Դ���������
	 * @throws CacheException
	 *             �����ѱ�����ʱ����
	 */
	public void setData(String data) throws CacheException {
		if (this.id == -1) {
			return;
		}
		checkSet();
		this.data = data;
		this.parseData();
	}

	@Override
	public String getPrimaryKey() {
		return "" + this.id;
	}

	@Override
	public String getDatabaseTable() {
		return "resm_item";
	}

	@Override
	public String getPrimaryKeyField() {
		return "id";
	}

	@Override
	public void updateResultSet(Connection conn, ResultSet rs) throws Exception {
		rs.updateInt("id", this.id);
		rs.updateString("modelid", this.modelId);
		rs.updateInt("enabled", this.enabled ? 1 : 0);
		// rs.updateString("data", this.data);

		CLOB clob = DbUtil.createTemporaryCLOB(conn, true,
				oracle.sql.CLOB.DURATION_SESSION);
		clob.setString(1, this.data);
		rs.updateClob("data", clob);
	}

	@Override
	public void readFromResultSet(ResultSet rs) throws Exception {
		this.id = rs.getInt("id");
		this.modelId = rs.getString("modelid");
		this.enabled = rs.getInt("enabled") == 1;
		// setData(rs.getString("data"));
		setData(DbUtil.clobToString(rs.getClob("data")));
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		ResourceObject obj = (ResourceObject) super.clone();
		obj.parseData();
		return obj;
	}

	/**
	 * ������Դ���������
	 * 
	 * @see #data
	 */
	@SuppressWarnings("unchecked")
	protected void parseData() {
		if (this.data == null || this.data.length() == 0)
			this.data = "<item/>";
		try {
			this.doc = XmlUtil.parseString(this.data);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		this.attributes = new HashMap<String, AttributeBase>();
		List<Element> fields = this.doc.getRootElement().selectNodes(
				"data/field");
		for (int i = 0; i < fields.size(); i++) {
			Element el = fields.get(i);
			String attrId = el.attributeValue("id");
			AttributeBase ab = getAttributeFromElement(el);
			if (ab == null) // ����ʧ��
				continue;
			if (this.attributes.get(ab.getAttribute().getAttrId()) == null) { // ������Ի���
				this.attributes.put(attrId, ab);
			}
		}
		AttributeBase idAttr = this.createAttribute("id");
		try {
			idAttr.setText("" + this.id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.attributes.put("id", idAttr);
	}

	/**
	 * ��xml�ڵ㴴����Դ����
	 * 
	 * @param el
	 *            xml�ڵ�
	 * @return ��Դ���Զ���
	 */
	protected AttributeBase getAttributeFromElement(Element el) {
		String attrId = el.attributeValue("id");
		AttributeBase ab = createAttribute(attrId);
		if (ab == null)
			return null;
		try {
			ab.deserialize(el.attributeValue("value"));
		} catch (Exception e) {
			logger.error("��ȡ��Դ����ʱ�����쳣��" + this.getId() + "/" + attrId);
			return null;
		}
		return ab;
	}

	/**
	 * ��ȡ��Դ���Ե���һ��ֵ���ڸ�����Դʱ�����ڹ�����ж�
	 * 
	 * @param attrId
	 *            ��Դ����id
	 * @return ��һ��ֵ���Ҳ���ʱ����null
	 */
	public String getOldAttributeValue(String attrId) {
		@SuppressWarnings("unchecked")
		List<Element> fields = this.doc.getRootElement().selectNodes("data");
		for (int i = 1; i < fields.size(); i++) {
			Element e0 = (Element) fields.get(i).selectSingleNode(
					"field[@id='" + attrId + "']");
			if (e0 != null) {
				AttributeBase ab = getAttributeFromElement(e0);
				if (ab != null)
					return ab.getText();
			}
		}
		return null;
	}

	/**
	 * ����һ����Դ���Զ���
	 * 
	 * @param attrId
	 *            ��Դ����id
	 * @return �µ���Դ���Զ���
	 */
	public AttributeBase createAttribute(String attrId) {
		AttributeDefine ad = ServiceManager.getAttributeService()
				.getAttributeById(attrId);
		if (ad == null) {
			logger.error("��Դ" + this.id + " createAttribute ʧ�ܣ�|"
					+ this.modelId + "/" + attrId + "|AttributeDefine������");
			return null;
		}
		AttributeBase ab = ad.createInstance(this.modelId);
		if(ab == null)
			logger.error("��Դ" + this.id + " createAttribute ʧ�ܣ�|"
					+ this.modelId + "/" + attrId + "|");
		if (ab instanceof ExpressionAttribute) {
			((ExpressionAttribute) ab).setResId(this.id);
		}
		return ab;
	}

	@Override
	public void onDeserialize() {
		parseData();
	}

	/**
	 * ������Դ�������ݣ���Ҫֱ�ӵ��ô˺���
	 * 
	 * @param el
	 *            xml�ڵ�
	 * @param attr
	 *            ����
	 */
	public void update(Element el, AttributeBase attr) {
		Element e0 = (Element) el.selectSingleNode("field[@id='"
				+ attr.getAttribute().getAttrId() + "']");
		if (e0 == null) {
			e0 = el.addElement("field");
			e0.addAttribute("id", attr.getAttribute().getAttrId());
		}
		e0.addAttribute("value", attr.serialize());
		this.attributes.put(attr.getAttribute().getAttrId(), attr);
	}

	/**
	 * ������Դ�������ݣ���Ҫֱ�ӵ��ô˺���
	 * 
	 * @param el
	 *            xml�ڵ�
	 * @param attrId
	 *            ����id
	 * @param attrValue
	 *            ����ֵ
	 * @throws Exception
	 *             ������Ч������ֵʱ����
	 */
	public void update(Element el, String attrId, String attrValue)
			throws Exception {
		AttributeBase attr = createAttribute(attrId);
		attr.setText(attrValue);
		update(el, attr);
	}

	/**
	 * ������Դ��������
	 * 
	 * @param list
	 *            �µ������б�
	 * @param userId
	 *            ���µ��û���
	 * @return trueΪ���³ɹ���falseΪ�ޱ仯
	 * @throws Exception
	 *             ���³��ֵ��쳣
	 */
	public boolean update(List<AttributeBase> list, int userId)
			throws Exception {
		return update(list, userId, true);
	}

	/**
	 * ������Դ��������
	 * 
	 * @param list
	 *            �µ������б�
	 * @param userId
	 *            ���µ��û���
	 * @param checkIgnoredAttributes
	 *            �Ƿ���������Ĳ�������
	 * @return trueΪ���³ɹ���falseΪ�ޱ仯
	 * @throws Exception
	 *             ���³��ֵ��쳣
	 */
	@SuppressWarnings("unchecked")
	protected boolean update(List<AttributeBase> list, int userId,
			boolean checkIgnoredAttributes) throws Exception {
		Model model = ServiceManager.getModelService().getModelById(
				this.modelId);
		if (model == null)
			throw new Exception("�Ƿ���ģ�͡�");
		Element el = null;
		List<AttributeBase> updateList = new ArrayList<AttributeBase>();
		for (int i = 0; i < list.size(); i++) {
			AttributeBase a1 = list.get(i);
			if (!model.hasAttribute(a1.getAttribute().getAttrId())) {
				logger.error("������Դʱ����δ��������ԣ�" + a1.getAttribute().getAttrId());
				continue;
			}

			// ���Բ������ԣ�ϵͳ�Զ����ɣ�����ǰ̨���ƣ�
			boolean isIgnored = false;
			if (checkIgnoredAttributes) {
				for (String ignor : BasicRule.IGNORED_ATTRIBUTES) {
					if (ignor.equals(a1.getAttribute().getAttrId())) {
						isIgnored = true;
						logger.debug("���Դ���Ĳ���" + ignor + "��" + a1.getText());
						break;
					}
				}
				// ���ʽ�͵��ֶβ�����
				if (a1 instanceof ExpressionAttribute)
					isIgnored = true;
			}
			if (isIgnored)
				continue;

			AttributeBase a2 = this.attributes.get(a1.getAttribute()
					.getAttrId());
			if (a2 == null || !a1.equals(a2)) {
				if (el == null) {
					el = DocumentHelper.createElement("data");
					this.doc.getRootElement().elements().add(0, el);
				}
				updateList.add(a1);
				update(el, a1);
			}
		}
		
		IResourceRule rule = model.getRule();
		// Ӧ����Դ���µĹ���
		if (updateList.size() > 0) {
			rule.update(this, updateList, el, userId);
		}
		rule.valid(this);

		if (el != null) {
			this.data = XmlUtil.getPrettyXmlString(this.doc);
			return true;
		}
		return false;
	}

	@Override
	public void dump() {
		System.out.println("��Դ(" + this.id + ")����Ϣ:");
		System.out.println("modelId = " + this.modelId);
		System.out.println("enabled = " + this.enabled);
		Iterator<AttributeBase> it = this.attributes.values().iterator();
		while (it.hasNext()) {
			AttributeBase ab = it.next();
			System.out.println("attribute(" + ab.getAttribute().getAttrId()
					+ ") = " + ab.getValueAsObject());
		}
		System.out.println("data = " + this.data);
	}

	/**
	 * ��ȡ��Դ����Ĺ�����ϵ
	 * 
	 * @return ��Դ����Ĺ�����ϵ�б�
	 */
	public List<ResourceRelation> getRelations() {
		return ServiceManager.getRelationService().getRelationsByResourceId(
				getId());
	}

	/**
	 * ��ȡ�����Ķ���
	 * 
	 * @return �����Ķ����б�
	 */
	public List<ResourceObject> getRelationObjects() {
		List<ResourceRelation> list = getRelations();
		List<ResourceObject> ret = new ArrayList<ResourceObject>();
		for (ResourceRelation rr : list) {
			int rid = rr.getItemId();
			if (rid == this.getId())
				rid = rr.getItemId2();
			ResourceObject obj = ServiceManager.getResourceService()
					.getResourceById(rid);
			if (obj != null)
				ret.add(obj);
		}
		return ret;
	}

	/**
	 * ��ȡ�����Ķ���
	 * 
	 * @param relationId
	 *            ������ϵ����
	 * @return �����Ķ����б�
	 */
	public List<ResourceObject> getRelationObjects(String relationId) {
		List<ResourceRelation> list = getRelations();
		List<ResourceObject> ret = new ArrayList<ResourceObject>();
		for (ResourceRelation rr : list) {
			if (!relationId.equals(rr.getRelationId()))
				continue;
			int rid = rr.getItemId();
			if (rid == this.getId())
				rid = rr.getItemId2();
			ResourceObject obj = ServiceManager.getResourceService()
					.getResourceById(rid);
			if (obj != null)
				ret.add(obj);
		}
		return ret;
	}

	/**
	 * ��ȡ�����Ķ���
	 * 
	 * @param relationId
	 *            ������ϵ����
	 * @param modelId1
	 *            ģ��id
	 * @return �����Ķ����б�
	 */
	public List<ResourceObject> getRelationObjects(String relationId,
			String modelId1) {
		List<ResourceRelation> list = getRelations();
		List<ResourceObject> ret = new ArrayList<ResourceObject>();
		for (ResourceRelation rr : list) {
			if (!relationId.equals(rr.getRelationId()))
				continue;
			int rid = rr.getItemId();
			if (rid == this.getId())
				rid = rr.getItemId2();
			ResourceObject obj = ServiceManager.getResourceService()
					.getResourceById(rid);
			if (obj != null && obj.getModelId().equals(modelId1))
				ret.add(obj);
		}
		return ret;
	}

	/**
	 * ��ȡ����ģ�Ͷ���
	 * 
	 * @return ����ģ�Ͷ���
	 */
	public Model getModel() {
		return ServiceManager.getModelService().getModelById(this.modelId);
	}

	/**
	 * �ڲ���������ȡ��Դչʾʱ�ı�ͷ��Ϣ
	 * 
	 * @param headIds
	 *            ����id�б�
	 * @return ��ͷ������������Ϣ
	 */
	protected List<AttributeBase> getHeader(String[] headIds) {
		List<AttributeBase> list = new ArrayList<AttributeBase>();
		for (String headId : headIds) {
			AttributeBase attr = this.getAttribute(headId);
			if (attr != null) {
				list.add(attr);
			}
		}
		return list;
	}

	/**
	 * ��ȡ��Դչʾʱ�ı�ͷ��Ϣ����������������
	 * 
	 * @return ��ͷ������������Ϣ
	 */
	public List<AttributeBase> getHeader() {
		// return getHeader(new String[] { "id", "name", "important",
		// "description", "resource_admin", "status" });
		return getHeader(new String[] { "id", "name", "customer_id",
				"order_id", "status", "description", "last_update_by",
				"last_update_time" });
	}

	/**
	 * ������Դչʾʱ�ı�ͷ��Ϣ���˺�������Ч����flex���л�ʱʹ��
	 * 
	 * @param header
	 *            ��Դչʾʱ�ı�ͷ��Ϣ
	 */
	public void setHeader(List<AttributeBase> header) {
		// �˴������κ���
	}

	/**
	 * ��ȡ��������Ϣ
	 * 
	 * @return ��������Ϣ
	 */
	public String getSimpleDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append("ID: ").append(this.getAttributeValue("id"));
		if (!this.enabled) {
			sb.append("(��ɾ��)");
		}
		sb.append("\n����: ").append(this.getAttributeValue("name"));
		sb.append("\n����: ").append(this.getAttributeValue("description")).append(
				"\n");
		return sb.toString();
	}

	/**
	 * ���ü�������Ϣ���˺�������Ч����flex���л�ʱʹ��
	 * 
	 * @param simpleDescription
	 *            ��������Ϣ
	 */
	public void setSimpleDescription(String simpleDescription) {
		// �˴������κ���
	}

	/**
	 * ����Դ��������ַ�����ģ���Ƚ�
	 * 
	 * @param str
	 *            �Ƚ�����
	 * @return true=ƥ�䣬false=��ƥ��
	 */
	public boolean isMatch(String str) {
		// TODO �˴����߼���Ҫ����, �Է��ϸ������������. ����, ���ԶԲ������н���, ������������
		if (str.equals(this.getAttributeValue("id")))
			return true;
		if (this.getAttributeValue("name").indexOf(str) != -1)
			return true;
		if (this.getAttributeValue("status").indexOf(str) != -1)
			return true;
		if (this.getAttributeValue("customer_id")!=null&&this.getAttributeValue("customer_id").indexOf(str) != -1)
			return true;
		if (this.getAttributeValue("order_id")!=null&&this.getAttributeValue("order_id").indexOf(str) != -1)
			return true;
		//if (this.getAttributeValue("description")!=null&&this.getAttributeValue("description").indexOf(str) != -1)
			//return true;
		if (this.getAttributeValue("last_update_by")!=null&&this.getAttributeValue("last_update_by").indexOf(str) != -1)
			return true;
		return false;
	}

	/**
	 * ƥ��ָ������, ָ��������ֵ.ָ���������isMatch(String str)
	 * @param strArr ����ֵ
	 * @return true=ƥ�䣬false=��ƥ��
	 */
	public boolean isMatch(String[] strArr){
		if(strArr == null || strArr.length == 0)
			return false;
		for(String str:strArr){
			str=str.trim();
			if(!"".equals(str)){
				boolean match=isMatch(str);
				if(match) return match;
			}
		}
		return false;
	}
	
	public boolean isMatch(Map<String,Object> paramMap){
		boolean ret = true;
		for(String key:paramMap.keySet()){
			String str=(String)paramMap.get(key);
			if(str!=null&&!"".equals(str)){
				boolean ret0=false;
				if(this.getAttributeValue(key)!=null&&this.getAttributeValue(key).indexOf(str) != -1)
					ret0=true;
				ret=ret&&ret0;
			}
		}
		return ret;
	}
	/**
	 * ��ȡģ��ƥ�������˵��
	 * 
	 * @return ģ��ƥ�������˵��
	 */
	public String getMatchDescription() {
		return "ID��ͬ��������,��״̬������������";
	}
	
	/**
	 * ������ָ�����������Ƿ�������
	 * @param period ����
	 * @return �Ƿ�������
	 */
	public boolean isChange(Object period){
		String lastupdate = this.getAttributeValue("last_update_time");
		try {
			long _l = DateTimeUtil.parseDate(lastupdate, null).getTime();
			//������ʱ��������ʱ��� >= ��ǰʱ��, ��˵�����ڼ��������ķ����˸���
			if(Integer.valueOf(period.toString())*1000*60*60*24 + _l <= System.currentTimeMillis())
				return true;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
}
