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

	// ������̨�ı�MAP����
	private static Map<String, String> fieldMap = new HashMap<String, String>();

	// ��������ע��
	private static Map<String, String> updateTaskParemMap = new HashMap<String, String>();

	// ��Դģ��Map ����, ��Ҫ���CI
	private static Map<String, String> resMap = new LinkedHashMap<String, String>();

	// ��Դģ��Map ���ͣ����� ģ�� remark ���
	private static Map<String, Object> detlaResMap = new LinkedHashMap<String, Object>();

	// ������
	private static Map<String, String> workFlowIDMap = new HashMap<String, String>();

	//ͳһ��ʱ���ʽ
	private static DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * �ͻ���Ϣ
	 */
	private static CusInfo cusInfo;

	  /**
     * 
     * Ԥռ�����ӿ�
     * 
     * @param requestNo ������ˮ��(ҵ����ˮ��)
     * @param customerno �ͻ����ű��
     * @param strTitle ������Ŀ
	 * @param strDesc ��������
     * @param resvcommand ҵ��ָ�
     * @param resvDuetime Ԥռʱ��
     * @param appendixs �����ֶ�
     * @return @return  0-����ɹ� ����Ϊ�������
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
	 * ������ͨ���
	 * 
	 * @param order
	 * @param type �ֱ��ִ�
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
	 * ��������Դ��(�� VHST=4)
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
	 * �Ƚ����ϵ���Դ
	 * 
	 * @return
	 */
	private Map<String, String> complCommand() {
		Map<String, String> result = new LinkedHashMap<String, String>();
		System.out.println("detlaResMap :"+detlaResMap.size());
		if(detlaResMap.size() >0){
			Object[] keyArr =  detlaResMap.keySet().toArray();
			// �Ƚ����϶��ڵ�
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
			// ����ϻ��µ�
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
	 * ƴ��CI�ֶ���Ҫ�ĸ�ʽ
	 * 
	 * @param resModel ģ��entity
	 * @param value ����ģ�͵�����
	 * @param remark ��ע
	 * @return
	 */
	private static String fillCi(Model resModel, String value,String remark) {
		return "{\"column1\":\"" + resModel.getName() + "\",\"column2\":\""
				+ resModel.getId() + "\",\"column3\":\"" + value
				+ "\",\"column4\":\""+remark+"\"}";
	}
	
	/**
	 * 
	 * ���ƴ��Cis �ֶ���Ҫ�ĸ�ʽ
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
	 * ��乤�����ڵ���Ϣ
	 * 
	 * @param cusInfoMap �ͻ���Ϣ
	 * @param strTitle  ����ͷ
	 * @param strDesc ��������
	 * @param inputParamMap ������Ϣ
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
	 * Ч��
	 * 
	 * @param requestNo ������ˮ��
	 * @param customerno �ͻ�ID
	 * @param strTitle ����ͷ
	 * @param strDesc ��������
	 * @param inputParamMap ���ݱ���Map
	 * @return 0Ϊ�ɹ� ����Ϊ������
	 */
	private static int validate(String requestNo, String customerno, String strTitle,
			String strDesc, Map<String, Object> inputParamMap) {
		// �ͻ����ű�Ų���ȷ
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
			// Ԥռ14��
			if (resvDuetime.after(getOffsetMonthDate(new Date(), 14))) {
				return 210;
			}
		}
		// Ԥռ�������
		if (inputParamMap.containsKey("resvcommand")) {
			int back = reccommod((String) inputParamMap.get("resvcommand"),"res");
			if (back != 0) {
				return back;
			}
		}
		// ҵ��״̬����ȷ
		if (inputParamMap.containsKey("requestType")) {
			if (!workFlowIDMap.containsKey(inputParamMap.get("requestType"))) {
                return 230;
			}
		}
		//ҵ�����Ѿ����ڣ����ڿ�ͨ�� 130
		if ("".equals(inputParamMap.get("requestNo"))) {
			return 130;
		}
		int doneCode = Integer
				.valueOf(((String) inputParamMap.get("requestNo")));
		List<CusBussiness> cusBussinessList = ((CusBussinessDao)ContextUtil.getBean("cusBussinessDao"))
				.queryCustomerBussinessList(doneCode);
		if (inputParamMap.containsKey("requestType")) {
			if (inputParamMap.get("requestType").equals("open")) {
				// ҵ�����Ѿ����ڣ����ڿ�ͨ�� 130
				if (cusBussinessList != null && cusBussinessList.size() != 0)
					return 130;
			} else {
				// ҵ���Ų����ڣ�����ҵ������ҵ����ֹ��140
				if (cusBussinessList == null || cusBussinessList.size() == 0)
					return 140;
			}
		}
		
		// �������
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
	 * ��������ʱ��
	 * 
	 * @param protoDate ��ʼʱ��
	 * @param weeksOffset Ҫ������Ƶ�
	 * @return ���ƺ��ʱ��
	 */
	public static Date getOffsetMonthDate(Date protoDate, int weeksOffset) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(protoDate);
		cal.add(Calendar.DATE, weeksOffset);
		return cal.getTime();
	}

	
	/**
	 * 
	 * IDCҵ�񹤵��ӿ�
	 * 
	 * @param requestNo
	 *            ������ˮ��(ҵ����ˮ��)
	 * @param requestType
	 *            ָ�����ͣ� ҵ��ͨ: open ҵ����: change ҵ��ֹͣ: terminate
	 * 
	 * @param busiType ҵ����������
	 * @param customerno �ͻ����ű��
	 * @param orderid ��ͬ���(�������)
	 * @param startDate ��ͬ��ʼ����
	 * @param endDate ��ͬ��������
	 * @param strTitle ������Ŀ
	 * @param strDesc ��������
	 * @param command ҵ��ָ�
	 * @param commandold ��ҵ��ָ�
	 * @return  0-����ɹ� ����Ϊ�������
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
