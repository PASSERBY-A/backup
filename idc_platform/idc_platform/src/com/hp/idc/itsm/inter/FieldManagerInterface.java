package com.hp.idc.itsm.inter;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;

import com.hp.idc.itsm.common.OperationResult;
import com.hp.idc.itsm.configure.FieldInfo;

public interface FieldManagerInterface {

	public void initCache() throws SQLException, IOException, DocumentException;
	
	public String getTypeDesc(String type);
	
	void loadFieldMaps();
	
	void loadField(ResultSet rs)  throws SQLException, IOException, DocumentException;
	
	void updateCache(FieldInfo obj);

	void loadFields() throws SQLException, IOException, DocumentException;
	
	public Map getFieldTypes();
	
	/**
	 * 返回具有指定模块名称的所有字段
	 * @param moduleName  参见ModuleName定义
	 * @param includeAll 是否包含公共的
	 * @return List<FieldInfo>
	 * @throws SQLException
	 * @throws IOException
	 */
	public List getFieldsOfModule(String moduleName,boolean includeAll) throws SQLException,IOException;
	
	public List getFieldsOfModule(int moduleOid,boolean includeAll) throws SQLException,IOException;

	public OperationResult updateField(Map map, String operName) throws SQLException;
	
	/**
	 * 单条信息
	 * @param oid
	 * @return 返回的对象是原对象的拷贝
	 * @throws SQLException
	 * @throws IOException
	 */
	public FieldInfo getFieldByOid(int oid);
	
	public FieldInfo getFieldById(String id);
	
	/**
	 * 
	 * @param oid
	 * @param clone,是否返回原对象的拷贝，（因为拷贝对象比较耗时）
	 * @return
	 */
	public FieldInfo getFieldByOid(int oid,boolean clone);
	
	public FieldInfo getFieldById(String id,boolean clone);

	/**
	 * 获取所有
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public List getFields() throws SQLException, IOException;
}
