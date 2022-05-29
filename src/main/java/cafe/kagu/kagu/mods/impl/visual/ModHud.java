/**
 * 
 */
package cafe.kagu.kagu.mods.impl.visual;

import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.impl.ModeSetting;

/**
 * @author lavaflowglow
 *
 */
public class ModHud extends Module {
	
	public ModHud() {
		super("Hud", Category.VISUAL);
		setSettings(arraylistAnimation);
	}
	
	public ModeSetting arraylistAnimation = new ModeSetting("Arraylist Animation", "Squeeze", "Squeeze", "Slide");
	
	@Override
	public boolean isEnabled() {
		return false;
	}
	
	@Override
	public boolean isDisabled() {
		return true;
	}
	
	@Override
	public void toggle() {
		
	}
	
	@Override
	public void enable() {
		
	}
	
	@Override
	public void disable() {
		
	}
	
}
