	/**
 * 
 */
package cafe.kagu.kagu.prot.ui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import cafe.kagu.kagu.Kagu;
import cafe.kagu.kagu.font.FontRenderer;
import cafe.kagu.kagu.font.FontUtils;
import cafe.kagu.kagu.utils.UiUtils;
import net.minecraft.client.gui.GuiScreen;

/**
 * @author DistastefulBannock
 *
 */
public class GuiAuthNeeded extends GuiScreen {
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		FontRenderer titleFr = FontUtils.STRATUM2_MEDIUM_18_AA;
		
		drawRect(0, 0, width, height, 0xff1c161a);
		UiUtils.drawRoundedRect(width * 0.4, height * 0.3, width * 0.6, height * 0.7, 0xff292425, 5);
		titleFr.drawCenteredString(Kagu.getName() + " Auth", width * 0.5, height * 0.3, -1);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	
	
}
