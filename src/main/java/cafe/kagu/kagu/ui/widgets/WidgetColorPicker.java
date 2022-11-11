/**
 * 
 */
package cafe.kagu.kagu.ui.widgets;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

/**
 * @author DistastefulBannock
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
		
		// Draw the hue wheel
		int hueWheelWidth = width / 9;
		int originX = x + width / 2;
		int originY = y - height / 2;
		
		Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        worldrenderer.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION_COLOR);
		for (int rotate = 0; rotate <= 360; rotate++) {
			Color color = Color.getHSBColor(rotate / 360f, 1, 1);
			int yaw = rotate - 90;
			double xFar = originX + Math.cos(Math.toRadians(yaw)) * (width / 2);
			double yFar = originY + Math.sin(Math.toRadians(yaw)) * (height / 2);
			double xClose = originX + Math.cos(Math.toRadians(yaw)) * (width / 2 - hueWheelWidth);
			double yClose = originY + Math.sin(Math.toRadians(yaw)) * (height / 2 - hueWheelWidth);
			worldrenderer.pos(xFar, yFar, 0).color(color.getRed() / 255d, color.getGreen() / 255d, color.getBlue() / 255d, 1d).endVertex();
			worldrenderer.pos(xClose, yClose, 0).color(color.getRed() / 255d, color.getGreen() / 255d, color.getBlue() / 255d, 1d).endVertex();
		}
		tessellator.draw();
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
		
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
