/**
 * 
 */
package cafe.kagu.kagu.mods.impl.visual;

import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.impl.EventSettingUpdate;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.impl.ModeSetting;

/**
 * @author lavaflowglow
 *
 */
public class ModAmbience extends Module {
	
	public ModAmbience() {
		super("Ambience", Category.VISUAL);
		setSettings(blockLighting, worldTime);
	}
	
	private ModeSetting blockLighting = new ModeSetting("Block Lighting", "Unchanged", "Unchanged", "Midnight", "Dusk", "Day");
	private ModeSetting worldTime = new ModeSetting("World Time", "Unchanged", "Unchanged", "Midnight", "Dusk", "Day");
	
	@Override
	public void onEnable() {
		mc.renderGlobal.loadRenderers();
	}
	
	@Override
	public void onDisable() {
		mc.renderGlobal.loadRenderers();
	}
	
	@EventHandler
	private Handler<EventSettingUpdate> onSettingUpdate = e -> {
		if (e.getSetting() == blockLighting) {
			mc.renderGlobal.loadRenderers();
		}
	};
	
	/**
	 * @return the blockLighting
	 */
	public ModeSetting getBlockLighting() {
		return blockLighting;
	}
	
	/**
	 * @return the worldTime
	 */
	public ModeSetting getWorldTime() {
		return worldTime;
	}
	
}
