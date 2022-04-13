/**
 * 
 */
package xyz.yiffur.yiffur.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;

/**
 * @author lavaflowglow
 *
 */
public class GlyphMap {

	/**
	 * Creates a new glyph map
	 * 
	 * @param font The font that will be turned into a glyph map
	 */
	public GlyphMap(Font font, boolean antiAliasing) {
		this.font = font;
		this.antiAliasing = antiAliasing;
		load();
	}

	private Map<Character, Glyph> glyphMap = new HashMap<>(); // Maps the chars to glyphs
	private BufferedImage glyphs; // Buffered image of all the glyphs
	private DynamicTexture glyphsTexture; // The texture of the buffered image above, used to render in game
	private Font font; // The font
	private boolean antiAliasing; // Whether the font has anti aliasing enabled or not
	
	private static Logger logger = LogManager.getLogger();
	private static FontRenderContext fontRenderContext;
	
	/**
	 * Turned the font into a glyph map
	 */
	public void load() {

		// This will be used to calculate the size of each char
		AffineTransform affineTransform = new AffineTransform();
		fontRenderContext = new FontRenderContext(affineTransform, antiAliasing, font.getSize2D() % 1 == 0);
		
		// Creates an array of chars that contains every char that the font supports
		char[] chars = new char[0];
		{

			// Get all the chars
			ArrayList<Character> characters = new ArrayList<>();
			for (int i = Character.MIN_VALUE; i <= Character.MAX_VALUE; i++) {
				if (font.canDisplay(i)) {
					characters.add((char)i);
				}
			}

			// Put them into the chars array
			chars = new char[characters.size()];
			int index = 0;
			for (char c : characters) {
				chars[index] = c;
				index++;
			}

		}

		// The max width and height for each character, will be used to calculate the
		// buffered image size later
		double maxWidth = 0, maxHeight = 0;

		// Calculate max width and height
		for (char c : chars) {
			Rectangle2D charBounds = font.getStringBounds(String.valueOf(c), fontRenderContext);
			if (maxWidth < charBounds.getWidth())
				maxWidth = charBounds.getWidth();
			if (maxHeight < charBounds.getHeight())
				maxHeight = charBounds.getHeight();
		}

		// Spacing between characters
		maxWidth += 10;
		maxHeight += 10;

		// Calculate the width and height of the image, it will be a square so we only
		// need 1 var
		int imageWidth = (int) (maxWidth * chars.length);
		
		// Create the buffered image
		glyphs = new BufferedImage(imageWidth, (int) maxHeight, BufferedImage.TYPE_INT_ARGB);

		// Draw to the image
		Graphics2D graphics = glyphs.createGraphics();

		// Clear background
		graphics.setBackground(new Color(0, 0, 0, 0));
		graphics.clearRect(0, 0, imageWidth, (int) maxHeight);
		
		// Anti aliasing
		graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, font.getSize2D() % 1 == 0 ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAliasing ? RenderingHints.VALUE_ANTIALIAS_OFF : RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, antiAliasing ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		
		// We use white because it will allow us to recolor with opengl
		graphics.setColor(Color.WHITE);
		
		// Draw all the characters and add them to the map
		{
			int x = 0;
			int y = (int) Math.floor(maxHeight) - 6;
			for (char c : chars) {
				
				// Draw char and create new glyph
				Rectangle2D bounds = font.getStringBounds(String.valueOf(c), fontRenderContext);
				graphics.drawString(String.valueOf(c), x, y);
				Glyph glyph = new Glyph(x, y, (int)bounds.getWidth(), (int)bounds.getHeight());
				glyphMap.put(c, glyph);
				
				x += maxWidth;
				if (x >= imageWidth) {
					logger.error("Failed to generate buffered image, too many characters");
					return;
				}
			}
		}
		
		// Create the dynamic texture
		glyphsTexture = new DynamicTexture(glyphs);
		
		// This writes the buffered image to a file, can be used for debugging
//		try {
//			ImageIO.write(glyphs, "png", new File("Yiffur/" + System.currentTimeMillis() + ".png"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
	}
	
	/**
	 * @param c     The character to draw
	 * @param x     The x pos of where the character should be drawn
	 * @param y     The y pos of where the character should be drawn
	 * @param color The color to draw the character
	 * @return The width of the character
	 */
	public double drawChar(char c, int x, int y) {
		Glyph glyph = glyphMap.get(c);
		if (glyph == null) {
			logger.debug("Font doesn't contain character, not rendering it");
			return 0;
		}
		
		// Draw character
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		
		GlStateManager.enableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();
		
		GlStateManager.bindTexture(glyphsTexture.getGlTextureId());
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, antiAliasing ? GL11.GL_LINEAR : GL11.GL_NEAREST);
		
		// In opengl the textures are found not through pixels but rather a value 0 to 1
//		y -= (glyphs.getHeight() - glyph.getHeight());
		float scaledTexX = ((float)(glyph.getX()) / glyphs.getWidth());
		float scaledTexY = (float)(glyphs.getHeight() - (glyph.getY() + 3)) / glyphs.getHeight();
		float scaledTexWidth = (float)glyph.getWidth() / glyphs.getWidth();
		float scaledTexHeight = (float)glyph.getHeight() / glyphs.getHeight();
		
		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glTexCoord2f(scaledTexX, scaledTexY);
		GL11.glVertex2f(x, y);
		
		GL11.glTexCoord2f(scaledTexX, scaledTexY + scaledTexHeight);
		GL11.glVertex2f(x, y + glyph.getHeight());
		
		GL11.glTexCoord2f(scaledTexX + scaledTexWidth, scaledTexY + scaledTexHeight);
		GL11.glVertex2f(x + glyph.getWidth(), y + glyph.getHeight());
		
		GL11.glTexCoord2f(scaledTexX + scaledTexWidth, scaledTexY);
		GL11.glVertex2f(x + glyph.getWidth(), y);
		
		GL11.glEnd();
		
//		Gui.drawRect(x, y, x + glyph.getWidth(), y + glyph.getHeight(), color);
		
		GlStateManager.disableTexture2D();
		
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
		
//		Minecraft.getMinecraft().fontRendererObj.drawString(String.valueOf(c), x, y, color);
		
		return glyph.getWidth();
	}
	
}
