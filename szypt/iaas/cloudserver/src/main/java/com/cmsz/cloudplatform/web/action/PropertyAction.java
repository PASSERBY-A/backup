package com.cmsz.cloudplatform.web.action;


import java.util.List;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmsz.cloudplatform.model.Property;
import com.cmsz.cloudplatform.model.request.ListPropertyRequest;
import com.cmsz.cloudplatform.model.request.SavePropertyRequest;
import com.cmsz.cloudplatform.model.response.EntityResponse;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.cmsz.cloudplatform.model.response.Response;
import com.cmsz.cloudplatform.service.LogManager;
import com.cmsz.cloudplatform.service.PropertyManager;
import com.cmsz.cloudplatform.utils.LogConstants;
import com.cmsz.cloudplatform.web.core.BaseAction;
import com.hp.util.Page;

public class PropertyAction extends BaseAction {

	@Autowired
	private LogManager logManager;
	
	private static final long serialVersionUID = -4355253863172120217L;
	private PropertyManager propertyManager =  null;
	private Property property=new Property();

	public PropertyAction() {
		super();
	}
	
	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}
	
	@Autowired
	public void setPropertyManager(PropertyManager propertyManager) {
		this.propertyManager = propertyManager;
	}
	
	public void deleteProperty() throws JSONException{
		String id = request.getParameter("id");
		String msg = null;
		
		try {
			propertyManager.remove(new Long(id));
			msg="删除设备成功";
			logManager.log(this.request.getRemoteHost(), LogConstants.PROPERTY_MANAGER, LogConstants.DELETE,this.activeUser.getLoginId(), "用户[" + this.activeUser.getLoginId() + "]。", LogConstants.SUCCESS, msg);
		} catch (Exception e) {
			msg="删除设备错误,请稍后再试";
			logManager.log(this.request.getRemoteHost(), LogConstants.PROPERTY_MANAGER, LogConstants.DELETE,this.activeUser.getLoginId(), "用户[" + this.activeUser.getLoginId() + "]。", LogConstants.FAIL, msg);
		}
		Response response = this.transformResponse(msg, "json", HttpStatus.SC_OK);
		writeResponse(response);
	}
	
	public String list(){
		
		ListPropertyRequest request = (ListPropertyRequest) this.wrapRequest(new ListPropertyRequest());
		
		ListResponse<Property> response = this.propertyManager.listProperty(request);
		
		int totalPage = 0;
		if (null != response.getResponses()) {
			totalPage = response.getCount();
		}
		
		Page page = new Page();
		page.setTotalCount(totalPage);
		page.setPageNo(request.getPage());
		page.setPageSize(request.getPagesize());
		JSONObject jsonPage = this.getJsonPage(page);
		jsonPage.put("propertys", response.getResponses());
		this.renderText(jsonPage.toString());
		/*
		if(StringUtils.isNotBlank(id)){
			property.setId(new Long(id));
			propertyList = propertyManager.list(property);
		}else{
			if(StringUtils.isNotBlank(keyword)){
				property.setCode(keyword);
				propertyList = propertyManager.list(property);
			}else{
					String propertyType = this.request.getParameter("type");
					String vendor = this.request.getParameter("vendor");
					String model = this.request.getParameter("model");
					String code = this.request.getParameter("code");
					String startDate = this.request.getParameter("start_date");
					String endDate = this.request.getParameter("end_date");
					String serialNum = this.request.getParameter("serial_num");
					String status = this.request.getParameter("status");
					String owner = this.request.getParameter("owner");
					
					addEqualQueryCriteria(detachedCriteria, "type",  propertyType);
					addEqualQueryCriteria(detachedCriteria, "vendor", vendor);
					addEqualQueryCriteria(detachedCriteria, "model", model);
					addEqualQueryCriteria(detachedCriteria, "code", code);
					addEqualQueryCriteria(detachedCriteria, "serial_num", serialNum);
					addEqualQueryCriteria(detachedCriteria, "status", status);
					addEqualQueryCriteria(detachedCriteria, "owner", owner);
					
//					String belong = this.request.getParameter("belong_to");
//					String room = this.request.getParameter("room");
//					String machineCabinet = this.request.getParameter("machine_cabinet");
//					
//					addEqualQueryCriteria(detachedCriteria, "belong_to",belong);
//					addEqualQueryCriteria(detachedCriteria, "room", room);
//					addEqualQueryCriteria(detachedCriteria, "machine_cabinet", machineCabinet);
					try {
						if (StringUtils.isNotBlank(startDate) ) {	
							SimpleDateFormat start = new SimpleDateFormat("yyyy-MM-dd");
							if( StringUtils.isNotBlank(endDate) ){
								SimpleDateFormat end = new SimpleDateFormat("yyyy-MM-dd");
								detachedCriteria.add(Restrictions.between("start_date", start.parse(startDate), end.parse(endDate)));
							}else{						
								detachedCriteria.add(Restrictions.gt("start_date", start.parse(startDate)));
							}
						}
						else if (StringUtils.isNotBlank(endDate)){
							SimpleDateFormat end = new SimpleDateFormat("yyyy-MM-dd");
							detachedCriteria.add(Restrictions.lt("start_date", end.parse(endDate)));
						}
						
						propertyList = propertyManager.listProperty(detachedCriteria, pageNumber, pageSize);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				
					propertyList  = propertyManager.listProperty(detachedCriteria, pageNumber, pageSize);
			}
		}
		
		int totalPage = 0;
		if (propertyList != null) {
			totalPage = (int) propertyManager.getTotalCount();
		}
		page.setTotalCount(totalPage);
		page.setPageNo(pageNumber);
		page.setPageSize(pageSize);
		JSONObject jsonPage = this.getJsonPage(page);
		jsonPage.put("propertys", propertyList);
		this.renderText(jsonPage.toString());
		
		*/
		/*Response response = this.transformResponse(propertyList, "json", HttpStatus.SC_OK);
		writeResponse(response);*/
		return null;
	}
	
	
	public String add(){
		String msg = "success";
		
		SavePropertyRequest request = (SavePropertyRequest) this.wrapRequest(new SavePropertyRequest());
		String errMsg = this.checkForm(request.getType(), request.getCode(), request.getSerial_num(), request.getId());
		if (errMsg.isEmpty()) {
			EntityResponse<Property> entityResponse = this.propertyManager.saveProperty(request);
			if(null!=request.getId()){
				logManager.log(this.request.getRemoteHost(), LogConstants.PROPERTY_MANAGER, LogConstants.MODIFY,this.activeUser.getLoginId(), "用户[" + this.activeUser.getLoginId() + "]。", LogConstants.SUCCESS, msg);
			}else{
				logManager.log(this.request.getRemoteHost(), LogConstants.PROPERTY_MANAGER, LogConstants.CREATE,this.activeUser.getLoginId(), "用户[" + this.activeUser.getLoginId() + "]。", LogConstants.SUCCESS, msg);
			}
		}else {
			msg = errMsg;
			if(null!=request.getId()){
				logManager.log(this.request.getRemoteHost(), LogConstants.PROPERTY_MANAGER, LogConstants.DELETE,this.activeUser.getLoginId(), "用户[" + this.activeUser.getLoginId() + "]。", LogConstants.FAIL, msg);
			}else{
				logManager.log(this.request.getRemoteHost(), LogConstants.PROPERTY_MANAGER, LogConstants.DELETE,this.activeUser.getLoginId(), "用户[" + this.activeUser.getLoginId() + "]。", LogConstants.FAIL, msg);
			}
		}
		
		Response response = this.transformResponse(msg, "json", HttpStatus.SC_OK);
		writeResponse(response);
		
		return NONE;
	}

	
	public String addX86Host(){
		return NONE;
		
	}
	
	
	public String addOARack(){
		return NONE;
	}
	
	
