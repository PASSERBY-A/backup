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
 * 资源对象
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ResourceObject extends CacheableObject {

	/**
	 * log4j日志
	 */
	private static Logger logger = Logger.getLogger(ResourceObject.class);

	/**
	 * 序列化UID
	 */
	private static final long serialVersionUID = -1121652367754998440L;

	/**
	 * 资源对象id
	 */
	protected int id = -1;

	/**
	 * 资源对象的类型
	 */
	protected String modelId;

	/**
	 * 是否使用
	 */
	protected boolean enabled;

	/**
	 * 资源对象数据的XML文档。在分析对象数据时实例化。
	 * 
	 * @see #data
	 * @see #parseData()
	 */
	protected Document doc = null;

	/**
	 * 资源对象的属性。在分析对象数据时实例化。
	 * 
	 * @see #data
	 * @see #parseData()
	 */
	protected Map<String, AttributeBase> attributes = null;

	/**
	 * 数据
	 */
	protected String data;

	/**
	 * 资源展示时的表头信息，序列化时使用
	 */
	protected List<AttributeBase> header;

	/**
	 * 简单描述信息
	 */
	protected String simpleDescription;
	
	/**
	 * 序列化使用, 不设值
	 */
	protected String name;

	/**
	 * 根据id获取资源对象的属性
	 * 
	 * @param attrId
	 *            属性id
	 * @return 资源对象的属性
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
	 * 资源对象类型
	 * 
	 * @return 资源对象类型
	 * @see Model#TYPE_OTHER
	 * @see Model#TYPE_PHYSICS
	 * @see Model#TYPE_BUSSINESS
	 */
	public int getResourceType() {
		return this.getModel().getResourceType();
	}

	/**
	 * 获取资源对象的所有属性
	 * 
	 * @return 资源对象的所有属性
	 */
	public List<AttributeBase> getAllAttributes() {
		List<AttributeBase> l = new ArrayList<AttributeBase>();
		l.addAll(this.attributes.values());
		return l;
	}

	/**
	 * 根据id获取资源对象的属性值
	 * 
	 * @param attrId
	 *            属性id
	 * @return 资源对象的属性值
	 */
	public String getAttributeValue(String attrId) {
		AttributeBase b = getAttribute(attrId);
		if (b == null)
			return null;
		return b.getText();
	}

	/**
	 * 获取资源名称
	 * 
	 * @return 资源名称
	 */
	public String getName() {
		return getAttributeValue("name");
	}

	public void setName(String name) {
		//
	}

	/**
	 * @return 资源对象id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * 设置资源对象id
	 * 
	 * @param id
	 *            资源对象id
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setId(int id) throws CacheException {
		checkSet();
		this.id = id;
	}

	/**
	 * @return 资源对象的模型id
	 * @see #modelId
	 */
	public String getModelId() {
		return this.modelId;
	}

	/**
	 * 设置资源对象的模型id
	 * 
	 * @param modelId
	 *            资源对象的模型id
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #modelId
	 */
	public void setModelId(String modelId) throws CacheException {
		checkSet();
		this.modelId = modelId;
	}

	/**
	 * 指示资源对象是否在使用中
	 * 
	 * @return true为使用中，false为已删除
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * 设置资源对象是否在使用中
	 * 
	 * @param enabled
	 *            true为使用中，false为已删除
	 * @throws CacheException
	 *             对象已被缓存时发生
	 */
	public void setEnabled(boolean enabled) throws CacheException {
		checkSet();
		this.enabled = enabled;
	}

	/**
	 * 资源对象的数据。格式为xml格式，请参阅设计文档。
	 * 
	 * @return 资源对象的数据
	 */
	public String getData() {
		return this.data;
	}

	/**
	 * 设置资源对象的数据
	 * 
	 * @param data
	 *            资源对象的数据
	 * @throws CacheException
	 *             对象已被缓存时发生
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
	 * 解析资源对象的数据
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
			if (ab == null) // 创建失败
				continue;
			if (this.attributes.get(ab.getAttribute().getAttrId()) == null) { // 添加属性缓存
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
	 * 从xml节点创建资源属性
	 * 
	 * @param el
	 *            xml节点
	 * @return 资源属性对象
	 */
	protected AttributeBase getAttributeFromElement(Element el) {
		String attrId = el.attributeValue("id");
		AttributeBase ab = createAttribute(attrId);
		if (ab == null)
			return null;
		try {
			ab.deserialize(el.attributeValue("value"));
		} catch (Exception e) {
			logger.error("读取资源属性时发生异常：" + this.getId() + "/" + attrId);
			return null;
		}
		return ab;
	}

	/**
	 * 获取资源属性的上一个值，在更新资源时，用于规则的判断
	 * 
	 * @param attrId
	 *            资源属性id
	 * @return 上一个值，找不到时返回null
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
	 * 创建一个资源属性对象
	 * 
	 * @param attrId
	 *            资源属性id
	 * @return 新的资源属性对象
	 */
	public AttributeBase createAttribute(String attrId) {
		AttributeDefine ad = ServiceManager.getAttributeService()
				.getAttributeById(attrId);
		if (ad == null) {
			logger.error("资源" + this.id + " createAttribute 失败：|"
					+ this.modelId + "/" + attrId + "|AttributeDefine不存在");
			return null;
		}
		AttributeBase ab = ad.createInstance(this.modelId);
		if(ab == null)
			logger.error("资源" + this.id + " createAttribute 失败：|"
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
	 * 更新资源对象内容，不要直接调用此函数
	 * 
	 * @param el
	 *            xml节点
	 * @param attr
	 *            属性
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
	 * 更新资源对象内容，不要直接调用此函数
	 * 
	 * @param el
	 *            xml节点
	 * @param attrId
	 *            属性id
	 * @param attrValue
	 *            属性值
	 * @throws Exception
	 *             设置无效的属性值时发生
	 */
	public void update(Element el, String attrId, String attrValue)
			throws Exception {
		AttributeBase attr = createAttribute(attrId);
		attr.setText(attrValue);
		update(el, attr);
	}

	/**
	 * 更新资源对象内容
	 * 
	 * @param list
	 *            新的属性列表
	 * @param userId
	 *            更新的用户名
	 * @return true为更新成功，false为无变化
	 * @throws Exception
	 *             更新出现的异常
	 */
	public boolean update(List<AttributeBase> list, int userId)
			throws Exception {
		return update(list, userId, true);
	}

	/**
	 * 更新资源对象内容
	 * 
	 * @param list
	 *            新的属性列表
	 * @param userId
	 *            更新的用户名
	 * @param checkIgnoredAttributes
	 *            是否跳过定义的部分属性
	 * @return true为更新成功，false为无变化
	 * @throws Exception
	 *             更新出现的异常
	 */
	@SuppressWarnings("unchecked")
	protected boolean update(List<AttributeBase> list, int userId,
			boolean checkIgnoredAttributes) throws Exception {
		Model model = ServiceManager.getModelService().getModelById(
				this.modelId);
		if (model == null)
			throw new Exception("非法的模型。");
		Element el = null;
		List<AttributeBase> updateList = new ArrayList<AttributeBase>();
		for (int i = 0; i < list.size(); i++) {
			AttributeBase a1 = list.get(i);
			if (!model.hasAttribute(a1.getAttribute().getAttrId())) {
				logger.error("更新资源时出现未定义的属性：" + a1.getAttribute().getAttrId());
				continue;
			}

			// 忽略部分属性（系统自动生成，不受前台控制）
			boolean isIgnored = false;
			if (checkIgnoredAttributes) {
				for (String ignor : BasicRule.IGNORED_ATTRIBUTES) {
					if (ignor.equals(a1.getAttribute().getAttrId())) {
						isIgnored = true;
						logger.debug("忽略传入的参数" + ignor + "：" + a1.getText());
						break;
					}
				}
				// 表达式型的字段不更新
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
		// 应用资源更新的规则
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
		System.out.println("资源(" + this.id + ")的信息:");
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
	 * 获取资源对象的关联关系
	 * 
	 * @return 资源对象的关联关系列表
	 */
	public List<ResourceRelation> getRelations() {
		return ServiceManager.getRelationService().getRelationsByResourceId(
				getId());
	}

	/**
	 * 获取关联的对象
	 * 
	 * @return 关联的对象列表
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
	 * 获取关联的对象
	 * 
	 * @param relationId
	 *            关联关系类型
	 * @return 关联的对象列表
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
	 * 获取关联的对象
	 * 
	 * @param relationId
	 *            关联关系类型
	 * @param modelId1
	 *            模型id
	 * @return 关联的对象列表
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
	 * 获取所属模型对象
	 * 
	 * @return 所属模型对象
	 */
	public Model getModel() {
		return ServiceManager.getModelService().getModelById(this.modelId);
	}

	/**
	 * 内部函数，获取资源展示时的表头信息
	 * 
	 * @param headIds
	 *            属性id列表
	 * @return 表头包含的属性信息
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
	 * 获取资源展示时的表头信息，在派生类中重载
	 * 
	 * @return 表头包含的属性信息
	 */
	public List<AttributeBase> getHeader() {
		// return getHeader(new String[] { "id", "name", "important",
		// "description", "resource_admin", "status" });
		return getHeader(new String[] { "id", "name", "customer_id",
				"order_id", "status", "description", "last_update_by",
				"last_update_time" });
	}

	/**
	 * 设置资源展示时的表头信息，此函数不生效，在flex序列化时使用
	 * 
	 * @param header
	 *            资源展示时的表头信息
	 */
	public void setHeader(List<AttributeBase> header) {
		// 此处不做任何事
	}

	/**
	 * 获取简单描述信息
	 * 
	 * @return 简单描述信息
	 */
	public String getSimpleDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append("ID: ").append(this.getAttributeValue("id"));
		if (!this.enabled) {
			sb.append("(已删除)");
		}
		sb.append("\n名称: ").append(this.getAttributeValue("name"));
		sb.append("\n描述: ").append(this.getAttributeValue("description")).append(
				"\n");
		return sb.toString();
	}

	/**
	 * 设置简单描述信息，此函数不生效，在flex序列化时使用
	 * 
	 * @param simpleDescription
	 *            简单描述信息
	 */
	public void setSimpleDescription(String simpleDescription) {
		// 此处不做任何事
	}

	/**
	 * 将资源与输入的字符进行模糊比较
	 * 
	 * @param str
	 *            比较内容
	 * @return true=匹配，false=不匹配
	 */
	public boolean isMatch(String str) {
		// TODO 此处的逻辑需要完善, 以符合更多的搜索条件. 例如, 可以对参数进行解析, 生成搜索条件
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
	 * 匹配指定属性, 指定的属性值.指定属性详见isMatch(String str)
	 * @param strArr 属性值
	 * @return true=匹配，false=不匹配
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
	 * 获取模糊匹配的条件说明
	 * 
	 * @return 模糊匹配的条件说明
	 */
	public String getMatchDescription() {
		return "ID相同，或名称,或状态包含输入内容";
	}
	
	/**
	 * 返回在指定的天数类是否发生更新
	 * @param period 天数
	 * @return 是否发生更新
	 */
	public boolean isChange(Object period){
		String lastupdate = this.getAttributeValue("last_update_time");
		try {
			long _l = DateTimeUtil.parseDate(lastupdate, null).getTime();
			//最后更新时间与间隔的时间和 >= 当前时间, 则说明是在间隔天数类的发生了更新
			if(Integer.valueOf(period.toString())*1000*60*60*24 + _l <= System.currentTimeMillis())
				return true;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
}
