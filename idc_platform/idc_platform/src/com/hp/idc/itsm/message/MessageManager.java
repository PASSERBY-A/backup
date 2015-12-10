package com.hp.idc.itsm.message;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hp.idc.context.util.ContextUtil;
import com.hp.idc.itsm.common.ItsmBean;
import com.hp.idc.itsm.configure.FieldInfo;
import com.hp.idc.itsm.dbo.ColumnData;
import com.hp.idc.itsm.dbo.OracleOperation;
import com.hp.idc.itsm.impl.ITSMFieldManagerImpl;
import com.hp.idc.itsm.inter.PersonInfoInterface;
import com.hp.idc.itsm.security.PersonManager;
import com.hp.idc.itsm.task.TaskData;
import com.hp.idc.itsm.util.DateTimeUtil;
import com.hp.idc.itsm.util.ItsmUtil;

/**
 * 表示消息管理类 可以通过此类发送短信、邮件及站内信息 相关数据表：itsm_message
 * 
 * @author 梅园
 * 
 */
public class MessageManager {
	
	/**
	 * 发送SMS的对象
	 */
	public static ISmsSender SmsSender = null;
	
	/**
	 * 消息类型：短信
	 */
	public static int TYPE_SMS = 1;
	
	/**
	 * 更新消息处理结果
	 * @param oid
	 * @param type
	 * @param success
	 * @param remark
	 * @param operName
	 * @throws SQLException
	 */
	public static void updateMessage(int oid, int type, boolean success, String remark, String operName) throws SQLException {
		ResultSet rs = null;
		OracleOperation u = new OracleOperation("itsm_message", operName);
		PreparedStatement ps = null;
		try {
			ps = u.getStatement("msg_oid=?");
			ps.setInt(1, oid);
			if (type == TYPE_SMS)
				u.setColumnData("msg_sms", new ColumnData(
						ColumnData.TYPE_INTEGER, new Integer(success ? 3 : 4)));
			if (!success)
				u.setColumnData("msg_remark", new ColumnData(
						ColumnData.TYPE_STRING, remark));
			u.setColumnData("msg_process", new ColumnData(
					ColumnData.TYPE_DATETIME, DateTimeUtil.formatDate(new Date(), "yyyyMMddHHmmss")));
			rs = ps.executeQuery();
			u.executeUpdate(rs);
		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			u.commit(rs);
		}

	}
	
	/**
	 * 获取待发送短信
	 * @param count 获取条数
	 * @param operName 操作人
	 * @return 返回待发送的短信 List<SmsMessage>
	 * @throws SQLException 
	 */
	public static List<SmsMessage> getSmsMessage(int count, String operName) throws SQLException {
		List<SmsMessage> ret = new ArrayList<SmsMessage>();
		ResultSet rs = null;
		OracleOperation u = new OracleOperation();
		PreparedStatement ps = null;
		try {
			ps = u.getSelectStatement("select t.* from itsm_message t where msg_sms=1 and msg_send<=sysdate");
			rs = ps.executeQuery();
			for (int i = 0; i < count; i++) {
				if (!rs.next())
					break;
				SmsMessage info = new SmsMessage();
				info.setOid(rs.getInt("msg_oid"));
				String to = rs.getString("msg_to");
				String from = rs.getString("msg_from");
				Pattern p = Pattern.compile("^1[0-9]{10}$");
				Matcher m = p.matcher(to);
				if (m.find()){
					info.setReceiver(to);
				} else {
					PersonInfoInterface p_to = PersonManager.getPersonById(to);
					if (p_to == null) {
						updateMessage(info.getOid(), TYPE_SMS, false, "找不到接收人", operName);
						continue;
					}
					info.setReceiver(p_to.getMobile());
				}
				PersonInfoInterface p_from = PersonManager.getPersonById(from);
				if (p_from != null)
					info.setSender(p_from.getMobile());
				info.setContent(rs.getString("msg_content"));
				ret.add(info);
			}
		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			u.commit(rs);
		}
		return ret;
	}

	/**
	 * 发送短信息接口
	 * 
	 * @param msg
	 *            短信内容
	 * @param to
	 *            接收人ID
	 * @param from
	 *            发送人ID
	 * @param sendTime
	 *            发送时间
	 * @param operName
	 *            操作人ID
	 * @return 返回信息ID
	 * @throws SQLException
	 *             写数据库失败时引发
	 */
	public static int sendSms(String msg, String to, String from,
			Date sendTime, String operName) throws SQLException {
		boolean isSendsms = true;
		ItsmBean bean = (ItsmBean) ContextUtil.getBean("itsmBean");
		if(bean != null && bean.getIsSendSms().equals("0"))
			isSendsms = false;
		return sendMessage(null, msg, false, isSendsms, false, to, from, sendTime,
				operName);
	}

