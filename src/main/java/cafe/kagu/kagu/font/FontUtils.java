/**
 * 
 */
package cafe.kagu.kagu.font;

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
	
	// Roboto fonts
	public static final FontRenderer ROBOTO_LIGHT_10 = new FontRenderer(getFontFromInputStream(FontUtils.class.getResourceAsStream("Roboto-Light.ttf"), 20, Font.PLAIN), 0.5);
	public static final FontRenderer ROBOTO_REGULAR_10 = new FontRenderer(getFontFromInputStream(FontUtils.class.getResourceAsStream("Roboto-Regular.ttf"), 20, Font.PLAIN), 0.5);
	
	// The CS:GO font
	public static final FontRenderer STRATUM2_REGULAR_8_AA = new FontRenderer(getFontFromInputStream(FontUtils.class.getResourceAsStream("stratum2_regular.ttf"), 15, Font.PLAIN), 0.5, true);
	public static final FontRenderer STRATUM2_REGULAR_10_AA = new FontRenderer(getFontFromInputStream(FontUtils.class.getResourceAsStream("stratum2_regular.ttf"), 20, Font.PLAIN), 0.5, true);
	public static final FontRenderer STRATUM2_MEDIUM_13_AA = new FontRenderer(getFontFromInputStream(FontUtils.class.getResourceAsStream("stratum2-medium.ttf"), 26, Font.PLAIN), 0.5, true);
	public static final FontRenderer STRATUM2_MEDIUM_18_AA = new FontRenderer(getFontFromInputStream(FontUtils.class.getResourceAsStream("stratum2-medium.ttf"), 36, Font.PLAIN), 0.5, true);
	
	// San francisco
	public static final FontRenderer SAN_FRANCISCO_REGULAR_10_AA = new FontRenderer(getFontFromInputStream(FontUtils.class.getResourceAsStream("SFUIDisplay-Regular.ttf"), 20, Font.PLAIN), 0.5, true);
	public static final FontRenderer SAN_FRANCISCO_THIN_10_AA = new FontRenderer(getFontFromInputStream(FontUtils.class.getResourceAsStream("SFUIDisplay-Light.ttf"), 20, Font.PLAIN), 0.5, true);
	
	// Small pixel
	public static final FontRenderer SMALL_PIXEL_4 = new FontRenderer(getFontFromInputStream(FontUtils.class.getResourceAsStream("small_pixel.ttf"), 16, Font.PLAIN), 0.25);
	
	/**
	 * Gets a font from an inputstream
	 * @param in The inputstream
	 * @param size The font size
	 * @param style The font style
	 * @return The loaded font
	 */
	public static Font getFontFromInputStream(InputStream in, float size, int style) {
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(style, size); // Load font
			in.close(); // Close stream
			return font; // Return
		} catch (FontFormatException | IOException e) {
			return null; // Could not load font
		}
	}
	
}
