package com.hp.idc.resm.util;

import com.hp.idc.resm.util.checker.IChecker;

/**
 * ֵ������
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class CheckerUtil {
	/**
	 * ����ֵ�Ƿ�Ϸ�
	 * 
	 * @param expr
	 *            ���ʽ
	 * @param value
	 *            ֵ
	 * @return �Ƿ���ԭ���������null�����ʾ����ͨ��
	 */
	@SuppressWarnings("unchecked")
	public static String isValid(String expr, Object value) {
		String[] checkers = expr.split(";");
		if (checkers.length == 1) {
			return null;
		}
		if (checkers[1].startsWith("/")) { 
			return null;
		}
		try {
			Class<? extends IChecker> cls = (Class<? extends IChecker>) Class.forName(checkers[1]);
			IChecker iCheck = cls.newInstance();
			return iCheck.isValid(checkers[0],value==null?"":value.toString());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
