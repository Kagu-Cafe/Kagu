/**
 * 
 */
package xyz.yiffur.yiffur.utils;

import xyz.yiffur.yiffur.settings.Setting;
import xyz.yiffur.yiffur.settings.impl.BooleanSetting;
import xyz.yiffur.yiffur.settings.impl.DecimalSetting;
import xyz.yiffur.yiffur.settings.impl.IntegerSetting;
import xyz.yiffur.yiffur.settings.impl.LongSetting;
import xyz.yiffur.yiffur.settings.impl.ModeSetting;

/**
 * @author lavaflowglow
 *
 */
public class MiscUtils {
	
	/**
	 * Gets the setting type
	 * @param setting The setting to get the type from
	 * @return The setting type
	 */
	public static String getSettingType(Setting setting) {
		if (setting instanceof BooleanSetting) {
			return "bool";
		}
		else if (setting instanceof DecimalSetting) {
			return "dec";
		}
		else if (setting instanceof IntegerSetting) {
			return "int";
		}
		else if (setting instanceof LongSetting) {
			return "long";
		}
		else if (setting instanceof ModeSetting) {
			return "mode";
		}
		return "";
	}
	
}
