/**
 * 
 */
package xyz.yiffur.yiffur.settings.impl;

import xyz.yiffur.yiffur.settings.Setting;

/**
 * @author lavaflowglow
 *
 */
public class BooleanSetting extends Setting {

	/**
	 * @param dependsOn The setting that this one depends on, can be null
	 * @param name      The name of the module
	 * @param enabled   Whether the setting is enabled by default or not
	 */
	public BooleanSetting(Setting dependsOn, String name, boolean enabled) {
		super(dependsOn, name);
		this.enabled = enabled;
	}
	
	/**
	 * @param name      The name of the module
	 * @param enabled   Whether the setting is enabled by default or not
	 */
	public BooleanSetting(String name, boolean enabled) {
		this(null, name, enabled);
	}
	
	private boolean enabled;
	private double clickguiToggleStatus = 0;

	/**
	 * @return True if the client is enabled, false otherwise
	 */
	public boolean isEnabled() {
		return enabled;
	}
	
	/**
	 * @return True if the client is disabled, false otherwise
	 */
	public boolean isDisabled() {
		return !enabled;
	}
	
	/**
	 * Disables the setting, does nothing if the setting is already disabled
	 */
	public void disable() {
		enabled = false;
	}
	
	/**
	 * Enables the setting, does nothing if the setting is already enabled
	 */
	public void enable() {
		enabled = true;
	}
	
	/**
	 * Flips the state of the setting
	 */
	public void toggle() {
		enabled = !enabled;
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

}
