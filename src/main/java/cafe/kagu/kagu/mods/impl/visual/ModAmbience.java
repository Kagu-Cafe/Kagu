/**
 * 
 */
package cafe.kagu.kagu.mods.impl.visual;

import java.util.HashMap;

import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.impl.EventPacketReceive;
import cafe.kagu.kagu.eventBus.impl.EventSettingUpdate;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.impl.BooleanSetting;
import cafe.kagu.kagu.settings.impl.IntegerSetting;
import cafe.kagu.kagu.settings.impl.ModeSetting;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.util.ResourceLocation;

/**
 * @author lavaflowglow
 *
 */
public class ModAmbience extends Module {
	
	public ModAmbience() {
		super("Ambience", Category.VISUAL);
		setSettings(blockLighting, worldTime, disableNightVision, customSkyColor, skyRed, skyGreen, skyBlue);
		for (ResourceLocation key : Block.blockRegistry.getKeys()) {
			lightValues.put(Block.blockRegistry.getObject(key), Block.blockRegistry.getObject(key).getLightValue());
		}
	}
	
	private ModeSetting blockLighting = new ModeSetting("Block Lighting", "Unchanged", "Unchanged", "Midnight", "Dusk", "Day");
	private ModeSetting worldTime = new ModeSetting("World Time", "Unchanged", "Unchanged", "Midnight", "Dusk", "Day");
	private BooleanSetting disableNightVision = new BooleanSetting("Disable Night Vision", true);
	private BooleanSetting customSkyColor = new BooleanSetting("Custom Sky Color", true);
	private IntegerSetting skyRed = new IntegerSetting("Sky R", 213, 0, 255, 1).setDependency(customSkyColor::isEnabled);
	private IntegerSetting skyGreen = new IntegerSetting("Sky G", 140, 0, 255, 1).setDependency(customSkyColor::isEnabled);
	private IntegerSetting skyBlue = new IntegerSetting("Sky B", 255, 0, 255, 1).setDependency(customSkyColor::isEnabled);
	
	private HashMap<Block, Integer> lightValues = new HashMap<>();
	
	@Override
	public void onEnable() {
		for (Block block : lightValues.keySet()) {
			switch (blockLighting.getMode()) {
        		case "Unchanged":block.setLightValue(lightValues.get(block));break;
        		case "Midnight":block.setLightValue(0);break;
        		case "Dusk":block.setLightValue(8);break;
        		case "Day":block.setLightValue(15);break;
			}
		}
		mc.renderGlobal.loadRenderers();
	}
	
	@Override
	public void onDisable() {
		for (Block block : lightValues.keySet()) {
			switch (blockLighting.getMode()) {
        		case "Unchanged":block.setLightValue(lightValues.get(block));break;
        		case "Midnight":block.setLightValue(0);break;
        		case "Dusk":block.setLightValue(8);break;
        		case "Day":block.setLightValue(15);break;
			}
		}
		mc.renderGlobal.loadRenderers();
	}
	
	@EventHandler
	private Handler<EventSettingUpdate> onSettingUpdate = e -> {
		if (e.getSetting() == blockLighting) {
			for (Block block : lightValues.keySet()) {
				switch (blockLighting.getMode()) {
	        		case "Unchanged":block.setLightValue(lightValues.get(block));break;
	        		case "Midnight":block.setLightValue(0);break;
	        		case "Dusk":block.setLightValue(8);break;
	        		case "Day":block.setLightValue(15);break;
				}
			}
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
	 * @return the disableNightVision
	 */
	public BooleanSetting getDisableNightVision() {
		return disableNightVision;
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
