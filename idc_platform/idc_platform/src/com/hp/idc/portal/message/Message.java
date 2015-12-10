package com.hp.idc.portal.message;

/**
 * 首页消息中心
 * 
 * @author chengqp
 * 
 */
public class Message {

	private int oid;
	/**
	 * 消息标题
	 */
	private String title="";
	/**
	 * 消息内容
	 */
	private String content="";
	/**
	 * 消息URL
	 */
	private String url="";
	/**
	 * 消息推送人
	 */
	private int userId;
	/**
	 * 消息状态
	 * 1、未读，2、已读
	 */
	private int status=1;
	
	/**
	 * 模块表示
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
