package com.hp.idc.boss;

import java.io.FilenameFilter;

public interface BOSSCommonInterface {
	   /**
	    * 返回错误信息
	    * @return 返回错误信息
	    */
	   public String getErrorMsg();
	   
	   /**
	    * 执行读文件保存数据操作
	    * 
	    * @param fileFilter 过滤文件
	    * @return 成功|失败
	    */
	   public boolean execute(FilenameFilter fileFilter);
	   
	   /**
	    * 执行读文件保存数据操作
	    * 
	    * @return 成功|失败
	    */
	   public boolean defExecute();
}
