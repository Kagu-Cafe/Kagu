	/**
 * 
 */
package cafe.kagu.kagu.prot.ui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import cafe.kagu.kagu.Kagu;
import cafe.kagu.kagu.font.FontRenderer;
import cafe.kagu.kagu.font.FontUtils;
import cafe.kagu.kagu.utils.SoundUtils;
import cafe.kagu.kagu.utils.UiUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

/**
 * @author DistastefulBannock
 *
 */
public class GuiAuthNeeded extends GuiScreen {
	
	private boolean leftClicked = false;
	
	private String[] textFields = new String[2];
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		FontRenderer titleFr = FontUtils.STRATUM2_MEDIUM_18_AA;
		FontRenderer buttonFr = FontUtils.STRATUM2_REGULAR_13_AA;
		FontRenderer authorFr = FontUtils.STRATUM2_REGULAR_13_AA;
		
		// Background
		drawRect(0, 0, width, height, 0xff1c161a);
		
		// Main window
		UiUtils.drawRoundedRect(width * 0.4, height * 0.3, width * 0.6, height * 0.7, 0xff292425, 5);
		
		// Title
		titleFr.drawCenteredString(Kagu.getName() + " Auth", width * 0.5, height * 0.3, -1);
		
		GlStateManager.pushMatrix();
		int margin = 5;
		float buttonHeight = buttonFr.getFontHeight() + 4;
		GlStateManager.translate(0, titleFr.getFontHeight() + margin, 0);
		
		for (int i = 0; i < 5; i++) {
			
			switch (i) {
				case 0:{
					UiUtils.drawRoundedRect(width * 0.4 + margin, height * 0.3, width * 0.6 - margin, height * 0.3 + buttonHeight, 0xff393435, 3);
				}break;
				default:{
					UiUtils.drawRoundedRect(width * 0.4 + margin, height * 0.3, width * 0.6 - margin, height * 0.3 + buttonHeight, 0xff393435, 3);
				}break;
			}
			
			GlStateManager.translate(0, buttonHeight + margin, 0);
		}
		
		GlStateManager.popMatrix();
		
		// Authors
		double centerMultiColorText = authorFr.getStringWidth("Made with <3 by DistastefulBannock & lavaflowglow") / 2;
		double authorOffset = 0;
		authorOffset += authorFr.drawString("Made with ", width * 0.5 - centerMultiColorText + authorOffset, height - authorFr.getFontHeight() - 2, -1);
		authorOffset += authorFr.drawString("<", width * 0.5 - centerMultiColorText + authorOffset, height - authorFr.getFontHeight() - 2, 0xffFF5555);
		authorOffset += authorFr.drawString("3", width * 0.5 - centerMultiColorText + authorOffset, height - authorFr.getFontHeight() - 2, 0xffFF5555);
		authorOffset += authorFr.drawString(" by ", width * 0.5 - centerMultiColorText + authorOffset, height - authorFr.getFontHeight() - 2, -1);
		authorOffset += authorFr.drawString("DistastefulBannock", width * 0.5 - centerMultiColorText + authorOffset, height - authorFr.getFontHeight() - 2, 0xffd58cff);
		authorOffset += authorFr.drawString(" & ", width * 0.5 - centerMultiColorText + authorOffset, height - authorFr.getFontHeight() - 2, -1);
		authorOffset += authorFr.drawString("lavaflowglow", width * 0.5 - centerMultiColorText + authorOffset, height - authorFr.getFontHeight() - 2, 0xffa5e0fe);
		
		leftClicked = false;
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseButton == 0)
			leftClicked = true;
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
}
