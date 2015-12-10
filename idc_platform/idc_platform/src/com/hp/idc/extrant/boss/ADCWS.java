package com.hp.idc.extrant.boss;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import cn.jnz.encryption.Des;
import cn.jnz.encryption.MD5;
import cn.jnz.encryption.Secret;

import com.hp.idc.ADC.service.CusCollect;
import com.hp.idc.context.util.ContextUtil;
import com.hp.idc.cusrelation.entity.CusManageContact;
import com.hp.idc.cusrelation.entity.CusManageInfo;
import com.hp.idc.cusrelation.entity.CusManageServant;
import com.hp.idc.itsm.util.XmlUtil;
/**
 * 
 * ADC Web Service �ӿ�
 * 
 * @author <a href="mailto:ruiw@hp.com">Wang Rui</a>
 * @version 1.0, ����4:57:30 2011-9-7
 *
 */
public class ADCWS {

	private static Des des = new Des(); 
	private static Secret secret = new Secret();  
	private static MD5 md5 = new MD5(); 
	
	private static String[] errorCode = {"0", "�ɹ���"};
	
	//�ܳ�
	private static String DesIV = "12345678";
	
	//�ܳ�
	private static String Deskey = "12345678";
	
	//����ָ��  Ϊ��Ч�鱨��������
	private static String md5Key = "";
	
	//body ����ת��ΪMap
	private static Map<String, String> contextMap = new HashMap<String, String>();
	
	//head ����ת��ΪMap
	private static Map<String, String> headMap = new LinkedHashMap<String, String>();
	
	
	/**
	 * ���ܱ�����Ϣ
	 * 
	 * @param bodyDES ���ܵ�����
	 * @return ���ܺ�ı�����Ϣ
	 * @throws Exception 
	 */
	private static String decrypt(String bodyDES) throws Exception {
		String body = des.decrypt(bodyDES, Deskey, DesIV);
		return body;
	}
	
	/**
	 * ���ܱ�������
	 * 
	 * @param bodyXml
	 * @return
	 * @throws Exception
	 */
	private static String encrypt(String bodyXml) throws Exception{
		String strDES = des.encrypt(bodyXml, Deskey,DesIV); 
		return strDES;
	}
	
	/**
	 * �������ص�xml ����
	 * 
	 * @return
	 * @throws Exception 
	 */
	private static String getStaffBindRspDocument(Map<String, String> feedbackMap) {
		DocumentFactory df = DocumentFactory.getInstance();
		Document document = DocumentHelper.createDocument();
		
		Document documentbody = DocumentHelper.createDocument();
		documentbody =df.createDocument("UTF-8");
		Element bodyroot = documentbody.addElement("BODY");
		Element ECID = bodyroot.addElement("ECID");
		ECID.setText(contextMap.get("ECID"));
		Element STAFFS = bodyroot.addElement("STAFFS");
		Element STAFF = STAFFS.addElement("STAFF");
		if(feedbackMap.containsKey("UID")){
			STAFF.addAttribute("UID",feedbackMap.get("UID"));
		}
		STAFF.addAttribute("RESULTCODE", feedbackMap.get("RESULTCODE"));
		STAFF.addAttribute("RESULTMSG", feedbackMap.get("RESULTMSG"));
		String context = "";
		try {
			String content = documentbody.asXML();
			String strMD5 = md5.encrypt(content);
			headMap.put("KEY", strMD5);
			context = encrypt(content);
		} catch (Exception e) {
		}
		
		document =df.createDocument("UTF-8");
		// ����һ���ӵ�
		Element root = document.addElement("StaffBindReq");
		Element head = root.addElement("HEAD");
		// ����head��һ���ӵ�
		addElementByMap(head, headMap);
		Element body = root.addElement("BODY");
		body.addText(context);
		
		return document.asXML();
		
	}
	
    /**
     * �������ص�xml ����
     * 
     * @return
     */
	private static String getCorpBindReqRspDocument(Map<String, String> feedbackMap) {
		DocumentFactory df = DocumentFactory.getInstance();
		Document document = DocumentHelper.createDocument();
		feedbackMap.put("URL", "");
		document =df.createDocument("UTF-8");
		// ����һ���ӵ�
		Element root = document.addElement("CorpBindReq");
		Element head = root.addElement("HEAD");
		//���ɼ�������
		String bodyContext = getDocumentXML(createFeedbackBody(feedbackMap));
		String  context= "";
		try {
			String strMD5 = md5.encrypt(bodyContext);
			headMap.put("KEY", strMD5);
			context = encrypt(bodyContext);
		} catch (Exception e) {
		}
		// ����head��һ���ӵ�
		addElementByMap(head, headMap);
		Element body = root.addElement("BODY");
		body.setText(context);
		
		return getDocumentXML(document);
	}
	
