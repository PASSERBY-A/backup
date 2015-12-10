/**
 * Linkage 2010
 */
package com.hp.idc.itsm.task.listener;

import java.util.List;

import com.hp.idc.itsm.impl.ITSMTaskManagerImpl;
import com.hp.idc.itsm.task.Event;
import com.hp.idc.itsm.task.TaskData;
import com.hp.idc.itsm.task.TaskEvent;
import com.hp.idc.itsm.task.TaskInfo;
import com.hp.idc.itsm.workflow.NodeInfo;
import com.hp.idc.itsm.workflow.WorkflowData;
import com.hp.idc.itsm.workflow.WorkflowInfo;
import com.hp.idc.itsm.workflow.WorkflowManager;

/**
 * @author Forrest
 * Sep 1, 2010 11:10:17 AM
 *
 */
public class QueueListener extends TaskListener {
	
	private ITSMTaskManagerImpl itmi = new ITSMTaskManagerImpl();

	/* (non-Javadoc)
	 * @see com.hp.idc.itsm.task.listener.TaskListener#accept(com.hp.idc.itsm.task.Event)
	 */
	@Override
	public boolean accept(Event event) {
		if (event instanceof TaskEvent) {
			TaskEvent e = (TaskEvent)event;
			if (e.getOperUser().equals("system"))   //remove the check over time task
				return false;
			TaskInfo taskInfo = e.getTaskInfo();
			TaskData td = taskInfo.getTaskData(e.getStartDataId());
			WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(taskInfo.getWfOid());
			WorkflowData wfData = wfInfo.getVersion(taskInfo.getWfVer());
			NodeInfo nodeInfo = wfData.getNode(td.getNodeId());
			boolean ret = itmi.checkWait(taskInfo.getWfOid(), nodeInfo, e.getOperUser());
			return !ret;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.hp.idc.itsm.task.listener.TaskListener#handleEvent(com.hp.idc.itsm.task.Event)
	 */
	@Override
	public void handleEvent(Event event) throws Exception {
		TaskEvent e = (TaskEvent) event;
		TaskInfo ti = e.getTaskInfo();
		List<TaskData> l = itmi.getWaitTaskData(ti.getWfOid(), ti.getTaskData(e.getStartDataId()).getNodeId(), e.getOperUser());
		if (l.size() > 0) {
			TaskData td = l.get(l.size()-1);
			TaskInfo tinfo = td.getOwner();
			td.setWait(false);
			//update the database the node attribute 'wait' to false
			new DBListener().updateTaskInfo(tinfo, e.getOperUser());
			//update the memory
			taskFactory.updateCache(tinfo);
		}				
	}
}
