/**
 * 
 */
package xyz.yiffur.yiffur.managers;

import java.io.File;

import xyz.yiffur.yiffur.Yiffur;
import xyz.yiffur.yiffur.mods.Module;
import xyz.yiffur.yiffur.mods.ModuleManager;

/**
 * @author lavaflowglow
 *
 */
public class ConfigManager {
	
	public static void save(File saveFile) {
		String config = "";
		for (Module module : ModuleManager.getModules()) {
			config += (config.isEmpty() ? "" : String.valueOf(Yiffur.UNIT_SEPARATOR)) + module.getName();
		}
	}
	
}
