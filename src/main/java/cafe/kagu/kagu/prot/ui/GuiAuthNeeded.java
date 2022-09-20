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
	
	private boolean leftClicked = false;
	private CurrentDisplay currentDisplay = CurrentDisplay.NONE;
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		FontRenderer titleFr = FontUtils.STRATUM2_MEDIUM_18_AA;
		FontRenderer buttonFr = FontUtils.STRATUM2_MEDIUM_13_AA;
		FontRenderer authorFr = FontUtils.STRATUM2_MEDIUM_13_AA;
		
		// Background
		drawRect(0, 0, width, height, 0xff1c161a);
		
		// Main window
		UiUtils.drawRoundedRect(width * 0.4, height * 0.3, width * 0.6, height * 0.7, 0xff292425, 5);
		
		// Title
		titleFr.drawCenteredString(Kagu.getName() + " Auth", width * 0.5, height * 0.3, -1);
		
		int margin = 5;
		switch (currentDisplay) {
			case NONE:{
				double buttonSize = buttonFr.getFontHeight() + 4;
				
				// Login button
				UiUtils.drawRoundedRect(width * 0.4 + margin, height * 0.3 + margin + titleFr.getFontHeight(), width * 0.6 - margin,
						height * 0.3 + margin + titleFr.getFontHeight() + buttonSize, 0xff393435, 5);
				UiUtils.enableWireframe();
				if (UiUtils.isMouseInsideRoundedRect(mouseX, mouseY, width * 0.4 + margin, height * 0.3 + margin + titleFr.getFontHeight(), width * 0.6 - margin,
						height * 0.3 + margin + titleFr.getFontHeight() + buttonSize, 5)) {
					GL11.glLineWidth(1.5f);
					if (leftClicked) {
						
						leftClicked = false;
					}
				}else {
					GL11.glLineWidth(1);
				}
				UiUtils.drawRoundedRect(width * 0.4 + margin, height * 0.3 + margin + titleFr.getFontHeight(), width * 0.6 - margin,
						height * 0.3 + margin + titleFr.getFontHeight() + buttonSize, 0xff1c161a, 5);
				UiUtils.disableWireframe();
				buttonFr.drawCenteredString("Login", width * 0.5, height * 0.3 + margin + titleFr.getFontHeight() + 2, -1);
				
				// Register button
				UiUtils.drawRoundedRect(width * 0.4 + margin, height * 0.3 + margin * 2 + titleFr.getFontHeight() + buttonSize, width * 0.6 - margin,
						height * 0.3 + margin * 2 + titleFr.getFontHeight() + buttonSize * 2, 0xff393435, 5);
				UiUtils.enableWireframe();
				if (UiUtils.isMouseInsideRoundedRect(mouseX, mouseY, width * 0.4 + margin, height * 0.3 + margin * 2 + titleFr.getFontHeight() + buttonSize, width * 0.6 - margin,
						height * 0.3 + margin * 2 + titleFr.getFontHeight() + buttonSize * 2, 5)) {
					GL11.glLineWidth(1.5f);
					if (leftClicked) {
						
						leftClicked = false;
					}
				}else {
					GL11.glLineWidth(1);
				}
				UiUtils.drawRoundedRect(width * 0.4 + margin, height * 0.3 + margin * 2 + titleFr.getFontHeight() + buttonSize, width * 0.6 - margin,
						height * 0.3 + margin * 2 + titleFr.getFontHeight() + buttonSize * 2, 0xff1c161a, 5);
				UiUtils.disableWireframe();
				buttonFr.drawCenteredString("Register", width * 0.5, height * 0.3 + margin * 2 + titleFr.getFontHeight() + buttonSize + 2, -1);
				
			}break;
		}
		
		double centerMultiColorText = authorFr.getStringWidth("Made with <3 by DistastefulBannock & lavaflowglow") / 2;
		double authorOffset = 0;
		authorOffset += authorFr.drawString("Made with ", width * 0.5 - centerMultiColorText + authorOffset, height - authorFr.getFontHeight() - 2, -1);
		authorOffset += authorFr.drawString("<", width * 0.5 - centerMultiColorText + authorOffset, height - authorFr.getFontHeight() - 3, 0xffFF5555);
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
	
	/**
	 * @author DistastefulBannock
	 *
	 */
	private enum CurrentDisplay{
		LOGIN,
		REGISTER,
		NONE
	}
	
}
