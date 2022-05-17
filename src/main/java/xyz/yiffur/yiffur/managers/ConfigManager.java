/**
 * 
 */
package xyz.yiffur.yiffur.managers;

import java.io.File;

import xyz.yiffur.yiffur.Yiffur;
import xyz.yiffur.yiffur.mods.Module;
import xyz.yiffur.yiffur.mods.ModuleManager;
import xyz.yiffur.yiffur.settings.Setting;
import xyz.yiffur.yiffur.settings.impl.BooleanSetting;
import xyz.yiffur.yiffur.settings.impl.DecimalSetting;
import xyz.yiffur.yiffur.settings.impl.IntegerSetting;
import xyz.yiffur.yiffur.settings.impl.LongSetting;
import xyz.yiffur.yiffur.settings.impl.ModeSetting;
import xyz.yiffur.yiffur.utils.MiscUtils;

/**
 * @author lavaflowglow
 *
 */
public class ConfigManager {
	
	/**
	 * Saves the config to a file
	 * @param saveFile The save file for the config
	 */
	public static void save(File saveFile) {
		
		// Serialize all the modules and settings in a format that our client can read
		String config = "";
		for (Module module : ModuleManager.getModules()) {
			config += (config.isEmpty() ? "" : String.valueOf(Yiffur.UNIT_SEPARATOR)) + module.getName() + String.valueOf(Yiffur.GROUP_SEPARATOR) 
				+ module.getCategory() + String.valueOf(Yiffur.GROUP_SEPARATOR) 
				+ module.isEnabled();
			for (Setting setting : module.getSettings()) {
				
				// Get the setting type
				String settingType = MiscUtils.getSettingType(setting);
				
				// Get the setting value
				String settingValue = "";
				if (setting instanceof BooleanSetting) {
					settingValue = ((BooleanSetting)setting).isEnabled() + "";
				}
				else if (setting instanceof DecimalSetting) {
					settingValue = ((DecimalSetting)setting).getValue() + "";
				}
				else if (setting instanceof IntegerSetting) {
					settingValue = ((IntegerSetting)setting).getValue() + "";
				}
				else if (setting instanceof LongSetting) {
					settingValue = ((LongSetting)setting).getValue() + "";
				}
				else if (setting instanceof ModeSetting) {
					settingValue = ((ModeSetting)setting).getMode() + "";
				}
				
				config += String.valueOf(Yiffur.GROUP_SEPARATOR) + setting.getName() + String.valueOf(Yiffur.RECORD_SEPARATOR) + settingType + String.valueOf(Yiffur.RECORD_SEPARATOR) + settingValue;
			}
		}
		
		// Save the config to a file
		FileManager.writeStringToFile(saveFile, config);
		
	}
	
	public static void load(File file) {
		
		// Load the file
		String config = FileManager.readStringFromFile(file);
		
	}
	
}
