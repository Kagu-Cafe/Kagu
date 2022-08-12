/**
 * 
 */
package cafe.kagu.kagu.mods.impl.player;

import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.impl.EventPacketReceive;
import cafe.kagu.kagu.eventBus.impl.EventPacketSend;
import cafe.kagu.kagu.eventBus.impl.EventPlayerUpdate;
import cafe.kagu.kagu.eventBus.impl.EventTick;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.impl.BooleanSetting;
import cafe.kagu.kagu.settings.impl.ModeSetting;
import cafe.kagu.kagu.utils.ChatUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.util.BlockPos;

/**
 * @author lavaflowglow
 *
 */
public class ModDisabler extends Module {
	
	public ModDisabler() {
		super("Disabler", Category.PLAYER);
		setSettings(mode);
	}
	
	private ModeSetting mode = new ModeSetting("Mode", "S08 C04", "S08 C04", "C04 Connect", "Hypixel NoSlow", "Rapid Rotate", "Inverse Rapid Rotate", "Always Send Rotating", "Test");
	
	private boolean changeNextC06 = false;
	private float rapidRotation = 0;
	
	@Override
	public void onEnable() {
		changeNextC06 = false;
		rapidRotation = 0;
	}
	
	@EventHandler
	private Handler<EventTick> onTick = e -> {
		if (e.isPost())
			return;
		setInfo(mode.getMode());
		switch (mode.getMode()) {
			case "Always Send Rotating":{
				// Tricks mc into thinking that the player rotated and that it needs to send an update to the server
				mc.thePlayer.setLastReportedYaw(mc.thePlayer.getLastReportedYaw() + 1);
				mc.thePlayer.setLastReportedPitch(mc.thePlayer.getLastReportedPitch() + 1);
			}break;
		}
	};
	
	@EventHandler
	private Handler<EventPlayerUpdate> onPlayerUpdate = e -> {
		if (e.isPost())
			return;
		EntityPlayerSP thePlayer = mc.thePlayer;
		switch (mode.getMode()) {
			case "Hypixel NoSlow":{
//				if (thePlayer.isUsingItem() && thePlayer.ticksExisted % 5 == 0) {
//					thePlayer.setItemInUseObject(null);
//					thePlayer.setItemInUse(thePlayer.getCurrentEquippedItem(), 21);
//				}
				if (thePlayer.isUsingItem() && thePlayer.ticksExisted % 15 == 0) {
					mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
				}
			}break;
		}
	};
	
	@EventHandler
	private Handler<EventPacketReceive> onPacketReceive = e -> {
		if (e.isPost())
			return;
		
		switch (mode.getMode()) {
			case "S08 C04":{
				if (e.getPacket() instanceof S08PacketPlayerPosLook)
					changeNextC06 = true;
			}break;
			case "Hypixel NoSlow":{
//				if (!(e.getPacket() instanceof S09PacketHeldItemChange))
//					break;
//				S09PacketHeldItemChange s09 = (S09PacketHeldItemChange)e.getPacket();
//				ChatUtils.addChatMessage("S09");
//				if (s09.getHeldItemHotbarIndex() == mc.thePlayer.inventory.currentItem) {
//					ChatUtils.addChatMessage("Canceled");
//					e.cancel();
//				}
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
			case "Hypixel NoSlow":{
				if (e.getPacket() instanceof C08PacketPlayerBlockPlacement) {
					C08PacketPlayerBlockPlacement c08 = (C08PacketPlayerBlockPlacement)e.getPacket();
					Item item = c08.getStack().getItem();
					if (item instanceof ItemSword || item instanceof ItemFood || item instanceof ItemBow) {
						mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
					}
				}
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
				if (!(e.getPacket() instanceof C06PacketPlayerPosLook))
					break;
				C06PacketPlayerPosLook c06 = (C06PacketPlayerPosLook)e.getPacket();
				e.setPacket(new C04PacketPlayerPosition(c06.getPositionX(), c06.getPositionY(), c06.getPositionZ(), c06.isOnGround()));
			}break;
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
