/**
 * 
 */
package cafe.kagu.kagu.mods.impl.visual;

import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.impl.BooleanSetting;
import cafe.kagu.kagu.settings.impl.ModeSetting;

/**
 * @author lavaflowglow
 *
 */
public class ModHud extends Module {
	
	public ModHud() {
		super("Hud", Category.VISUAL);
		setSettings(hudEnabled, visual3dEnabled, arraylistAnimation, arraylistColors);
	}
	
	public BooleanSetting hudEnabled = new BooleanSetting("HUD Enabled", true);
	public BooleanSetting visual3dEnabled = new BooleanSetting("3D Visuals Enabled", true);
	
	// ArrayList options
	public ModeSetting arraylistAnimation = new ModeSetting("ArrayList Animation", "Squeeze", "Squeeze", "Slide");
	public ModeSetting arraylistColors = new ModeSetting("ArrayList Colors", "White", "White", "Category Colors");
	
	@Override
	public double getArraylistAnimation() {
		return 0;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	@Override
	public boolean isDisabled() {
		return false;
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
