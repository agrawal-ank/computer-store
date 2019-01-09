package com.tcs.salesmgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.tcs.salesmgmt.cli.CommandLauncher;

@SpringBootApplication
public class SalesMgmtApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SalesMgmtApplication.class, args);
		
		CommandLauncher commandLauncher = context.getBean(CommandLauncher.class);
		commandLauncher.launchCommandPrompt();
	}
}

