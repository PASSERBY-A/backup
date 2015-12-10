package com.hp.idc.itsm.configure;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;

import com.hp.idc.context.util.ContextUtil;
import com.hp.idc.itsm.common.Cache;
import com.hp.idc.itsm.common.OperationResult;
import com.hp.idc.itsm.dbo.ColumnData;
import com.hp.idc.itsm.dbo.OracleOperation;
import com.hp.idc.itsm.dsm.DSMCenter;
import com.hp.idc.itsm.util.ItsmUtil;

/**
 * 
 * @author <a href="mailto:lihz@lianchuang.com">FluteD</a>
 * @since 2007-06-05
 */
public class FormManager {
	public static void initCache() throws SQLException, IOException, DocumentException {
		System.out.println("loading forms...");
		Cache.Forms = new HashMap<Integer,FormInfo>();
		loadForms();
	}

	protected static void updateCache(FormInfo obj) {
		if (obj != null) {
			Cache.Forms.put(new Integer(obj.getOid()), obj);
		}
		
	}
	
	protected static void loadForms() throws SQLException, IOException, DocumentException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			String sql = "select * from ITSM_CFG_FORMS";
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				FormInfo info = new FormInfo();
				info.parse(rs);
				updateCache(info);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
	}
	
	
	public static void reloadFormInfo(int oid) throws IOException, DocumentException, SQLException{
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			String sql = "select * from ITSM_CFG_FORMS where form_oid="+oid;
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				FormInfo info = new FormInfo();
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
	 * 返回具有指定模块名称的所有字段
	 * @param moduleName 参见ModuleName定义
	 * @return List<FormInfo>
	 */
	public static List<FormInfo> getFormsOfModule(String moduleName) {
		return getFormsOfModule(ModuleName.moduleName2Oid(moduleName));
	}
	public static List<FormInfo> getFormsOfModule(String origin,String moduleName) {
		if (origin==null || origin.equals(""))
			return getFormsOfModule(ModuleName.moduleName2Oid(moduleName));
		else
			return getFormsOfModule(origin,ModuleName.moduleName2Oid(moduleName));
	}
	
	public static List<FormInfo> getFormsOfModule(int moduleOid) {
		List<FormInfo> returnList = new ArrayList<FormInfo>();
		Object[] flds = Cache.Forms.values().toArray();
		for (int i = 0; i < flds.length; i++) {
			FormInfo info = (FormInfo)flds[i];
			if (info.inModule(moduleOid))
				returnList.add(info);
		}
		return returnList;
	}
	
	public static List<FormInfo> getFormsOfModule(String origin,int moduleOid) {
		if (origin == null || origin.equals(""))
			return getFormsOfModule(moduleOid);
		List<FormInfo> returnList = new ArrayList<FormInfo>();
		Object[] flds = Cache.Forms.values().toArray();
		for (int i = 0; i < flds.length; i++) {
			FormInfo info = (FormInfo)flds[i];
			if (info.getOrigin().equals(origin) && info.inModule(moduleOid))
				returnList.add(info);
		}
		return returnList;
	}
	
	
	/**
	 * 更改
	 * @param info
	 * @param operName
	 * @throws SQLException
	 */
	synchronized public static OperationResult updateForm(Map<String,String> map, String operName) throws SQLException {
		OperationResult ret = new OperationResult();
		ResultSet rs = null;
		PreparedStatement ps = null;
		int oid = Integer.parseInt((String)map.get("fld_oid"));
		String name = (String)map.get("fld_name");
		String applyto = (String)map.get("fld_applyto");
		String xml = (String)map.get("fld_xml");
		String origin = (String)map.get("origin");
		
		OracleOperation u = new OracleOperation("ITSM_CFG_FORMS", operName);
		FormInfo form = new FormInfo();
		try {	
			boolean isNew = false;
			if (oid == -1) {
				isNew = true;
				oid = ItsmUtil.getSequence("form");
			}
			form.parse(oid, name, applyto, xml,origin);
			
			if (isNew) {
				u.setColumnData("FORM_OID", new ColumnData(
						ColumnData.TYPE_INTEGER, new Integer(oid)));
			}
			u.setColumnData("FORM_NAME", new ColumnData(
					ColumnData.TYPE_STRING, name));
			u.setColumnData("FORM_APPLYTO", new ColumnData(
					ColumnData.TYPE_STRING, applyto));
			u.setColumnData("FORM_CONFIGURE", new ColumnData(
					ColumnData.TYPE_CLOB, xml));

			if (isNew) {
				rs = u.getResultSet(null);
				u.executeInsert(rs);
			} else {
				ps = u.getStatement("FORM_OID=?");
				ps.setInt(1, oid);
				rs = ps.executeQuery();
				u.executeUpdate(rs);
			}
		} catch (Exception e) {
			ret.setSuccess(false);
			ret.setMessage(e.toString());
			e.printStackTrace();
		} finally {
			u.commit(rs);
		}
		
		if (ret.isSuccess()) {
			updateCache(form);
			
			DSMCenter dsmc = (DSMCenter)ContextUtil.getBean("DSMCenter");
			dsmc.publishEvent("FORM", form.getOid());
		}
		return ret;
	}
	
	/**
	 * 单条信息
	 * @param oid
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public static FormInfo getFormByOid(int oid) {
		return (FormInfo)Cache.Forms.get(new Integer(oid));
	}

	
	/**
	 * 获取所有
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public static List<FormInfo> getForms() {
		List<FormInfo> returnList = new ArrayList<FormInfo>();
		Object[] flds = Cache.Forms.values().toArray();
		for (int i = 0; i < flds.length; i++) {
			FormInfo info = (FormInfo)flds[i];
			returnList.add(info);
		}
		return returnList;
	}
}
