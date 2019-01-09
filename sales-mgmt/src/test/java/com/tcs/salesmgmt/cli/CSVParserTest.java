package com.tcs.salesmgmt.cli;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcs.salesmgmt.domain.model.CSVItem;

@RunWith(SpringRunner.class)
public class CSVParserTest {
	CSVParser parser = new CSVParser();
	
	@Test
	public void parseCSVTestWithValidEntry() throws IOException {
		Path path = getPath("COMPUTER;2000;series_number:compn002;manufacturer:dell;price:70000;form-factor:nettop" + "\n"
				+ "COMPUTER;40;series_number:compm004;manufacturer:lenovo;price:45000;form-factor:monoblock");
		List<CSVItem> csvList = parser.parseCSV(path);
		
		Assert.assertTrue(csvList.get(0).getProductType().equals("COMPUTER"));
		Assert.assertTrue(csvList.get(0).getAmount() == 2000);
		Assert.assertTrue(csvList.get(0).getItemProperties().get("series_number").equals("compn002"));
		
		path.toFile().deleteOnExit();
	}
	
	@Test
	public void parseCSVTestWithBlankCSVFile() throws IOException {
		Path path = getPath("");
		
		String expectedErrorMsg = "Invalid CSV File";
		String actualErrorMsg = null;
		
		try {
			parser.parseCSV(path);
		} catch(RuntimeException e) {
			actualErrorMsg = e.getMessage();
		}
		
		Assert.assertTrue(expectedErrorMsg.equals(actualErrorMsg));
		path.toFile().deleteOnExit();
	}
	
	@Test
	public void parseCSVTestWithIncorrectNumberOfCSVValues() throws IOException {
		Path path = getPath("DVDDRIVE;2000;series_number:compn002;manufacturer:dell;price:70000");
		
		String expectedErrorMsg = "Invalid CSV File: Total Value Count Should Be 6!";
		String actualErrorMsg = null;
		
		try {
			parser.parseCSV(path);
		} catch(RuntimeException e) {
			actualErrorMsg = e.getMessage();
		}
		
		Assert.assertTrue(expectedErrorMsg.equals(actualErrorMsg));
		path.toFile().deleteOnExit();
	}
	
	@Test
	public void parseCSVTestWithIncorrectProductType() throws IOException {
		Path path = getPath("DVDDRIVE;2000;series_number:compn002;manufacturer:dell;price:70000;form-factor:nettop");
		
		String expectedErrorMsg = "Invalid CSV File: Incorrect ProductType: DVDDRIVE";
		String actualErrorMsg = null;
		
		try {
			parser.parseCSV(path);
		} catch(RuntimeException e) {
			actualErrorMsg = e.getMessage();
		}
		
		Assert.assertTrue(expectedErrorMsg.equals(actualErrorMsg));
		path.toFile().deleteOnExit();
	}

	private Path getPath(final String csvData) throws IOException {
		Path path = Files.createTempFile("temp", ".txt");
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			writer.write(csvData);
		}
		return path;
	}
}
