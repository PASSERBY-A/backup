/**
 * 
 */
package com.hp.idc.itsm.task;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 * 
 */
public class TaskWaitingInfo {	
	
	private Log logger = LogFactory.getLog(getClass());

	/**
	 * 当工单处于等待状态时，此属性存储等待的工单OID
	 */
	protected int waiting_task_oid = -1;

	/**
	 * //等待结束的目标工单状态
	 * 当oid=waiting_task_oid的工单，状态为此时，则结束等待，继续往下执行
	 */
	protected String waiting_end_status = "";
	
	protected int wfOid;
	
	protected int wfVer;
	
	public boolean waiteEnd(){
		TaskInfo ti = null;
		try {
			ti = TaskManager.getTaskInfoByOid("ITSM", waiting_task_oid, wfOid, wfVer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (ti == null){
			logger.error("判读【等待状态】结束标志出错，无法获取目标工单(taskOid:"+waiting_task_oid+",wfOid:"+wfOid+",wfVer:"+wfOid+")");
		}
		if (ti.getStatus() != TaskInfo.STATUS_OPEN)
			return true;
		String[] waiteStatus = waiting_end_status.split("|");
		List<TaskData> tdl= ti.getTaskData();
		for (int i = 0; i < tdl.size(); i++) {
			TaskData _td = tdl.get(i);
			for (int j = 0; j < waiteStatus.length; j++){
				if (_td.getNodeDesc().equals(waiteStatus[j]))
					return true;
			}
		}
		
		return false;
	}
	

	public int getWaiting_task_oid() {
		return waiting_task_oid;
	}

	public void setWaiting_task_oid(int waiting_task_oid) {
		this.waiting_task_oid = waiting_task_oid;
	}

	public String getWaiting_end_status() {
		return waiting_end_status;
	}

	public void setWaiting_end_status(String waiting_end_status) {
		this.waiting_end_status = waiting_end_status;
	}


	public int getWfOid() {
		return wfOid;
	}


	public void setWfOid(int wfOid) {
		this.wfOid = wfOid;
	}


	public int getWfVer() {
		return wfVer;
	}


	public void setWfVer(int wfVer) {
		this.wfVer = wfVer;
	}
}
