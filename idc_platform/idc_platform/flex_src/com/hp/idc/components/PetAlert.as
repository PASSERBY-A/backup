package com.hp.idc.components{
	import flash.display.Bitmap;
	import flash.display.Sprite;
	
	import flashx.textLayout.debug.assert;
	
	import mx.controls.Alert;
	import spark.components.Image;
	import mx.events.FlexEvent;
	import mx.events.MoveEvent;
	import mx.managers.PopUpManager;
	import mx.styles.CSSStyleDeclaration;

	[Style(name="pet", type="Class", inherit="no")]
	[Style(name="petPosition", type="String", enumeration="left, right", inherit="no")]
	[Style(name="borderColor", type="uint", format="Color", inherit="no")]
	[Style(name="backgroundColor", type="uint", format="Color", inherit="no")]
	[Style(name="borderThickness", type="Number", format="Length", inherit="no")]
	[Style(name="legSize", type="Number", format="Length", inherit="no")]
	[Style(name="legPointY", type="Number", format="Length", inherit="no")]
	[Style(name="roundHeight", type="Number", format="Length", inherit="no")]
	public class PetAlert extends Alert {
		
		public static const YES:uint = 0x0001;
	    
	    public static const NO:uint = 0x0002;
	    public static const OK:uint = 0x0004;
	    public static const CANCEL:uint= 0x0008;
	    public static const NONMODAL:uint = 0x8000;	
	    private static const HEIGHT_DIFFERENCE:Number = 10;	
	    private static const defaultStyle:CSSStyleDeclaration = createDefaultStyle();

//		[Embed(source="assets/lr2.png")]
		[Embed(source="assets/PetAlert.png")]
	    private static const defaultPet:Class;
		
		[Embed(source="assets/Alert_Error.png")]
		public static const ICON_ERROR : Class;
	    
		[Embed(source="assets/Alert_Info.png")]
		public static const ICON_INFO : Class;

		[Embed(source="assets/Alert_Stop.png")]
		public static const ICON_STOP : Class;

		[Embed(source="assets/Alert_Tip.png")]
		public static const ICON_TIP : Class;

		[Embed(source="assets/Alert_Warning.png")]
		public static const ICON_WARNING : Class;

		[Embed(source="assets/Alert_Alarm.png" )]
		public static const ICON_ALARM : Class;

		private static function createDefaultStyle():CSSStyleDeclaration {
 			var style:CSSStyleDeclaration = new CSSStyleDeclaration();
            style.defaultFactory = function():void {
                this.pet = defaultPet;
                this.petPosition = "left";
                this.borderColor = 0x1F1F1F;
                this.backgroundColor = 0xFFFFFF;
                this.borderThickness = 1;
                this.legSize = 5;
                this.legPointY = 60;
                this.roundHeight = 20;
            }
            return style;	    	
	    }
	    
		public static function getStyle(alert:Alert, styleProp:String):* {
			var result:* = alert.getStyle(styleProp);
			if(!result) {
				result = defaultStyle.getStyle(styleProp);
			}
			return result;
		}
		
		public static function showThisPetAt(styleName:String, x:Number, y:Number, text:String = "", title:String = "",
                                flags:uint = 0x4 /* PetAlert.OK */, 
                                parent:Sprite = null, 
                                closeHandler:Function = null, 
                                iconClass:Class = null, 
                                defaultButtonFlag:uint = 0x4 /* PetAlert.OK */):Alert {
			var alert:Alert = showThisPet(styleName, text, title, flags, parent, closeHandler, iconClass, defaultButtonFlag);
			alert.callLater(function():void {
				alert.move(x, y);
			});
			return alert;                               	
		}
		
		public static function showAt(x:Number, y:Number, text:String = "", title:String = "",
                                flags:uint = 0x4 /* PetAlert.OK */, 
                                parent:Sprite = null, 
                                closeHandler:Function = null, 
                                iconClass:Class = null, 
                                defaultButtonFlag:uint = 0x4 /* PetAlert.OK */):Alert {
			var alert:Alert = showThisPet("PetAlert", text, title, flags, parent, closeHandler, iconClass, defaultButtonFlag);
			alert.callLater(function():void {
				alert.move(x, y);
			});
			return alert;
		}
		
		public static function showThisPet(styleName:String, text:String = "", title:String = "",
                                flags:uint = 0x4 /* PetAlert.OK */, 
                                parent:Sprite = null, 
                                closeHandler:Function = null, 
                                iconClass:Class = null, 
                                defaultButtonFlag:uint = 0x4 /* PetAlert.OK */):Alert {
			var alert:Alert = Alert.show(text, title, flags, parent, closeHandler, iconClass, defaultButtonFlag);
			alert.styleName = styleName;
			//alert.defaultButton
			alert.height += HEIGHT_DIFFERENCE;
			var legSize:Number = getStyle(alert,"legSize");
			var legPointY:Number = getStyle(alert,"legPointY");
			var petPosition:String = getStyle(alert,"petPosition");
			alert.setStyle("legPoint", petPosition == "right" ? ["L"+(alert.width + legSize), legPointY]  : ["L"+(legSize * -2), legPointY]);
			alert.setStyle("paddingBottom", 10); 
			var obj:Object = getStyle(alert,"pet");
			var bmp:Bitmap = new obj; 
			if(alert.width + bmp.width + legSize *2 > alert.stage.width){
				alert.width = alert.stage.width - bmp.width - legSize * 2;
			}
			alert.callLater(function():void {
				alert.x = petPosition == "right" ? alert.x - bmp.width/2 : alert.x + bmp.width/2;
				PopUpManager.addPopUp(image, alert.parent);
			});
			var image:Image = new Image;
			image.source = bmp;
			image.width = bmp.width;
			image.height = bmp.height;
			alert.data = image;
			alert.addEventListener(MoveEvent.MOVE, handleAlertMove);
			alert.addEventListener(FlexEvent.REMOVE, handleAlertRemove);
			image.x = petPosition == "right" ? alert.x+alert.width : alert.x-bmp.width;
			image.x += petPosition == "right" ? legSize *2 : legSize * -2;
			image.y = alert.y;
			return alert;
		}
		
		private static function handleAlertRemove(event:FlexEvent):void {
			PopUpManager.removePopUp(event.target.data);
			event.target.removeEventListener(MoveEvent.MOVE, handleAlertMove);
			event.target..removeEventListener(FlexEvent.REMOVE, handleAlertRemove);
		}
		
		private static function handleAlertMove(event:MoveEvent):void {
			var legSize:Number = getStyle(Alert(event.target),"legSize");
			var petPosition:String = getStyle(Alert(event.target),"petPosition");
			event.target.data.x = petPosition == "right" ? event.target.x+event.target.width : event.target.x-event.target.data.width;
			event.target.data.x += petPosition == "right" ? legSize *2 : legSize * -2;
			event.target.data.y = event.target.y;
		}
		
		public static function show(text:String = "", title:String = "",
                                flags:uint = 0x4 /* PetAlert.OK */, 
                                parent:Sprite = null, 
                                closeHandler:Function = null, 
								iconClass:Class = null, 
                                defaultButtonFlag:uint = 0x4 /* PetAlert.OK */):Alert {
			if (iconClass != null) {
				var img :Image = new Image;
				img.source = iconClass;
				img.width = 32;
			
			}
			return showThisPet("PetAlert", text, title, flags, parent, closeHandler, iconClass, defaultButtonFlag);	
		} 
				
	}
}


