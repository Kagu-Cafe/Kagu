/**
 * 
 */
package cafe.kagu.kagu.ui.widgets;

import java.awt.Color;

import net.minecraft.client.gui.Gui;

/**
 * @author lavaflowglow
 * A triangle color picker to mc, doesn't support alpha values
 */
public class WidgetColorPicker {
	
	public static final int NO_BACKGROUND = 0b0001;
	public static final int CLOSE_ON_MISS_CLICK = 0b0010;
	public static final int DISPLAY_CONFIRM_BUTTON = 0b0100;
	public static final int DISPLAY_RESET_BUTTON = 0b1000;
	
	private int flags;
	private Color color, defaultColor;
	private Runnable onClose, onColorUpdate;
	private int[] mouseClickPos = null;
	
	/**
	 * @param flags The flags you want for the color picker
	 * @param color The current color to display
	 * @param defaultColor The default color
	 * @param onClose A runnable that gets ran when the color picker is closed
	 * @param onColorUpdate A runnable that gets ran when the color gets updated
	 */
	public WidgetColorPicker(int flags, int color, int defaultColor, Runnable onClose, Runnable onColorUpdate) {
		this.flags = flags;
		this.color = new Color(color);
		this.defaultColor = new Color(defaultColor);
		this.onClose = onClose;
		this.onColorUpdate = onColorUpdate;
	}
	
	/**
	 * Call this on screen draw to hook it into a gui
	 * @param x The x pos to render the color picker at
	 * @param y The y pos to render the color picker at
	 * @param width The width of the color picker
	 * @param height The height of the color picker, should be width + 20 for it to be rendered correctly
	 */
	public void draw(int x, int y, int width, int height) {
		
		// Draw background
		if ((flags & NO_BACKGROUND) == 0) {
			Gui.drawRect(x, y, x + width, y - height, 0xff1e1e1e);
		}
		
		// Draw the color wheel
		int colorWheelWidth = width / 6;
		for (int rotate = 0; rotate < 360; rotate++) {
			int color = Color.HSBtoRGB(rotate / 360, 0.5f, 0.5f);
		}
		
	}
	
	/**
	 * Call this on mouse click to hook it into a gui
	 * @param mouseX The x position of the mouse
	 * @param mouseY The y position of the mouse
	 */
	public void mouseClick(int mouseX, int mouseY) {
		mouseClickPos = new int[] {mouseX, mouseY};
	}
	
	/**
	 * @param color the color to set
	 */
	public void setColor(int color) {
		this.color = new Color(color);
	}
	
	/**
	 * @return the color
	 */
	public int getColor() {
		return color.getRGB();
	}
	
}
