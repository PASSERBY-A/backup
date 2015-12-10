/*
 * @(#)ProductCatalogDtl.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.business.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


/**
 * 产品目录详细信息,一个产品目录会有若干个详细信息
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 11:20:13 PM Jul 17, 2011
 * 
 */

@Entity
@Table(name="business_product_catalog_dtl")
@AssociationOverrides(value = { 
		@AssociationOverride(name="catalog", joinColumns=@JoinColumn(name="catalog_id")),
		@AssociationOverride(name="product", joinColumns=@JoinColumn(name="product_id"))
})
public class ProductCatalogDtl implements Serializable{
	
	public final static String[] EFFECT_TYPE = new String[]{"保留", "可立即生效", "下周期生效"};
	public final static String[] SELECT = new String[]{"必选产品", "预选产品", "可选产品", "不可修改"};

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private ProductCatalogDtlPK id;
	
	@Transient
	private Integer effectType;
	
	@Transient
	private Integer selectFlag = 0;
	
	@Transient
	private Integer minQuanity = 0;
	
	@Transient
	private Integer maxQuanity;
	
	@Column(name="product_status",length=2)
	private Integer status;
	
	@Column(name="default_quanity",length=5)
	private Integer defaultQuanity = 1;
	
	@Column(name="note",length=100)
	private String note;

	@Column(name="create_date")
	@Temporal(value=TemporalType.DATE)
	private Date createDate;
	
	@Column(name="update_date")
	@Temporal(value=TemporalType.DATE)
	private Date upldateDate;
	


	public Integer getEffectType() {
		return effectType;
	}

	public void setEffectType(Integer effectType) {
		this.effectType = effectType;
	}

	public Integer getSelectFlag() {
		return selectFlag;
	}

	public void setSelectFlag(Integer selectFlag) {
		this.selectFlag = selectFlag;
	}

	public Integer getMinQuanity() {
		return minQuanity;
	}

	public void setMinQuanity(Integer minQuanity) {
		this.minQuanity = minQuanity;
	}

	public Integer getMaxQuanity() {
		return maxQuanity;
	}

	public void setMaxQuanity(Integer maxQuanity) {
		this.maxQuanity = maxQuanity;
	}

	public Integer getDefaultQuanity() {
		return defaultQuanity;
	}

	public void setDefaultQuanity(Integer defaultQuanity) {
		this.defaultQuanity = defaultQuanity;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpldateDate() {
		return upldateDate;
	}

	public void setUpldateDate(Date upldateDate) {
		this.upldateDate = upldateDate;
	}

	public ProductCatalogDtlPK getId() {
		return id;
	}

	public void setId(ProductCatalogDtlPK id) {
		this.id = id;
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public int hashCode(){
		return getId().hashCode();
	}
	
	@Override
	public boolean equals(Object object){
		if (ProductCatalogDtl.class.isInstance(object)) {
			ProductCatalogDtl pc = (ProductCatalogDtl)object;
		    if (pc.getId().equals(getId())) {
		    	return true;
		    }
		}
		return false;
	}


	
}
