package xyz.yiffur.yiffur;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import xyz.yiffur.yiffur.commands.CommandManager;
import xyz.yiffur.yiffur.eventBus.EventBus;
import xyz.yiffur.yiffur.mods.ModuleManager;

/**
 * @author lavaflowglow
 *
 */
public class Yiffur {
	
	private static Logger logger = LogManager.getLogger();
	
	/**
	 * The start method, everything should be initialized here
	 */
	public static void start() {
		
		logger.info("Starting Yiffur :3");
		
		// Starts the event bus
		logger.info("Starting the event bus...");
		EventBus.start();
		logger.info("Started the event bus");
		
		// Start the module manager
		logger.info("Starting the module manager...");
		ModuleManager.start();
		logger.info("Started the module manager");
		
		// Start the command manager
		logger.info("Starting the command manager...");
		CommandManager.start();
		logger.info("Started the command manager");
		
		logger.info("Yiffur has been started");
		
	}
	
}
