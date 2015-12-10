package com.volkswagen.tel.billing.common;

import java.util.Arrays;
import java.util.Properties;
import java.util.Date;    

import javax.mail.Address;    
import javax.mail.Authenticator;
import javax.mail.BodyPart;    
import javax.mail.Message;    
import javax.mail.MessagingException;    
import javax.mail.Multipart;    
import javax.mail.PasswordAuthentication;
import javax.mail.Session;    
import javax.mail.Transport;    
import javax.mail.internet.InternetAddress;    
import javax.mail.internet.MimeBodyPart;    
import javax.mail.internet.MimeMessage;    
import javax.mail.internet.MimeMultipart;    

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;



public class EmailSender {

	 private static final Logger log = Logger.getLogger(EmailSender.class);
	 
	 public static class MailSenderInfo {    
		    // 发送邮件的服务器的IP和端口    
		    private String mailServerHost;    
		    private String mailServerPort = "25";    
		    // 邮件发送者的地址    
		    private String fromAddress;    
		    // 邮件接收者的地址    
		    private String toAddress;    
		    // 登陆邮件发送服务器的用户名和密码    
		    private String userName;    
		    private String password;    
		    // 是否需要身份验证    
		    private boolean validate = false;    
		    // 邮件主题    
		    private String subject;    
		    // 邮件的文本内容    
		    private String content;    
		    // 邮件附件的文件名    
		    private String[] attachFileNames;      
		    /**   
		      * 获得邮件会话属性   
		      */    
		    public Properties getProperties(){    
		      Properties p = new Properties();    
		      p.put("mail.smtp.host", this.mailServerHost);    
		      p.put("mail.smtp.port", this.mailServerPort);    
		      p.put("mail.smtp.auth", validate ? "true" : "false");    
		      return p;    
		    }    
		    
		    public MailSenderInfo(){
		    	
		    }
		    public String getMailServerHost() {    
		      return mailServerHost;    
		    }    
		    public void setMailServerHost(String mailServerHost) {    
		      this.mailServerHost = mailServerHost;    
		    }   
		    public String getMailServerPort() {    
		      return mailServerPort;    
		    }   
		    public void setMailServerPort(String mailServerPort) {    
		      this.mailServerPort = mailServerPort;    
		    }   
		    public boolean isValidate() {    
		      return validate;    
		    }   
		    public void setValidate(boolean validate) {    
		      this.validate = validate;    
		    }   
		    public String[] getAttachFileNames() {    
		      return attachFileNames;    
		    }   
		    public void setAttachFileNames(String[] fileNames) {    
		      this.attachFileNames = fileNames;    
		    }   
		    public String getFromAddress() {    
		      return fromAddress;    
		    }    
		    public void setFromAddress(String fromAddress) {    
		      this.fromAddress = fromAddress;    
		    }   
		    public String getPassword() {    
		      return password;    
		    }   
		    public void setPassword(String password) {    
		      this.password = password;    
		    }   
		    public String getToAddress() {    
		      return toAddress;    
		    }    
		    public void setToAddress(String toAddress) {    
		      this.toAddress = toAddress;    
		    }    
		    public String getUserName() {    
		      return userName;    
		    }   
		    public void setUserName(String userName) {    
		      this.userName = userName;    
		    }   
		    public String getSubject() {    
		      return subject;    
		    }   
		    public void setSubject(String subject) {    
		      this.subject = subject;    
		    }   
		    public String getContent() {    
		      return content;    
		    }   
		    public void setContent(String textContent) {    
		      this.content = textContent;    
		    }

			@Override
			public String toString() {
				return "MailSenderInfo [mailServerHost=" + mailServerHost
						+ ", mailServerPort=" + mailServerPort
						+ ", fromAddress=" + fromAddress + ", toAddress="
						+ toAddress + ", userName=" + userName + ", password="
						+ password + ", validate=" + validate + ", subject="
						+ subject + ", content=" + content
						+ ", attachFileNames="
						+ Arrays.toString(attachFileNames) + "]";
			}  
		    
		    
		    
		}   
	 


	/* public static boolean send(String emailAddress, String title, String content) {
	      MailSenderInfo mailInfo = new MailSenderInfo();    
	      mailInfo.setMailServerHost(PropUtil.getString("config", "mail.smtp.host"));    
	      mailInfo.setMailServerPort(PropUtil.getString("config", "mail.smtp.port"));    
	      mailInfo.setValidate(PropUtil.getString("config", "mail.smtp.auth").equals("true"));    
	      mailInfo.setUserName(PropUtil.getString("config", "mail.username"));    
	      mailInfo.setPassword(PropUtil.getString("config", "mail.password"));//您的邮箱密码    
	      mailInfo.setFromAddress(PropUtil.getString("config", "mail.from.address"));    
	      mailInfo.setToAddress(emailAddress);    
	      mailInfo.setSubject(title);    
	      mailInfo.setContent(content);    
	         //这个类主要来发送邮件   
	      SimpleMailSender sms = new SimpleMailSender();   
	         //sms.sendTextMail(mailInfo);//发送文体格式    
	      return sms.sendHtmlMail(mailInfo);//发送html格式   
	}*/
	
	public static class MyAuthenticator extends Authenticator{   
	    String userName=null;   
	    String password=null;   
	        
