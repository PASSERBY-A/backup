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
	 * ��ȡ��Դ������ֵ
	 * @author ÷԰
	 *
	 */
	class AttrKeyGetter implements ICompareKeyGetter<String, ResourceObject> {
		/**
		 * ����id
		 */
		protected String keyAttrId;
		
		/**
		 * ���캯��
		 * @param attrId ����id
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
	 * ��ȡ��Դ��ģ��id+����ֵ
	 * @author ÷԰
	 *
	 */
	class ModelAttrKeyGetter extends AttrKeyGetter {
		/**
		 * ���캯��
		 * @param attrId ����id
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
	 * ����id
	 */
	private String attrId;
	
	/**
	 * �������� 0=ȫ��������1=ģ��������
	 */
	private int type;
	
	/**
	 * ���캯��
	 * @param attrId ����id
	 * @param type 0=ȫ��������1=ģ��������
	 * @param initList ��ʼ������
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
	 * ��ȡ����id
	 * @return attrId ����id
	 * @see #attrId
	 */
	public String getAttrId() {
		return this.attrId;
	}

	/**
	 * ��ȡ��������
	 * @return type ��������
	 * @see #type
	 */
	public int getType() {
		return this.type;
	}
}
