/**
 * 
 */
package xyz.yiffur.yiffur.settings.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import xyz.yiffur.yiffur.settings.Setting;

/**
 * @author lavaflowglow
 *
 */
public class ModeSetting extends Setting {

	/**
	 * @param dependsOn
	 * @param name
	 * @param defaultMode The default mode
	 * @param modes The modes, must include the default mode
	 */
	public ModeSetting(Setting dependsOn, String name, String defaultMode, String... modes) {
		super(dependsOn, name);
		this.modes.addAll(Arrays.asList(modes));
	}

	private List<String> modes = new ArrayList<>();
	private int modeIndex;

	/**
	 * @return the modes
	 */
	public List<String> getModes() {
		return modes;
	}

	/**
	 * @param mode Sets the mode
	 */
	public void setMode(String mode) {
		modeIndex = modes.indexOf(mode) == -1 ? 0 : modes.indexOf(mode);
	}
	
	/**
	 * @return The current mode
	 */
	public String getMode() {
		return modeIndex >= modes.size() ? modes.get(modeIndex = 0) : modes.get(modeIndex);
	}
	
	/**
	 * 
	 * @param mode The mode that you want to check
	 * @return Whether or not the passed in mode is the current mode
	 */
	public boolean is(String mode) {
		return modes.get(modeIndex).equals(mode);
	}
	
}
