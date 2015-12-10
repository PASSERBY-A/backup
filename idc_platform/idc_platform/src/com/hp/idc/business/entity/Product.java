/*
 * @(#)Product.java
 *
 * Copyright (c) 2011, hp and/or its affiliates. All rights reserved.
 * HP PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.hp.idc.business.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 产品信息, 属于某一个产品目录
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 11:27:50 PM Jul 17, 2011
 * 
 */

@Entity
@Table(name="business_product")
public class Product implements Serializable{
	
	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="product_id",length=8)
	private long id;
	
	@Column(name="product_name",length=100)
	private String name;
	
	@Column(name="product_desc",length=200)
	private String description;
	
	@Column(name="sub_param", length=225)
	private String subParam;
	
	@Column(name="product_status",length=2)
	private Integer status;	
	
	@Column(name="org_id",length=6)
	private Long orgId;
	
	@Column(name="create_oper_id",length=10)
	private String creator;
	
	@Column(name="create_date")
	@Temporal(value=TemporalType.DATE)
	private Date createDate;
	
	@Column(name="effect_date")
	@Temporal(value=TemporalType.DATE)
	private Date effectDate;
	
	@Column(name="expire_date")
	@Temporal(value=TemporalType.DATE)
	private Date expireDate;

	@ManyToMany(cascade ={CascadeType.REFRESH}, fetch=FetchType.LAZY)
	@JoinTable(name = "business_product_service", joinColumns = {@JoinColumn(name = "product_id",nullable = false)}, 
			inverseJoinColumns = @JoinColumn(name = "service_id",nullable = false))
	private Set<Service> services= new HashSet<Service>();
	
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

	public String getSubParam() {
		return subParam;
	}

	public void setSubParam(String subParam) {
		this.subParam = subParam;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}


	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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

	public Set<Service> getServices() {
		return services;
	}

	public void setServices(Set<Service> services) {
		this.services = services;
	}
	
	public void addService(Service service) {
		if(!this.services.contains(service))
			this.services.add(service);
	}

	public void removeService(Service service) {
		this.services.remove(service);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}



}
