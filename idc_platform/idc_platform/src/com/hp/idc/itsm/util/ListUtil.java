package com.hp.idc.itsm.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated
 * @author ÷԰
 *
 */
public class ListUtil {

	/**
	 * ����list1�в�������list2�еĲ��֡�
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static List exclude(List list1, List list2) {
		List l = new ArrayList();
		for (int i = 0; i < list1.size(); i++) {
			Object o1 = list1.get(i);
			boolean found = false;
			for (int j = 0; j < list2.size(); j++) {
				Object o2 = list2.get(j);
				if (o2.equals(o1)) {
					found = true;
					break;
				}
			}
			if (found == false)
				l.add(o1);
		}
		return l;
	}
}
