/**
 * 
 */
package cafe.kagu.kagu.ui.gui;

import java.io.IOException;

import javax.vecmath.Vector4d;

import cafe.kagu.kagu.utils.UiUtils;
import net.minecraft.client.gui.GuiScreen;

/**
 * @author lavaflowglow
 *
 */
public class GuiAltManager extends GuiScreen {
	
	private static final GuiAltManager INSTANCE = new GuiAltManager();
	public static GuiAltManager getInstance() {
		return INSTANCE;
	}
	
	private static int backgroundColor = UiUtils.getColorFromVector(new Vector4d(0.156862745, 0.156862745, 0.156862745, 1)), 
			buttonPanelsBackgroundColor = UiUtils.getColorFromVector(new Vector4d(0.0784313725, 0.0784313725, 0.0784313725, 1)), 
			buttonDefaultColor = UiUtils.getColorFromVector(new Vector4d(0.211764706, 0.211764706, 0.211764706, 1)),
			buttomHoverColor = UiUtils.getColorFromVector(new Vector4d(0.149019608, 0.149019608, 0.149019608, 1));
	private static boolean leftMouseClicked = false;
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		drawRect(0, 0, width, height, backgroundColor);
		
		if (leftMouseClicked)
			leftMouseClicked = false;
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseButton == 0)
			leftMouseClicked = true;
	}
	
}
