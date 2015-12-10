/*
 * @(#)ProductCatalog.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.business.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * 产品目录信息
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 10:37:32 PM Jul 17, 2011
 * 
 */

@Entity
@Table(name="business_product_catalog")
public class ProductCatalog implements Serializable {

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="catalog_id", length=8)
	private long id;

	@Column(name="catalog_name", length=100)
	private String name;

	@Column(name="catalog_desc", length=200)
	private String description;

	@Column(name="catalog_status",length=2)
	private Long status;

	@Column(name="pkg_id",length=6)
	private Long pkgId;

	@Column(name="effect_date")
	@Temporal(value=TemporalType.DATE)
	private Date effectDate;

	@Column(name="expire_date")
	@Temporal(value=TemporalType.DATE)
	private Date expireDate;

	@Column(name="create_date")
	@Temporal(value=TemporalType.DATE)
	private Date createDate;
	
	@Column(name="create_oper_id",length=10)
	private String creator;

	@Column(name="update_date")
	@Temporal(value=TemporalType.DATE)
	private Date updateDate;

	@Column(name="update_oper_id",length=10)
	private String updater;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Long getPkgId() {
		return pkgId;
	}

	public void setPkgId(Long pkgId) {
		this.pkgId = pkgId;
	}

	public Date getEffectDate() {
		return effectDate;
	}

	public void setEffectDate(Date effectDate) {
		this.effectDate = effectDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}



	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}


	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}


	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
