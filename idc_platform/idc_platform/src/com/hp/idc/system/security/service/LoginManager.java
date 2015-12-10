/*
 * @(#)LoginManager.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.system.security.service;

import com.hp.idc.cas.auc.PersonInfo;
import com.hp.idc.cas.auc.PersonManager;
import com.hp.idc.cas.common.Encrypt;

/**
 * 用户登录验证
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 4:12:32 PM Jul 14, 2011
 * 
 */

public class LoginManager {
	
	/**
	 * 由于系统使用的是Md5加密, 不可逆, 
	 * 所以对界面输入密码进行Md5后, 与数据库比较进行验证
	 * @param userId
	 * @param password
	 * @return
	 * 登录成功 ====> 0
	 * 用户不存在 ====> 1
	 * 密码错误 ====> 2 
	 * 用户被禁用 ====>3
	 * 用户密码超期 ====>4
	 * 用户尝试登录次数过多 ====>5
	 * 首次登录修改密码 ====>6
	 */
	public int validate(String userId, String password){
		PersonInfo pi = PersonManager.getPersonById(userId);
		String _password = Encrypt.MD5(password);
		if (pi == null) {
			return 1;
		}
		else if (pi.getStatus() == 1) {
			return 3;
		}
		else if(!PersonManager.validLocked(pi, true))
			return 5;
		else if(!pi.getPassword().equals(_password)){
			PersonManager.loginErr(userId);
			return 2;
		}
		else if(!PersonManager.validPassPeriod(pi))
				return 4;
		else if(PersonManager.validPassFirstTime(pi))
			return 6;
		return 0;		
	}
}
