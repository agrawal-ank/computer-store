package com.tcs.salesmgmt.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.test.context.junit4.SpringRunner;

import com.tcs.salesmgmt.domain.model.CSVItem;
import com.tcs.salesmgmt.domain.model.ItemDetails;
import com.tcs.salesmgmt.domain.model.ManufactureDetails;
import com.tcs.salesmgmt.domain.model.ProductProperties;
import com.tcs.salesmgmt.domain.model.ProductType;
import com.tcs.salesmgmt.domain.repo.ManufacturerRepository;
import com.tcs.salesmgmt.domain.repo.ProductItemRepository;

@RunWith(SpringRunner.class)
public class ProductItemServiceTest {
	@Mock
	ProductItemRepository productItemRepository;
	
	@Mock
	ManufacturerRepository manufacturerRepository;
	
	@InjectMocks
	ProductItemService service;
	
	@Test
	public void saveCSVItemListTest() {
		List<CSVItem> csvItemList = getCSVItemList();
		
		ProductType item = new ProductType();
		ManufactureDetails manufacturer = null;
		
		when(productItemRepository.findByProductType(anyString())).thenReturn(item);
		when(manufacturerRepository.findByManufacturerName(anyString())).thenReturn(manufacturer);
		
		service.saveOrUpdateCSVItemList(csvItemList);
		
		long actualAmout = item.getProductProperties().get(0).getManufacturedBy().get(0).getItemDetails().get(0).getAmount();
		long expectdAmount = 5000;
		Assert.assertTrue(actualAmout == expectdAmount);
	}
		
	@Test
	public void updateCSVItemListTest() {
		List<CSVItem> csvItemList = getCSVItemList();
		
		ProductType item = getItem();
		ManufactureDetails manufacturer = getManufacturer();
		when(productItemRepository.findByProductType(anyString())).thenReturn(item);
		when(manufacturerRepository.findByManufacturerName(anyString())).thenReturn(manufacturer);
		
		service.saveOrUpdateCSVItemList(csvItemList);
		
		long actualAmout = item.getProductProperties().get(0).getManufacturedBy().get(0).getItemDetails().get(0).getAmount();
		long expectdAmount = 5012;
		Assert.assertTrue(actualAmout == expectdAmount);
		
	}
	
	private ProductType getItem() {
		ProductType item = new ProductType();
		item.setProductType("computer");
		
		ProductProperties computerProperties = new ProductProperties();
		computerProperties.setPropertyName("form-factor");
		computerProperties.setPropertyValue("desktop");
		List<ProductProperties> computerPropertiesList = new ArrayList<>();
		computerPropertiesList.add(computerProperties);
		
		item.setProductProperties(computerPropertiesList);
		
		ManufactureDetails manufacturer1 = new ManufactureDetails();
		manufacturer1.setManufacturerName("HP");
		ManufactureDetails manufacturer2 = new ManufactureDetails();
		manufacturer2.setManufacturerName("Lenovo");
		List<ManufactureDetails> computerManufacturerList = new ArrayList<>();
		computerManufacturerList.add(manufacturer1);
		computerManufacturerList.add(manufacturer2);
		
		computerProperties.setManufacturedBy(computerManufacturerList);
		
		ItemDetails item1 = new ItemDetails("C001", 80000, 12);
		ItemDetails item2 = new ItemDetails("C002", 100000, 10);
		List<ItemDetails> itemList  = new ArrayList<>();
		itemList.add(item1);
		itemList.add(item2);
		
		manufacturer1.setItemDetails(itemList);
		
		return item;
	}
	
	private ManufactureDetails getManufacturer() {
		ItemDetails item1 = new ItemDetails("C001", 80000, 12);
		ItemDetails item2 = new ItemDetails("C002", 100000, 10);
		List<ItemDetails> itemList  = new ArrayList<>();
		itemList.add(item1);
		itemList.add(item2);
		
		ManufactureDetails manufacturer1 = new ManufactureDetails();
		manufacturer1.setManufacturerName("HP");
		manufacturer1.setItemDetails(itemList);
		
		return manufacturer1;
	}
	
	private List<CSVItem> getCSVItemList() {
		List<CSVItem> csvItemList = new ArrayList<>();
		
		CSVItem item1 = new CSVItem();
		item1.setProductType("computer");
		item1.setAmount(5000);
		HashMap<String, String> propertyMap = new HashMap<>();
		propertyMap.put("form-factor", "desktop");
		propertyMap.put("series_number", "C001");
		propertyMap.put("price", "80000");
		propertyMap.put("manufacturer", "HP");
		item1.setItemProperties(propertyMap);
		
		CSVItem item2 = new CSVItem();
		item2.setProductType("computer");
		item2.setAmount(1000);
		HashMap<String, String> propertyMap1 = new HashMap<>();
		propertyMap1.put("form-factor", "desktop");
		propertyMap1.put("series_number", "C002");
		propertyMap1.put("price", "100000");
		propertyMap1.put("manufacturer", "HP");
		item2.setItemProperties(propertyMap1);
		
		csvItemList.add(0, item1);
		csvItemList.add(1, item2);
		
		return csvItemList;
	}

}
