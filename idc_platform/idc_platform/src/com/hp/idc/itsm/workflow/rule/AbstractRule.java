/**
 * 
 */
package com.hp.idc.itsm.workflow.rule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.idc.json.JSONException;
import com.hp.idc.json.JSONObject;

/**
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 *
 */
public abstract class AbstractRule implements IWorkflowRule {

	
	public static final String RULE_RUN_POSITION_ENTER = "enter";
	public static final String RULE_RUN_POSITION_UPDATE = "update";
	public static final String RULE_RUN_POSITION_UPDATE_END = "updateEnd";
	public static final String RULE_RUN_POSITION_OVERTIME = "overtime";
	public static final String RULE_RUN_POSITION_ROLLBACK = "rollback";
	public static final String RULE_RUN_POSITION_REALTIME = "realtime";
	
	protected Log logger = LogFactory.getLog(getClass());

	/**
	 * 规则运行位置
	 */
	private String runPosition;
	
	public void parse(JSONObject json) throws JSONException {
		if (json == null)
			return;
		if (json.has("runPosition"))
			runPosition = json.getString("runPosition");
	}
	
	/**
	 * @param runPosition the runPosition to set
	 */
	public void setRunPosition(String runPosition) {
		this.runPosition = runPosition;
	}

	/**
	 * @return the runPosition
	 */
	public String getRunPosition() {
		return runPosition;
	}

}
