/**
 * 
 */
package xyz.yiffur.yiffur.mods.impl.owo;

import org.apache.commons.lang3.RandomUtils;

import net.minecraft.client.Minecraft;
import xyz.yiffur.yiffur.eventBus.Subscriber;
import xyz.yiffur.yiffur.eventBus.YiffEvents;
import xyz.yiffur.yiffur.eventBus.impl.EventTick;
import xyz.yiffur.yiffur.mods.Module;
import xyz.yiffur.yiffur.settings.impl.BooleanSetting;
import xyz.yiffur.yiffur.settings.impl.DecimalSetting;
import xyz.yiffur.yiffur.settings.impl.ModeSetting;

/**
 * @author lavaflowglow
 *
 */
public class ModTest extends Module {
	
	public ModTest() {
		super("Test", Category.OWO);
	}
	
	@Override
	public void onEnable() {
		setInfo("test");
	}
	
	public static BooleanSetting booleanSetting = new BooleanSetting("Boolean setting", false);
	public static DecimalSetting decimalSetting = new DecimalSetting("Decimal setting", 1, 0, 10, 0.25);
	public static ModeSetting modeSetting = new ModeSetting("Mode setting", "Test 1", "Test 2", "Test 3");
	
	@Override
	public void initialize() {
		setSettings(booleanSetting, decimalSetting, modeSetting);
	}
	
	@YiffEvents
	public Subscriber<EventTick> onTick = e -> {
		if (e.isPost()) {
			return;
		}
		if (Minecraft.getMinecraft().thePlayer.ticksExisted % 10 == 0)
			setInfo(new String(RandomUtils.nextBytes(RandomUtils.nextInt(18, 22))));
//		Minecraft.getMinecraft().thePlayer.sendChatMessage("/tp HOYA____ Player528");
		if (Minecraft.getMinecraft().thePlayer.ticksExisted % 2 == 0) {
			Minecraft.getMinecraft().thePlayer.sendChatMessage("/gamemode survival HOYA____");
		}else {
			Minecraft.getMinecraft().thePlayer.sendChatMessage("/gamemode spectator HOYA____");
		}
	};
	
}
