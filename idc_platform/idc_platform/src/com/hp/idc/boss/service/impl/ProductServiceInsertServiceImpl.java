package com.hp.idc.boss.service.impl;

import java.io.FilenameFilter;
import java.util.Map;

import org.apache.commons.io.filefilter.WildcardFileFilter;

import com.hp.idc.boss.BOSSBase;
import com.hp.idc.boss.service.ProductServiceInsertService;
import com.hp.idc.business.entity.Product;
import com.hp.idc.business.entity.Service;

/**
 * ��Ʒ������ϵ��Ϣͬ��
 * 
 * 
 * @author <a href="mailto:ruiw@hp.com">Wang Rui</a>
 * @version 1.0, ����3:09:36 2011-8-4
 *
 */
public class ProductServiceInsertServiceImpl extends BOSSBase implements
		ProductServiceInsertService {

	@Override
	public boolean saveResult(Map<String, String> result) throws Exception {
		try {
			String productID = result.get("product_id");
			String serviceID = result.get("service_id");
			Long idL = Long.parseLong(serviceID);
			Service service = getServiceDao().get(idL);
			if (service == null || "".equals(service.getName())) {
				setErrorMsg("�޹����ķ�����Ϣ.");
				return false;
			}
			idL = Long.parseLong(productID);
			Product product = getProductDao().get(idL);
			if (product == null || "".equals(product.getName())) {
				setErrorMsg("�޹����Ĳ�Ʒ��Ϣ.");
				return false;
			}
			product.addService(service);
			getProductDao().save(product);
			return true;
		} catch (Exception e) {
			setErrorMsg(e.getMessage());
			return false;
		}
	}
	
	FilenameFilter filenameFilter = new WildcardFileFilter("idcprodservicerelation*.unl"); 
	
	@Override
	public FilenameFilter getFilenameFilter() {
		return filenameFilter;
	}

}
