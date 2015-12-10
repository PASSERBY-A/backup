package com.hp.idc.util
{
	import com.hp.idc.components.Associate_im_navigatorContent;
	import com.hp.idc.components.BaseNavigatorContent;
	import com.hp.idc.components.HistoryInfoNavigatorContent;
	import com.hp.idc.components.PetAlert;
	import com.hp.idc.components.TopoNavigatorContent;
	import com.hp.idc.components.rmList;
	import com.hp.idc.components.widgets.ProgressPanel;
	import com.hp.idc.resm.resource.ResourceObjectUpdate;
	import com.hp.idc.resm.resource.ResourceObjectView;
	import com.hp.idc.resm.ui.admin.AdminNavigate;
	import com.hp.idc.resm.ui.data.DataNavigate;
	import com.hp.idc.resm.ui.tasklink.TaskLinkView;
	
	import flash.display.DisplayObject;
	import flash.display.Stage;
	import flash.utils.ByteArray;
	import flash.utils.Dictionary;
	import flash.utils.getDefinitionByName;
	
	import mx.collections.ArrayCollection;
	import mx.containers.ViewStack;
	import mx.controls.Alert;
	import mx.core.INavigatorContent;
	import mx.managers.CursorManager;
	
	import spark.components.Application;
	import spark.components.NavigatorContent;

	public class UiUtil
	{
		//动态创建对象引用
		public static var rmlist:rmList;
		public static var baseinfo:BaseNavigatorContent;
		public static var datainfo:DataNavigate;
		public static var s:Associate_im_navigatorContent;
		public static var history:HistoryInfoNavigatorContent;
		public static var topo:TopoNavigatorContent;
		public static var resourceUpdate:ResourceObjectUpdate;
		public static var resourceView:ResourceObjectView;
		public static var taskLink:TaskLinkView;
		
		public static var vs :ViewStack;
		public static var progress :ProgressPanel;
		public static var application:Application;
		
		[Embed(source="link_config.xml",mimeType="application/octet-stream")]
		public static const CONFIG_XML_FILE:Class;
		public static const CONFIG_XML:XML = initConst();
		
		private static function initConst():XML {
			var ba:ByteArray = new CONFIG_XML_FILE() as ByteArray;
			return new XML(ba.readUTFBytes(ba.length));
		}
		
		private static function getProperty( key:String ):String {
			return XML( CONFIG_XML.child( key )[0] ).text()[0];
		}
		
		
		public static function showProgress() : void {
			application.mouseChildren = false;
			application.invalidateProperties();
			progress.visible = true;
			//application.cursorManager.setBusyCursor();
			//CursorManager.hideCursor();
		}
		
		public static function hideProgress() : void {
			progress.visible = false;
			application.mouseChildren = true;
			//CursorManager.showCursor();
		}

		/**
		 * 添加至当前vs中
		 */
		public static function addToVs(name:String,obj:DisplayObject):void{
			if(vs.getChildByName(name)!=null){
				return;
			}else{
				vs.addChild(obj);
			}
		}
		
		
		/**
		 * 根据name切换显示对象，具体的现实对象在link_config.xml中配置，格式为
		 * <config>
	     *		<name>对象类名全路径</name>
		 * </config>
		 * 要添加新的现实对象，创建NavigatorContent的子类（目前没有支持带参数的构造对象）
		 * 在UiUtil.as文件中添加类型的引用以便flex可以将新的类型编译进来
		 */
		public static function changUi(name:String):void{
			if(vs.getChildByName(name)==null){
				//根据配置文件创建显示对象
				if(getProperty(name)==null){
					Alert.show("类型创建错误，请在link_config.xml中配置相关对象！！！");
				}else{
					//动态创建NavigatorContent对象并添加至vs中
					var cla:Class=getDefinitionByName(getProperty(name)) as Class;
					var instance:Object=new cla();
					instance.name = name;
					
					addToVs(name,instance as DisplayObject);
					//设置为当前显示对象
					vs.selectedChild = instance as INavigatorContent;
				}
			}else{
				vs.selectedChild = vs.getChildByName(name) as INavigatorContent;
			}
		}
		
		public static function changUiObject(name:String, params:Object):void{
			if(vs.getChildByName(name)==null){
				//根据配置文件创建显示对象
				if(getProperty(name)==null){
					Alert.show("类型创建错误，请在link_config.xml中配置相关对象！！！");
				}else{
					//动态创建NavigatorContent对象并添加至vs中
					var cla:Class=getDefinitionByName(getProperty(name)) as Class;
					var instance:Object=new cla();
					instance.name = name;
					instance.params = params;
					addToVs(name,instance as DisplayObject);
					//设置为当前显示对象
					vs.selectedChild = instance as INavigatorContent;
				}
			}else{
				vs.selectedChild = vs.getChildByName(name) as INavigatorContent;
			}
		}
		
		public static function changUiParam(name:String,params:ArrayCollection):void{
			if(vs.getChildByName(name)==null){
				//根据配置文件创建显示对象
				if(getProperty(name)==null){
					Alert.show("类型创建错误，请在link_config.xml中配置相关对象！！！");
				}else{
					//动态创建NavigatorContent对象并添加至vs中
					var cla:Class = getDefinitionByName(getProperty(name)) as Class;
					var instance:Object=new cla(params);
					instance.name = name;
					addToVs(name,instance as DisplayObject);
					//设置为当前显示对象
					vs.selectedChild = instance as INavigatorContent;
				}
			}else{
				vs.selectedChild = vs.getChildByName(name) as INavigatorContent;
			}
		}
		
		
		
		/**
		 * 关闭当前页面
		 */
		public static function closeCurrent():void{
			var s:DisplayObject = vs.selectedChild as DisplayObject;
			vs.removeChild(s);
		}
	}
}