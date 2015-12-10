/*
 * @(#)ILogManager.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.unitvelog.manager;

import java.util.List;

import com.hp.idc.unitvelog.Log;

/**
 * 日志管理
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, May 17, 2011 2011
 * 
 */

public interface ILogManager {
	/**
	 * 各模块添加日志的接口方法
	 * @param log
	 * @return
	 */
	public abstract int addLog(Log log);
	
	/**
	 * 批量添加日志
	 * @param logs
	 * @return	插入日志数
	 */
	public abstract int addLogList(List<Log> logs);
}
