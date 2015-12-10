package com.hp.idc.boss.service.impl;

import java.io.FilenameFilter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;


import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.springframework.stereotype.Service;

import com.hp.idc.boss.BOSSBase;
import com.hp.idc.boss.service.ProductCatalogInsertService;
import com.hp.idc.business.entity.ProductCatalog;

@Service("productCatalogInsertService")
public class ProductCatalogInsertServiceImpl extends BOSSBase implements
		ProductCatalogInsertService {
	
	@Override
	public boolean saveResult(Map<String, String> result) throws Exception {
		ProductCatalog productCatalog = new ProductCatalog();
		try {
			BeanUtils.populate(productCatalog, result);
			DateFormat df = new SimpleDateFormat(getTimeFormat());
			productCatalog.setCreateDate(df.parse(result.get("create_date")));
			getProductCatalogDao().save(productCatalog);
			return true;
		} catch (Exception e) {
			setErrorMsg(e.getMessage());
			return false;
		}
	}

	public FilenameFilter filenameFilter = new WildcardFileFilter("idcproductcatlog*.unl");

	@Override
	public FilenameFilter getFilenameFilter() {
		return filenameFilter;
	}
}
