package com.tcs.salesmgmt.domain.model;

import java.util.ArrayList;
import java.util.List;

public enum CommonProperty {
	SERIES_NUMBER, MANUFACTURER, PRICE;
	
	public static List<String> getValidCommonProperties() {
		List<String> validPropertiesList = new ArrayList<>();
		for(CommonProperty prop : CommonProperty.values()) {
			validPropertiesList.add(prop.name());
		}
		return validPropertiesList;
	}
}
