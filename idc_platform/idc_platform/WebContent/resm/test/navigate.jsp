<%@ page contentType="text/html; charset=GBK" language="java"%>
<%@ page import="java.util.*"%>
[{
	leaf:false,
	text: '1.��ѯ����',
	children:[ {
			leaf:true,
			text: '1.1 �����ѯ',
			href: 'code.jsp'
		}, {
			leaf:true,
			text: '1.2 ģ�Ͳ�ѯ',
			href: 'model.jsp'
		}, {
			leaf:true,
			text: '1.3 �������Ͳ�ѯ',
			href: 'attributeType.jsp'
		},{
			leaf:true,
			text: '1.4 ��Դ���Բ�ѯ',
			href: 'attribute.jsp'
		},{
			leaf:true,
			text: '1.5 ��Դ�����ѯ',
			href: 'resource.jsp'
		},{
			leaf:true,
			text: '1.6 ���ʽ��ѯ',
			href: 'expression.jsp'
		}]
},{
	leaf:false,
	text: '2.�������� <font color=red>д����ҪС�ģ�</font>',
	children:[ {
			leaf:true,
			text: '2.1 ����/ɾ��ģ����ϸ��',
			href: 'createTable.jsp'
		},{
			leaf:true,
			text: '2.2 ������Դ����',
			href: 'createRes.jsp?modelId=server'
		},{
			leaf:true,
			text: '2.3 ������Դ����',
			href: 'resRelation.jsp'
		}]
}
]
