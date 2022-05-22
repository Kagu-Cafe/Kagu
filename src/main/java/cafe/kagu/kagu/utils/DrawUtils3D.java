/**
 * 
 */
package cafe.kagu.kagu.utils;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author lavaflowglow
 *
 */
public class DrawUtils3D {
	
	/**
	 * Draws a 3d box
	 * @param corner1X The x pos of the first corner
	 * @param corner1Y The y pos of the first corner
	 * @param corner1Z The z pos of the first corner
	 * @param corner2X The x pos of the second corner
	 * @param corner2Y The y pos of the second corner
	 * @param corner2Z The z pos of the second corner
	 * @param partialTicks The partial ticks
	 * @param color The color of the box
	 */
	public static void drawColored3DWorldBox(double corner1X, double corner1Y, double corner1Z, double corner2X, double corner2Y, double corner2Z, int color) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		Vector4d color4d = UiUtils.getVectorFromColor(color);
		
		// Fix the position of the render
		Vector3d renderOffsets = get3dWorldOffsets();
		corner1X -= renderOffsets.getX();
		corner1Y -= renderOffsets.getY();
		corner1Z -= renderOffsets.getZ();
		corner2X -= renderOffsets.getX();
		corner2Y -= renderOffsets.getY();
		corner2Z -= renderOffsets.getZ();
		
		// Begin
		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		
		// Back side
		worldRenderer.pos(corner1X, corner1Y, corner1Z).color(color4d.getX(), color4d.getY(), color4d.getZ(), color4d.getW()).endVertex();
		worldRenderer.pos(corner2X, corner1Y, corner1Z).color(color4d.getX(), color4d.getY(), color4d.getZ(), color4d.getW()).endVertex();
		worldRenderer.pos(corner2X, corner2Y, corner1Z).color(color4d.getX(), color4d.getY(), color4d.getZ(), color4d.getW()).endVertex();
		worldRenderer.pos(corner1X, corner2Y, corner1Z).color(color4d.getX(), color4d.getY(), color4d.getZ(), color4d.getW()).endVertex();
		
		// Front side
		worldRenderer.pos(corner1X, corner1Y, corner2Z).color(color4d.getX(), color4d.getY(), color4d.getZ(), color4d.getW()).endVertex();
		worldRenderer.pos(corner2X, corner1Y, corner2Z).color(color4d.getX(), color4d.getY(), color4d.getZ(), color4d.getW()).endVertex();
		worldRenderer.pos(corner2X, corner2Y, corner2Z).color(color4d.getX(), color4d.getY(), color4d.getZ(), color4d.getW()).endVertex();
		worldRenderer.pos(corner1X, corner2Y, corner2Z).color(color4d.getX(), color4d.getY(), color4d.getZ(), color4d.getW()).endVertex();
		
		// Top
		worldRenderer.pos(corner1X, corner2Y, corner1Z).color(color4d.getX(), color4d.getY(), color4d.getZ(), color4d.getW()).endVertex();
		worldRenderer.pos(corner2X, corner2Y, corner1Z).color(color4d.getX(), color4d.getY(), color4d.getZ(), color4d.getW()).endVertex();
		worldRenderer.pos(corner2X, corner2Y, corner2Z).color(color4d.getX(), color4d.getY(), color4d.getZ(), color4d.getW()).endVertex();
		worldRenderer.pos(corner1X, corner2Y, corner2Z).color(color4d.getX(), color4d.getY(), color4d.getZ(), color4d.getW()).endVertex();
		
		// Bottom
		worldRenderer.pos(corner1X, corner1Y, corner1Z).color(color4d.getX(), color4d.getY(), color4d.getZ(), color4d.getW()).endVertex();
		worldRenderer.pos(corner2X, corner1Y, corner1Z).color(color4d.getX(), color4d.getY(), color4d.getZ(), color4d.getW()).endVertex();
		worldRenderer.pos(corner2X, corner1Y, corner2Z).color(color4d.getX(), color4d.getY(), color4d.getZ(), color4d.getW()).endVertex();
		worldRenderer.pos(corner1X, corner1Y, corner2Z).color(color4d.getX(), color4d.getY(), color4d.getZ(), color4d.getW()).endVertex();
		
		// Left side
		worldRenderer.pos(corner1X, corner1Y, corner1Z).color(color4d.getX(), color4d.getY(), color4d.getZ(), color4d.getW()).endVertex();
		worldRenderer.pos(corner1X, corner1Y, corner2Z).color(color4d.getX(), color4d.getY(), color4d.getZ(), color4d.getW()).endVertex();
		worldRenderer.pos(corner1X, corner2Y, corner2Z).color(color4d.getX(), color4d.getY(), color4d.getZ(), color4d.getW()).endVertex();
		worldRenderer.pos(corner1X, corner2Y, corner1Z).color(color4d.getX(), color4d.getY(), color4d.getZ(), color4d.getW()).endVertex();
		
		// Right side
		worldRenderer.pos(corner2X, corner1Y, corner1Z).color(color4d.getX(), color4d.getY(), color4d.getZ(), color4d.getW()).endVertex();
		worldRenderer.pos(corner2X, corner1Y, corner2Z).color(color4d.getX(), color4d.getY(), color4d.getZ(), color4d.getW()).endVertex();
		worldRenderer.pos(corner2X, corner2Y, corner2Z).color(color4d.getX(), color4d.getY(), color4d.getZ(), color4d.getW()).endVertex();
		worldRenderer.pos(corner2X, corner2Y, corner1Z).color(color4d.getX(), color4d.getY(), color4d.getZ(), color4d.getW()).endVertex();
		
		// Draw
		tessellator.draw();
		
	}
	
	/**
	 * Gets the render offsets for the 3d world
	 * @return The render offsets
	 */
	public static Vector3d get3dWorldOffsets() {
		double posX = Minecraft.getMinecraft().getRenderManager().getRenderPosX();
		double posY = Minecraft.getMinecraft().getRenderManager().getRenderPosY();
		double posZ = Minecraft.getMinecraft().getRenderManager().getRenderPosZ();
		
		return new Vector3d(posX, posY, posZ);
	}
	
	/**
	 * Gets the render offsets for the 3d world
	 * @return The render offsets
	 */
	public static Vector3d get3dPlayerOffsets() {
		double posX = Minecraft.getMinecraft().getRenderViewEntity().posX + 0.5;
		double posY = Minecraft.getMinecraft().getRenderViewEntity().posY;
		double posZ = Minecraft.getMinecraft().getRenderViewEntity().posZ + 0.5;
		return new Vector3d(posX, posY, posZ);
	}
	
}
