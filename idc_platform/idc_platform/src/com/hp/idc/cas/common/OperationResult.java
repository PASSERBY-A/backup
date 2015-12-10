// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   OperationResult.java

package com.hp.idc.cas.common;


public class OperationResult
{

    protected boolean success;
    protected String message;

    public OperationResult()
    {
        success = true;
        message = "您的请求已处理成功";
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
    	if(message.startsWith("java.lang"))
    		message = message.substring(message.indexOf(":")+1).trim();  	
        this.message = message;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }
}
