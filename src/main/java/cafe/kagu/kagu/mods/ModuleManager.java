/**
 * 
 */
package cafe.kagu.kagu.mods;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cafe.kagu.kagu.eventBus.EventBus;
import cafe.kagu.kagu.mods.impl.misc.ModTest;
import cafe.kagu.kagu.mods.impl.move.ModMoonJump;
import cafe.kagu.kagu.mods.impl.move.ModSprint;
import cafe.kagu.kagu.mods.impl.player.ModNoFall;
import cafe.kagu.kagu.mods.impl.visual.ModAnimations;

/**
 * @author lavaflowglow
 *
 */
public class ModuleManager {

	private static Logger logger = LogManager.getLogger();
	
	// All the modules in the client
	
	// Misc
	public static ModTest modTest = new ModTest();
	
	// Movement
	public static ModMoonJump modMoonJump = new ModMoonJump();
	public static ModSprint modSprint = new ModSprint();
	
	// Player
	public static ModNoFall modNoFall = new ModNoFall();
	
	// Visual
	public static ModAnimations modAnimations = new ModAnimations();
	
	// An array of all the modules in the client
	private static final Module[] MODULES = new Module[] {
			
			// Misc
			modTest,
			
			// Visual
			modAnimations,
			
			// Movement
			modMoonJump,
			modSprint,
			
			// Player
			modNoFall
			
	};

	/**
	 * Called at the start of the client
	 */
	public static void start() {
		logger.info("Loading modules...");
		
		for (Module module : MODULES) {
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
