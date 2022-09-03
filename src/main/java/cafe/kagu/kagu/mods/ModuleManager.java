/**
 * 
 */
package cafe.kagu.kagu.mods;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cafe.kagu.kagu.eventBus.EventBus;
import cafe.kagu.kagu.mods.impl.combat.ModBacktrack;
import cafe.kagu.kagu.mods.impl.combat.ModHitboxes;
import cafe.kagu.kagu.mods.impl.combat.ModKillAura;
import cafe.kagu.kagu.mods.impl.combat.ModReach;
import cafe.kagu.kagu.mods.impl.exploit.ModAntiCrash;
import cafe.kagu.kagu.mods.impl.exploit.ModBlink;
import cafe.kagu.kagu.mods.impl.exploit.ModCivBreak;
import cafe.kagu.kagu.mods.impl.exploit.ModCrasher;
import cafe.kagu.kagu.mods.impl.exploit.ModCreative64Stack;
import cafe.kagu.kagu.mods.impl.exploit.ModGroundClip;
import cafe.kagu.kagu.mods.impl.exploit.ModKeepSprintAfterCombat;
import cafe.kagu.kagu.mods.impl.exploit.ModNoHCollisionSlowdown;
import cafe.kagu.kagu.mods.impl.exploit.ModSpecialSlime;
import cafe.kagu.kagu.mods.impl.exploit.ModTimer;
import cafe.kagu.kagu.mods.impl.ghost.ModHideHud;
import cafe.kagu.kagu.mods.impl.ghost.ModObsProofEsp;
import cafe.kagu.kagu.mods.impl.ghost.ModObsProofUi;
import cafe.kagu.kagu.mods.impl.move.ModAntiVoid;
import cafe.kagu.kagu.mods.impl.move.ModFly;
import cafe.kagu.kagu.mods.impl.move.ModMoonJump;
import cafe.kagu.kagu.mods.impl.move.ModNoSlow;
import cafe.kagu.kagu.mods.impl.move.ModSafeWalk;
import cafe.kagu.kagu.mods.impl.move.ModScaffold;
import cafe.kagu.kagu.mods.impl.move.ModSpeed;
import cafe.kagu.kagu.mods.impl.move.ModSpider;
import cafe.kagu.kagu.mods.impl.move.ModSprint;
import cafe.kagu.kagu.mods.impl.move.ModStep;
import cafe.kagu.kagu.mods.impl.move.ModVelocity;
import cafe.kagu.kagu.mods.impl.player.ModAntiAim;
import cafe.kagu.kagu.mods.impl.player.ModAntiBot;
import cafe.kagu.kagu.mods.impl.player.ModChestStealer;
import cafe.kagu.kagu.mods.impl.player.ModDisabler;
import cafe.kagu.kagu.mods.impl.player.ModInventoryManager;
import cafe.kagu.kagu.mods.impl.player.ModNoFall;
import cafe.kagu.kagu.mods.impl.visual.ModAmbience;
import cafe.kagu.kagu.mods.impl.visual.ModAnimations;
import cafe.kagu.kagu.mods.impl.visual.ModCamera;
import cafe.kagu.kagu.mods.impl.visual.ModDistastefulEars;
import cafe.kagu.kagu.mods.impl.visual.ModChestEsp;
import cafe.kagu.kagu.mods.impl.visual.ModClickGui;
import cafe.kagu.kagu.mods.impl.visual.ModDebugBoundingBoxes;
import cafe.kagu.kagu.mods.impl.visual.ModEsp;
import cafe.kagu.kagu.mods.impl.visual.ModFunnyLimbs;
import cafe.kagu.kagu.mods.impl.visual.ModHud;
import cafe.kagu.kagu.mods.impl.visual.ModNormalZoomCam;
import cafe.kagu.kagu.mods.impl.visual.ModTargetHud;
import cafe.kagu.kagu.mods.impl.visual.ModViewModels;
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
	public static ModBacktrack modBacktrack = new ModBacktrack();
	public static ModReach modReach = new ModReach();
	public static ModHitboxes modHitboxes = new ModHitboxes();
	
	// Movement
	public static ModMoonJump modMoonJump = new ModMoonJump();
	public static ModSprint modSprint = new ModSprint();
	public static ModSpeed modSpeed = new ModSpeed();
	public static ModFly modFly = new ModFly();
	public static ModScaffold modScaffold = new ModScaffold();
	public static ModVelocity modVelocity = new ModVelocity();
	public static ModNoSlow modNoSlow = new ModNoSlow();
	public static ModStep modStep = new ModStep();
	public static ModAntiVoid modAntiVoid = new ModAntiVoid();
	public static ModSafeWalk modSafeWalk = new ModSafeWalk();
	public static ModSpider modSpider = new ModSpider();
	
	// Player
	public static ModAntiAim modAntiAim = new ModAntiAim();
	public static ModDisabler modDisabler = new ModDisabler();
	public static ModChestStealer modChestStealer = new ModChestStealer();
	public static ModInventoryManager modInventoryManager = new ModInventoryManager();
	public static ModAntiBot modAntiBot = new ModAntiBot();
	public static ModNoFall modNoFall = new ModNoFall();
	
	// Visual
	public static ModClickGui modClickGui = new ModClickGui();
	public static ModHud modHud = new ModHud();
	public static ModDebugBoundingBoxes modDebugBoundingBoxes = new ModDebugBoundingBoxes();
	public static ModViewModels modViewModels = new ModViewModels();
	public static ModAnimations modAnimations = new ModAnimations();
	public static ModChestEsp modChestEsp = new ModChestEsp();
	public static ModEsp modEsp = new ModEsp();
	public static ModNormalZoomCam modNormalZoomCam = new ModNormalZoomCam();
	public static ModDistastefulEars modDistastefulEars = new ModDistastefulEars();
	public static ModAmbience modAmbience = new ModAmbience();
	public static ModTargetHud modTargetHud = new ModTargetHud();
	public static ModCamera modCamera = new ModCamera();
	public static ModFunnyLimbs modFunnyLimbs = new ModFunnyLimbs();
	
	// Exploit
	public static ModCreative64Stack modCreative64Stack = new ModCreative64Stack();
	public static ModTest modTest = new ModTest();
	public static ModAntiCrash modAntiCrash = new ModAntiCrash();
	public static ModTimer modTimer = new ModTimer();
	public static ModBlink modBlink = new ModBlink();
	public static ModCrasher modCrasher = new ModCrasher();
	public static ModNoHCollisionSlowdown modNoHCollisionSlowdown = new ModNoHCollisionSlowdown();
	public static ModGroundClip modGroundClip = new ModGroundClip();
	public static ModSpecialSlime modSpecialSlime = new ModSpecialSlime();
	public static ModKeepSprintAfterCombat modKeepSprintAfterCombat = new ModKeepSprintAfterCombat();
	public static ModCivBreak modCivBreak = new ModCivBreak();
	
	// Ghost
	public static ModHideHud modHideHud = new ModHideHud();
	public static ModObsProofUi modObsProofUi = new ModObsProofUi();
	public static ModObsProofEsp modObsProofEsp = new ModObsProofEsp();
	
	// An array of all the modules in the client
	private static final Module[] MODULES = new Module[] {
			modClickGui,
			modAntiAim,
			modHud,
			modAnimations,
			modChestEsp,
			modEsp,
			modNormalZoomCam,
			modMoonJump,
			modSprint,
			modDisabler,
			modCreative64Stack,
			modDebugBoundingBoxes,
			modTest,
			modAntiCrash,
			modViewModels,
			modKillAura,
			modBacktrack,
			modTimer,
			modCrasher,
			modReach,
			modHitboxes,
			modNoHCollisionSlowdown,
			modSpeed,
			modDistastefulEars,
			modAmbience,
			modFly,
			modScaffold,
			modChestStealer,
			modGroundClip,
			modSpecialSlime,
			modVelocity,
			modInventoryManager,
			modAntiBot,
			modNoSlow,
			modKeepSprintAfterCombat,
			modCivBreak,
			modStep,
			modTargetHud,
			modNoFall,
			modAntiVoid,
			modHideHud,
			modCamera,
			modObsProofUi,
			modObsProofEsp,
			modSafeWalk,
			modFunnyLimbs,
			modSpider,
			
			modBlink
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
