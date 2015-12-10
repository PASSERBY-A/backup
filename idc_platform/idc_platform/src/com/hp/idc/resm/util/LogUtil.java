/**
 * 
 */
package com.hp.idc.resm.util;

import java.util.List;

import com.hp.idc.unitvelog.Log;
import com.hp.idc.unitvelog.manager.ILogManager;
import com.hp.idc.context.util.ContextUtil;

/**
 * ��־��¼
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * 
 */
public class LogUtil {

	/**
	 * ��������Դ���Ը��£���Ӧͳһ��־��ѯģ������ñ�
	 */
	public static final int OP_ATTRIBUTE = 2;

	/**
	 * ��������Դģ�͸��£���Ӧͳһ��־��ѯģ������ñ�
	 */
	public static final int OP_MODEL = 3;

	/**
	 * ��������Դģ�����Ը��£���Ӧͳһ��־��ѯģ������ñ�
	 */
	public static final int OP_MODEL_ATTRIBUTE = 4;
	
	/**
	 * ��������Դ������£���Ӧͳһ��־��ѯģ������ñ�
	 */
	public static final int OP_RESOURCE = 5;
	
	/**
	 * ������������ϵ����Ӧͳһ��־��ѯģ������ñ�
	 */
	public static final int OP_RELATION = 6;
	
	/**
	 * ��������ɫ����Ӧͳһ��־��ѯģ������ñ�
	 */
	public static final int OP_ROLE = 7;
	
	/**
	 * ��������ɫȨ�ޣ���Ӧͳһ��־��ѯģ������ñ�
	 */
	public static final int OP_ROLEPERMISSION = 8;
	
	/**
	 * ����������ɹ�
	 */
	public static final int RESULT_SUCCESS = 0;

	/**
	 * ���������ʧ��
	 */
	public static final int RESULT_FAIL = 1;

	/**
	 * �������ͣ�����
	 */
	public static final int TYPE_ADD = 1;

	/**
	 * �������ͣ�ɾ��
	 */
	public static final int TYPE_REMOVE = 2;

	/**
	 * �������ͣ��޸�
	 */
	public static final int TYPE_UPDATE = 3;

	/**
	 * �������ͣ���ѯ
	 */
	public static final int TYPE_QUEUE = 4;

	/**
	 * ����ͳһ��־ģ���¼��־
	 * 
	 * @param l
	 *            ��־����
	 */
	public static void log(Log l) {
		ILogManager lm = (ILogManager) ContextUtil.getBean("logManager");
		l.setOperatorResult(LogUtil.RESULT_SUCCESS);
		l.setEndTime(System.currentTimeMillis());
		lm.addLog(l);
	}

	/**
	 * ����ͳһ��־ģ���¼��־
	 * 
	 * @param list
	 *            ��־�����б�
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
	 * ����ͳһ��־ģ���¼��־
	 * 
	 * @param l
	 *            ��־����
	 */
	public static void logError(Log l) {
		ILogManager lm = (ILogManager) ContextUtil.getBean("logManager");
		l.setOperatorResult(LogUtil.RESULT_FAIL);
		l.setEndTime(System.currentTimeMillis());
		lm.addLog(l);
	}
	
	/**
	 * ����ͳһ��־ģ���¼��־
	 * 
	 * @param list
	 *            ��־�����б�
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
