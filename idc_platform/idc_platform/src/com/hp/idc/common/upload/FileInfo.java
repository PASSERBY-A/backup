package com.hp.idc.common.upload;


public class FileInfo {

	String file_path = "";
	String file_name = "";
	String upload_time = "";
	boolean del_after = false;
	
	public String getFile_path() {
		return file_path;
	}
	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getUpload_time() {
		return upload_time;
	}
	public void setUpload_time(String upload_time) {
		this.upload_time = upload_time;
	}
	public boolean isDel_after() {
		return del_after;
	}
	public void setDel_after(boolean delAfter) {
		del_after = delAfter;
	}
	
}
