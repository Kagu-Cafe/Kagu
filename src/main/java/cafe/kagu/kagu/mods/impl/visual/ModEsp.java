/**
 * 
 */
package cafe.kagu.kagu.mods.impl.visual;

import org.lwjgl.opengl.GL11;

import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.impl.Event3DRender;
import cafe.kagu.kagu.eventBus.impl.EventEntitiesRender;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.impl.BooleanSetting;
import cafe.kagu.kagu.utils.DrawUtils3D;
import cafe.kagu.kagu.utils.StencilUtil;
import net.minecraft.client.renderer.GlStateManager;

/**
 * @author lavaflowglow
 *
 */
public class ModEsp extends Module {
	
	public ModEsp() {
		super("ESP", Category.VISUAL);
		setSettings(colorMaskRed, colorMaskGreen, colorMaskBlue);
	}
	
	private BooleanSetting colorMaskRed = new BooleanSetting("Color mask red", true);
	private BooleanSetting colorMaskGreen = new BooleanSetting("Color mask green", true);
	private BooleanSetting colorMaskBlue = new BooleanSetting("Color mask blue", true);
	
	@EventHandler
	private Handler<EventEntitiesRender> onRender3D = e -> {
		
		if (e.isPre()) {
			
			// Setup stencil
			StencilUtil.enableStencilTest();
			StencilUtil.enableWrite();
			StencilUtil.clearStencil();
			
			// Make color mask changes so entities render with them
			GlStateManager.colorMask(colorMaskRed.isEnabled(), colorMaskGreen.isEnabled(), colorMaskBlue.isEnabled(), true);
			
		}
		else {
			
			GlStateManager.colorMask(true, true, true, true);
			
			StencilUtil.disableWrite();
			StencilUtil.setTestOutcome(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
			StencilUtil.glStencilFunc(GL11.GL_NOTEQUAL, 1);
			
			StencilUtil.disableStencilTest();
		}
		
	};
	
}
