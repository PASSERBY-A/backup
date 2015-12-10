package com.hp.idc.itsm.ci;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;

import com.hp.idc.context.util.ContextUtil;
import com.hp.idc.itsm.common.Cache;
import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.common.OperationResult;
import com.hp.idc.itsm.configure.FieldInfo;
import com.hp.idc.itsm.configure.FieldManager;
import com.hp.idc.itsm.configure.FormInfo;
import com.hp.idc.itsm.configure.FormManager;
import com.hp.idc.itsm.dbo.ColumnData;
import com.hp.idc.itsm.dbo.OracleOperation;
import com.hp.idc.itsm.dsm.DSMCenter;
import com.hp.idc.itsm.util.DateTimeUtil;
import com.hp.idc.itsm.util.ItsmUtil;
import com.hp.idc.itsm.util.StringUtil;
import com.hp.idc.itsm.util.TreeUtil;

/**
 * 表示CI管理类
 * 
 * @author 梅园
 * 
 */
public class CIManager {

	/**
	 * 获取所有关联关系类型
	 * 
	 * @return 返回所有关联关系类型List<RelationTypeInfo>
	 */
	public static List getRelationTypes() {
		List l = new ArrayList();
		Object[] objs = Cache.RelationTypes.values().toArray();
		for (int i = 0; i < objs.length; i++)
			l.add(objs[i]);
		return l;
	}

	/**
	 * 获取具有指定oid的关联关系类型
	 * 
	 * @param oid
	 *            指定oid
	 * @return 返回找到的关联关系类型
	 */
	public static RelationTypeInfo getRelationTypeByOid(int oid) {
		return (RelationTypeInfo) Cache.RelationTypes.get(new Integer(oid));
	}

	/**
	 * 获取具有指定oid的配置项
	 * 
	 * @param oid
	 *            指定oid
	 * @return 返回找到的配置项
	 */
	public static CIInfo getCIByOid(int oid) {
		Object o = Cache.CIs2.get(new Integer(oid));
		if (o != null)
			return (CIInfo) o;
		return null;
	}

	/**
	 * 获取具有指定id的配置项
	 * 
	 * @param id
	 *            指定id
	 * @return 返回找到的配置项
	 */
	public static CIInfo getCIById(String id) {
		Object o = Cache.CIs.get(id);
		if (o != null)
			return (CIInfo) o;
		return null;
	}

	/**
	 * 更新配置项属性
	 * 
	 * @deprecated
	 * @param info
	 *            配置项对象
	 * @param map
	 *            字段名->字段值
	 * @param operName
	 *            操作人
	 * @return 操作结果
	 * @throws SQLException
	 */
	public static OperationResult updateCI(CIInfo info, Map map, String operName)
			throws SQLException {
		Map m = new HashMap();
		m.put("fld_ci_category", "" + info.getType());
		m.put("fld_oid", "" + info.getOid());
		m.put("fld_ci_admin", info.getAdmin());
		m.put("fld_ci_id", info.getId());
		m.put("fld_ci_status", "" + info.getStatus());
		CIInfo p = CIManager.getCIByOid(info.getParentOid());
		if (p == null)
			m.put("fld_ci_parent", "");
		else
			m.put("fld_ci_parent", p.getId());
		m.put("fld_name", info.getName());
		Object[] keys = map.keySet().toArray();
		String key = "";
		for (int i = 0; i < keys.length; i++) {
			if (i > 0)
				key += ",";
			key += keys[i].toString();
			m.put(keys[i], map.get(keys[i]));
		}
		m.put("_form_fields_", key);
		return updateCI(m, operName);
	}

	/**
	 * 调整配置项表单字段
	 * 
	 * @param fieldList
	 *            表单字段列表List<FieldInfo>
	 */
	public static void adjectCIFormFields(List fieldList) {
		for (int i = 0; i < fieldList.size(); i++) {
			FieldInfo fieldInfo = (FieldInfo) fieldList.get(i);
			String s = fieldInfo.getId();
			if (s.equals("ci_id") || s.equals("name") || s.equals("ci_admin")
					|| s.equals("ci_category") || s.equals("ci_status")) {
				fieldList.remove(i);
				i--;
				continue;
			}
		}
		fieldList.add(0, FieldManager.getFieldById("ITSM", "ci_status").clone(
				false, true, "", "", false));
		fieldList.add(0, FieldManager.getFieldById("ITSM", "ci_admin").clone(
				false, true, "", "", false));
		fieldList.add(0, FieldManager.getFieldById("ITSM", "name").clone(false,
				true, "", "", false));
		fieldList.add(0, FieldManager.getFieldById("ITSM", "ci_id").clone(
				false, true, "", "", false));
		fieldList.add(0, FieldManager.getFieldById("ITSM", "ci_category")
				.clone(false, true, "", "", false));
	}

