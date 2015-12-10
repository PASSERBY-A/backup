// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   PageInfo.java

package com.hp.idc.cas.dbo;


public class PageInfo
{

    protected int startPage;
    protected int count;
    protected int rowsPerPage;
    protected int pageCount;

    public PageInfo()
    {
        startPage = 0;
        count = 0;
        rowsPerPage = 50;
    }

    public int getPageCount()
    {
        return pageCount;
    }

    public void setPageCount(int pageCount)
    {
        this.pageCount = pageCount;
    }

    public void recalc()
    {
        pageCount = (count - 1) / rowsPerPage + 1;
        if(startPage >= pageCount)
            startPage = pageCount - 1;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public int getRowsPerPage()
    {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage)
    {
        if(rowsPerPage <= 0)
            rowsPerPage = 50;
        this.rowsPerPage = rowsPerPage;
    }

    public int getStartPage()
    {
        return startPage;
    }

    public void setStartPage(int startPage)
    {
        this.startPage = startPage;
    }
}