	/**
	 * 发送信息接口，包括短信、邮件及站内信息
	 * 
	 * @param title
	 *            信息标题
	 * @param content
	 *            信息内容
	 * @param email
	 *            是否发送邮件
	 * @param sms
	 *            是否发送短信
	 * @param internal
	 *            是否发送站内信息
	 * @param to
	 *            接收人ID
	 * @param from
	 *            发送人ID
	 * @param sendTime
	 *            发送时间
	 * @param operName
	 *            操作人ID
	 * @return 返回信息ID
	 * @throws SQLException
	 *             写数据库失败时引发
	 */
	public static int sendMessage(String title, String content, boolean email,
			boolean sms, boolean internal, String to, String from,
			Date sendTime, String operName) throws SQLException {
		int oid = ItsmUtil.getSequence("message");
		ResultSet rs = null;
		OracleOperation u = new OracleOperation("itsm_message", operName);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			u.setColumnData("msg_oid", new ColumnData(ColumnData.TYPE_INTEGER,
					new Integer(oid)));
			u.setColumnData("msg_title", new ColumnData(ColumnData.TYPE_STRING,
					title));
			u.setColumnData("msg_content", new ColumnData(
					ColumnData.TYPE_STRING, content));
			u.setColumnData("msg_email", new ColumnData(
					ColumnData.TYPE_INTEGER, new Integer(email ? 1 : 0)));
			u.setColumnData("msg_sms", new ColumnData(ColumnData.TYPE_INTEGER,
					new Integer(sms ? 1 : 0)));
			u.setColumnData("msg_internal", new ColumnData(
					ColumnData.TYPE_INTEGER, new Integer(internal ? 1 : 0)));
			u.setColumnData("msg_to",
					new ColumnData(ColumnData.TYPE_STRING, to));
			u.setColumnData("msg_from", new ColumnData(ColumnData.TYPE_STRING,
					from));
			u.setColumnData("msg_create", new ColumnData(
					ColumnData.TYPE_DATETIME, sdf.format(new Date())));
			u.setColumnData("msg_send", new ColumnData(
					ColumnData.TYPE_DATETIME, sdf.format(sendTime)));
			u.setColumnData("msg_process", new ColumnData(ColumnData.TYPE_NULL,
					null));

			rs = u.getResultSet(null);
			u.executeInsert(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			u.commit(rs);
		}
		return oid;
	}
	
	/**
	 * 处理短信里的宏
	 * @param smsTemplate
	 * @param args
	 * @return
	 */
	public static String getInjectedMessage(String smsTemplate,String[] args, TaskData td){
		if (smsTemplate == null)
			return "";
		if (args == null)
			return smsTemplate;
		for (int i = 0; i < args.length; i++){
			String s = args[i];
			s = s == null?"":s;
			s = s.replaceAll("\\$", "\\\\\\$");
			smsTemplate = smsTemplate.replaceAll("\\$\\{"+(i+1)+"\\}", s);
		}
		smsTemplate = getInjectedReceiver(smsTemplate,td);
		String regx = "(getFV\\(\"([a-zA-Z0-9_]+)\"\\))";
		Pattern p = Pattern.compile(regx);
		
		Matcher m = p.matcher(smsTemplate);
		while (m.find()) {
			String fieldId = m.group(2);
			String ret1 = m.group(1);
			ret1 = ret1.replaceAll("\\(", "[(]");
			ret1 = ret1.replaceAll("\\)", "[)]");
			String fieldValue = td.getAttribute(fieldId);
			ITSMFieldManagerImpl impl = new ITSMFieldManagerImpl();
			FieldInfo fi = impl.getFieldById(fieldId);
			fieldValue = fi.getHtmlCode(fieldValue);
			smsTemplate = smsTemplate.replaceAll(ret1, fieldValue);
		}
		return smsTemplate;
	}
	
	/**
	 * 替换宏getFV(fieldId)
	 * @param str
	 * @param td
	 * @return
	 */
	public static String getInjectedReceiver(String str, TaskData td){
		if (str == null)
			return "";
		String regx = "(getFV\\(\"([a-zA-Z0-9_]+)\"\\))";
		Pattern p = Pattern.compile(regx);
		
		Matcher m = p.matcher(str);
		while (m.find()) {
			String fieldId = m.group(2);
			String ret1 = m.group(1);
			ret1 = ret1.replaceAll("\\(", "[(]");
			ret1 = ret1.replaceAll("\\)", "[)]");
			String fieldValue = td.getAttribute(fieldId);
			str = str.replaceAll(ret1, fieldValue);
		}
		return str;
	}

}
