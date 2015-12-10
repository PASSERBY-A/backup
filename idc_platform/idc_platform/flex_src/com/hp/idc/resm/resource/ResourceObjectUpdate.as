package com.hp.idc.resm.resource
{
	import com.hp.idc.components.HSplitLine;
	import com.hp.idc.components.HtRemoteObject;
	import com.hp.idc.components.ImageLink;
	import com.hp.idc.components.PetAlert;
	import com.hp.idc.components.RoundCornerPanel;
	import com.hp.idc.components.TreeItem;
	import com.hp.idc.components.TreeItemCollection;
	import com.hp.idc.components.widgets.ProgressPanel;
	import com.hp.idc.resm.resource.edit.DefaultGroup;
	import com.hp.idc.resm.resource.edit.IResourceAttributeEdit;
	import com.hp.idc.resm.resource.edit.IResourceAttributeEditBase;
	import com.hp.idc.ui.FaultHandler;
	import com.hp.idc.util.UiUtil;
	import com.hp.idc.util.UserTool;
	import com.hp.idc.util.json.JSON;
	
	import flash.events.DataEvent;
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.controls.Spacer;
	import mx.controls.Tree;
	import mx.core.ClassFactory;
	import mx.events.IndexChangedEvent;
	import mx.graphics.GradientEntry;
	import mx.graphics.LinearGradient;
	import mx.managers.CursorManager;
	import mx.managers.PopUpManager;
	import mx.rpc.AbstractOperation;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import spark.components.Button;
	import spark.components.HGroup;
	import spark.components.Image;
	import spark.components.Label;
	import spark.components.List;
	import spark.components.NavigatorContent;
	import spark.components.Scroller;
	import spark.components.VGroup;
	import spark.components.supportClasses.TextBase;
	import spark.events.IndexChangeEvent;
	import spark.events.ListEvent;
	import spark.layouts.VerticalLayout;
	import spark.primitives.Rect;
	
	public class ResourceObjectUpdate extends NavigatorContent
	{
		private var editTag:Boolean  = true;
		
		private var tree:List;
		private var content:VGroup;
		[Bindable]
		private var selectModelId:String;
		
		private var defaultGroup:DefaultGroup;
		
		private var _dynElement:Array = [null, null, new Object()];
		
		[Embed(source="images/icon11.png")]
		private static var icon0:Class;
			
		
		public function ResourceObjectUpdate(params:ArrayCollection)
		{
			super();
			this.editTag = params.getItemAt(0) as Boolean;
			createComponents();
			
			initTreeDate();
		}
		
		private function createComponents():void{
			
			//初始化背景
			initBackground();
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
			
			initToolBar(treeGroup);
			initTree(treeGroup);
			
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
			content.paddingTop = 20;
			
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
			this.addElement(group);
			_dynElement[0] = saveButton;
		}
		
		/**
		 * 增加背景
		 */
		private function initBackground():void{
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
			this.addElement(rect);
		}
		
		
		/**
		 * 初始化工具栏
		 */
		private function initToolBar(treeGroup:VGroup):void{
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
			titleImag.source = icon0;
			var titleLabel:Label = new Label();
			titleLabel.text = "常用功能";
			titleLabel.setStyle("fontSize",14);
			titleHgroup.addElement(spacer);
			titleHgroup.addElement(titleImag);
			titleHgroup.addElement(titleLabel);
			
			var closeLink:ImageLink = new ImageLink();
			closeLink.leftSpaceWidth = 10;
			closeLink.image = "images/right.gif";
			closeLink.label = "关闭当前页面";
			closeLink.addEventListener(MouseEvent.CLICK,closeCurrentPage);
			
			var copyAddLink:ImageLink = new ImageLink();
			copyAddLink.leftSpaceWidth = 10;
			copyAddLink.image = "images/right.gif";
			copyAddLink.label = "复制一个资源";
			copyAddLink.addEventListener(MouseEvent.CLICK,copyAddWindown);
				
			var resLink:ImageLink = new ImageLink();
			resLink.leftSpaceWidth = 10;
			resLink.image = "images/icon20.gif";
			resLink.label = "编辑关联资源";
			resLink.enabled = false;
			resLink.addEventListener(MouseEvent.CLICK,relationWindown);
			
			var hline:HSplitLine = new HSplitLine();
			hline.percentWidth = 98;
			
			var hline1:HSplitLine = new HSplitLine();
			hline1.percentWidth = 98;
			
			var hline2:HSplitLine = new HSplitLine();
			hline2.percentWidth = 98;
			
			var hline3:HSplitLine = new HSplitLine();
			hline3.percentWidth = 98;
			
			toolbar.addElement(titleHgroup);
			toolbar.addElement(hline);
			toolbar.addElement(closeLink);
			toolbar.addElement(hline1);
			toolbar.addElement(copyAddLink);
			toolbar.addElement(hline2);
			toolbar.addElement(resLink);
			toolbar.addElement(hline3);
			
			treeGroup.addElement(toolbar);
			_dynElement[1] = resLink;
		}
		
		/**
		 * 初始化tree的模型选择
		 */
		private function initTree(treeGroup:VGroup):void{
			//tree标题
			var treetitleHgroup:HGroup = new HGroup();
			var treespacer:Spacer = new Spacer();
			treespacer.width = 15;
			var treetitleImag:Image = new Image();
			treetitleImag.source = icon0;
			var treetitleLabel:Label = new Label();
			treetitleLabel.text = "模型";
			treetitleLabel.setStyle("fontSize",14);
			treetitleHgroup.addElement(treespacer);
			treetitleHgroup.addElement(treetitleImag);
			treetitleHgroup.addElement(treetitleLabel);
			
			
			var subTreeGroup:RoundCornerPanel = new RoundCornerPanel();
			var subTreeGroupLayout:VerticalLayout = new VerticalLayout();
			subTreeGroupLayout.paddingLeft = 10;
			subTreeGroupLayout.paddingTop = 10;
			subTreeGroupLayout.paddingRight = 10;
			subTreeGroupLayout.paddingBottom = 10;
			subTreeGroup.layout = subTreeGroupLayout;
			subTreeGroup.percentWidth = 100;
			subTreeGroup.percentHeight = 100;
			
			this.tree = new List();
			tree.itemRenderer = new ClassFactory(com.hp.idc.components.TreeItemRenderer);
			tree.setStyle("borderVisible",false);
			tree.labelField = "@label";
			tree.percentHeight = 100;
			tree.percentWidth = 100;
			
			var treehline:HSplitLine = new HSplitLine();
			treehline.percentWidth = 98;
			
			subTreeGroup.addElement(treetitleHgroup);
			subTreeGroup.addElement(treehline);
			subTreeGroup.addElement(tree);
			treeGroup.addElement(subTreeGroup);
		}
		
		
		
		private function tree_changeHandler(event:IndexChangeEvent):void
		{
			
			if(tree.selectedItem==null||(tree.selectedItem.childs!=null&&tree.selectedItem.childs.length!=0)){
				return;
			}
			
			UiUtil.showProgress();
			
			(_dynElement[0] as Button).enabled = true;
			(_dynElement[1] as ImageLink).enabled = true;
			_dynElement[2] = new Object();
			
			var id:String = tree.selectedItem.id;
			this.selectModelId = id;
			getModeleAttribute(id);
		}
		//添加资源对象
		private function addResourceObject(params:ArrayCollection):void{
			var remoteOperation:HtRemoteObject = new HtRemoteObject("resourceUpdateService", null, false, true);   
			remoteOperation.onFault = FaultHandler.defaultFaultHandler;
			remoteOperation.onResult = function(event:ResultEvent):void{PetAlert.show("资源对象添加成功！", "操作结果", 4, null, function():void{
				(_dynElement[0] as Button).enabled = true;
				(_dynElement[1] as ImageLink).enabled = true;
				_dynElement[2] = event.result;
				
			}, PetAlert.ICON_INFO);};
			
			remoteOperation.call("addResource",this.selectModelId,params,UserTool.userId);
		}
		
		//调用后台，初始化tree
		private function initTreeDate():void{
			var remoteOperation:RemoteObject = new RemoteObject();   
			remoteOperation.destination = "modelService";   
			
			var op0:AbstractOperation = remoteOperation.getOperation( "getModelTree" );    
			
			op0.addEventListener(ResultEvent.RESULT,resultTreeHandler);      
			op0.addEventListener(FaultEvent.FAULT,FaultHandler.defaultFaultHandler);  
			remoteOperation.getModelTree();
		}
		
		//模型初始化添加资源对象界面
		private function getModeleAttribute(modId:String):void{
			var remoteOperation:RemoteObject = new RemoteObject();   
			remoteOperation.destination = "modelService"; 
			
			var op0:AbstractOperation = remoteOperation.getOperation( "getModelAttributesByModelId" );  
			op0.addEventListener(ResultEvent.RESULT,initContent);      
			op0.addEventListener(FaultEvent.FAULT,FaultHandler.defaultFaultHandler);  
			remoteOperation.getModelAttributesByModelId(modId);
		}
		
		private function resultTreeHandler(event:ResultEvent):void
		{
			var returnString :String = event.result as String;	
			var treeData : XML = new XML(returnString);   
			
			tree.dataProvider = TreeItem.createDataProviderFromXML(treeData); 
			tree.addEventListener(IndexChangeEvent.CHANGE,tree_changeHandler)
				
		}
		
		private function initContent(event:ResultEvent):void{
			content.removeAllElements();
			
			var tHgroup:HGroup = new HGroup();
			var tspacer:Spacer = new Spacer();
			tspacer.width = 15;
			var tImag:Image = new Image();
			tImag.source = icon0;
			var tLabel:Label = new Label();
			tLabel.text = "新增资源对象 - [ " + tree.selectedItem.label + " ]";
			tLabel.setStyle("fontSize",15);
			tLabel.setStyle("fontWeight","bold");
			tHgroup.addElement(tspacer);
			tHgroup.addElement(tImag);
			tHgroup.addElement(tLabel);
			
			content.addElement(tHgroup);
			
			if((tree.selectedItem as TreeItem).enable == "false"){
				var disableText:Label = new Label();
				disableText.setStyle("fontSize",20);
				disableText.setStyle("fontWeight","bold");
				disableText.text = "此模型已经被禁用!";
				var dgroup:HGroup = new HGroup();
				dgroup.paddingLeft = 20;
				dgroup.paddingTop = 20;
				dgroup.addElement(disableText);
				content.addElement(dgroup);
				UiUtil.hideProgress();
			} else {
				var list:ArrayCollection = event.result as ArrayCollection;
				defaultGroup = new DefaultGroup(list,this.editTag);
				content.addElement(defaultGroup.getContainer());
				UiUtil.hideProgress();
			}
		}
		
		private function closeCurrentPage(event:MouseEvent):void{
			UiUtil.closeCurrent();
		}
		
		private function saveResourceObject(event:MouseEvent):void{
			var params:ArrayCollection = new ArrayCollection();
			var defaultAttributes:ArrayCollection = this.defaultGroup.getResourceAttributes();
			for(var i:int=0;i<defaultAttributes.length;i++){
				var o:IResourceAttributeEditBase = defaultAttributes.getItemAt(i) as IResourceAttributeEditBase;
				params.addItem(o.getResourceAttribute());
			}
			
			addResourceObject(params);
		}
		
		private function relationWindown(event:MouseEvent):void {
			var obj:RelationGrid = RelationGrid(PopUpManager.createPopUp(content, RelationGrid, true));
			obj.resourceId = int(_dynElement[2].toString());
			PopUpManager.centerPopUp(obj);	
		}
		
		private function copyAddWindown(event:MouseEvent):void {
			var obj:ResList = ResList(PopUpManager.createPopUp(content, ResList, true));
			obj.p = this;
			PopUpManager.centerPopUp(obj);
		}
		
		public function initResourceData(resource:ResourceObject):void{
			(_dynElement[0] as Button).enabled = true;
			(_dynElement[1] as ImageLink).enabled = true;
			_dynElement[2] = new Object();
			
			this.selectModelId = resource.modelId;
			var remoteService:HtRemoteObject = new HtRemoteObject("resourceService",null,false,true);
			remoteService.onFault = FaultHandler.defaultFaultHandler;
			remoteService.onResult = getResourceAttributes;
			remoteService.call("getResourceAttributes",resource.id);
		}
		
		private function getResourceAttributes(event:ResultEvent):void{
			content.removeAllElements();
			var attributes:ArrayCollection = event.result as ArrayCollection;
			defaultGroup = new DefaultGroup(attributes,true);
			content.addElement(defaultGroup.getContainer());
		}
	}
}