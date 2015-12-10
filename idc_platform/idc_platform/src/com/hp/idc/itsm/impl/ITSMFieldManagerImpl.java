package com.hp.idc.itsm.impl;

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
import com.hp.idc.itsm.configure.FieldInfo;
import com.hp.idc.itsm.configure.FieldManager;
import com.hp.idc.itsm.configure.ModuleName;
import com.hp.idc.itsm.dbo.ColumnData;
import com.hp.idc.itsm.dbo.OracleOperation;
import com.hp.idc.itsm.dsm.DSMCenter;
import com.hp.idc.itsm.inter.FieldManagerInterface;
import com.hp.idc.itsm.util.ItsmUtil;

public class ITSMFieldManagerImpl implements FieldManagerInterface {

	public static final String ITSM = "ITSM";

	public void initCache() throws SQLException, IOException, DocumentException {

		FieldManager.classInsab.put("ITSM", this.getClass().getName());
		FieldManager.classIns.put("ITSM", this);

		System.out.println("loading ITSM fields...");
		Cache.Fields = new HashMap();
		Cache.Fields2 = new HashMap();
		Cache.FieldTypes = new HashMap();

		loadFields();
		loadFieldMaps();
		System.out.println("loading ITSM fields end...");
	}

	public String getTypeDesc(String type) {
		return (String) Cache.FieldTypes.get(ITSM + "_" + type);
	}

	public void loadFieldMaps() {
		Cache.FieldTypes.put(ITSM + "_" + "com.hp.idc.itsm.configure.fields.PasswordFieldInfo", "密码");
		Cache.FieldTypes.put(ITSM + "_" + "com.hp.idc.itsm.configure.fields.IdFieldInfo", "自动生成ID");
		Cache.FieldTypes.put(ITSM + "_" + "com.hp.idc.itsm.configure.fields.CIFieldInfo", "配置项列表");
		Cache.FieldTypes
				.put(ITSM + "_" + "com.hp.idc.itsm.configure.fields.CheckboxFieldInfo", "复选框");
		Cache.FieldTypes.put(ITSM + "_" + "com.hp.idc.itsm.configure.fields.DateFieldInfo", "日期");
		Cache.FieldTypes.put(ITSM + "_" + "com.hp.idc.itsm.configure.fields.DateTimeFieldInfo",
				"日期时间");
		Cache.FieldTypes.put(ITSM + "_" + "com.hp.idc.itsm.configure.fields.FileFieldInfo", "附件");
		Cache.FieldTypes.put(ITSM + "_" + "com.hp.idc.itsm.configure.fields.IntegerFieldInfo", "整型");
		Cache.FieldTypes.put(ITSM + "_" + "com.hp.idc.itsm.configure.fields.PersonFieldInfo", "人员");
		Cache.FieldTypes.put(ITSM + "_" + "com.hp.idc.itsm.configure.fields.RadioFieldInfo", "单选框");
		Cache.FieldTypes.put(ITSM + "_" + "com.hp.idc.itsm.configure.fields.SelectFieldInfo", "列表型");
		Cache.FieldTypes.put(ITSM + "_" + "com.hp.idc.itsm.configure.fields.StringFieldInfo", "字符型");
		Cache.FieldTypes.put(ITSM + "_" + "com.hp.idc.itsm.configure.fields.TextButtonFieldInfo",
				"文本按钮型");
		Cache.FieldTypes.put(ITSM + "_" + "com.hp.idc.itsm.configure.fields.TreeFieldInfo", "树型列表");
		Cache.FieldTypes.put(ITSM + "_" + "com.hp.idc.itsm.configure.fields.CIRelationFieldInfo",
				"关联配置项");
		Cache.FieldTypes.put(ITSM + "_" + "com.hp.idc.itsm.configure.fields.URLFieldInfo", "超链接");
		Cache.FieldTypes.put(ITSM + "_" + "com.hp.idc.itsm.configure.fields.GridFieldInfo", "表格");
	}

