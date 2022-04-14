/**
 * 
 */
package xyz.yiffur.yiffur.settings;

/**
 * @author lavaflowglow
 *
 */
public abstract class Setting {

	/**
	 * 
	 * @param dependsOn If the setting defined in the variable is hidden than this
	 *                  one will be hidden too, can be null
	 * @param name      The name of the setting
	 */
	public Setting(Setting dependsOn, String name) {

	}

	private String name;
	private Setting dependsOn;
	private boolean hidden = false;

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
		return hidden;
	}

	/**
	 * @param hidden the hidden to set
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

}
