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
 * 流程系统分布式管理类(distributed-system-manager) 
 * 原理：以其中一个应用为主，通过webservice方式，管理各个副系统。
 * 比如，当主系统中流程配置做了变化，会通知副系统进行更新。
 * 副系统通过相关的配置，在启动时注册进主系统
 * 
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 * 
 */
public class DSMCenter {

	protected Log logger = LogFactory.getLog(getClass());
	
	/**
	 * 是否是主应用
	 */
	private static boolean master = true;
	
	/**
	 * 主应用地址，例如：http://bomctest.js.cmcc/itsm/
	 */
	private static String remoteAddress;
	
	/**
	 * 本地应用url地址,例如：http://bomctest.js.cmcc/itsm/
	 */
	private static String localAddress;
	
	/**
	 * 运行的流程字符串
	 */
	private static String wfStr;
	
	/**
	 * 代理的流程列表（副应用使用。对于主应用=所有的流程-各个副应用代理的）
	 */
	public static List<String> wfList = new ArrayList<String>();
	
	

	/**
	 * 所有的ITSM web应用数组 key="分布式系统webservice地址" value=List<流程OID>;
	 */
	private static Map<String, List<String>> dsMap = new HashMap<String, List<String>>();
	/**
	 * 已经被代理运行的流程列表
	 */
	private static Map<String,String> proxyedWF = new HashMap<String,String>();


	public String registerSystem(String xml) {
		String ret = "";
		logger.debug("得到系统注册请求");
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
//				desc.addText("地址已经存在"+url);
//			}
			String sysURL = url.getText();
			Element wfStr = (Element)ele.selectSingleNode("./wfstr");
			String wf = wfStr.getText();
			
			if (wf == null || wf.equals("")){
				desc.addText("警告：运行的流程列表为空，请配置dsm.wfstr属性");
			} else {
				String[] wfstr = wf.split(",");
				boolean hasRuned = false;
				String proxyWF = "";
				
				for (int i = 0; i < wfstr.length; i++) {
					//如果已经被代理，且与请求地址不同
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
					desc.addText("下列流程已经由其他系统代理运行("+proxyWF+")");
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
	 * 如果不是主系統，則啟動時把應用的地址加載進主系統
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
					logger.debug("注册系统成功。"+desc);
					try {
						updateDB();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				} else {
					logger.error("注册系统失败："+desc);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			master = true;
			Consts.showConfPage = true;
			
			//主系统重启时，把所有副系统再重新加载进来
			try {
				loadDB();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 把由其他系统代理的流程工单，从内存里去除，不再维护
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
	 * 从数据库里加载副系统
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
				logger.debug("应用已经关闭,删除固化数据（"+l.get(i)+"）");
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
			logger.warn("访问地址出错",e);
			//e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * 把副系统除了注册到主系统上，还要写进数据库，以供主服务重启时重新加载
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
	 * 通知各个副系统更新配置
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
	 * 通知各个副系统更新配置
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
	 * 供主系统，通过webservice接口调用
	 * @param category
	 * @param objectOid
	 */
	public void getUpdateByOid(String category, int objectOid) {
		if (category == null || category.equals(""))
			return;
		logger.debug("得到配置更新通知("+category+","+objectOid+")");
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
	 * 供主系统，通过webservice接口调用
	 * @param category
	 * @param id
	 */
	public void getUpdateById(String category,String id){
		if (category == null || category.equals(""))
			return;
		logger.debug("得到配置更新通知("+category+","+id+")");
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
