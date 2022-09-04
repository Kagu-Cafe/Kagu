/**
 * 
 */
package cafe.kagu.kagu.mods.impl.visual;

import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.impl.ModeSetting;
import cafe.kagu.kagu.ui.clickgui.GuiCsgoClickgui;
import cafe.kagu.kagu.utils.ClickGuiUtils;

/**
 * @author lavaflowglow
 *
 */
public class ModClickGui extends Module {
	
	public ModClickGui() {
		super("ClickGui", Category.VISUAL);
	}
	
	private ModeSetting mode = new ModeSetting("Mode", "CS:GO", "CS:GO", "Dropdown");
	
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
		else if (ClickGuiUtils.isInClickGui()) {
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
