/**
 * 
 */
package cafe.kagu.kagu.utils;

import cafe.kagu.kagu.settings.Setting;
import cafe.kagu.kagu.settings.impl.BooleanSetting;
import cafe.kagu.kagu.settings.impl.DecimalSetting;
import cafe.kagu.kagu.settings.impl.IntegerSetting;
import cafe.kagu.kagu.settings.impl.LongSetting;
import cafe.kagu.kagu.settings.impl.ModeSetting;

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
