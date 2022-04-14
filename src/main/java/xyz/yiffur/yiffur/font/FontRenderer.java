/**
 * 
 */
package xyz.yiffur.yiffur.font;

import java.awt.Color;
import java.awt.Font;
import java.io.File;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import xyz.yiffur.yiffur.font.GlyphUtils.GlyphMap;

/**
 * @author lavaflowglow
 *
 */
public class FontRenderer {
	
	/**
	 * @param font The font to use
	 * @param scale Allows you to change the scaling of the font, this way you can have really small text that still looks crisp
	 */
	public FontRenderer(Font font, double scale) {
		glyphMap = GlyphUtils.genereateGlyphBufferedImageFromFont(font, true);
		glyphsTexture = new DynamicTexture(glyphMap.getBufferedImage());
		this.scale = scale;
	}
	
	private GlyphMap glyphMap;
	private DynamicTexture glyphsTexture;
	private double scale;
	
	/**
	 * @param string The string to render
	 * @param x The x pos of the string
	 * @param y The y pos of the string
	 * @param color The color to draw the string with
	 * @return The width of the string
	 */
	public double drawString(String string, double x, double y, int color) {
		return drawString(string, x, y, color, false);
	}
	
	/**
	 * @param string The string to render
	 * @param x The x pos of the string
	 * @param y The y pos of the string
	 * @param color The color to draw the string with
	 * @param shadow Whether or not the string should have a shadow
	 * @return The width of the string
	 */
	public double drawString(String string, double x, double y, int color, boolean shadow) {
		
		// Character offset, also string width after drawing the entire string
		double offset = 0;
		
		// Draw chars
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		
		// If the text has a shadow
		if (shadow) {
			// Colors
			float alpha = (float)(color >> 24 & 255) / 255.0F;
			for (char c : string.toCharArray()) {
				offset += drawChar(c, x + offset + 1, y + 1, new Color(0, 0, 0, alpha).getRGB());
			}
		}
		
		offset = 0;
		
		// Non shadow text
		for (char c : string.toCharArray()) {
			offset += drawChar(c, x + offset, y, color);
		}
		
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
		
		return offset;
	}
	
	/**
	 * Draws a single character from the glyph texture
	 * @param c The character to draw
	 * @param x The x pos to draw at
	 * @param y The y post to draw at
	 * @return
	 */
	public double drawChar(char c, double x, double y, int color) {
		Glyph glyph = glyphMap.getMapping().get(c);
		
		// If the font doesn't have a glyph for that character than return
		if (glyph == null) {
			return 0;
		}
		
		y -= glyph.getRenderYOffset();
//		y -= glyph.getHeight() / 2;
		
		// Draw character
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		
		// Color
		float red = (float)(color >> 16 & 255) / 255.0F;
		float blue = (float)(color >> 8 & 255) / 255.0F;
		float green = (float)(color & 255) / 255.0F;
		float alpha = (float)(color >> 24 & 255) / 255.0F;
		
		// Scaling
		GlStateManager.scale(scale, scale, scale);
		x *= 1 / scale;
		y *= 1 / scale;
		
		// Render
		GL11.glEnable(GL11.GL_BLEND);
		GlStateManager.enableAlpha();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableTexture2D();
//		GlStateManager.alp
		GlStateManager.bindTexture(glyphsTexture.getGlTextureId());
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST); // Nearest looks better on anti aliased text as well as non anti aliased text compared to GL_LINEAR
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		
//		GL11.glBegin(GL11.GL_QUADS);
//		
//		GL11.glTexCoord2d(glyph.getScaledX(), glyph.getScaledY());
//		GL11.glVertex2d(x, y);
//		
//		GL11.glTexCoord2d(glyph.getScaledX(), glyph.getScaledY() + glyph.getScaledHeight());
//		GL11.glVertex2d(x, y + glyph.getHeight());
//		
//		GL11.glTexCoord2d(glyph.getScaledX() + glyph.getScaledWidth(), glyph.getScaledY() + glyph.getScaledHeight());
//		GL11.glVertex2d(x + glyph.getWidth(), y + glyph.getHeight());
//		
//		GL11.glTexCoord2d(glyph.getScaledX() + glyph.getScaledWidth(), glyph.getScaledY());
//		GL11.glVertex2d(x + glyph.getWidth(), y);
//		
//		GL11.glEnd();
		
		Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(x, y, 0).tex(glyph.getScaledX(), glyph.getScaledY()).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(x, y + glyph.getHeight(), 0).tex(glyph.getScaledX(), glyph.getScaledY() + glyph.getScaledHeight()).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(x + glyph.getWidth(), y + glyph.getHeight(), 0).tex(glyph.getScaledX() + glyph.getScaledWidth(), glyph.getScaledY() + glyph.getScaledHeight()).color(red, green, blue, alpha).endVertex();
        worldrenderer.pos(x + glyph.getWidth(), y, 0).tex(glyph.getScaledX() + glyph.getScaledWidth(), glyph.getScaledY()).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
		
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
		
		return glyph.getWidth() * scale;
	}
	
}
