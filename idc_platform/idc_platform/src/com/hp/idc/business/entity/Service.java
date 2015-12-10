/*
 * @(#)Service.java
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hp.idc.common.core.bo.AbstractBaseBO;

/**
 * 服务定义
 * 
 * @author <a href="mailto:anjing.zhong@hp.com">Silence</a>
 * @version 1.0, 11:30:43 PM Jul 17, 2011
 * 
 */

@Entity
@Table(name="business_service")
public class Service extends AbstractBaseBO implements Serializable{

	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 1L;

	public final static String[] TYPE = new String[]{"基础服务","增值服务"};
	
	@Id
	@Column(name="service_id",length=5)
	private long id;
	
	@Column(name="service_template_id",length=5)
	private long templateId;
	
	@Column(name="service_name",length=100)
	private String name;
	
	@Column(name="service_type",length=2)
	private Integer type;

	@Column(name="service_desc", length=200)
	private String description;
	
	@Column(name="service_status",length=2)
	private Integer status;
	
	@Column(name="service_value",length=40)
	private String serviceValue;
	
	@ManyToOne(cascade=CascadeType.REFRESH,optional=true,fetch=FetchType.LAZY)
	@JoinColumn(name="parent_service_id")
	private Service parentService;
	
//	@OneToMany(cascade=CascadeType.REFRESH, fetch=FetchType.LAZY, mappedBy="parentService")
//    private Set<Service> children = new HashSet<Service>(); 
	
	@Column(name="create_date")
	@Temporal(value=TemporalType.DATE)
	private Date createDate;
	
	@Column(name="effect_date")
	@Temporal(value=TemporalType.DATE)
	private Date effectDate;
	
	@Column(name="expire_date")
	@Temporal(value=TemporalType.DATE)
	private Date expireDate;
	
	@ManyToMany(cascade = {CascadeType.REFRESH},mappedBy="services", fetch = FetchType.LAZY)
	private Set<Product> products=new HashSet<Product>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getServiceValue() {
		return serviceValue;
	}

	public void setServiceValue(String serviceValue) {
		this.serviceValue = serviceValue;
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

	public Service getParentService() {
		return parentService;
	}

	public void setParentService(Service parentService) {
		this.parentService = parentService;
	}

//	public Set<Service> getChildren() {
//		return children;
//	}
//
//	public void setChildren(Set<Service> children) {
//		this.children = children;
//	}

}
