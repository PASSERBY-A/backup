package com.hp.idc.resm.resource
{
	import com.hp.idc.resm.security.Organization;
	import com.hp.idc.resm.security.Person;
	
	import mx.collections.ArrayCollection;
	import mx.core.INavigatorContent;
	
	import spark.components.gridClasses.GridColumn;

	[Bindable]
	[RemoteClass(alias="com.hp.idc.resm.resource.ResourceObject")]
	public class ResourceObject
	{
		public var id:int;
		public var modelId:String;
		public var enabled:Boolean;
		public var data:String;
		public var header:ArrayCollection;
		public var simpleDescription:String;
		public var name:String;

		[Transient]
		private var xml:XML = null;
		
		public function getAttribute(attrId:String) :String
		{
			if (xml == null)
				xml = new XML(data);
			var list:XMLList = xml.child("*").child("field");
			trace(list.length());
			for each (var x:XML in list)
			{
				if (x.@id == attrId)
					return x.@value;
			}
			return "";
		}
		
		public static function getLabelFunction(id:String):Function{
			var fun:Function = function displayName(oItem:Object, iCol:GridColumn):String
			{
				var res:ResourceObject = oItem as ResourceObject;
				for each (var xx:AttributeBase in res.header)
				{
					if (xx.getAttribute().attrId == id){
						if(id == "id" && !res.enabled){
							return (xx as Object).text+"(已删除)";
						}
						return (xx as Object).text;
					}
				}
				return "";
			}
			return fun;
		}
		
		
		private function DO_NOT_CALL() : void
		{
			// 把类编辑进去
			var a0 :Person;
			var a1 :Organization;
			var ab :AttributeBase;
			var ab1 :BooleanAttribute;
			var ab2:DateAttribute;
			var ab3:DateTimeAttribute;
			var ab4:ExpressionAttribute;
			var ab5:IntegerAttribute;
			var ab6:NumberAttribute;
			var ab7:ReferenceAttribute;
			var ab8:StringAttribute;
			var ab9:TableAttribute;
			var ab10:TimeAttribute;
		}
		
	}
}