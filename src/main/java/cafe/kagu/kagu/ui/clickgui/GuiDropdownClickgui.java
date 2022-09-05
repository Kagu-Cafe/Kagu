/**
 * 
 */
package cafe.kagu.kagu.ui.clickgui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cafe.kagu.kagu.eventBus.Event.EventPosition;
import cafe.kagu.kagu.eventBus.EventBus;
import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.impl.EventCheatRenderTick;
import cafe.kagu.kagu.eventBus.impl.EventKeyUpdate;
import cafe.kagu.kagu.font.FontRenderer;
import cafe.kagu.kagu.font.FontUtils;
import cafe.kagu.kagu.mods.Module.Category;
import cafe.kagu.kagu.mods.ModuleManager;
import cafe.kagu.kagu.mods.impl.visual.ModClickGui;
import cafe.kagu.kagu.settings.Setting;
import cafe.kagu.kagu.settings.impl.KeybindSetting;
import cafe.kagu.kagu.utils.UiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * @author lavaflowglow
 *
 */
public class GuiDropdownClickgui extends GuiScreen {
	
	private GuiDropdownClickgui() {
		
	}
	
	private static GuiDropdownClickgui instance;
	/**
	 * @return The instance of the clickgui
	 */
	public static GuiDropdownClickgui getInstance() {
		if (instance == null) {
			instance = new GuiDropdownClickgui();
		}
		return instance;
	}
	
	private HashMap<String, BackgroundImage> bgImages = new HashMap<>();
	private boolean isLeftClick = false, isRightClick = false;
	private Setting<?> selectedSetting = null;
	private long antiFuckupShittyMcCodeIsDogShitAndCantDoAnythingWithoutBreakingNinetyPercentOfMyShit = System.currentTimeMillis();
	private BackgroundImage backgroundImage;
	private double bgImageAnimation = 0;
	private FontRenderer tabTitleFontRenderer = FontUtils.STRATUM2_MEDIUM_13_AA;
	private FontRenderer tabModuleFontRenderer = FontUtils.STRATUM2_REGULAR_8_AA;
	private final Tab[] TABS = new Tab[Category.values().length];
	private int[] mouseOffsets = new int[2];
	private Tab draggedTab = null;
	
	private final int TAB_CORNER_SIZE = 5;
	
	/**
	 * Called when the client starts
	 */
	public void start() {
		EventBus.setSubscriber(this, true);
		
		// Populate the background image resource location data map
		String dropdownImageFolder = "Kagu/dropdownClickgui/bgImage/";
		bgImages.put("Fleur 1", new BackgroundImage(dropdownImageFolder + "fleur1.png"));
		bgImages.put("Fleur 2", new BackgroundImage(dropdownImageFolder + "fleur2.png"));
		bgImages.put("Distasteful", new BackgroundImage(dropdownImageFolder + "dark.png"));
		bgImages.put("Astolfo 1", new BackgroundImage(dropdownImageFolder + "astolfo1.png"));
		bgImages.put("Astolfo 2", new BackgroundImage(dropdownImageFolder + "astolfo2.png"));
		bgImages.put("Astolfo 3", new BackgroundImage(dropdownImageFolder + "astolfo3.png"));
		bgImages.put("Felix 1", new BackgroundImage(dropdownImageFolder + "felix1.png"));
		bgImages.put("Felix 2", new BackgroundImage(dropdownImageFolder + "felix2.png"));
		bgImages.put("Wolf O'Donnell", new BackgroundImage(dropdownImageFolder + "wolf_odonnell.png"));
		bgImages.put("Peter Griffin 1", new BackgroundImage(dropdownImageFolder + "peter1.png"));
		bgImages.put("Peter Griffin 2", new BackgroundImage(dropdownImageFolder + "peter2.png"));
		backgroundImage = bgImages.get(ModuleManager.modClickGui.getMode().getMode());
		resetBackgroundImage();
		
		// Create all the tabs
		int index = 0;
		for (Category category : Category.values()) {
			TABS[index++] = new Tab(category);
		}
		
	}
	
