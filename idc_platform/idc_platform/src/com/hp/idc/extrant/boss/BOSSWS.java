package com.hp.idc.extrant.boss;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;

import com.hp.idc.business.dao.ServiceDao;
import com.hp.idc.business.dao.ServiceResourceDao;
import com.hp.idc.context.util.ContextUtil;
import com.hp.idc.customer.dao.CusBussinessDao;
import com.hp.idc.customer.dao.CusInfoDao;
import com.hp.idc.customer.entity.CusBussiness;
import com.hp.idc.customer.entity.CusInfo;
import com.hp.idc.itsm.webservice.ITSMWebService;
import com.hp.idc.resm.model.Model;
import com.hp.idc.business.entity.Service;
import com.hp.idc.business.entity.ServiceResource;

public class BOSSWS {

	// 传往后台的表单MAP数组
	private static Map<String, String> fieldMap = new HashMap<String, String>();

	// 函数参数注入
	private static Map<String, String> updateTaskParemMap = new HashMap<String, String>();

	// 资源模型Map 类型, 按要求的CI
	private static Map<String, String> resMap = new LinkedHashMap<String, String>();

	// 资源模型Map 类型，数量 模型 remark 组合
	private static Map<String, Object> detlaResMap = new LinkedHashMap<String, Object>();

	// 工作流
	private static Map<String, String> workFlowIDMap = new HashMap<String, String>();

	//统一的时间格式
	private static DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 客户信息
	 */
	private static CusInfo cusInfo;

	  /**
     * 
     * 预占工单接口
     * 
     * @param requestNo 请求流水号(业务流水号)
     * @param customerno 客户集团编号
     * @param strTitle 工单题目
	 * @param strDesc 工单描述
     * @param resvcommand 业务指令串
     * @param resvDuetime 预占时间
     * @param appendixs 备用字段
     * @return @return  0-请求成功 其它为错误代码
     */
	@SuppressWarnings("unchecked")
	public static int sendResourceReserveCmd(String requestNo, String customerno,
			String strTitle, String strDesc, String resvcommand,
			Date resvDuetime, String[] appendixs) {
		LinkedHashMap<String, Object> inputParamMap = new LinkedHashMap<String, Object>();
		inputParamMap.put("requestNo", requestNo);
		inputParamMap.put("resvcommand", resvcommand);
		inputParamMap.put("resvDuetime", resvDuetime);
		inputParamMap.put("appendixs", appendixs);
		int back = validate(requestNo, customerno, strTitle, strDesc,
				inputParamMap);
		if (back != 0) {
			return back;
		}
		Map<String, Object> cusInfoMap = new LinkedHashMap<String, Object>();
		try {
			cusInfoMap = BeanUtils.describe(cusInfo);
		} catch (Exception e1) {
			e1.printStackTrace();
			back = 500;
		}
	   
		fillMap(cusInfoMap, strTitle, strDesc, inputParamMap);
		
		fieldMap.put("cis", fillCis(resMap));
		fieldMap.put("busi_res_req", fillCis(resMap));
		System.out.println("fillCis(resMap) :" + fillCis(resMap));
		
		int workFlowID = Integer.parseInt(workFlowIDMap.get("start"));
		try {
			back = ITSMWebService.updateTask(-1, workFlowID, fieldMap, -1,
					updateTaskParemMap.get("toNodePath"),
					updateTaskParemMap.get("actionId"),
					updateTaskParemMap.get("assignTo"),
					updateTaskParemMap.get("operName"), 0);
		} catch (Exception e) {
			e.printStackTrace();
			back = 500;
		}

		return back;
	}
	
	/**
	 * 
	 * 分析开通命令串
	 * 
	 * @param order
	 * @param type 分辨字串
	 * @return
	 */
	private static int reccommod(String order, String type) {
		int back = 0;
		if (order != null && !"".equals(order)) {
			if (order.contains(",")) {
				String[] reqs = order.split(",");
				if (reqs.length == 1)
					return 200;
				for (String rec : reqs) {
					if(!Pattern.matches("\\w+=\\d", rec))
						return 200;
					String[] keyValues = rec.split("=");
					String key = keyValues[0];
					String value = keyValues[1];
					back = reqFill(key, value, type);
				}
			} else {
				if(!Pattern.matches("\\w+=\\d", order))
					return 200;
				String[] keyValues = order.split("=");
				String key = keyValues[0];
				String value = keyValues[1];
				back = reqFill(key, value, type);
			}
		}
		return back;
	}
	
