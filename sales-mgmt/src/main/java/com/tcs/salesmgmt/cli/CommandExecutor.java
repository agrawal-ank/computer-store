package com.tcs.salesmgmt.cli;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcs.salesmgmt.domain.model.CSVItem;
import com.tcs.salesmgmt.services.ProductItemService;

/**
 * Responsible for executing the commands by invoking corresponding @Service
 * component.
 **/ 
@Component
public class CommandExecutor {
	Log log = LogFactory.getLog(CommandExecutor.class);

	@Autowired
	CSVParser parser;

	@Autowired
	ProductItemService itemService;

	public void importItems(final String fileStr) throws IOException {
		log.debug("fileStr: " + fileStr);

		Path path = Paths.get(fileStr);
		List<CSVItem> csvItemList = parser.parseCSV(path);
		log.debug(csvItemList);

		itemService.saveOrUpdateCSVItemList(csvItemList);
	}

	public void exportItems(final String fileStr) throws IOException {
		log.debug("fileStr: " + fileStr);

		Path path = Paths.get(fileStr);

		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			final String treeViewStr = itemService.getItemDetails();
			writer.write(treeViewStr);
		}
	}
}
