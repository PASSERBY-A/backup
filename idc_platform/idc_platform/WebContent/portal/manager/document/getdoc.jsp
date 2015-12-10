<%@ page contentType="text/html;charset=GB2312"%>
<%@ page import="java.sql.*"%>
<%@page import="com.hp.idc.portal.bean.Document"%>
<%@page import="com.hp.idc.portal.mgr.DocumentMgr"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%
	String oid = request.getParameter("oid");
	DocumentMgr mgr = (DocumentMgr)ContextUtil.getBean("documentMgr");
	Document bean = mgr.getBeanById(oid);

	try {
		java.io.InputStream in = new FileInputStream(bean.getFilepath() + bean.getFilename());
		java.io.OutputStream outStream = response.getOutputStream();
		byte[] buf = new byte[1024];
		int bytes = 0;
		while ((bytes = in.read(buf)) != -1)
			outStream.write(buf, 0, bytes);
		in.close();
		outStream.flush();
		outStream.close();
		outStream=null;
		response.flushBuffer();
		out.clear();
		out = pageContext.pushBody();
	} catch (Throwable e) {
		out.println(e.toString());
	}
%>