/**
 * 
 */
package xyz.yiffur.yiffur.mods.impl.misc;

import org.apache.commons.lang3.RandomUtils;

import net.minecraft.client.Minecraft;
import xyz.yiffur.yiffur.eventBus.Subscriber;
import xyz.yiffur.yiffur.eventBus.YiffEvents;
import xyz.yiffur.yiffur.eventBus.impl.EventTick;
import xyz.yiffur.yiffur.mods.Module;
import xyz.yiffur.yiffur.settings.impl.BooleanSetting;
import xyz.yiffur.yiffur.settings.impl.DecimalSetting;
import xyz.yiffur.yiffur.settings.impl.IntegerSetting;
import xyz.yiffur.yiffur.settings.impl.LongSetting;
import xyz.yiffur.yiffur.settings.impl.ModeSetting;

/**
 * @author lavaflowglow
 *
 */
public class ModTest extends Module {
	
	public ModTest() {
		super("Test", Category.MISC);
	}
	
	@Override
	public void onEnable() {
		setInfo("test");
	}
	
	public static BooleanSetting booleanSetting = new BooleanSetting("Boolean setting", false);
	public static DecimalSetting decimalSetting1 = new DecimalSetting("Decimal setting 1", 1, 0, 10, 0.25), 
								 decimalSetting2 = new DecimalSetting("Decimal setting 2", 1, -10, 10, 0.25), 
								 decimalSetting3 = new DecimalSetting("Decimal setting 3", 10, 10, 20, 0.25);
	public static IntegerSetting integerSetting1 = new IntegerSetting("Integer setting 1", 1, 0, 10, 1), 
								 integerSetting2 = new IntegerSetting("Integer setting 2", 1, -10, 10, 1), 
								 integerSetting3 = new IntegerSetting("Integer setting 3", 10, 10, 20, 1);
	public static LongSetting longSetting1 = new LongSetting("Long setting 1", 1, 0, 10, 1), 
							  longSetting2 = new LongSetting("Long setting 2", 1, -10, 10, 1), 
							  longSetting3 = new LongSetting("Long setting 3", 10, 10, 20, 1);
	public static ModeSetting modeSetting1 = new ModeSetting("Mode setting 1", "Test 1", "Test 1", "Test 2", "Test 3"),
							  modeSetting2 = new ModeSetting("Mode setting 2", "Test 1", "Test 1"),
							  modeSetting3 = new ModeSetting("Mode setting 3", "Test 1");
	
	@Override
	public void initialize() {
		setSettings(booleanSetting, decimalSetting1, decimalSetting2, decimalSetting3, modeSetting1, modeSetting2, modeSetting3, integerSetting1, integerSetting2, integerSetting3, longSetting1, longSetting2, longSetting3);
	}
	
	@YiffEvents
	public Subscriber<EventTick> onTick = e -> {
		if (e.isPost()) {
			return;
		}
		if (Minecraft.getMinecraft().thePlayer.ticksExisted % 10 == 0)
			setInfo(new String(RandomUtils.nextBytes(RandomUtils.nextInt(18, 22))));
	};
	
}
