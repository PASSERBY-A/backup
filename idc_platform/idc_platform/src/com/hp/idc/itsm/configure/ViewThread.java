package com.hp.idc.itsm.configure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.itsm.task.TaskData;
import com.hp.idc.itsm.task.TaskInfo;
import com.hp.idc.itsm.workflow.WorkflowManager;

/**
 * 处理过滤工单的线程类
 * @author FluteD
 *
 */
public class ViewThread extends Thread{

	public int b = 0;
	public int e = 0;
	protected ViewFilter filter = null;
	private String operName = "";

	private List<Map<String,String>> retList = new ArrayList<Map<String,String>>();
	private List<TaskInfo> taskList = new ArrayList<TaskInfo>();
	
	//对于关闭的工单，排除ID一样的（有分支的时候，分支回另开记录，会有工单ID的情况）
	private Map<String,Map<String,String>> tempMap = new HashMap<String,Map<String,String>>();

	public ViewThread(){
		
	}

	public ViewThread(List<TaskInfo> taskList,ViewFilter filter,String operName,int begin,int end){
		this.taskList = taskList;
		this.filter = filter;
		this.operName = operName;
		this.b = begin;
		this.e = end;
	}
	
	public void run(){
		//System.out.println(this.getId()+"--b:"+b+","+e+","+System.currentTimeMillis());
		for (int i = b; i < e; i++) {
			TaskInfo info = (TaskInfo)taskList.get(i);
			if (info.getStatus() == TaskInfo.STATUS_OPEN) {
				List taskDataL = info.getTaskData();
				for (int j  = 0; j < taskDataL.size(); j++) {
					TaskData tempTD_ = (TaskData)taskDataL.get(j);
					if (filter.applyFilter(tempTD_,operName))
						retList.add(tempTD_.getAllData());
				}
//				if (applyFilter(info,operName)) {
//					tempMap_.put(info.getOrigin()+"_"+info.getOid(), info.getValues());
//				}
			} else {
				Map<String,String> valuesMap = info.getValues();
				if (filter.applyFilter(valuesMap,operName)) {
					if (info.getParentOid()!=-1) {
						//if (tempMap_.get(info.getOrigin()+"_"+info.getParentOid()) == null) {
						//	TaskInfo parentInfo = TaskManager.getTaskInfoByOid(info.getOrigin(), info.getParentOid(),info.getWfOid());
						valuesMap.put("TASK_OID", info.getParentOid()+"");
						valuesMap.put("OID", info.getParentOid()+"");
						if (info.getPwfOid()!=-1){
							valuesMap.put("TASK_WF_OID", info.getPwfOid()+"");
							valuesMap.put("WORKFLOW", WorkflowManager.getWorkflowByOid(info.getPwfOid()).getName());
							valuesMap.put("TASK_WF_NAME", WorkflowManager.getWorkflowByOid(info.getPwfOid()).getName());
						}
						tempMap.put(info.getOrigin()+"_"+info.getParentOid(), valuesMap);
						//}
					} else
						tempMap.put(info.getOrigin()+"_"+info.getOid(), valuesMap);
				}
			}
		}
		//System.out.println(this.getId()+"--e:"+System.currentTimeMillis());

	}
	public List<Map<String, String>> getRetList() {
		return retList;
	}

	public void setRetList(List<Map<String, String>> retList) {
		this.retList = retList;
	}

	public List<TaskInfo> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<TaskInfo> taskList) {
		this.taskList = taskList;
	}
	
	public ViewFilter getFilter() {
		return filter;
	}
	public void setFilter(ViewFilter filter) {
		this.filter = filter;
	}
	
	public Map<String, Map<String, String>> getTempMap() {
		return tempMap;
	}
	public void setTempMap(Map<String, Map<String, String>> tempMap) {
		this.tempMap = tempMap;
	}

	public static void main(String[] args){
		for (int i = 0; i < 10; i ++) {
			System.out.println("kaishi :"+i);
			ViewThread f = new ViewThread();
			f.b = 1;
			f.e = 10;
			f.start();
		}
	}
}
