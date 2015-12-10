// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   LogInfo.java

package com.hp.idc.cas.dbo;

import java.util.ArrayList;
import java.util.List;

public class LogInfo
{

    protected String tableName;
    protected String desc;
    protected int oid;
    protected String keyColumn;
    protected String insertColumns[];
    protected String deleteColumns[];
    protected String updateColumns[];

    public LogInfo()
    {
    }

    public boolean isLogable(String columnName, String operType)
    {
        String s[] = (String[])null;
        if(operType.equals("update"))
            s = updateColumns;
        else
        if(operType.equals("insert"))
            s = insertColumns;
        else
        if(operType.equals("delete"))
            s = deleteColumns;
        if(s == null || s.length == 0)
            return false;
        for(int i = 0; i < s.length; i++)
            if(s[i].equals(columnName))
                return true;

        return false;
    }

    public static String formatString(String str)
    {
        String s[] = convertString(str);
        return formatString(s);
    }

    public static String formatString(String s[])
    {
        if(s == null || s.length == 0)
            return "";
        String r = s[0];
        for(int i = 1; i < s.length; i++)
            r = r + "," + s[i];

        return r;
    }

    protected static String[] convertString(String str)
    {
        if(str == null)
            return null;
        String ts[] = str.split(",");
        int count = 0;
        if(ts.length == 0)
            return null;
        for(int i = 0; i < ts.length; i++)
        {
            ts[i] = ts[i].trim();
            if(ts[i].length() != 0)
            {
                if(i != count)
                    ts[count] = ts[i];
                count++;
            }
        }

        if(count == 0)
            return null;
        if(count == ts.length)
            return ts;
        String ret[] = new String[count];
        for(int i = 0; i < count; i++)
            ret[i] = ts[i];

        return ret;
    }

    public String getDeleteColumns()
    {
        return formatString(deleteColumns).toLowerCase();
    }

    public List getDeleteList()
    {
        List l = new ArrayList();
        if(deleteColumns != null)
        {
            for(int i = 0; i < deleteColumns.length; i++)
                l.add(deleteColumns[i]);

        }
        return l;
    }

    public List getInsertList()
    {
        List l = new ArrayList();
        if(insertColumns != null)
        {
            for(int i = 0; i < insertColumns.length; i++)
                l.add(insertColumns[i]);

        }
        return l;
    }

    public List getUpdateList()
    {
        List l = new ArrayList();
        if(updateColumns != null)
        {
            for(int i = 0; i < updateColumns.length; i++)
                l.add(updateColumns[i]);

        }
        return l;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public String getInsertColumns()
    {
        return formatString(insertColumns).toLowerCase();
    }

    public void setInsertColumns(String insertColumns)
    {
        this.insertColumns = convertString(insertColumns);
    }

    public String getKeyColumn()
    {
        return keyColumn;
    }

    public void setKeyColumn(String keyColumn)
    {
        this.keyColumn = keyColumn.toLowerCase();
    }

    public int getOid()
    {
        return oid;
    }

    public void setOid(int oid)
    {
        this.oid = oid;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName.toLowerCase();
    }

    public String getUpdateColumns()
    {
        return formatString(updateColumns).toLowerCase();
    }

    public void setUpdateColumns(String updateColumns)
    {
        this.updateColumns = convertString(updateColumns);
    }

    public void setDeleteColumns(String deleteColumns)
    {
        this.deleteColumns = convertString(deleteColumns);
    }
}
