/**
 * 
 */
package xyz.yiffur.yiffur.managers;

import java.io.File;

import xyz.yiffur.yiffur.Yiffur;
import xyz.yiffur.yiffur.mods.Module;
import xyz.yiffur.yiffur.mods.Module.Category;
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
			config += (config.isEmpty() ? "" : Yiffur.UNIT_SEPARATOR) + module.getName() + Yiffur.GROUP_SEPARATOR
				+ module.getCategory() + Yiffur.GROUP_SEPARATOR
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
				
				config += Yiffur.GROUP_SEPARATOR + setting.getName()
						+ Yiffur.RECORD_SEPARATOR + setting.isHidden()
						+ Yiffur.RECORD_SEPARATOR + settingType
						+ Yiffur.RECORD_SEPARATOR + settingValue;
				
			}
		}
		
		// Save the config to a file
		FileManager.writeStringToFile(saveFile, config);
		
	}
	
	/**
	 * Loads configs from a file
	 * @param file The file to load the config from
	 */
	public static void load(File file) {
		
		// Load the file
		String config = FileManager.readStringFromFile(file);
		
		String[] modulesString = config.split(Yiffur.UNIT_SEPARATOR);
		for (String moduleString : modulesString) {
			
			String[] moduleSplit = moduleString.split(Yiffur.GROUP_SEPARATOR);
			
			// Name and category
			String name = moduleSplit[0];
			Category category = Category.valueOf(moduleSplit[1]);
			boolean enabled = moduleSplit[2].equals("true");
			
			// Find the module
			for (Module module : ModuleManager.getModules()) {
				if (!(module.getCategory() == category && module.getName().equals(name)))
					continue;
				
				// Disable if enabled
				module.disable();
				
				// Load settings
				if (moduleSplit.length > 3) for (int settingsIndex = 3; settingsIndex < moduleSplit.length; settingsIndex++) {
					String settingString = moduleSplit[settingsIndex];
					String[] settingSplit = settingString.split(Yiffur.RECORD_SEPARATOR);
					
					// Load the setting info
					String settingName = settingSplit[0];
					String settingHidden = settingSplit[1];
					String settingType = settingSplit[2];
					String settingValue = settingSplit[3];
					
					// Find setting and load the value
					for (Setting setting : module.getSettings()) {
						if (!(setting.getName().equals(settingName) && MiscUtils.getSettingType(setting).equals(settingType)))
							continue;
						
						// Set hidden
						setting.setHidden(settingHidden.equals("true"));
						
						switch (settingType) {
						case "bool":{
							if (settingValue.equals("true")) {
								((BooleanSetting)setting).enable();
							}else {
								((BooleanSetting)setting).enable();
							}
						}break;
						
						case "dec":{
							((DecimalSetting)setting).setValue(Double.parseDouble(settingValue));
						}break;
						
						case "int":{
							((IntegerSetting)setting).setValue(Integer.parseInt(settingValue));
						}break;
						
						case "long":{
							((LongSetting)setting).setValue(Long.parseLong(settingValue));
						}break;
						
						case "mode":{
							((ModeSetting)setting).setMode(settingValue);
						}break;

						default:
							break;
						}
					}
					
				}
				
				// Set the module to the required state
				if (enabled) {
					module.enable();
				}else {
					module.disable();
				}
				
			}
			
		}
		
	}
	
}
