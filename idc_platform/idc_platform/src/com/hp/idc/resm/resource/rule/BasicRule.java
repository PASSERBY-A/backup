/**
 * 
 */
package com.hp.idc.resm.resource.rule;

import java.io.Serializable;
import java.util.List;

import org.dom4j.Element;

import com.hp.idc.resm.cache.ResourceObjectCacheStore;
import com.hp.idc.resm.model.AttributeDefine;
import com.hp.idc.resm.model.Model;
import com.hp.idc.resm.model.ModelAttribute;
import com.hp.idc.resm.model.SearchCodeMapping;
import com.hp.idc.resm.resource.AttributeBase;
import com.hp.idc.resm.resource.DateTimeAttribute;
import com.hp.idc.resm.resource.ResourceObject;
import com.hp.idc.resm.service.CachedModelService;
import com.hp.idc.resm.service.CachedResourceService;
import com.hp.idc.resm.service.ServiceManager;
import com.hp.idc.resm.util.DbUtil;
import com.hp.idc.resm.util.StringUtil;

/**
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class BasicRule implements IResourceRule, Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 3356261941866338107L;
	/**
	 * 在更新时忽略的属性
	 */
	public static final String IGNORED_ATTRIBUTES[] = { 
			"id", // 资源id
			"last_update_time", // 最后更新时间
			"last_update_by", // 最后更新人
			"create_time", // 创建时间
			"" };

	/**
	 * 判断本次是否更新了某个属性
	 * 
	 * @param attributes
	 *            更新列表
	 * @param attrId
	 *            属性id
	 * @return true=更新了/false=无更新
	 */
	protected boolean isAttributeUpdated(List<AttributeBase> attributes,
			String attrId) {
		for (AttributeBase ab : attributes) {
			if (ab.getAttribute().getAttrId().equals(attrId))
				return true;
		}
		return false;
	}

	/**
	 * 获取更新的属性值
	 * 
	 * @param attributes
	 *            更新列表
	 * @param attrId
	 *            属性id
	 * @return 更新的属性值
	 */
	protected String getUpdateText(List<AttributeBase> attributes, String attrId) {
		for (AttributeBase ab : attributes) {
			if (ab.getAttribute().getAttrId().equals(attrId))
				return ab.getText();
		}
		return null;
	}

	public void update(ResourceObject object, List<AttributeBase> attributes,
			Element el, int userId) throws Exception {
		String currTime = StringUtil.getDateTime(
				DateTimeAttribute.DATE_TIME_FORMAT, System.currentTimeMillis());
		// 最后更新人，最后更新时间
		object.update(el, "last_update_time", currTime);
		object.update(el, "last_update_by", "" + userId);

		// 生成创建时间
		if (object.getAttribute("create_time").isNull()) {
			object.update(el, "create_time", currTime);
		}

//		// 根据审核状态，生成最近审核时间
//		if (isAttributeUpdated(attributes, "audit_status")) {
//			if ("已审核".equals(getUpdateText(attributes, "audit_status"))) { // 从"未审核"状态变更
//				object.update(el, "last_audit_time", currTime);
//			}
//		}
//
//		if (!isAttributeUpdated(attributes, "delete_status")) {
//			// 删除状态没有更新时，检查无值时，设置默认值“正常”
//			if (object.getAttribute("delete_status").isNull()) {
//				object.update(el, "delete_status", "正常");
//			}
//		} else if ("已删除".equals(getUpdateText(attributes, "delete_status"))) {
//			// 更改为"已删除"时，生成删除时间
//			object.update(el, "delete_time", currTime);
//		}

		// 生成搜索代码
		if (object.getAttribute("searchcode").isNull()) {
			String sc = getSearchCode(object.getModelId());
			if (sc != null)
				object.update(el, "searchcode", sc);
		}
	}

	/**
	 * 生成搜索代码
	 * 
	 * @param modelId
	 *            模型id
	 * @return 搜索代码
	 * @throws Exception
	 *             无可用搜索代码时发生
	 */
	private String getSearchCode(String modelId) throws Exception {
		CachedModelService cms = (CachedModelService) ServiceManager
				.getModelService();
		List<SearchCodeMapping> scmList = cms.getSearchCodeMappingCache()
				.getAll();
		String prefix = null;
		for (SearchCodeMapping scm : scmList) {
			if (scm.getModelId().equals(modelId)) {
				prefix = scm.getPrefix();
				break;
			}
		}
		if (prefix == null) // 没有定义前缀，认为不用搜索代码
			return null;
		int raw = DbUtil.getSequence("searchcode_" + prefix); // 记录原始序列
		ResourceObjectCacheStore store = (ResourceObjectCacheStore) ((CachedResourceService) ServiceManager
				.getResourceService()).getCache().getCacheStore();
		for (;;) {
			raw = raw % 1000000; // 只取6位
			if (raw == 0)
				break;
			String num = "" + raw;
			// 补齐6位数字
			while (num.length() < 6)
				num = "0" + num;
			List<ResourceObject> l = store.findInGlobal("searchcode", prefix
					+ num);
			if (l.size() == 0) // 无重复项
				return prefix + num;
			raw++; // 计数加一，继续查找
		}
		throw new Exception("无可用的搜索代码");
	}

	public void valid(ResourceObject object) throws Exception {
		Model model = object.getModel();
		List<ModelAttribute> list = model.getAttributes();
		String error = "";
		ResourceObjectCacheStore store = (ResourceObjectCacheStore) ((CachedResourceService) ServiceManager
				.getResourceService()).getCache().getCacheStore();
		for (ModelAttribute attr : list) {
			// 检查空值
//			if (!attr.isNullable()) {
//				String value = object.getAttributeValue(attr.getAttrId());
//				if (value == null || value.trim().length() == 0) {
//					error += "属性“" + attr.getName() + "/" + attr.getAttrId()
//							+ "”不能为空\n";
//				}
//			}
			
			//检查数据合法性以及空值
			AttributeBase ab = object.getAttribute(attr.getAttrId());
			if(ab != null){
				String valid = ab.isValid(object);
				if(valid != null)
					error += (valid+"\n");
			}

			// 检查唯一性
			String value = object.getAttributeValue(attr.getAttrId());
			if (value != null && value.length() > 0) { // 只对非空值检查唯一性
				AttributeDefine ad = attr.getDefine();
				List<ResourceObject> l = null;
				String area = "";
				// 检查唯一性
				if (ad.isGlobalUnique()) {
					l = store.findInGlobal(attr.getAttrId(), object
							.getAttributeValue(attr.getAttrId()));
					area = "系统内";
				} else if (ad.isModelUnique()) {
					l = store.findInModel(object.getModelId(),
							attr.getAttrId(), object.getAttributeValue(attr
									.getAttrId()));
					area = "模型内";
				}
				if (l != null) {
					if (l.size() > 1) {
						error += "属性“" + attr.getName() + "/"
								+ attr.getAttrId() + "”违反唯一性约束条件[“"+value+"“"+area+"已存在]\n";
					} else if (l.size() == 1
							&& l.get(0).getId() != object.getId()) {
						error += "属性“" + attr.getName() + "/"
								+ attr.getAttrId() + "”违反唯一性约束条件[“"+value+"“"+area+"已存在]\n";
					}
				}
			}
		}
		if (error.length() > 0) {
			throw new Exception(error.substring(0, error.length() - 1));
		}
	}
}
