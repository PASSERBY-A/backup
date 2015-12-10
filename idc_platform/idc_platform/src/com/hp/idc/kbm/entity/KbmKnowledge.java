/**
 * 知识点信息表
 * @author fancy
 * @date 2011-7-14
 */
package com.hp.idc.kbm.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.hp.idc.system.security.entity.SysUser;

@Entity(name = "KbmKnowledge")
@Table(name = "KBM_KNOWLEDGE")
public class KbmKnowledge implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String title;
	private String keywords;
	private KbmCategory categoryId;
	private String description;
	private String solution;
	private String professionType;
	private String deviceName;
	private String solutionContent;
	private Date createDate;
	private Date updateDate;
	private String creator;
	private String updater;
	private int isRetired;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_kbm_knowledge_id")
	@SequenceGenerator(name = "seq_kbm_knowledge_id", sequenceName = "seq_kbm_knowledge_id")
	@Column(name = "knowledge_id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "title", length = 500)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH }, optional = true)
	@JoinColumn(name = "category_id", nullable = false)
	public KbmCategory getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(KbmCategory categoryId) {
		this.categoryId = categoryId;
	}

	@Column(name = "keywords", length = 500)
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Column(name = "description", length = 2000)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "solution", length = 2000)
	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	@Column(name = "profession_type", length = 255)
	public String getProfessionType() {
		return professionType;
	}

	public void setProfessionType(String professionType) {
		this.professionType = professionType;
	}

	@Column(name = "device_name", length = 255)
	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	@Column(name = "solution_content", length = 2000)
	public String getSolutionContent() {
		return solutionContent;
	}

	public void setSolutionContent(String solutionContent) {
		this.solutionContent = solutionContent;
	}

	@Column(name = "create_date")
	@Temporal(value = TemporalType.DATE)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "last_update_date")
	@Temporal(value = TemporalType.DATE)
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Column(name = "creator")
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	// @ManyToOne(fetch=FetchType.LAZY, optional = true)
	// @JoinColumn(name="last_updater")
	@Column(name = "last_updater")
	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	@Column(name = "retired")
	public int getIsRetired() {
		return isRetired;
	}

	public void setIsRetired(int isRetired) {
		this.isRetired = isRetired;
	}

}
