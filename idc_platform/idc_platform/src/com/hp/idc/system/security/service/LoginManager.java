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
 * �û���¼��֤
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 4:12:32 PM Jul 14, 2011
 * 
 */

public class LoginManager {
	
	/**
	 * ����ϵͳʹ�õ���Md5����, ������, 
	 * ���ԶԽ��������������Md5��, �����ݿ�ȽϽ�����֤
	 * @param userId
	 * @param password
	 * @return
	 * ��¼�ɹ� ====> 0
	 * �û������� ====> 1
	 * ������� ====> 2 
	 * �û������� ====>3
	 * �û����볬�� ====>4
	 * �û����Ե�¼�������� ====>5
	 * �״ε�¼�޸����� ====>6
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
