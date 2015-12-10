package com.hp.idc.itsm.workflow;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;

import com.hp.idc.context.util.ContextUtil;
import com.hp.idc.itsm.common.Cache;
import com.hp.idc.itsm.common.OperationResult;
import com.hp.idc.itsm.common.SMSTemplate;
import com.hp.idc.itsm.configure.ModuleName;
import com.hp.idc.itsm.dbo.ColumnData;
import com.hp.idc.itsm.dbo.OracleOperation;
import com.hp.idc.itsm.dsm.DSMCenter;
import com.hp.idc.itsm.security.RuleManager;
import com.hp.idc.itsm.task.BaseTableStructure;
import com.hp.idc.itsm.util.ItsmUtil;

/**
 * 流程管理类
 *
 * @author 梅园
 *
 */
public class WorkflowManager {
	/**
	 * 初始化缓存
	 *
	 * @throws SQLException
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void initCache() throws SQLException, IOException,
			DocumentException {
		System.out.println("loading workflow...");
		Cache.Workflows = new HashMap<Integer,WorkflowInfo>();
		loadWorkflows();
	}

	/**
	 * 更新流程对象缓存
	 *
	 * @param obj
	 *            更新的流程对象
	 */
	protected static void updateCache(WorkflowInfo obj) {
		if (obj != null) {
			Cache.Workflows.put(new Integer(obj.getOid()), obj);
		}
	}

