/**
 * 
 */
package com.hp.idc.itsm.workflow.rule;

import com.hp.idc.itsm.task.TaskUpdateInfo;
import com.hp.idc.itsm.workflow.NodeInfo;

/**
 * ���̽ڵ��ϵĹ���ӿ�
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 *
 */
public interface IWorkflowRule {
	
	
	public Object execute(TaskUpdateInfo updateInfo, NodeInfo node, Object ... params) throws Exception;
	
}
