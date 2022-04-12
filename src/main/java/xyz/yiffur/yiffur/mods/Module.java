/**
 * 
 */
package xyz.yiffur.yiffur.mods;

import xyz.yiffur.yiffur.settings.Setting;

/**
 * @author lavaflowglow
 *
 */
public abstract class Module {

	/**
	 * @param name The name of the module
	 */
	public Module(String name, Category category) {
		this.name = name;
		this.category = category;
	}

	/**
	 * Called when the client starts, it will add settings and do other shit
	 */
	public void initialize() {
	}

	/**
	 * @param settings An array of settings that will replace the current array of
	 *                 settings on the module
	 */
	protected void setSettings(Setting... settings) {
		this.settings = settings;
	}

	private String name = ""; // The name of the module
	private String[] info = new String[0]; // The info displayed next to the name on the arraylist
	private Setting[] settings = new Setting[0]; // Any settings that the module may have
	private Category category; // The category of the module
	private boolean enabled = false;

	/**
	 * @param info An array of strings that will be displayed next to the module on
	 *             the arraylist
	 */
	protected void setInfo(String... info) {
		this.info = info;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the info
	 */
	public String[] getInfo() {
		return info;
	}

	/**
	 * @return the settings
	 */
	public Setting[] getSettings() {
		return settings;
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @return true if enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @return true if disabled
	 */
	public boolean isDisabled() {
		return !enabled;
	}
	
	/**
	 * Toggle the module
	 */
	public void toggle() {
		enabled = !enabled;
	}
	
	/**
	 * Disables the module, does nothing if the module is already disabled
	 */
	public void disable() {
		enabled = false;
	}
	
	/**
	 * Enables the module, does nothing if the module is already enabled
	 */
	public void enable() {
		enabled = true;
	}
	
	/**
	 * @author lavaflowglow 
	 * Used to sort modules in the clickgui
	 */
	public static enum Category {
		COMBAT("Combat", 0xffe63e3e), 
		MOVEMENT("Movement", 0xff3dd979), 
		VISUAL("Visual", 0xff3dc2d9),
		OWO("OwO", 0xffff4af6), 
		MISC("Miscellaneous", 0xfffff34a);

		private String name;
		private int arraylistColor = -1;

		/**
		 * @param name           The display name of the category
		 * @param arraylistColor The color displayed on the arraylist
		 */
		Category(String name, int arraylistColor) {
			this.name = name;
			this.arraylistColor = arraylistColor;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the arraylistColor
		 */
		public int getArraylistColor() {
			return arraylistColor;
		}

	}

}
