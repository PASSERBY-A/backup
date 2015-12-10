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
	 * ���л�id
	 */
	private static final long serialVersionUID = 3356261941866338107L;
	/**
	 * �ڸ���ʱ���Ե�����
	 */
	public static final String IGNORED_ATTRIBUTES[] = { 
			"id", // ��Դid
			"last_update_time", // ������ʱ��
			"last_update_by", // ��������
			"create_time", // ����ʱ��
			"" };

	/**
	 * �жϱ����Ƿ������ĳ������
	 * 
	 * @param attributes
	 *            �����б�
	 * @param attrId
	 *            ����id
	 * @return true=������/false=�޸���
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
	 * ��ȡ���µ�����ֵ
	 * 
	 * @param attributes
	 *            �����б�
	 * @param attrId
	 *            ����id
	 * @return ���µ�����ֵ
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
		// �������ˣ�������ʱ��
		object.update(el, "last_update_time", currTime);
		object.update(el, "last_update_by", "" + userId);

		// ���ɴ���ʱ��
		if (object.getAttribute("create_time").isNull()) {
			object.update(el, "create_time", currTime);
		}

//		// �������״̬������������ʱ��
//		if (isAttributeUpdated(attributes, "audit_status")) {
//			if ("�����".equals(getUpdateText(attributes, "audit_status"))) { // ��"δ���"״̬���
//				object.update(el, "last_audit_time", currTime);
//			}
//		}
//
//		if (!isAttributeUpdated(attributes, "delete_status")) {
//			// ɾ��״̬û�и���ʱ�������ֵʱ������Ĭ��ֵ��������
//			if (object.getAttribute("delete_status").isNull()) {
//				object.update(el, "delete_status", "����");
//			}
//		} else if ("��ɾ��".equals(getUpdateText(attributes, "delete_status"))) {
//			// ����Ϊ"��ɾ��"ʱ������ɾ��ʱ��
//			object.update(el, "delete_time", currTime);
//		}

		// ������������
		if (object.getAttribute("searchcode").isNull()) {
			String sc = getSearchCode(object.getModelId());
			if (sc != null)
				object.update(el, "searchcode", sc);
		}
	}

	/**
	 * ������������
	 * 
	 * @param modelId
	 *            ģ��id
	 * @return ��������
	 * @throws Exception
	 *             �޿�����������ʱ����
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
		if (prefix == null) // û�ж���ǰ׺����Ϊ������������
			return null;
		int raw = DbUtil.getSequence("searchcode_" + prefix); // ��¼ԭʼ����
		ResourceObjectCacheStore store = (ResourceObjectCacheStore) ((CachedResourceService) ServiceManager
				.getResourceService()).getCache().getCacheStore();
		for (;;) {
			raw = raw % 1000000; // ֻȡ6λ
			if (raw == 0)
				break;
			String num = "" + raw;
			// ����6λ����
			while (num.length() < 6)
				num = "0" + num;
			List<ResourceObject> l = store.findInGlobal("searchcode", prefix
					+ num);
			if (l.size() == 0) // ���ظ���
				return prefix + num;
			raw++; // ������һ����������
		}
		throw new Exception("�޿��õ���������");
	}

	public void valid(ResourceObject object) throws Exception {
		Model model = object.getModel();
		List<ModelAttribute> list = model.getAttributes();
		String error = "";
		ResourceObjectCacheStore store = (ResourceObjectCacheStore) ((CachedResourceService) ServiceManager
				.getResourceService()).getCache().getCacheStore();
		for (ModelAttribute attr : list) {
			// ����ֵ
//			if (!attr.isNullable()) {
//				String value = object.getAttributeValue(attr.getAttrId());
//				if (value == null || value.trim().length() == 0) {
//					error += "���ԡ�" + attr.getName() + "/" + attr.getAttrId()
//							+ "������Ϊ��\n";
//				}
//			}
			
			//������ݺϷ����Լ���ֵ
			AttributeBase ab = object.getAttribute(attr.getAttrId());
			if(ab != null){
				String valid = ab.isValid(object);
				if(valid != null)
					error += (valid+"\n");
			}

			// ���Ψһ��
			String value = object.getAttributeValue(attr.getAttrId());
			if (value != null && value.length() > 0) { // ֻ�Էǿ�ֵ���Ψһ��
				AttributeDefine ad = attr.getDefine();
				List<ResourceObject> l = null;
				String area = "";
				// ���Ψһ��
				if (ad.isGlobalUnique()) {
					l = store.findInGlobal(attr.getAttrId(), object
							.getAttributeValue(attr.getAttrId()));
					area = "ϵͳ��";
				} else if (ad.isModelUnique()) {
					l = store.findInModel(object.getModelId(),
							attr.getAttrId(), object.getAttributeValue(attr
									.getAttrId()));
					area = "ģ����";
				}
				if (l != null) {
					if (l.size() > 1) {
						error += "���ԡ�" + attr.getName() + "/"
								+ attr.getAttrId() + "��Υ��Ψһ��Լ������[��"+value+"��"+area+"�Ѵ���]\n";
					} else if (l.size() == 1
							&& l.get(0).getId() != object.getId()) {
						error += "���ԡ�" + attr.getName() + "/"
								+ attr.getAttrId() + "��Υ��Ψһ��Լ������[��"+value+"��"+area+"�Ѵ���]\n";
					}
				}
			}
		}
		if (error.length() > 0) {
			throw new Exception(error.substring(0, error.length() - 1));
		}
	}
}
