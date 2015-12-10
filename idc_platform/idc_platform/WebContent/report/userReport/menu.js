
	//一级导航
	var root = new Ext.tree.TreeNode({
		id:"root",
		text:"根节点"
	});

	//客户统计
	var userCount = new Ext.tree.TreeNode({
		id:"userCount",
		text:"客户统计",
		expanded:true
	});


	
	//客户服务统计报告
	var UserReportManager1 = new Ext.tree.TreeNode({
		id:"userReportManager1",
		text:"客户服务统计",
		listeners:{
			"click":userQueryFn1
		}
		
	});
	
	//客户服务清单报告
	var UserReportManager2 = new Ext.tree.TreeNode({
		id:"userReportManager2",
		text:"客户服务清单",
		listeners:{
			"click":userQueryFn2
		}
	});

	//客户变动明细报告
	var UserReportManager3 = new Ext.tree.TreeNode({
		id:"userReportManager3",
		text:"客户变动明细",
		listeners:{
			"click":userQueryFn3
		}
	});
	
	//客户变动统计报告
	var UserReportManager4 = new Ext.tree.TreeNode({
		id:"userReportManager4",
		text:"客户变动统计",
		listeners:{
			"click":userQueryFn4
		}
	});

	/*//客户业务类别统计报告
	var UserReportManager5 = new Ext.tree.TreeNode({
		id:"userReportManager5",
		text:"客户业务类别统计",
		listeners:{
			"click":userQueryFn5
		}
	});*/

	/*//客户物品进入统计报告
	var UserReportManager6 = new Ext.tree.TreeNode({
		id:"userReportManager6",
		text:"客户物品进入统计",
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
	