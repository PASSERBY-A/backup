package com.hp.idc.components
{
	import flash.events.Event;
	
	import mx.controls.Image;
	import mx.controls.Spacer;
	import mx.core.IButton;
	
	import spark.components.Button;
	import spark.components.supportClasses.TextBase;

	public class ImageLink extends Button
	{
			[SkinPart(required="false")]
			public var imageDisplay: Image;

			[SkinPart(required="false")]
			public var leftSpacer: Spacer;
			
			[SkinPart(required="false")]
			public var centerSpacer: Spacer;
			
			private var _image:Object = null;
			private var _imageSize:Number = 0;
			private var _labelWidth:Number = 0;
			private var _leftSpaceWidth:Number = 0;
			private var _centerSpaceWidth:Number = 0;
			
			public function ImageLink()
			{
				super();
				// Sprite variables.
				buttonMode = true; // enables the hand cursor
				
			}
			
			override public function set content(value:Object):void
			{
				super.content = value;
				if (labelDisplay) {
//					if (value == null || value == "")
//						labelDisplay.visible = labelDisplay.includeInLayout = false;
//					else
//					labelDisplay.visible = labelDisplay.includeInLayout = true;
				}
			}
			
			override public function set enabled(value:Boolean):void
			{
				super.enabled = value;
				buttonMode = value;
			}
			
			public function set image(value:Object):void
			{
				_image = value;
				if (imageDisplay)
					imageDisplay.source = _image;
			}
			
			public function set imageSize(value:Number):void
			{
				_imageSize = value;
				if (imageDisplay) {
					imageDisplay.width = _imageSize;
					imageDisplay.height = _imageSize;
				}
			}
			
			public function set leftSpaceWidth(value:Number):void
			{
				_leftSpaceWidth = value;
				if (leftSpacer) {
					leftSpacer.width = _leftSpaceWidth;
				}
			}
			
			public function set centerSpaceWidth(value:Number):void
			{
				_centerSpaceWidth = value;
				if (centerSpacer) {
					centerSpacer.width = _centerSpaceWidth;
				}
			}

			public function set labelWidth(value:Number):void
			{
				_labelWidth = value;
				if (labelDisplay) {
//					labelDisplay.width = _labelWidth;
				}
			}

			override protected function partAdded(partName:String, instance:Object):void
			{
				super.partAdded(partName, instance);
				if (instance == imageDisplay) {
					if (_image != null) {
						imageDisplay.source = _image;
						if (_imageSize > 0) {
							imageDisplay.width = _imageSize;
							imageDisplay.height = _imageSize;
						}
					}
				}
				else if (instance == labelDisplay) {
						if (_labelWidth > 0) {
//							labelDisplay.width = _labelWidth;
						}
				}
				
				else if (instance == leftSpacer) {
					if (_leftSpaceWidth > 0) {
						leftSpacer.width = _leftSpaceWidth;
					}
				}
				else if (instance == centerSpacer) {
					if (_centerSpaceWidth > 0) {
						centerSpacer.width = _centerSpaceWidth;
					}
				}
			}
	}
}