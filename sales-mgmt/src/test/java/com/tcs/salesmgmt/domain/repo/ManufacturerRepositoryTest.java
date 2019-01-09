package com.tcs.salesmgmt.domain.repo;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcs.salesmgmt.domain.model.ItemDetails;
import com.tcs.salesmgmt.domain.model.ManufactureDetails;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ManufacturerRepositoryTest {
	@Autowired
	TestEntityManager em;
	
	@Autowired
	ManufacturerRepository manufacturerRepository;
	
	@Test
	public void findByManufacturerNameTest() {
		ItemDetails item1 = new ItemDetails("C001", 80000, 12);
		ItemDetails item2 = new ItemDetails("C002", 100000, 10);
		List<ItemDetails> itemList  = new ArrayList<>();
		itemList.add(item1);
		itemList.add(item2);
		ManufactureDetails manufacturer = new ManufactureDetails("HP", itemList);
		
		em.persist(manufacturer);
		em.flush();
		
		ManufactureDetails found = manufacturerRepository.findByManufacturerName("HP");
		assertTrue(found.getManufacturerName().equals(manufacturer.getManufacturerName()));
		assertTrue(found.equals(manufacturer));
	}
}