//	public String add(){
//		String msg = "success";
//		String type=this.request.getParameter("type");
//		String vendor = this.request.getParameter("vendor");
//		String model = this.request.getParameter("model");
//		String code=this.request.getParameter("code");
//		String serialNum = this.request.getParameter("serial_num");
//		String owner = this.request.getParameter("owner");
//		String status = this.request.getParameter("status");
//		String u_bit = this.request.getParameter("u_bit");
//		String u_high = this.request.getParameter("u_high");
//		
//		/*String osNameChn = this.request.getParameter("os_name_chn");
//		String osNameEng = this.request.getParameter("os_name_eng");
//		String belong = this.request.getParameter("belong_to");
//		String room = this.request.getParameter("room");
//		String machineCabinet = this.request.getParameter("machine_cabinet");
//		String using = this.request.getParameter("using");
//		String power = this.request.getParameter("power");
//		String check_point = this.request.getParameter("check_point");*/
//		
//		String cpu_account = this.request.getParameter("cpu_account");
//		String memory_size = this.request.getParameter("memory_size");
//		String hdd_size = this.request.getParameter("hdd_size");
//		String startDate = this.request.getParameter("start_date");
//		String endDate = this.request.getParameter("end_date");
//		
//		/**
//		 * 按照二期需求新增如下字段
//		 */
//		String cost = this.request.getParameter("cost");
//		String position = this.request.getParameter("position");
//		String position_desc = this.request.getParameter("position_desc");
//		String contractInfo = this.request.getParameter("contractInfo");
//		String service_period = this.request.getParameter("service_period");
//		String expireDate= this.request.getParameter("expire_date");
//		
//		
//		String errMsg = this.checkForm(type,code);
//		boolean isUpdate = false;
//		if (errMsg.isEmpty()) {
//			Property property = null;
//			Object[] ids = this.requestParams.get("id");
//			if (null != ids && ids.length > 0) {
//				Long id = Long.parseLong((String)ids[0]);
//				property = propertyManager.get(id);
//				isUpdate = true;
//
//				property.setModifiedBy(this.activeUser.getLoginId());
//				property.setModifiedOn(new Date());
//			}
//			else{			
//				property = new Property();
//				property.setCreatedBy(this.activeUser.getLoginId());
//				property.setCreatedOn(new Date());
//				property.setModifiedBy(this.activeUser.getLoginId());
//				property.setModifiedOn(new Date());
//			}
//			
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			Date date = null;
//			Date end_Date = null;
//			Date expire_date=null;
//			try {
//				if (StringUtils.isNotBlank(startDate)) {					
//					date = sdf.parse(startDate);
//				}
//				if (StringUtils.isNotBlank(endDate)) {					
//					end_Date = sdf.parse(endDate);
//				}
//				if (StringUtils.isNotBlank(expireDate)) {					
//					expire_date = sdf.parse(expireDate);
//				}
//			} catch (ParseException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}	
//			
//			if (!isUpdate) {
//				//以下字段不可更新
//				property.setType(type);
//				property.setVendor(vendor);
//				property.setModel(model);
//				property.setCode(code);
//				property.setSerial_num(serialNum);
//				property.setOwner(owner);
//				property.setU_high(u_high);
//				property.setStart_date(date);
//				property.setEnd_date(end_Date);
//				property.setExpire_date(expire_date);
//			}
//			
//			property.setOwner(owner);
//			property.setStatus(status);
//			property.setU_bit(u_bit);
//
//			/**
//			 * 按照二期需求注释一期部分字段
//			 */
//			
//			/*property.setOs_name_chn(osNameChn);
//			property.setOs_name_eng(osNameEng);
//			property.setBelong_to(belong);
//			property.setRoom(room);
//			property.setMachine_cabinet(machineCabinet);
//			
//			property.setUsing(Integer.parseInt(using));
//			property.setPower(power);
//			property.setCheck_point(Integer.parseInt(check_point));*/
//			
//			property.setCpu_account(cpu_account);
//			property.setMemory_size(memory_size);
//			property.setHdd_size(hdd_size);
//			
//			/**
//			 * 新增的字段如下
//			 */
//			property.setCost(cost);
//			property.setPosition(position);
//			property.setPosition_desc(position_desc);
//			property.setContractInfo(contractInfo);
//			property.setService_period(service_period);
//			
//			
//			try {
//				propertyManager.save(property);
//			} catch (Exception e) {
//				msg += e.getMessage();
//			}
//		}
//		else {
//			msg = errMsg;
//		}
//		Response response = this.transformResponse(msg, "json", HttpStatus.SC_OK);
//		writeResponse(response);
//		return NONE;
//	}
	
	public String checkForm(String type,String code, String sn, Long id){
		boolean isValidate = true;
		String errMsg = "";
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(code)) {
			switch (Integer.parseInt(type)) {
			case 0:
				if (!code.startsWith("10")) {
					isValidate = false;
				}
				break;
			case 1:
				if (!code.startsWith("11")) {
					isValidate = false;
				}
				break;
			case 2:
				if (!code.startsWith("12")) {
					isValidate = false;
				}
				break;
			case 3:
				if (!code.startsWith("20")) {
					isValidate = false;
				}
				break;
			case 4:
				if (!code.startsWith("21")) {
					isValidate = false;
				}
				break;
			case 5:
				if (!code.startsWith("22")) {
					isValidate = false;
				}
				break;
			case 6:
				if (!code.startsWith("30")) {
					isValidate = false;
				}
				break;
			default:
				break;
			}
		}
		if (!isValidate) {
			errMsg = "请根据设备类型输入对应的设备编码";
		} else {
			Property exampleEntity = new Property();
			exampleEntity.setCode(code);
			List<Property>  list = (List<Property>) propertyManager.findByExample(exampleEntity);
			if(id==null && list!=null && list.size()>0){
				errMsg = "同一个设备编号只能对应一条资产信息";
				return errMsg;
			}
			if(id!=null && list!=null && list.size()>0 && list.get(0).getId().longValue()!=id.longValue()){
				errMsg = "同一个设备编号只能对应一条资产信息";
				return errMsg;
			}
			
			
			exampleEntity = new Property();
			exampleEntity.setSerial_num(sn);
			list = (List<Property>) propertyManager.findByExample(exampleEntity);
			
			if(id==null && list!=null && list.size()>0){
				errMsg = "同一个序列号只能对应一条资产信息";
			}
			if(id!=null && list!=null && list.size()>0 && list.get(0).getId().longValue()!=id.longValue()){
				errMsg = "同一个序列号只能对应一条资产信息";
			}
			/*Integer count = propertyManager.countByExample(exampleEntity);
			if ((id == null && count != null && count > 0) || (id != null && count != null && count > 1)) {
				
			}*/
			
			/*exampleEntity = new Property();
			exampleEntity.sets
			Integer count = propertyManager.countByExample(exampleEntity);
			if ((id == null && count != null && count > 0) || (id != null && count != null && count > 1)) {
				errMsg = "同一个设备编号只能对应一条资产信息";
			}*/

		}
		return errMsg;
	}
	/**
	 * 组装jqGrid分页信息参数
	 * 
	 * @throws JSONException
	 * */
	public JSONObject getJsonPage(Page page) throws JSONException {

		JSONObject jsonPage = new JSONObject();
		jsonPage.put("page", page.getPageNo());
		jsonPage.put("total", page.getTotalPages());
		jsonPage.put("records", page.getTotalCount());
		return jsonPage;
	}
	
}
