/**
 * 
 */
package com.hp.idc.itsm.workflow.rule;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.hp.idc.itsm.task.TaskData;
import com.hp.idc.itsm.task.TaskInfo;
import com.hp.idc.itsm.task.TaskUpdateInfo;
import com.hp.idc.itsm.workflow.NodeInfo;
import com.hp.idc.json.JSONException;
import com.hp.idc.json.JSONObject;

/**
 * 动态代码处理规则
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 *
 */
public class DynamicCodeRule extends AbstractRule {
	
	public static final String METHOD_NAME_ONENTER = "onEnter";
	public static final String METHOD_NAME_ONUPDATE = "onUpdate";
	public static final String METHOD_NAME_ONUPDATEEND = "onUpdateEnd";
	public static final String METHOD_NAME_ONROLLBACK = "onRollback";
	public static final String METHOD_NAME_ONOVERTIME = "onOvertime";
	public static final String METHOD_NAME_ONREALTIME = "onRealtime";
//
//	private String methodName = "";
//	
//	private String className = "";
	
	private boolean runEnter = false;
	private boolean runUpdate = false;
	private boolean runUpdateEnd = false;
	private boolean runRollback = false;
	private boolean runOvertime = false;
	private boolean runRealtime = false;

	/* (non-Javadoc)
	 * @see com.hp.idc.itsm.workflow.rule.AbstractRule#parse(org.dom4j.Element)
	 */
	public void parse(JSONObject json) throws JSONException {
		super.parse(json);
		if (json.has("runEnter"))
			this.runEnter = json.getBoolean("runEnter");
		if (json.has("runUpdate"))
			this.runUpdate = json.getBoolean("runUpdate");
		if (json.has("runUpdateEnd"))
			this.runUpdateEnd = json.getBoolean("runUpdateEnd");
		if (json.has("runRollback"))
			this.runRollback = json.getBoolean("runRollback");
		if (json.has("runOvertime"))
			this.runOvertime = json.getBoolean("runOvertime");
		if (json.has("runRealtime"))
			this.runRealtime = json.getBoolean("runRealtime");
	}
	
	/* 
	 * params[0]:方法名（onEnter\onUpdate\onUpdateEnd\onRollback\onOvertime\onRealtime）
	 * 
	 */
	public Object execute(TaskUpdateInfo updateInfo, NodeInfo node, Object ... params) throws Exception {

		if (params[0].equals(METHOD_NAME_ONENTER)) {
			if (!this.runEnter)
				return null;
		} else if (params[0].equals(METHOD_NAME_ONUPDATE)){
			if (!this.runUpdate)
				return null;
		} else if (params[0].equals(METHOD_NAME_ONUPDATEEND)){
			if (!this.runUpdateEnd)
				return null;
		} else if (params[0].equals(METHOD_NAME_ONROLLBACK)){
			if (!this.runRollback)
				return null;
		} else if (params[0].equals(METHOD_NAME_ONOVERTIME)){
			if (!this.runOvertime)
				return null;
		} else if (params[0].equals(METHOD_NAME_ONREALTIME)){
			if (!this.runRealtime)
				return null;
		}
		com.hp.idc.itsm.runtime.Runtime rt = new com.hp.idc.itsm.runtime.Runtime();
		Class<?> c;
		Method m = null;
		try {
			c = rt.findClass(node.getClassName());
			Method[] ms = c.getMethods();
			for (Method m_ : ms) {
				if (m_.getName().equals(params[0])) {
					m = m_;
					break;
				}
			}
		} catch (Exception e) {
			return null;
		}
		if (m == null)
			return null;
		try {
			Object[] methodParams = new Object[m.getParameterTypes().length];
			Class<?>[] paraTypes = m.getParameterTypes();
			for (int i = 0; i < paraTypes.length; i++){
				if (paraTypes[i] == TaskUpdateInfo.class)
					methodParams[i] = updateInfo;
				else if (paraTypes[i] == TaskData.class)
					methodParams[i] = updateInfo.getTaskData();
				else if (paraTypes[i] == TaskInfo.class)
					methodParams[i] = updateInfo.getTaskInfo();
				else if (paraTypes[i] == String.class)
					methodParams[i] = updateInfo.getOperName();
				else 
					methodParams[i] = params;
			}
			m.invoke(c.newInstance(), methodParams);
		} catch (InvocationTargetException e) {
			Throwable t = e.getCause();
			if (t instanceof Exception)
				throw (Exception)t;
			throw e;
		}

		return null;
	}

}
