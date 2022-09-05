/**
 * 
 */
package cafe.kagu.kagu.ui.clickgui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cafe.kagu.kagu.eventBus.EventBus;
import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.Event.EventPosition;
import cafe.kagu.kagu.eventBus.impl.EventCheatRenderTick;
import cafe.kagu.kagu.eventBus.impl.EventKeyUpdate;
import cafe.kagu.kagu.mods.ModuleManager;
import cafe.kagu.kagu.mods.Module.Category;
import cafe.kagu.kagu.mods.impl.visual.ModClickGui;
import cafe.kagu.kagu.settings.Setting;
import cafe.kagu.kagu.settings.impl.KeybindSetting;
import cafe.kagu.kagu.utils.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
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
		bgImages.put("Astolfo 1", new BackgroundImage(dropdownImageFolder + "astolfo.png"));
		bgImages.put("Wolf O'Donnell", new BackgroundImage(dropdownImageFolder + "wolf_odonnell.png"));
		backgroundImage = bgImages.get(ModuleManager.modClickGui.getMode().getMode());
		resetBackgroundImage();
		
	}
	
	@Override
	public void initGui() {
		antiFuckupShittyMcCodeIsDogShitAndCantDoAnythingWithoutBreakingNinetyPercentOfMyShit = System.currentTimeMillis();
		selectedSetting = null;
		isLeftClick = false;
		isRightClick = false;
		Mouse.getDWheel();
	}
	
	@Override
	public void onGuiClosed() {
		selectedSetting = null;
		isLeftClick = false;
		isRightClick = false;
		ModuleManager.modClickGui.getMode().setMode("CS:GO");
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		BackgroundImage backgroundImage = this.backgroundImage;
		ModClickGui modClickGui = ModuleManager.modClickGui;
		
		// Draw background color
		drawRect(0, 0, width, height, backgroundImage.getSampledColor());
		
		// Draw background image
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
			case "Left From Right":{
				animationX = imageWidth * (1 - bgImageAnimation);
			}break;
			case "Diagonal From Corner":{
				animationX = imageWidth * (1 - bgImageAnimation);
				animationY = imageHeight * (1 - bgImageAnimation);
			}break;
		}
		drawTexture(width - imageWidth + animationX, height - imageHeight + animationY, imageWidth, imageHeight, true);
		
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
//		System.out.println(bgImageAnimation);
		
	};
	
	public void resetBackgroundImage() {
		backgroundImage = bgImages.get(ModuleManager.modClickGui.getBgImage().getMode());
	}
	
	private class Tab {
		
		/**
		 * The category for the tab
		 */
		public Tab(Category category) {
			this.category = category;
		}
		
		private Category category;
		private int offsetX = 0, offsetY = 0;
		private double expandAnimation = 0;

		/**
		 * @return the category
		 */
		public Category getCategory() {
			return category;
		}

		/**
		 * @param category the category to set
		 */
		public void setCategory(Category category) {
			this.category = category;
		}

		/**
		 * @return the offsetX
		 */
		public int getOffsetX() {
			return offsetX;
		}

		/**
		 * @param offsetX the offsetX to set
		 */
		public void setOffsetX(int offsetX) {
			this.offsetX = offsetX;
		}

		/**
		 * @return the offsetY
		 */
		public int getOffsetY() {
			return offsetY;
		}

		/**
		 * @param offsetY the offsetY to set
		 */
		public void setOffsetY(int offsetY) {
			this.offsetY = offsetY;
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
			
		}
		
		private final float ALPHA = 75 / 255f;
		private final float WHITE_ALPHA = 35 / 255f;
		
		private ResourceLocation resourceLocation;
		private int width, height, sampledColor;
		
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

	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		switch (mouseButton) {
			case 0:{
				isLeftClick = false;
			}break;
			case 1:{
				isRightClick = false;
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
	
}
