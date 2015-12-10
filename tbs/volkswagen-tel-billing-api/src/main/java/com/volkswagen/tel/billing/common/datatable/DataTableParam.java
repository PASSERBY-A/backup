package com.volkswagen.tel.billing.common.datatable;

import java.io.Serializable;
import java.util.List;

public class DataTableParam implements Serializable {
	private static final long serialVersionUID = -8816050698474150452L;
	private String isQuery;
	private List<VOSet> voList;

	public String getIsQuery() {
		return isQuery;
	}

	public void setIsQuery(String isQuery) {
		this.isQuery = isQuery;
	}

	public List<VOSet> getVoList() {
		return voList;
	}

	public void setVoList(List<VOSet> voList) {
		this.voList = voList;
	}
}
