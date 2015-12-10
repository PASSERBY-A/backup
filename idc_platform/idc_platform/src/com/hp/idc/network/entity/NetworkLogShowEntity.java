package com.hp.idc.network.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 网络历史状态数据结构
 * 
 * @author Wang Rui
 * @version 1.0,
 * 
 */
@Entity
@Table(name= "SITESCOPELOG")
public class NetworkLogShowEntity implements Serializable {

	private static final long serialVersionUID = 86454402763623542L;

	@Column(length=255)
	private String category;

	@Column(name="CLASS", length=255)
	private String class_;
    
	@Id
	@Column(length=255)
	private String datex;

	@Column(length=255)
	private String groupname;

	@Column(length=255)
	private String monitorid;

	@Column(length=255)
	private String monitorname;

	@Column(length=255)
	private String sample;

	@Column(length=255)
	private String servername;

	@Column(length=255)
	private String status;

	@Column(length=255)
	private String value1;

	@Column(length=255)
	private String value10;

	@Column(length=255)
	private String value2;

	@Column(length=255)
	private String value3;

	@Column(length=255)
	private String value4;

	@Column(length=255)
	private String value5;

	@Column(length=255)
	private String value6;

	@Column(length=255)
	private String value7;

	@Column(length=255)
	private String value8;

	@Column(length=255)
	private String value9;

    public NetworkLogShowEntity() {
    }

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getClass_() {
		return this.class_;
	}

	public void setClass_(String class_) {
		this.class_ = class_;
	}

	public String getDatex() {
		return this.datex;
	}

	public void setDatex(String datex) {
		this.datex = datex;
	}

	public String getGroupname() {
		return this.groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getMonitorid() {
		return this.monitorid;
	}

	public void setMonitorid(String monitorid) {
		this.monitorid = monitorid;
	}

	public String getMonitorname() {
		return this.monitorname;
	}

	public void setMonitorname(String monitorname) {
		this.monitorname = monitorname;
	}

	public String getSample() {
		return this.sample;
	}

	public void setSample(String sample) {
		this.sample = sample;
	}

	public String getServername() {
		return this.servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getValue1() {
		return this.value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue10() {
		return this.value10;
	}

	public void setValue10(String value10) {
		this.value10 = value10;
	}

	public String getValue2() {
		return this.value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getValue3() {
		return this.value3;
	}

	public void setValue3(String value3) {
		this.value3 = value3;
	}

	public String getValue4() {
		return this.value4;
	}

	public void setValue4(String value4) {
		this.value4 = value4;
	}

	public String getValue5() {
		return this.value5;
	}

	public void setValue5(String value5) {
		this.value5 = value5;
	}

	public String getValue6() {
		return this.value6;
	}

	public void setValue6(String value6) {
		this.value6 = value6;
	}

	public String getValue7() {
		return this.value7;
	}

	public void setValue7(String value7) {
		this.value7 = value7;
	}

	public String getValue8() {
		return this.value8;
	}

	public void setValue8(String value8) {
		this.value8 = value8;
	}

	public String getValue9() {
		return this.value9;
	}

	public void setValue9(String value9) {
		this.value9 = value9;
	}

}