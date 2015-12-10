package com.hp.idc.portal.util;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.hp.idc.context.util.ContextUtil;
import com.hp.idc.portal.bean.Document;
import com.hp.idc.portal.mgr.DocumentMgr;

@SuppressWarnings("serial")
public class Uploadify extends HttpServlet {
	
	@SuppressWarnings("unchecked")
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Date date = new Date();
		Document document = new Document();
        String savePath = this.getServletConfig().getServletContext().getRealPath("");
        savePath = savePath + "/uploads/";
        document.setFilepath(savePath);//filepath
        document.setCreator(3401);//creator
        document.setCreateTime(date);//createTime
        document.setUpdateTime(date);//upDateTime
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
                document.setFilesize(size+"");
                if (name == null || name.trim().equals("")) {
                    continue;
                }
                //扩展名格式：  
                if (name.lastIndexOf(".") >= 0) {
                    extName = name.substring(name.lastIndexOf(".")+1);
                }
                document.setFiletype(extName);//fileType
                File file = null;
                do {
                    //生成文件名：
                    name = UUID.randomUUID().toString(); 
                    file = new File(savePath + name + "." +extName);
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
        response.getWriter().print(name + extName);
    }

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
	}
}