	@Override
	public void initGui() {
		antiFuckupShittyMcCodeIsDogShitAndCantDoAnythingWithoutBreakingNinetyPercentOfMyShit = System.currentTimeMillis();
		selectedSetting = null;
		isLeftClick = false;
		isRightClick = false;
		draggedTab = null;
		Mouse.getDWheel();
		
		// Position the tabs in a semi neat order if they're unset
		boolean setTabPositions = true;
		for (Tab tab : TABS) {
			if (tab.getPosX() != 0 || tab.getPosY() != 0)
				setTabPositions = false;
		}
		if (setTabPositions) {
			resetTabs();
		}
		
	}
	
	@Override
	public void onGuiClosed() {
		selectedSetting = null;
		isLeftClick = false;
		isRightClick = false;
		draggedTab = null;
		ModuleManager.modClickGui.getMode().setMode("CS:GO");
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		GlStateManager.pushMatrix();
		GlStateManager.pushAttrib();
		
		BackgroundImage backgroundImage = this.backgroundImage;
		ModClickGui modClickGui = ModuleManager.modClickGui;
		
		// Draw background color
		drawRect(0, 0, width, height, backgroundImage.getSampledColor());
		
		// Draw background image
		boolean isFlipBgImage = modClickGui.getBgImageFlip().isEnabled();
		double bgImageScale = (height * 0.5 / backgroundImage.getHeight()) * modClickGui.getBgImageScale().getValue();
		mc.getTextureManager().bindTexture(backgroundImage.getResourceLocation());
		double imageWidth = backgroundImage.getWidth() * bgImageScale;
		double imageHeight = backgroundImage.getHeight() * bgImageScale;
		double animationX = 0;
		double animationY = 0;
		switch(modClickGui.getBgImageAnimation().getMode()) {
			case "Up From Bottom":{
				animationY = imageHeight * (1 - bgImageAnimation);
			}break;
			case "From Side":{
				animationX = imageWidth * (1 - bgImageAnimation);
			}break;
			case "Diagonal From Corner":{
				animationX = imageWidth * (1 - bgImageAnimation);
				animationY = imageHeight * (1 - bgImageAnimation);
			}break;
		}
		if (isFlipBgImage) {
//			GlStateManager.pushMatrix();
//			GlStateManager.disableCull();
//			GL11.glDisable(GL11.GL_CULL_FACE);
//			GlStateManager.translate(imageWidth - (imageWidth - animationX) + imageWidth / 2, 0, 0);
//			GlStateManager.scale(-1, 1, 1);
//			GlStateManager.translate(-(imageWidth - (imageWidth - animationX) + imageWidth / 2), 0, 0);
			drawTexture(-(animationX), height - imageHeight + animationY, imageWidth, imageHeight, true);
//			GlStateManager.popMatrix();
		}else {
			drawTexture(width - imageWidth + animationX, height - imageHeight + animationY, imageWidth, imageHeight, true);
		}
		
		// Draw all the tabs
		GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
		int offsetUnits = TABS.length;
		final int tabTitleColor = 0xff0a0611;
		final int textColor = -1;
		FontRenderer tabTitleFontRenderer = this.tabTitleFontRenderer;
		double yOffset = 0;
		int coolColor = backgroundImage.getSampleSolidColor();
		for (Tab tab : TABS) {
			
			if (draggedTab == tab) {
				tab.setPosX(mouseX - mouseOffsets[0]);
				tab.setPosY(mouseY - mouseOffsets[1]);
			}
			
			GlStateManager.pushMatrix();
			GlStateManager.pushAttrib();
			GlStateManager.translate(tab.getPosX(), tab.getPosY(), 0);
			yOffset = 0;
			double tabWidth = tab.getWidth();
			GL11.glPolygonOffset(1f, -(offsetUnits--)); // Makes the tabs render in the correct order using polygon offsets
			
			// Title
			UiUtils.drawRoundedRect(0, yOffset, tabWidth, yOffset += (tabTitleFontRenderer.getFontHeight() + TAB_CORNER_SIZE * 2), tabTitleColor, TAB_CORNER_SIZE, TAB_CORNER_SIZE, 0d, 0d);
//			tabTitleFontRenderer.drawCenteredString(tab.getCategory().getName(), tab.getWidth() / 2, TAB_CORNER_SIZE, -1);
			tabTitleFontRenderer.drawString(tab.getCategory().getName(), TAB_CORNER_SIZE, TAB_CORNER_SIZE, textColor);
			if ((isLeftClick || isRightClick) && UiUtils.isMouseInsideRoundedRect(mouseX - tab.getPosX(), mouseY - tab.getPosY(), 0, 0, tab.getWidth(), tabTitleFontRenderer.getFontHeight() + TAB_CORNER_SIZE * 2, TAB_CORNER_SIZE, 0)) {
				if (isLeftClick) {
					mouseOffsets[0] = mouseX - tab.getPosX();
					mouseOffsets[1] = mouseY - tab.getPosY();
					draggedTab = tab;
					isLeftClick = false;
				}else if (isRightClick) {
					tab.setExpanded(!tab.isExpanded());
					isRightClick = false;
				}
			}
			
			// Footer
			drawRect(0, yOffset, tabWidth, yOffset + 1, coolColor);
			if ((isLeftClick || isRightClick) && UiUtils.isMouseInsideRoundedRect(mouseX - tab.getPosX(), mouseY - tab.getPosY(), 0, yOffset + 1 ,tabWidth, yOffset + TAB_CORNER_SIZE * 2, 0, TAB_CORNER_SIZE)) {
				if (isLeftClick) {
					mouseOffsets[0] = mouseX - tab.getPosX();
					mouseOffsets[1] = mouseY - tab.getPosY();
					draggedTab = tab;
					isLeftClick = false;
				}else if (isRightClick) {
					tab.setExpanded(!tab.isExpanded());
					isRightClick = false;
				}
			}
			UiUtils.drawRoundedRect(0, yOffset + 1 ,tabWidth, yOffset += (TAB_CORNER_SIZE * 2), tabTitleColor, 0d, 0d, TAB_CORNER_SIZE, TAB_CORNER_SIZE);
			
			GlStateManager.popAttrib();
			GlStateManager.popMatrix();
			
		}
		GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
		
		GlStateManager.popAttrib();
		GlStateManager.popMatrix();
		
		isLeftClick = false;
		isRightClick = false;
	}
	
