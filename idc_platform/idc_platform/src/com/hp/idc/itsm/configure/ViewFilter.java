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
	
	//��������Ϸ�ʽ
	public static final int COMBINATION_MODE_AND = 1;//ȫ��
	public static final int COMBINATION_MODE_OR = 2;//ȫ��
	public static final int COMBINATION_MODE_OTHER = 3;//���������ʽ
	
	private int combination_mode = 1;
	
	private String combination_expression = "";
	
	//���ԭʼ�Ĺ�����������Ϣ
	private List<FilterInfo> filters = new ArrayList<FilterInfo>();
	
	//����Ѿ��滻��[�����]�Ĺ�����������Ϣ
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
		//��ȡ���������ʽ
		Element com_exp = (Element)filterEle.selectSingleNode("./combination_expression");
		if (com_exp!=null){
			combination_expression = com_exp.getText()==null?"":com_exp.getText();
			String com_mode = com_exp.attributeValue("combination_mode");
			if (com_mode!=null && !com_mode.equals(""))
				combination_mode = Integer.parseInt(com_mode);
		}
		//��ȡ��������Ϣ
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
		//����filters,
		tempFilters = new ArrayList<FilterInfo>();
		for (int j = 0; j < this.filters.size(); j++) {
			FilterInfo f = (FilterInfo)filters.get(j);
			FilterInfo cloneFilter = f.cloneFilterInfo();
			//�滻������
			String val2 = MacroManager.replaceMacro(cloneFilter.getValue(), operName);
			cloneFilter.setValue(val2);
			tempFilters.add(cloneFilter);
		}
	}
	
	/**
	 * �Թرյ���ʷ��¼���й���
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
			//����ʷ�رյĹ�����������ǻ����ֶΣ����õı���д�ֶΣ����б�codeת�������ģ��ٽ���ƥ��
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
			} else {//Ĭ����ȫ��
				if (!f.match(value, operName)) {
					return false;
				}
			}
			
		}
		boolean ret = true;
		
		if (combination_mode == COMBINATION_MODE_OR)
			ret = false;
		if(m.size()>0) {
	
			//������������ϱ��ʽ
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
			//���������õ��ܲ������д������й������û�
			else if (f.getFieldId().equals("task_wf_super_edit")){
				WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(obj.getOwner().getWfOid());
				value = wfInfo.getAnalyzedEditRule();
			} else if (f.getFieldId().equals("task_wf_handler")) {
				//�����ܴ������Ĵ�����
				if (obj.getStatus() != TaskData.STATUS_CLOSE) {
					//����ִ����
					String gu = obj.getAttribute(Consts.FLD_EXECUTE_GROUP_USER);
					value = gu;
					
					//���������õ��ܲ������д������й������û�
					WorkflowInfo wfInfo = WorkflowManager.getWorkflowByOid(obj.getOwner().getWfOid());
					String value1 = wfInfo.getAnalyzedEditRule();
					if (value1!=null && !value1.equals(""))
						value += value1;
					
					//��������������˶����Դ���ȡ������Ա
					if (wfInfo.isAllowLocalGroup()){
						value += obj.getPersonsOfLocalGroup();
					}
					
					//���ڴ򿪵Ľڵ㣬��������˼�Э����
					if (obj.getStatus() == TaskData.STATUS_OPEN) {
						//ȡ������
						String[] gus = gu.split("/");
						for (int i = 0; i < gus.length; i++) {
							if (gus[i] == null || gus[i].equals(""))
								continue;
							String value2 = PersonManager.getFactor(gus[i]);
							if (value2!=null && !value2.equals(""))
								value += value2;
						}
						
						//ȡЭ����
						if(obj.getAssistant()!=null && !obj.getAssistant().equals(""))
							value += obj.getAssistant().replaceAll("\\,", "/");
						if (!value.endsWith("/"))
							value += "/";
					}
				}
				
				//��֪��======���ڹرյĹ�����ֻȡ��֪��
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
			} else {//Ĭ����ȫ��
				if (!isMatch) {
					return false;
				}
			}
			
		}
		
		boolean ret = true;
		if (combination_mode == COMBINATION_MODE_OR)
			ret = false;
		//������������ϱ��ʽ
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
