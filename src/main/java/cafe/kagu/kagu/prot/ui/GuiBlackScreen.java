/**
 * 
 */
package cafe.kagu.kagu.prot.ui;

import java.io.IOException;

import net.minecraft.client.gui.GuiScreen;

/**
 * @author DistastefulBannock
 *
 */
public class GuiBlackScreen extends GuiScreen {
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawRect(0, 0, width, height, 0xff000000);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
}
