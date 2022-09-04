/**
 * 
 */
package cafe.kagu.kagu.utils;

import cafe.kagu.kagu.eventBus.EventBus;
import cafe.kagu.kagu.ui.clickgui.GuiCsgoClickgui;
import net.minecraft.client.Minecraft;

/**
 * @author lavaflowglow
 *
 */
public class ClickGuiUtils {
	
	private static Minecraft mc = Minecraft.getMinecraft();
	
	/**
	 * Called at the start of the client
	 */
	public static void start() {
		
	}
	
	/**
	 * @return true if the player is in the clickgui, otherwise false
	 */
	public static boolean isInClickGui() {
		return mc.getCurrentScreen() instanceof GuiCsgoClickgui;
	}
	
}
