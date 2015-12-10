package com.cmsz.cloudplatform.utils;

import java.util.Comparator;  

import com.cmsz.cloudplatform.model.WorkItem;

public class Compare implements Comparator {  
  
    /*  
     * 这里表示按id从小到大排序，如果该对象o1小于、等于或大于指定对象o2，则分别返回负整数、零或正整数 
     * 如果需要从大到小排序，则如果对象o1小于、等于或大于指定对象o2，则分别返回正整数、零或负整数 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object) 
     */  
    public int compare(Object o1, Object o2) {  
        WorkItem s1=(WorkItem)o1;  
        WorkItem s2=(WorkItem)o2;  
        
        if(s1.getStep()<s2.getStep()){  
            return -1;  
        }  
        if(s1.getStep()>s2.getStep()){  
            return 1;  
        }  
        return 0;  
    }  
}  