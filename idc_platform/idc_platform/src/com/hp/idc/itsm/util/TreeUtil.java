package com.hp.idc.itsm.util;

import java.util.ArrayList;
import java.util.List;

import com.hp.idc.itsm.common.TreeObject;

/**
 * 树操作工具类
 * @author 梅园
 *
 */
public class TreeUtil {
	
	/**
	 * 对列表进行整理，生成树状父子结构
	 * @param list 要转换的树 List<TreeObject>
	 * @return 生成的带父子结构的树
	 */
	public static List makeTree(List list) {
		// TODO 优化
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
	 * 在列表及所有子对象中查找具有oid的对象
	 * @param list 输入列表
	 * @param oid 要查找的oid
	 * @return 具有oid的对象，找不到时返回null
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
