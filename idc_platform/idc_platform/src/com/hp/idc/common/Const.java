package com.hp.idc.common;

public class Const {

	/* 知识库管理模块常量KBM */
	public static final long KBM_ROOT_ID = 0;
	// ID
	public static final long KBM_CATE_EVENT = -1;

	public static final long KBM_CATE_CASE = -2;

	// 所属知识库-知识点 Base_Type
	public static final int KBM_BASETYPE_EVENT = 1;

	// 所属知识库-案例
	public static final int KBM_BASETYPE_CASE = 2;

	//
	public static final String KBM_MSG_SAVE_EVENT = "故障知识类别保存成功!";

	public static final String KBM_MSG_SAVE_CASE = "案例类别保存成功!";

	public static final String KBM_MSG_SAVE_KNOWLEDGE = "知识点保存成功!";
	public static final String KBM_MSG_ERROR = "操作失败!";

	// 节点是否废弃
	public static final int KBM_ISRETIRED_YES = 1;//

	public static final int KBM_ISRETIRED_NO = 0;//

	public static final String KBM_MSG_BLANK = "不能为空，请填写";

	public static final String KBM_MSG_DEL_ERROR = "删除失败!";

	public static final String KBM_MSG_DEL_SUCCESS = "删除成功!";

	/* 客户关系管理模块常量KBM */
	// ICP许可证：是
	public static final long CUS_ICP_YES = 1;
	// ICP许可证：否
	public static final long CUS_ICP_NO = 0;
	
	
}
