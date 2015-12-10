<%@ page language="java" contentType="text/html; charset=utf-8"%>
[ {
	id : 'views',
	text : '系统管理',
	expanded : true,
	cls : 'feeds-node',
	singleClickExpand : true,
	children : [ {
		id : 'layout_mgr',
		text : '布局管理',
		cls : 'feed',
		leaf : true,
		href : 'layout/index.jsp'
	}, {
		id : 'node_conf',
		text : '节点配置',
		cls : 'feed',
		leaf : true,
		href : 'node/index.jsp'
	}, {
		id : 'menu_mgr',
		text : '菜单管理',
		cls : 'feed',
		leaf : true,
		href : 'menu/index.jsp'
	}, {
		id : 'sys_conf',
		text : '系统配置',
		cls : 'feed',
		leaf : true,
		href : 'systemConf.jsp'
	} ]
}, {
	id : 'resm',
	text : '资源管理配置',
	expanded : true,
	cls : 'feeds-node',
	singleClickExpand : true,
	children : [ {
		id : 'resm_admin',
		text : '数据模型管理',
		cls : 'feed',
		leaf : true,
		href : '/resm/resm/ResourceAdmin.html'
	}, {
		id : 'resm_role_admin',
		text : '权限管理',
		cls : 'feed',
		leaf : true,
		href : '/resm/resm/RoleAdmin.jsp'
	} ]
}, {
	id : 'aboutMe',
	text : '个人相关',
	expanded : true,
	cls : 'feeds-node',
	singleClickExpand : true,
	children : [ {
		id : 'workPlan',
		text : '工作计划',
		cls : 'feed',
		leaf : true,
		href : 'workPlan/index.jsp'
	}, {
		id : 'document',
		text : '个人文档',
		cls : 'feed',
		leaf : true,
		href : 'document/index.jsp'
	}, {
		id : 'view_conf',
		text : '视图配置',
		cls : 'feed',
		leaf : true,
		href : 'view/index.jsp'
	}, {
		id : 'favorites',
		text : '收藏夹',
		cls : 'feed',
		leaf : true,
		href : 'favorites/index.jsp'
	}, {
		id : 'message',
		text : '消息中心',
		cls : 'feed',
		leaf : true,
		href : 'message/index.jsp'
	}, {
		id : 'menu_param',
		text : '菜单参数管理',
		cls : 'feed',
		leaf : true,
		href : 'menu/param.jsp'
	} ]
} ]