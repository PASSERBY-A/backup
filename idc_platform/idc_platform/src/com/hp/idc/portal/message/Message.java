package com.hp.idc.portal.message;

/**
 * ��ҳ��Ϣ����
 * 
 * @author chengqp
 * 
 */
public class Message {

	private int oid;
	/**
	 * ��Ϣ����
	 */
	private String title="";
	/**
	 * ��Ϣ����
	 */
	private String content="";
	/**
	 * ��ϢURL
	 */
	private String url="";
	/**
	 * ��Ϣ������
	 */
	private int userId;
	/**
	 * ��Ϣ״̬
	 * 1��δ����2���Ѷ�
	 */
	private int status=1;
	
	/**
	 * ģ���ʾ
	 */
	private String module="";
	
	public int getOid() {
		return oid;
	}
	public void setOid(int oid) {
		this.oid = oid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
}
