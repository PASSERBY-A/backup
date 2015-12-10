package com.hp.idc.components
{
	import com.hp.idc.components.resource.edit.IResourceAttributeEdit;
	import com.hp.idc.components.resource.edit.IResourceAttributeEditBase;
	import com.hp.idc.components.resource.edit.base.BaseGroup;
	import com.hp.idc.components.resource.edit.base.physics.PhysicsGroup;
	import com.hp.idc.components.resource.edit.base.physics.PhysicsGroup1;
	import com.hp.idc.components.resource.edit.base.physics.server.ServerGroup;
	import com.hp.idc.components.resource.model.StringAttributeInput;
	import com.hp.idc.util.UiUtil;
	
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.controls.Image;
	import mx.controls.Spacer;
	import mx.controls.Tree;
	import mx.graphics.GradientEntry;
	import mx.graphics.LinearGradient;
	import mx.rpc.AbstractOperation;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import spark.components.Button;
	import spark.components.HGroup;
	import spark.components.Label;
	import spark.components.NavigatorContent;
	import spark.components.Scroller;
	import spark.components.VGroup;
	import spark.layouts.VerticalLayout;
	import spark.primitives.Rect;
	
	public class ResourceObjectUpdate extends NavigatorContent
	{
		private var editTag:Boolean  = false;
		
		private var tree:Tree;
		private var content:VGroup;
		
		private var base:BaseGroup;
		private var physics:PhysicsGroup1;
		private var server:ServerGroup;
		
		
		public function ResourceObjectUpdate(param:ArrayCollection)
		{
			super();
			this.editTag = param.getItemAt(0) as Boolean;
			createComponents();
			
			initTreeDate();
			
		}
		
		private function createComponents():void{
			
			//添加背景
			var rect:Rect = new Rect();
			rect.left = 0;
			rect.right = 0;
			rect.top = 0;
			rect.height = 100;
			rect.radiusX = 0;
			
			var lineargradient:LinearGradient = new LinearGradient();
			lineargradient.rotation = 90;
			var gradientEntry1:GradientEntry = new GradientEntry();
			var gradientEntry2:GradientEntry = new GradientEntry();
			gradientEntry1.color = 0xD4EDE9;
			gradientEntry1.alpha = 1;
			
			gradientEntry2.color = 0xFFFFFF;
			gradientEntry2.alpha = 1;
			
			
				
			var entries1:Array = new Array();
			entries1.push(gradientEntry1);
			entries1.push(gradientEntry2);
			lineargradient.entries = entries1;
			
			rect.fill = lineargradient;
			
			
			//组件容器
			var group:VGroup = new VGroup();
			group.paddingLeft = 15;
			group.paddingTop = 15;
			group.paddingBottom = 10;
			group.paddingRight = 10;
			group.percentWidth = 100;
			group.percentHeight = 100;
			
			var hgroup:HGroup = new HGroup();
			hgroup.percentWidth = 100;
			hgroup.percentHeight = 100;
			
			//tree
			var treeGroup:VGroup = new VGroup();
			treeGroup.percentHeight = 100;
			treeGroup.width = 200;
			
			
			//tree标题
			var treetitleHgroup:HGroup = new HGroup();
			var treespacer:Spacer = new Spacer();
			treespacer.width = 15;
			var treetitleImag:Image = new Image();
			treetitleImag.source = "icons/arrows/icon11.png";
			var treetitleLabel:Label = new Label();
			treetitleLabel.text = "模型";
			treetitleLabel.setStyle("fontSize",14);
			treetitleHgroup.addElement(treespacer);
			treetitleHgroup.addElement(treetitleImag);
			treetitleHgroup.addElement(treetitleLabel);
			
			
			//常用工具栏
			var toolbar:RoundCornerPanel = new RoundCornerPanel();
			var layout:VerticalLayout = new VerticalLayout();
			layout.gap = 3;
			layout.paddingLeft = 5;
			layout.paddingTop = 10;
			toolbar.layout = layout;
			toolbar.height = 150;
			toolbar.width = 200;
			
			//工具栏标题
			var titleHgroup:HGroup = new HGroup();
			var spacer:Spacer = new Spacer();
			spacer.width = 15;
			var titleImag:Image = new Image();
			titleImag.source = "icons/arrows/icon11.png";
			var titleLabel:Label = new Label();
			titleLabel.text = "常用功能";
			titleLabel.setStyle("fontSize",14);
			titleHgroup.addElement(spacer);
			titleHgroup.addElement(titleImag);
			titleHgroup.addElement(titleLabel);
			
			var closeLink:ImageLink = new ImageLink();
			closeLink.leftSpaceWidth = 10;
			closeLink.image = "icons/arrows/right.gif";
			closeLink.label = "关闭当前页面";
			closeLink.addEventListener(MouseEvent.CLICK,closeCurrentPage)
			
			var hline:HSplitLine = new HSplitLine();
			hline.percentWidth = 98;
			
			toolbar.addElement(titleHgroup);
			toolbar.addElement(hline);
			toolbar.addElement(closeLink);
			
			
			treeGroup.addElement(toolbar);
			
			var subTreeGroup:RoundCornerPanel = new RoundCornerPanel();
			var subTreeGroupLayout:VerticalLayout = new VerticalLayout();
			subTreeGroupLayout.paddingLeft = 10;
			subTreeGroupLayout.paddingTop = 10;
			subTreeGroupLayout.paddingRight = 10;
			subTreeGroupLayout.paddingBottom = 10;
			subTreeGroup.layout = subTreeGroupLayout;
			subTreeGroup.percentWidth = 100;
			subTreeGroup.percentHeight = 100;

			this.tree = new Tree();
			tree.doubleClickEnabled = true;
			tree.setStyle("borderVisible",false);
			tree.showRoot = false;
			tree.labelField = "@label";
			tree.percentHeight = 100;
			tree.percentWidth = 100;
			tree.addEventListener(MouseEvent.DOUBLE_CLICK,tree_doubleClickHandler)
				
			
			var treehline:HSplitLine = new HSplitLine();
			treehline.percentWidth = 98;
			
			subTreeGroup.addElement(treetitleHgroup);
			subTreeGroup.addElement(treehline);
			subTreeGroup.addElement(tree);
			treeGroup.addElement(subTreeGroup);
			
			
			//content
			var scroller:Scroller = new Scroller();
			scroller.percentHeight = 100;
			scroller.percentWidth = 100;
			
			
			//保存按钮
			var buttonGroup:HGroup = new HGroup();
			buttonGroup.percentWidth = 100;
			buttonGroup.paddingBottom = 10;
			buttonGroup.paddingRight = 10;
			buttonGroup.horizontalAlign = "right";
			
			
			this.content = new VGroup();
			content.gap = 20;
			content.percentHeight = 100;
			content.percentWidth = 100;
			
			var saveButton:Button = new Button();
			saveButton.label = "保存";
			saveButton.addEventListener(MouseEvent.CLICK,saveResourceObject)
			buttonGroup.addElement(saveButton);
			
			var contentGroup:VGroup = new VGroup();
			contentGroup.percentHeight = 100;
			contentGroup.percentWidth = 100;
			contentGroup.addElement(content);
			contentGroup.addElement(buttonGroup);
			
			
			scroller.viewport = contentGroup;
			
			
			hgroup.addElement(treeGroup);
			hgroup.addElement(scroller);
			
			
			group.addElement(hgroup);
			this.addElement(rect);
			this.addElement(group);
		}
		
		private function tree_doubleClickHandler(event:MouseEvent):void
		{
			var id:String = tree.selectedItem.@id;
			getModeleAttribute(id);
		}
		//添加资源对象
		private function addResourceObject(params:ArrayCollection):void{
			
			var remoteOperation:RemoteObject = new RemoteObject();   
			remoteOperation.destination = "resourceUpdateService";   
			
			var op0:AbstractOperation = remoteOperation.getOperation( "addResource" );    
			
			op0.addEventListener(ResultEvent.RESULT,addSuccess);      
			op0.addEventListener(FaultEvent.FAULT,faultHandler);  
			remoteOperation.addResource("server",params,10086);
			
		}
		
		//调用后台，初始化tree
		private function initTreeDate():void{
			var remoteOperation:RemoteObject = new RemoteObject();   
			remoteOperation.destination = "modelService";   
			
			var op0:AbstractOperation = remoteOperation.getOperation( "getModelTree" );    
			
			op0.addEventListener(ResultEvent.RESULT,resultTreeHandler);      
			op0.addEventListener(FaultEvent.FAULT,faultHandler);  
			remoteOperation.getModelTree();
		}
		
		//模型初始化添加资源对象界面
		private function getModeleAttribute(modId:String):void{
			var remoteOperation:RemoteObject = new RemoteObject();   
			remoteOperation.destination = "modelService";   
			
			var op0:AbstractOperation = remoteOperation.getOperation( "getModelAttributesByModelId" );  
			op0.addEventListener(ResultEvent.RESULT,initContent);      
			op0.addEventListener(FaultEvent.FAULT,faultHandler);  
			remoteOperation.getModelAttributesByModelId(modId);
		}
		
		private function addSuccess(event:ResultEvent):void{
			Alert.show("添加成功！！");
		}
		
		private function resultTreeHandler(event:ResultEvent):void
		{
			var returnString :String = event.result as String;	
			var treeDate : XML = new XML(returnString);   
			
			tree.dataProvider = treeDate;   
		}
		
		private function initContent(event:ResultEvent):void{
			content.removeAllElements();
			var list:ArrayCollection = event.result as ArrayCollection;
			
			base = new BaseGroup(list,false);
			content.addElement(base.getContainer());
			
			physics = new PhysicsGroup1(list,false);
			content.addElement(physics.getContainer());
			
			server = new ServerGroup(list,false);
			content.addElement(server.getContainer());
		}
		
		private function faultHandler(event:FaultEvent):void
		{
			Alert.show(event.fault.getStackTrace(),"错误");
		}
		
		private function closeCurrentPage(event:MouseEvent):void{
			UiUtil.closeCurrent();
		}
		
		private function saveResourceObject(event:MouseEvent):void{
			var params:ArrayCollection = new ArrayCollection();
			
			var baseattributes:ArrayCollection = this.base.getResourceAttributes();
			for(var j:int=0;j<baseattributes.length;j++){
				var baseattribute:IResourceAttributeEditBase = baseattributes.getItemAt(j) as IResourceAttributeEditBase;
				params.addItem(baseattribute.getResourceAttribute());
			}
			
			var physicsattributes:ArrayCollection = this.physics.getResourceAttributes();
			for(var k:int=0;k<physicsattributes.length;k++){
				var phsicsattribute:IResourceAttributeEditBase = physicsattributes.getItemAt(k) as IResourceAttributeEditBase;
				params.addItem(phsicsattribute.getResourceAttribute());
			}
			
			
			var serverattributes:ArrayCollection = this.server.getResourceAttributes();
			for(var i:int=0;i<serverattributes.length;i++){
				var o:IResourceAttributeEditBase = serverattributes.getItemAt(i) as IResourceAttributeEditBase;
				params.addItem(o.getResourceAttribute());
			}
			
			addResourceObject(params);
		}
	}
}