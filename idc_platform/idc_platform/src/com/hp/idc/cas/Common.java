package com.hp.idc.cas;

import com.hp.idc.cas.auc.SystemManager;
import com.hp.idc.cas.common.CommonInfo;

public class Common {

	public static String Split_Str = "_|_";
	public void init() throws Exception{
		Cache.reloadAuc();
		CommonInfo.SYS_CONFIG = SystemManager.getConfig();
	}
}
