package com.hp.idc.portal.security;

import java.util.*;

import com.hp.idc.common.CodePage;
import com.hp.idc.portal.mgr.SystemConfig;

public class Common {

	public static String Split_Str = "_|_";

	public void init() throws Exception {
		/**
		 * 初始化缓存数据
		 */
		Cache.reloadAuc();
		
		//for idc use the common/codepage
		//Cache.initCodePage();
		Cache.CodeMapping = CodePage.codeMapping;
		
		SystemConfig.initData();
	}

	/**
	 * 附件下载的servlet，新版本的上传附件组件使用。
	 */
	public static String DOWNLOAD_SERVLET = "/portal/download";

	public static String UPLOAD_SERVLET = "/portal/upload";

	/**
	 * 通知人员
	 */
	public static String NOTICE_TO = "notice_to";

	/**
	 * 通知消息
	 */
	public static String NOTICE_MESSAGE = "notice_message";

	/**
	 * 管理员的登录名
	 */
	public static String ADMIN_ID = "root";

	/**
	 * CI与CI之间的关联关系
	 */
	public static int RELATION_CI_CI = 0;

	/**
	 * 每页记录数
	 */
	public static int ITEMS_PER_PAGE = 20;

	/**
	 * 本页记录描述
	 */
	public static String MSG_PAGE_DISPLAY = "第 {0} - {1} 条，共 {2} 条记录";

	/**
	 * 无记录的供述
	 */
	public static String MSG_PAGE_EMPTY = "无记录";

	/**
	 * 正在处理的描述
	 */
	public static String MSG_WAIT = "正在处理，请稍候...";

	/**
	 * 成功时的提示框标题
	 */
	public static String MSG_BOXTITLE_SUCCESS = "信息";

	/**
	 * 失败时的提示框标题
	 */
	public static String MSG_BOXTITLE_FAILED = "失败";

	/**
	 * 成功时的提示框内容
	 */
	public static String MSG_SUCCESS = "您的请求已被成功的处理。";

	/**
	 * 生成表单时的名称前缀 如FieldInfo.getId() == "user_name"时 生成的html为<input
	 * name="fld_user_name" ..../>
	 */
	public static String FLD_PREFIX = "fld_";

	/**
	 * “执行人”字段的ID
	 */
	public static String FLD_EXECUTE_USER = "execute_user";

	/**
	 * "抄送人"或者叫“阅知人”字段ID，若提交表单里此字段不为空 则相关人员会受到一条待阅工单，打开后，只能填写阅知信息。
	 */
	public static String FLD_READ_USER = "read_user";

	/**
	 * “执行人”字段的ID,包含分配到组的
	 */
	public static String FLD_EXECUTE_GROUP_USER = "execute_group_user";

	/**
	 * "附件"字段
	 */
	public static String FLD_ATTACHMENT = "attachments";

	/**
	 * “回退”字段的ID
	 */
	public static String FLD_ROLLBACK = "rollback";

	/**
	 * “消息”字段的ID
	 */
	public static String FLD_MESSAGE = "message";

	/**
	 * 系统字段列表
	 */
	public static Map<String, String> SYSTEM_FIELDS = new HashMap<String, String>();

	/**
	 * 配置项状态：正常
	 */
	public static int STATUS_NORMAL = 0;

	/**
	 * 配置项状态：已删除
	 */
	public static int STATUS_DELETED = 1;
	
	/**
	 * 默认表单ID
	 */
	public static int FORM_DEFAULT = 1;

	/**
	 * 代码类型：配置项类别
	 */
	public static int CODETYPE_CICATEGORY = 1;

	/**
	 * 代码类型：模块类别
	 */
	public static int CODETYPE_MODULE = 2;

	/**
	 * 配置项类别：组织
	 */
	public static int CICATEGORY_ORGANIZATION = 3;

	/**
	 * 配置项类别：工作组
	 */
	public static int CICATEGORY_WORKGROUP = 4;

	/**
	 * 配置项类别：人员
	 */
	public static int CICATEGORY_PERSON = 5;

	/**
	 * 关系:工作组->个人
	 */
	public static int RT_WORKGROUP_PERSON = 1;

	/**
	 * 关系:个人->工作组
	 */
	public static int RT_PERSON_WORKGROUP = 2;

	/**
	 * 关系:组织->个人
	 */
	public static int RT_ORGANIZATION_PERSON = 3;

	/**
	 * 关系:个人->组织
	 */
	public static int RT_PERSON_ORGANIZATION = 4;

	/**
	 * 本地工作组
	 */
	public static int RT_LOCALGROUP_PERSON = 5;

	public String getDOWNLOAD_SERVLET() {
		return DOWNLOAD_SERVLET;
	}

	public void setDOWNLOAD_SERVLET(String down_servlet) {
		DOWNLOAD_SERVLET = down_servlet;
	}

	public String getUPLOAD_SERVLET() {
		return UPLOAD_SERVLET;
	}

	public void setUPLOAD_SERVLET(String upload_servlet) {
		UPLOAD_SERVLET = upload_servlet;
	}

}
