package com.hp.idc.customer.view;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Required;

import com.hp.idc.common.Const;
import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.view.AbstractAction;
import com.hp.idc.customer.entity.CusAccount;
import com.hp.idc.customer.entity.CusBussiness;
import com.hp.idc.customer.entity.CusContact;
import com.hp.idc.customer.entity.CusInfo;
import com.hp.idc.customer.entity.CusServant;
import com.hp.idc.customer.entity.CusService;
import com.hp.idc.customer.service.CustomerManageService;
import com.hp.idc.kbm.entity.KbmKnowledge;
import com.hp.idc.kbm.view.KnowledgeAction;

/**
 * 
 * 
 * @function �ͻ���ϵ����Action
 * @author Fancy
 * @version 1.0, 2:25:22 PM Jul 29, 2011
 * 
 */
public class CustomerManageAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8266029141371869113L;
	/**
	 * 
	 */
	

	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	// �ͻ�������Ϣʵ��
	private CusInfo cusInfo;

	//@Resource
	private CustomerManageService customerManageService;
	private Long search_id;
	private Integer search_typeId;
	private String search_name;
	private String search_managerName;
	private String search_phoneNo;
	private String search_email;

	private String id;

	/**
	 * @function ����ͻ���ϵ������ҳ��
	 * @return
	 */
	public String toCustomerManage() {

		return SUCCESS;
	}

	/**
	 * @function ��ҳ��ѯ�ͻ�������Ϣ
	 * @return
	 */
	public String queryCustomerInfo() {
		try {
			/****** ��װ��ѯ���� Start ***********/
			Map<String, Object> paramMap = new HashMap<String, Object>();
			// ���ű��
			if (search_id != null && search_id > 0) {
				paramMap.put("id", Long.valueOf(search_id));
			}
			// �ͻ����
			if (search_typeId != null && search_typeId > 0) {
				paramMap.put("typeId", search_typeId);
			}
			// �ͻ�����
			if (search_name != null && !search_name.equals("")) {
				paramMap.put("name", search_name.trim());
			}
			// ��ͻ�����
			if (search_managerName != null && !search_managerName.equals("")) {
				paramMap.put("managerName", search_managerName.trim());
			}
			// �绰
			if (search_phoneNo != null && !search_phoneNo.equals("")) {
				paramMap.put("phoneNo", search_phoneNo.trim());
			}
			// email
			if (search_email != null && !search_email.equals("")) {
				paramMap.put("email", search_email.trim());
			}

			/****** ��װ��ѯ���� End ***********/
			LinkedHashMap<String, String> sortMap = new LinkedHashMap<String, String>();
			System.out.println(customerManageService);
			Page<CusInfo> page = customerManageService.queryCustomerInfo(
					paramMap, sortMap, start / limit + 1, limit);
			List<CusInfo> list = page.getResult();

			/** ��װ����jsonObject */
			jsonObject = new JSONObject();
			jsonArray = new JSONArray();

			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setExcludes(new String[] { "openTime", "cancelTime",
					"activeTime" });

			for (CusInfo customer : list) {
				JSONObject json = JSONObject.fromObject(customer, jsonConfig);

				json.put("openTime", customer.getOpenTime() == null ? "" : df
						.format(customer.getOpenTime()).toString());// ����ʱ��
				json.put("cancelTime", customer.getCancelTime() == null ? ""
						: df.format(customer.getCancelTime()).toString());// ����ʱ��
				json.put("activeTime", customer.getActiveTime() == null ? ""
						: df.format(customer.getActiveTime()).toString());// ��פʱ��
				jsonArray.add(json);

			}
			jsonObject.put(KnowledgeAction.JSON_RESULT, jsonArray.toString());
			jsonObject.put(KnowledgeAction.JSON_TOTAL_COUNT,
					page.getTotalSize());
		} catch (Exception e) {
			logError("CustomerManageAction-queryCustomerInfo", e);
		}
		return SUCCESS;
	}

	/**
	 * @function ��ת���ͻ���ϵ��Ϣ�鿴����
	 * @return
	 */
	public String detailCustomer() {
		if (id != null) {
			cusInfo = customerManageService
					.queryCusInfoById(Long.parseLong(id));
		}
		return SUCCESS;
	}

	/**
	 * @function ��ѯ�ͻ���ϵ����Ϣ
	 * @return
	 */
	public String queryContact() {
		try {
			/****** ��װ��ѯ���� Start ***********/
			Map<String, Object> paramMap = new HashMap<String, Object>();

			// ���ű��
			if (id != null && !id.equals("")) {
				paramMap.put("customerId", Long.valueOf(id.trim()));
			}
			/****** ��װ��ѯ���� End ***********/
			LinkedHashMap<String, String> sortMap = new LinkedHashMap<String, String>();
			Page<CusContact> page = customerManageService.queryCustomerContact(
					paramMap, sortMap, start / limit + 1, limit);
			List<CusContact> list = page.getResult();

			/** ��װ����jsonObject */
			jsonObject = new JSONObject();
			jsonArray = new JSONArray();

			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setExcludes(new String[] { "doneDate" });

			for (CusContact contact : list) {
				JSONObject json = JSONObject.fromObject(contact, jsonConfig);

				json.put("doneDate", contact.getDoneDate() == null ? "" : df
						.format(contact.getDoneDate()).toString());// ��������
				jsonArray.add(json);

			}
			jsonObject.put(KnowledgeAction.JSON_RESULT, jsonArray.toString());
			jsonObject.put(KnowledgeAction.JSON_TOTAL_COUNT,
					page.getTotalSize());
		} catch (Exception e) {
			logError("CustomerManageAction-queryContact", e);
		}
		return SUCCESS;
	}

	/**
	 * @function ��ѯ�ͻ����������Ϣ
	 * @return
	 */
	public String queryAccount() {
		try {
			/****** ��װ��ѯ���� Start ***********/
			Map<String, Object> paramMap = new HashMap<String, Object>();

			// ���ű��
			if (id != null && !id.equals("")) {
				paramMap.put("customerId", Long.valueOf(id.trim()));
			}
			/****** ��װ��ѯ���� End ***********/
			LinkedHashMap<String, String> sortMap = new LinkedHashMap<String, String>();
			Page<CusAccount> page = customerManageService.queryCustomerAccount(
					paramMap, sortMap, start / limit + 1, limit);
			List<CusAccount> list = page.getResult();

			/** ��װ����jsonObject */
			jsonObject = new JSONObject();
			jsonArray = new JSONArray();

			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setExcludes(new String[] {});

			for (CusAccount account : list) {
				JSONObject json = JSONObject.fromObject(account, jsonConfig);
				jsonArray.add(json);

			}
			jsonObject.put(KnowledgeAction.JSON_RESULT, jsonArray.toString());
			jsonObject.put(KnowledgeAction.JSON_TOTAL_COUNT,
					page.getTotalSize());
		} catch (Exception e) {
			logError("CustomerManageAction-queryAccount", e);
		}
		return SUCCESS;
	}

	/**
	 * @function ��ѯ�ͻ�������Ϣ
	 * @return
	 */
	public String queryServant() {
		try {
			/****** ��װ��ѯ���� Start ***********/
			Map<String, Object> paramMap = new HashMap<String, Object>();

			// ���ű��
			if (id != null && !id.equals("")) {
				paramMap.put("customerId", Long.valueOf(id.trim()));
			}
			/****** ��װ��ѯ���� End ***********/
			LinkedHashMap<String, String> sortMap = new LinkedHashMap<String, String>();
			Page<CusServant> page = customerManageService.queryCustomerServant(
					paramMap, sortMap, start / limit + 1, limit);
			List<CusServant> list = page.getResult();

			/** ��װ����jsonObject */
			jsonObject = new JSONObject();
			jsonArray = new JSONArray();

			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setExcludes(new String[] { "createDate","validDate","expireDate","doneDate" });

			for (CusServant servant : list) {
				JSONObject json = JSONObject.fromObject(servant, jsonConfig);
				json.put("createDate", servant.getCreateDate() == null ? "" : df
						.format(servant.getCreateDate()).toString());// ��������
				json.put("validDate", servant.getValidDate() == null ? "" : df
						.format(servant.getValidDate()).toString());// ��Ч����
				json.put("expireDate", servant.getExpireDate() == null ? "" : df
						.format(servant.getExpireDate()).toString());// ʧЧ����
				json.put("doneDate", servant.getDoneDate() == null ? "" : df
						.format(servant.getDoneDate()).toString());// ��������
				jsonArray.add(json);

			}
			jsonObject.put(KnowledgeAction.JSON_RESULT, jsonArray.toString());
			jsonObject.put(KnowledgeAction.JSON_TOTAL_COUNT,
					page.getTotalSize());
		} catch (Exception e) {
			logError("CustomerManageAction-queryServant", e);
		}
		return SUCCESS;
	}

	/**
	 * @function ��ѯ�ͻ����Ѽ�¼��Ϣ
	 * @return
	 */
	public String queryService() {
		try {
			/****** ��װ��ѯ���� Start ***********/
			Map<String, Object> paramMap = new HashMap<String, Object>();

			// ���ű��
			if (id != null && !id.equals("")) {
				paramMap.put("customerId", Long.valueOf(id.trim()));
			}
			/****** ��װ��ѯ���� End ***********/
			LinkedHashMap<String, String> sortMap = new LinkedHashMap<String, String>();
			Page<CusService> page = customerManageService.queryCustomerService(
					paramMap, sortMap, start / limit + 1, limit);
			List<CusService> list = page.getResult();

			/** ��װ����jsonObject */
			jsonObject = new JSONObject();
			jsonArray = new JSONArray();

			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setExcludes(new String[] { });

			for (CusService service : list) {
				JSONObject json = JSONObject.fromObject(service, jsonConfig);

				jsonArray.add(json);

			}
			jsonObject.put(KnowledgeAction.JSON_RESULT, jsonArray.toString());
			jsonObject.put(KnowledgeAction.JSON_TOTAL_COUNT,
					page.getTotalSize());
		} catch (Exception e) {
			logError("CustomerManageAction-queryService", e);
		}
		return SUCCESS;
	}

	/**
	 * @function ��ѯBoss IDCҵ����Ϣ
	 * @return
	 */
	public String queryBussiness() {
		try {
			/****** ��װ��ѯ���� Start ***********/
			Map<String, Object> paramMap = new HashMap<String, Object>();

			// ���ű��
			if (id != null && !id.equals("")) {
				paramMap.put("customerId", Long.valueOf(id.trim()));
			}
			/****** ��װ��ѯ���� End ***********/
			LinkedHashMap<String, String> sortMap = new LinkedHashMap<String, String>();
			Page<CusBussiness> page = customerManageService.queryCustomerBussiness(
					paramMap, sortMap, start / limit + 1, limit);
			List<CusBussiness> list = page.getResult();

			/** ��װ����jsonObject */
			jsonObject = new JSONObject();
			jsonArray = new JSONArray();

			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setExcludes(new String[] { "createDate","validDate","expireDate","doneDate" });

			for (CusBussiness bussiness : list) {
				JSONObject json = JSONObject.fromObject(bussiness, jsonConfig);

				json.put("createDate", bussiness.getCreateDate() == null ? "" : df
						.format(bussiness.getCreateDate()).toString());// ��������
				json.put("validDate", bussiness.getValidDate() == null ? "" : df
						.format(bussiness.getValidDate()).toString());// ��Ч����
				json.put("expireDate", bussiness.getExpireDate() == null ? "" : df
						.format(bussiness.getExpireDate()).toString());// ʧЧ����
				json.put("doneDate", bussiness.getDoneDate() == null ? "" : df
						.format(bussiness.getDoneDate()).toString());// ҵ������
				jsonArray.add(json);

			}
			jsonObject.put(KnowledgeAction.JSON_RESULT, jsonArray.toString());
			jsonObject.put(KnowledgeAction.JSON_TOTAL_COUNT,
					page.getTotalSize());
		} catch (Exception e) {
			logError("CustomerManageAction-queryBussiness", e);
		}
		return SUCCESS;
	}

	public long getSearch_id() {
		return search_id;
	}

	public void setSearch_id(long search_id) {
		this.search_id = search_id;
	}

	public int getSearch_typeId() {
		return search_typeId;
	}

	public void setSearch_typeId(int search_typeId) {
		this.search_typeId = search_typeId;
	}

	public String getSearch_name() {
		return search_name;
	}

	public void setSearch_name(String search_name) {
		this.search_name = search_name;
	}

	public String getSearch_managerName() {
		return search_managerName;
	}

	public void setSearch_managerName(String search_managerName) {
		this.search_managerName = search_managerName;
	}

	public String getSearch_phoneNo() {
		return search_phoneNo;
	}

	public void setSearch_phoneNo(String search_phoneNo) {
		this.search_phoneNo = search_phoneNo;
	}

	public String getSearch_email() {
		return search_email;
	}

	public void setSearch_email(String search_email) {
		this.search_email = search_email;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setSearch_id(Long search_id) {
		this.search_id = search_id;
	}

	public void setSearch_typeId(Integer search_typeId) {
		this.search_typeId = search_typeId;
	}

	public CusInfo getCusInfo() {
		return cusInfo;
	}

	public void setCusInfo(CusInfo cusInfo) {
		this.cusInfo = cusInfo;
	}

	//@Required
	public CustomerManageService getCustomerManageService() {
		return customerManageService;
	}

	public void setCustomerManageService(
			CustomerManageService customerManageService) {
		this.customerManageService = customerManageService;
	}

}
