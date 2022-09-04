/**
 * 
 */
package cafe.kagu.kagu.ui.clickgui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import cafe.kagu.kagu.eventBus.EventBus;
import cafe.kagu.kagu.eventBus.Event.EventPosition;
import cafe.kagu.kagu.eventBus.impl.EventKeyUpdate;
import cafe.kagu.kagu.settings.Setting;
import cafe.kagu.kagu.settings.impl.KeybindSetting;
import net.minecraft.client.gui.GuiScreen;

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
	
	/**
	 * Called when the client starts
	 */
	public void start() {
		EventBus.setSubscriber(this, true);
	}
	
	private boolean isLeftClick = false, isRightClick = false;
	private Setting<?> selectedSetting = null;
	private long antiFuckupShittyMcCodeIsDogShitAndCantDoAnythingWithoutBreakingNinetyPercentOfMyShit = System.currentTimeMillis();
	
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
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		isLeftClick = false;
		isRightClick = false;
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
	
	private class Tab {
		
	}
	
}
