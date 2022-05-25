/**
 * 
 */
package cafe.kagu.kagu.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.opengl.GL11;

import cafe.kagu.kagu.Kagu;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.impl.Event2DRender;
import cafe.kagu.kagu.font.FontRenderer;
import cafe.kagu.kagu.font.FontUtils;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.mods.ModuleManager;
import cafe.kagu.kagu.utils.StencilUtil;
import cafe.kagu.kagu.utils.UiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

/**
 * @author lavaflowglow
 *
 */
public class Hud {
	
	@EventHandler
	public Handler<Event2DRender> renderHud = e -> {
		
		// We only want to render on the post event
		if (e.isPre()) {
			return;
		}
		
		// mc and sr
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution sr = new ScaledResolution(mc);
		
		// The fonts used
		FontRenderer mainFr = FontUtils.STRATUM2_MEDIUM_13;
		
		// Render hud
		mainFr.drawString(Kagu.getName() + " v" + Kagu.getVersion(), 3, 4, 0x80000000);
		mainFr.drawString(Kagu.getName() + " v" + Kagu.getVersion(), 2, 3, -1);
		
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
		FontRenderer moduleFr = FontUtils.OPEN_SANS_REGULAR_10_AA;
		FontRenderer infoFr = FontUtils.OPEN_SANS_THIN_10_AA;
		List<Module> mods = new ArrayList<Module>(Arrays.asList(ModuleManager.getModules()));
		double rightPad = 2;
		double topPad = 0.5;
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
			moduleFr.drawString(module.getName(), sr.getScaledWidth() - moduleFr.getStringWidth(module.getName()) - infoLength, index * moduleFr.getFontHeight(), -1, true);
			
			// Module info
			infoFr.drawString(info, sr.getScaledWidth() - infoLength, index * moduleFr.getFontHeight(), -1, true);
			
			// Used to calculate y
			index++;
		}
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
		
	}
	
}
