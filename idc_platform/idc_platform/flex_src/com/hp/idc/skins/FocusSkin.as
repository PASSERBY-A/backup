////////////////////////////////////////////////////////////////////////////////
//
//  ADOBE SYSTEMS INCORPORATED
//  Copyright 2008 Adobe Systems Incorporated
//  All Rights Reserved.
//
//  NOTICE: Adobe permits you to use, modify, and distribute this file
//  in accordance with the terms of the license agreement accompanying it.
//
////////////////////////////////////////////////////////////////////////////////

package com.hp.idc.skins
{

import flash.display.Bitmap;
import flash.display.BitmapData;
import flash.display.DisplayObject;
import flash.display.Graphics;
import flash.display.IBitmapDrawable;
import flash.events.Event;
import flash.filters.GlowFilter;
import flash.geom.ColorTransform;
import flash.geom.Matrix;
import flash.geom.Matrix3D;
import flash.geom.Point;
import flash.geom.Rectangle;

import mx.core.UIComponent;
import mx.core.mx_internal;
import mx.events.FlexEvent;

import spark.components.supportClasses.Skin;
import spark.components.supportClasses.SkinnableComponent;
import spark.core.DisplayObjectSharingMode;
import spark.core.IGraphicElement;
import spark.skins.SparkSkin;
import spark.skins.spark.HighlightBitmapCaptureSkin;

use namespace mx_internal;

/**
 *  Defines the "glow" around Spark components when the component has focus.
 *  
 *  @langversion 3.0
 *  @playerversion Flash 10
 *  @playerversion AIR 1.5
 *  @productversion Flex 4
 */
public class FocusSkin extends HighlightBitmapCaptureSkin
{
    private static var rect:Rectangle = new Rectangle();;
    private static var filterPt:Point = new Point();
                 
    //--------------------------------------------------------------------------
    //
    //  Constructor
    //
    //--------------------------------------------------------------------------
    
    /**
     * Constructor.
     */
    public function FocusSkin()
    {
        super();
    }

    //--------------------------------------------------------------------------
    //
    //  Variables
    //
    //--------------------------------------------------------------------------
    
    /**
     *  @inheritDoc
     */
    override protected function get borderWeight() : Number
    {
        if (target)
            return target.getStyle("focusThickness");
        
        // No target, return default value
        return getStyle("focusThickness");
    }
    
    //--------------------------------------------------------------------------
    //
    //  Overridden methods
    //
    //--------------------------------------------------------------------------
    
    /**
     * @inheritDoc
     */
    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
    {   
        super.updateDisplayList(unscaledWidth, unscaledHeight);
         
        if (target)
            blendMode = target.getStyle("focusBlendMode");
    }
    
    /**
     *  @private
     */
    override protected function processBitmap() : void
    {
        rect.x = rect.y = 1;
        rect.width = bitmap.width - 2;
        rect.height = bitmap.height - 2;
		
	
        // If the focusObject has an errorString, use "errorColor" instead of "focusColor" 
        if (target.errorString != null && target.errorString != "") 
        {
			bitmap.bitmapData.fillRect(rect, 0x80000000 | target.getStyle("errorColor"));
        }
        else
        {
			bitmap.bitmapData.fillRect(rect, 0x80000000 | target.getStyle("focusColor"));
        }
    }
}
}        
