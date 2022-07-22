/**
 * 
 */
package cafe.kagu.kagu.mods.impl.move;

import cafe.kagu.kagu.eventBus.EventHandler;
import cafe.kagu.kagu.eventBus.Handler;
import cafe.kagu.kagu.eventBus.impl.EventPlayerUpdate;
import cafe.kagu.kagu.mods.Module;
import cafe.kagu.kagu.settings.impl.ModeSetting;
import cafe.kagu.kagu.utils.ChatUtils;
import cafe.kagu.kagu.utils.MovementUtils;
import cafe.kagu.kagu.utils.RotationUtils;
import cafe.kagu.kagu.utils.SpoofUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.client.C03PacketPlayer;

/**
 * @author lavaflowglow
 *
 */
public class ModFly extends Module {
	
	public ModFly() {
		super("Fly", Category.MOVEMENT);
		setSettings(mode);
	}
	
	private ModeSetting mode = new ModeSetting("Mode", "Verus", "Verus");
	
	@Override
	public void onEnable() {
		switch (mode.getMode()) {
			case "Verus":{
				if (MovementUtils.isTrueOnGround(-2) || MovementUtils.isTrueOnGround(-3.5)) {
					toggle();
					ChatUtils.addChatMessage("Error: Make sure you have at least 3.5 blocks of space above you");
					return;
				}
				NetworkManager networkManager = mc.getNetHandler().getNetworkManager();
				EntityPlayerSP thePlayer = mc.thePlayer;
				verusFlyTicks = 0;
				verusDamage = false;
				networkManager.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(thePlayer.posX, thePlayer.posY + 3.0001, thePlayer.posZ, false));
				networkManager.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(thePlayer.posX, thePlayer.posY, thePlayer.posZ, false));
				networkManager.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(thePlayer.posX, thePlayer.posY, thePlayer.posZ, true));
			}break;
		}
	}
	
	private int verusFlyTicks = 0;
	private double verusMotion = 0;
	private boolean verusDamage = false;
	
	@EventHandler
	private Handler<EventPlayerUpdate> onPlayerUpdate = e -> {
		if (e.isPost())
			return;
		
		EntityPlayerSP thePlayer = mc.thePlayer;
		
		switch (mode.getMode()) {
			case "Verus":{
				mc.thePlayer.setSprinting(true);
				if (thePlayer.hurtTime > 0 && !verusDamage) {
					verusFlyTicks = 22;
					verusMotion = 7.5;
					thePlayer.setPosition(thePlayer.posX, thePlayer.posY + 3, thePlayer.posZ);
					verusDamage = true;
				}
				
				if (verusDamage) {
					if (verusFlyTicks > 0) {
						if (MovementUtils.isPlayerMoving())
							MovementUtils.setMotion(verusMotion, thePlayer.rotationYaw);
						verusMotion *= 0.91;
						thePlayer.motionY = -0.0784000015258789;
						thePlayer.onGround = true;
						verusFlyTicks--;
						if (thePlayer.isCollidedHorizontally)
							toggle();
					}else {
						MovementUtils.setMotion(0);
						toggle();
					}
					if (MovementUtils.isTrueOnGround()) {
						toggle();
					}
				}
			}break;
			case "AirHop":{
				
			}break;
		}
		
	};
	
}
