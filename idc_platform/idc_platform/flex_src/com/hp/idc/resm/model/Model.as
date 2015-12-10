// ActionScript file
package com.hp.idc.resm.model
{
	import com.hp.idc.resm.resource.ReferenceAttribute;
	
	import mx.collections.ArrayCollection;

	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.model.Model")]
	public class Model
	{
	
		/**
		 * 模型ID
		 */
		public var id :String;
		
		/**
		 * 父模型ID
		 */
		public var parentId:String;
		
		/**
		 * 模型名称
		 */
		public var name:String;
		
		/**
		 * 标明只是一个目录
		 */
		public var directoryOnly :Boolean;
		
		/**
		 * 图标16*16
		 */
		public var icon :int;
		
		/**
		 * 大图标32*32
		 */
		public var largeIcon:int;
		
		/**
		 * 使用状态
		 */
		public var enabled :Boolean;
		
		/**
		 * 维度模型
		 */
		public var dimModel :Boolean;
		
		/**
		 * 资源展示时的表头信息，序列化时使用
		 */
		public var header:ArrayCollection;
		
	}
}
