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
import cafe.kagu.kagu.mods.impl.player.ModAntiAim;
import cafe.kagu.kagu.mods.impl.player.ModNoFall;
import cafe.kagu.kagu.mods.impl.visual.ModAnimations;
import cafe.kagu.kagu.mods.impl.visual.ModChestEsp;
import cafe.kagu.kagu.mods.impl.visual.ModEsp;
import cafe.kagu.kagu.mods.impl.visual.ModNormalZoomCam;

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
	public static ModAntiAim modAntiAim = new ModAntiAim();
	
	// Visual
	public static ModAnimations modAnimations = new ModAnimations();
	public static ModChestEsp modChestEsp = new ModChestEsp();
	public static ModEsp modEsp = new ModEsp();
	public static ModNormalZoomCam modNormalZoomCam = new ModNormalZoomCam();
	
	// An array of all the modules in the client
	private static final Module[] MODULES = new Module[] {
			
			// Misc
			modTest,
			
			// Visual
			modAnimations,
			modChestEsp,
			modEsp,
			modNormalZoomCam,
			
			// Movement
			modMoonJump,
			modSprint,
			
			// Player
			modNoFall,
			modAntiAim
			
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
