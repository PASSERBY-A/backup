package com.hp.idc.itsm.dsm;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;

import com.hp.idc.itsm.authorization.MenuManager;
import com.hp.idc.itsm.ci.CIManager;
import com.hp.idc.itsm.common.Cache;
import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.configure.FormManager;
import com.hp.idc.itsm.configure.ViewManager;
import com.hp.idc.itsm.dbo.ColumnData;
import com.hp.idc.itsm.dbo.OracleOperation;
import com.hp.idc.itsm.impl.ITSMFieldManagerImpl;
import com.hp.idc.itsm.security.LocalgroupManager;
import com.hp.idc.itsm.task.TaskInfo;
import com.hp.idc.itsm.util.XmlUtil;
import com.hp.idc.itsm.workflow.WorkflowManager;

/**
 * ����ϵͳ�ֲ�ʽ������(distributed-system-manager) 
 * ԭ��������һ��Ӧ��Ϊ����ͨ��webservice��ʽ�����������ϵͳ��
 * ���磬����ϵͳ�������������˱仯����֪ͨ��ϵͳ���и��¡�
 * ��ϵͳͨ����ص����ã�������ʱע�����ϵͳ
 * 
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 * 
 */
public class DSMCenter {

	protected Log logger = LogFactory.getLog(getClass());
	
	/**
	 * �Ƿ�����Ӧ��
	 */
	private static boolean master = true;
	
	/**
	 * ��Ӧ�õ�ַ�����磺http://bomctest.js.cmcc/itsm/
	 */
	private static String remoteAddress;
	
	/**
	 * ����Ӧ��url��ַ,���磺http://bomctest.js.cmcc/itsm/
	 */
	private static String localAddress;
	
	/**
	 * ���е������ַ���
	 */
	private static String wfStr;
	
	/**
	 * ����������б���Ӧ��ʹ�á�������Ӧ��=���е�����-������Ӧ�ô���ģ�
	 */
	public static List<String> wfList = new ArrayList<String>();
	
	

	/**
	 * ���е�ITSM webӦ������ key="�ֲ�ʽϵͳwebservice��ַ" value=List<����OID>;
	 */
	private static Map<String, List<String>> dsMap = new HashMap<String, List<String>>();
	/**
	 * �Ѿ����������е������б�
	 */
	private static Map<String,String> proxyedWF = new HashMap<String,String>();


