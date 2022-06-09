/**
 * 
 */
package cafe.kagu.kagu.mods;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cafe.kagu.kagu.eventBus.EventBus;
import cafe.kagu.kagu.mods.impl.combat.ModKillAura;
import cafe.kagu.kagu.mods.impl.exploit.ModAntiCrash;
import cafe.kagu.kagu.mods.impl.exploit.ModCreative64Stack;
import cafe.kagu.kagu.mods.impl.move.ModMoonJump;
import cafe.kagu.kagu.mods.impl.move.ModSprint;
import cafe.kagu.kagu.mods.impl.player.ModAntiAim;
import cafe.kagu.kagu.mods.impl.player.ModDisabler;
import cafe.kagu.kagu.mods.impl.visual.ModAnimations;
import cafe.kagu.kagu.mods.impl.visual.ModChestEsp;
import cafe.kagu.kagu.mods.impl.visual.ModClickGui;
import cafe.kagu.kagu.mods.impl.visual.ModEsp;
import cafe.kagu.kagu.mods.impl.visual.ModHud;
import cafe.kagu.kagu.mods.impl.visual.ModNormalZoomCam;
import cafe.kagu.kagu.mods.impl.visual.ModThirdPersonPlus;
import cafe.kagu.kagu.mods.impl.yiff.ModDebugBoundingBoxes;
import cafe.kagu.kagu.mods.impl.yiff.ModTest;

/**
 * @author lavaflowglow
 *
 */
public class ModuleManager {

	private static Logger logger = LogManager.getLogger();
	
	// All the modules in the client
	
	// Combat
	public static ModKillAura modKillAura = new ModKillAura();
	
	// Movement
	public static ModMoonJump modMoonJump = new ModMoonJump();
	public static ModSprint modSprint = new ModSprint();
	
	// Player
	public static ModAntiAim modAntiAim = new ModAntiAim();
	public static ModDisabler modDisabler = new ModDisabler();
	
	// Visual
	public static ModClickGui modClickGui = new ModClickGui();
	public static ModHud modHud = new ModHud();
	public static ModAnimations modAnimations = new ModAnimations();
	public static ModChestEsp modChestEsp = new ModChestEsp();
	public static ModEsp modEsp = new ModEsp();
	public static ModNormalZoomCam modNormalZoomCam = new ModNormalZoomCam();
	public static ModThirdPersonPlus modThirdPersonPlus = new ModThirdPersonPlus();
	
	// Exploit
	public static ModCreative64Stack modCreative64Stack = new ModCreative64Stack();
	public static ModAntiCrash modAntiCrash = new ModAntiCrash();
	
	// Development
	public static ModTest modTest = new ModTest();
	public static ModDebugBoundingBoxes modDebugBoundingBoxes = new ModDebugBoundingBoxes();
	
	// An array of all the modules in the client
	private static final Module[] MODULES = new Module[] {
			modClickGui,
			modKillAura,
			modHud,
			modAnimations,
			modChestEsp,
			modEsp,
			modNormalZoomCam,
			modMoonJump,
			modSprint,
			modAntiAim,
			modDisabler,
			modCreative64Stack,
			modDebugBoundingBoxes,
			modTest,
			modThirdPersonPlus,
			modAntiCrash
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