	@EventHandler
	private Handler<EventCheatRenderTick> onCheatRenderTick = e -> {
		if (e.isPost())
			return;
		
		double animationSpeed = 0.1;
		Minecraft mc = Minecraft.getMinecraft();
		
		if (mc.getCurrentScreen() instanceof GuiDropdownClickgui) {
			bgImageAnimation += (1 - bgImageAnimation) * animationSpeed;
		}else {
			bgImageAnimation -= bgImageAnimation * animationSpeed;
		}
		
	};
	
	/**
	 * Rechecks and sets the background image
	 */
	public void resetBackgroundImage() {
		backgroundImage = bgImages.get(ModuleManager.modClickGui.getBgImage().getMode());
	}
	
	private class Tab {
		
		/**
		 * The category for the tab
		 */
		public Tab(Category category) {
			this.category = category;
			width = (int)Math.ceil(tabTitleFontRenderer.getStringWidth(category.getName()) * 2);
		}
		
		private Category category;
		private int posX = 0, posY = 0, width = 0;
		private double expandAnimation = 0;
		private boolean expanded = false;

		/**
		 * @return the posX
		 */
		public int getPosX() {
			return posX;
		}

		/**
		 * @param posX the posX to set
		 */
		public void setPosX(int posX) {
			this.posX = posX;
		}

		/**
		 * @return the posY
		 */
		public int getPosY() {
			return posY;
		}

		/**
		 * @param posY the posY to set
		 */
		public void setPosY(int posY) {
			this.posY = posY;
		}

		/**
		 * @return the expandAnimation
		 */
		public double getExpandAnimation() {
			return expandAnimation;
		}

		/**
		 * @param expandAnimation the expandAnimation to set
		 */
		public void setExpandAnimation(double expandAnimation) {
			this.expandAnimation = expandAnimation;
		}

		/**
		 * @return the expanded
		 */
		public boolean isExpanded() {
			return expanded;
		}

		/**
		 * @param expanded the expanded to set
		 */
		public void setExpanded(boolean expanded) {
			this.expanded = expanded;
		}

		/**
		 * @return the category
		 */
		public Category getCategory() {
			return category;
		}

		/**
		 * @return the width
		 */
		public int getWidth() {
			return width;
		}

	}
	
	private class BackgroundImage {
		
