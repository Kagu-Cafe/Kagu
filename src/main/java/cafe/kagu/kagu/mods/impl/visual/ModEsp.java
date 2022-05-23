/**
 * 
 */
package cafe.kagu.kagu.mods.impl.visual;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;

import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.impl.Event2DRender;
import cafe.kagu.kagu.eventBus.impl.Event3DRender;
import cafe.kagu.kagu.eventBus.impl.EventEntitiesRender;
import cafe.kagu.kagu.eventBus.impl.EventEntityRender;
import cafe.kagu.kagu.font.FontRenderer;
import cafe.kagu.kagu.font.FontUtils;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.impl.BooleanSetting;
import cafe.kagu.kagu.settings.impl.ModeSetting;
import cafe.kagu.kagu.utils.DrawUtils3D;
import cafe.kagu.kagu.utils.Shader;
import cafe.kagu.kagu.utils.StencilUtil;
import cafe.kagu.kagu.utils.UiUtils;
import cafe.kagu.kagu.utils.Shader.ShaderType;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

/**
 * @author lavaflowglow
 *
 */
public class ModEsp extends Module {
	
	public ModEsp() {
		super("ESP", Category.VISUAL);
		try {
			Shader shader = new Shader(ShaderType.FRAGMENT, "#version 330\r\n"
					+ "\r\n"
					+ "out vec4 fragColor;\r\n"
					+ "\r\n"
					+ "void main() {\r\n"
					+ "    fragColor = vec4(1.0, 1.0, 1.0, 1.0);\r\n"
					+ "}");
			shader.create();
			shader.link();
			this.shader = shader;
		} catch (Exception e) {
			
		}
		setSettings(mode);
	}
	
	// ESP modes
	public ModeSetting mode = new ModeSetting("Mode", "Kagu 2D", "Kagu 2D", "Test");
	
	private Shader shader;
	
	private boolean secondPass = false;
	
	/**
	 * @return the chamsPass
	 */
	public boolean isSecondPass() {
		return secondPass;
	}
	
	/**
	 * @return the shader
	 */
	public Shader getShader() {
		return shader;
	}

	@Override
	public void onEnable() {
		secondPass = false;
	}
	
	@Override
	public void onDisable() {
		secondPass = false;
	}
	
	private ArrayList<EspEntity> draw2dEntities = new ArrayList();
	
	@EventHandler
	private Handler<Event2DRender> onRender2D = e -> {
		
		if (mode.is("Kagu 2D")) {
			
			for (EspEntity ent : draw2dEntities) {
				UiUtils.enableWireframe();
				Gui.drawRect(ent.getLeft(), ent.getTop(), ent.getRight(), ent.getBottom(), 0xffffffff);
				UiUtils.disableWireframe();
				FontRenderer nametagRenderer = FontUtils.ROBOTO_LIGHT_10;
				nametagRenderer.drawCenteredString(ent.getEntityLivingBase().getName(), (ent.getLeft() + ent.getRight()) / 2, ent.getTop() - nametagRenderer.getFontHeight() - 1, 0xffffffff);
			}
			
		}
		
	};
	
	@EventHandler
	private Handler<Event3DRender> onRender3D = e -> {
		
		if (mode.is("Kagu 2D")) {
			
			ArrayList<EspEntity> draw2dEntities = new ArrayList<EspEntity>();
			for (Entity ent : mc.theWorld.loadedEntityList) {
				
				// Only get living entities
				if (!(ent instanceof EntityLivingBase)) {
					continue;
				}
				
				EntityLivingBase entityLivingBase = (EntityLivingBase)ent;
				
				// Ignore the player if in first person
				if (entityLivingBase == mc.thePlayer && mc.gameSettings.thirdPersonView == 0)
					continue;
				
				double left = Integer.MAX_VALUE, top = Integer.MAX_VALUE, right = Integer.MIN_VALUE, bottom = Integer.MIN_VALUE;
				
				AxisAlignedBB boundingBox = entityLivingBase.getEntityBoundingBox();
				
				// All the corners for the bounding box -> screen coords for each position
				Vector3d offsets = entityLivingBase == mc.thePlayer ? DrawUtils3D.get3dPlayerOffsets() : DrawUtils3D.get3dWorldOffsets();
				Vector3f backBl = DrawUtils3D.project2D((float)boundingBox.minX, (float)boundingBox.minY, (float)boundingBox.minZ, offsets);
				Vector3f backBr = DrawUtils3D.project2D((float)boundingBox.maxX, (float)boundingBox.minY, (float)boundingBox.minZ, offsets);
				Vector3f backTl = DrawUtils3D.project2D((float)boundingBox.minX, (float)boundingBox.maxY, (float)boundingBox.minZ, offsets);
				Vector3f backTr = DrawUtils3D.project2D((float)boundingBox.maxX, (float)boundingBox.maxY, (float)boundingBox.minZ, offsets);
				Vector3f frontBl = DrawUtils3D.project2D((float)boundingBox.minX, (float)boundingBox.minY, (float)boundingBox.maxZ, offsets);
				Vector3f frontBr = DrawUtils3D.project2D((float)boundingBox.maxX, (float)boundingBox.minY, (float)boundingBox.maxZ, offsets);
				Vector3f frontTl = DrawUtils3D.project2D((float)boundingBox.minX, (float)boundingBox.maxY, (float)boundingBox.maxZ, offsets);
				Vector3f frontTr = DrawUtils3D.project2D((float)boundingBox.maxX, (float)boundingBox.maxY, (float)boundingBox.maxZ, offsets);
				Vector3f[] corners = new Vector3f[] {backBl, backBr, backTl, backTr, 
													 frontBl, frontBr, frontTl, frontTr};
				
				// Calculate the best box coords
				for (Vector3f corner : corners) {
					if (corner == null || corner.getZ() < 0 || corner.getZ() >= 1)
						continue;
					
					left = Math.min(left, corner.getX());
					top = Math.min(top, corner.getY());
					right = Math.max(right, corner.getX());
					bottom = Math.max(bottom, corner.getY());
				}
				
				// Remove ones where we couldn't get all the render coords
				if (left == Integer.MAX_VALUE || top == Integer.MAX_VALUE || right == Integer.MIN_VALUE || bottom == Integer.MIN_VALUE) {
					continue;
				}
				
				draw2dEntities.add(new EspEntity(entityLivingBase, left, top, right, bottom));
				
			}
			
			this.draw2dEntities = draw2dEntities;
			
		}
		
	};
	
	private class EspEntity {

		/**
		 * @param entityLivingBase The entity to render
		 * @param left The left of the box
		 * @param top The top of the box
		 * @param right The right of the box
		 * @param bottom The bottom of the box
		 */
		public EspEntity(EntityLivingBase entityLivingBase, double left, double top, double right, double bottom) {
			super();
			this.entityLivingBase = entityLivingBase;
			this.left = left;
			this.top = top;
			this.right = right;
			this.bottom = bottom;
		}
		
		private EntityLivingBase entityLivingBase;
		private double left, top, right, bottom;
		
		/**
		 * @return the entityLivingBase
		 */
		public EntityLivingBase getEntityLivingBase() {
			return entityLivingBase;
		}
		
		/**
		 * @return the left
		 */
		public double getLeft() {
			return left;
		}

		/**
		 * @return the top
		 */
		public double getTop() {
			return top;
		}

		/**
		 * @return the right
		 */
		public double getRight() {
			return right;
		}

		/**
		 * @return the bottom
		 */
		public double getBottom() {
			return bottom;
		}
		
	}
	
}
