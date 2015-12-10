<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@page import="org.apache.commons.fileupload.disk.*"%>
<%@page import="java.util.*"%>
<%@page import="java.io.*"%>
<%@page import="org.apache.commons.fileupload.servlet.*"%>
<%@page import="org.apache.commons.fileupload.FileUploadException"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="com.hp.idc.portal.mgr.DocumentMgr"%>
<%@page import="com.hp.idc.portal.bean.Document"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%
	Date date = new Date();
	Document document = new Document();
	String savePath = this.getServletConfig().getServletContext().getRealPath("");
	savePath = savePath + "/uploads/";
	document.setFilepath(savePath);//filepath
	document.setCreator(3401);//creator
	document.setCreateTime(date);//createTime
	document.setUpdateTime(date);//updateTime
	File f1 = new File(savePath);
	if (!f1.exists()) {
		f1.mkdirs();
	}
	DiskFileItemFactory fac = new DiskFileItemFactory();
	ServletFileUpload upload = new ServletFileUpload(fac);
	upload.setHeaderEncoding("utf-8");
	List fileList = null;
	try {
		fileList = upload.parseRequest(request);
	} catch (FileUploadException ex) {
		return;
	}
	Iterator<FileItem> it = fileList.iterator();
	String name = "";
	String extName = "";
	while (it.hasNext()) {
		FileItem item = it.next();
		if (!item.isFormField()) {
			name = item.getName();
			document.setName(name);//name
			long size = item.getSize();
			document.setFilesize(size + "");
			String type = item.getContentType();
			System.out.println(size + " " + type);
			if (name == null || name.trim().equals("")) {
				continue;
			}
			//扩展名格式：  
			if (name.lastIndexOf(".") >= 0) {
				extName = name.substring(name.lastIndexOf(".") + 1);
			}
			document.setFiletype(extName);//fileType
			File file = null;
			do {
				//生成文件名：
				name = UUID.randomUUID().toString();
				file = new File(savePath + name + "." + extName);
			} while (file.exists());
			File saveFile = new File(savePath + name + "." + extName);
			try {
				item.write(saveFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			document.setFilename(name + "." + extName);
			DocumentMgr mgr = (DocumentMgr)ContextUtil.getBean("documentMgr");
            mgr.add(document);
		}
	}
	System.out.println(name + extName);
	response.getWriter().print(name + extName);
%>