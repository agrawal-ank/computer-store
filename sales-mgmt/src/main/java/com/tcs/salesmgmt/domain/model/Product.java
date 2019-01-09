package com.tcs.salesmgmt.domain.model;

import java.util.ArrayList;
import java.util.List;

public enum Product {
	COMPUTER("FORM-FACTOR"), LAPTOP("SIZE"), MONITOR("RESOLUTION"), HDD("CAPACITY");
	
	private String property;
	
	Product(String property) {
		this.property = property;
	}

	public String getProperty() {
		return property;
	}

	public static List<String> getValidProducts() {
		List<String> productList = new ArrayList<>();
		for(Product product : Product.values()) {
			productList.add(product.name());
		}
		return productList;
	}
}
