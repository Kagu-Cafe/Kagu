/**
 * 
 */
package xyz.yiffur.yiffur.settings;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import xyz.yiffur.yiffur.Yiffur;
import xyz.yiffur.yiffur.settings.impl.ModeSetting;

/**
 * @author lavaflowglow
 *
 */
public abstract class Setting {

	/**
	 * @param dependsOn If the setting defined in the variable is hidden than this one will be hidden too, can be null
	 * @param name The name of the setting
	 */
	public Setting(Setting dependsOn, String name) {
		
		// We use these two chars when saving and loading files, if the config name contains them it could create issues
		if (name.contains(String.valueOf(Yiffur.UNIT_SEPARATOR)) || name.contains(String.valueOf(Yiffur.RECORD_SEPARATOR)) || name.contains(String.valueOf(Yiffur.GROUP_SEPARATOR))) {
			logger.error("Name of setting (" + name + ") contains a forbidden character, please refrain from using the unit and record separator character when naming modules as they break file loading");
			System.exit(0);
			return;
		}
		
		this.dependsOn = dependsOn;
		this.name = name;
	}

	private String name, requiredMode;
	private Setting dependsOn;
	private boolean hidden = false;
	
	private static Logger logger = LogManager.getLogger();
	
	/**
	 * If the setting that this setting depends on is an instance of the mode
	 * setting, you can set a required mode where the setting stays hidden unless the
	 * depends on setting is showing and is on that current mode
	 * @param mode
	 * @return
	 */
	public Setting setRequiredDependsOnMode(String mode) {
		this.requiredMode = mode;
		return this;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		
		// We use these two chars when saving and loading files, if the config name contains them it could create issues
		if (name.contains(String.valueOf(Yiffur.UNIT_SEPARATOR)) || name.contains(String.valueOf(Yiffur.RECORD_SEPARATOR)) || name.contains(String.valueOf(Yiffur.GROUP_SEPARATOR))) {
			logger.error("Name of setting (" + name + ") contains a forbidden character, please refrain from using the unit and record separator character when naming modules as they break file loading");
			System.exit(0);
			return;
		}
		
		this.name = name;
	}

	/**
	 * @return the dependsOn
	 */
	public Setting getDependsOn() {
		return dependsOn;
	}

	/**
	 * @param dependsOn the dependsOn to set
	 */
	public void setDependsOn(Setting dependsOn) {
		this.dependsOn = dependsOn;
	}

	/**
	 * @return the hidden
	 */
	public boolean isHidden() {
		return dependsOn == null ? hidden
				: ((dependsOn.isHidden() || (dependsOn instanceof ModeSetting && requiredMode != null
						&& !((ModeSetting) dependsOn).is(requiredMode))) || hidden);
	} 

	/**
	 * @param hidden the hidden to set
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
}
