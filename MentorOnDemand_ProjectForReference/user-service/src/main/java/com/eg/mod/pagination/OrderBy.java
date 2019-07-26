package com.eg.mod.pagination;

public enum OrderBy {

	ID("id"), NAME("name"), USERID("userId");

	private String OrderByCode;

	private OrderBy(String orderBy) {
		this.OrderByCode = orderBy;
	}

	public String getOrderByCode() {
		return this.OrderByCode;
	}

}
