package com.hp.idc.boss.service.impl;

import java.io.FilenameFilter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import com.hp.idc.boss.BOSSBase;
import com.hp.idc.boss.service.ProductInsertService;
import com.hp.idc.business.entity.Product;

/**
 * 
 * 产品信息同步
 * 
 * @author <a href="mailto:ruiw@hp.com">Wang Rui</a>
 * @version 1.0, 下午2:45:54 2011-8-4
 *
 */
public class ProductInsertServiceImpl extends BOSSBase implements
		ProductInsertService {

	@Override
	public boolean saveResult(Map<String, String> result) throws Exception {
		Product product = new Product();
		try {
			BeanUtils.populate(product, result);
			DateFormat df = new SimpleDateFormat(getTimeFormat());
			product.setCreateDate(df.parse(result.get("create_date")));
			getProductDao().save(product);
			return true;
		} catch (Exception e) {
			setErrorMsg(e.getMessage());
			return false;
		}
	}
	
	FilenameFilter filenameFilter = new WildcardFileFilter("idcproduct*.unl"); 

	@Override
	public FilenameFilter getFilenameFilter() {
		return filenameFilter;
	}
}
