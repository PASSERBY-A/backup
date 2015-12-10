package com.hp.idc.resm.resource
{

	public class AttributeFactory
	{
		//静态工厂模式，返回创建的attribute
		public static function factory(attribute:String):AttributeBase{
			if(attribute == "string"){
				return new StringAttribute();
			}else if(attribute == "boolean"){
				return new BooleanAttribute();
			}else if(attribute == "date"){
				return new DateAttribute();
			}else if(attribute == "time"){
				return new TimeAttribute();
			}else if(attribute == "datetime"){
				return new DateTimeAttribute();
			}else if(attribute == "int"){
				return new IntegerAttribute();
			}else if(attribute == "number"){
				return new NumberAttribute();
			}else if(attribute == "reference"){
				return new ReferenceAttribute();
			}else if(attribute == "table"){
				return new TableAttribute();
			}else if(attribute == "expression"){
				return new ExpressionAttribute();
			}else{
				return null;
			}
		}
	}
}