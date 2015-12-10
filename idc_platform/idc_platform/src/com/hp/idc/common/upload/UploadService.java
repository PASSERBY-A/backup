package com.hp.idc.common.upload;


public class UploadService{
	/**
	 * ����һ��json��{oid:'',name:'',path:''}
	 * @param oid
	 * @return
	 * @throws Exception
	 */
	public static String getFileInfoByOid(long oid) throws Exception{
		FileInfo fInfo = UploadManager.getRecordByOid(oid);
		String ret = "";
		if (fInfo!=null)
			ret = "oid:'"+oid+"',name:'"+fInfo.getFile_name()+"',path:'"+fInfo.getFile_path()+"'";
		ret = "{"+ret+"}";
		return ret;
	}
	
	/**
	 * ����һ��������¼
	 * @param oid ����Ҫ���û�ȡgetFileRecordOid()������������㴫ֵ����
	 * @param module ģ��
	 * @param filePath �ļ�������ȫ·��
	 * @param fileName �ļ���
	 * @throws Exception
	 */
	public static void createFileRecord(long oid,String module, String filePath,String fileName) throws Exception{
		UploadManager.createRecord(oid, module, filePath, fileName);
	}
	
	/**
	 * ��ȡһ���ļ���¼��OID
	 * @return
	 * @throws Exception
	 */
	public static String getFileRecordOid() throws Exception{
		return UploadManager.getOid()+"";
	}
}
