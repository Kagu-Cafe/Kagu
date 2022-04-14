/**
 * 
 */
package xyz.yiffur.yiffur.utils;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

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
	public static void drawGradientRect(int left, int top, int right, int bottom, int topLeftColor, int topRightColor, int bottomLeftColor, int bottomRightColor) {
		
		// The following is a method from inside the game that I modified
        if (left < right)
        {
            int i = left;
            left = right;
            right = i;
        }

        if (top < bottom)
        {
            int j = top;
            top = bottom;
            bottom = j;
        }
        
		// Colors
		float tlRed = (float)(topLeftColor >> 16 & 255) / 255.0F;
		float tlBlue = (float)(topLeftColor >> 8 & 255) / 255.0F;
		float tlGreen = (float)(topLeftColor & 255) / 255.0F;
		float tlAlpha = (float)(topLeftColor >> 24 & 255) / 255.0F;
		
		float trRed = (float)(topRightColor >> 16 & 255) / 255.0F;
		float trBlue = (float)(topRightColor >> 8 & 255) / 255.0F;
		float trGreen = (float)(topRightColor & 255) / 255.0F;
		float trAlpha = (float)(topRightColor >> 24 & 255) / 255.0F;
		
		float blRed = (float)(bottomLeftColor >> 16 & 255) / 255.0F;
		float blBlue = (float)(bottomLeftColor >> 8 & 255) / 255.0F;
		float blGreen = (float)(bottomLeftColor & 255) / 255.0F;
		float blAlpha = (float)(bottomLeftColor >> 24 & 255) / 255.0F;
		
		float brRed = (float)(bottomRightColor >> 16 & 255) / 255.0F;
		float brBlue = (float)(bottomRightColor >> 8 & 255) / 255.0F;
		float brGreen = (float)(bottomRightColor & 255) / 255.0F;
		float brAlpha = (float)(bottomRightColor >> 24 & 255) / 255.0F;
		
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
		
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos((double)left, (double)bottom, 0.0D).color(blRed, blGreen, blBlue, blAlpha).endVertex();
        worldrenderer.pos((double)right, (double)bottom, 0.0D).color(brRed, brGreen, brBlue, brAlpha).endVertex();
        worldrenderer.pos((double)right, (double)top, 0.0D).color(trRed, trGreen, trBlue, trAlpha).endVertex();
        worldrenderer.pos((double)left, (double)top, 0.0D).color(tlRed, tlGreen, tlBlue, tlAlpha).endVertex();
        tessellator.draw();
        
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
		
	}
	
}
