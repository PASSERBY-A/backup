
	//һ������
	var root = new Ext.tree.TreeNode({
		id:"root",
		text:"���ڵ�"
	});

	//�ͻ�ͳ��
	var userCount = new Ext.tree.TreeNode({
		id:"userCount",
		text:"�ͻ�ͳ��",
		expanded:true
	});


	
	//�ͻ�����ͳ�Ʊ���
	var UserReportManager1 = new Ext.tree.TreeNode({
		id:"userReportManager1",
		text:"�ͻ�����ͳ��",
		listeners:{
			"click":userQueryFn1
		}
		
	});
	
	//�ͻ������嵥����
	var UserReportManager2 = new Ext.tree.TreeNode({
		id:"userReportManager2",
		text:"�ͻ������嵥",
		listeners:{
			"click":userQueryFn2
		}
	});

	//�ͻ��䶯��ϸ����
	var UserReportManager3 = new Ext.tree.TreeNode({
		id:"userReportManager3",
		text:"�ͻ��䶯��ϸ",
		listeners:{
			"click":userQueryFn3
		}
	});
	
	//�ͻ��䶯ͳ�Ʊ���
	var UserReportManager4 = new Ext.tree.TreeNode({
		id:"userReportManager4",
		text:"�ͻ��䶯ͳ��",
		listeners:{
			"click":userQueryFn4
		}
	});

	/*//�ͻ�ҵ�����ͳ�Ʊ���
	var UserReportManager5 = new Ext.tree.TreeNode({
		id:"userReportManager5",
		text:"�ͻ�ҵ�����ͳ��",
		listeners:{
			"click":userQueryFn5
		}
	});*/

	/*//�ͻ���Ʒ����ͳ�Ʊ���
	var UserReportManager6 = new Ext.tree.TreeNode({
		id:"userReportManager6",
		text:"�ͻ���Ʒ����ͳ��",
		listeners:{
			"click":userQueryFn6
		}
	});*/
		

	
	userCount.appendChild(UserReportManager1);
	userCount.appendChild(UserReportManager2);
	userCount.appendChild(UserReportManager3);
	userCount.appendChild(UserReportManager4);
	/*userCount.appendChild(UserReportManager5);
	userCount.appendChild(UserReportManager6);*/

	root.appendChild(userCount);
	
	var menu = new Ext.tree.TreePanel({
		width : 200,
		border:false,
		root:root,
		rootVisible : false
	});

	
	var userQueryPageIsOpen1 = false;
	var userQueryPageIsOpen2 = false;
	var userQueryPageIsOpen3 = false;
	var userQueryPageIsOpen4 = false;
	/*var userQueryPageIsOpen5 = false;
	var userQueryPageIsOpen6 = false;*/
	