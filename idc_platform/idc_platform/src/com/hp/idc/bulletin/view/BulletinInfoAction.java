package com.hp.idc.bulletin.view;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hp.idc.bulletin.entity.BulletinInfo;
import com.hp.idc.bulletin.service.BulletingService;
import com.hp.idc.bulletin.util.DateTimeUtil;
import com.hp.idc.cas.auc.PersonInfo;
import com.hp.idc.cas.auc.PersonManager;
import com.hp.idc.common.core.bo.Page;
import com.hp.idc.common.core.view.AbstractAction;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class BulletinInfoAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7321016931741612883L;

	
    private BulletinInfo bulletinInfo;
    
    private String title;
    
    @Resource
    private BulletingService bulletinService;
	
	private String successMessage;
	
	private String bulletinId;
	
	private String beginTime;
	
	private String endTime;
	
	private String createDate;
	
	public String preAddBulletinInfo(){
		
		return SUCCESS;
	}
	
	public String addBulletinInfo()
	{
		try{
			bulletinInfo.setBeginTime(DateTimeUtil.getDataByString(beginTime));
			bulletinInfo.setEndTime(DateTimeUtil.getDataByString(endTime));
			bulletinInfo.setCreator(getLoginUserId());
			bulletinService.addBulletinInfo(bulletinInfo);	
		    setJSONResponse( true);
		}catch(Exception e){
			setJSONResponse( false);
		}
		return SUCCESS;
	}

	public String updateBulletinInfo()
	{
		try{
			bulletinInfo.setBeginTime(DateTimeUtil.getDataByString(beginTime));
			bulletinInfo.setEndTime(DateTimeUtil.getDataByString(endTime));
			bulletinInfo.setCreatedDate(DateTimeUtil.getDataByString(createDate));
			bulletinService.updateBulletinInfo(bulletinInfo);	
		    setJSONResponse( true);
		}catch(Exception e){
			setJSONResponse( false);
		}
		return SUCCESS;
	}
	
	public String queryBulletinInfo()
	{
		Map<String, Object> paramMap=new HashMap<String, Object>();
		System.out.println("title----> "+title);
		LinkedHashMap<String,String> sortMap=new LinkedHashMap<String,String>();
		sortMap.put("id", "desc");
		if(title!=null&&!"".equals(title))paramMap.put("title", title);
		Page<BulletinInfo> page = bulletinService.getBulletinList(paramMap,sortMap, start/limit+1, limit);
        jsonObject = new JSONObject();
        jsonArray=new JSONArray();
        List<BulletinInfo> list=page.getResult();
        if(list.size()>0){
			for(BulletinInfo model:list){
				JSONObject json=new JSONObject();
				json.put("id", model.getId());
				json.put("title", model.getTitle());
				json.put("content", model.getContent());
				json.put("begin_time", DateTimeUtil.formatDate(model.getBeginTime(),DateTimeUtil.DATE_yyyy_MM_dd));
				json.put("end_time", DateTimeUtil.formatDate(model.getEndTime(),DateTimeUtil.DATE_yyyy_MM_dd));
				json.put("created_time",DateTimeUtil.formatDate(model.getCreatedDate(),DateTimeUtil.DATE_yyyy_MM_dd));
				json.put("creator", model.getCreator());	
				if(model.getCreator()!=null)json.put("creatorName", PersonManager.getPersonById(model.getCreator()).getName());
				jsonArray.add(json);
			}
		}

        jsonObject.put("totalCount", page.getTotalSize());
		jsonObject.put("result", jsonArray);
		logDebug("json:"+jsonObject.toString());
		System.out.println(jsonObject.toString());
		return SUCCESS;
	}
	
	
	public String getBulletinInfoDetail()
	{
		
		bulletinInfo = bulletinService.getBulletinInfo(Long.parseLong(bulletinId));
		JSONObject json=JSONObject.fromObject(bulletinInfo);
		if(bulletinInfo.getCreator()!=null){
			PersonInfo pi = PersonManager.getPersonById(bulletinInfo.getCreator());
			bulletinInfo.setCreator(pi.getName());
			json.put("creator", pi.getName());
		}
		jsonObject=new JSONObject();
		jsonObject.put(JSON_RET_SUCCESS, true);
		jsonObject.put(JSON_RET_MESSAGE, json);
		
		return SUCCESS;
	}
	
	public void deleteBulletinInfo()
	{
		try
		{
		  bulletinService.deleteBulletinInfoBySql(Long.parseLong(bulletinId));
		  sendMsg("success");
		  
		  setJSONResponse(true);
		}
		catch(Exception e)
		{
			try {
				sendMsg("fail");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			setJSONResponse(false,e.getMessage());
		}
		
	}
	
	
	public BulletinInfo getBulletinInfo() {
		return bulletinInfo;
	}

	public void setBulletinInfo(BulletinInfo bulletinInfo) {
		this.bulletinInfo = bulletinInfo;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}


	public BulletingService getBulletinService() {
		return bulletinService;
	}


	public void setBulletinService(BulletingService bulletinService) {
		this.bulletinService = bulletinService;
	}


	public String getBulletinId() {
		return bulletinId;
	}


	public void setBulletinId(String bulletinId) {
		this.bulletinId = bulletinId;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getBeginTime() {
		return beginTime;
	}


	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}


	public String getEndTime() {
		return endTime;
	}


	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	
}
