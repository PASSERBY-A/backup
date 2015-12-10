package com.hp.idc.components
{
	import flash.events.Event;
	
	import mx.core.UIComponent;
	import mx.managers.CursorManager;
	import mx.rpc.AsyncToken;
	import mx.rpc.Fault;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.mxml.RemoteObject;
	import mx.utils.ObjectUtil;
	
	public class HtRemoteObject extends RemoteObject
	{
		//指定默认的失败回调函数，成功的回调在创建远程调用时指定，不需要默认的，这样在我们使用时只要指定成功的回调就行了，失败的就用默认的，这样省去每次都要指定两个回调的麻烦。
		public var onResult:Function=null;
		
		public var onFault:Function = HtRemoteObject.onFaultDefault;
		// 用户自定义对象，在返回的事件中 e.token.ids 中可获取到，要扩展远程调用就是简单的事情了
		public var ids:Object=null;
		
		/**
		 * 构造函数
		 * @param destination    网关的终点
		 * @param source    远端函数的命名空间和类名，也就是远端函数所处的位置
		 * @param debug    是否连接调试服务器（调试模式）
		 * @param showBusyCursor    是否显示鼠标忙指针
		 *
		 */
		public function HtRemoteObject(destination:String=null, source:String=null, debug:Boolean=false, showBusyCursor:Boolean=false)
		{
			super(destination);
			super.source=source;
			super.showBusyCursor=showBusyCursor; //鼠标指针忙
		}
		
		/**
		 * 远程通讯，默认的失败处理函数
		 * @param e 事件
		 *
		 */
		static public function onFaultDefault(event:FaultEvent):void
		{
			CursorManager.removeBusyCursor();	
			PetAlert.show("远程服务器错误:" + ObjectUtil.toString(event),event.fault.faultString);
		}
		
		/**
		 * 远程调用 Server 中的函数 
		 * @foo 函数名
		 * @args 参数列表
		 */
		public function call(foo:String, ...args):void
		{
			if(foo.length==0)
				throw new Error("foo can not be empty!");
			
			//创建一个远程调用函数	
			var operation:mx.rpc.AbstractOperation=this.getOperation(foo);
			//Token
			var call:AsyncToken = operation.send.apply(null, args);
			
			// ids 中承载了处理函数和用户自定义ids，在 onDSResultParser onFaultParser 中解析和处理
			var idsWapper : Object = new Object;
			//使用自定的错误回调函数由onFaultParser调度，使用前要设定this.onFault
			idsWapper.onFaultCB = this.onFault;
			//使用自定的正确回调函数由onResultParser调度，使用时要设定this.onResult
			idsWapper.onResultCB = this.onResult;
			idsWapper.ids = this.ids;
			//如果要显示进程
//			idsWapper.progress = progress;
			call.ids = idsWapper;
			//设定回调处理器
			call.addResponder(new mx.rpc.Responder(HtRemoteObject.onResultParser,HtRemoteObject.onFaultParser));			
		}
		
		/**
		 * 处理服务器返回的数据，如果正确，则调用'数据返回回调'，错误则调用'错误处理回调'
		 * @param evt 成功事件
		 * 
		 */		
		static protected function onResultParser(e:ResultEvent):void
		{
			try
			{
				var idsWapper : Object = e.token.ids;
				
				// 删除进度条对象
				if (null != idsWapper.progress)
				{
					var progress : UIComponent = idsWapper.progress as UIComponent;
					idsWapper.progress = null;
					if (null != progress && null != progress.parent && progress.parent.contains(progress))
						progress.parent.removeChild(progress);
				}
				
				CursorManager.removeBusyCursor();
				var onResult : Function = idsWapper.onResultCB as Function;
				e.token.ids = idsWapper.ids;
				if (null != onResult)
					onResult (e);
			}
			catch(err:Error)
			{
				PetAlert.show("处理数据错误",err.toString());
			}
		}
		
		static protected function onFaultParser(e:FaultEvent):void
		{
			try
			{
				var idsWapper : Object = e.token.ids;
				
				// 删除进度条对象
				if (null != idsWapper.progress)
				{
					var progress : UIComponent = idsWapper.progress as UIComponent;
					idsWapper.progress = null;
					if (null != progress && null != progress.parent && progress.parent.contains(progress))
						progress.parent.removeChild(progress);
				}
				
				CursorManager.removeBusyCursor();
				
				var onFault : Function = idsWapper.onFaultCB as Function;
				e.token.ids = idsWapper.ids;
				if (null == onFault)
					onFault = HtRemoteObject.onFaultDefault;
				onFault(e);
			}
			catch(err:Error)
			{
				PetAlert.show("处理数据错误",err.toString());
			}
		}
		
	}
}