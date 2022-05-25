/**
 * 
 */
package cafe.kagu.kagu.utils;

import cafe.kagu.kagu.settings.Setting;
import cafe.kagu.kagu.settings.impl.BooleanSetting;
import cafe.kagu.kagu.settings.impl.DoubleSetting;
import cafe.kagu.kagu.settings.impl.IntegerSetting;
import cafe.kagu.kagu.settings.impl.LongSetting;
import cafe.kagu.kagu.settings.impl.ModeSetting;
import net.minecraft.util.ChatComponentText;

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
		else if (setting instanceof DoubleSetting) {
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
	
	/**
	 * Removes mc color formatting codes from a string
	 * @param input The input to remove codes from
	 * @return The input with all formatting codes removed
	 */
	public static String removeFormatting(String input) {
		String output = "";
		boolean removeNext = false;
		for (char c : input.toCharArray()) {
			if (c == '§') {
				removeNext = true;
			}
			else if (!removeNext) {
				output += c;
			}else {
				removeNext = false;
			}
		}
		return output;
	}
	
}