	public void loadField(ResultSet rs) throws SQLException, IOException, DocumentException {
		String cls = rs.getString("fld_type");

		if (cls == null || cls.length() == 0)
			cls = "com.hp.idc.itsm.configure.fields.StringFieldInfo";
		FieldInfo obj = null;
		try {
			Class c = Class.forName(cls);
			obj = (FieldInfo) c.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		obj.parse(rs);
		obj.setOrigin(ITSM);
		updateCache(obj);
	}

	public void updateCache(FieldInfo obj) {
		if (obj != null) {
			Cache.Fields.put(ITSM + "_" + obj.getId(), obj);
			Cache.Fields2.put(ITSM + "_" + obj.getOid(), obj);
		}

	}

	public void loadFields() throws SQLException, IOException, DocumentException {
		ResultSet rs = null;
		OracleOperation u = new OracleOperation();
		PreparedStatement ps = null;
		try {
			String sql = "select * from ITSM_CFG_FIELDS";
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				loadField(rs);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
	}

	public void reloadField(int oid) throws SQLException, IOException, DocumentException {
		ResultSet rs = null;
		OracleOperation u = new OracleOperation();
		PreparedStatement ps = null;
		try {
			String sql = "select * from ITSM_CFG_FIELDS where FLD_OID=" + oid;
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();

			if (rs.next()) {
				loadField(rs);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
	}

	public Map getFieldTypes() {
		return Cache.FieldTypes;
	}

	/**
	 * 返回具有指定模块名称的所有字段
	 * 
	 * @param moduleName
	 *            参见ModuleName定义
	 * @param includeAll
	 *            是否包含公共的
	 * @return List<FieldInfo>
	 * @throws SQLException
	 * @throws IOException
	 */
	public List getFieldsOfModule(String moduleName, boolean includeAll) throws SQLException, IOException {
		return getFieldsOfModule(ModuleName.moduleName2Oid(moduleName), includeAll);
	}

	public List getFieldsOfModule(int moduleOid, boolean includeAll) throws SQLException, IOException {
		List returnList = new ArrayList();
		Object[] flds = Cache.Fields.values().toArray();
		for (int i = 0; i < flds.length; i++) {
			FieldInfo info = (FieldInfo) flds[i];
			if (info.inModule(moduleOid, includeAll)) {
				FieldInfo info_ = info.cloneFieldInfo();
				returnList.add(info_);
			}
		}
		return returnList;
	}

	synchronized public OperationResult updateField(Map map, String operName) throws SQLException {
		OperationResult ret = new OperationResult();
		ResultSet rs = null;
		PreparedStatement ps = null;
		String id = (String) map.get("fld_id");
		String name = (String) map.get("fld_name");
		String type = (String) map.get("fld_type");
		String applyto = (String) map.get("fld_applyto");
		int fld_oid = -1;
		if (map.get("fld_oid") != null && !map.get("fld_oid").equals("")) {
			fld_oid = Integer.parseInt((String) map.get("fld_oid"));
		}
		OracleOperation u = new OracleOperation("ITSM_CFG_FIELDS", operName);
		FieldInfo field = null;
		try {
			field = (FieldInfo) Class.forName(type).newInstance();
			String xml = field.getXmlConfig(map);
			boolean isNew = false;
			int oid = -1;

			FieldInfo field2 = getFieldById(id);
			if (field2 == null) {
				isNew = true;
				oid = ItsmUtil.getSequence("field");
			} else {
				if (field2.getOid() != fld_oid) {
					ret.setSuccess(false);
					ret.setMessage("字段" + id + "已存在");
					return ret;
				}
				oid = field2.getOid();
			}
			field.parse(oid, id, name, type, applyto, false, xml);

			if (isNew) {
				u.setColumnData("FLD_OID", new ColumnData(ColumnData.TYPE_INTEGER, new Integer(oid)));
				u.setColumnData("FLD_ID", new ColumnData(ColumnData.TYPE_STRING, id));
				u.setColumnData("FLD_APPLYTO", new ColumnData(ColumnData.TYPE_STRING, applyto));
				u.setColumnData("FLD_TYPE", new ColumnData('s', type));
				u.setColumnData("FLD_EDITABLE", new ColumnData('i', FieldInfo.TYPE_SYSTEM_NO));
			}
			u.setColumnData("FLD_NAME", new ColumnData(ColumnData.TYPE_STRING, name));

			u.setColumnData("FLD_CONFIGURE", new ColumnData(ColumnData.TYPE_CLOB, xml));

			if (isNew) {
				rs = u.getResultSet(null);
				u.executeInsert(rs);
			} else {
				ps = u.getStatement("FLD_OID=?");
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
			field.setOrigin(ITSM);
			updateCache(field);

			DSMCenter dsmc = (DSMCenter) ContextUtil.getBean("DSMCenter");
			dsmc.publishEvent("FIELD", field.getOid());
		}
		return ret;
	}

	/**
	 * 单条信息
	 * 
	 * @param oid
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public FieldInfo getFieldByOid(int oid) {
		return getFieldByOid(oid, true);
	}

	public FieldInfo getFieldById(String id) {
		return getFieldById(id, true);
	}

	/**
	 * 
	 * @param oid
	 * @param clone
	 *            ,是否返回原对象的拷贝，（因为拷贝对象比较耗时）
	 * @return
	 */
	public FieldInfo getFieldByOid(int oid, boolean clone) {
		Object o = Cache.Fields2.get(ITSM + "_" + oid);
		if (o != null) {
			FieldInfo info = (FieldInfo) o;
			if (clone) {
				FieldInfo info_ = info.cloneFieldInfo();
				return info_;
			} else
				return info;
		}
		return null;
	}

	public FieldInfo getFieldById(String id, boolean clone) {
		Object o = Cache.Fields.get(ITSM + "_" + id);
		if (o == null)
			o = Cache.Fields.get(ITSM + "_" + id.toUpperCase());
		if (o != null) {
			FieldInfo info = (FieldInfo) o;
			if (clone) {
				FieldInfo info_ = info.cloneFieldInfo();
				return info_;
			} else
				return info;
		}
		return null;
	}

	/**
	 * 获取所有
	 * 
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public List getFields() throws SQLException, IOException {
		List returnList = new ArrayList();
		Object[] flds = Cache.Fields.values().toArray();
		for (int i = 0; i < flds.length; i++) {
			FieldInfo info = (FieldInfo) flds[i];
			if (info.getOrigin().equals(ITSM))
				returnList.add(info);
		}
		return returnList;
	}
}
