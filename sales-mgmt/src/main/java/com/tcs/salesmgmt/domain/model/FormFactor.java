package com.tcs.salesmgmt.domain.model;

import java.util.ArrayList;
import java.util.List;

public enum FormFactor {
	DESKTOP, NETTOP, MONOBLOCK;
	
	public static List<String> getValidFormFactors() {
		List<String> validFormFactorsList = new ArrayList<>();
		for(FormFactor ff : FormFactor.values()) {
			validFormFactorsList.add(ff.name());
		}
		return validFormFactorsList;
	}
}
