/**
 * 
 */
package com.hp.idc.resm.cache;

import java.util.List;

import com.hp.idc.resm.resource.ResourceObject;
import com.hp.idc.resm.util.ICompareKeyGetter;
import com.hp.idc.resm.util.SortedArrayList;
import com.hp.idc.resm.util.StringCompareHandler;

/**
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 *
 */
public class ResourceIndex extends SortedArrayList<String, ResourceObject> {
	
	/**
	 * 获取资源的属性值
	 * @author 梅园
	 *
	 */
	class AttrKeyGetter implements ICompareKeyGetter<String, ResourceObject> {
		/**
		 * 属性id
		 */
		protected String keyAttrId;
		
		/**
		 * 构造函数
		 * @param attrId 属性id
		 */
		public AttrKeyGetter(String attrId) {
			this.keyAttrId = attrId;
		}
		
		public String getCompareKey(ResourceObject obj) {
			String str = obj.getAttributeValue(this.keyAttrId);
			return (str == null ? "" : str);
		}
	}
	
	/**
	 * 获取资源的模型id+属性值
	 * @author 梅园
	 *
	 */
	class ModelAttrKeyGetter extends AttrKeyGetter {
		/**
		 * 构造函数
		 * @param attrId 属性id
		 */
		public ModelAttrKeyGetter(String attrId) {
			super(attrId);
		}
		
		@Override
		public String getCompareKey(ResourceObject obj) {
			return obj.getModelId() + "." + super.getCompareKey(obj);
		}
	}
	
	/**
	 * 属性id
	 */
	private String attrId;
	
	/**
	 * 索引类型 0=全局索引，1=模型内索引
	 */
	private int type;
	
	/**
	 * 构造函数
	 * @param attrId 属性id
	 * @param type 0=全局索引，1=模型内索引
	 * @param initList 初始化数据
	 */
	public ResourceIndex(String attrId, int type,
			List<ResourceObject> initList) {
		super(null, null);
		this.attrId = attrId;
		this.type = type;
		if (type == 0)
			this.keyGetter = new AttrKeyGetter(attrId);
		else
			this.keyGetter = new ModelAttrKeyGetter(attrId);
		this.compareHandler = new StringCompareHandler();
		initData(initList);
	}

	/**
	 * 获取属性id
	 * @return attrId 属性id
	 * @see #attrId
	 */
	public String getAttrId() {
		return this.attrId;
	}

	/**
	 * 获取索引类型
	 * @return type 索引类型
	 * @see #type
	 */
	public int getType() {
		return this.type;
	}
}
