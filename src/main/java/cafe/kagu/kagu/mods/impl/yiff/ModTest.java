/**
 * 
 */
package cafe.kagu.kagu.mods.impl.yiff;

import javax.vecmath.Vector3d;

import org.apache.commons.lang3.RandomUtils;

import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.impl.EventPlayerUpdate;
import cafe.kagu.kagu.eventBus.impl.EventTick;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.impl.BooleanSetting;
import cafe.kagu.kagu.settings.impl.DoubleSetting;
import cafe.kagu.kagu.settings.impl.IntegerSetting;
import cafe.kagu.kagu.settings.impl.LongSetting;
import cafe.kagu.kagu.settings.impl.ModeSetting;
import cafe.kagu.kagu.utils.MathUtils;
import cafe.kagu.kagu.utils.MiscUtils;
import cafe.kagu.kagu.utils.SpoofUtils;
import cafe.kagu.kagu.utils.UiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C18PacketSpectate;

/**
 * @author lavaflowglow
 *
 */
public class ModTest extends Module {
	
	public ModTest() {
		super("Test", Category.DEVELOPMENT);
		setSettings(booleanSetting, decimalSetting1, decimalSetting2, decimalSetting3, modeSetting1, modeSetting2, modeSetting3, integerSetting1, integerSetting2, integerSetting3, longSetting1, longSetting2, longSetting3);
	}
	
	@Override
	public void onEnable() {
		setInfo("test");
	}
	
	public static BooleanSetting booleanSetting = new BooleanSetting("Boolean setting", false);
	public static DoubleSetting decimalSetting1 = new DoubleSetting("Decimal setting 1", 1, 0, 10, 0.25), 
								 decimalSetting2 = new DoubleSetting("Decimal setting 2", 1, -10, 10, 0.25), 
								 decimalSetting3 = new DoubleSetting("Decimal setting 3", 10, 10, 20, 0.25);
	public static IntegerSetting integerSetting1 = new IntegerSetting("Integer setting 1", 1, 0, 10, 1), 
								 integerSetting2 = new IntegerSetting("Integer setting 2", 1, -10, 10, 1), 
								 integerSetting3 = new IntegerSetting("Integer setting 3", 10, 10, 20, 1);
	public static LongSetting longSetting1 = new LongSetting("Long setting 1", 1, 0, 10, 1), 
							  longSetting2 = new LongSetting("Long setting 2", 1, -10, 10, 1), 
							  longSetting3 = new LongSetting("Long setting 3", 10, 10, 20, 1);
	public static ModeSetting modeSetting1 = new ModeSetting("Mode setting 1", "Test 1", "Test 1", "Test 2", "Test 3"),
							  modeSetting2 = new ModeSetting("Mode setting 2", "Test 1", "Test 1"),
							  modeSetting3 = new ModeSetting("Mode setting 3", "Test 1");
	
	@EventHandler
	public Handler<EventTick> onTick = e -> {
		if (e.isPost())
			return;
	};
	
}
