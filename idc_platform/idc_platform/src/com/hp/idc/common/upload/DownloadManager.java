package com.hp.idc.common.upload;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hp.idc.resm.util.ExcelUtil;

/**
 * 从服务器下载文件
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 8:18:08 AM Aug 23, 2011
 *
 */
public class DownloadManager extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {
		doGet(request,response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String fileOid = request.getParameter("oid");
		String modelId = request.getParameter("modelId");
		String data = request.getParameter("data");
		if ((fileOid == null || fileOid.equals("")) && (modelId == null || modelId.equals(""))) {
			response.setContentType("application/xml;charset=UTF-8");
			response.getWriter().print("<response success=\"false\">"+StringUtil.escapeJava("oid 为空或不是对象")+"</response>");
			return;
		}
		FileInfo downFile = null;
		try {
			if(fileOid != null){
				downFile = UploadManager.getRecordByOid(Long
						.parseLong(fileOid));	
			}
			else if (modelId != null) {
				if(data != null)
					//资源数据导出为excel
					downFile = this.parseFileName(new ExcelUtil().getResouceDataToExcel(modelId));
				else 
					//资源管理生成模板excel
					downFile = this.parseFileName(new ExcelUtil().getModelExcel(modelId));
				
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (downFile == null) {
			response.setContentType("application/xml;charset=gbk");
			response.getWriter().print("<response success=\"false\">"+StringUtil.escapeJava("找不到文件Oid("+fileOid+")对应的文件")+"</response>");
			return;
		}

		File file = new File(downFile.getFile_path());
		if (file.exists()) {
			try {
				System.out.println("Get File: "+downFile.getFile_path());
				FileInputStream fileContext = new FileInputStream(downFile.getFile_path());
				BufferedInputStream inStream = new BufferedInputStream(
						fileContext);
				response.reset();
				response.setContentType("application/x-msdownload");
				String fileName = java.net.URLEncoder.encode(downFile.getFile_name(),
						"utf-8");
				response.setHeader("Content-Disposition",
						"attachment; filename=" + fileName);

				OutputStream outs = response.getOutputStream();
				byte[] b = new byte[4096];
				int len = 0;
				while ((len = inStream.read(b)) != -1) {
					outs.write(b, 0, len);
				}
				inStream.close();
				outs.close();
				if(downFile.isDel_after()){
					file.delete();
				}
			} catch (IOException ex) {
			}
		} else {
			response.setContentType("application/xml;charset=gbk");
			response.getWriter().print("<response success=\"false\">"+StringUtil.escapeJava("找不到源文件oid="+fileOid+"")+")</response>");
		}
	}
	
	private FileInfo parseFileName(String fileName){
		if (fileName == null || fileName.length() == 0) {
			return null;
		}
		FileInfo fileInfo = new FileInfo();
		fileInfo.setFile_name(fileName.substring(fileName.lastIndexOf("/")));
		fileInfo.setFile_path(fileName);
		fileInfo.setDel_after(true);
		return fileInfo;
	}
}
