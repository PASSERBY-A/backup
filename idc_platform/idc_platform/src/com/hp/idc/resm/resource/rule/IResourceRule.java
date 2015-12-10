/**
 * 
 */
package com.hp.idc.resm.resource.rule;

import java.util.List;

import org.dom4j.Element;

import com.hp.idc.resm.resource.AttributeBase;
import com.hp.idc.resm.resource.ResourceObject;

/**
 * ������Դ���º���֤�Ľӿ�
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public interface IResourceRule {

	/**
	 * ������Դʱ����
	 * 
	 * @param object
	 *            ��Դ����
	 * @param attributes
	 *            �б仯������
	 * @param el
	 *            ���µ����ݽڵ�
	 * @param userId
	 *            �����û�id
	 * @throws Exception
	 *             ���쳣ʱ����
	 */
	public void update(ResourceObject object, List<AttributeBase> attributes,
			Element el, int userId) throws Exception;

	/**
	 * ����Դ�������ݽ���
	 * 
	 * @param object
	 *            ��Դ����
	 * @throws Exception
	 *             ��֤��ͨ��ʱ����
	 */
	public void valid(ResourceObject object) throws Exception;

}
