/**
 * 
 */
package xyz.yiffur.yiffur.mods;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import xyz.yiffur.yiffur.eventBus.EventBus;
import xyz.yiffur.yiffur.mods.impl.move.ModMoonJump;

/**
 * @author lavaflowglow
 *
 */
public class ModuleManager {

	private static Logger logger = LogManager.getLogger();

	// All the modules in the client
	public static ModMoonJump modMoonJump = new ModMoonJump();

	// An array of all the modules in the client
	private static Module[] modules = new Module[] {
			modMoonJump
	};

	/**
	 * Called at the start of the client
	 */
	public static void start() {
		logger.info("Loading modules...");
		
		for (Module module : modules) {
			module.initialize(); // Initialize the module
			EventBus.setSubscriber(module, true); // Subscribe any listeners to the event bus
		}
		
		logger.info("Loaded all the modules");
	}

	/**
	 * @return the modules
	 */
	public static Module[] getModules() {
		return modules;
	}

}
