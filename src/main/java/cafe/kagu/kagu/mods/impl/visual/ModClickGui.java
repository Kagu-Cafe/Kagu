	/**
 * 
 */
package cafe.kagu.kagu.mods.impl.visual;

import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.impl.EventSettingUpdate;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.impl.BooleanSetting;
import cafe.kagu.kagu.settings.impl.DoubleSetting;
import cafe.kagu.kagu.settings.impl.ModeSetting;
import cafe.kagu.kagu.ui.clickgui.GuiCsgoClickgui;
import cafe.kagu.kagu.ui.clickgui.GuiDropdownClickgui;
import cafe.kagu.kagu.utils.ClickGuiUtils;

/**
 * @author lavaflowglow
 *
 */
public class ModClickGui extends Module {
	
	public ModClickGui() {
		super("ClickGui", Category.VISUAL);
		setSettings(mode, bgImage, bgImageAnimation, bgImageScale, bgImageFlip, resetClickGuiTabs);
	}
	
	private ModeSetting mode = new ModeSetting("Mode", "CS:GO", "CS:GO", "Dropdown");
	private ModeSetting bgImage = new ModeSetting("BG Image", "Fleur 1", "Fleur 1", "Fleur 2", "Distasteful", "Sylveon 1", "Vaporeon 1",
			"Astolfo 1", "Astolfo 2", "Astolfo 3", "Felix 1", "Felix 2", "Wolf O'Donnell", "Peter Griffin 1",
			"Peter Griffin 2", "Yoshi 1", "Yoshi 2", "Crazy Frog 1").setDependency(() -> mode.is("Dropdown"));
	private ModeSetting bgImageAnimation = new ModeSetting("BG Image Animation", "Up From Bottom", "None", "Up From Bottom", "From Side", "Diagonal From Corner").setDependency(() -> mode.is("Dropdown"));
	private DoubleSetting bgImageScale = new DoubleSetting("BG Image Scale", 1, 0.1, 4, 0.1).setDependency(() -> mode.is("Dropdown"));
	private BooleanSetting bgImageFlip = new BooleanSetting("Flip BG Image", false).setDependency(() -> mode.is("Dropdown"));
	private BooleanSetting resetClickGuiTabs = new BooleanSetting("Reset Tabs", false).setDependency(() -> mode.is("Dropdown"));
	
	@EventHandler
	private Handler<EventSettingUpdate> onSettingUpdate = e -> {
		if (e.getSetting() == bgImage) {
			GuiDropdownClickgui.getInstance().resetBackgroundImage();
		}
		else if (e.getSetting() == resetClickGuiTabs) {
			if (resetClickGuiTabs.isEnabled()) {
				GuiDropdownClickgui.getInstance().resetTabs();
				resetClickGuiTabs.toggle();
			}
		}
		else if (e.getSetting() != mode)
			return;
//		switch (mode.getMode()) {
//			case "CS:GO":{
//				mc.displayGuiScreen(GuiCsgoClickgui.getInstance());
//			}break;
//			case "Dropdown":{
//				mc.displayGuiScreen(GuiDropdownClickgui.getInstance());
//			}break;
//		}
	};
	
	@Override
	public boolean isEnabled() {
		return false;
	}
	
	@Override
	public boolean isDisabled() {
		return true;
	}
	
	@Override
	public void toggle() {
		if (mc.getCurrentScreen() == null) {
			switch (mode.getMode()) {
				case "CS:GO":{
					mc.displayGuiScreen(GuiCsgoClickgui.getInstance());
				}break;
				case "Dropdown":{
					mc.displayGuiScreen(GuiDropdownClickgui.getInstance());
				}break;
			}
		}
		else if (ClickGuiUtils.isInClickGui()) {
			mc.displayGuiScreen(null);
		}
	}
	
	@Override
	public void enable() {
		
	}
	
	@Override
	public void disable() {
		
	}
	
	/**
	 * @return the mode
	 */
	public ModeSetting getMode() {
		return mode;
	}
	
	/**
	 * @return the bgImage
	 */
	public ModeSetting getBgImage() {
		return bgImage;
	}
	
	/**
	 * @return the bgImageScale
	 */
	public DoubleSetting getBgImageScale() {
		return bgImageScale;
	}
	
	/**
	 * @return the bgImageAnimation
	 */
	public ModeSetting getBgImageAnimation() {
		return bgImageAnimation;
	}
	
	/**
	 * @return the bgImageFlip
	 */
	public BooleanSetting getBgImageFlip() {
		return bgImageFlip;
	}
	
}
