package com.hp.idc.itsm.configure;

import java.io.IOException;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.idc.context.util.ContextUtil;
import com.hp.idc.itsm.common.Cache;
import com.hp.idc.itsm.common.OperationResult;
import com.hp.idc.itsm.dbo.ColumnData;
import com.hp.idc.itsm.dbo.OracleOperation;
import com.hp.idc.itsm.dbo.ResultSetOperation;
import com.hp.idc.itsm.dsm.DSMCenter;
import com.hp.idc.itsm.util.ItsmUtil;

/**
 * 
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 * @since 2007-06-05
 */
public class ViewManager {
	
	public static void initCache() throws IOException, SQLException{
		System.out.println("loading view...");
		Cache.Views = new HashMap<Integer,ViewInfo>();
		loadViews(); 
	}
	
	protected static void updateCache(ViewInfo vInfo){
		Cache.Views.put(new Integer(vInfo.getOid()),vInfo);
	}
	
	protected static void loadViews() throws IOException, SQLException{
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			String sql = "select * from itsm_cfg_views";
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				ViewInfo info = getViewInfoFromResultSet(rs);
				info.parseXml();
				updateCache(info);
			}
		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			u.commit(rs);
		}
	}
	
	public static void reloadView(int oid) throws IOException, SQLException{
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			String sql = "select * from itsm_cfg_views where view_oid="+oid;
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				ViewInfo info = getViewInfoFromResultSet(rs);
				info.parseXml();
				updateCache(info);
			}
		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			u.commit(rs);
		}
	}
	
	/**
	 * 从结果集里面获取ViewInfo
	 * @param rs
	 * @return ViewInfo
	 * @throws SQLException
	 * @throws IOException
	 */
	protected static ViewInfo getViewInfoFromResultSet(ResultSet rs) throws SQLException,IOException {
		ViewInfo viewInfo = new ViewInfo();
		viewInfo.setOid(rs.getInt("VIEW_OID"));
		viewInfo.setName(rs.getString("VIEW_NAME"));
		viewInfo.setApplyTo(rs.getString("VIEW_APPLYTO"));

		Clob clob = rs.getClob("VIEW_CONFIGURE");
		viewInfo.setConfigure(ResultSetOperation.clobToString(clob));
		return viewInfo;
	}
	
	/**
	 * 返回具有指定模块名称的所有字段，包含公共的
	 * @param moduleName
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public static List getViewsOfModule(String moduleName) throws SQLException,IOException {
		return getViewsOfModule(moduleName,true);
	}
	
	/**
	 * 返回具有指定模块名称的所有字段
	 * @param moduleName 参见ModuleName定义
	 * @param includeALL 是否包含公共的
	 * @return List<ViewInfo>
	 */
	public static List getViewsOfModule(String moduleName,boolean includeALL) throws SQLException,IOException {
		if (!ModuleName.moduleNameIsRight(moduleName))
			throw new SQLException("参数moduleName传入错误！(\""+moduleName+"\" 不存在)");
		List views = getViews();
		List returnList = new ArrayList();
		for (int i = 0; i< views.size(); i++) {
			ViewInfo info = (ViewInfo)views.get(i);
			if(info.applyTo.equals(moduleName))
				returnList.add(info.cloneInfo());
			if (includeALL) {
				if (info.applyTo.equals(ModuleName.ALL+""))
					returnList.add(info.cloneInfo());
			}
		}
		return returnList;
	}
	
	public static List<ViewInfo> getViews() throws SQLException, IOException {
		List<ViewInfo> returnList = new ArrayList<ViewInfo>();
		Map views = Cache.Views;
		Object[] keys = views.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			returnList.add((ViewInfo)views.get(keys[i]));
		}
		return returnList;
	}
	
	public static List<ViewInfo> getViewsOfUser(String userId) {
		List<ViewInfo> returnList = new ArrayList<ViewInfo>();
		if (userId == null)
			return returnList;
		Map views = Cache.Views;
		Object[] keys = views.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			ViewInfo vi = (ViewInfo)views.get(keys[i]);
			if (userId.equals("root") || userId.equals(vi.getAttribute("create_by")))
					returnList.add(vi);
		}
		return returnList;
	}
	
	
	/**
	 * 增加
	 * @param info
	 * @param operName
	 * @return
	 * @throws SQLException
	 */
	public static int addView(ViewInfo info, String operName) throws SQLException {
		OperationResult ret = new OperationResult();
		
		ResultSet rs = null;
		int oid = info.getOid();
		if (oid == 0)
			oid = ItsmUtil.getSequence("view");
		
		OracleOperation u = new OracleOperation("ITSM_CFG_VIEWS", operName);
		//u.setJdbcMode(ItsmUtil.JDBC_URL,ItsmUtil.JDBC_USER,ItsmUtil.JDBC_PASSWORD);
		try {

			u.setColumnData("VIEW_OID", new ColumnData(
					ColumnData.TYPE_INTEGER, new Integer(oid)));
			u.setColumnData("VIEW_NAME", new ColumnData(
					ColumnData.TYPE_STRING, info.getName()));			
			u.setColumnData("VIEW_APPLYTO", new ColumnData(
					ColumnData.TYPE_STRING, info.getApplyTo()));
			u.setColumnData("VIEW_CONFIGURE", new ColumnData(
					ColumnData.TYPE_CLOB, info.getConfigure()));

			rs = u.getResultSet(null);
			u.executeInsert(rs);
		} catch (SQLException e) {
			ret.setSuccess(false);
			ret.setMessage(e.toString());
			throw e;
		} finally {
			u.commit(rs);
		}
		if (ret.isSuccess()) {
			info.setOid(oid);
			updateCache(info);
			
			DSMCenter dsmc = (DSMCenter)ContextUtil.getBean("DSMCenter");
			dsmc.publishEvent("VIEW", info.getOid());
		}
		return oid;
	}
	
	/**
	 * 更改
	 * @param info
	 * @param operName
	 * @throws SQLException
	 */
	public static void updateView(ViewInfo info,String operName) throws SQLException {
		
		OperationResult ret = new OperationResult();
		
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation("ITSM_CFG_VIEWS", operName);
		//u.setJdbcMode(ItsmUtil.JDBC_URL,ItsmUtil.JDBC_USER,ItsmUtil.JDBC_PASSWORD);
		try {
			u.setColumnData("VIEW_NAME", new ColumnData(
					ColumnData.TYPE_STRING, info.getName()));
			
			u.setColumnData("VIEW_APPLYTO", new ColumnData(
					ColumnData.TYPE_STRING, info.getApplyTo()));
			u.setColumnData("VIEW_CONFIGURE", new ColumnData(
					ColumnData.TYPE_CLOB, info.getConfigure()));
			ps = u.getStatement("VIEW_OID=?");
			ps.setInt(1,info.getOid());
			rs = ps.executeQuery();
			u.executeUpdate(rs);
		} catch (SQLException e) {
			ret.setSuccess(false);
			ret.setMessage(e.toString());
			throw e;
		} finally {
			u.commit(rs);
		}
		if (ret.isSuccess()) {
			updateCache(info);
			DSMCenter dsmc = (DSMCenter)ContextUtil.getBean("DSMCenter");
			dsmc.publishEvent("VIEW", info.getOid());
		}
	}
	
	/**
	 * 删除
	 * @param oid
	 * @param operName
	 * @throws SQLException
	 */
	public static void deleteView(int oid, String operName) throws SQLException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation("ITSM_CFG_VIEWS", operName);
		try {
			ps = u.getStatement("VIEW_OID=?");
			ps.setInt(1,oid);
			rs = ps.executeQuery();
			u.executeDelete(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
	}
	
	/**
	 * 单条信息
	 * @param oid
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public static ViewInfo getViewByOid(int oid) throws SQLException,IOException {
		if(oid== -1)
			return null;
		
		ViewInfo vInfo = (ViewInfo)Cache.Views.get(new Integer(oid));
		if (vInfo!=null)
			return vInfo.cloneInfo();
		else
			return null;
	}
	
	/**
	 * 根据sql语句直接查询
	 * @param sql
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public static List getViewsBySQL(String sql) throws SQLException,IOException {
		List returnList = new ArrayList();
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				ViewInfo info = getViewInfoFromResultSet(rs);
				info.parseXml();
				returnList.add(info);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		return returnList;
	}
	
	/**
	 * 判断可否修改，如果有表单阴影OID标识的域，则不允许修改，包括删除
	 * @param oid
	 * @return
	 */
	public static boolean isModifiable(int oid) {
		
		return true;
	}
	
}
