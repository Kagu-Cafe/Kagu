/**
 * 
 */
package xyz.yiffur.yiffur.font;

import java.awt.Font;

import net.minecraft.client.renderer.GlStateManager;

/**
 * @author lavaflowglow
 *
 */
public class FontRenderer {
	
	/**
	 * @param fontFileName The name of the ttf file that you want to load
	 * @param size The size of the font
	 * @param antiAliasing Whether or not anti aliasing is enabled
	 * @param style The awt font style
	 */
	public FontRenderer(String fontFileName, int size, boolean antiAliasing, int style) {
		Font font = FontUtils.getFontFromInputStream(FontRenderer.class.getResourceAsStream(fontFileName), size, style);
		glyphs = new GlyphMap(font, antiAliasing);
	}
	
	private GlyphMap glyphs;
	
	/**
	 * Draws a string onto the screen
	 * @param text The string to draw
	 * @param x The x pos for the string
	 * @param y The y pos for the string
	 * @param color The color of the text
	 */
	public void drawString(String text, double x, double y, int color) {
		
		float red = (float)(color >> 16 & 255) / 255.0F;
		float blue = (float)(color >> 8 & 255) / 255.0F;
		float green = (float)(color & 255) / 255.0F;
		float alpha = (float)(color >> 24 & 255) / 255.0F;
		
		GlStateManager.pushMatrix();
		GlStateManager.color(red, green, blue, alpha);
		
		for (char c : text.toCharArray()) {
			x += glyphs.drawChar(c, (int)x, (int)y);
		}
		
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.popMatrix();
		
	}
	
}
