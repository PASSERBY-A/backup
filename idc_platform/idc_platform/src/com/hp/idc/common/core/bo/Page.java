package com.hp.idc.common.core.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * ∑÷“≥¿‡
 * @ClassName: Page
 * @Descprition: TODO
 * @author <a href="mailto:si-qi.liang@hp.com">Liang, Si-Qi</a>
 * @version 1.0
 * @param <T>
 */
public class Page<T> implements Serializable
{

	private static final long serialVersionUID = -7120518943554405107L;
	
	public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    private int pageSize;
    private int start;
    private int currentPageSize;
    private int totalSize;
    private List<T> result;
    private int currentPageNo;
    private int totalPageCount;

    public Page()
    {
        this(0, 0, 0, 10, ((List<T>) (new ArrayList<T>())));
    }

    public Page(int start, int currentPageSize, int totalSize, int pageSize, List<T> data)
    {
        this.pageSize = 10;
        this.currentPageSize = currentPageSize;
        this.pageSize = pageSize;
        this.start = start;
        this.totalSize = totalSize;
        result = data;
        currentPageNo = (start - 1) / pageSize + 1;
        totalPageCount = ((totalSize + pageSize) - 1) / pageSize;
        if(totalSize == 0 && currentPageSize == 0)
        {
            currentPageNo = 1;
            totalPageCount = 1;
        }
    }

    public List<T> getResult()
    {
        return result;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public boolean hasNextPage()
    {
        return getCurrentPageNo() < getTotalPageCount();
    }

    public boolean hasPreviousPage()
    {
        return getCurrentPageNo() > 1;
    }

    public int getStart()
    {
        return start;
    }

    public int getEnd()
    {
        int end = (getStart() + getCurrentPageSize()) - 1;
        if(end < 0)
            end = 0;
        return end;
    }

    public int getStartOfPreviousPage()
    {
        return Math.max(start - pageSize, 1);
    }

    public int getStartOfNextPage()
    {
        return start + currentPageSize;
    }

    public static int getStartOfAnyPage(int pageNo)
    {
        return getStartOfAnyPage(pageNo, 10);
    }

    public static int getStartOfAnyPage(int pageNo, int pageSize)
    {
        int startIndex = (pageNo - 1) * pageSize + 1;
        if(startIndex < 1)
            startIndex = 1;
        return startIndex;
    }

    public int getCurrentPageSize()
    {
        return currentPageSize;
    }

    public int getTotalSize()
    {
        return totalSize;
    }

    public int getCurrentPageNo()
    {
        return currentPageNo;
    }

    public int getNextPageNo()
    {
        return currentPageNo + 1;
    }

    public int getTotalPageCount()
    {
        return totalPageCount;
    }

    public void setResult(List<T> result)
    {
        this.result = result;
    }

    public void setTotalSize(int totalSize)
    {
        this.totalSize = totalSize;
    }

    public void setCurrentPageNo(int currentPageNo)
    {
        this.currentPageNo = currentPageNo;
    }

    public void setCurrentPageSize(int currentPageSize)
    {
        this.currentPageSize = currentPageSize;
    }

}