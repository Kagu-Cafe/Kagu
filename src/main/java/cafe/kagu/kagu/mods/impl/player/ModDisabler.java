/**
 * 
 */
package cafe.kagu.kagu.mods.impl.player;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.impl.EventPacketReceive;
import cafe.kagu.kagu.eventBus.impl.EventPacketSend;
import cafe.kagu.kagu.eventBus.impl.EventPlayerUpdate;
import cafe.kagu.kagu.eventBus.impl.EventRender2D;
import cafe.kagu.kagu.eventBus.impl.EventTick;
import cafe.kagu.kagu.font.FontUtils;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.impl.BooleanSetting;
import cafe.kagu.kagu.settings.impl.ModeSetting;
import cafe.kagu.kagu.utils.ChatUtils;
import cafe.kagu.kagu.utils.MovementUtils;
import cafe.kagu.kagu.utils.TimerUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSword;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

/**
 * @author lavaflowglow
 *
 */
public class ModDisabler extends Module {
	
	public ModDisabler() {
		super("Disabler", Category.PLAYER);
		setSettings(mode);
	}
	
	private ModeSetting mode = new ModeSetting("Mode", "S08 C04", "S08 C04", "C04 Connect", "Rapid Rotate", "Inverse Rapid Rotate", "Always Send Rotating", "Test");
	
	private boolean changeNextC06 = false;
	private float rapidRotation = 0;
	
	private boolean syncedC06 = false;
	private Queue<Packet<?>> pingPackets = new LinkedList<>();
	private TimerUtil c03Timer = new TimerUtil();
	private int c0fsInQueue = 0;
	
	@Override
	public void onEnable() {
		changeNextC06 = false;
		rapidRotation = 0;
		syncedC06 = false;
		pingPackets.clear();
		c03Timer.reset();
		c0fsInQueue = 0;
	}
	
	@EventHandler
	private Handler<EventTick> onTick = e -> {
		if (!mode.is("Test"))
			setInfo(mode.getMode());
		EntityPlayerSP thePlayer = mc.thePlayer;
		switch (mode.getMode()) {
			case "Always Send Rotating":{
				if (e.isPost())
					return;
				// Tricks mc into thinking that the player rotated and that it needs to send an update to the server
				mc.thePlayer.setLastReportedYaw(mc.thePlayer.getLastReportedYaw() + 1);
				mc.thePlayer.setLastReportedPitch(mc.thePlayer.getLastReportedPitch() + 1);
			}break;
			case "Test":{
				if (thePlayer.ticksExisted == 0) {
					syncedC06 = false;
					setInfo("Please Wait...");
					pingPackets.clear();
				}
				if (e.isPost()) {
					if (pingPackets.size() > 0) {
						setInfo("Made watchdog our bitch");
						syncedC06 = true;
					}
					NetworkManager networkManager = mc.getNetHandler().getNetworkManager();
					pingPackets.iterator().forEachRemaining(networkManager::sendPacketNoEvent);
					pingPackets.clear();
					c0fsInQueue = 0;
//					if (syncedC06)
//						ChatUtils.addChatMessage("Cum");
				}
			}break;
		}
	};
	
	@EventHandler
	private Handler<EventPlayerUpdate> onPlayerUpdate = e -> {
		EntityPlayerSP thePlayer = mc.thePlayer;
		switch (mode.getMode()) {
			case "Test":{
//				if (!(thePlayer.isUsingItem() || thePlayer.isBlocking()))
//					return;
//				if (e.isPre()) {
//					mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
//				}else {
//					mc.getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f));
//				}
			}break;
		}
	};
	
	@EventHandler
	private Handler<EventPacketReceive> onPacketReceive = e -> {
		if (e.isPost())
			return;
		
		EntityPlayerSP thePlayer = mc.thePlayer;
		switch (mode.getMode()) {
			case "S08 C04":{
				if (e.getPacket() instanceof S08PacketPlayerPosLook)
					changeNextC06 = true;
			}break;
			case "Test":{
				
			}break;
		}
	};
	
