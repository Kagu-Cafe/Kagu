/**
 * 
 */
package xyz.yiffur.yiffur.managers;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.yiffur.yiffur.Yiffur;
import xyz.yiffur.yiffur.eventBus.EventBus;
import xyz.yiffur.yiffur.eventBus.Subscriber;
import xyz.yiffur.yiffur.eventBus.YiffEvents;
import xyz.yiffur.yiffur.eventBus.impl.EventKeyUpdate;
import xyz.yiffur.yiffur.mods.Module;
import xyz.yiffur.yiffur.mods.ModuleManager;

/**
 * @author lavaflowglow
 *
 */
public class KeybindManager {
	
	/**
	 * Private because I don't want anything else creating an instance of this class
	 */
	private KeybindManager() {}
	
	private static Map<String, Integer[]> keybinds = new HashMap<>();
	
	/**
	 * Called when the client starts
	 */
	public static void start() {
		
		// Load the default keybinds if they exist
		if (FileManager.DEFAULT_KEYBIND_SET.exists()) {
			load(FileManager.DEFAULT_KEYBIND_SET);
		}
		
		// Subscribe to the key event so we can use the keybinds
		EventBus.setSubscriber(new KeybindManager(), true);
		
	}
	
	/**
	 * Adds a keybind
	 * @param module The name of the module
	 * @param keyCode The keycode to bind
	 */
	public static void addKeybind(String module, int keyCode) {
		module = module.toLowerCase();
		List<Integer> binds = new ArrayList<>(Arrays.asList(getKeybinds(module)));
		if (binds.contains(keyCode))
			return;
		binds.add(keyCode);
		if (keybinds.containsKey(module)) {
			keybinds.replace(module, binds.toArray(new Integer[0]));
		}else {
			keybinds.put(module, binds.toArray(new Integer[0]));
		}
		save(FileManager.DEFAULT_KEYBIND_SET);
	}
	
	/**
	 * Removes all keybinds for a module
	 * @param module The name of the module
	 */
	public static void removeKeybind(String module) {
		module = module.toLowerCase();
		keybinds.remove(module);
		save(FileManager.DEFAULT_KEYBIND_SET);
	}
	
	/**
	 * Returns an array of keybinds for the module
	 * @param module The name of the module
	 * @return
	 */
	public static Integer[] getKeybinds(String module) {
		module = module.toLowerCase();
		List<Integer> binds = new ArrayList<>();
		try {
			binds.addAll(Arrays.asList(keybinds.get(module)));
		} catch (Exception e) {}
		
		return binds.toArray(new Integer[0]);
	}
	
	/**
	 * Saves the keybinds to a file
	 * @param saveFile The file to save them to
	 */
	public static void save(File saveFile) {
		String save = "";
		for (String module : keybinds.keySet()) {
			save += (save.isEmpty() ? "" : String.valueOf(Yiffur.UNIT_SEPARATOR)) + module + String.valueOf(Yiffur.GROUP_SEPARATOR);
			String binds = "";
			for (Integer bind : getKeybinds(module)) {
				binds += (binds.isEmpty() ? "" : String.valueOf(Yiffur.RECORD_SEPARATOR)) + bind;
			}
			save += binds;
		}
		FileManager.writeStringToFile(saveFile, save);
	}
	
	/**
	 * Saves the keybinds from a file
	 * @param file The file to load them from
	 */
	public static void load(File file) {
		String fileData = FileManager.readStringFromFile(file);
		if (fileData.isEmpty())
			return;
		keybinds.clear();
		for (String bind : fileData.split(String.valueOf(Yiffur.UNIT_SEPARATOR))) {
			try {
				String[] bindArray = bind.split(String.valueOf(Yiffur.GROUP_SEPARATOR));
				
				List<Integer> keyCodes = new ArrayList<>();
				for (String code : bindArray[1].split(String.valueOf(Yiffur.RECORD_SEPARATOR))) {
					keyCodes.add(Integer.valueOf(code));
				}
				
				keybinds.put(bindArray[0], keyCodes.toArray(new Integer[0]));
			} catch (Exception e) {
				
			}
		}
	}
	
	/**
	 * Listener for the keybinds
	 */
	@YiffEvents
	private Subscriber<EventKeyUpdate> subscriber = e -> {
		if (e.isPost() || !e.isPressed())
			return;
		
		int keyCode = e.getKeyCode();
		for (Module module : ModuleManager.getModules()) {
			if (Arrays.asList(getKeybinds(module.getName())).contains(keyCode)) {
				module.toggle();
			}
		}
		
	};
	
}
