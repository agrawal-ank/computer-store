package com.tcs.salesmgmt.domain.repo;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcs.salesmgmt.domain.model.ItemDetails;
import com.tcs.salesmgmt.domain.model.ManufactureDetails;
import com.tcs.salesmgmt.domain.model.ProductProperties;
import com.tcs.salesmgmt.domain.model.ProductType;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductItemRepositoryTest {
	@Autowired
	TestEntityManager em;
	
	@Autowired
	ProductItemRepository productItemRepository;
	
	@Test
	public void findByProductTypeTest() {
		ProductType item = getItem();
		
		em.persist(item);
		em.flush();
		
		ProductType found = productItemRepository.findByProductType("computer");
		assertTrue(found.equals(item));
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
}
