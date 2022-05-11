/**
 * 
 */
package xyz.yiffur.yiffur.mods;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import xyz.yiffur.yiffur.eventBus.EventBus;
import xyz.yiffur.yiffur.mods.impl.move.ModMoonJump;
import xyz.yiffur.yiffur.mods.impl.yiff.ModTest;

/**
 * @author lavaflowglow
 *
 */
public class ModuleManager {

	private static Logger logger = LogManager.getLogger();

	// All the modules in the client
	public static ModMoonJump modMoonJump = new ModMoonJump();
	public static ModTest modTest = new ModTest();

	// An array of all the modules in the client
	private static final Module[] MODULES = new Module[] {
			modMoonJump,
			modTest
	};

	/**
	 * Called at the start of the client
	 */
	public static void start() {
		logger.info("Loading modules...");
		
		for (Module module : MODULES) {
			module.initialize(); // Initialize the module
			EventBus.setSubscriber(module, true); // Subscribe any listeners to the event bus
		}
		
		logger.info("Loaded all the modules");
	}

	/**
	 * @return the modules
	 */
	public static Module[] getModules() {
		return MODULES;
	}

}
