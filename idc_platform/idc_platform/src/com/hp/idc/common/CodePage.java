/*
 * @(#)CodePage.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.hp.idc.resm.util.StringUtil;

/**
 * GBK对应UNICODE表
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 3:48:46 PM Jul 6, 2011
 * 
 */

public final class CodePage {
	
	/**
	 * GBK对应UNICODE表
	 */
	public static int codeMapping[] = null;

	/**
	 * 初始化代码表
	 * 
	 * @throws IOException
	 *             IO异常时发生
	 */
	public static void initCodePage() throws IOException {
		System.out.println("loading codepage 936 ...");
		int[] codeMapping1 = new int[65536];
		for (int i = 0; i < codeMapping1.length; i++)
			codeMapping1[i] = 0;
		InputStream inp = StringUtil.class
				.getResourceAsStream("/META-INF/CP936.TXT");
		BufferedReader bufferedreader = new BufferedReader(
				new InputStreamReader(inp));
		String s;
		while ((s = bufferedreader.readLine()) != null) {
			String[] ss = s.split("\t");
			if (ss.length >= 2 && ss[0].startsWith("0x")
					&& ss[1].startsWith("0x")) {
				int n1 = Integer.parseInt(ss[0].substring(2), 16);
				int n2 = Integer.parseInt(ss[1].substring(2), 16);
				codeMapping1[n2] = n1;
			}
		}
		codeMapping = codeMapping1;
		System.out.println("loading codepage 936 ...ok");
	}
}
