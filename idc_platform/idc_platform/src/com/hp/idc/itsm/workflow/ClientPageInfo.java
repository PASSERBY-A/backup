/**
 * 
 */
package com.hp.idc.itsm.workflow;

import org.dom4j.Element;

import com.hp.idc.itsm.configure.FormInfo;
import com.hp.idc.itsm.configure.FormManager;

/**
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 * 
 */
public class ClientPageInfo {

	/**
	 * չʾҳ��ģʽ��Զ��ģʽ��
	 */
	public static final String MODE_REMOTE = "remote";
	
	/**
	 * չʾҳ��ģʽ������ģʽ��
	 */
	public static final String MODE_LOCAL = "local";

	/**
	 * չʾҳ��ģʽ
	 */
	private String mode;
	
	/**
	 * Զ��չʾҳ�棨��mode=ClientPageInfo.MODE_REMOTEʱ��Ч��
	 */
	private String remotePage;
	
	/**
	 * ���ر����󣨵�mode=ClientPageInfo.MODE_LOCALʱ��Ч��
	 */
	private FormInfo localForm;
	
	/**
	 * ���ر�չʾģ�壨��mode=ClientPageInfo.MODE_LOCALʱ��Ч��
	 */
	private String localTemplate;
	
	public void parse(Element node){
		 mode = node.attributeValue("mode");
		 Element el = (Element)node.selectSingleNode("./local");
		 String localFormId = el.attributeValue("fromId");
		 localForm = FormManager.getFormByOid(Integer.parseInt(localFormId));
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getRemotePage() {
		return remotePage;
	}

	public void setRemotePage(String remotePage) {
		this.remotePage = remotePage;
	}

	public FormInfo getLocalForm() {
		return localForm;
	}

	public void setLocalForm(FormInfo localForm) {
		this.localForm = localForm;
	}

	public String getLocalTemplate() {
		return localTemplate;
	}

	public void setLocalTemplate(String localTemplate) {
		this.localTemplate = localTemplate;
	}

}
