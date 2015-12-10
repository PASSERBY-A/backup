package com.hp.idc.resm.model;

import java.util.ArrayList;
import java.util.List;

import com.hp.idc.json.JSONObject;
import com.hp.idc.resm.cache.CacheException;
import com.hp.idc.resm.resource.AttributeBase;
import com.hp.idc.resm.resource.StringAttribute;
import com.hp.idc.resm.service.CachedAttributeService;
import com.hp.idc.resm.service.ServiceManager;

/**
 * 字符型资源属性定义
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public final class StringAttributeDefine extends AttributeDefine {

	/**
	 * 序列化UID
	 */
	private static final long serialVersionUID = 6684545192930735356L;

	/**
	 * 操作符定义
	 */
	private static AttributeOperator[] OPERATORS = {
		new AttributeOperator("!=", "不等于", "$0 != \"$1\""),
		new AttributeOperator("==", "等于", "$0 == \"$1\""),
		new AttributeOperator("sub", "包含", "$0.indexOf(\"$1\") != -1"),
		new AttributeOperator("nsub", "不包含", "$0.indexOf(\"$1\") == -1"),
		new AttributeOperator("starts", "以...开始", "$0.startsWith(\"$1\")"),
		new AttributeOperator("ends", "以...结束", "$0.endsWith(\"$1\")"),
		new AttributeOperator("isnull", "为空", "$null.isNull($0)"),
		new AttributeOperator("notnull", "不为空", "$null.isNotNull($0)")
	};
	
	/**
	 * 常量定义
	 */
	private static AttributeConst[] CONSTS = {
	};
	
	/**
	 * 代码类型：0=普通字符串，1=选择型，2=树形选择, 3=文本框
	 */
	private int codeType = 0;

	/**
	 * 代码id
	 */
	private String codeId = null;

	/**
	 * 关联的代码列表，只是为了序列化使用的，此变量不设置值
	 */
	private List<Code> codes = null;
	
	
	/**
	 * 获取代码类型
	 * 
	 * @return codeType 代码类型：0=普通字符串，1=选择型，2=树形选择
	 * @see #codeType
	 */
	public int getCodeType() {
		return this.codeType;
	}

	/**
	 * 设置代码类型
	 * 
	 * @param codeType
	 *            代码类型：0=普通字符串，1=选择型，2=树形选择
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #codeType
	 */
	public void setCodeType(int codeType) throws CacheException {
		checkSet();
		this.codeType = codeType;
	}

	/**
	 * 获取代码id
	 * 
	 * @return 代码id
	 * @see #codeId
	 */
	public String getCodeId() {
		return this.codeId;
	}

	/**
	 * 设置代码id
	 * 
	 * @param codeId
	 *            代码id
	 * @throws CacheException
	 *             对象已被缓存时发生
	 * @see #codeId
	 */
	public void setCodeId(String codeId) throws CacheException {
		checkSet();
		this.codeId = codeId;
	}

	/**
	 * 获取关联的代码
	 * 
	 * @return codes 代码列表
	 * @see #codes
	 */
	public List<Code> getCodes() {
		CachedAttributeService cas = (CachedAttributeService) ServiceManager
				.getAttributeService();
		return cas.getCodeCache().getCodeList(this.codeId);
	}

	/**
	 * 设置关联的代码，此方法无效，资源属性定义是从缓存中读取的
	 * 
	 * @param codes
	 *            关联的代码
	 * @see #codes
	 */
	public void setCodes(List<Code> codes) {
		// 空函数
	}

	@Override
	public void setArguments(String arguments) throws CacheException {
		if (arguments == null) {
			arguments = "{ }";
		}
		try {
			JSONObject json = new JSONObject(arguments);
			setCodeType(Integer.parseInt(json.optString("codeType", "0")));
			json.put("codeType", this.codeType);
			setCodeId(json.optString("codeId", ""));
			json.put("codeId", this.codeId);
			super.setArguments(arguments);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CacheException("参数格式错误: " + this.getId() + "/" + this.getName());
		}
	}

	@Override
	public String valid(String value, String value1, String attrId, String op) {
		if (value == null || value.length() == 0)
			return null;
		if (this.codeType > 0) {
			for (Code c : this.codes) {
				if (value.equals(c.getName()))
					return null;
			}
		}
		if (this.codeType == 2) { // 树型结构，遍历所有子节点
			List<Code> list = new ArrayList<Code>();
			for (Code c : this.codes) {
				if (c.getChilds() != null)
					list.addAll(c.getChilds());
			}
			while (list.size() > 0) {
				Code c = list.remove(list.size() - 1);
				if (value.equals(c.getName()))
					return null;
				if (c.getChilds() != null)
					list.addAll(c.getChilds());
			}
		}
		return "属性“" + this.getName() + "”无效的取值:" + value;
	}
	
	@Override
	public String getDataType() {
		return "varchar2";
	}

	@Override
	public int getLength() {
		return this.length;
	}
	
	@Override
	protected AttributeBase createInstance() {
		return new StringAttribute();
	}
	
	@Override
	public List<AttributeOperator> getOperators() {
		List<AttributeOperator> list = new ArrayList<AttributeOperator>(
				OPERATORS.length);
		for (AttributeOperator op : OPERATORS)
			list.add(op);
		return list;
	}
	
	@Override
	public List<AttributeConst> getConsts() {
		List<AttributeConst> list = new ArrayList<AttributeConst>(
				CONSTS.length);
		for (AttributeConst c : CONSTS)
			list.add(c);
		return list;
	}
}
