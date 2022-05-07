/**
 * 
 */
package xyz.yiffur.yiffur.utils;

import java.awt.Color;

import javax.vecmath.Vector4d;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

/**
 * @author lavaflowglow
 *
 */
public class UiUtils {
	
	/**
	 * Draws a rectangle with a gradient
	 * 
	 * @param left             The left of the rect
	 * @param top              The top of the triangle
	 * @param right            The right of the triangle
	 * @param bottom           The bottom of the triangle
	 * @param topLeftColor     The top left color of the triangle
	 * @param topRightColor    The top right color of the triangle
	 * @param bottomLeftColor  The bottom left color of the triangle
	 * @param bottomRightColor The bottom right color of the triangle
	 */
	public static void drawGradientRect(double left, double top, double right, double bottom, int topLeftColor, int topRightColor, int bottomLeftColor, int bottomRightColor) {
		
        if (left < right)
        {
        	double i = left;
            left = right;
            right = i;
        }

        if (top < bottom)
        {
        	double j = top;
            top = bottom;
            bottom = j;
        }
        
		// Colors
		float tlRed = (float)(topLeftColor >> 16 & 255) / 255.0F;
		float tlGreen = (float)(topLeftColor >> 8 & 255) / 255.0F;
		float tlBlue = (float)(topLeftColor & 255) / 255.0F;
		float tlAlpha = (float)(topLeftColor >> 24 & 255) / 255.0F;
		
		float trRed = (float)(topRightColor >> 16 & 255) / 255.0F;
		float trGreen = (float)(topRightColor >> 8 & 255) / 255.0F;
		float trBlue = (float)(topRightColor & 255) / 255.0F;
		float trAlpha = (float)(topRightColor >> 24 & 255) / 255.0F;
		
		float blRed = (float)(bottomLeftColor >> 16 & 255) / 255.0F;
		float blGreen = (float)(bottomLeftColor >> 8 & 255) / 255.0F;
		float blBlue = (float)(bottomLeftColor & 255) / 255.0F;
		float blAlpha = (float)(bottomLeftColor >> 24 & 255) / 255.0F;
		
		float brRed = (float)(bottomRightColor >> 16 & 255) / 255.0F;
		float brGreen = (float)(bottomRightColor >> 8 & 255) / 255.0F;
		float brBlue = (float)(bottomRightColor & 255) / 255.0F;
		float brAlpha = (float)(bottomRightColor >> 24 & 255) / 255.0F;
		
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
		
        Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();

		worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		worldrenderer.pos((double) left, (double) bottom, 0.0D).color(blRed, blGreen, blBlue, blAlpha).endVertex();
		worldrenderer.pos((double) right, (double) bottom, 0.0D).color(brRed, brGreen, brBlue, brAlpha).endVertex();
		worldrenderer.pos((double) right, (double) top, 0.0D).color(trRed, trGreen, trBlue, trAlpha).endVertex();
		worldrenderer.pos((double) left, (double) top, 0.0D).color(tlRed, tlGreen, tlBlue, tlAlpha).endVertex();
		tessellator.draw();
        
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
		
	}

	/**
	 * Draws a rounded rect
	 * 
	 * @param left       The left of the rect
	 * @param top        The top of the rect
	 * @param right      The right of the rect
	 * @param bottom     The bottom of the rect
	 * @param color      The color of the rect
	 * @param cornerSize How big the rounded corners should be
	 */
	public static void drawRoundedRect(double left, double top, double right, double bottom, int color, double cornerSize) {
		drawRoundedRect(left, top, right, bottom, color, cornerSize, cornerSize, cornerSize, cornerSize);
	}
	
	/**
	 * Draws a rounded rect
	 * 
	 * @param left         The left of the rect
	 * @param top          The top of the rect
	 * @param right        The right of the rect
	 * @param bottom       The bottom of the rect
	 * @param color        The color of the rect
	 * @param cornerSizeTl The corner size for the top left corner
	 * @param cornerSizeTr The corner size for the top right corner
	 * @param cornerSizeBl The corner size for the bottom left corner
	 * @param cornerSizeBr The corner size for the bottom right corner
	 */
	public static void drawRoundedRect(double left, double top, double right, double bottom, int color, double cornerSizeTl, double cornerSizeTr, double cornerSizeBl, double cornerSizeBr) {
		
		// Vars
		double stepsIncrement = 5;
		
		if (top > bottom) {
			double temp = top;
			top = bottom;
			bottom = temp;
		}
		
		if (left > right) {
			double temp = left;
			left = right;
			right = temp;
		}
		
		// Colors
		float red = (float) (color >> 16 & 255) / 255.0F;
		float green = (float) (color >> 8 & 255) / 255.0F;
		float blue = (float) (color & 255) / 255.0F;
		float alpha = (float) (color >> 24 & 255) / 255.0F;
		
		// Draw
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GlStateManager.shadeModel(GL11.GL_SMOOTH);

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();

		worldRenderer.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_COLOR);
		
