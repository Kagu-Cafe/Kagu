package cafe.kagu.kagu;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cafe.kagu.kagu.commands.CommandManager;
import cafe.kagu.kagu.eventBus.EventBus;
import cafe.kagu.kagu.eventBus.Event.EventPosition;
import cafe.kagu.kagu.eventBus.impl.EventCheatTick;
import cafe.kagu.kagu.font.FontUtils;
import cafe.kagu.kagu.managers.FileManager;
import cafe.kagu.kagu.managers.KeybindManager;
import cafe.kagu.kagu.mods.ModuleManager;
import cafe.kagu.kagu.ui.Hud;
import cafe.kagu.kagu.ui.clickgui.GuiCsgoClickgui;
import cafe.kagu.kagu.ui.gui.GuiMainMenu;
import net.minecraft.client.Minecraft;

/**
 * @author lavaflowglow
 *
 */
public class Kagu {
	
	private static String name = "Kagu";
	private static double version = 0;
	
	private static Logger logger = LogManager.getLogger();
	
//	public static final char UNIT_SEPARATOR = (char)31;
//	public static final char RECORD_SEPARATOR = (char)30;
//	public static final char GROUP_SEPARATOR = (char)29;
	
	public static final String UNIT_SEPARATOR = "﷽";
	public static final String RECORD_SEPARATOR = "👺";
	public static final String GROUP_SEPARATOR = "�?�";
	
	/**
	 * The start method, everything should be initialized here
	 */
	public static void start() {
		
		logger.info("Starting " + name + " v" + version + " :3");
		
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
		
		// Start the file manager
		logger.info("Starting the file manager...");
		FileManager.start();
		logger.info("Started the file manager");
		
		// Start the file manager
		logger.info("Starting the keybind manager...");
		KeybindManager.start();
		logger.info("Started the keybind manager");
		
		// Start the clickgui
		logger.info("Starting the clickgui...");
		GuiCsgoClickgui.getInstance().start();
		logger.info("Started the clickgui");
		
		// Start a cheat loop thread
		logger.info("Starting the cheat loop thread...");
		Thread cheatThread = new Thread(() -> {
			double tps = 64;
			while (true) {
				try {
					Thread.sleep((long) (1000 / tps));
				} catch (Exception e) {
					// TODO: handle exception
				}
				// Kagu hook
				{
					EventCheatTick eventCheatTick = new EventCheatTick(EventPosition.PRE);
					eventCheatTick.post();
					if (eventCheatTick.isCanceled())
						continue;
				}
				// Kagu hook
				{
					EventCheatTick eventCheatTick = new EventCheatTick(EventPosition.POST);
					eventCheatTick.post();
					if (eventCheatTick.isCanceled())
						continue;
				}
			}
		}, "Cheat loop");
		cheatThread.setDaemon(true);
		cheatThread.start();
		logger.info("Started the cheat loop thread");
		
		// Load fonts
		logger.info("Loading fonts...");
		FontUtils.start();
		logger.info("Loaded fonts");
		
		// Load the main menu
		logger.info("Loading the main menu...");
		GuiMainMenu.start();
		logger.info("Loaded the main menu");
		
		// Hook the hud
		logger.info("Hooking the hud...");
		EventBus.setSubscriber(new Hud(), true);
		logger.info("Hooked the hud");
		
		logger.info(name + " v" + version + " has been started");
		
	}

	/**
	 * @return the name
	 */
	public static String getName() {
		return name;
	}

	/**
	 * @return the version
	 */
	public static double getVersion() {
		return version;
	}

}