		public BackgroundImage(String resourceLocation) {
			this.resourceLocation = new ResourceLocation(resourceLocation);
			
			// Load the image, sample it for the average color, save color and cleanup and streams used
			InputStream in = GuiDropdownClickgui.class.getClassLoader().getResourceAsStream("assets/minecraft/" + resourceLocation);
			if (in == null) {
				throw new IllegalArgumentException("The resource location provided does not point to any real resource, please double check the resource path");
			}
			
			// Load the image from the stream
			BufferedImage bufferedImage;
			try {
				bufferedImage = ImageIO.read(in);
			} catch (IOException e1) {
				e1.printStackTrace();
				try {
					in.close();
				} catch (Exception e) {
					
				}
				return;
			}
			
			// Close the stream
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// Sample the image for the average color
			long[] data = new long[4];
			int width = bufferedImage.getWidth(), height = bufferedImage.getHeight();
			this.width = width; this.height = height;
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					Color pixel = new Color(bufferedImage.getRGB(x, y));
					if (pixel.getAlpha() <= 10)
						continue;
					data[0] += pixel.getRed();
					data[1] += pixel.getGreen();
					data[2] += pixel.getBlue();
					data[3]++;
				}
			}
			
			// Calculate and save the sampled color
			this.sampledColor = new Color((data[0] / data[3]) / 255f, (data[1] / data[3]) / 255f, (data[2] / data[3]) / 255f, 
					((data[0] / data[3]) / 255f + (data[1] / data[3]) / 255f + (data[2] / data[3]) / 255f) > 2.2 ? WHITE_ALPHA : ALPHA).getRGB();
			this.sampleSolidColor = new Color((data[0] / data[3]) / 255f, (data[1] / data[3]) / 255f, (data[2] / data[3]) / 255f, 1).getRGB();
			
		}
		
		private final float ALPHA = 75 / 255f;
		private final float WHITE_ALPHA = 35 / 255f;
		
		private ResourceLocation resourceLocation;
		private int width, height, sampledColor, sampleSolidColor;
		
		/**
		 * @return the resourceLocation
		 */
		public ResourceLocation getResourceLocation() {
			return resourceLocation;
		}

		/**
		 * @return the width
		 */
		public int getWidth() {
			return width;
		}

		/**
		 * @return the height
		 */
		public int getHeight() {
			return height;
		}

		/**
		 * @return the sampledColor
		 */
		public int getSampledColor() {
			return sampledColor;
		}
		
		/**
		 * @return the sampleSolidColor
		 */
		public int getSampleSolidColor() {
			return sampleSolidColor;
		}
		
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		switch (mouseButton) {
			case 0:{
				isLeftClick = true;
			}break;
			case 1:{
				isRightClick = true;
			}break;
		}
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		switch (state) {
			case 0:{
				draggedTab = null;
			}break;
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		
		if (selectedSetting instanceof KeybindSetting) {
			((KeybindSetting)selectedSetting).setKeybind(keyCode == Keyboard.KEY_ESCAPE ? Keyboard.KEY_NONE : keyCode);
			selectedSetting = null;
			return;
		}
		
		if (keyCode == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(null);
			return;
		}
		
		if (System.currentTimeMillis() - antiFuckupShittyMcCodeIsDogShitAndCantDoAnythingWithoutBreakingNinetyPercentOfMyShit < 100)
			return;
		
        // Kagu hook
        {
        	EventKeyUpdate eventKeyUpdate = new EventKeyUpdate(EventPosition.PRE, keyCode, true);
        	eventKeyUpdate.post();
        	if (eventKeyUpdate.isCanceled()) {
        		return;
        	}
        }
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	/**
	 * Resets the tab positioning
	 */
	public void resetTabs() {
		if (ModuleManager.modClickGui.getBgImageFlip().isEnabled()) {
			int spaceShift = width - 25;
			ArrayList<Tab> tempTabs = new ArrayList<>(Arrays.asList(TABS));
			Collections.reverse(tempTabs);
			for (Tab tab : tempTabs) {
				tab.setPosX(spaceShift - tab.getWidth());
				tab.setPosY(25);
				spaceShift -= (tab.getWidth() * 1.1);
			}
		}else {
			int spaceShift = 25;
			for (Tab tab : TABS) {
				tab.setPosX(spaceShift);
				tab.setPosY(25);
				spaceShift += (tab.getWidth() * 1.1);
			}
		}
	}
	
}
