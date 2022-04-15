/**
 * 
 */
package xyz.yiffur.yiffur.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import xyz.yiffur.yiffur.Yiffur;
import xyz.yiffur.yiffur.eventBus.Subscriber;
import xyz.yiffur.yiffur.eventBus.YiffEvents;
import xyz.yiffur.yiffur.eventBus.impl.Event2DRender;
import xyz.yiffur.yiffur.font.FontRenderer;
import xyz.yiffur.yiffur.font.FontUtils;
import xyz.yiffur.yiffur.mods.Module;
import xyz.yiffur.yiffur.mods.ModuleManager;
import xyz.yiffur.yiffur.utils.UiUtils;

/**
 * @author lavaflowglow
 *
 */
public class Hud {
	
	@YiffEvents
	public Subscriber<Event2DRender> renderHud = e -> {
		
		// We only want to render on the post event
		if (e.isPre()) {
			return;
		}
		
		// mc and sr
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution sr = new ScaledResolution(mc);
		
		// The fonts used
		FontRenderer mainFr = FontUtils.ROBOTO_LIGHT_10;
		
		// Render hud
		mainFr.drawString(Yiffur.getName() + " v" + Yiffur.getVersion(), 4, 4, 0x80000000);
		mainFr.drawString(Yiffur.getName() + " v" + Yiffur.getVersion(), 3, 3, -1);
		
		// Arraylist
		drawArraylist(mc, sr);
		
	};
	
	/**
	 * Renders the arraylist
	 * @param mc Minecraft
	 * @param sr Scaled Resolution
	 */
	private static void drawArraylist(Minecraft mc, ScaledResolution sr) {
		
		// Vars
		String separator = " - ";
		FontRenderer moduleFr = FontUtils.ROBOTO_REGULAR_10;
		FontRenderer infoFr = FontUtils.ROBOTO_LIGHT_10;
		List<Module> mods = new ArrayList<Module>(Arrays.asList(ModuleManager.getModules()));
		double rightPad = 2;
		double topPad = 1;
		int index = 0;
		
		// Sort mods
		mods = mods.stream().filter(mod -> mod.isEnabled()).collect(Collectors.toList());
		mods.sort(Comparator.comparingDouble(module -> moduleFr.getStringWidth(module.getName()) + infoFr.getStringWidth(module.getInfoAsString(separator))));
		Collections.reverse(mods);
		
		// Draw
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		GlStateManager.translate(-rightPad, topPad, 0);
		for (Module module : mods) {
			
			// Module info string
			String info = module.getInfoAsString(separator);
			double infoLength = infoFr.getStringWidth(info);
			
			// Module name
			moduleFr.drawString(module.getName(), sr.getScaledWidth() - moduleFr.getStringWidth(module.getName()) - infoLength, index * moduleFr.getFontHeight(), -1);
			
			// Module info
			infoFr.drawString(info, sr.getScaledWidth() - infoLength, index * moduleFr.getFontHeight(), -1);
			
			// Used to calculate y
			index++;
		}
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
		
	}
	
}
