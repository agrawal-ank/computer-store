package com.tcs.salesmgmt.cli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import com.tcs.salesmgmt.domain.model.CSVItem;
import com.tcs.salesmgmt.domain.model.CommonProperty;
import com.tcs.salesmgmt.domain.model.FormFactor;
import com.tcs.salesmgmt.domain.model.Product;

/**
 * Responsible for parsing csv file, validating and mapping of each item to
 * object. Expect CSV file item in follwing format: 
 * <product_type>;<amount>;series_number:<>;manufacturer:<>;price:<>;<product_specific_key>:<product_specific_value>
 **/ 
@Component
public class CSVParser {
	Log log = LogFactory.getLog(CSVParser.class);
	
	public List<CSVItem> parseCSV(Path csvFilePath) throws IOException {
		List<String> csvItemLines = Files.readAllLines(csvFilePath);
		if (csvItemLines.size() == 0) 
			throw new RuntimeException("Invalid CSV File");
		
		Function<String, CSVItem> parsingLogic = getLogic();
		List<CSVItem> itemList = csvItemLines.stream().map(parsingLogic).collect(Collectors.toList());
		log.debug(itemList);
		return itemList;
	}

	private CSVItem mapToItemObj(List<String> tokenList) {
		if(tokenList.size() != 6) {
			throw new RuntimeException("Invalid CSV File: Total Value Count Should Be 6!");
		}
		
		if(!Product.getValidProducts().contains(tokenList.get(0).toUpperCase())) {
			throw new RuntimeException("Invalid CSV File: Incorrect ProductType: " + tokenList.get(0));
		}
		final String productType = tokenList.get(0);
		
		if(!tokenList.get(1).chars().allMatch(Character::isDigit)) {
			throw new RuntimeException("Invalid CSV File: Incorrect Amount: " + tokenList.get(1));
		}
		final int amount = Integer.valueOf(tokenList.get(1));

		Map<String, String> itemProperties = tokenList.stream().filter(each -> each.contains(":")).map(getKeyValueParsingLogic())
				.collect(Collectors.toMap(each -> each[0], each -> each[1]));
		if(!isKeyValuePropertiesValid(productType, itemProperties)) {
			throw new RuntimeException("Invalid CSV File: Incorrect Item Property");
		}
		
		CSVItem item = new CSVItem();
		item.setProductType(productType);
		item.setAmount(amount);
		item.setItemProperties(itemProperties);
		
		return item;
	}
	
	private Function<String, CSVItem> getLogic() {
		Function<String, CSVItem> function = eachItemLine -> {
			log.debug(eachItemLine);
			StringTokenizer st = new StringTokenizer(eachItemLine, ";");
			List<String> tokenList = new ArrayList<>();
			while (st.hasMoreTokens()) {
				tokenList.add(st.nextToken());
			}
			CSVItem item = mapToItemObj(tokenList);
			log.debug(item);
			return item;
		};
		return function;
	}
	
	private Function<String, String[]> getKeyValueParsingLogic() {
		return eachItemLine -> {
			log.debug(eachItemLine);
			String[] tokens = eachItemLine.split(":");
			tokens[0] = tokens[0].toUpperCase();
			log.debug(tokens);
			return tokens;
		};
	}
	
	/**
	 * Validate Product Specific and Common Properties
	 * 
	 * @param productType
	 * @param itemProperties
	 * @return
	 */
	private boolean isKeyValuePropertiesValid(String productType, Map<String, String> itemProperties) {
		List<String> validPropertiesKey = CommonProperty.getValidCommonProperties();
		final String productSpecificProperty = Product.valueOf(productType.toUpperCase()).getProperty();
		validPropertiesKey.add(productSpecificProperty);

		for (String key : itemProperties.keySet()) {
			if (!validPropertiesKey.contains(key)) {
				return false;
			}
			validPropertiesKey.remove(key);
		}

		if (validPropertiesKey.size() != 0)
			return false;
		
		/*
		 *  Validating form-factor if ProductType is Computer.
		 *  Return true if valid otherwise false
		 */
		if (Product.COMPUTER.name().equalsIgnoreCase(productType)) {
			List<String> validFormFactorList = FormFactor.getValidFormFactors();
			final String formFactor = itemProperties.get(Product.COMPUTER.getProperty());
			return validFormFactorList.contains(formFactor.toUpperCase());
		}
		return true;
	}
}
