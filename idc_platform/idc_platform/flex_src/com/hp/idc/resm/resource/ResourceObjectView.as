package com.hp.idc.resm.resource
{
	import com.hp.idc.components.HSplitLine;
	import com.hp.idc.components.HtRemoteObject;
	import com.hp.idc.components.ImageLink;
	import com.hp.idc.components.PetAlert;
	import com.hp.idc.components.RoundCornerPanel;
	import com.hp.idc.resm.resource.edit.DefaultGroup;
	import com.hp.idc.resm.resource.edit.IResourceAttributeEditGroup;
	import com.hp.idc.resm.resource.edit.ResourceAttributeEditBase;
	import com.hp.idc.ui.FaultHandler;
	import com.hp.idc.util.UiUtil;
	import com.hp.idc.util.UserTool;
	
	import flash.events.MouseEvent;
	import flash.utils.ByteArray;
	import flash.utils.getDefinitionByName;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.controls.Spacer;
	import mx.core.IVisualElement;
	import mx.graphics.GradientEntry;
	import mx.graphics.LinearGradient;
	import mx.managers.PopUpManager;
	import mx.rpc.AbstractOperation;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	
	import spark.components.Button;
	import spark.components.HGroup;
	import spark.components.Image;
	import spark.components.Label;
	import spark.components.NavigatorContent;
	import spark.components.Scroller;
	import spark.components.VGroup;
	import spark.layouts.VerticalLayout;
	import spark.primitives.Rect;

	public class ResourceObjectView extends NavigatorContent
	{
		
		[Embed(source="type.xml",mimeType="application/octet-stream")]
		public static const CONFIG_XML_FILE:Class;
		public static const CONFIG_XML:XML = initConst();
		
		[Embed(source="images/icon11.png")]
		private static var icon0:Class;
		
		private var content:VGroup;
		
		private var edit:Boolean = false;
		
		/**
		 *  资源对象id
		 */
		private var resourceId:int;
		
		/**
		 *	模型id
		 */
		private var modelId:String;
		
		/**
		 * 资源展现页面, 暂时没有按照type.xml设计的那样灵活展现
		 */
		private var resGroup:DefaultGroup;
		
		/**
		 * 构造资源展示对应表,即根据模型id查找资源展示页面
		 */
		private static function initConst():XML {
			var ba:ByteArray = new CONFIG_XML_FILE() as ByteArray;
			return new XML(ba.readUTFBytes(ba.length));
		}
		
		/**
		 * 获取资源展示页面的反射类型
		 */
		private static function getProperty( key:String ):String {
			return XML( CONFIG_XML.child( key )[0] ).text()[0];
		}
		
		/**
		 * (--动态创建参数没办法用...params,索性用array代替--)此设计实现已经放弃
		 * params[1]	模型id
		 * params[0]	资源id
		 */
		public function ResourceObjectView(params:ArrayCollection){
			super();
			this.edit = params.getItemAt(0) as Boolean;
			this.resourceId = params.getItemAt(1) as int;
			//this.modelId = params[1];
			initBackground();
			initContent();
			this.mouseEnabled =false;
			this.mouseChildren = false;
			initResourceData();
			this.mouseChildren = true;
			this.mouseEnabled =true;
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
		private function initToolBar(hGroup:HGroup):void{
			//常用工具栏
			var toolbar:RoundCornerPanel = new RoundCornerPanel();
			var layout:VerticalLayout = new VerticalLayout();
			layout.gap = 3;
			layout.paddingLeft = 2;
			layout.paddingRight = 2;
			layout.paddingTop = 5;
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
			titleLabel.setStyle("fontWeight","bold");
			titleHgroup.addElement(spacer);
			titleHgroup.addElement(titleImag);
			titleHgroup.addElement(titleLabel);
			
			var closeLink:ImageLink = new ImageLink();
			closeLink.leftSpaceWidth = 10;
			closeLink.image = "images/right.gif";
			closeLink.label = "关闭当前页面";
			closeLink.addEventListener(MouseEvent.CLICK,closeCurrentPage);
			
			var resLink:ImageLink = new ImageLink();
			resLink.leftSpaceWidth = 10;
			resLink.image = "images/icon20.gif";
			resLink.label = "编辑关联资源";
			resLink.addEventListener(MouseEvent.CLICK,relationWindown);
			
			var hline:HSplitLine = new HSplitLine();
			hline.percentWidth = 98;
			
			var hline1:HSplitLine = new HSplitLine();
			hline1.percentWidth = 98;
			
			var hline2:HSplitLine = new HSplitLine();
			hline2.percentWidth = 98;
			
			var spaceTop:Spacer = new Spacer();
			spaceTop.height = 5;
			var spaceBottom:Spacer = new Spacer();
			spaceBottom.height = 5;
			
			toolbar.addElement(spaceTop);
			toolbar.addElement(titleHgroup);
			toolbar.addElement(spaceBottom);
			toolbar.addElement(hline);
			toolbar.addElement(closeLink);
			toolbar.addElement(hline1);
			toolbar.addElement(resLink);
			toolbar.addElement(hline2);
			if(!this.edit){
				var editLink:ImageLink = new ImageLink();
				editLink.leftSpaceWidth = 10;
				editLink.image = "images/right.gif";
				editLink.label = "修改当前对象";
				editLink.addEventListener(MouseEvent.CLICK,editCurrentResourceObj)
				toolbar.addElement(editLink);
			}
			hGroup.addElement(toolbar);
		}
		
		
		/**
		 * 初始化内容容器、工具栏
		 */
		private function initContent():void{
			var hgroup:HGroup = new HGroup();
			hgroup.percentWidth = 100;
			hgroup.percentHeight = 100;
			hgroup.gap = 15;
			hgroup.paddingBottom = 15;
			hgroup.paddingLeft = 15;
			hgroup.paddingRight = 15;
			hgroup.paddingTop = 15;
			
			//工具栏
			initToolBar(hgroup);
			var scroller:Scroller = new Scroller();
			scroller.percentHeight = 100;
			scroller.percentWidth = 100;
			
			this.content = new VGroup();
			content.percentHeight = 100;
			content.percentWidth = 100;
			
			scroller.viewport = this.content;
			
			hgroup.addElement(scroller);
			
			this.addElement(hgroup);
		}
		
		/**
		 * 初始化资源数据，根据moudleId构造页面，如果没有对于的id则按默认页面展示
		 */
		private function initResourceData():void{
			var remoteService:HtRemoteObject = new HtRemoteObject("resourceService",null,false,true);
			remoteService.onFault = FaultHandler.defaultFaultHandler;
			remoteService.onResult = getResourceAttributes;
			remoteService.call("getResourceAttributes",resourceId);
		}
		
		private function getResourceAttributes(event:ResultEvent):void{
			var attributes:ArrayCollection = event.result as ArrayCollection;
//			var viewType:String = getProperty(this.modelId);
//			if(viewType==null){
//				viewType = getProperty("default");
//			}
//			var cla:Class = getDefinitionByName(viewType) as Class;
//			var instance:Object=new cla(attributes);
			
			resGroup = new DefaultGroup(attributes,true);
			this.content.addElement(resGroup.getContainer(this.edit));
			if(this.edit){
				this.addSaveBtton();
			}			
			
		}
		
		/**
		 * 关闭当前页面
		 */
		private function closeCurrentPage(event:MouseEvent):void{
			UiUtil.closeCurrent();
		}
		
		/**
		 * 修改当前资源对象
		 */
		private function editCurrentResourceObj(event:MouseEvent):void{
			var defaultAttributes:ArrayCollection = this.resGroup.getResourceAttributes();
			for(var i:int=0;i<defaultAttributes.length;i++){
				var o:ResourceAttributeEditBase = defaultAttributes.getItemAt(i) as ResourceAttributeEditBase;
				o.resourceAttribute.setEdit(true);
			}
			addSaveBtton();
		}
		
		private function addSaveBtton():void {
			//保存按钮
			var buttonGroup:HGroup = new HGroup();
			buttonGroup.percentWidth = 100;
			buttonGroup.paddingBottom = 10;
			buttonGroup.paddingRight = 10;
			buttonGroup.horizontalAlign = "right";
			
			var saveButton:Button = new Button();
			saveButton.label = "保存";
			saveButton.addEventListener(MouseEvent.CLICK,saveResourceObject);
			buttonGroup.addElement(saveButton);
			this.content.addElement(buttonGroup);		
		}
		
		private function saveResourceObject(event:MouseEvent):void{
			var params:ArrayCollection = new ArrayCollection();
			var defaultAttributes:ArrayCollection = this.resGroup.getResourceAttributes();
			for(var i:int=0;i<defaultAttributes.length;i++){
				var o:ResourceAttributeEditBase = defaultAttributes.getItemAt(i) as ResourceAttributeEditBase;
				params.addItem(o.getResourceAttribute());
			}
			//更新资源对象
			var remoteService:HtRemoteObject = new HtRemoteObject("resourceUpdateService",null,false,true);
			remoteService.onFault = FaultHandler.defaultFaultHandler;
			remoteService.onResult = function():void{PetAlert.show("资源对象更新成功！", "操作结果", 4, null, function():void{
			}, PetAlert.ICON_INFO);};
			remoteService.call("updateResource",this.resourceId,params,UserTool.userId);	
		}
		
		private function relationWindown(event:MouseEvent):void {
			var obj:RelationGrid = RelationGrid(PopUpManager.createPopUp(content, RelationGrid, true));
			obj.resourceId = this.resourceId;
			PopUpManager.centerPopUp(obj);	
		}
	}
}