	@EventHandler
	private Handler<EventPacketSend> onPacketSend = e -> {
		if (e.isPost())
			return;
		
		EntityPlayerSP thePlayer = mc.thePlayer;
		
		switch (mode.getMode()) {
			case "S08 C04":{
				if (!changeNextC06 || !(e.getPacket() instanceof C06PacketPlayerPosLook))
					break;
				C06PacketPlayerPosLook c06 = (C06PacketPlayerPosLook)e.getPacket();
				e.setPacket(new C04PacketPlayerPosition(c06.getPositionX(), c06.getPositionY(), c06.getPositionZ(), c06.isOnGround()));
			}break;
			case "C04 Connect":{
				if (thePlayer.ticksExisted > 0 || !(e.getPacket() instanceof C06PacketPlayerPosLook))
					break;
				C06PacketPlayerPosLook c06 = (C06PacketPlayerPosLook)e.getPacket();
				e.setPacket(new C04PacketPlayerPosition(c06.getPositionX(), c06.getPositionY(), c06.getPositionZ(), c06.isOnGround()));
			}break;
			case "Rapid Rotate":
			case "Inverse Rapid Rotate":{
				if (e.getPacket() instanceof C03PacketPlayer) {
					C03PacketPlayer c03 = (C03PacketPlayer)e.getPacket();
					if (c03.isRotating())
						return;
					// If c03 is c04
					if (c03 instanceof C04PacketPlayerPosition) {
						C04PacketPlayerPosition c04 = (C04PacketPlayerPosition)c03;
						e.setPacket(new C06PacketPlayerPosLook(c04.getPositionX(), c04.getPositionY(), c04.getPositionZ(), mc.thePlayer.getLastReportedYaw(), mc.thePlayer.getLastReportedPitch(), c03.isOnGround()));
					}
					
					// Else could only be normal c03 because c05 & c06 both have rotating set to true
					else {
						e.setPacket(new C05PacketPlayerLook(mc.thePlayer.getLastReportedYaw(), mc.thePlayer.getLastReportedPitch(), c03.isOnGround()));
					}
				}
			}break;
			case "Test":{
				if (e.getPacket() instanceof C0FPacketConfirmTransaction) {
					C0FPacketConfirmTransaction c0f = (C0FPacketConfirmTransaction)e.getPacket();
					if (c0f.getUid() > 0 || c0f.getWindowId() != 0)
						return;
//					ChatUtils.addChatMessage(c0f.getUid());
					if (pingPackets.offer(c0f)) {
						c0fsInQueue++;
						e.cancel();
					}
				}
				else if (e.getPacket() instanceof C00PacketKeepAlive) {
					if (syncedC06)
						if (pingPackets.offer(e.getPacket()))
							e.cancel();
				}
				else if (e.getPacket() instanceof C03PacketPlayer)
					if (!syncedC06 && thePlayer.ticksExisted < 60)
						e.cancel();
			}break;
		}
	};
	
	@EventHandler
	private Handler<EventRender2D> onRender2D = e -> {
		if (e.isPost())
			return;
		if (mode.is("Test") && !syncedC06) {
			ScaledResolution sr = new ScaledResolution(mc);
			FontUtils.ROBOTO_REGULAR_10.drawCenteredString("Desyncing (" + (mc.thePlayer.ticksExisted < 60 ? "1" : "2") + "/2)...", sr.getScaledWidth() / 2, sr.getScaledHeight() * 0.75, 0xffff0000);
		}
	};
	
	/**
	 * @return the mode
	 */
	public ModeSetting getMode() {
		return mode;
	}
	
	/**
	 * @return the rapidRotation
	 */
	public float getRapidRotation() {
		if (mode.is("Inverse Rapid Rotate"))
			rapidRotation--;
		else
			rapidRotation++;
		return rapidRotation;
	}
	
}
