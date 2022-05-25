package cafe.kagu.kagu.ui.gui;

import java.io.IOException;

import javax.vecmath.Vector4d;

import cafe.kagu.kagu.Kagu;
import cafe.kagu.kagu.font.FontRenderer;
import cafe.kagu.kagu.font.FontUtils;
import cafe.kagu.kagu.utils.MiscUtils;
import cafe.kagu.kagu.utils.UiUtils;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiDefaultMainMenu extends GuiScreen {
	
	protected GuiDefaultMainMenu() {}
	
	/**
	 * Called when the client starts
	 */
	public static void start() {
		
	}
	
	private static Vector4d backgroundColor = new Vector4d(0.0784313725, 0.0784313725, 0.0784313725, 1), 
							buttonPanelBackgroundColor = new Vector4d(0.168627451, 0.168627451, 0.168627451, 1), 
							buttonColor = new Vector4d(0.239215686, 0.239215686, 0.239215686, 1);
	private boolean leftMouseClicked = false;
	
	@Override
	public void initGui() {
		leftMouseClicked = false;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		FontRenderer titleFr = FontUtils.STRATUM2_MEDIUM_40;
		FontRenderer buttonFr = FontUtils.STRATUM2_MEDIUM_18;
		
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		
		// Draw the background
		drawRect(0, 0, width, height, UiUtils.getColorFromVector(backgroundColor));
		
		// Draw menu background
		UiUtils.drawRoundedRect(width * 0.3, height * 0.1, width * 0.7, height * 0.9, UiUtils.getColorFromVector(buttonPanelBackgroundColor), 10);
		
		// Title
		titleFr.drawCenteredString(Kagu.getName(), width / 2, height * 0.105, -1, true);
		
		// Draw bootins
		double maxButtonSpace = (height * 0.9) - ((height * 0.105) + titleFr.getFontHeight());
		int buttonCount = 6;
		maxButtonSpace -= maxButtonSpace * (0.25 / buttonCount);
		double buttonSize = maxButtonSpace * (0.75 / buttonCount); // They get 90% of the space, and there are 6 buttons
		double buttonPadding = maxButtonSpace * (0.25 / buttonCount); // The padding will make up 20% of the space, and there are six buttons
		
		// Singleplayer
		UiUtils.drawRoundedRect(width * 0.35, ((height * 0.105) + titleFr.getFontHeight()) + buttonPadding,
				width * 0.65, ((height * 0.105) + titleFr.getFontHeight()) + buttonPadding + buttonSize, UiUtils.getColorFromVector(buttonColor), 5);
		buttonFr.drawCenteredString(MiscUtils.removeFormatting(I18n.format("menu.singleplayer")), width / 2, ((height * 0.105) + titleFr.getFontHeight()) - (buttonFr.getFontHeight() / 2) + buttonPadding + (buttonSize / 2), -1);
		
		UiUtils.drawRoundedRect(width * 0.35, ((height * 0.105) + titleFr.getFontHeight()) + (buttonPadding * 2) + buttonSize,
				width * 0.65, ((height * 0.105) + titleFr.getFontHeight()) + (buttonPadding * 2) + (buttonSize * 2), UiUtils.getColorFromVector(buttonColor), 5);
		
		UiUtils.drawRoundedRect(width * 0.35, ((height * 0.105) + titleFr.getFontHeight()) + (buttonPadding * 3) + (buttonSize * 2),
				width * 0.65, ((height * 0.105) + titleFr.getFontHeight()) + (buttonPadding * 3) + (buttonSize * 3), UiUtils.getColorFromVector(buttonColor), 5);
		
		UiUtils.drawRoundedRect(width * 0.35, ((height * 0.105) + titleFr.getFontHeight()) + (buttonPadding * 4) + (buttonSize * 3),
				width * 0.65, ((height * 0.105) + titleFr.getFontHeight()) + (buttonPadding * 4) + (buttonSize * 4), UiUtils.getColorFromVector(buttonColor), 5);
		
		UiUtils.drawRoundedRect(width * 0.35, ((height * 0.105) + titleFr.getFontHeight()) + (buttonPadding * 5) + (buttonSize * 4),
				width * 0.65, ((height * 0.105) + titleFr.getFontHeight()) + (buttonPadding * 5) + (buttonSize * 5), UiUtils.getColorFromVector(buttonColor), 5);
		
		UiUtils.drawRoundedRect(width * 0.35, ((height * 0.105) + titleFr.getFontHeight()) + (buttonPadding * 6) + (buttonSize * 5),
				width * 0.65, ((height * 0.105) + titleFr.getFontHeight()) + (buttonPadding * 6) + (buttonSize * 6), UiUtils.getColorFromVector(buttonColor), 5);
		
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
		
		if (leftMouseClicked) {
			leftMouseClicked = false;
		}
		
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
//		leftMouseClicked = true;
		mc.displayGuiScreen(new GuiSelectWorld(this));
//		mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
	}
	
	
}