	/**
	 * 
	 * 处理单个资源串(如 VHST=4)
	 * 
	 * @param rec
	 * @return
	 */
	private static int reqFill(String key, String value, String type) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("serviceValue", key);
		List<Service> serviceList = ((ServiceDao)ContextUtil.getBean("serviceDao"))
				.queryResultList(paramMap);
		if (serviceList == null || serviceList.size() == 0) {
			return 170;
		}
		Service service = serviceList.get(0);
		paramMap = new HashMap<String, Object>();
		paramMap.put("serviceId", service.getId());
		LinkedHashMap<String, String> sortMap = new LinkedHashMap<String, String>();
		List<ServiceResource> serviceResourceList = ((ServiceResourceDao)ContextUtil.getBean("serviceResourceDao"))
				.queryResultList(paramMap, sortMap);
		if (serviceResourceList == null
				|| serviceResourceList.size() == 0) {
			return 170;
		}
		
		for (ServiceResource serviceResource : serviceResourceList) {
			String remark = "";
			if (serviceResource.getRemark() != null
					&& !"".equals(serviceResource.getRemark())) {
				remark = serviceResource.getRemark();
			}
			Model resModel = new Model();
			resModel = serviceResource.getResModel();
			if (!"new".equals(type)) {
				resMap.put(type + "-" + key, fillCi(resModel, value, remark));
			}
			if (!"res".equals(type)) {
				Object[] objectArr = new Object[3];
				objectArr[0] = value;
				objectArr[1] = resModel;
				objectArr[2] = remark;
				detlaResMap.put(type + "-" + key, objectArr);
			}
		}
		return 0;
	}
	
	/**
	 * 
	 * 比较新老的资源
	 * 
	 * @return
	 */
	private Map<String, String> complCommand() {
		Map<String, String> result = new LinkedHashMap<String, String>();
		System.out.println("detlaResMap :"+detlaResMap.size());
		if(detlaResMap.size() >0){
			Object[] keyArr =  detlaResMap.keySet().toArray();
			// 比较新老都在的
			for (int i = 0; i > keyArr.length; i++) {
				for (int j = i; j > keyArr.length; j++) {
					if (((String)keyArr[i]).substring("old-".length()).equalsIgnoreCase(
							((String)keyArr[j]).substring("new-".length()))) {
						Object[] objectOld = (Object[]) detlaResMap
								.get(keyArr[i]);
						Object[] objectNew = (Object[]) detlaResMap
								.get(keyArr[j]);
						int value = Integer.valueOf((String) objectNew[0])
								- Integer.valueOf((String) objectOld[0]);
						if (value != 0) {
							result.put(
									((String)keyArr[i]).substring("old-".length()),
									fillCi((Model) objectNew[1],
											String.valueOf(value),
											(String) objectNew[2]));
						}
					}
				}
			}
			// 添加老或新的
			for (int i = 0; i > keyArr.length; i++) {
				String key = ((String)keyArr[i]);
				if (key.contains("new-")) {
					String newKey = key.substring("new-".length());
					if (!result.containsKey(newKey)) {
						Object[] objectNew = (Object[]) detlaResMap.get(key);
						result.put(
								((String)keyArr[i]).substring("new-".length()),
								fillCi((Model) objectNew[1],
										String.valueOf(objectNew[0]),
										(String) objectNew[2]));
					}
				} else if (key.contains("old-")) {
					String oldKey = key.substring("old-".length());
					if (!result.containsKey(oldKey)) {
						Object[] objectOld = (Object[]) detlaResMap.get(key);
						int value = Integer.valueOf((String) objectOld[0]);
						result.put(
								((String)keyArr[i]).substring("new-".length()),
								fillCi((Model) objectOld[1],
										String.valueOf(-value),
										(String) objectOld[2]));
					}
				}

			}
		}
		return result;
	}
	
	/**
	 * 
	 * 拼接CI字段需要的格式
	 * 
	 * @param resModel 模型entity
	 * @param value 订购模型的数量
	 * @param remark 备注
	 * @return
	 */
	private static String fillCi(Model resModel, String value,String remark) {
		return "{\"column1\":\"" + resModel.getName() + "\",\"column2\":\""
				+ resModel.getId() + "\",\"column3\":\"" + value
				+ "\",\"column4\":\""+remark+"\"}";
	}
	
	/**
	 * 
	 * 最后拼成Cis 字段需要的格式
	 * 
	 * @param resMap
	 * @return
	 */
	private static String fillCis(Map<String, String> resMap){
		String Cis = "[";
		int i = 0;
		for (String key : resMap.keySet()) {
			if (i == 0) {
				Cis = Cis + resMap.get(key);
			} else {
				Cis = Cis + "," + resMap.get(key);
			}
			i++;
		}
		Cis = Cis +"]";
		return Cis;
	}
		
	/**
	 * 
	 * 填充工作流节点信息
	 * 
	 * @param cusInfoMap 客户信息
	 * @param strTitle  工单头
	 * @param strDesc 工单描述
	 * @param inputParamMap 输入信息
	 */
	private static void fillMap(Map<String, Object> cusInfoMap, String strTitle,
			String strDesc, Map<String, Object> inputParamMap) {
		for (String key : fieldMap.keySet()) {
			if (fieldMap.get(key) != null
					&& cusInfoMap.containsKey(fieldMap.get(key))) {
				fieldMap.put(key, (String) cusInfoMap.get(fieldMap.get(key)));
			}
		}
		fieldMap.put("title", strTitle);
		fieldMap.put("content", strDesc);
		fieldMap.put("task_create_time", timeFormat.format(new Date()));
		fieldMap.put("task_create_by", "root");
		fieldMap.put("taskOper", "root");
		fieldMap.put("carry_deadline", timeFormat.format(new Date()));
		fieldMap.put("doneCode", (String) inputParamMap.get("requestNo"));
		
		if (inputParamMap.containsKey("orderid")) {
			fieldMap.put("orderNo", (String) inputParamMap.get("orderid"));
		}
		if (inputParamMap.containsKey("startDate")) {
			fieldMap.put("effect_date", timeFormat.format(inputParamMap.get("startDate")));
		}
		if (inputParamMap.containsKey("endDate")) {
			fieldMap.put("expire_date", timeFormat.format(inputParamMap.get("endDate")));
		}
		if (inputParamMap.containsKey("busiType")) {
			fieldMap.put("type", (String) inputParamMap.get("busiType"));
		}
	}

	
	
	/**
	 * 
	 * 效验
	 * 
	 * @param requestNo 请求流水号
	 * @param customerno 客户ID
	 * @param strTitle 工单头
	 * @param strDesc 工单描述
	 * @param inputParamMap 数据变量Map
	 * @return 0为成功 其他为有问题
	 */
	private static int validate(String requestNo, String customerno, String strTitle,
			String strDesc, Map<String, Object> inputParamMap) {
		// 客户集团编号不正确
		System.out.println("customerno " + customerno);
		if ("".equals(customerno)) {
			return 110;
		} else {
			cusInfo = ((CusInfoDao)ContextUtil.getBean("cusInfoDao")).get(Long.parseLong(customerno));
			if (cusInfo == null || "".equals(cusInfo.getName())) {
				return 110;
			}
		}
		if (inputParamMap.containsKey("resvDuetime")) {
			Date resvDuetime = (Date) inputParamMap.get("resvDuetime");
			// 预占14天
			if (resvDuetime.after(getOffsetMonthDate(new Date(), 14))) {
				return 210;
			}
		}
		// 预占命令分析
		if (inputParamMap.containsKey("resvcommand")) {
			int back = reccommod((String) inputParamMap.get("resvcommand"),"res");
			if (back != 0) {
				return back;
			}
		}
		// 业务状态不正确
		if (inputParamMap.containsKey("requestType")) {
			if (!workFlowIDMap.containsKey(inputParamMap.get("requestType"))) {
                return 230;
			}
		}
		//业务编号已经存在（对于开通） 130
		if ("".equals(inputParamMap.get("requestNo"))) {
			return 130;
		}
		int doneCode = Integer
				.valueOf(((String) inputParamMap.get("requestNo")));
		List<CusBussiness> cusBussinessList = ((CusBussinessDao)ContextUtil.getBean("cusBussinessDao"))
				.queryCustomerBussinessList(doneCode);
		if (inputParamMap.containsKey("requestType")) {
			if (inputParamMap.get("requestType").equals("open")) {
				// 业务编号已经存在（对于开通） 130
				if (cusBussinessList != null && cusBussinessList.size() != 0)
					return 130;
			} else {
				// 业务编号不存在（对于业务变更、业务终止）140
				if (cusBussinessList == null || cusBussinessList.size() == 0)
					return 140;
			}
		}
		
		// 命令分析
		if (inputParamMap.containsKey("commandold")) {
			int back = reccommod((String) inputParamMap.get("commandold"),"old");
			if (back != 0) {
				return back;
			}
		}
		if (inputParamMap.containsKey("command")) {
			int back = reccommod((String) inputParamMap.get("command"),"new");
			if (back != 0) {
				return back;
			}
		}
		return 0;

	}
	
	/**
	 * 
	 * 计算推移时间
	 * 
	 * @param protoDate 开始时间
	 * @param weeksOffset 要向后推移的
	 * @return 推移后的时间
	 */
	public static Date getOffsetMonthDate(Date protoDate, int weeksOffset) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(protoDate);
		cal.add(Calendar.DATE, weeksOffset);
		return cal.getTime();
	}

	
	/**
	 * 
	 * IDC业务工单接口
	 * 
	 * @param requestNo
	 *            请求流水号(业务流水号)
	 * @param requestType
	 *            指令类型： 业务开通: open 业务变更: change 业务停止: terminate
	 * 
	 * @param busiType 业务类型类型
	 * @param customerno 客户集团编号
	 * @param orderid 合同编号(订单编号)
	 * @param startDate 合同开始日期
	 * @param endDate 合同结束日期
	 * @param strTitle 工单题目
	 * @param strDesc 工单描述
	 * @param command 业务指令串
	 * @param commandold 旧业务指令串
	 * @return  0-请求成功 其它为错误代码
	 */
	@SuppressWarnings("unchecked")
	public int sendServiceCmd(String requestNo, String requestType,
			String busiType, String customerno, String orderid, Date startDate,
			Date endDate, String strTitle, String strDesc, String command,
			String commandold) {
		Map<String, Object> inputParamMap = new LinkedHashMap<String, Object>();
		inputParamMap.put("requestNo", requestNo);
		inputParamMap.put("orderid", orderid);
		inputParamMap.put("requestType", requestType);
		inputParamMap.put("busiType", busiType);
		inputParamMap.put("startDate", startDate);
		inputParamMap.put("endDate", endDate);
		inputParamMap.put("command", command);
		inputParamMap.put("commandold", commandold);
		int back = validate(requestNo, customerno, strTitle, strDesc,
				inputParamMap);
		if (back != 0) {
			return back;
		}
		Map<String, Object> cusInfoMap = new LinkedHashMap<String, Object>();
		try {
			cusInfoMap = BeanUtils.describe(cusInfo);
		} catch (Exception e1) {
			e1.printStackTrace();
			back = 500;
		}
		fillMap(cusInfoMap, strTitle, strDesc, inputParamMap);
		Map<String, String> detlaMap = complCommand();
		fieldMap.put("cis", fillCis(resMap));
		fieldMap.put("busi_res_req", fillCis(detlaMap));
		
		int workflowID = (int) Long.parseLong(workFlowIDMap.get(requestType));
		
		try {
			back = ITSMWebService.updateTask(-1, workflowID, fieldMap, -1, updateTaskParemMap.get("toNodePath"),
					updateTaskParemMap.get("actionId"),
					updateTaskParemMap.get("assignTo"),
					updateTaskParemMap.get("operName"), 0);
		} catch (Exception e) {
			e.printStackTrace();
			back = 500;
		}
		return back;
	}

	public Map<String, String> getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(Map<String, String> fieldMap) {
		BOSSWS.fieldMap = fieldMap;
	}

	public Map<String, String> getWorkFlowIDMap() {
		return workFlowIDMap;
	}

	public void setWorkFlowIDMap(Map<String, String> workFlowIDMap) {
		BOSSWS.workFlowIDMap = workFlowIDMap;
	}

	public Map<String, String> getUpdateTaskParemMap() {
		return updateTaskParemMap;
	}

	public void setUpdateTaskParemMap(Map<String, String> updateTaskParemMap) {
		BOSSWS.updateTaskParemMap = updateTaskParemMap;
	}

}
