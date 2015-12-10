/**
 * 知识类别表
 * @author fancy
 * @date 2011-7-14
 */
package com.hp.idc.kbm.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity(name = "KbmCategory")
@Table(name = "KBM_CATEGORY")
public class KbmCategory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	private String categoryName;
	private long parentCategoryId;
	private int baseType;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_kbm_category_id")
	@SequenceGenerator(name = "seq_kbm_category_id", sequenceName = "seq_kbm_category_id")
	@Column(name = "category_id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "category_name", length = 255, nullable = true)
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	// @ManyToOne(fetch = FetchType.LAZY, cascade = {
	// CascadeType.PERSIST,CascadeType.MERGE}, optional = true)
	// @JoinColumn(name = "parent_category_id", nullable = true)
	@Column(name = "parent_category_id")
	public long getParentCategoryId() {
		return parentCategoryId;
	}

	public void setParentCategoryId(long parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	@Column(name = "base_type", nullable = false)
	public int getBaseType() {
		return baseType;
	}

	public void setBaseType(int baseType) {
		this.baseType = baseType;
	}

}
