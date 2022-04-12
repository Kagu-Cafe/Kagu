/**
 * 
 */
package xyz.yiffur.yiffur.font;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import org.newdawn.slick.TrueTypeFont;

/**
 * @author lavaflowglow
 *
 */
public class FontUtils {
	
	/**
	 * Gets a font from an inputstream
	 * @param in The inputstream
	 * @param size The font size
	 * @param style The font style
	 * @return The loaded font
	 */
	public static Font getFontFromInputStream(InputStream in, float size, int style) {
		try {
			return Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(style, size); // Load font
		} catch (FontFormatException | IOException e) {
			return null; // Could not load font
		}
	}
	
}
