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
	
	/**
	 * @param name
	 * @param defaultMode The default mode
	 * @param modes The modes, must include the default mode
	 */
	public ModeSetting(String name, String defaultMode, String... modes) {
		this(null, name, defaultMode, modes);
	}

	private List<String> modes = new ArrayList<>();
	private int modeIndex;
	private double clickguiToggleStatus = 0;
	private boolean clickguiExtended = false;
	
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
	 * @param mode The mode that you want to check
	 * @return Whether or not the passed in mode is the current mode
	 */
	public boolean is(String mode) {
		return modes.get(modeIndex).equals(mode);
	}
	
	/**
	 * @return the clickguiToggleStatus
	 */
	public double getClickguiToggleStatus() {
		return clickguiToggleStatus;
	}

	/**
	 * @param clickguiToggleStatus the clickguiToggleStatus to set
	 */
	public void setClickguiToggleStatus(double clickguiToggleStatus) {
		this.clickguiToggleStatus = clickguiToggleStatus;
	}

	/**
	 * @return the clickguiExtended
	 */
	public boolean isClickguiExtended() {
		return clickguiExtended;
	}

	/**
	 * @param clickguiExtended the clickguiExtended to set
	 */
	public void setClickguiExtended(boolean clickguiExtended) {
		this.clickguiExtended = clickguiExtended;
	}

}
