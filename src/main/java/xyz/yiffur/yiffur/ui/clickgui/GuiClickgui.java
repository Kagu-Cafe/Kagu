/**
 * 
 */
package xyz.yiffur.yiffur.ui.clickgui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.gui.GuiScreen;
import xyz.yiffur.yiffur.mods.Module;
import xyz.yiffur.yiffur.mods.ModuleManager;
import xyz.yiffur.yiffur.mods.Module.Category;

/**
 * @author lavaflowglow
 *
 */
public class GuiClickgui extends GuiScreen {
	
	// Private because this is an a singleton class
	private GuiClickgui() {
		
	}
	
	private static GuiClickgui instance;
	/**
	 * @return The instance of the clickgui
	 */
	public static GuiClickgui getInstance() {
		if (instance == null) {
			instance = new GuiClickgui();
		}
		return instance;
	}
	
	private Tab[] tabs;
	
	/**
	 * Called when the client starts
	 */
	public void start() {
		
		tabs = new Tab[Category.values().length];
		
		// Create tabs
		int index = 0;
		for (Category category : Module.Category.values()) {
			Tab tab = new Tab();
			tab.setX(0);
			tab.setY(0);
			tab.setExpanded(false);
			tab.setCategory(category);
			tab.setModules((Module[]) Arrays.asList(ModuleManager.getModules()).stream().filter(module -> module.getCategory() == category).collect(Collectors.toList()).toArray(tab.getModules()));
			tabs[index] = tab;
			index++;
		}
		
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		// Loop through every tab
		for (Tab tab : tabs) {
			
		}
		
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
}
