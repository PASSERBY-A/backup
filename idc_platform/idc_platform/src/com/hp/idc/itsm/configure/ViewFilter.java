package com.hp.idc.itsm.configure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.common.MacroManager;
import com.hp.idc.itsm.security.PersonManager;
import com.hp.idc.itsm.task.TaskData;
import com.hp.idc.itsm.util.ExpressionCalculate;
import com.hp.idc.itsm.workflow.WorkflowInfo;
import com.hp.idc.itsm.workflow.WorkflowManager;

public class ViewFilter {
	
	//过滤器组合方式
	public static final int COMBINATION_MODE_AND = 1;//全与
	public static final int COMBINATION_MODE_OR = 2;//全或
	public static final int COMBINATION_MODE_OTHER = 3;//其他，表达式
	
	private int combination_mode = 1;
	
	private String combination_expression = "";
	
	//存放原始的过滤类配置信息
	private List<FilterInfo> filters = new ArrayList<FilterInfo>();
	
	//存放已经替换过[宏变量]的过滤类配置信息
	private List<FilterInfo> tempFilters = new ArrayList<FilterInfo>(); 

	public int getCombination_mode() {
		return combination_mode;
	}

	public void setCombination_mode(int combination_mode) {
		this.combination_mode = combination_mode;
	}

	public List getFilters() {
		return filters;
	}
	
	public void addFilter(FilterInfo fInfo){
		filters.add(fInfo);
	}

	public void setFilters(List filters) {
		this.filters = filters;
	}
	
	public String getCombination_expression() {
		return combination_expression == null?"":combination_expression;
	}

	public void setCombination_expression(String combination_expression) {
		this.combination_expression = combination_expression;
	}
	
	public void parse(Element filterEle){
		//读取过滤器表达式
		Element com_exp = (Element)filterEle.selectSingleNode("./combination_expression");
		if (com_exp!=null){
			combination_expression = com_exp.getText()==null?"":com_exp.getText();
			String com_mode = com_exp.attributeValue("combination_mode");
			if (com_mode!=null && !com_mode.equals(""))
				combination_mode = Integer.parseInt(com_mode);
		}
		//读取过滤器信息
		List filters = filterEle.selectNodes("./filter");
		for (int i = 0; i < filters.size(); i++) {
			Element filter_ele = (Element)filters.get(i);
			FilterInfo filter_ = new FilterInfo();
			filter_.setFieldId(filter_ele.attributeValue("field"));
			filter_.setRelation(filter_ele.attributeValue("relation"));
			filter_.setValue(filter_ele.getText());
			String origin = filter_ele.attributeValue("origin");
			filter_.setOrigin((origin==null || origin.equals(""))?"ITSM":origin);
			
			//if (filter==null) {
			//	filter = new ArrayList();
			//}
			this.addFilter(filter_);
		}
	}
	
	public void dealMacro(String operName) {
		//处理filters,
		tempFilters = new ArrayList<FilterInfo>();
		for (int j = 0; j < this.filters.size(); j++) {
			FilterInfo f = (FilterInfo)filters.get(j);
			FilterInfo cloneFilter = f.cloneFilterInfo();
			//替换其宏变量
			String val2 = MacroManager.replaceMacro(cloneFilter.getValue(), operName);
			cloneFilter.setValue(val2);
			tempFilters.add(cloneFilter);
		}
	}
	
