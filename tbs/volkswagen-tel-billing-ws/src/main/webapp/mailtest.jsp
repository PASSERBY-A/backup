<%@page import="com.volkswagen.tel.billing.common.EmailSender.SimpleMailSender"%>
<%@page import="com.volkswagen.tel.billing.common.GenericConfig"%>
<%@page import="com.volkswagen.tel.billing.common.EmailSender.MailSenderInfo"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	String mailTo = request.getParameter("mailto");

	if(StringUtils.isNotBlank(mailTo))
	{
		
		
		try{
			
			 MailSenderInfo mailInfo = new MailSenderInfo();    
		      mailInfo.setMailServerHost(GenericConfig.get("mail.smtp.host"));    
		      mailInfo.setMailServerPort(GenericConfig.get("mail.smtp.port"));    
		      mailInfo.setValidate(GenericConfig.get("mail.smtp.auth").equals("true"));    
		      mailInfo.setUserName(GenericConfig.get("mail.username"));    
		      mailInfo.setPassword(GenericConfig.get("mail.password"));//您的邮箱密码    
		      mailInfo.setFromAddress(GenericConfig.get("mail.from.address"));     
		      mailInfo.setToAddress(mailTo);    
		      mailInfo.setSubject("testing...");    
		      mailInfo.setContent("<html>	<head>	 <meta http-equiv=Content-Type content='text/html;charset=utf-8'>	</head>	<body>		Dear liqiang, 		<br/>		<p>								This is Telephone Billing System (TBS) notification. The last month's bill of office phone number and cellphone number is uploaded to TBS. Please login system <a href='https://web.ap.vwg/portal'>https://web.ap.vwg/portal</a> and check your private call. 				Also, below bills are still open for you to check.		</p>				<br/>		<p>				<table>		 		 				<tr>				<td>[13801010101]</td><td>[201510]</td>			</tr>						<tr>				<td>[13801010101]</td><td>[201509]</td>			</tr>						<tr>				<td>[13801010101]</td><td>[201507]</td>			</tr>						<tr>				<td>[13801010101]</td><td>[201506]</td>			</tr>						<tr>				<td>[13801010101]</td><td>[201505]</td>			</tr>						<tr>				<td>[13801010101]</td><td>[201504]</td>			</tr>						<tr>				<td>[13801010101]</td><td>[201503]</td>			</tr>						<tr>				<td>[13801010101]</td><td>[201502]</td>			</tr>						<tr>				<td>[13801010101]</td><td>[201501]</td>			</tr>						<tr>				<td>[13801010101]</td><td>[201412]</td>			</tr>						<tr>				<td>[13801010101]</td><td>[201411]</td>			</tr>						<tr>				<td>[13801010101]</td><td>[201410]</td>			</tr>						<tr>				<td>[13801010101]</td><td>[201409]</td>			</tr>						<tr>				<td>[13801010101]</td><td>[201408]</td>			</tr>						<tr>				<td>[13801010101]</td><td>[201407]</td>			</tr>													 				<tr>				<td>[13809090909]</td><td>[201506]</td>			</tr>						<tr>				<td>[13809090909]</td><td>[201505]</td>			</tr>						<tr>				<td>[13809090909]</td><td>[201504]</td>			</tr>						<tr>				<td>[13809090909]</td><td>[201503]</td>			</tr>						<tr>				<td>[13809090909]</td><td>[201502]</td>			</tr>						<tr>				<td>[13809090909]</td><td>[201501]</td>			</tr>						<tr>				<td>[13809090909]</td><td>[201412]</td>			</tr>						<tr>				<td>[13809090909]</td><td>[201411]</td>			</tr>						<tr>				<td>[13809090909]</td><td>[201410]</td>			</tr>						<tr>				<td>[13809090909]</td><td>[201409]</td>			</tr>						<tr>				<td>[13809090909]</td><td>[201408]</td>			</tr>						<tr>				<td>[13809090909]</td><td>[201407]</td>			</tr>																<tr>				<td>[65318888]</td><td>[201510]</td>				</tr>			 					<tr>				<td>[65318888]</td><td>[201508]</td>				</tr>			 					<tr>				<td>[65318888]</td><td>[201507]</td>				</tr>			 					<tr>				<td>[65318888]</td><td>[201506]</td>				</tr>			 					<tr>				<td>[65318888]</td><td>[201505]</td>				</tr>			 					<tr>				<td>[65318888]</td><td>[201504]</td>				</tr>			 					<tr>				<td>[65318888]</td><td>[201503]</td>				</tr>			 					<tr>				<td>[65318888]</td><td>[201502]</td>				</tr>			 					<tr>				<td>[65318888]</td><td>[201501]</td>				</tr>			 					<tr>				<td>[65318888]</td><td>[201412]</td>				</tr>			 					<tr>				<td>[65318888]</td><td>[201411]</td>				</tr>			 					<tr>				<td>[65318888]</td><td>[201410]</td>				</tr>			 					<tr>				<td>[65318888]</td><td>[201409]</td>				</tr>			 					<tr>				<td>[65318888]</td><td>[201408]</td>				</tr>			 					<tr>				<td>[65318888]</td><td>[201407]</td>				</tr>			 				</table>				</p>		<p>		Please contact VGC UHD 861065313333 or  VGC.UHD@volkswagen.com.cn if you need support. 		<br>		<br>		Thank you. 		<br>		<br>		Your TBS Admin		</p>		 	</body></html>");    
		         //这个类主要来发送邮件   
		      SimpleMailSender sms = new SimpleMailSender();   
		      sms.sendHtmlMail(mailInfo);//发送html格式   
			
		}catch(Throwable tx)
	      {
			response.getWriter().println("<script>alert('"+tx.getMessage()+"'); </script>");
			tx.printStackTrace();
			
	      }
	}


	
	
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!-- created by fzx -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=9" />
<title>Telephone Billing System</title>

<script type="text/javascript">
</script>

</head>

<body>
	
	<dir style="margin-left: 400px;" >
			<form action="#" method="post" >
					<input type="text" name="mailto" value="Extern.Qiang.Li@volkswagen.com.cn">
					<input type="submit"/> 
			</form>
			
			
	</dir>


</body>
</html>

