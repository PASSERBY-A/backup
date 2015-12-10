/**
 * 
 */
package com.hp.idc.resm.util;

import java.util.List;

import com.hp.idc.unitvelog.Log;
import com.hp.idc.unitvelog.manager.ILogManager;
import com.hp.idc.context.util.ContextUtil;

/**
 * 日志记录
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class LogUtil {

	/**
	 * 操作：资源属性更新，对应统一日志查询模块的配置表
	 */
	public static final int OP_ATTRIBUTE = 2;

	/**
	 * 操作：资源模型更新，对应统一日志查询模块的配置表
	 */
	public static final int OP_MODEL = 3;

	/**
	 * 操作：资源模型属性更新，对应统一日志查询模块的配置表
	 */
	public static final int OP_MODEL_ATTRIBUTE = 4;
	
	/**
	 * 操作：资源对象更新，对应统一日志查询模块的配置表
	 */
	public static final int OP_RESOURCE = 5;
	
	/**
	 * 操作：关联关系，对应统一日志查询模块的配置表
	 */
	public static final int OP_RELATION = 6;
	
	/**
	 * 操作：角色，对应统一日志查询模块的配置表
	 */
	public static final int OP_ROLE = 7;
	
	/**
	 * 操作：角色权限，对应统一日志查询模块的配置表
	 */
	public static final int OP_ROLEPERMISSION = 8;
	
	/**
	 * 操作结果，成功
	 */
	public static final int RESULT_SUCCESS = 0;

	/**
	 * 操作结果，失败
	 */
	public static final int RESULT_FAIL = 1;

	/**
	 * 操作类型，新增
	 */
	public static final int TYPE_ADD = 1;

	/**
	 * 操作类型，删除
	 */
	public static final int TYPE_REMOVE = 2;

	/**
	 * 操作类型，修改
	 */
	public static final int TYPE_UPDATE = 3;

	/**
	 * 操作类型，查询
	 */
	public static final int TYPE_QUEUE = 4;

	/**
	 * 调用统一日志模块记录日志
	 * 
	 * @param l
	 *            日志对象
	 */
	public static void log(Log l) {
		ILogManager lm = (ILogManager) ContextUtil.getBean("logManager");
		l.setOperatorResult(LogUtil.RESULT_SUCCESS);
		l.setEndTime(System.currentTimeMillis());
		lm.addLog(l);
	}

	/**
	 * 调用统一日志模块记录日志
	 * 
	 * @param list
	 *            日志对象列表
	 */
	public static void log(List<Log> list) {
		if (list.size() == 0)
			return;
		ILogManager lm = (ILogManager) ContextUtil.getBean("logManager");
		for (Log l : list) {
			l.setOperatorResult(LogUtil.RESULT_SUCCESS);
			l.setEndTime(System.currentTimeMillis());
		}
		lm.addLogList(list);
	}

	/**
	 * 调用统一日志模块记录日志
	 * 
	 * @param l
	 *            日志对象
	 */
	public static void logError(Log l) {
		ILogManager lm = (ILogManager) ContextUtil.getBean("logManager");
		l.setOperatorResult(LogUtil.RESULT_FAIL);
		l.setEndTime(System.currentTimeMillis());
		lm.addLog(l);
	}
	
	/**
	 * 调用统一日志模块记录日志
	 * 
	 * @param list
	 *            日志对象列表
	 */
	public static void logError(List<Log> list) {
		if (list.size() == 0)
			return;
		ILogManager lm = (ILogManager) ContextUtil.getBean("logManager");
		for (Log l : list) {
			l.setOperatorResult(LogUtil.RESULT_FAIL);
			l.setEndTime(System.currentTimeMillis());
		}
		lm.addLogList(list);
	}
}