	    public MyAuthenticator(){   
	    }   
	    public MyAuthenticator(String username, String password) {    
	        this.userName = username;    
	        this.password = password;    
	    }    
	    protected PasswordAuthentication getPasswordAuthentication(){   
	        return new PasswordAuthentication(userName, password);   
	    }   
	}   
	
	public static class SimpleMailSender  {    
		/**   
		  * 以文本格式发送邮件   
		  * @param mailInfo 待发送的邮件的信息   
		  */    
		    public boolean sendTextMail(MailSenderInfo mailInfo) {    
		      // 判断是否需要身份认证    
		      MyAuthenticator authenticator = null;    
		      Properties pro = mailInfo.getProperties();   
		      if (mailInfo.isValidate()) {    
		      // 如果需要身份认证，则创建一个密码验证器    
		        authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());    
		      }   
		      // 根据邮件会话属性和密码验证器构造一个发送邮件的session    
		      Session sendMailSession = Session.getDefaultInstance(pro,authenticator);    
		      try {    
		      // 根据session创建一个邮件消息    
		      Message mailMessage = new MimeMessage(sendMailSession);    
		      // 创建邮件发送者地址    
		      Address from = new InternetAddress(mailInfo.getFromAddress());    
		      // 设置邮件消息的发送者    
		      mailMessage.setFrom(from);    
		      // 创建邮件的接收者地址，并设置到邮件消息中    
		      Address to = new InternetAddress(mailInfo.getToAddress());    
		      mailMessage.setRecipient(Message.RecipientType.TO,to);    
		      // 设置邮件消息的主题    
		      mailMessage.setSubject(mailInfo.getSubject());    
		      // 设置邮件消息发送的时间    
		      mailMessage.setSentDate(new Date());    
		      // 设置邮件消息的主要内容    
		      String mailContent = mailInfo.getContent();    
		      mailMessage.setText(mailContent);    
		      // 发送邮件    
		      Transport.send(mailMessage);   
		      return true;    
		      } catch (MessagingException ex) {    
		          ex.printStackTrace();    
		      }    
		      return false;    
		    }    
		       
		    /**   
		      * 以HTML格式发送邮件   
		      * @param mailInfo 待发送的邮件信息   
		     * @throws MessagingException 
		      */    
		    public static boolean sendHtmlMail(MailSenderInfo mailInfo) throws MessagingException{    
		      // 判断是否需要身份认证    
		      MyAuthenticator authenticator = null;   
		      Properties pro = mailInfo.getProperties();   
		      //如果需要身份认证，则创建一个密码验证器     
		      if (mailInfo.isValidate()) {    
		        authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());   
		      }    
		      // 根据邮件会话属性和密码验证器构造一个发送邮件的session    
		      Session sendMailSession = Session.getDefaultInstance(pro,authenticator);    
		      try {    
		      // 根据session创建一个邮件消息    
		      Message mailMessage = new MimeMessage(sendMailSession);    
		      // 创建邮件发送者地址    
		      Address from = new InternetAddress(mailInfo.getFromAddress());    
		      // 设置邮件消息的发送者    
		      mailMessage.setFrom(from);    
		      // 创建邮件的接收者地址，并设置到邮件消息中    
		      Address to = new InternetAddress(mailInfo.getToAddress());    
		      // Message.RecipientType.TO属性表示接收者的类型为TO    
		      mailMessage.setRecipient(Message.RecipientType.TO,to);    
		      // 设置邮件消息的主题    
		      mailMessage.setSubject(mailInfo.getSubject());    
		      // 设置邮件消息发送的时间    
		      mailMessage.setSentDate(new Date());    
		      // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象    
		      Multipart mainPart = new MimeMultipart();    
		      // 创建一个包含HTML内容的MimeBodyPart    
		      BodyPart html = new MimeBodyPart();    
		      // 设置HTML内容    
		      html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");    
		      mainPart.addBodyPart(html);    
		      // 将MiniMultipart对象设置为邮件内容    
		      mailMessage.setContent(mainPart);    
		      // 发送邮件    
		      Transport.send(mailMessage);    
		      return true;    
		      } catch (MessagingException ex) {  
		          throw ex;
		      }    
		    }    
		}   
	
	
	/**
	 * @param args
	 * @throws MessagingException 
	 */
	public static void main(String[] args) throws MessagingException 
	{
		
	      MailSenderInfo mailInfo = new MailSenderInfo();    
	      mailInfo.setMailServerHost(GenericConfig.get("mail.smtp.host"));    
	      mailInfo.setMailServerPort(GenericConfig.get("mail.smtp.port"));    
	      mailInfo.setValidate(GenericConfig.get("mail.smtp.auth").equals("true"));    
	      mailInfo.setUserName(GenericConfig.get("mail.username"));    
	      mailInfo.setPassword(GenericConfig.get("mail.password"));//您的邮箱密码    
	      mailInfo.setFromAddress(GenericConfig.get("mail.from.address"));     
	      mailInfo.setToAddress("Extern.Qiang.Li@volkswagen.com.cn");    
	      mailInfo.setSubject("testing");    
	      mailInfo.setContent("设置邮箱内容 如http://www.guihua.org 中国桂花网 是中国最大桂花网站==");    
	         //这个类主要来发送邮件   
	      SimpleMailSender sms = new SimpleMailSender();   
	      sms.sendHtmlMail(mailInfo);//发送html格式   
	}
	
	

}
