package com.hp.idc.common.upload;


public class UploadService{
	/**
	 * 返回一个json串{oid:'',name:'',path:''}
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
	 * 创建一条附件记录
	 * @param oid 首先要调用获取getFileRecordOid()方法，不能随便传值进来
	 * @param module 模块
	 * @param filePath 文件保存后的全路径
	 * @param fileName 文件名
	 * @throws Exception
	 */
	public static void createFileRecord(long oid,String module, String filePath,String fileName) throws Exception{
		UploadManager.createRecord(oid, module, filePath, fileName);
	}
	
	/**
	 * 获取一个文件记录的OID
	 * @return
	 * @throws Exception
	 */
	public static String getFileRecordOid() throws Exception{
		return UploadManager.getOid()+"";
	}
}
