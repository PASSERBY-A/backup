/**
 * 
 */
package com.hp.idc.resm.model;

import java.sql.Connection;
import java.sql.ResultSet;

import com.hp.idc.resm.cache.CacheableObject;

/**
 * 搜索代码对应
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 *
 */
public class SearchCodeMapping extends CacheableObject {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 8134534176596064652L;
	
	/**
	 * 模型id
	 */
	private String modelId;
	
	/**
	 * 搜索代码前缀
	 */
	private String prefix;

	/**
	 * 获取模型id
	 * @return 模型id
	 * @see #modelId
	 */
	public String getModelId() {
		return this.modelId;
	}

	/**
	 * 设置模型id
	 * @param modelId 模型id
	 * @see #modelId
	 */
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	/**
	 * 获取搜索代码前缀
	 * @return 搜索代码前缀
	 * @see #prefix
	 */
	public String getPrefix() {
		return this.prefix;
	}

	/**
	 * 设置搜索代码前缀
	 * @param prefix 搜索代码前缀
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
		// 不做更新操作
	}

	@Override
	public void readFromResultSet(ResultSet rs) throws Exception {
		this.modelId = rs.getString("modelid");
		this.prefix = rs.getString("prefix");
	}
}