		// Top left
		if (cornerSizeTl <= 0) {
			worldRenderer.pos(left, top, 0).color(red, green, blue, alpha).endVertex();
		}else {
			for (int i = 0; i <= 90; i += stepsIncrement) {
				worldRenderer.pos(left + cornerSizeTl + Math.sin(i * Math.PI / 180) * cornerSizeTl * -1,
						top + cornerSizeTl + Math.cos(i * Math.PI / 180) * cornerSizeTl * -1, 0).color(red, green, blue, alpha).endVertex();
			}
		}
		
		// Bottom left
		if (cornerSizeBl <= 0) {
			worldRenderer.pos(left, bottom, 0).color(red, green, blue, alpha).endVertex();
		}else {
			for (int i = 90; i <= 180; i += stepsIncrement) {
				worldRenderer.pos(left + cornerSizeBl + Math.sin(i * Math.PI / 180) * cornerSizeBl * -1,
						bottom - cornerSizeBl + Math.cos(i * Math.PI / 180) * cornerSizeBl * -1, 0).color(red, green, blue, alpha).endVertex();
			}
		}
		
		// Bottom right
		if (cornerSizeBr <= 0) {
			worldRenderer.pos(right, bottom, 0).color(red, green, blue, alpha).endVertex();
		}else {
			for (int i = 0; i <= 90; i += stepsIncrement) {
				worldRenderer.pos(right - cornerSizeBr + Math.sin(i * Math.PI / 180) * cornerSizeBr,
						bottom - cornerSizeBr + Math.cos(i * Math.PI / 180) * cornerSizeBr, 0).color(red, green, blue, alpha).endVertex();
			}
		}
		
		// Top right
		if (cornerSizeTr <= 0) {
			worldRenderer.pos(right, top, 0).color(red, green, blue, alpha).endVertex();
		}else {
			for (int i = 90; i <= 180; i += stepsIncrement) {
				worldRenderer.pos(right - cornerSizeTr + Math.sin(i * Math.PI / 180) * cornerSizeTr,
						top + cornerSizeTr + Math.cos(i * Math.PI / 180) * cornerSizeTr, 0).color(red, green, blue, alpha).endVertex();
			}
		}
		
        tessellator.draw();
        
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
        
	}
	
	/**
	 * Enables wireframe mode
	 */
	public static void enableWireframe() {
		GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_LINE);
		GL11.glPolygonMode(GL11.GL_BACK, GL11.GL_LINE);
	}
	
	/**
	 * Disables wireframe mode
	 */
	public static void disableWireframe() {
		GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_FILL);
		GL11.glPolygonMode(GL11.GL_BACK, GL11.GL_FILL);
	}
	
	/**
	 * Turns a vector into a color that can be used with minecraft's draw utils
	 * @param vectorColor The color as a vector
	 * @return The color as an int
	 */
	public static int getColorFromVector(Vector4d vectorColor) {
		return new Color((float)vectorColor.x, (float)vectorColor.y, (float)vectorColor.z, (float)vectorColor.w).getRGB();
	}
	
	/**
	 * Enables gl scissor
	 * @param left The left side of the box
	 * @param top The top of the box
	 * @param right The right side of the box
	 * @param bottom The bottom of the box
	 */
	public static void enableScissor(double left, double top, double right, double bottom) {
		enableScissor((int)left, (int)top, (int)right, (int)bottom);
	}
	
	/**
	 * Enables gl scissor
	 * @param left The left side of the box
	 * @param top The top of the box
	 * @param right The right side of the box
	 * @param bottom The bottom of the box
	 */
	public static void enableScissor(int left, int top, int right, int bottom) {
		
		if (left > right) {
			int temp = left;
			left = right;
			right = temp;
		}
		
		if (top > bottom) {
			int temp = top;
			top = bottom;
			bottom = temp;
		}
		
		// Annoying gui scaling
		ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
		int scaleFactor = scaledResolution.getScaleFactor();
		
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glScissor(left * scaleFactor, top * scaleFactor, (right - left) * scaleFactor, (bottom - top) * scaleFactor);
		
	}
	
	/**
	 * Disables gl scissor
	 */
	public static void disableScissor() {
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}
	
	/**
	 * Lerps a color
	 * @param startColor The start color
	 * @param endColor   The end color
	 * @param t          The lerp value
	 * @return The lerped color
	 */
	public static Vector4d lerpColor(Vector4d startColor, Vector4d endColor, double t) {
		t = MathHelper.clamp_double(t, 0, 1);
		double inverseT = 1 - t;
		
		return new Vector4d((startColor.getX() * t) + (endColor.getX() * inverseT), 
				(startColor.getY() * t) + (endColor.getY() * inverseT), 
				(startColor.getZ() * t) + (endColor.getZ() * inverseT), 
				(startColor.getW() * t) + (endColor.getW() * inverseT));
	}
	
	/**
	 * Calls glcolor with an int
	 * @param color The color to use
	 */
	public static void glColorWithInt(int color) {
		float red = (float) (color >> 16 & 255) / 255.0F;
		float green = (float) (color >> 8 & 255) / 255.0F;
		float blue = (float) (color & 255) / 255.0F;
		float alpha = (float) (color >> 24 & 255) / 255.0F;
		GlStateManager.color(red, green, blue, alpha);
	}
	
}
