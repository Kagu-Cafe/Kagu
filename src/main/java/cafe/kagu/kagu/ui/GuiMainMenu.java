package cafe.kagu.kagu.ui;

import java.io.IOException;

import cafe.kagu.kagu.Kagu;
import cafe.kagu.kagu.font.FontUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.util.ResourceLocation;

public class GuiMainMenu extends GuiScreen {
	
	public static final ResourceLocation BACKGROUND_IMAGE = new ResourceLocation("Kagu/mainMenu.jpg");
	
	/**
	 * Called when the client starts
	 */
	public static void start() {
		
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		// Draw the background
		mc.getTextureManager().bindTexture(BACKGROUND_IMAGE);
		drawModalRectWithCustomSizedTexture(0, 0, 0, 0, width, height, width, height);
		
		// Draw title
		FontUtils.ROBOTO_LIGHT_48.drawString(Kagu.getName(), width / 20, (height / 20) * 3, -1);
		
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		mc.displayGuiScreen(new GuiSelectWorld(this));
	}
	
}
