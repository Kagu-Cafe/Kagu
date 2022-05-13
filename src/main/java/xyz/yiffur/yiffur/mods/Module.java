/**
 * 
 */
package xyz.yiffur.yiffur.mods;

import net.minecraft.client.Minecraft;
import xyz.yiffur.yiffur.eventBus.Event.EventPosition;
import xyz.yiffur.yiffur.eventBus.impl.EventModuleStateUpdate;
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
	public void initialize() {}
	
	/**
	 * Called when the module is enabled
	 */
	public void onEnable() {}
	
	/**
	 * Called when the module is disabled
	 */
	public void onDisable() {}

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
	private boolean enabled = false, isClickguiExtended = false;
	private double clickguiExtension = 0, clickguiToggle = 0;
	
	protected static Minecraft mc = Minecraft.getMinecraft(); // The minecraft instance used for the modules

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
	 * @param separator The separator to use
	 * @return the info array formatted as a string
	 */
	public String getInfoAsString(String separator) {
		String info = "";
		for (String str : getInfo()) {
			info += separator + str;
		}
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
	 * @return the clickguiExtension
	 */
	public double getClickguiExtension() {
		return clickguiExtension;
	}

	/**
	 * @param clickguiExtension the clickguiExtension to set
	 */
	public void setClickguiExtension(double clickguiExtension) {
		this.clickguiExtension = clickguiExtension;
	}

	/**
	 * @return the clickguiToggle
	 */
	public double getClickguiToggle() {
		return clickguiToggle;
	}

	/**
	 * @param clickguiToggle the clickguiToggle to set
	 */
	public void setClickguiToggle(double clickguiToggle) {
		this.clickguiToggle = clickguiToggle;
	}

	/**
	 * @return the isClickguiExtended
	 */
	public boolean isClickguiExtended() {
		return isClickguiExtended;
	}

	/**
	 * @param isClickguiExtended the isClickguiExtended to set
	 */
	public void setClickguiExtended(boolean isClickguiExtended) {
		this.isClickguiExtended = isClickguiExtended;
	}

	/**
	 * Toggles the module
	 */
	public void toggle() {
		// Yiffur hook
		{
			EventModuleStateUpdate eventModuleStateUpdate = new EventModuleStateUpdate(EventPosition.PRE, isEnabled(), isDisabled());
			eventModuleStateUpdate.post();
			if (eventModuleStateUpdate.isCanceled())
				return;
		}
		
		enabled = !enabled;
		
		// Calls the onEnable or onDisable method
		if (isEnabled()){
			onEnable();
		}else{
			onDisable();
		}
		
		// Yiffur hook
		{
			EventModuleStateUpdate eventModuleStateUpdate = new EventModuleStateUpdate(EventPosition.POST, isDisabled(), isEnabled());
			eventModuleStateUpdate.post();
			if (eventModuleStateUpdate.isCanceled())
				return;
		}
	}
	
	/**
	 * Disables the module, does nothing if the module is already disabled
	 */
	public void disable() {
		// Yiffur hook
		{
			EventModuleStateUpdate eventModuleStateUpdate = new EventModuleStateUpdate(EventPosition.PRE, isEnabled(), false);
			eventModuleStateUpdate.post();
			if (eventModuleStateUpdate.isCanceled())
				return;
		}
		
		enabled = false;
		
		// Yiffur hook
		{
			EventModuleStateUpdate eventModuleStateUpdate = new EventModuleStateUpdate(EventPosition.POST, isEnabled(), false);
			eventModuleStateUpdate.post();
			if (eventModuleStateUpdate.isCanceled())
				return;
		}
	}
	
	/**
	 * Enables the module, does nothing if the module is already enabled
	 */
	public void enable() {
		// Yiffur hook
		{
			EventModuleStateUpdate eventModuleStateUpdate = new EventModuleStateUpdate(EventPosition.PRE, isEnabled(), true);
			eventModuleStateUpdate.post();
			if (eventModuleStateUpdate.isCanceled())
				return;
		}
		
		enabled = true;
		
		// Yiffur hook
		{
			EventModuleStateUpdate eventModuleStateUpdate = new EventModuleStateUpdate(EventPosition.POST, isEnabled(), true);
			eventModuleStateUpdate.post();
			if (eventModuleStateUpdate.isCanceled())
				return;
		}
	}
	
	/**
	 * @author lavaflowglow 
	 * Used to sort modules in the clickgui
	 */
	public static enum Category {
		COMBAT("Combat", 0xffe63e3e), 
		MOVEMENT("Movement", 0xff3dd979), 
		VISUAL("Visual", 0xff3dc2d9),
		PLAYER("Player", 0xffd9883d),
		YIFF("OwO", 0xffff4af6), 
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
