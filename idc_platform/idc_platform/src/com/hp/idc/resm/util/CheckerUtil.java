package com.hp.idc.resm.util;

import com.hp.idc.resm.util.checker.IChecker;

/**
 * 值较验器
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class CheckerUtil {
	/**
	 * 较验值是否合法
	 * 
	 * @param expr
	 *            表达式
	 * @param value
	 *            值
	 * @return 非法的原因。如果返回null，则表示较验通过
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
