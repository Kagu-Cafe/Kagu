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

	private boolean enabled;

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
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	/**
	 * Flips the state of the setting
	 */
	public void toggle() {
		enabled = !enabled;
	}
	
}
