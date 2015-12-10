package com.hp.idc.itsm.util;

import java.util.ArrayList;
import java.util.List;

import com.hp.idc.itsm.common.TreeObject;

/**
 * ������������
 * @author ÷԰
 *
 */
public class TreeUtil {
	
	/**
	 * ���б��������������״���ӽṹ
	 * @param list Ҫת������ List<TreeObject>
	 * @return ���ɵĴ����ӽṹ����
	 */
	public static List makeTree(List list) {
		// TODO �Ż�
		List retList = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			TreeObject c1 = (TreeObject)list.get(i);
			if (c1.getParentOid() == -1) {
				retList.add(c1);
				continue;
			}
			for (int j = 0; j < list.size(); j++) {
				TreeObject c2 = (TreeObject)list.get(j);
				if (j == i)
					continue;
				if (c2.getOid() == c1.getParentOid()) {
					c2.getSubItems().add(c1);
					c1.setParent(c2);
				}
			}				
		}
		return retList;
	}
	
	/**
	 * ���б������Ӷ����в��Ҿ���oid�Ķ���
	 * @param list �����б�
	 * @param oid Ҫ���ҵ�oid
	 * @return ����oid�Ķ����Ҳ���ʱ����null
	 */
	public static TreeObject getObjectByOid(List list, int oid) {
		if (list==null)
			return null;
		for (int i = 0; i < list.size(); i++) {
			TreeObject obj = (TreeObject)list.get(i);
			if (obj.getOid() == oid)
				return obj;
			obj = getObjectByOid(obj.getSubItems(), oid);
			if (obj != null)
				return obj;
		}
		return null;
	}
}
