/**
 * 
 */
package com.hp.idc.resm.model;

import java.sql.Connection;
import java.sql.ResultSet;

import com.hp.idc.resm.cache.CacheableObject;

/**
 * ���������Ӧ
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 *
 */
public class SearchCodeMapping extends CacheableObject {

	/**
	 * ���л�id
	 */
	private static final long serialVersionUID = 8134534176596064652L;
	
	/**
	 * ģ��id
	 */
	private String modelId;
	
	/**
	 * ��������ǰ׺
	 */
	private String prefix;

	/**
	 * ��ȡģ��id
	 * @return ģ��id
	 * @see #modelId
	 */
	public String getModelId() {
		return this.modelId;
	}

	/**
	 * ����ģ��id
	 * @param modelId ģ��id
	 * @see #modelId
	 */
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	/**
	 * ��ȡ��������ǰ׺
	 * @return ��������ǰ׺
	 * @see #prefix
	 */
	public String getPrefix() {
		return this.prefix;
	}

	/**
	 * ������������ǰ׺
	 * @param prefix ��������ǰ׺
	 * @see #prefix
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public String getPrimaryKey() {
		return this.modelId;
	}

	@Override
	public String getDatabaseTable() {
		return "resm_searchcode";
	}

	@Override
	public String getPrimaryKeyField() {
		return "modelid";
	}

	@Override
	public void updateResultSet(Connection conn, ResultSet rs) {
		// �������²���
	}

	@Override
	public void readFromResultSet(ResultSet rs) throws Exception {
		this.modelId = rs.getString("modelid");
		this.prefix = rs.getString("prefix");
	}
}
