
	//һ������
	var root = new Ext.tree.TreeNode({
		id:"root",
		text:"���ڵ�"
	});

	//ҵ��ͳ��
	var businessCount = new Ext.tree.TreeNode({
		id:"userCount",
		text:"ҵ��ͳ��",
		expanded:true
	});
	

	
	//ҵ�񹤵�ͳ�Ʊ���
	var businessReportManager1 = new Ext.tree.TreeNode({
		id:"businessReportManager1",
		text:"ҵ�񹤵�ͳ��",
		listeners:{
			"click":businessQueryFn1
		}
		
	});
	
	//ҵ��䶯ͳ�Ʊ���
	var businessReportManager2 = new Ext.tree.TreeNode({
		id:"businessReportManager2",
		text:"ҵ��䶯ͳ��",
		listeners:{
			"click":businessQueryFn2
		}
	});

	//����ҵ����Ϣͳ�Ʊ���
	var businessReportManager3 = new Ext.tree.TreeNode({
		id:"businessReportManager3",
		text:"����ҵ����Ϣͳ��",
		listeners:{
			"click":businessQueryFn3
		}
	});
	
	//��ֵҵ����Ϣͳ�Ʊ���
	var businessReportManager4 = new Ext.tree.TreeNode({
		id:"businessReportManager4",
		text:"��ֵҵ����Ϣͳ��",
		listeners:{
			"click":businessQueryFn4
		}
	});


	businessCount.appendChild(businessReportManager1);
	businessCount.appendChild(businessReportManager2);
	businessCount.appendChild(businessReportManager3);
	businessCount.appendChild(businessReportManager4);
	
	root.appendChild(businessCount);


	
	var menu = new Ext.tree.TreePanel({
		width : 200,
		border:false,
		root:root,
		rootVisible : false
	});

	var businessQueryPageIsOpen1 = false;
	var businessQueryPageIsOpen2 = false;
	var businessQueryPageIsOpen3 = false;
	var businessQueryPageIsOpen4 = false;