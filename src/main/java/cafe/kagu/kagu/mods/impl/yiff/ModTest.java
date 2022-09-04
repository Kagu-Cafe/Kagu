/**
 * 
 */
package cafe.kagu.kagu.mods.impl.yiff;

import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.vecmath.Vector3d;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.impl.EventPacketSend;
import cafe.kagu.kagu.eventBus.impl.EventPlayerUpdate;
import cafe.kagu.kagu.eventBus.impl.EventRender3D;
import cafe.kagu.kagu.eventBus.impl.EventTick;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.mods.ModuleManager;
import cafe.kagu.kagu.settings.impl.BooleanSetting;
import cafe.kagu.kagu.settings.impl.DoubleSetting;
import cafe.kagu.kagu.settings.impl.IntegerSetting;
import cafe.kagu.kagu.settings.impl.KeybindSetting;
import cafe.kagu.kagu.settings.impl.ModeSetting;
import cafe.kagu.kagu.settings.impl.SlotSetting;
import cafe.kagu.kagu.utils.ChatUtils;
import cafe.kagu.kagu.utils.MathUtils;
import cafe.kagu.kagu.utils.MiscUtils;
import cafe.kagu.kagu.utils.MovementUtils;
import cafe.kagu.kagu.utils.PlayerUtils;
import cafe.kagu.kagu.utils.RotationUtils;
import cafe.kagu.kagu.utils.SpoofUtils;
import cafe.kagu.kagu.utils.UiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.util.MathHelper;

/**
 * @author lavaflowglow
 *
 */
public class ModTest extends Module {

	public ModTest() {
		super("Test", Category.EXPLOIT);
		slotSetting1.setInvalidSlots(slotSetting2);
		slotSetting2.setInvalidSlots(slotSetting1);
		setSettings(booleanSetting, decimalSetting1, decimalSetting2, decimalSetting3, modeSetting1, modeSetting2,
				modeSetting3, integerSetting1, integerSetting2, integerSetting3, keybindSetting, slotSetting1,
				slotSetting2);
	}

	private BooleanSetting booleanSetting = new BooleanSetting("Boolean setting", false);
	private DoubleSetting decimalSetting1 = new DoubleSetting("Decimal setting 1", 1, 0, 10, 0.25),
			decimalSetting2 = new DoubleSetting("Decimal setting 2", 1, -10, 10, 0.25),
			decimalSetting3 = new DoubleSetting("Decimal setting 3", 10, 10, 20, 0.25);
	private IntegerSetting integerSetting1 = new IntegerSetting("Integer setting 1", 1, 0, 10, 1),
			integerSetting2 = new IntegerSetting("Integer setting 2", 1, -10, 10, 1),
			integerSetting3 = new IntegerSetting("Integer setting 3", 10, 10, 20, 1);
	private ModeSetting modeSetting1 = new ModeSetting("Mode setting 1", "Test 1", "Test 1", "Test 2", "Test 3"),
			modeSetting2 = new ModeSetting("Mode setting 2", "Test 1", "Test 1"),
			modeSetting3 = new ModeSetting("Mode setting 3", "Test 1");
	private KeybindSetting keybindSetting = new KeybindSetting("Keybind", Keyboard.KEY_W);
	private SlotSetting slotSetting1 = new SlotSetting("Slot 1", 1), slotSetting2 = new SlotSetting("Slot 2", 2);

	private float[] lastRotations = new float[] { 0, 0 };
	private int ticks = 0;
	
	@Override
	public void onEnable() {
		EntityPlayerSP thePlayer = mc.thePlayer;
		lastRotations[0] = thePlayer.rotationYaw;
		lastRotations[1] = thePlayer.rotationPitch;
		ticks = 0;
	}

	@EventHandler
	private Handler<EventPlayerUpdate> onPlayerUpdate = e -> {
		if (e.isPost())
			return;
		if (mc.thePlayer.hurtResistantTime == 19) {
			mc.thePlayer.offsetPosition(1, 5.324, 1);
			e.setPosY(mc.thePlayer.posY);
//			MovementUtils.setMotion(2);
		}
	};
	
	@EventHandler
	private Handler<EventPacketSend> onPacketSend = e -> {
		if (e.isPost())
			return;
		if (e.getPacket() instanceof C00PacketKeepAlive) {
//			e.cancel();
		}
	};
	
}
