/*
 * @(#)IPChecker.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.resm.util.checker;

/**
 * 
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 2:11:13 PM Sep 8, 2011
 * 
 */

public class IPChecker implements IChecker {

	/* (non-Javadoc)
	 * @see com.hp.idc.resm.util.checker.IChecker#isValid(java.lang.String, java.lang.String)
	 */
	@Override
	public String isValid(String attrName, String value) {
		String[] _ip = value.split("[.]");
		for(String p : _ip){
			try {
				int _p = Integer.parseInt(p);
				if (_p > 254 || _p < 0) {
					return "“"+attrName+"数值格式非法,请填写正确IP";
				}
			} catch (Exception e) {
				return "“"+attrName+"数值格式非法,请填写正确IP";
			}
		}
		return null;
	}

}
