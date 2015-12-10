<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
[{
	leaf:false,
	text: '1.查询测试',
	children:[ {
			leaf:true,
			text: '1.1 代码查询',
			href: 'code.jsp'
		}, {
			leaf:true,
			text: '1.2 模型查询',
			href: 'model.jsp'
		}, {
			leaf:true,
			text: '1.3 属性类型查询',
			href: 'attributeType.jsp'
		},{
			leaf:true,
			text: '1.4 资源属性查询',
			href: 'attribute.jsp'
		},{
			leaf:true,
			text: '1.5 资源对象查询',
			href: 'resource.jsp'
		},{
			leaf:true,
			text: '1.6 表达式查询',
			href: 'expression.jsp'
		}]
},{
	leaf:false,
	text: '2.操作测试 <font color=red>写操作要小心！</font>',
	children:[ {
			leaf:true,
			text: '2.1 创建/删除模型明细表',
			href: 'createTable.jsp'
		},{
			leaf:true,
			text: '2.2 创建资源对象',
			href: 'createRes.jsp?modelId=server'
		},{
			leaf:true,
			text: '2.3 关联资源对象',
			href: 'resRelation.jsp'
		}]
}
]
