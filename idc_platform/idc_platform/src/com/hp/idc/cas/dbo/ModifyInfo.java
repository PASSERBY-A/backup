// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   ModifyInfo.java

package com.hp.idc.cas.dbo;


public class ModifyInfo
{

    protected String operName;
    protected String tableName;
    protected String columnName;
    protected String fromValue;
    protected String operType;
    protected String toValue;
    protected String content;
    private static final int MAX_SIZE = 1024;

    public ModifyInfo(String operName, String operType, String tableName, String columnName, String fromValue, String toValue, String content)
    {
        this.operName = operName;
        this.operType = operType;
        this.tableName = tableName;
        this.columnName = columnName;
        this.fromValue = fromValue;
        this.toValue = toValue;
        this.content = content;
    }

    public String getColumnName()
    {
        return columnName;
    }

    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }

    public String getFromValue()
    {
        if(fromValue != null && fromValue.length() > 1024)
            return fromValue.substring(0, 1024) + "...此处省略" + (fromValue.length() - 1024) + "字.";
        else
            return fromValue;
    }

    public void setFromValue(String fromValue)
    {
        this.fromValue = fromValue;
    }

    public String getOperName()
    {
        return operName;
    }

    public void setOperName(String operName)
    {
        this.operName = operName;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getToValue()
    {
        if(toValue != null && toValue.length() > 1024)
            return toValue.substring(0, 1024) + "...此处省略" + (toValue.length() - 1024) + "字.";
        else
            return toValue;
    }

    public void setToValue(String toValue)
    {
        this.toValue = toValue;
    }

    public String getOperType()
    {
        return operType;
    }

    public void setOperType(String operType)
    {
        this.operType = operType;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }
}