	/**
	 * 加载缓存：流程数据
	 *
	 * @throws SQLException
	 * @throws IOException
	 * @throws DocumentException
	 */
	protected static void loadWorkflows() throws SQLException, IOException,
			DocumentException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			String sql = "select * from ITSM_CFG_WORKFLOW";
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println("load workflow(oid="+rs.getInt("WF_OID")+")");
				WorkflowInfo info = new WorkflowInfo();
				info.parse(rs);
				updateCache(info);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
	}
	
	public static void reloadWorkflow(int oid) throws SQLException,
			IOException, DocumentException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			String sql = "select * from ITSM_CFG_WORKFLOW where wf_oid="+oid;
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				System.out.println("load workflow(oid=" + rs.getInt("WF_OID")
						+ ")");
				WorkflowInfo info = new WorkflowInfo();
				info.parse(rs);
				updateCache(info);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
	}

	/**
	 * 获取所有流程
	 *
	 * @param includeUnused
	 *            <tt>true</tt>:包括停止使用的；<tt>false</tt>:不包括
	 * @return List[WorkflowInfo]
	 */
	public static List<WorkflowInfo> getWorkflows(boolean includeUnused) {
		List<WorkflowInfo> returnList = new ArrayList<WorkflowInfo>();
		Object[] flds = null;
		if (Cache.Workflows!=null && Cache.Workflows.values()!=null)
			flds = Cache.Workflows.values().toArray();
		if (flds == null)
			return returnList;
		for (int i = 0; i < flds.length; i++) {
			WorkflowInfo info = (WorkflowInfo) flds[i];
			if (!includeUnused && info.getStatus() == WorkflowInfo.STATUS_UNUSED)
				continue;
			returnList.add(info);
		}
		return returnList;
	}
	
	/**
	 * 获取本应用运行的流程（多应用分布式部署后实行）
	 * @return
	 */
	public static List<WorkflowInfo> getRuningWorkflow(boolean includeUnused){
		List<WorkflowInfo> returnList = new ArrayList<WorkflowInfo>();
		if (DSMCenter.isMaster()) {
			List<WorkflowInfo> l = getWorkflows(includeUnused);
			for (int i = 0; i < l.size(); i++){
				WorkflowInfo info = (WorkflowInfo)l.get(i);
				List<String> proxyedList = DSMCenter.getProxyedWF();
				boolean proxyed = false;
				for (int j = 0; j < proxyedList.size(); j++){
					if (proxyedList.get(j).equals(info.getOid()+"")) {
						proxyed = true;
						break;
					}
				}
				if (!proxyed)
					returnList.add(info);
			}
		} else {
			List<String> wfList = DSMCenter.wfList;
			for (int i = 0; i < wfList.size(); i++) {
				WorkflowInfo info = Cache.Workflows.get(new Integer(wfList.get(i)));
				if (!includeUnused && info.getStatus() == WorkflowInfo.STATUS_UNUSED)
					continue;
				returnList.add(info);
			}
		}
		return returnList;
	}

	/**
	 * 返回登录用户有权限的流程
	 * @param userId
	 * @param includeUnused
	 * @return
	 */
	public static List<WorkflowInfo> getWorkflows(String userId,boolean includeUnused) {
		List<WorkflowInfo> returnList = new ArrayList<WorkflowInfo>();
		List<WorkflowInfo> l = getRuningWorkflow(includeUnused);
		for (int i = 0; i < l.size(); i++){
			WorkflowInfo info = (WorkflowInfo)l.get(i);
			if (RuleManager.valid(userId,info.getRule(),true)) {
				returnList.add(info);
			}
		}
		return returnList;
	}

	/**
	 * 获取所有流程,并把其按名称格式分组(<code>如:任务/任务1；任务/任务2，处理后，分成两级
	 * 任务是父级，任务1任务2是子级</code>)，只处理一级（即只解析第一个“/”）
	 * @param includeUnused
	 * <tt>true</tt>:包括停止使用的；<tt>false</tt>:不包括
	 * @return
	 */
	public static Map getWorkflowsTree(String userId,boolean includeUnused) {
		Map retMap = new HashMap();
		List l = getWorkflows(userId,includeUnused);
		for (int i = 0; i < l.size(); i++){
			WorkflowInfo wfInfo = (WorkflowInfo)l.get(i);
			String name = wfInfo.getName();
			int pos = name.indexOf("/");
			if(pos!=-1){
				String pName = name.substring(0,pos);
				List subList = (List)retMap.get(pName);
				if (subList==null){
					subList = new ArrayList();
					retMap.put(pName,subList);
				}
				subList.add(wfInfo);
			}else{
				retMap.put(name, wfInfo);
			}
		}
		if (retMap instanceof WorkflowInfo){

		}
		return retMap;
	}

	/**
	 * 根据OID返回流程信息
	 *
	 * @param oid
	 *            流程记录oid
	 * @return 找到的流程信息，找不到时返回null
	 */
	public static WorkflowInfo getWorkflowByOid(int oid) {
		return Cache.Workflows.get(new Integer(oid));
	}

	/**
	 * 根据模块名称查找相关流程
	 *
	 * @param moduleName
	 *            模块名称
	 * @return 满足条件的所有流程对象
	 * @throws SQLException
	 * @throws IOException
	 */
	public static List getWorkflowsOfModule(String moduleName)
			throws SQLException, IOException {
		return getWorkflowsOfModule(ModuleName.moduleName2Oid(moduleName));
	}

	/**
	 * 根据模块名称查找相关流程
	 *
	 * @param moduleOid
	 *            模块名称
	 * @return 满足条件的所有流程对象
	 * @throws SQLException
	 * @throws IOException
	 */
	public static List getWorkflowsOfModule(int moduleOid) throws SQLException,
			IOException {
		List returnList = new ArrayList();
		if(moduleOid == -1){
			returnList.addAll(Cache.Workflows.values());
			return returnList;
		}
		Object[] flds = Cache.Workflows.values().toArray();
		for (int i = 0; i < flds.length; i++) {
			WorkflowInfo info = (WorkflowInfo) flds[i];
			if (info.inModule(moduleOid))
				returnList.add(info);
		}
		return returnList;
	}

	/**
	 * 查找用到指定流程指定版本的工单数量
	 * @param wfOid
	 * @param version
	 * @return
	 * @throws SQLException
	 */
	public static int getTaskInfoOfWfVersion(int wfOid,int version) throws SQLException{
		WorkflowInfo wfInfo = getWorkflowByOid(wfOid);
		WorkflowData wfData = wfInfo.getVersion(version);
		String sql = "select count(*) count from "+wfData.getDataTable()+" where TASK_WF_OID="+ wfOid +" and TASK_WF_VER="+version;
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		int ret = 0;
		try {
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			if (rs.next())
			ret = rs.getInt("count");
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		return ret;
	}

	/**
	 * 更新流程信息
	 *
	 * @param map
	 *            前台传过来的表单MAP
	 * @param operName
	 *            操作人
	 * @return 操作结果
	 * @throws SQLException
	 * @throws DocumentException
	 */
	synchronized public static OperationResult updateWorkflow(Map<String,String> map,
			String operName) throws SQLException, DocumentException {
		OperationResult ret = new OperationResult();
		ResultSet rs = null;
		PreparedStatement ps = null;
		int oid = Integer.parseInt((String) map.get("fld_oid"));
		String name = (String) map.get("fld_name");
		String applyto = (String) map.get("fld_applyto");
		String xml = (String) map.get("fld_xml");
		String origin ="ITSM";
		if (map.get("fld_origin")!=null)
			origin = (String)map.get("fld_origin");
		String rule = "";
		if (map.get("fld_rule")!=null)
			rule = (String)map.get("fld_rule");
		int status = 1;
		if (map.get("fld_status")!=null)
			status = Integer.parseInt((String)map.get("fld_status"));
		String editRule = "";
		if (map.get("fld_editRule")!=null)
			editRule = (String)map.get("fld_editRule");
		String enableSMS = "false";
		if (map.get("fld_enableSMS")!=null)
			enableSMS = (String)map.get("fld_enableSMS");

		String allowLocalGroup = "false";
		if (map.get("fld_allowLocalGroup")!=null)
			allowLocalGroup = (String)map.get("fld_allowLocalGroup");
		
		String loadHisToCache = "false";
		if (map.get("fld_loadHisToCache")!=null)
			loadHisToCache = (String)map.get("fld_loadHisToCache");

		String dealPage = "";
		if (map.get("fld_deal_page")!=null)
			dealPage = (String)map.get("fld_deal_page");
		if (dealPage == null || dealPage.equals(""))
			dealPage = "$ITSM_HOME/task/taskInfo.jsp";
		
		String snapMode = "";
		if (map.get("fld_snap_mode")!=null)
			snapMode = (String)map.get("fld_snap_mode");
		String snapLocalFormOid = "";
		if (map.get("snap_local_form")!=null)
			snapLocalFormOid = (String)map.get("snap_local_form");
		String snapLocalFormTemplate = "";
		if (map.get("snap_local_viewTemplate")!=null)
			snapLocalFormTemplate = (String)map.get("snap_local_viewTemplate");
		String snapRemoteViewPage = "";
		if (map.get("snap_remote_viewPage")!=null)
			snapRemoteViewPage = (String)map.get("snap_remote_viewPage");

		String dataTable = "";
		if (map.get("fld_data_table")!=null)
			dataTable = (String)map.get("fld_data_table");
		
		//是否强制覆盖
		boolean forceModify = false;
		if (map.get("forceModify")!=null)
			forceModify = Boolean.parseBoolean(map.get("forceModify"));
		
		//变更申请流程
		int changeWfOid = -1;
		if (map.get("fld_changeWf")!=null && !map.get("fld_changeWf").equals(""))
			changeWfOid = Integer.parseInt((String)map.get("fld_changeWf"));

		WorkflowInfo oldInfo = null;
		WorkflowInfo wfInfo = new WorkflowInfo();
		OracleOperation u = new OracleOperation("ITSM_CFG_WORKFLOW", operName);


		boolean isNew = false;
		try {
			if (oid == -1) {
				isNew = true;
				oid = ItsmUtil.getSequence("workflow");
				wfInfo.parse(oid, name, operName, status,
						System.currentTimeMillis(), applyto, "",origin);
				wfInfo.addVersion(xml, operName);

			} else {
				oldInfo = getWorkflowByOid(oid);
				int count = 0;
				if (oldInfo.getOrigin().equals("ITSM")){
					if (BaseTableStructure.tableIsExist(dataTable))
						count = getTaskInfoOfWfVersion(oldInfo.getOid(),oldInfo.getCurrentVersionId());
				}
				wfInfo.parse(oldInfo.getOid(), name, operName,
						status, System.currentTimeMillis(),
						applyto, oldInfo.getXmlData(),origin);
				if (count > 0 && !forceModify)
					wfInfo.addVersion(xml, operName);
				else
					wfInfo.updateCurrentVersion(xml, operName);
			}
			wfInfo.setEditRule(editRule);
			wfInfo.setEnableSMS("false".equals(enableSMS)?false:true);
			wfInfo.setAllowLocalGroup("on".equals(allowLocalGroup)?true:false);
			wfInfo.setLoadHisToCache("on".equals(loadHisToCache)?true:false);
			wfInfo.setRule(rule);
			wfInfo.setDealPage(dealPage);
			wfInfo.setSnapMode(snapMode);
			wfInfo.setSnapLocalFormOid(snapLocalFormOid);
			wfInfo.setSnapLocalFormTemplate(snapLocalFormTemplate);
			wfInfo.setSnapRemoteViewPage(snapRemoteViewPage);
			wfInfo.setChangeWfOid(changeWfOid);
			SMSTemplate smsTemp = new SMSTemplate();
			smsTemp.setSmsNew(map.get("sms_new").trim());
			smsTemp.setSmsDealed(map.get("sms_dealed").trim());
			smsTemp.setSmsRollback(map.get("sms_rollback").trim());
			smsTemp.setSmsOvertime(map.get("sms_overtime").trim());
			smsTemp.setSmsOverdue(map.get("sms_overdue").trim());
			smsTemp.setSmsRemind(map.get("sms_remind").trim());
			wfInfo.setSmsTemplate(smsTemp);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			if (isNew)
				u.setColumnData("WF_OID", new ColumnData(
						ColumnData.TYPE_INTEGER, new Integer(oid)));
			u.setColumnData("WF_NAME", new ColumnData(ColumnData.TYPE_STRING,
					wfInfo.getName()));
			u.setColumnData("WF_ORIGIN", new ColumnData(ColumnData.TYPE_STRING,
					wfInfo.getOrigin()));
			if (wfInfo.getLastUpdate() == 0)
				wfInfo.setLastUpdate(System.currentTimeMillis());
			u.setColumnData("WF_LASTUPDATE", new ColumnData(
					ColumnData.TYPE_DATETIME, sdf.format(new Date(wfInfo
							.getLastUpdate()))));

			u.setColumnData("WF_STATUS", new ColumnData(
					ColumnData.TYPE_INTEGER, new Integer(wfInfo.getStatus())));
			u.setColumnData("WF_EDITBY", new ColumnData(ColumnData.TYPE_STRING,
					wfInfo.getEditBy()));
			u.setColumnData("WF_CATALOG", new ColumnData(
					ColumnData.TYPE_STRING, wfInfo.getCategory()));
			u.setColumnData("WF_CONFIGURE", new ColumnData(
					ColumnData.TYPE_CLOB, wfInfo.getXmlData()));

			if (isNew) {
				rs = u.getResultSet(null);
				u.executeInsert(rs);
			} else {
				ps = u.getStatement("WF_OID=?");
				ps.setInt(1, oid);
				rs = ps.executeQuery();
				u.executeUpdate(rs);
			}


		} catch (Exception e) {
			ret.setSuccess(false);
			ret.setMessage(e.toString());
		} finally {
			u.commit(rs);
		}
		if (ret.isSuccess()){

			updateCache(wfInfo);
			
			if (wfInfo.getStatus() == WorkflowInfo.STATUS_USED && ("ITSM").equals(wfInfo.getOrigin())) {
				//更新流程数据表结构
				Map structure = new HashMap();
				if (!dataTable.equals(""))
					structure.put("SYS_TABLE_NAME", dataTable);
				else
					structure.put("SYS_TABLE_NAME", "itsm_task_"+wfInfo.getOid());
				List cols = wfInfo.getColumnFieldsList();

				for (int i = 0; i < cols.size(); i++) {
					ColumnFieldInfo cfInfo = (ColumnFieldInfo)cols.get(i);
					structure.put("SYS_COL_"+cfInfo.getColumnName(), "varchar2(1024)");
				}
				BaseTableStructure.updateTableStructure(structure);

			}
			
			DSMCenter dsmc = (DSMCenter)ContextUtil.getBean("DSMCenter");
			dsmc.publishEvent("WORKFLOW", wfInfo.getOid());
		}
		return ret;
	}

	/**
	 * 删除流程版本
	 * @param wfOid
	 * @param version
	 * @param operName
	 * @return
	 * @throws SQLException
	 */
	public static OperationResult removeWorkflowVersion(int wfOid,int version,String operName) throws SQLException {
		OperationResult ret = new OperationResult();
		WorkflowInfo wfInfo = getWorkflowByOid(wfOid);
		if (wfInfo == null) {
			ret.setSuccess(false);
			ret.setMessage("查无此流程");
			return ret;
		}
		WorkflowData wfData = wfInfo.getVersion(version);
		if (wfData == null) {
			ret.setSuccess(false);
			ret.setMessage("查无此流程版本");
			return ret;
		}
		int count = 0;
		if (wfInfo.getOrigin().equals("ITSM")){
			if (BaseTableStructure.tableIsExist(wfData.getDataTable()))
				count = getTaskInfoOfWfVersion(wfOid,version);
		}
		if (count == 0) {
			wfInfo.removeVersion(version);
		}
		
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation("ITSM_CFG_WORKFLOW", operName);
		try {
			u.setColumnData("WF_CONFIGURE", new ColumnData(
					ColumnData.TYPE_CLOB, wfInfo.getXmlData()));
			ps = u.getStatement("WF_OID=?");
			ps.setInt(1, wfOid);
			rs = ps.executeQuery();
			u.executeUpdate(rs);
		} catch (Exception e) {
			ret.setSuccess(false);
			ret.setMessage(e.toString());
		} finally {
			u.commit(rs);
		}
		if (ret.isSuccess()){
			updateCache(wfInfo);
		}
		return ret;
	}
	/**
	 * 获取所有流程所有版本的工单数据存储表名
	 * @return
	 */
	public static List getAllDataTablesName(boolean includeUnused){
		return getAllDataTablesName(null,includeUnused);
	}

	/**
	 * 获取所有流程所有版本的工单数据存储表名
	 * @param origin
	 * @param includeUnused
	 * @return
	 */
	public static List<String> getAllDataTablesName(String origin,boolean includeUnused){
		List<String> retList = new ArrayList<String>();
		List<WorkflowInfo> wfLoaded = getRuningWorkflow(includeUnused);
		Map<String,String> dtMap = new HashMap<String,String>();
		for (int i = 0; i < wfLoaded.size(); i++) {
			WorkflowInfo wfInfo = (WorkflowInfo)wfLoaded.get(i);
			if (origin!=null && !wfInfo.getOrigin().equals(origin))
				continue;
			Map<String,String> map_ = wfInfo.getDataTablesName();
			if(map_.size()>0)
				dtMap.putAll(map_);
		}
		retList.addAll(dtMap.keySet());

		return retList;
	}

}
