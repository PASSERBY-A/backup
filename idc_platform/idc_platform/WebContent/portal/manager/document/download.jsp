<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="com.hp.idc.portal.mgr.*"%>
<%@page import="java.util.*"%>
<%@page import="com.hp.idc.portal.bean.*"%>
<%@page import="com.hp.idc.json.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.IOException"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%@page import="java.io.File"%>

<%
	String oid = request.getParameter("oid");
	DocumentMgr mgr = (DocumentMgr)ContextUtil.getBean("documentMgr");
	Document d = mgr.getBeanById(oid);
	//ʵ����ʼ��
	int status = 0;
	String ret = "";

	String fileName = URLEncoder.encode(d.getName(),"UTF-8");

	int BUFSIZE = 1024 * 8;
	int rtnPos = 0;
	byte[] buffs = new byte[BUFSIZE];
	FileInputStream inStream = null;
	InputStream is = null;
	try {
		File file = new File(d.getFilepath()+d.getFilename());
		if(file.exists()){
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-disposition", "attachment; filename="+fileName);
			inStream = new FileInputStream(d.getFilepath()+d.getFilename());
	
			while ((rtnPos = inStream.read(buffs)) > 0)
				response.getOutputStream().write(buffs, 0, rtnPos);
			response.getOutputStream().close();
			inStream.close();
			out.clear();
			out = pageContext.pushBody();
		}else{
			status = 8;
		}
	} catch (IOException e) {
		ret = e.getMessage();
		status = 9;
	} finally {
		try {
			if (inStream != null) {
				inStream.close();
				inStream = null;
			}
			if (is != null) {
				is.close();
				is = null;
			}
		} catch (IOException e) {
			ret = e.getMessage();
			status = 9;
		}
	}
%>
<script type="text/javascript">
var status = "<%=status%>";
if (status == 8)
	alert("Դ�ļ�����!����ϵ����Ա");
else if(status == 9)
	alert("�����쳣��<%=ret%>!����ϵ����Ա");
window.close();
</script>