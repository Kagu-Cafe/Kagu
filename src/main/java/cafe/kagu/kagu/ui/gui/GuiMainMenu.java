package cafe.kagu.kagu.ui.gui;

import java.io.IOException;

import javax.vecmath.Vector4d;

import cafe.kagu.kagu.Kagu;
import cafe.kagu.kagu.font.FontUtils;
import cafe.kagu.kagu.utils.UiUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiMainMenu extends GuiScreen {
	
	public static final ResourceLocation BACKGROUND_IMAGE = new ResourceLocation("Kagu/mainMenu.jpg");
	
	/**
	 * Called when the client starts
	 */
	public static void start() {
		
	}
	
	private static Vector4d backgroundColor = new Vector4d(0.0784313725, 0.0784313725, 0.0784313725, 1),
							buttonPanelBackgroundColor = new Vector4d(0.168627451, 0.168627451, 0.168627451, 1);
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		
		// Draw the background
		drawRect(0, 0, width, height, UiUtils.getColorFromVector(backgroundColor));
		
		// Button panel background
		UiUtils.drawRoundedRect(width * 0.05, height * 0.1, width * 0.4, height * 0.9, UiUtils.getColorFromVector(buttonPanelBackgroundColor), 10);
		
		
		
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
		
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		mc.displayGuiScreen(new GuiSelectWorld(this));
	}
	
}
