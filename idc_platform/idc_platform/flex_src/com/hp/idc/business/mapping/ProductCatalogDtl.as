package com.hp.idc.business.mapping
{
	[RemoteClass(alias="com.hp.idc.business.entity.ProductCatalogDtl")]
	[Bindable]
	public class ProductCatalogDtl
	{
		
		public var dtlId:int;
		
		public var catalogId:int;
		
		public var productId:int;
		
		public var effectType:int;
		
		public var selectFlag:int = 0;
		
		public var minQuanity:int = 0;
		
		public var maxQuanity:int;
		
		public var defaultQuanity:int = 1;
		
		public var note:String;
		
		
		public function ProductCatalogDtl()
		{
		}
	}
}