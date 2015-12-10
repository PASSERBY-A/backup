/**
 * 
 */
package com.hp.idc.resm.util;

import com.hp.idc.resm.resource.ResourceObject;
import com.hp.idc.resm.service.ServiceManager;

/**
 * һЩ��Դ����Ĺ�����
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class ResmUtil {

	/**
	 * ��ȡ��Դ����
	 * 
	 * @param id
	 *            ��Դid
	 * @return ��Դ����
	 * @throws Exception
	 *             �Ҳ�����Դʱ����
	 */
	public static String getResourceName(int id) throws Exception {
		ResourceObject obj = ServiceManager.getResourceService()
				.getResourceById(id);
		if (obj == null)
			throw new Exception("�Ҳ�����Դ" + id);
		return obj.getAttributeValue("name");
	}

	/**
	 * ��ȡ��id��Դ����
	 * 
	 * @param id
	 *            ��Դid
	 * @return ��Դ����(id)
	 * @throws Exception
	 *             �Ҳ�����Դʱ����
	 */
	public static String getResourceNameWithId(int id) throws Exception {
		return getResourceName(id) + "(" + id + ")";
	}

	/**
	 * ��ȡ�û���
	 * 
	 * @param id
	 *            �û�id
	 * @return �û���,�Ҳ���ʱֱ�ӷ���id
	 */
	public static String getUserName(int id) {
		ResourceObject obj = ServiceManager.getResourceService()
				.getResourceById(id);
		if (obj == null)
			return "" + id;
		return obj.getAttributeValue("name");
	}

	/**
	 * ��ȡ��id���û���
	 * 
	 * @param id
	 *            �û�id
	 * @return �û���(id)
	 */
	public static String getUserNameWithId(int id) {
		ResourceObject obj = ServiceManager.getResourceService()
				.getResourceById(id);
		if (obj == null)
			return "" + id;
		return obj.getAttributeValue("name") + "(" + id + ")";
	}
}
