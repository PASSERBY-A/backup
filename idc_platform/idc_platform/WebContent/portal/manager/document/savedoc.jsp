<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.hp.idc.portal.mgr.DocumentMgr"%>
<%@page import="com.hp.idc.portal.bean.Document"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="java.io.File"%>
<%@page import="java.util.Date"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="java.util.*"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="com.hp.idc.context.util.ContextUtil"%>
<%
	String oid = null;
	String title = null;
	FileItemFactory factory = new DiskFileItemFactory();
	ServletFileUpload upload = new ServletFileUpload(factory);
	upload.setHeaderEncoding("utf-8");
	List fileItemList = upload.parseRequest(request);
	FileItem fileItem = null;
	long size = 0;
	if (fileItemList != null) {
		for (Iterator itr = fileItemList.iterator(); itr.hasNext();) {
			FileItem item = (FileItem) itr.next();
			if (!item.isFormField()) {
				size = item.getSize();
				String name = item.getName();
				fileItem = item;
				if (name == null || name.trim().equals("")) {
					out.write("文件传输出错");
                    break;
                }
			}else{
				if(item.getFieldName().equalsIgnoreCase("oid")){
					oid = item.getString();
				}else if(item.getFieldName().equalsIgnoreCase("title")){
					title = java.net.URLDecoder.decode(item.getString(),"UTF-8");
				}
			}
		}
	}
	DocumentMgr mgr = (DocumentMgr)ContextUtil.getBean("documentMgr");
	Document bean = mgr.getBeanById(oid);
	bean.setName(title);
	bean.setUpdateTime(new Date());
    bean.setFilesize(size+"");
    File file = new File(bean.getFilepath() +bean.getFilename());
    try {
    	fileItem.write(file);
    } catch (Exception e) {	
    	out.write("上传文件出错");
    	return;
    }
    if(mgr.update(bean)){
    	out.write("保存成功");
    }else{
    	out.write("保存失败");
    }
	
	
%>