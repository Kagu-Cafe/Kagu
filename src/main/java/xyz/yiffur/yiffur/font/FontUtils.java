/**
 * 
 */
package xyz.yiffur.yiffur.font;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author lavaflowglow
 *
 */
public class FontUtils {
	
	/**
	 * Called when the client starts, does nothing but load the class and by extension calls the <clinit\> method which in return loads all the fonts
	 */
	public static void start() {
		
	}
	
	// These are the fonts that I used to test, the bugs are most likely caused when the glyphs are loaded from the texture, 
	// 		most likely an issue with positioning of the coords
	// I would look into it and fix it but it's 1am and if I stick to 10 pt font I should be fine for now
	public static final FontRenderer ROBOTO_REGULAR_30 = new FontRenderer("Roboto-Regular.ttf", 30, false, Font.PLAIN); // broken
	public static final FontRenderer ROBOTO_REGULAR_20 = new FontRenderer("Roboto-Regular.ttf", 20, false, Font.PLAIN); // weird kerning
	public static final FontRenderer ROBOTO_REGULAR_10 = new FontRenderer("Roboto-Regular.ttf", 10, false, Font.PLAIN); // works flawlessly
	public static final FontRenderer ROBOTO_REGULAR_5 = new FontRenderer("Roboto-Regular.ttf", 5, false, Font.PLAIN); // broken
	public static final FontRenderer ROBOTO_REGULAR_1 = new FontRenderer("Roboto-Regular.ttf", 1, false, Font.PLAIN); // broken
	
	public static final FontRenderer OPENSANS_REGULAR_10 = new FontRenderer("OpenSans-Regular.ttf", 10, false, Font.PLAIN); // some letters have weird kerning, some don't
	
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
