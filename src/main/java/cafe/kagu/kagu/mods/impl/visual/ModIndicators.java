/**
 * 
 */
package cafe.kagu.kagu.mods.impl.visual;

import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.impl.EventRender2D;
import cafe.kagu.kagu.font.FontRenderer;
import cafe.kagu.kagu.font.FontUtils;
import cafe.kagu.kagu.managers.KeybindManager;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.mods.ModuleManager;
import cafe.kagu.kagu.settings.impl.BooleanSetting;
import cafe.kagu.kagu.ui.gui.GuiDefaultMainMenu;
import cafe.kagu.kagu.utils.MovementUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

/**
 * @author lavaflowglow
 *
 */
public class ModIndicators extends Module {
	
	public ModIndicators() {
		super("Indicators", Category.VISUAL);
	}
	
	@EventHandler
	private Handler<EventRender2D> onRender2D = e -> {
		FontRenderer indicatorFr = FontUtils.VERDANA_BOLD_10;
		ScaledResolution sr = new ScaledResolution(mc);
		Module[] modules = ModuleManager.getModules();
		int enabledIndicators = 0;
		for (Module module : modules) {
			if (KeybindManager.getKeybinds(module.getName()).length == 0 || module instanceof ModClickGui)
				continue;
			enabledIndicators++;
		}
		double textY = sr.getScaledHeight() / 2 - enabledIndicators * (indicatorFr.getFontHeight() / 2) - 1;
		for (Module module : modules) {
			if (KeybindManager.getKeybinds(module.getName()).length == 0 || module instanceof ModClickGui)
				continue;
			int darkColor = module.isEnabled() ? 0xff445622 : 0xff5c0c08;
			int color = module.isEnabled() ? 0xff89ac45 : 0xffb91912;
			indicatorFr.drawString(module.getName().toUpperCase(), 2.5, textY + 0.5, darkColor);
			indicatorFr.drawString(module.getName().toUpperCase(), 1.5, textY - 0.5, darkColor);
			indicatorFr.drawString(module.getName().toUpperCase(), 2.5, textY - 0.5, darkColor);
			indicatorFr.drawString(module.getName().toUpperCase(), 1.5, textY + 0.5, darkColor);
			indicatorFr.drawString(module.getName().toUpperCase(), 2, textY, color);
			textY += indicatorFr.getFontHeight();
		}
	};
	
}
