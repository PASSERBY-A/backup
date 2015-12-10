package com.hp.idc.itsm.configure.fields;

import java.net.URLDecoder;
import java.net.URLEncoder;

import com.hp.idc.itsm.common.Consts;
import com.hp.idc.itsm.configure.FieldInfo;
import com.hp.idc.itsm.util.StringUtil;
import com.hp.idc.json.JSONArray;
import com.hp.idc.json.JSONException;
import com.hp.idc.json.JSONObject;

/**
 * �����ֶ�
 * @author ÷԰<br>
 * 
 * v1:�����ϴ���Ϊjson��ʽ��ͬʱ�����ϵĸ�ʽ���ݣ�2009-04-01��<a mailto:lihz@lianchuang.com>fluted</a>
 *
 */
public class FileFieldInfo extends FieldInfo{
	
	
	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#clone()
	 */
	public Object clone() {
		return new FileFieldInfo();
	}

	/**
	 * 
	 * @see com.hp.idc.itsm.configure.FieldInfo#getFormCode(int)
	 */
	public String getFormCode(int width) {
		StringBuffer sb = new StringBuffer();
		sb.append("var " + Consts.FLD_PREFIX + getId() +
			" = new Ext.form.UploadField2({");
		if (this.transform != null)
			sb.append("transform:" + this.transform + ",");
		sb.append("fieldLabel:\"" + StringUtil.escapeJavaScript(getName()) + "\",");
		sb.append("hiddenName:'" + Consts.FLD_PREFIX + getId() + "',");
		sb.append("uploadUrl:'"+Consts.UPLOAD_SERVLET+"',");
		sb.append("downLoadUrl:'"+Consts.DOWNLOAD_SERVLET+"',");
		if (this.notNull)
			sb.append("allowBlank:false,");
		String d_ = getDesc();
		if (d_.equals(getName()))
			d_ = "";
		sb.append("desc:'"+ StringUtil.escapeJavaScript(d_) +"',");
		sb.append("module:'ITSM',");
		sb.append("width:" + width);
		sb.append(",msgTarget:'title'});\n");
		if (this.readOnly)
			sb.append(Consts.FLD_PREFIX + getId() + ".setReadOnly(true);\n");
		return sb.toString();
	}
	
	/**
	 * v1:��ֵ�ĸ�ʽ����json��ʽ�����Լ����ϰ汾�ĸ�ʽ
	 * @see com.hp.idc.itsm.configure.FieldInfo#getHtmlCode(java.lang.String)
	 */
	public String getHtmlCode(String value){
		JSONArray ja = new JSONArray();
		if (value == null || value.equals(""))
			return "";
		//�����ϰ汾�Ľṹ
		if(value.charAt(0) == '/' || value.indexOf("uploads/") == 0){
			String[] files = value.split(";");
			for (int i = 0, pos; i < files.length; i++) {
				if (files[i].length() == 0)
					continue;
				String fileName = files[i];
				String filePath = "";
				if ((pos = fileName.lastIndexOf("/")) != -1){
					fileName = fileName.substring(pos + 1);
					filePath = files[i];
				} else if ((pos = fileName.lastIndexOf("\\")) != -1){
					fileName = fileName.substring(pos + 1);
					filePath = files[i];
				} else {
					if ((pos = fileName.lastIndexOf("fileName=")) != -1){
						fileName = fileName.substring(pos + 9);
						int _id = fileName.indexOf("&");
						if (_id!=-1)
							fileName = fileName.substring(0,_id);
						fileName = URLDecoder.decode(fileName);
					}
					filePath = Consts.ITSM_HOME+"/"+files[i];
				}
				JSONObject jo = new JSONObject();
				try {
					jo.put("fp", filePath);
					jo.put("fn", fileName);
					jo.put("oldFormat", true);
					jo.put("newAdd", false);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				ja.put(jo);
				
			}
		} else {
			try {
				ja = new JSONArray(value);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		//���ɰ������ӵ�ַ�Ĵ�
		StringBuffer sb = new StringBuffer();
		for (int i = 0, count = 0; i < ja.length(); i++) {
			if (count++ > 0)
				sb.append("<br/>");
			try {
				JSONObject jo = ja.getJSONObject(i);
				if (jo.has("oldFormat") && jo.getBoolean("oldFormat")){
					String fileName = jo.getString("fn");
					String filePath = jo.getString("fp");
					String upTime = "\\(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\)$";
					filePath = filePath.replaceAll(upTime, "");
					fileName = fileName.replaceAll(upTime, "");
					if (!filePath.startsWith(Consts.ITSM_HOME+"/"))
						filePath = Consts.ITSM_HOME+"/download.jsp?fileName="+URLEncoder.encode(filePath);
					
					sb.append("<a href=\""+filePath+"\">"+fileName+"</a>");
				} else {
					String fileOid = jo.getString("oid");
					String fileName = jo.getString("name");
					sb.append("<a href=\""+Consts.DOWNLOAD_SERVLET+"?oid="+fileOid+"\">"+fileName+"</a>");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