	/**
	 * 对关闭的历史记录进行过滤
	 * @param obj
	 * @param operName
	 * @return
	 */
	public boolean applyFilter(Map<String,String> valuesMap, String operName) {
		Map m = new HashMap();
		for (int j = 0; j < this.tempFilters.size(); j++) {
			FilterInfo f = tempFilters.get(j);
			FieldInfo fi = FieldManager.getFieldById(f.getFieldId());
			String fieldId = f.getFieldId();
			if (fi!=null && fi.isSystem())
				fieldId = fieldId.toUpperCase();
			String value = valuesMap.get(fieldId);
			//FieldInfo field = FieldManager.getFieldById(f.getOrigin(),f.getFieldId());
			//WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(taskInfo.getWfOid());
			//Map columnFields = wfInfo.getColumnFieldsMap();
			//String orgValue = f.getValue(); 
			//对历史关闭的工单，如果不是基本字段（配置的表单填写字段）把列表code转化成中文，再进行匹配
//			if (columnFields.get(f.getFieldId().toUpperCase())!=null)
//				orgValue = field.getHtmlCode(orgValue==null?"":orgValue);
//			f.setValue(orgValue);
			value = value==null?"":value;
			if (combination_mode == COMBINATION_MODE_OTHER){
				if (combination_expression == null || combination_expression.equals("")){
					if (!f.match(value, operName)) {
						return false;
					}
				} else
					m.put(f.getFieldId(), f.match(value, operName)+"");
			} else if (combination_mode == COMBINATION_MODE_OR){
				if (f.match(value, operName)) {
					return true;
				}
			} else {//默认是全与
				if (!f.match(value, operName)) {
					return false;
				}
			}
			
		}
		boolean ret = true;
		
		if (combination_mode == COMBINATION_MODE_OR)
			ret = false;
		if(m.size()>0) {
	
			//过滤条件的组合表达式
			if (combination_mode == COMBINATION_MODE_OTHER){
				if (combination_expression != null && !combination_expression.equals("")){
					try {
						ret = ExpressionCalculate.calculateBoolean(m, combination_expression);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return ret;
	}
	
	public boolean applyFilter(TaskData obj, String operName) {
		Map m = new HashMap();

		for (int j = 0; j < tempFilters.size(); j++) {
			FilterInfo f = tempFilters.get(j);
			String value = "";
			
			boolean isMatch = true;
			if(f.getFieldId().equals("task_data_status")){
				value = ""+obj.getStatus();
			}
			//流程中配置的能操作所有此流程中工单的用户
			else if (f.getFieldId().equals("task_wf_super_edit")){
				WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(obj.getOwner().getWfOid());
				value = wfInfo.getAnalyzedEditRule();
			} else if (f.getFieldId().equals("task_wf_handler")) {
				//所有能处理工单的处理人
				if (obj.getStatus() != TaskData.STATUS_CLOSE) {
					//工单执行人
					String gu = obj.getAttribute(Consts.FLD_EXECUTE_GROUP_USER);
					value = gu;
					
					//流程中配置的能操作所有此流程中工单的用户
					WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(obj.getOwner().getWfOid());
					String value1 = wfInfo.getAnalyzedEditRule();
					if (value1!=null && !value1.equals(""))
						value += value1;
					
					//如果允许本组内所人都可以处理，取本组人员
					if (wfInfo.isAllowLocalGroup()){
						value += obj.getPersonsOfLocalGroup();
					}
					
					//对于打开的节点，允许代理人及协助人
					if (obj.getStatus() == TaskData.STATUS_OPEN) {
						//取代理人
						String[] gus = gu.split("/");
						for (int i = 0; i < gus.length; i++) {
							if (gus[i] == null || gus[i].equals(""))
								continue;
							String value2 = PersonManager.getFactor(gus[i]);
							if (value2!=null && !value2.equals(""))
								value += value2;
						}
						
						//取协助人
						if(obj.getAssistant()!=null && !obj.getAssistant().equals(""))
							value += obj.getAssistant().replaceAll("\\,", "/");
						if (!value.endsWith("/"))
							value += "/";
					}
				}
				
				//阅知人======对于关闭的工单，只取阅知人
				if (obj.getReadUser()!=null && !obj.getReadUser().equals("")){
					String v_ = "/"+obj.getReadUser().replaceAll("\\,", "/")+"/";
					if (obj.getReadMessages().get(operName)!=null)
						v_ = v_.replaceAll("/"+operName+"/", "/");
					value += v_;
				}
			} else if (f.getFieldId().equals(Consts.FLD_READ_USER)){
				if (obj.getReadUser()!=null && !obj.getReadUser().equals(""))
					value = "/"+obj.getReadUser().replaceAll("\\,", "/")+"/";
				if (obj.getReadMessages().get(operName)!=null)
					value = value.replaceAll("/"+operName+"/", "/");
			} else {
				value = obj.getAttribute(f.getFieldId());
			}
			isMatch = f.match(value, operName);
			
			value = value==null?"":value;
			if (combination_mode == COMBINATION_MODE_OTHER){
				if (combination_expression == null || combination_expression.equals("")){
					if (!isMatch) {
						return false;
					}
				} else {
					m.put(f.getFieldId(), isMatch+"");
				}
			} else if (combination_mode == COMBINATION_MODE_OR){
				if (isMatch) {
					return true;
				}
			} else {//默认是全与
				if (!isMatch) {
					return false;
				}
			}
			
		}
		
		boolean ret = true;
		if (combination_mode == COMBINATION_MODE_OR)
			ret = false;
		//过滤条件的组合表达式
		if(m.size()>0) {
			if (combination_mode == COMBINATION_MODE_OTHER){
				if (combination_expression != null && !combination_expression.equals("")){
					try {
						ret = ExpressionCalculate.calculateBoolean(m, combination_expression);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return ret;
	}
}
