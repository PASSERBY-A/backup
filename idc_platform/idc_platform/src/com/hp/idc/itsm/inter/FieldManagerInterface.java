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
	 * ���ؾ���ָ��ģ�����Ƶ������ֶ�
	 * @param moduleName  �μ�ModuleName����
	 * @param includeAll �Ƿ����������
	 * @return List<FieldInfo>
	 * @throws SQLException
	 * @throws IOException
	 */
	public List getFieldsOfModule(String moduleName,boolean includeAll) throws SQLException,IOException;
	
	public List getFieldsOfModule(int moduleOid,boolean includeAll) throws SQLException,IOException;

	public OperationResult updateField(Map map, String operName) throws SQLException;
	
	/**
	 * ������Ϣ
	 * @param oid
	 * @return ���صĶ�����ԭ����Ŀ���
	 * @throws SQLException
	 * @throws IOException
	 */
	public FieldInfo getFieldByOid(int oid);
	
	public FieldInfo getFieldById(String id);
	
	/**
	 * 
	 * @param oid
	 * @param clone,�Ƿ񷵻�ԭ����Ŀ���������Ϊ��������ȽϺ�ʱ��
	 * @return
	 */
	public FieldInfo getFieldByOid(int oid,boolean clone);
	
	public FieldInfo getFieldById(String id,boolean clone);

	/**
	 * ��ȡ����
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public List getFields() throws SQLException, IOException;
}
