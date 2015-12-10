package com.hp.idc.boss.service.impl;

import java.io.FilenameFilter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import com.hp.idc.boss.BOSSBase;
import com.hp.idc.boss.service.ProductCatalogDtlInsertService;
import com.hp.idc.business.entity.Product;
import com.hp.idc.business.entity.ProductCatalog;
import com.hp.idc.business.entity.ProductCatalogDtl;
import com.hp.idc.business.entity.ProductCatalogDtlPK;

/**
 * 
 *	产品目录与产品关系信息同步
 * 
 * @author <a href="mailto:ruiw@hp.com">Wang Rui</a>
 * @version 1.0, 下午2:20:55 2011-8-4
 *
 */
public class ProductCatalogDtlInsertServiceImpl extends BOSSBase implements
		ProductCatalogDtlInsertService {

	@Override
	public boolean saveResult(Map<String, String> result) throws Exception {
		ProductCatalogDtl productCatalogDtl = new ProductCatalogDtl();
		ProductCatalogDtlPK productCatalogDtlPK = new ProductCatalogDtlPK();
		try {
			BeanUtils.populate(productCatalogDtl, result);
			String catalogID = result.get("catalog_id");
			String productID = result.get("product_id");
			if (productID != null && !"".equals(productID) && catalogID != null
					&& !"".equals(catalogID)) {
				Long idL = Long.parseLong(catalogID);
				ProductCatalog catalog = getProductCatalogDao().get(idL);
				if (catalog == null || "".equals(catalog.getName())) {
					setErrorMsg("catalog miss.");
					return false;
				}
				idL = Long.parseLong(productID);
				Product product = getProductDao().get(idL);
				if (product == null || "".equals(product.getName())) {
					setErrorMsg("product miss.");
					return false;
				}
				productCatalogDtlPK.setCatalog(catalog);
				productCatalogDtlPK.setProduct(product);
			} else {
				setErrorMsg("key field miss.");
				return false;
			}
			DateFormat df = new SimpleDateFormat(getTimeFormat());
			productCatalogDtl.setCreateDate(df.parse(result.get("create_date")));
			productCatalogDtl.setId(productCatalogDtlPK);
			getProductCatalogDtlDao().save(productCatalogDtl);
			return true;
		} catch (Exception e) {
			setErrorMsg(e.getMessage());
			return false;
		}
	}
	
	FilenameFilter filenameFilter = new WildcardFileFilter("idccatlogproductrelationship*.unl"); 
    
	@Override
	public FilenameFilter getFilenameFilter() {
		return filenameFilter;
	}
}