	public String registerSystem(String xml) {
		String ret = "";
		logger.debug("�õ�ϵͳע������");
		try {
			Document retdoc = XmlUtil.parseString("<return/>");
			Element success = retdoc.getRootElement().addElement("success");
			Element desc = retdoc.getRootElement().addElement("desc");
			success.setText("true");
			desc.setText("");
			
			Document doc = XmlUtil.parseString(xml);
			Element ele = doc.getRootElement();
			Element url = (Element)ele.selectSingleNode("./url");
//			if (dsMap.get(url)!=null) {
//				success.setText("false");
//				desc.addText("��ַ�Ѿ�����"+url);
//			}
			String sysURL = url.getText();
			Element wfStr = (Element)ele.selectSingleNode("./wfstr");
			String wf = wfStr.getText();
			
			if (wf == null || wf.equals("")){
				desc.addText("���棺���е������б�Ϊ�գ�������dsm.wfstr����");
			} else {
				String[] wfstr = wf.split(",");
				boolean hasRuned = false;
				String proxyWF = "";
				
				for (int i = 0; i < wfstr.length; i++) {
					//����Ѿ����������������ַ��ͬ
					if (proxyedWF.get(wfstr[i])!=null) {
						if (!proxyedWF.get(wfstr[i]).equals(sysURL)) {
							hasRuned = true;
							if (!proxyWF.equals(""))
								proxyWF += ",";
							proxyWF += wfstr[i];
						}
					}
				}
				
				if (hasRuned) {
					success.setText("false");
					desc.addText("���������Ѿ�������ϵͳ��������("+proxyWF+")");
				} else {
					List<String> wfList_ = new ArrayList<String>();
					for (int i = 0; i < wfstr.length; i++) {
						wfList_.add(wfstr[i]);
						proxyedWF.put(wfstr[i],sysURL);
					}

					if (wfList_.size()>0) {
						dsMap.put(sysURL, wfList_);
						removeWFTask(wfList_);
					}
				}

			}
			ret = retdoc.asXML();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * ���������ϵ�y���t���ӕr�ё��õĵ�ַ���d�M��ϵ�y
	 */
	public void init(){
		if (remoteAddress!=null && !remoteAddress.equals("")){
			if(!remoteAddress.endsWith("/"))
				remoteAddress += "/";
			String webServiceURL = remoteAddress+"service/DSWebService";
			master = false;
			try {
				Document doc = XmlUtil.parseString("<dsm/>");
				Element rootEle = doc.getRootElement();
				Element url = rootEle.addElement("url");
				url.setText(localAddress);
				Element wfstr = rootEle.addElement("wfstr");
				wfstr.setText(wfStr);
				
				String ret = "";
				Service service = new Service();
				Call call = (Call) service.createCall();
				call.setTargetEndpointAddress(webServiceURL);
				call.setOperationName(new QName(webServiceURL, "registerSystem"));
				ret = (String)call.invoke(new Object[] { doc.asXML() });
				
				Document retDoc = XmlUtil.parseString(ret);
				String success=retDoc.getRootElement().selectSingleNode("./success").getText();
				String desc=retDoc.getRootElement().selectSingleNode("./desc").getText();
				if ("true".equals(success)){
					logger.debug("ע��ϵͳ�ɹ���"+desc);
					try {
						updateDB();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} else {
					logger.error("ע��ϵͳʧ�ܣ�"+desc);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			master = true;
			Consts.showConfPage = true;
			
			//��ϵͳ����ʱ�������и�ϵͳ�����¼��ؽ���
			try {
				loadDB();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ��������ϵͳ��������̹��������ڴ���ȥ��������ά��
	 * @param wfList
	 */
	private void removeWFTask(List<String> wfList){
		if(wfList == null)
			return;
		for (int i = 0; i < wfList.size(); i++) {
			String wfOid = wfList.get(i);
			Map<String,TaskInfo> tm = Cache.Workflow_Tasks.remove(wfOid);
			if (tm!=null && tm.values()!=null) {
				Object[] taskArray = tm.values().toArray();
				for (int j = 0; j < taskArray.length; j++) {
					TaskInfo ti = (TaskInfo)taskArray[j];
					Cache.Tasks.remove(ti.getOrigin()+"_"+ti.getWfOid()+"_"+ti.getOid());
				}
			}
			
			Map<String,TaskInfo> tmHis = Cache.Workflow_TasksHis.remove(wfOid);
			if (tmHis!=null && tmHis.values()!=null) {
				Object[] taskHisArray = tmHis.values().toArray();
				for (int j = 0; j < taskHisArray.length; j++) {
					TaskInfo ti = (TaskInfo)taskHisArray[j];
					Cache.TasksHis.remove(ti.getOrigin()+"_"+ti.getWfOid()+"_"+ti.getOid());
				}
			}
		}
	}
	
	/**
	 * �����ݿ�����ظ�ϵͳ
	 * @throws SQLException
	 */
	public void loadDB() throws SQLException{
		dsMap = new HashMap<String, List<String>>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation("ITSM_CFG_DSM","system");
		try {
			String sql = "select * from ITSM_CFG_DSM";
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			List<String> l = new ArrayList<String>();
			while (rs.next()) {
				String url = rs.getString("APPLICATION_ADDRESS");
				String wfStr = rs.getString("WFSTR");
				boolean active = isActive(url);
				if (active){
					List<String> wfList = new ArrayList<String>();
					String[] wfstr_ = wfStr.split(",");
					for (int i = 0; i < wfstr_.length; i++) {
						wfList.add(wfstr_[i]);
						proxyedWF.put(wfstr_[i],url);
					}
					dsMap.put(url, wfList);
				} else {
					l.add(url);
					
				}
			}
			for (int i = 0; i < l.size(); i++) {
				logger.debug("Ӧ���Ѿ��ر�,ɾ���̻����ݣ�"+l.get(i)+"��");
				ps = u.getStatement("APPLICATION_ADDRESS=?");
				ps.setString(1,l.get(i));
				rs = ps.executeQuery();
				u.executeDelete(rs);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
	}
	
	private boolean isActive(String appUrl){
		boolean ret = false;
		try {
			if(!appUrl.endsWith("/"))
				appUrl += "/";
			String webServiceURL = appUrl+"service/DSWebService";
			logger.debug(webServiceURL);
			Service service = new Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(webServiceURL);
			call.setOperationName(new QName(webServiceURL, "isActive"));
			ret = (Boolean)call.invoke(new Object[] {});
		} catch (Exception e) {
			ret = false;
			logger.warn("���ʵ�ַ����",e);
			//e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * �Ѹ�ϵͳ����ע�ᵽ��ϵͳ�ϣ���Ҫд�����ݿ⣬�Թ�����������ʱ���¼���
	 * @throws SQLException
	 */
	public void updateDB() throws SQLException{
		ResultSet rs = null;		
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation("ITSM_CFG_DSM", "system");
		try {
			u.setColumnData("APPLICATION_ADDRESS", new ColumnData(
					ColumnData.TYPE_STRING, localAddress));
			u.setColumnData("WFSTR", new ColumnData(
					ColumnData.TYPE_STRING, wfStr));

			ps = u.getStatement("APPLICATION_ADDRESS=?");
			ps.setString(1, localAddress);
			rs = ps.executeQuery();
			u.executeDelete(rs);
			
			rs = u.getResultSet(null);
			u.executeInsert(rs);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			u.commit(rs);
		}
		
	}

	/**
	 * ֪ͨ������ϵͳ��������
	 * @param category
	 * @param objectOid
	 */
	public void publishEvent(String category, int objectOid) {
		Object[] keySet = dsMap.keySet().toArray();
		for (int i= 0; i < keySet.length; i++) {
			String wsURL = (String)keySet[i];
			if(!wsURL.endsWith("/"))
				wsURL += "/";
			wsURL += "service/DSWebService";
			try {
				Service service = new Service();
				Call call = (Call) service.createCall();
				call.setTargetEndpointAddress(wsURL);
				call.setOperationName(new QName(wsURL, "getUpdateByOid"));
				call.invoke(new Object[] { category, objectOid });
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ֪ͨ������ϵͳ��������
	 * @param category
	 * @param objectId
	 */
	public void publishEvent(String category, String objectId) {
		Object[] keySet = dsMap.keySet().toArray();
		for (int i= 0; i < keySet.length; i++) {
			String wsURL = (String)keySet[i];
			wsURL += "/service/DSWebService";
			 try {
				 Service service = new Service();
				 Call call = (Call) service.createCall();
				 call.setTargetEndpointAddress(wsURL);
				 call.setOperationName(new QName(wsURL, "getUpdateById"));
				 call.invoke(new Object[] {category,objectId});
			 }catch (Exception e){
				 e.printStackTrace();
			 }
		}
	}

	/**
	 * ����ϵͳ��ͨ��webservice�ӿڵ���
	 * @param category
	 * @param objectOid
	 */
	public void getUpdateByOid(String category, int objectOid) {
		if (category == null || category.equals(""))
			return;
		logger.debug("�õ����ø���֪ͨ("+category+","+objectOid+")");
		try {
			if (category.equals("FORM")) {
				FormManager.reloadFormInfo(objectOid);
			}
			if (category.equals("WORKFLOW")) {
				WorkflowManager.reloadWorkflow(objectOid);
			}
			if (category.equals("FIELD")) {
				ITSMFieldManagerImpl ifm = new ITSMFieldManagerImpl();
				ifm.reloadField(objectOid);
			}
			if (category.equals("VIEW")) {
				ViewManager.reloadView(objectOid);
			}
			if (category.equals("CODETYPE")) {
				CIManager.reloadCodeType(objectOid);
			}
			if (category.equals("CODE")) {
				CIManager.reloadCode(objectOid);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ����ϵͳ��ͨ��webservice�ӿڵ���
	 * @param category
	 * @param id
	 */
	public void getUpdateById(String category,String id){
		if (category == null || category.equals(""))
			return;
		logger.debug("�õ����ø���֪ͨ("+category+","+id+")");
		try {
			if (category.equals("MENU")) {
				MenuManager.reloadMenu(id);
			}
			
			if (category.equals("LOCALGROUP")){
				LocalgroupManager.reloadLocalgroup(id);
			}
			
			if (category.equals("LOCALGROUP_R")){
				LocalgroupManager.reloadLU(id);
			}
			
			if (category.equals("FACTOR")){
				LocalgroupManager.reloadLU(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setLocalAddress(String localAddress) {
		this.localAddress = localAddress;
	}

	public String getLocalAddress() {
		return localAddress;
	}

	public void setWfStr(String wfStr) {
		this.wfStr = wfStr;
		if (wfStr!=null && !wfStr.equals("")) {
			String[] wf = wfStr.split(",");
			for (int i = 0; i < wf.length; i++) {
				wfList.add(wf[i]);
			}
		}
	}

	public static String getWfStr() {
		return wfStr;
	}

	public static boolean isMaster() {
		return master;
	}

	public static Map<String, List<String>> getDsMap() {
		return dsMap;
	}

	public static List<String> getProxyedWF() {
		List<String> ret = new ArrayList<String>();
		if (proxyedWF.size() >0)
			ret.addAll(proxyedWF.keySet());
		return ret;
	}
	
	public static void main(String[] args) {
		DSMCenter c = new DSMCenter();
		System.out.println(c.isActive("http://10.32.145.233/itsm/"));
	}

}
