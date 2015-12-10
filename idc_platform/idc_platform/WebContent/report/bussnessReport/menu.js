
	//一级导航
	var root = new Ext.tree.TreeNode({
		id:"root",
		text:"根节点"
	});

	//业务统计
	var businessCount = new Ext.tree.TreeNode({
		id:"userCount",
		text:"业务统计",
		expanded:true
	});
	

	
	//业务工单统计报告
	var businessReportManager1 = new Ext.tree.TreeNode({
		id:"businessReportManager1",
		text:"业务工单统计",
		listeners:{
			"click":businessQueryFn1
		}
		
	});
	
	//业务变动统计报告
	var businessReportManager2 = new Ext.tree.TreeNode({
		id:"businessReportManager2",
		text:"业务变动统计",
		listeners:{
			"click":businessQueryFn2
		}
	});

	//基本业务信息统计报告
	var businessReportManager3 = new Ext.tree.TreeNode({
		id:"businessReportManager3",
		text:"基本业务信息统计",
		listeners:{
			"click":businessQueryFn3
		}
	});
	
	//增值业务信息统计报告
	var businessReportManager4 = new Ext.tree.TreeNode({
		id:"businessReportManager4",
		text:"增值业务信息统计",
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