/**
 * 
 */
package cafe.kagu.kagu.ui.gui;

import java.io.IOException;

import javax.vecmath.Vector4d;

import cafe.kagu.kagu.font.FontRenderer;
import cafe.kagu.kagu.font.FontUtils;
import cafe.kagu.kagu.utils.SoundUtils;
import cafe.kagu.kagu.utils.UiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

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
			buttonDefaultColor = UiUtils.getColorFromVector(new Vector4d(0.211764706, 0.211764706, 0.211764706, 1)),
			mojangButtonColor = UiUtils.getColorFromVector(new Vector4d(0.937254902, 0.196078431, 0.239215686, 1)),
			mojangButtonHoverColor = UiUtils.getColorFromVector(new Vector4d(0.831372549, 0.062745098, 0.105882353, 1)),
			microsoftButtonColor = UiUtils.getColorFromVector(new Vector4d(1, 1, 1, 1)),
			microsoftButtonHoverColor = UiUtils.getColorFromVector(new Vector4d(0.788235294, 0.788235294, 0.788235294, 1)),
			microsoftFontColor = UiUtils.getColorFromVector(new Vector4d(0.454901961, 0.454901961, 0.454901961, 1));
	private static boolean leftMouseClicked = false;
	private static ResourceLocation defaultSteve3D = new ResourceLocation("Kagu/altManager/steve 3d.png"),
									defaultSteveFace = new ResourceLocation("Kagu/altManager/steve face.png"),
									mojangLogo = new ResourceLocation("Kagu/altManager/mojang.png"),
									microsoftLogo = new ResourceLocation("Kagu/altManager/microsoft.png");
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		double buttonWidth = width * 0.2334375, buttonHeight = height * 0.0925925926, buttonImageSize = height * 0.0653333333,
				buttonImageMargin = (buttonHeight - buttonImageSize) / 2;
		double buttonMargin = 7;
		double buttonCurve = 5;
		double playerScale = 1;
		double playerHeight = buttonWidth * playerScale * 1.62025316;
		FontRenderer mojangAltFont = FontUtils.MOJANG_LOGO_20;
		FontRenderer microsoftAltFont = FontUtils.MICROSOFT_LOGO_16;
		FontRenderer altNameFont = FontUtils.SMALL_PIXEL_16;
		FontRenderer altInfoFont = FontUtils.SMALL_PIXEL_10;
		
		drawRect(0, 0, width, height, backgroundColor);
		
		// Add premium alt button
		int mojangButtonColor = GuiAltManager.mojangButtonColor;
		if (UiUtils.isMouseInsideRoundedRect(mouseX, mouseY, width - buttonWidth - buttonMargin, height - buttonHeight - buttonMargin, width - buttonMargin, height - buttonMargin, buttonCurve)) {
			if (leftMouseClicked) {
				mc.getSoundHandler().playSound(SoundUtils.getClickSound());
				leftMouseClicked = false;
			}
			mojangButtonColor = GuiAltManager.mojangButtonHoverColor;
		}
		UiUtils.drawRoundedRect(width - buttonWidth - buttonMargin, height - buttonHeight - buttonMargin, width - buttonMargin, height - buttonMargin, mojangButtonColor, buttonCurve);
		mc.getTextureManager().bindTexture(mojangLogo);
		Gui.drawTexture(width - buttonWidth - buttonMargin + buttonImageMargin, height - buttonHeight - buttonMargin + buttonImageMargin, buttonImageSize, buttonImageSize);
		mojangAltFont.drawCenteredString("Add Premium Alt", width - buttonWidth - buttonMargin + buttonImageMargin + buttonImageSize + ((buttonWidth - buttonImageSize - buttonImageMargin * 2) / 2),
				height - buttonHeight / 2 - buttonMargin - mojangAltFont.getFontHeight() / 2 + 2, 0xffffffff);
		
		// Add microsoft alt button
		int microsoftButtonColor = GuiAltManager.microsoftButtonColor;
		if (UiUtils.isMouseInsideRoundedRect(mouseX, mouseY, width - buttonWidth * 2 - buttonMargin * 2, height - buttonHeight - buttonMargin, width - buttonMargin * 2 - buttonWidth, height - buttonMargin, buttonCurve)) {
			if (leftMouseClicked) {
				mc.getSoundHandler().playSound(SoundUtils.getClickSound());
				leftMouseClicked = false;
			}
			microsoftButtonColor = GuiAltManager.microsoftButtonHoverColor;
		}
		UiUtils.drawRoundedRect(width - buttonWidth * 2 - buttonMargin * 2, height - buttonHeight - buttonMargin, width - buttonMargin * 2 - buttonWidth, height - buttonMargin, microsoftButtonColor, buttonCurve);
		mc.getTextureManager().bindTexture(microsoftLogo);
		Gui.drawTexture(width - buttonWidth * 2 - buttonMargin * 2 + buttonImageMargin, height - buttonHeight - buttonMargin + buttonImageMargin, buttonImageSize, buttonImageSize);
		microsoftAltFont.drawCenteredString("Add Microsoft Alt", width - buttonWidth * 2 - buttonMargin * 2 + buttonImageMargin + buttonImageSize + ((buttonWidth - buttonImageSize - buttonImageMargin * 2) / 2),
				height - buttonHeight / 2 - buttonMargin - microsoftAltFont.getFontHeight() / 2 - 2, microsoftFontColor);
		
		// Alt skin
		mc.getTextureManager().bindTexture(defaultSteve3D);
		Gui.drawTexture(width - buttonWidth - buttonMargin * 1.5 - buttonWidth * playerScale / 2, height - buttonHeight - buttonMargin * 2 - playerHeight, buttonWidth * playerScale, playerHeight);
		
		// Alt username and other info
		altNameFont.drawCenteredString(mc.getSession().getUsername(), width - buttonWidth - buttonMargin * 1.5 + buttonWidth * playerScale * 0.025, height - buttonHeight - buttonMargin * 2 - playerHeight - altNameFont.getFontHeight() * 2, 0xffffffff);
		altInfoFont.drawCenteredString(mc.getSession().getToken().equals("0") ? "Cracked" : "Premium", width - buttonWidth - buttonMargin * 1.5 + buttonWidth * playerScale * 0.025, height - buttonHeight - buttonMargin * 2 - playerHeight - altNameFont.getFontHeight(), mc.getSession().getToken().equals("0") ? 0xffff0000 : 0xff00ff00);
		
		if (leftMouseClicked)
			leftMouseClicked = false;
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseButton == 0)
			leftMouseClicked = true;
	}
	
}
