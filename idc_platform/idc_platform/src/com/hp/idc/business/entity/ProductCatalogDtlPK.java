package com.hp.idc.business.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class ProductCatalogDtlPK implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2735457608096676686L;

	@ManyToOne(cascade=CascadeType.REFRESH,optional=false)
	@JoinColumn(name="catalog_id",nullable=false)
	private ProductCatalog catalog;
	
	@ManyToOne(cascade=CascadeType.REFRESH,optional=false)
	@JoinColumn(name="product_id",nullable=false)
	private Product product;

	public ProductCatalog getCatalog() {
		return catalog;
	}

	public void setCatalog(ProductCatalog catalog) {
		this.catalog = catalog;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	@Override
	public int hashCode(){
		int result = 1;
		final int prime=31;
		result = (int) (prime * result + ((catalog==null&&catalog.getId()>0)?0:catalog.getId()));
		result = (int) (prime * result + ((product==null&&product.getId()>0)?0:product.getId()));
		return result;
	}
	
	@Override
	public boolean equals(Object object){
		if (ProductCatalogDtlPK.class.isInstance(object)) {
			ProductCatalogDtlPK pk = (ProductCatalogDtlPK)object;
		    if (pk.getCatalog().getId()==(getCatalog().getId())
		    		&&pk.getProduct().equals(getProduct().getId())) {
		    	return true;
		    }
		}
		return false;
	}


}
