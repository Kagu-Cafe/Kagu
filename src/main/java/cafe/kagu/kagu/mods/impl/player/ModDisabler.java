/**
 * 
 */
package cafe.kagu.kagu.mods.impl.player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C12PacketUpdateSign;
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
import net.minecraft.util.IChatComponent;

/**
 * @author lavaflowglow
 *
 */
public class ModDisabler extends Module {
	
	public ModDisabler() {
		super("Disabler", Category.PLAYER);
		setSettings(mode);
	}
	
	private ModeSetting mode = new ModeSetting("Mode", "S08 C04", "S08 C04", "C04 Connect", "Rapid Rotate", "Inverse Rapid Rotate", "Always Send Rotating", "Hypixel Strafe", "Test");
	
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
		if (!mode.is("Hypixel Strafe"))
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
			case "Hypixel Strafe":{
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
					if (c0fsInQueue < 8)
						return;
					NetworkManager networkManager = mc.getNetHandler().getNetworkManager();
//					pingPackets.iterator().forEachRemaining(networkManager::sendPacketNoEvent);
					List<Packet> removePackets = new ArrayList();
					pingPackets.iterator().forEachRemaining(p -> {
						if (p instanceof C00PacketKeepAlive && (pingPackets.size() - removePackets.size()) - c0fsInQueue > 15) {
							networkManager.sendPacketNoEvent(p);
							removePackets.add(p);
						}
						if (p instanceof C0FPacketConfirmTransaction) {
							c0fsInQueue--;
							networkManager.sendPacketNoEvent(p);
							removePackets.add(p);
						}
					});
//					pingPackets.clear();
					pingPackets.removeAll(removePackets);
//					c0fsInQueue = 0;
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
			case "Hypixel Strafe":{
				if (!(thePlayer.isUsingItem() || thePlayer.isBlocking()) || thePlayer.ticksExisted % 3 != 0)
					return;
				if (e.isPre()) {
//					ChatUtils.addChatMessage("test1");
//					mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C12PacketUpdateSign(BlockPos.ORIGIN, new IChatComponent[0]));
					if (thePlayer.isSprinting()) {
//						mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C0BPacketEntityAction(thePlayer, net.minecraft.network.play.client.C0BPacketEntityAction.Action.STOP_SPRINTING));
					}
//					mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
//					mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C09PacketHeldItemChange(thePlayer.inventory.currentItem == 0 ? 1 : 0));
//					mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C09PacketHeldItemChange(thePlayer.inventory.currentItem));
//					mc.getNetHandler().getNetworkManager().sendPacket(
//							new C08PacketPlayerBlockPlacement(BlockPos.ORIGIN, 255, thePlayer.inventory.getStackInSlot(thePlayer.inventory.currentItem == 0 ? 1 : 0), 0, 0, 0));
//					ChatUtils.addChatMessage("Bypass");
//					mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C0EPacketClickWindow(0, thePlayer.inventory.currentItem, 40, 2, null, thePlayer.openContainer.getNextTransactionID(thePlayer.inventory)));
//					mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C0EPacketClickWindow(0, -999, 0, 0, thePlayer.getCurrentEquippedItem(), thePlayer.openContainer.getNextTransactionID(thePlayer.inventory)));
//					mc.getNetHandler().getNetworkManager().sendPacket(
//							new C08PacketPlayerBlockPlacement(thePlayer.getCurrentEquippedItem()));
				}else {
//					mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
//					mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C09PacketHeldItemChange(thePlayer.inventory.currentItem)); // Bypasses food and bow noslow check
//					mc.getNetHandler().getNetworkManager().sendPacket(
//							new C08PacketPlayerBlockPlacement(BlockPos.ORIGIN, -1, null, 0, 0, 0));
//					mc.getNetHandler().getNetworkManager().sendPacket(
//							new C08PacketPlayerBlockPlacement(BlockPos.ORIGIN, 255, thePlayer.inventory.getStackInSlot(thePlayer.inventory.currentItem == 0 ? 1 : 0), 0, 0, 0));
//					for (int i = 0; i < 3; i++)
//						mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer(false));
//					mc.getNetHandler().getNetworkManager().sendPacket(
//							new C08PacketPlayerBlockPlacement(thePlayer.getCurrentEquippedItem()));
					if (thePlayer.isSprinting()) {
//						mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C0BPacketEntityAction(thePlayer, net.minecraft.network.play.client.C0BPacketEntityAction.Action.START_SPRINTING));
					}
//					ChatUtils.addChatMessage("C03");
				}
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
			case "Hypixel Strafe":{
				
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
			case "Hypixel Strafe":{
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
				else if (e.getPacket() instanceof C03PacketPlayer) {
					if (!syncedC06 && thePlayer.ticksExisted < 60)
						e.cancel();
				}
				else if (e.getPacket() instanceof C08PacketPlayerBlockPlacement) {
					C08PacketPlayerBlockPlacement c08 = (C08PacketPlayerBlockPlacement)e.getPacket();
					if (c08.getPlacedBlockDirection() == 255) {
//						mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C08PacketPlayerBlockPlacement(
//								thePlayer.getPosition().add(0, -1, 0), 1, thePlayer.getCurrentEquippedItem(), 0, 0, 0));
//						e.cancel();
//						mc.getNetHandler().getNetworkManager().sendPacketNoEvent(e.getPacket());
//						mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, thePlayer.getPosition().add(0, -1, 0), EnumFacing.UP));
//						mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, thePlayer.getPosition().add(0, -1, 0), EnumFacing.UP));
						ChatUtils.addChatMessage("Cum");
					}
				}
			}break;
			case "Test":{
				if (e.getPacket() instanceof C03PacketPlayer) {
					C03PacketPlayer c03 = (C03PacketPlayer)e.getPacket();
					if (!c03.isMoving() && !c03.isRotating())
						e.cancel();
				}
				else if (e.getPacket() instanceof C00PacketKeepAlive || e.getPacket() instanceof C0FPacketConfirmTransaction) {
					ChatUtils.addChatMessage(e.getPacket());
					e.cancel();
				}
			}break;
		}
	};
	
	@EventHandler
	private Handler<EventRender2D> onRender2D = e -> {
		if (e.isPost())
			return;
		if (mode.is("Hypixel Strafe") && !syncedC06) {
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
