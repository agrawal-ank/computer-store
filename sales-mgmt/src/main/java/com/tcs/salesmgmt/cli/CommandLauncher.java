package com.tcs.salesmgmt.cli;

import java.io.Console;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Responsible for Launching CommandLine Prompt, Validate Command and Invoke
 * Command Execution.
 **/ 
@Component
public class CommandLauncher {
	Log log = LogFactory.getLog(CommandLauncher.class);

	@Autowired
	CommandExecutor executor;

	public void launchCommandPrompt() {
		Console console = System.console();
		console.printf("%s\n", welcomeMsg);
		console.printf("%s\n", helpMsg);

		String cmdStr = null;

		while (true) {
			try {
				cmdStr = console.readLine(cmdPrompt);
				String[] commandTokens = cmdStr.split("\\s+");
				log.debug("cmdStr: " + cmdStr + "\ncommandTokens: " + commandTokens);

				boolean isCommandValid = validateCommand(commandTokens);
				if (!isCommandValid) {
					console.printf("%s\n%s\n", invalidCmdMsg, cmdStr);
					console.printf("%s\n", helpMsg);
				} else {
					if (cmdStr.equalsIgnoreCase(Command.EXIT.name())) {
						return;
					}
					executeCommand(commandTokens);
				}
			} catch (NoSuchFileException e) {
				console.printf("%s\n", "No Such File: " + e.getMessage());
				log.error(e.getMessage(), e);
			} catch (RuntimeException e) {
				console.printf("%s\n", e.getMessage());
				log.error(e.getMessage(), e);
			} catch (Exception e) {
				console.printf("%s\n", e.getMessage());
				log.error(e.getMessage(), e);
			}
		}
	}

	private boolean validateCommand(String[] commandTokens) {
		boolean isCommandValid = false;

		if (commandTokens.length > 2) {
			return false;
		}

		List<String> validCommands = Command.getValidCommands();
		if (!validCommands.contains(commandTokens[0].toUpperCase())) {
			return false;
		}

		Command command = Command.valueOf(commandTokens[0].toUpperCase());
		switch (command) {
		case IMPORT:
		case EXPORT:
			isCommandValid = commandTokens.length == 2;
			break;
		case EXIT:
			isCommandValid = commandTokens.length == 1;
			break;
		default:
			isCommandValid = false;
		}
		
		return isCommandValid;
	}

	private void executeCommand(String[] commandTokens) throws FileNotFoundException, IOException {
		Command command = Command.valueOf(commandTokens[0].toUpperCase());
		switch (command) {
		case IMPORT:
			executor.importItems(commandTokens[1]);
			break;
		case EXPORT:
			executor.exportItems(commandTokens[1]);
			break;
		default:
			return;
		}
	}

	final String helpMsg = "\nValid Commands:\n" + "  import <csv-file>\n" + "  export <file>\n" + "  exit\n";
	final String invalidCmdMsg = "Invalid Command!";
	final String cmdPrompt = "Command>";
	final String welcomeMsg = "\n*************************************" 
			+ "\nWelcome to Computer Store Application"
			+ "\n*************************************\n";
}