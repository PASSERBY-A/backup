package com.hp.idc.common.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.hp.idc.resm.util.ExcelUtil;

/**
 * 上传文件至服务器
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 8:11:23 AM Aug 23, 2011
 *
 */
public class UploadManager extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException {
		// response.setContentType("application/xml;charset=UTF-8");
		// System.out.println(request.getCharacterEncoding());
		// //request.setCharacterEncoding("UTF-8");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Expires", "0");
		response.setContentType("application/xml;charset=UTF-8");
		request.setCharacterEncoding("GBK");
		response.setCharacterEncoding("UTF-8");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + File.separatorChar + "MM");
			String ym = sdf.format(new java.util.Date());
			String sp = "";
			if (Common.SAVE_PATH.length() == 0) {
				Common.SAVE_PATH = System.getProperty("user.dir")+"/../webapps/" + request.getContextPath() + "/attachement/";
			}
			if (Common.TEMP_PATH.length() == 0) {
				Common.TEMP_PATH = System.getProperty("user.dir")+"/../temp/";
			}
			if (Common.SAVE_PATH.endsWith("/") || Common.SAVE_PATH.endsWith("\\"))
				sp = Common.SAVE_PATH + ym;
			else
				sp = Common.SAVE_PATH + File.separatorChar + ym;
			File uploadPath = new File(sp);
			if (!uploadPath.exists()) {
				uploadPath.mkdirs();
			}
			File tempPath = new File(Common.TEMP_PATH);
			if (!tempPath.exists()) {
				tempPath.mkdirs();
			}
			// Create a factory for disk-based file items
			DiskFileItemFactory factory = new DiskFileItemFactory();

			// Set factory constraints
			factory.setSizeThreshold(Common.UPLOAD_CACHE);
			factory.setRepository(tempPath);

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			// Set overall request size constraint
			upload.setSizeMax(Common.FILE_MAX_SIZE);

			List<FileItem> items = upload.parseRequest(request);// 得到所有的文件
			Iterator<FileItem> i = items.iterator();
			String module = "NO";
			for(FileItem fi : items){
				if (fi.getFieldName().equals("module")) {
					module = fi.getString();
					break;
				}
			}
			if (module == null || module.equals(""))
				module = "NO";
			String retStr = "";
			String modelId = request.getParameter("modelId");
			while (i.hasNext()) {
				FileItem item = (FileItem) i.next();
				if (!item.isFormField()) {// 如果是附件框
					String fileName = item.getName();
					System.out.println("upload file----:" + fileName);
					if (fileName != null) {
						long fileOid = getOid();
						// if (fullFile.exists()){
						String originName = "";
						if (fileName.indexOf("\\") != -1)
							originName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						else if (fileName.indexOf("/") != -1)
							originName = fileName.substring(fileName.lastIndexOf("/") + 1);
						else
							originName = fileName;

						String fName = originName;
						if (fName.lastIndexOf(".") != -1)
							fName = fileOid + fName.substring(fName.lastIndexOf("."));
						else
							fName = fileOid + "";
						File savedFile = new File(uploadPath, fName);
						
						item.write(savedFile);

						FileInputStream fi = new FileInputStream(savedFile);
						byte[] b = new byte[2];
						fi.read(b);
						if (new String(b).trim().length()==0) {
							throw new Exception(StringUtil.escapeXml("The file Content is Null!"));
						}
						createRecord(fileOid, module, uploadPath + "/" + fName, originName);

						if (!retStr.equals(""))
							retStr += ",";
						retStr += "{oid:'" + fileOid + "',name:'" + originName + "'}";
						// }
					}
				} else {
					if (item.getSize() == 0||item.getName()==null) 
						continue;
					File savedFile = new File(System.getProperty("user.dir")+"/../temp/", modelId+"_read.xls");
					item.write(savedFile);
					new ExcelUtil().readModelExcel(savedFile, modelId);
				}
			}
			retStr = "[" + retStr + "]";
			response.getWriter().print(
					"<response success=\"true\">" + StringUtil.escapeXml(retStr) + "</response>");
		} catch (Exception e) {
			// 可以跳转出错页面
			response.getWriter().print(
					"<response success=\"false\">" + StringUtil.escapeJavaScript(e.toString())
							+ "</response>");
			e.printStackTrace();
		}
	}

	/**
	 * 存储附件信息
	 * 
	 * @param module
	 * @param filePath
	 * @param fileName
	 * @throws Exception
	 */
	public static void createRecord(long oid, String module, String filePath, String fileName)
			throws Exception {
		String sql = "insert into upload_files(oid,module,file_path,file_name,upload_time) values(?,?,?,?,sysdate)";
		DBUtil.getJdbcTemplate().update(sql, new Object[] { oid, module, filePath, fileName });
	}

	public static FileInfo getRecordByOid(long oid) throws Exception {
		String sql = "select * from upload_files t where t.oid = ?";
		FileInfo ret = (FileInfo) DBUtil.getJdbcTemplate().query(sql, new Object[] { oid },
				new ResultSetExtractor<Object>() {
					public FileInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
						if (rs.next()) {
							FileInfo fInfo = new FileInfo();

							fInfo.setFile_path(rs.getString("file_path"));
							fInfo.setFile_name(rs.getString("file_name"));
							fInfo.setUpload_time(rs.getString("upload_time"));
							return fInfo;
						}
						return null;
					}
				});
		return ret;
	}

	public static long getOid() throws Exception {
		String sql = "select seq_upload.nextval oid from dual";
		long oid = DBUtil.getJdbcTemplate().queryForLong(sql);
		return oid;
	}
}
