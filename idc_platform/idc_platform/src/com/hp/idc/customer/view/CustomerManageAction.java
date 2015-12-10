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
 * @function 客户关系管理Action
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
	// 客户基本信息实体
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
	 * @function 进入客户关系管理首页面
	 * @return
	 */
	public String toCustomerManage() {

		return SUCCESS;
	}

	/**
	 * @function 分页查询客户基本信息
	 * @return
	 */
	public String queryCustomerInfo() {
		try {
			/****** 组装查询条件 Start ***********/
			Map<String, Object> paramMap = new HashMap<String, Object>();
			// 集团编号
			if (search_id != null && search_id > 0) {
				paramMap.put("id", Long.valueOf(search_id));
			}
			// 客户类别
			if (search_typeId != null && search_typeId > 0) {
				paramMap.put("typeId", search_typeId);
			}
			// 客户名称
			if (search_name != null && !search_name.equals("")) {
				paramMap.put("name", search_name.trim());
			}
			// 大客户经理
			if (search_managerName != null && !search_managerName.equals("")) {
				paramMap.put("managerName", search_managerName.trim());
			}
			// 电话
			if (search_phoneNo != null && !search_phoneNo.equals("")) {
				paramMap.put("phoneNo", search_phoneNo.trim());
			}
			// email
			if (search_email != null && !search_email.equals("")) {
				paramMap.put("email", search_email.trim());
			}

			/****** 组装查询条件 End ***********/
			LinkedHashMap<String, String> sortMap = new LinkedHashMap<String, String>();
			System.out.println(customerManageService);
			Page<CusInfo> page = customerManageService.queryCustomerInfo(
					paramMap, sortMap, start / limit + 1, limit);
			List<CusInfo> list = page.getResult();

			/** 组装返回jsonObject */
			jsonObject = new JSONObject();
			jsonArray = new JSONArray();

			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setExcludes(new String[] { "openTime", "cancelTime",
					"activeTime" });

			for (CusInfo customer : list) {
				JSONObject json = JSONObject.fromObject(customer, jsonConfig);

				json.put("openTime", customer.getOpenTime() == null ? "" : df
						.format(customer.getOpenTime()).toString());// 开户时间
				json.put("cancelTime", customer.getCancelTime() == null ? ""
						: df.format(customer.getCancelTime()).toString());// 销户时间
				json.put("activeTime", customer.getActiveTime() == null ? ""
						: df.format(customer.getActiveTime()).toString());// 入驻时间
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
	 * @function 跳转到客户关系信息查看窗口
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
	 * @function 查询客户联系人信息
	 * @return
	 */
	public String queryContact() {
		try {
			/****** 组装查询条件 Start ***********/
			Map<String, Object> paramMap = new HashMap<String, Object>();

			// 集团编号
			if (id != null && !id.equals("")) {
				paramMap.put("customerId", Long.valueOf(id.trim()));
			}
			/****** 组装查询条件 End ***********/
			LinkedHashMap<String, String> sortMap = new LinkedHashMap<String, String>();
			Page<CusContact> page = customerManageService.queryCustomerContact(
					paramMap, sortMap, start / limit + 1, limit);
			List<CusContact> list = page.getResult();

			/** 组装返回jsonObject */
			jsonObject = new JSONObject();
			jsonArray = new JSONArray();

			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setExcludes(new String[] { "doneDate" });

			for (CusContact contact : list) {
				JSONObject json = JSONObject.fromObject(contact, jsonConfig);

				json.put("doneDate", contact.getDoneDate() == null ? "" : df
						.format(contact.getDoneDate()).toString());// 操作日期
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
	 * @function 查询客户账务费用信息
	 * @return
	 */
	public String queryAccount() {
		try {
			/****** 组装查询条件 Start ***********/
			Map<String, Object> paramMap = new HashMap<String, Object>();

			// 集团编号
			if (id != null && !id.equals("")) {
				paramMap.put("customerId", Long.valueOf(id.trim()));
			}
			/****** 组装查询条件 End ***********/
			LinkedHashMap<String, String> sortMap = new LinkedHashMap<String, String>();
			Page<CusAccount> page = customerManageService.queryCustomerAccount(
					paramMap, sortMap, start / limit + 1, limit);
			List<CusAccount> list = page.getResult();

			/** 组装返回jsonObject */
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
	 * @function 查询客户订购信息
	 * @return
	 */
	public String queryServant() {
		try {
			/****** 组装查询条件 Start ***********/
			Map<String, Object> paramMap = new HashMap<String, Object>();

			// 集团编号
			if (id != null && !id.equals("")) {
				paramMap.put("customerId", Long.valueOf(id.trim()));
			}
			/****** 组装查询条件 End ***********/
			LinkedHashMap<String, String> sortMap = new LinkedHashMap<String, String>();
			Page<CusServant> page = customerManageService.queryCustomerServant(
					paramMap, sortMap, start / limit + 1, limit);
			List<CusServant> list = page.getResult();

			/** 组装返回jsonObject */
			jsonObject = new JSONObject();
			jsonArray = new JSONArray();

			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setExcludes(new String[] { "createDate","validDate","expireDate","doneDate" });

			for (CusServant servant : list) {
				JSONObject json = JSONObject.fromObject(servant, jsonConfig);
				json.put("createDate", servant.getCreateDate() == null ? "" : df
						.format(servant.getCreateDate()).toString());// 创建日期
				json.put("validDate", servant.getValidDate() == null ? "" : df
						.format(servant.getValidDate()).toString());// 生效日期
				json.put("expireDate", servant.getExpireDate() == null ? "" : df
						.format(servant.getExpireDate()).toString());// 失效日期
				json.put("doneDate", servant.getDoneDate() == null ? "" : df
						.format(servant.getDoneDate()).toString());// 受理日期
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
	 * @function 查询客户消费记录信息
	 * @return
	 */
	public String queryService() {
		try {
			/****** 组装查询条件 Start ***********/
			Map<String, Object> paramMap = new HashMap<String, Object>();

			// 集团编号
			if (id != null && !id.equals("")) {
				paramMap.put("customerId", Long.valueOf(id.trim()));
			}
			/****** 组装查询条件 End ***********/
			LinkedHashMap<String, String> sortMap = new LinkedHashMap<String, String>();
			Page<CusService> page = customerManageService.queryCustomerService(
					paramMap, sortMap, start / limit + 1, limit);
			List<CusService> list = page.getResult();

			/** 组装返回jsonObject */
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
	 * @function 查询Boss IDC业务信息
	 * @return
	 */
	public String queryBussiness() {
		try {
			/****** 组装查询条件 Start ***********/
			Map<String, Object> paramMap = new HashMap<String, Object>();

			// 集团编号
			if (id != null && !id.equals("")) {
				paramMap.put("customerId", Long.valueOf(id.trim()));
			}
			/****** 组装查询条件 End ***********/
			LinkedHashMap<String, String> sortMap = new LinkedHashMap<String, String>();
			Page<CusBussiness> page = customerManageService.queryCustomerBussiness(
					paramMap, sortMap, start / limit + 1, limit);
			List<CusBussiness> list = page.getResult();

			/** 组装返回jsonObject */
			jsonObject = new JSONObject();
			jsonArray = new JSONArray();

			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setExcludes(new String[] { "createDate","validDate","expireDate","doneDate" });

			for (CusBussiness bussiness : list) {
				JSONObject json = JSONObject.fromObject(bussiness, jsonConfig);

				json.put("createDate", bussiness.getCreateDate() == null ? "" : df
						.format(bussiness.getCreateDate()).toString());// 创建日期
				json.put("validDate", bussiness.getValidDate() == null ? "" : df
						.format(bussiness.getValidDate()).toString());// 生效日期
				json.put("expireDate", bussiness.getExpireDate() == null ? "" : df
						.format(bussiness.getExpireDate()).toString());// 失效日期
				json.put("doneDate", bussiness.getDoneDate() == null ? "" : df
						.format(bussiness.getDoneDate()).toString());// 业务日期
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