	/**
	 * 更新配置项
	 * 
	 * @deprecated
	 * @param map
	 *            前台传过来的表单MAP<br>
	 *            fld_ci_category=配置项类别<br>
	 *            fld_oid=配置项oid, 新增时为-1<br>
	 *            fld_ci_admin=配置项管理员<br>
	 *            fld_ci_id=配置项ID<br>
	 *            fld_ci_status=配置项状态<br>
	 *            fld_ci_parent=父配置项id<br>
	 *            fld_name=配置项名称<br>
	 *            _form_fields_=更新的字段，以","分隔，为空时按配置项关联的表单更新<br>
	 * @param operName
	 *            操作人
	 * @return 操作结果
	 * @throws SQLException
	 */
	synchronized public static OperationResult updateCI(Map map, String operName)
			throws SQLException {
		OperationResult ret = new OperationResult();
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation("ITSM_CI", operName);
		CIInfo oldCI = null;
		CIInfo info = null;
		try {
			int type = Integer.parseInt((String) map.get("fld_ci_category"));
			CICategoryInfo category = getCICategoryByOid(type);
			if (category == null) {
				ret.setSuccess(false);
				ret.setMessage("找不到配置项类别！");
				return ret;
			}
			info = (CIInfo) category.getObjectClass().newInstance();
			int oid = Integer.parseInt((String) map.get("fld_oid"));
			boolean isNew = false;
			if (oid == -1) {
				oid = ItsmUtil.getSequence("ci");
				isNew = true;
				info.setOid(oid);
				info.setCreateTime(System.currentTimeMillis());
				info.setXmlData("<ci/>");
				info.setAdmin(operName);
			} else {
				oldCI = getCIByOid(oid);
				if (oldCI == null) {
					ret.setSuccess(false);
					ret.setMessage("系统内部错误！");
					return ret;
				}
				info.cloneAttribute(oldCI);
			}
			if (map.get("fld_ci_admin") == null)
				map.put("fld_ci_admin", info.getAdmin());

			info.setLastUpdate(System.currentTimeMillis());
			info.setId((String) map.get("fld_ci_id"));
			info.setName((String) map.get("fld_name"));
			info.setAdmin((String) map.get("fld_ci_admin"));
			info.setType(type);
			info.setStatus(Integer.parseInt((String) map.get("fld_ci_status")));

			CIInfo parentCI = getCIById((String) map.get("fld_ci_parent"));
			if (parentCI == null)
				info.setParentOid(-1);
			else {
				info.setParentOid(parentCI.getOid());
				info.setParent(parentCI);
				if (oid == info.getParentOid()) {
					ret.setSuccess(false);
					ret.setMessage("父配置项无法设置为自身");
					return ret;
				}
				for (;;) {
					parentCI = (CIInfo) parentCI.getParent();
					if (parentCI == null)
						break;
					if (parentCI.getOid() == oid) {
						ret.setSuccess(false);
						ret.setMessage("父配置项设置中出现循环");
						return ret;
					}
				}
			}

			if (isNew && getCIById(info.getId()) != null) {
				ret.setSuccess(false);
				ret.setMessage("配置项ID已存在");
				return ret;
			}

			String relation = "";
			List fields = new ArrayList();
			boolean updateFieldMode = (map.get("_form_fields_") != null);
			if (updateFieldMode) {
				String[] ss = map.get("_form_fields_").toString().split(",");
				for (int i = 0; i < ss.length; i++) {
					FieldInfo field = FieldManager.getFieldById("ITSM", ss[i]);
					if (field != null)
						fields.add(field);
					else
						System.out.println(ss[i] + " not found.");
				}
			} else {
				FormInfo form = FormManager.getFormByOid(category.getFormOid());
				if (form != null) {
					fields = form.getFields();
					adjectCIFormFields(fields);
				}
			}
			for (int i = 0; i < fields.size(); i++) {
				FieldInfo field = (FieldInfo) fields.get(i);
				if (updateFieldMode)
					info.setAttribute(field.getId(), (String) map.get(field
							.getId()), operName);
				else
					info.setAttribute(field.getId(),
							field.getRequestValue(map), operName);
				if (field.getRelationType() == Consts.RELATION_CI_CI) {
					String r = field.getRequestValue(map);
					if (r != null && r.length() > 0) {
						if (relation.length() > 0)
							relation = relation + ";" + r;
						else
							relation = field.getRequestValue(map);
					}
				}
			}
			info.setRelationDesc(relation);

			String rel = info.checkRelations(oldCI);
			if (rel != null && rel.length() > 0) {
				ret.setSuccess(false);
				ret.setMessage(rel);
				return ret;
			}

			if (isNew) {
				u.setColumnData("CI_OID", new ColumnData(
						ColumnData.TYPE_INTEGER, new Integer(oid)));
				u.setColumnData("CI_CREATETIME", new ColumnData(
						ColumnData.TYPE_DATETIME, DateTimeUtil.formatDate(info
								.getCreateTime(), "yyyyMMddHHmmss")));
			}
			u.setColumnData("CI_ID", new ColumnData(ColumnData.TYPE_STRING,
					info.getId()));
			u.setColumnData("CI_LASTUPDATE", new ColumnData(
					ColumnData.TYPE_DATETIME, DateTimeUtil.formatDate(info
							.getLastUpdate(), "yyyyMMddHHmmss")));
			u.setColumnData("CI_STATUS", new ColumnData(
					ColumnData.TYPE_INTEGER, new Integer(info.getStatus())));
			u.setColumnData("CI_TYPE_OID", new ColumnData(
					ColumnData.TYPE_INTEGER, new Integer(info.getType())));
			u.setColumnData("CI_PARENT_OID", new ColumnData(
					ColumnData.TYPE_INTEGER, new Integer(info.getParentOid())));
			u.setColumnData("CI_ADMIN", new ColumnData(ColumnData.TYPE_STRING,
					info.getAdmin()));
			u.setColumnData("CI_NAME", new ColumnData(ColumnData.TYPE_STRING,
					info.getName()));
			u.setColumnData("CI_DATA", new ColumnData(ColumnData.TYPE_CLOB,
					info.getXmlData()));

			if (isNew) {
				rs = u.getResultSet(null);
				u.executeInsert(rs);
			} else {
				ps = u.getStatement("CI_OID=?");
				ps.setInt(1, oid);
				rs = ps.executeQuery();
				u.executeUpdate(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ret.setSuccess(false);
			ret.setMessage(e.toString());
		} finally {
			u.commit(rs);
		}

		if (ret.isSuccess()) {
			updateCICache(info, oldCI, false);
		}
		return ret;
	}

	/**
	 * 更新代码
	 * 
	 * @param map
	 *            前台传过来的表单MAP
	 * @param operName
	 *            操作人
	 * @return 操作结果
	 * @throws SQLException
	 */
	synchronized public static OperationResult updateCode(Map map,
			String operName) throws SQLException {
		OperationResult ret = new OperationResult();
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation("ITSM_CODES", operName);
		CodeInfo oldInfo = null;
		CodeInfo info = new CodeInfo();
		try {
			int type = Integer.parseInt((String) map.get("fld_type_oid"));
			CodeTypeInfo codeType = getCodeTypeByOid(type);
			//info = (CodeInfo) codeType.getObjectClass().newInstance();
			info.setTypeOid(type);
			int oid = Integer.parseInt((String) map.get("fld_oid"));
			boolean isNew = false;
			if (oid == -1) {
				oid = ItsmUtil.getSequence("code");
				isNew = true;
			} else if ((oldInfo = getCodeByOid(oid)) == null) {
				ret.setSuccess(false);
				ret.setMessage("系统内部错误！");
				return ret;
			}
			info.setOid(oid);
			int parent_oid = StringUtil.parseInt((String) map
					.get("fld_parent_oid"), -1);
			info.setParentOid(parent_oid);
			info.setParent(getCodeByOid(info.getParentOid()));
			if (parent_oid != -1) {
				if (parent_oid == oid) {
					ret.setSuccess(false);
					ret.setMessage("父节点不能是本身");
					return ret;
				}
				CodeInfo p = (CodeInfo) info.getParent();
				if (p == null) {
					ret.setSuccess(false);
					ret.setMessage("找不到父节点");
					return ret;
				}
				for (; p != null; p = (CodeInfo) p.getParent()) {
					if (p.getOid() == oid) {
						ret.setSuccess(false);
						ret.setMessage("无法设置父节点");
						return ret;
					}
				}
			}
			if (info.getParentOid() != -1 && info.getParent() == null) {
				ret.setSuccess(false);
				ret.setMessage("父节点不存在");
				return ret;
			}
			info.setName((String) map.get("fld_name"));
			info.setDesc((String) map.get("fld_desc"));
			info.setCodeId((String) map.get("fld_id"));
			info.setIconOid(StringUtil.parseInt((String) map
					.get("fld_icon_oid"), -1));
			info.setEnabled(map.get("fld_enabled") != null);
			info.setClickable(map.get("fld_clickable") != null);
			info
					.setOrder(StringUtil.parseInt(
							(String) map.get("fld_order"), 0));
			info.processPost(map);
			info.setAlertMsg((String) map.get("fld_alertMsg"));

			if (isNew && getCodeByOid(oid) != null) {
				ret.setSuccess(false);
				ret.setMessage("代码已存在");
				return ret;
			}

			if (isNew) {
				u.setColumnData("CODE_OID", new ColumnData(
						ColumnData.TYPE_INTEGER, new Integer(oid)));
			}
			u.setColumnData("CODE_PARENT_OID", new ColumnData(
					ColumnData.TYPE_INTEGER, new Integer(info.getParentOid())));
			u.setColumnData("CODE_TYPE_OID", new ColumnData(
					ColumnData.TYPE_INTEGER, new Integer(info.getTypeOid())));
			u.setColumnData("CODE_NAME", new ColumnData(ColumnData.TYPE_STRING,
					info.getName()));
			u.setColumnData("CODE_DESC", new ColumnData(ColumnData.TYPE_STRING,
					info.getDesc()));
			u.setColumnData("CODE_ICON_OID", new ColumnData(
					ColumnData.TYPE_INTEGER, new Integer(info.getIconOid())));
			u.setColumnData("CODE_ENABLED", new ColumnData(
					ColumnData.TYPE_INTEGER, new Integer(info.isEnabled() ? 1
							: 0)));
			u.setColumnData("CODE_ORDER", new ColumnData(
					ColumnData.TYPE_INTEGER, new Integer(info.getOrder())));
			u.setColumnData("CODE_DATA", new ColumnData(ColumnData.TYPE_CLOB,
					info.getXmlData()));
			u.setColumnData("CODE_ID", new ColumnData(ColumnData.TYPE_STRING,
					info.getCodeId()));
			u.setColumnData("CODE_CLICK", new ColumnData(
					ColumnData.TYPE_INTEGER, new Integer(info.isClickable() ? 1
							: 0)));

			if (isNew) {
				rs = u.getResultSet(null);
				u.executeInsert(rs);
			} else {
				ps = u.getStatement("CODE_OID=?");
				ps.setInt(1, oid);
				rs = ps.executeQuery();
				u.executeUpdate(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ret.setSuccess(false);
			ret.setMessage(e.toString());
		} finally {
			u.commit(rs);
		}

		if (ret.isSuccess()) {
			updateCodeCache(info, oldInfo, false);

			DSMCenter dsmc = (DSMCenter) ContextUtil.getBean("DSMCenter");
			dsmc.publishEvent("CODE", info.getOid());
		}
		return ret;
	}

	/**
	 * 更新代码类型
	 * 
	 * @param map
	 *            前台传过来的表单MAP
	 * @param operName
	 *            操作人
	 * @return 操作结果
	 * @throws SQLException
	 */
	synchronized public static OperationResult updateCodeType(Map map,
			String operName) throws SQLException {
		OperationResult ret = new OperationResult();
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation("ITSM_CODE_TYPE", operName);
		CodeTypeInfo oldInfo = null;
		CodeTypeInfo info = new CodeTypeInfo();
		try {
			int oid = Integer.parseInt((String) map.get("fld_oid"));
			boolean isNew = false;
			if (oid == -1) {
				oid = ItsmUtil.getSequence("codetype");
				isNew = true;
				info.setOid(oid);
				info.setClass(null);
			} else {
				oldInfo = getCodeTypeByOid(oid);
				if (oldInfo == null) {
					ret.setSuccess(false);
					ret.setMessage("系统内部错误！");
					return ret;
				}
				info = new CodeTypeInfo(oldInfo);
			}

			info.setName((String) map.get("fld_name"));
			info.setType(Integer.parseInt((String) map.get("fld_type")));
			info.setEnabled(map.get("fld_enabled") != null);
			info.setCatalog(Integer.parseInt((String) map.get("fld_catalog")));

			if (isNew && getCodeTypeByOid(oid) != null) {
				ret.setSuccess(false);
				ret.setMessage("代码类型已存在");
				return ret;
			}

			if (isNew) {
				u.setColumnData("TYPE_OID", new ColumnData(
						ColumnData.TYPE_INTEGER, new Integer(oid)));
			}
			u.setColumnData("TYPE_NAME", new ColumnData(ColumnData.TYPE_STRING,
					info.getName()));
			u.setColumnData("TYPE_TYPE", new ColumnData(
					ColumnData.TYPE_INTEGER, new Integer(info.getType())));
			u.setColumnData("TYPE_ENABLED", new ColumnData(
					ColumnData.TYPE_INTEGER, new Integer(info.isEnabled() ? 1
							: 0)));
			u.setColumnData("TYPE_CATALOG", new ColumnData(
					ColumnData.TYPE_INTEGER, new Integer(info.getCatalog())));

			if (isNew) {
				rs = u.getResultSet(null);
				u.executeInsert(rs);
			} else {
				ps = u.getStatement("TYPE_OID=?");
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

		if (ret.isSuccess()) {
			updateCodeTypeCache(info, oldInfo);

			DSMCenter dsmc = (DSMCenter) ContextUtil.getBean("DSMCenter");
			dsmc.publishEvent("CODETYPE", info.getOid());
		}
		return ret;
	}

	/**
	 * 获取所有代码类型
	 * 
	 * @return 返回所有代码类型List<CodeTypeInfo>
	 */
	public static List getCodeTypes() {
		Object types[] = Cache.CodeTypes.values().toArray();
		List list = new ArrayList();
		for (int i = 0; i < types.length; i++) {
			list.add(types[i]);
		}
		return list;
	}

	/**
	 * 按类别返回代码类型
	 * 
	 * @param catalogId
	 * @return
	 */
	public static List getCodeTypesOfCatalog(int catalogId) {
		Object types[] = Cache.CodeTypes.values().toArray();
		List list = new ArrayList();
		for (int i = 0; i < types.length; i++) {
			CodeTypeInfo ctInfo = (CodeTypeInfo) types[i];
			if (ctInfo.getCatalog() == catalogId)
				list.add(types[i]);
		}
		return list;
	}

	/**
	 * 获取具有指定oid的代码类型
	 * 
	 * @param oid
	 *            指定oid
	 * @return 返回找到的代码类型
	 */
	public static CodeTypeInfo getCodeTypeByOid(int oid) {
		return (CodeTypeInfo) Cache.CodeTypes.get(new Integer(oid));
	}

	/**
	 * 获取具有指定oid的代码
	 * 
	 * @param oid
	 *            指定oid
	 * @return 返回找到的代码
	 */
	public static CodeInfo getCodeByOid(int oid) {
		if (Cache.AllCodes.get(new Integer(oid)) != null)
			return (CodeInfo) Cache.AllCodes.get(new Integer(oid));
		else
			return null;
	}

	/**
	 * 根据代码类型、代码ID，查找代码对象
	 * 
	 * @param id
	 *            代码ID
	 * @param codeTypeOid
	 *            代码类型OID
	 * @return
	 */
	public static CodeInfo getCodeById(String id, int codeTypeOid) {
		if (Cache.AllCodes2.get(codeTypeOid + "_" + id) != null)
			return (CodeInfo) Cache.AllCodes2.get(codeTypeOid + "_" + id);
		else
			return null;
	}

	/**
	 * 根据代码类型、代码名称查找代码对象
	 * 
	 * @param text
	 *            代码名称
	 * @param codeTypeOid
	 *            代码类型OID
	 * @return
	 */
	public static CodeInfo getCodeByText(String text, int codeTypeOid) {
		List<CodeInfo> l = new ArrayList<CodeInfo>();
		if (Cache.Codes.get(new Integer(codeTypeOid)) != null)
			l = (List) Cache.Codes.get(new Integer(codeTypeOid));
		for (int i = 0; i < l.size(); i++) {
			CodeInfo ci = l.get(i);
			if (ci.getName().equals(text))
				return ci;
		}
		return null;
	}

	/**
	 * 获取具有指定的代码类型的代码
	 * 
	 * @param oid
	 *            代码类型oid
	 * @return 返回具有指定的代码类型的代码List<CodeInfo>
	 */
	public static List<CodeInfo> getCodesByTypeOid(int oid) {
		List<CodeInfo> ret = new ArrayList<CodeInfo>();
		Object o = Cache.CodesTree.get(new Integer(oid));
		if (o != null)
			ret = (List<CodeInfo>) o;
		else {
			Cache.CodesTree.put(new Integer(oid), ret);
		}
		return ret;
	}

	/**
	 * 
	 * @param oid
	 * @param includeAll
	 *            是否包含不启用的
	 * @return
	 */
	public static List<CodeInfo> getCodesByTypeOid(int oid, boolean includeAll) {
		List<CodeInfo> codeList = getCodesByTypeOid(oid);
		List<CodeInfo> retList = new ArrayList<CodeInfo>();
		if (!includeAll) {
			for (int i = 0; i < codeList.size(); i++) {
				CodeInfo ci = (CodeInfo) codeList.get(i);
				if (ci.isEnabled())
					retList.add(ci);
			}
		} else {
			if (codeList != null && codeList.size() > 0)
				retList.addAll(codeList);
		}
		return retList;
	}

	/**
	 * @deprecated
	 * @param oid
	 * @return
	 */
	public static List getCIByCategoryId(int oid) {
		Object ret = Cache.CategoryToCI.get(oid + "");
		if (ret != null)
			return (List) ret;
		else
			return new ArrayList();
	}

	/**
	 * 获取具有指定oid的配置项类别
	 * 
	 * @deprecated
	 * @param oid
	 *            指定的oid
	 * @return 返回找到的配置项类别
	 */
	public static CICategoryInfo getCICategoryByOid(int oid) {
		Object o = Cache.CICategory.get(new Integer(oid));
		CICategoryInfo info = null;
		if (o != null)
			info = (CICategoryInfo) o;
		return info;
	}

	/**
	 * 获取配置项的关联关系列表
	 * 
	 * @deprecated
	 * @param oid
	 *            配置项oid
	 * @param type
	 *            关系类型, <=0时查找所有关系
	 * @param includeRev
	 *            是否包含逆向关系
	 * @return 返回找到的关联关系列表
	 */
	public static List getCIRelations(int oid, int type, boolean includeRev) {
		Object objs[] = Cache.Relations.values().toArray();
		RelationTypeInfo rt = CIManager.getRelationTypeByOid(type);
		List ret = new ArrayList();
		for (int i = 0; i < objs.length; i++) {
			RelationInfo r = (RelationInfo) objs[i];
			if (r.getObjectA().getOid() == oid) {
				if (type > 0 && type != r.getType().getOid())
					continue;
				ret.add(r);
			} else if (includeRev) {
				if (r.getObjectB().getOid() != oid)
					continue;
				if (type > 0 && rt != null
						&& rt.getReverseOid() != r.getType().getOid())
					continue;
				ret.add(r);
			}

		}
		return ret;
	}

	/**
	 * @deprecated
	 * @param id
	 * @param type
	 * @param includeRev
	 * @return
	 */
	public static List getCIRelations(String id, int type, boolean includeRev) {
		Object objs[] = Cache.Relations.values().toArray();
		RelationTypeInfo rt = CIManager.getRelationTypeByOid(type);
		List ret = new ArrayList();
		for (int i = 0; i < objs.length; i++) {
			RelationInfo r = (RelationInfo) objs[i];
			if (r.getObjectA().getId().equals(id)) {
				if (type > 0 && type != r.getType().getOid())
					continue;
				ret.add(r);
			} else if (includeRev) {
				if (!r.getObjectB().getId().equals(id))
					continue;
				if (type > 0 && rt != null
						&& rt.getReverseOid() != r.getType().getOid())
					continue;
				ret.add(r);
			}

		}
		return ret;
	}

	/**
	 * 更新配置项缓存
	 * 
	 * @deprecated
	 * @param info
	 *            新的配置项对象
	 * @param old
	 *            原配置项对象
	 * @param isLoading
	 *            是否正在初始化缓存
	 */
	protected static void updateCICache(CIInfo info, CIInfo old,
			boolean isLoading) {
		if (old != null) {
			if (!old.getId().equals(info.getId())) {
				CICategoryInfo typeInfo = getCICategoryByOid(info.getType());
				typeInfo.removeCI(old);
				Cache.CIs.remove(old.getId());
			}
			Object objs[] = old.getSubItems().toArray();
			for (int i = 0; i < objs.length; i++) {
				CIInfo ci = (CIInfo) objs[i];
				ci.setParent(info);
				info.getSubItems().add(objs[i]);
			}

			List l = getCIRelations(old.getOid(), -1, true);
			for (int i = 0; i < l.size(); i++) {
				RelationInfo r = (RelationInfo) l.get(i);
				if (r.getObjectA() == old)
					Cache.Relations.remove(r.getId());
				else
					r.setObjectB(info);
			}
		}
		Cache.CIs.put(info.getId(), info);
		Cache.CIs2.put(new Integer(info.getOid()), info);
		CICategoryInfo typeInfo = getCICategoryByOid(info.getType());
		typeInfo.addCI(info);
		if (!isLoading)
			updateCIRelationCache(info);
	}

	/**
	 * 更新配置项关系缓存
	 * 
	 * @deprecated
	 * @param info
	 *            配置项
	 */
	protected static void updateCIRelationCache(CIInfo info) {
		String str = info.getRelationDesc();
		if (str == null || str.length() == 0)
			return;
		String[] vals = str.split(";");
		for (int i = 0, pos; i < vals.length; i++) {
			if (vals[i].length() == 0)
				continue;
			String s = vals[i];
			if ((pos = s.lastIndexOf("-")) == -1)
				continue;
			int a = Integer.parseInt(s.substring(0, pos));
			int b = Integer.parseInt(s.substring(pos + 1));
			CIInfo item = CIManager.getCIByOid(b);
			RelationTypeInfo rtInfo = CIManager.getRelationTypeByOid(a);
			if (item != null && rtInfo != null) {
				RelationInfo ri = new RelationInfo();
				ri.setObjectA(info);
				ri.setObjectB(item);
				ri.setType(rtInfo);
				Cache.Relations.put(ri.getId(), ri);
			}
		}
	}

	/**
	 * 更新代码类型缓存
	 * 
	 * @param info
	 *            新代码类型对象
	 * @param oldInfo
	 *            原代码类型对象
	 */
	protected static void updateCodeTypeCache(CodeTypeInfo info,
			CodeTypeInfo oldInfo) {
		Cache.CodeTypes.put(new Integer(info.getOid()), info);
		if (Cache.Codes.get(new Integer(info.getOid())) == null)
			Cache.Codes.put(new Integer(info.getOid()), new ArrayList());
	}

	/**
	 * 更新代码缓存
	 * 
	 * @param info
	 *            新的代码
	 * @param oldInfo
	 *            原代码
	 * @param isInit
	 *            是否为初始化缓存模式
	 */
	protected static void updateCodeCache(CodeInfo info, CodeInfo oldInfo,
			boolean isInit) {
		if (info == null)
			return;
		Cache.AllCodes.put(new Integer(info.getOid()), info);
		Cache.AllCodes2.put(info.getTypeOid() + "_" + info.getCodeId(), info);
		if (isInit)
			return;
		List<CodeInfo> l = getCodesByTypeOid(info.getTypeOid());
		if (oldInfo != null) {
			Object objs[] = oldInfo.getSubItems().toArray();
			for (int i = 0; i < objs.length; i++)
				info.getSubItems().add(objs[i]);
			if (oldInfo.getParentOid() == -1) {
				if (!l.remove(oldInfo))
					System.out.println("EEEEEEEEEEEEEEEEEE1");
			} else {
				if (!oldInfo.getParent().getSubItems().remove(oldInfo))
					System.out.println("EEEEEEEEEEEEEEEEEE2");
			}
			oldInfo.onUpdateCache(info);
		}
		if (info.getParentOid() != -1)
			l = info.getParent().getSubItems();

		int pos = -1;
		for (int i = 0; i < l.size(); i++) {
			CodeInfo c = (CodeInfo) l.get(i);
			if (c.getOrder() > info.getOrder()) {
				pos = i;
				break;
			}
		}
		if (pos == -1)
			l.add(info);
		else
			l.add(pos, info);
	}

	/**
	 * 将数据读入缓存
	 * 
	 * @throws SQLException
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ClassNotFoundException
	 * @throws ParseException
	 */
	public void initCache() throws SQLException, DocumentException,
			IOException, ClassNotFoundException, ParseException {

		System.out.println("loading codetypes...");
		Cache.CodeTypes = new HashMap();
		loadCodeTypes();
		System.out.println("loading codes...");
		Cache.Codes = new HashMap();
		Cache.AllCodes = new HashMap();
		Cache.AllCodes2 = new HashMap();
		loadCodes();
		System.out.println("loading relationtypes...");
		Cache.RelationTypes = new HashMap();
		Cache.Relations = new HashMap();
		loadRelationTypes();
		//System.out.println("loading ci category...");
		//loadCICategory();
		System.out.println("loading ci...");
		Cache.CICategory = new HashMap();
		Cache.CIs = new HashMap();
		Cache.CIs2 = new HashMap();
		//loadCIs();
	}

	/**
	 * 加载缓存:配置项关联关系类型
	 * 
	 * @throws SQLException
	 */
	private static void loadRelationTypes() throws SQLException {
		Map<Integer, RelationTypeInfo> m = new HashMap<Integer, RelationTypeInfo>();
		List<RelationTypeInfo> list = new ArrayList<RelationTypeInfo>();
		String sql = "select * from itsm_ci_relation_type";
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				RelationTypeInfo info = new RelationTypeInfo();
				info.parse(rs);
				m.put(new Integer(info.getOid()), info);
				list.add(info);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		Cache.RelationTypes = m;
		for (int i = 0; i < list.size(); i++) {
			RelationTypeInfo info = (RelationTypeInfo) list.get(i);
			if (info.getReverseOid() != -1)
				info.setReverse((RelationTypeInfo) Cache.RelationTypes
						.get(new Integer(info.getReverseOid())));
		}
	}

	/**
	 * @throws SQLException
	 */
	private static void loadCICategory() throws SQLException {
		Map<Integer, CICategoryInfo> m = new HashMap<Integer, CICategoryInfo>();
		List<CICategoryInfo> list = new ArrayList<CICategoryInfo>();
		String sql = "select cat_oid,cat_parent_oid,cat_type,cat_name from CMDB_CI_CATEGORY";
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				CICategoryInfo catInfo = new CICategoryInfo();
				catInfo.setOid(rs.getInt("cat_oid"));
				catInfo.setParentOid(rs.getInt("cat_parent_oid"));
				catInfo.setTypeOid(rs.getInt("cat_type"));
				catInfo.setName(rs.getString("cat_name"));
				list.add(catInfo);
				m.put(new Integer(catInfo.getOid()), catInfo);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		Cache.CICategory = m;
		Cache.CICategoryTree = TreeUtil.makeTree(list);
	}

	/**
	 * 加载缓存:配置项
	 * 
	 * @throws SQLException
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ParseException
	 */
	private static void loadCIs() throws SQLException, DocumentException,
			IOException, ParseException {
		Map<String, CIInfo> m = new HashMap<String, CIInfo>();
		Map<Integer, CIInfo> m2 = new HashMap<Integer, CIInfo>();
		String sql = "select CI_OID,CI_ID,CI_STATUS,CI_CAT_OID,CI_NAME from CMDB_CI";
		ResultSet rs = null;
		PreparedStatement ps = null;
		List list = new ArrayList();
		OracleOperation u = new OracleOperation();
		try {
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				CIInfo info = new CIInfo();
				info.setOid(rs.getInt("CI_OID"));
				info.setId(rs.getString("CI_ID"));
				info.setStatus(rs.getInt("CI_STATUS"));
				info.setType(rs.getInt("CI_CAT_OID"));
				info.setName(rs.getString("CI_NAME"));
				list.add(info);
				m.put(info.getId(), info);
				m2.put(new Integer(info.getOid()), info);
				updateCategoryTOCI(info);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		Cache.CIs = m;
		Cache.CIs2 = m2;
	}

	/**
	 * @deprecated
	 * @param info
	 */
	public static void updateCategoryTOCI(CIInfo info) {
		int categoryOid = info.getType();
		Object o = Cache.CategoryToCI.get(categoryOid + "");
		if (o == null) {
			List l = new ArrayList();
			l.add(info);
			Cache.CategoryToCI.put(categoryOid + "", l);
		} else {
			List l = (List) o;
			l.add(info);
		}

	}

	/**
	 * 加载缓存:代码
	 * 
	 * @throws SQLException
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ClassNotFoundException
	 */
	private static void loadCodes() throws SQLException, DocumentException,
			IOException, ClassNotFoundException {
		Object c[] = Cache.CodeTypes.values().toArray();
		Map m = new HashMap();
		Map mt = new HashMap();
		for (int i = 0; i < c.length; i++) {
			CodeTypeInfo info = (CodeTypeInfo) c[i];
			List list = new ArrayList();
			List listTree = new ArrayList();
			String sql = "select * from itsm_codes where code_type_oid=? order by code_order";
			ResultSet rs = null;
			PreparedStatement ps = null;
			OracleOperation u = new OracleOperation();
			try {
				ps = u.getSelectStatement(sql);
				ps.setInt(1, info.getOid());
				rs = ps.executeQuery();
				while (rs.next()) {
					CodeInfo codeInfo = new CodeInfo();
//					try {
//						Object obj = info.getObjectClass().newInstance();
//						if (!(obj instanceof CodeInfo))
//							continue;
//						codeInfo = (CodeInfo) obj;
//					} catch (InstantiationException e) {
//						e.printStackTrace();
//						continue;
//					} catch (IllegalAccessException e) {
//						e.printStackTrace();
//						continue;
//					}
					codeInfo.parse(rs);
					list.add(codeInfo);
					listTree.add(codeInfo);
					updateCodeCache(codeInfo, null, true);
				}
			} catch (SQLException e) {
				throw e;
			} finally {
				u.commit(rs);
			}
			if (info.getType() == CodeTypeInfo.TYPE_TREE) {
				listTree = TreeUtil.makeTree(listTree);
			}
			m.put(new Integer(info.getOid()), list);
			mt.put(new Integer(info.getOid()), listTree);
		}
		Cache.Codes = m;
		Cache.CodesTree = mt;
	}

	public static void reloadCode(int oid) throws SQLException,
			DocumentException, IOException {

		String sql = "select * from itsm_codes where CODE_OID=?";
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		CodeInfo codeInfo = null;
		try {
			ps = u.getSelectStatement(sql);
			ps.setInt(1, oid);
			rs = ps.executeQuery();
			if (rs.next()) {
				codeInfo = new CodeInfo();
				codeInfo.parse(rs);
			}
			CodeInfo oldInfo = getCodeByOid(oid);
			updateCodeCache(codeInfo, oldInfo, false);
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}

	}

	/**
	 * 加载缓存:代码类型
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	private static void loadCodeTypes() throws SQLException,
			ClassNotFoundException {
		Map m = new HashMap();
		String sql = "select * from itsm_code_type";
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				CodeTypeInfo info = new CodeTypeInfo();
				info.parse(rs);
				m.put(new Integer(info.getOid()), info);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		Cache.CodeTypes = m;
	}

	public static void reloadCodeType(int oid) throws SQLException,
			ClassNotFoundException {
		String sql = "select * from itsm_code_type where TYPE_OID=" + oid;
		ResultSet rs = null;
		PreparedStatement ps = null;
		OracleOperation u = new OracleOperation();
		try {
			ps = u.getSelectStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				CodeTypeInfo info = new CodeTypeInfo();
				info.parse(rs);
				updateCodeTypeCache(info, null);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
	}

}
