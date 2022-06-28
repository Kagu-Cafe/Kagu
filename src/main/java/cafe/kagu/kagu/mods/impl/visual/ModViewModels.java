/**
 * 
 */
package cafe.kagu.kagu.mods.impl.visual;

import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL20;

import com.mojang.authlib.GameProfile;

import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.impl.EventRender3D;
import cafe.kagu.kagu.eventBus.impl.EventTick;
import cafe.kagu.kagu.managers.FileManager;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.impl.BooleanSetting;
import cafe.kagu.kagu.settings.impl.IntegerSetting;
import cafe.kagu.kagu.utils.Shader;
import cafe.kagu.kagu.utils.Shader.ShaderType;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.shader.Framebuffer;

/**
 * @author lavaflowglow
 *
 */
public class ModViewModels extends Module {
	
	public ModViewModels() {
		super("ViewModel", Category.VISUAL);
		setSettings(overrideF3, desyncModels, desyncViewModelRed, desyncViewModelGreen, desyncViewModelBlue,
				desyncViewModelAlpha);
		
		// Load the shaders
		try {
			desyncModelShader = new Shader(ShaderType.FRAGMENT, FileManager.readStringFromFile(FileManager.COLOR_SHADER));
			desyncModelShader.create();
			desyncModelShader.link();
			desyncModelShader.createUniform("rgba");
		} catch (Exception e) {
			logger.error("Failed to load the color shader, this may cause issues", e);
		}
		
	}
	
	private BooleanSetting overrideF3 = new BooleanSetting("Override F3", true);
	private BooleanSetting desyncModels = new BooleanSetting("Desync Models", false);
	
	// Desync model colors
	private IntegerSetting desyncViewModelRed = (IntegerSetting) new IntegerSetting("Desync Red", 0, 0, 255, 1).setDependency(desyncModels::isEnabled);
	private IntegerSetting desyncViewModelGreen = (IntegerSetting) new IntegerSetting("Desync Green", 0, 0, 255, 1).setDependency(desyncModels::isEnabled);
	private IntegerSetting desyncViewModelBlue = (IntegerSetting) new IntegerSetting("Desync Blue", 255, 0, 255, 1).setDependency(desyncModels::isEnabled);
	private IntegerSetting desyncViewModelAlpha = (IntegerSetting) new IntegerSetting("Desync Alpha", 110, 0, 255, 1).setDependency(desyncModels::isEnabled);
	
	private EntityOtherPlayerMP desyncModel = null;
	private Shader desyncModelShader;
	
	private static Logger logger = LogManager.getLogger();
	
	/**
	 * @return the desyncModel
	 */
	public EntityOtherPlayerMP getDesyncModel() {
		return desyncModel;
	}
	
	
	@EventHandler
	private Handler<EventTick> onTick = e -> {
		
		// Desync player model
		if (desyncModels.isEnabled()) {
			
			if (e.isPre()) {
				if (desyncModel == null) {
					desyncModel = new EntityOtherPlayerMP(mc.theWorld, new GameProfile(UUID.fromString("bd9d1397-fa0e-469c-9854-8b063f093f4f"), "https_e621_net"));
				}
				desyncModel.lastTickPosX = desyncModel.posX;
				desyncModel.lastTickPosY = desyncModel.posY;
				desyncModel.lastTickPosZ = desyncModel.posZ;
				desyncModel.copyLocationAndAnglesFrom(mc.thePlayer);
				desyncModel.inventory = mc.thePlayer.inventory;
				desyncModel.rotationYaw += 90;
			}else {
				
			}
			
		}
		
	};
	
	private static Framebuffer framebuffer = null;
	@EventHandler
	private Handler<EventRender3D> onRender3D = e -> {
		
		if (desyncModels.isEnabled()) {
			
			GlStateManager.pushMatrix();
			GlStateManager.pushAttrib();
			GlStateManager.translate(desyncModel.posX - mc.thePlayer.posX, desyncModel.posY - mc.thePlayer.posY, desyncModel.posZ - mc.thePlayer.posZ);
			RenderPlayer renderDesync = new RenderPlayer(mc.getRenderManager());
			
			desyncModelShader.bind();
			GL20.glUniform4f(desyncModelShader.getUniform("rgba"), desyncViewModelRed.getValue() / 255f, desyncViewModelGreen.getValue() / 255f, desyncViewModelBlue.getValue() / 255f, desyncViewModelAlpha.getValue() / 255f);
			renderDesync.doRender(desyncModel, 0, 0, 0, 0, e.getPartialTicks());
			desyncModelShader.unbind();
			
			GlStateManager.popAttrib();
			GlStateManager.popMatrix();
		}
		
	};
	
	/**
	 * @return the overrideF3
	 */
	public BooleanSetting getOverrideF3() {
		return overrideF3;
	}
	
	@Override
	public double getArraylistAnimation() {
		return 0;
	}
	
	/**
	 * @return the desyncModelShader
	 */
	public Shader getDesyncModelShader() {
		return desyncModelShader;
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
