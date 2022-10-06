/**
 * 
 */
package cafe.kagu.kagu.mods.impl.visual;

import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.impl.EventPacketReceive;
import cafe.kagu.kagu.eventBus.impl.EventSettingUpdate;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.impl.BooleanSetting;
import cafe.kagu.kagu.settings.impl.IntegerSetting;
import cafe.kagu.kagu.settings.impl.ModeSetting;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

/**
 * @author lavaflowglow
 *
 */
public class ModAmbience extends Module {
	
	public ModAmbience() {
		super("Ambience", Category.VISUAL);
		setSettings(blockLighting, worldTime, customSkyColor, skyRed, skyGreen, skyBlue);
	}
	
	private ModeSetting blockLighting = new ModeSetting("Block Lighting", "Unchanged", "Unchanged", "Midnight", "Dusk", "Day");
	private ModeSetting worldTime = new ModeSetting("World Time", "Unchanged", "Unchanged", "Midnight", "Dusk", "Day");
	private BooleanSetting customSkyColor = new BooleanSetting("Custom Sky Color", true);
	private IntegerSetting skyRed = new IntegerSetting("Sky R", 213, 0, 255, 1).setDependency(customSkyColor::isEnabled);
	private IntegerSetting skyGreen = new IntegerSetting("Sky G", 140, 0, 255, 1).setDependency(customSkyColor::isEnabled);
	private IntegerSetting skyBlue = new IntegerSetting("Sky B", 255, 0, 255, 1).setDependency(customSkyColor::isEnabled);
	
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
	
	@EventHandler
	private Handler<EventPacketReceive> onPacketReceive = e -> {
		if (e.isPost() || worldTime.is("Unchanged") || !(e.getPacket() instanceof S03PacketTimeUpdate))
			return;
		e.cancel();
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
	
	/**
	 * @return the customSkyColor
	 */
	public BooleanSetting getCustomSkyColor() {
		return customSkyColor;
	}
	
	/**
	 * @return the skyRed
	 */
	public IntegerSetting getSkyRed() {
		return skyRed;
	}
	
	/**
	 * @return the skyGreen
	 */
	public IntegerSetting getSkyGreen() {
		return skyGreen;
	}
	
	/**
	 * @return the skyBlue
	 */
	public IntegerSetting getSkyBlue() {
		return skyBlue;
	}
	
}
