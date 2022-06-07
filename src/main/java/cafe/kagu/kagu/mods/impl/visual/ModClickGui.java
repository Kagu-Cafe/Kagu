/**
 * 
 */
package cafe.kagu.kagu.mods.impl.visual;

import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.ui.clickgui.GuiCsgoClickgui;

/**
 * @author lavaflowglow
 *
 */
public class ModClickGui extends Module {
	
	public ModClickGui() {
		super("ClickGui", Category.VISUAL);
	}
	
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
		if (mc.getCurrentScreen() == null) {
			mc.displayGuiScreen(GuiCsgoClickgui.getInstance());
		}
		else if (mc.getCurrentScreen() instanceof GuiCsgoClickgui) {
			mc.displayGuiScreen(null);
		}
	}
	
	@Override
	public void enable() {
		
	}
	
	@Override
	public void disable() {
		
	}
	
}