	/**
	 * ��mapת����element����ȥ
	 * 
	 * @param element �ڵ�
	 * @param contextMap map
	 */
	private static void addElementByMap(Element element, Map<String, String> contextMap) {
		for (String key : contextMap.keySet()) {
			Element headElement = element.addElement(key);
			headElement.addText(contextMap.get(key));
		}
	}
	
	/**
	 * ��������xml��Ϊ����body
	 * 
	 * @return
	 */
	private static Document createFeedbackBody(Map<String, String> feedbackMap){
//		DocumentFactory df = DocumentFactory.getInstance();
		Document document = DocumentHelper.createDocument();
		Element body = document.addElement("BODY");
		addElementByMap(body, feedbackMap);
		return document;
	}
	
	 /**  
     * ȡ��  
     * @param document ����Ҫд�������  
     */  
	private static String getDocumentXML(Document document) {
		String s = "";
		try {
			// �����ʽ����
			OutputFormat format = new OutputFormat("  ", true);
			// ���ñ���
			format.setEncoding("UTF-8");
			format.setXHTML(true);
			// xml�����
			StringWriter out = new StringWriter();
			XMLWriter xmlWriter = new XMLWriter(out, format);
			// ��ӡdoc
			xmlWriter.write(document);
			xmlWriter.flush();
			// �ر����������������printWriter
			s = out.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	 
	 /**
	  * ����head
	  * 
	  * @param root
	  */
	@SuppressWarnings("rawtypes")
	private static void dealHead(Element root) {
		for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {
			Element e = (Element) iterator.next();
			if (e.getName().equalsIgnoreCase("HEAD")) {
				getDeskey(e);
			}
		}
	}
	 
	/**
	 * ȡ�ñ���key
	 * 
	 * @param element head�ڵ�
	 */
	@SuppressWarnings("rawtypes")
	private static void getDeskey(Element element) {
		for (Iterator iterator = element.elementIterator(); iterator.hasNext();) {
			Element e = (Element) iterator.next();
			headMap.put(e.getName(), e.getTextTrim());
			if (e.getName().equalsIgnoreCase("KEY")) {
				String key = e.getTextTrim();
				if(!"".equals(key)){
					md5Key = key;
				}
			}
			if (e.getName().equalsIgnoreCase("SERVICEID")) {
				String serviceID = e.getTextTrim();
				if(!"".equals(serviceID)){
					contextMap.put("SERVICEID", serviceID);
				}
			}
		}
	}
	
	
	/**
	 * ����body
	 * 
	 * @param root
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	private static void dealBody(Element root) throws Exception {
		for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {
			Element e = (Element) iterator.next();
			if (e.getName().equalsIgnoreCase("BODY")) {
				String context = e.getText();
				//ͨ������ָ��Ч�� ���ĵ�������
				if (!"".equals(md5Key) && !secret.judgeDes(context, md5Key, Deskey, Deskey)) {
					errorCode[0] = "2101";
					errorCode[1] = "���ݷǷ�����";
					throw new Exception();
				}
				Document d = DocumentHelper.createDocument();
				try {
					context = decrypt(context);
				} catch (Exception e2) {
					errorCode[0] = "2101";
					errorCode[1] = "���ݷǷ�����";
					throw new Exception();
				}
//				context = "<BODY>"+ context + "</BODY>";
//				System.out.println("context: "+context);
				try {
					d = DocumentHelper.parseText(context);
				} catch (DocumentException e1) {
					errorCode[0] = "2102";
					errorCode[1] = "�������ݴ���";
					throw new Exception();
				}
				xmlToMap(d.getRootElement());
			}
		}
	}
	
	
	/**
	 * ����xml��Map
	 * 
	 * @param element
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Element xmlToMap(Element element) {
		if (element != null) {
			List list = element.elements();
			if (list.size() > 0) {
				for (Iterator iterator = element.elementIterator(); iterator
						.hasNext();) {
					Element e = (Element) iterator.next();
					xmlToMap(e);
				}
			} else {
				if (!"".equals(element.getTextTrim())) {
					contextMap.put(element.getName(), element.getTextTrim());
				} else {
					List<Attribute> attributeList = element.attributes();
					if (attributeList.size() > 0) {
						if (element.getName().equals("CORPINFOMAP"))
						contextMap.put(element.attribute("ECINFONAME")
								.getValue(), element.attribute("ECINFOVALUE")
								.getValue());
						if (element.getName().equals("ORDERPOINTMAP")) {
							if (!"".equals(element.attribute("POINTNAME")
									.getValue()))
								contextMap.put("POINTNAME",element.attribute("POINTNAME")
										.getValue());
								contextMap.put("POINTVALUE",
										element.attribute("POINTVALUE")
												.getValue());
						}
						if (element.getName().equals("USERINFOMAP"))
							contextMap.put(element.attribute("USERINFONAME")
									.getValue(), element.attribute("USERINFOVALUE")
									.getValue());
					} else {
						contextMap.put(element.getName(), element.asXML());
					}
				}
				return null;
			}
		}
		return null;
	}
	 
	/**
	 * ��ҵ��Ϣͬ���ӿ�
	 * 
	 * @param xml ����
	 * @return ��ִ����
	 */
	public static String CorpBind(String xml) {
		Map<String, String> feedbackMap = new LinkedHashMap<String, String>();
		if (!"".equals(xml)) {
		try {
//			System.out.println("CorpBind ���ģ� "+xml);
			Document doc = XmlUtil.parseString(xml);
			Element root = doc.getRootElement();
			dealHead(root);
			dealBody(root);
//			System.out.println("���� map�� "+contextMap);
			CusCollect cusCollect = (CusCollect) ContextUtil
					.getBean("cusCollect");
			CusManageInfo cusInfo = cusCollect.getCusInfo(contextMap);
			cusInfo.setMajorContact(contextMap.get("CORP_LINKMAN"));
			cusInfo.setLonginName(contextMap.get("ECNAME"));
			CusManageServant cusServant= cusCollect.getCusServant(contextMap);
			if (contextMap.containsKey("OPTYPE")) {
				String operType = contextMap.get("OPTYPE");
				errorCode = cusCollect.dealCusInfo(cusInfo, operType);
			}
			if(!"0".equals(errorCode[0])){
				if (!"0".equals(errorCode[0])) {
					feedbackMap.put("RESULTCODE", errorCode[0]);
					feedbackMap.put("RESULTMSG", errorCode[1]);
				}
				return getCorpBindReqRspDocument(feedbackMap);
			}
			if (contextMap.containsKey("OPTYPE")) {
				String operType = contextMap.get("OPTYPE");
				errorCode = cusCollect.dealCusServant(cusServant, operType);
			}
			if(!"0".equals(errorCode[0])){
				if (!"0".equals(errorCode[0])) {
					feedbackMap.put("RESULTCODE", errorCode[0]);
					feedbackMap.put("RESULTMSG", errorCode[1]);
				}
				return getCorpBindReqRspDocument(feedbackMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (!"0".equals(errorCode[0])) {
				feedbackMap.put("RESULTCODE", errorCode[0]);
				feedbackMap.put("RESULTMSG", errorCode[1]);
			}else{
				feedbackMap.put("RESULTCODE", "2299");
				feedbackMap.put("RESULTMSG", "��������");
			}
			return getCorpBindReqRspDocument(feedbackMap);
		}
		} else{
			
		}
		feedbackMap.put("RESULTCODE", "0");
		feedbackMap.put("RESULTMSG", "�ɹ���");
		feedbackMap.put("URL", "");
		return getCorpBindReqRspDocument(feedbackMap);
	}
	
	/**
	 * �û��˺�ͬ���ӿ�
	 * 
	 * @param xml ����
	 * @return ��ִ����
	 */
	public static String StaffBind(String xml){
		Map<String, String> feedbackMap = new LinkedHashMap<String, String>();
		if (!"".equals(xml)) {
			try {
//				System.out.println("StaffBind ���ģ� "+xml);
				Document doc = XmlUtil.parseString(xml);
				Element root = doc.getRootElement();
				dealHead(root);
				dealBody(root);
//				System.out.println("���� map�� "+contextMap);
				feedbackMap.put("UID", contextMap.get("UID"));
				CusCollect cusCollect = (CusCollect) ContextUtil
						.getBean("cusCollect");
				CusManageContact cusContact = cusCollect.getCusContact(contextMap);
				if (contextMap.containsKey("OPTYPE")) {
					String operType = contextMap.get("OPTYPE");
					errorCode = cusCollect.dealCusContact(cusContact, operType);
				}
				if(!"0".equals(errorCode[0])){
					if (!"0".equals(errorCode[0])) {
						feedbackMap.put("RESULTCODE", errorCode[0]);
						feedbackMap.put("RESULTMSG", errorCode[1]);
					}
					return getStaffBindRspDocument(feedbackMap);
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (!"0".equals(errorCode[0])) {
					feedbackMap.put("RESULTCODE", errorCode[0]);
					feedbackMap.put("RESULTMSG", errorCode[1]);
				}else{
					feedbackMap.put("RESULTCODE", "2299");
					feedbackMap.put("RESULTMSG", "��������");
				}
				return getStaffBindRspDocument(feedbackMap);
			}
		} else{
			
		}
		feedbackMap.put("RESULTCODE", "0");
		feedbackMap.put("RESULTMSG", "�ɹ���");
		return getStaffBindRspDocument(feedbackMap);
	}
}
