package com.hp.idc.boss;

import java.io.FilenameFilter;

public interface BOSSCommonInterface {
	   /**
	    * ���ش�����Ϣ
	    * @return ���ش�����Ϣ
	    */
	   public String getErrorMsg();
	   
	   /**
	    * ִ�ж��ļ��������ݲ���
	    * 
	    * @param fileFilter �����ļ�
	    * @return �ɹ�|ʧ��
	    */
	   public boolean execute(FilenameFilter fileFilter);
	   
	   /**
	    * ִ�ж��ļ��������ݲ���
	    * 
	    * @return �ɹ�|ʧ��
	    */
	   public boolean defExecute();
}
